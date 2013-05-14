package srpg.map.ui;

import game.Draw;
import game.Plane;
import game.Sprite;

import java.awt.Color;
import java.awt.Graphics;

import srpg.SRPGGameObjectManager;
import srpg.map.MapObject;
import srpg.map.StageDrawOrder;
import srpg.map.StageMap;

public class TurnCounter extends MapObject {
	
	int count, denom;

	public TurnCounter(SRPGGameObjectManager gom, Sprite sp, int plnNo, int x, int y, StageMap m) {
		super(gom, sp, plnNo, x, y, m);
		
		sp.setPos(plnNo, gom.width - 120, 50);
		sp.setPos(plnNo, StageDrawOrder.INFO.z());
		sp.setDraw(plnNo, new Draw() {

			public void drawing(Graphics g, Plane pln) {
				g.setColor(Color.BLACK);
				Sprite.drawShadedString("Time : " + count / 100f + " / " + (denom == 0 ? "Åá" : denom), pln.coord.x.intValue(), pln.coord.y.intValue(), g);
				
			}
			
		});
	}
	
	public void setCount(int count) {
		this.count = count;
	}
	
	public void setDenom(int denom) {
		this.denom = denom;
	}
}
