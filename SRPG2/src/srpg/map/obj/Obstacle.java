package srpg.map.obj;

import game.Motionable;
import game.Sprite;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import myutil.Coord;
import srpg.SRPGGameObjectManager;
import srpg.map.MapDrawOrder;
import srpg.map.MapObject;
import srpg.map.StageMap;

public class Obstacle extends MapObject implements Motionable{

	final int height;

	BufferedImage img;

	public Obstacle(SRPGGameObjectManager gom, Sprite sp, int plnNo, final int x,
			final int y, StageMap m) {
		super(gom, sp, plnNo, x, y, m);

		String str = "tree";
		sp.addGrp(str);
		img = sp.getGrp(str);

		initImg();
		drawImg();

		sp.setPos(plnNo, map.getHexCoord(x, y));
		sp.setPos(plnNo, MapDrawOrder.UNIT.z() + y * StageMap.YZRate);

		height = map.hexH / 3;
//		sp.setDraw(plnNo, new Draw() {
//
//			public void drawing(Graphics g, Plane pln) {
//				if(pln.img == null) {
//					if(pln.img == null)
//						pln.initImage(map.hexW, map.hexH * 2);
//					Graphics gbg = pln.getGraphics();
//					gbg.drawImage(img, 0, 0, null);
//					gbg.dispose();
//				}
//			}
//
//		});

		gom.addMotionable(this);
	}

	@Override
	public void destructor() {
		gom.removeMotionable(this);
		super.destructor();
	}


	public void motion() {
		Coord<Double> c = map.getHexCoord(x, y);
		sp.setPos(plnNo, c.x.intValue(), c.y.intValue() - map.hexH * 2/3 - 3);
	}

	public int processPriority() {
		return 0;
	}

	private void initImg() {
		sp.initImg(plnNo, map.hexW, map.hexH * 2);
	}

	private void drawImg() {
		Graphics g = sp.createGraphics(plnNo);
		g.drawImage(img, 0, 0, null);
		g.dispose();
	}
}
