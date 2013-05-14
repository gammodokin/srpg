package srpg.map.ui;

import game.Draw;
import game.Plane;
import game.Sprite;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import srpg.Damage;
import srpg.SRPGGameObjectManager;
import srpg.map.MapUserIF;
import srpg.map.StageDrawOrder;
import srpg.map.StageMap;

public class ShowDamage extends MapUserIF {
	
	int damage;
	String str = "";
	boolean hit;
	Color c;
	
	double topX, topX2;
	double topY;
	
	Random rn;

	public ShowDamage(SRPGGameObjectManager gom, Sprite sp, int plnNo, int x,
			int y, StageMap m) {
		super(gom, sp, plnNo, x, y, m);
		
		sp.setPos(plnNo, StageDrawOrder.EFFECT.z());
		
		// ‰æ–ÊŠO‚É
		sp.setPos(plnNo, -100, -100);
		
		sp.setDraw(plnNo, new Draw() {

			public void drawing(Graphics g, Plane pln) {
				g.setColor(c);
				Sprite.drawShadedString(str, pln.coord.x.intValue(), pln.coord.y.intValue(), Color.DARK_GRAY, g);
			}
		});
		
		rn = new Random();
		
		topX = (rn.nextDouble() - 0.5) * map.hexW/2;
		if(Math.abs(topX) < 0.1)
			topX = Math.signum(topX) * 0.1;
		topX2 = Math.pow(topX, 2);
		topY = (rn.nextDouble() * 2/3 + 1/3f) * map.hexHeight * 4;
		
	}
	
	@Override
	public void destructor() {
		map.unitMap[y][x].damaging();
		super.destructor();
	}
	
	public void setDamage(Damage d, boolean hit) {
		this.hit = hit;
		if(!hit) {
			 c = Color.WHITE;
			 str += "miss";
			 damage = -1;
			 sp.playSE(Sprite.MISS);
			 return;
		}
		
		if(d.hp > 0 || d.sp > 0)
			sp.playSE(Sprite.DAMAGE);
		else if(d.hp < 0 || d.sp < 0)
			sp.playSE(Sprite.SUPPORT);
		
		if(d.sp != 0) {
			c = new Color(120, 180, 240);
			str += Math.abs(d.sp);
			damage = d.sp;
		} else if(d.hp >= 0) {
			c = Color.WHITE;
			str += d.hp;
			damage = d.hp;
		}else {
			c = new Color(110, 210, 140);
			str += -d.hp;
			damage = d.hp;
		}
	}

	int count = 0;
	int countMax = 10;
	@Override
	public void action() {
		sp.setPos(plnNo, map.getHexCoord(x, y));
		sp.setMov(plnNo, map.hexW/2 - str.length() * 3, map.hexH/2 - map.hexHeight * 2);
		
		double x, y;
		if(damage >= 0) {
			x = topX * 5/2 * count / countMax;
			y = topY/topX2 * Math.pow((x - topX), 2) - topY;// - yRate * x;
		}else {
			x = 0;
			y = -map.hexHeight * 3.5 * count / countMax;
		}
		sp.setMov(plnNo, x, y);
		
		count++;
		
		if(count > countMax)
			destructor();
	}
	
}
