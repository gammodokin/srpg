package srpg.map.obj;

import java.awt.Color;
import java.awt.Graphics;

import game.Draw;
import game.Motionable;
import game.Plane;
import game.Sprite;
import srpg.SRPGGameObjectManager;
import srpg.UnitStatus;
import srpg.map.MapDrawOrder;
import srpg.map.MapObject;
import srpg.map.StageMap;

public class Gage extends MapObject implements Motionable{

	Unit unit;

	public Gage(SRPGGameObjectManager gom, Sprite sp, int plnNo, int x, int y, StageMap m) {
		super(gom, sp, plnNo, x, y, m);

		sp.setDraw(plnNo, new Draw() {

			public void drawing(Graphics g, Plane pln) {
				drawGages(g, pln.coord.x, pln.coord.y);
			}

		});

		unit = map.unitMap[y][x];

		gom.addMotionable(this);

		//motion();
	}

	@Override
	public void destructor() {
		gom.removeMotionable(this);
		super.destructor();
	}

	public void motion() {
		sp.setPos(plnNo, map.getHexCoord(unit.getPos()));
		sp.setMov(plnNo, 0, -map.hexH * 3/4);
		sp.setPos(plnNo, MapDrawOrder.UNIT.z() + unit.getPos().y * StageMap.YZRate);
	}

	public int processPriority() {
		return 0;
	}

	Color hpc = new Color(0xff, 0xaa, 0x88, 0xcc), spc = new Color(0x88, 0xaa, 0xff, 0xcc),
		tpc = new Color(0xaa, 0xff, 0x88, 0xcc), tpMaxc = new Color(0xdd, 0xff, 0x88, 0xcc);
	protected void drawGages(Graphics g, double x, double y) {
		int h = 4;
		UnitStatus status = unit.getStatus();
		drawGage(g, x, y, h, hpc, status.maxHP, status.hp);
		drawGage(g, x, y + h, h, spc, status.maxSP, status.sp);
		drawGage(g, x, y + h*2, h, status.tension < 1 ? tpc : tpMaxc, 100, (int)(status.tension * 100));
	}

	protected void drawGage(Graphics g, double x, double y, int height, Color c, int max, int p) {
		g.setColor(c);
		int width = map.hexW - 4;
		int posX = (int)x + 2, posY = (int)y - map.hexH * 2 + map.hexH + 14;//map.hexH/2 - figure.getHeight() - 7;
		g.fillRect(posX, posY, width * p / max, height);

		g.setColor(Color.BLACK);
		g.drawRect(posX, posY, width, height);
	}

}
