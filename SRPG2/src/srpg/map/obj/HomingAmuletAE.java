package srpg.map.obj;

import game.Sprite;
import myutil.Coord;
import srpg.SRPGGameObjectManager;
import srpg.map.StageMap;

public class HomingAmuletAE extends NormalAE {

//	private Coord<Double> before, after;
//
//	private double dir;
//	private double cos, sin;
//
//	private int count = 0;
//	private int countMax = 6;
	
//	BufferedImage img;

	private enum Phase{BULLET, EXPLODE}
	
	Phase phase = Phase.BULLET;
	
	public HomingAmuletAE(SRPGGameObjectManager gom, Sprite sp, int plnNo, int x, int y, StageMap m) {
		super(gom, sp, plnNo, x, y, m);
//
//		String file = "tama2";
//		sp.addGrp(file);
//		img = sp.getGrp(file);
//		
//		sp.setDraw(plnNo, new Draw() {
//
//			int size = 16;
//			public void drawing(Graphics g, Plane pln) {
//				if(count == 0) {
//					g.drawImage(img, pln.coord.x.intValue() - size/2, pln.coord.y.intValue() - size, null);
//				} else {
//					g.setColor(new Color(0xff, 0xff, 0xee, 0xff * (countMax - count) / countMax));
//					int sizeW = map.hexW * (map.current.getCurrentAction().area.max + 1) * count / countMax;
//					int sizeH = (int)Math.round(sizeW * map.depthRate);
//					g.fillOval(pln.coord.x.intValue() - sizeW, pln.coord.y.intValue() - sizeH, sizeW * 2, sizeH * 2);
//				}
//				g.setColor(Color.BLACK);
//			}
//
//		});
//
//		before = map.getHexCoord(map.current.getPos());
//		setHexCenter(before);
//		sp.setPos(plnNo, before);
//
//		//setHexCenter(target);
//		dir = Math.atan2(target.y - before.y, target.x - before.x);
//		cos = Math.cos(dir);
//		sin = Math.sin(dir);
//
		initBullet("tama4", 3, 15, Sprite.SHOOT);
//		sp.playSE(Sprite.SHOOT);
	}

	@Override
	public void action() {
		switch(phase) {
		case BULLET:
			if(bulletAction()) {
				phase = Phase.EXPLODE;
				
				sp.initImgs(plnNo);
				sp.setAnimeImg(plnNo, "animation_explosion001", 3);
//				sp.setTrailCount(plnNo, 1);
				
				Coord<Integer> size = sp.getImgSize(plnNo);
				sp.setPos(plnNo, target);
				sp.setMov(plnNo, -size.x/2, -size.y/2 - 20);
				
				sp.playSE(Sprite.EXPLODE_S);
			}
			break;
		case EXPLODE:
			if(sp.isAnimeClosed(plnNo)) {
				destructor();
				damage();
			}
			break;
		}
	}

//	private int speed = 15;
//	@Override
//	public void action() {
//		current = sp.getPos(plnNo);
//		if((cos > 0 && current.x > target.x) ||
//				(cos < 0 && current.x < target.x) ||
//				(sin > 0 && current.y > target.y) ||
//				(sin < 0 && current.y < target.y)) {
//			if(count == 0)
//				sp.playSE(Sprite.EXPLODE_S);
//			
//			count++;
//			
//			if(count >= countMax) {
//				destructor();
//				damage();
//			}
//			//map.unitMap[targetCood.y][targetCood.x].damaged(power);
//			return;
//		}
//
//		target = map.getHexCoord(x, y);
//		setHexCenter(target);
//
//		after = map.getHexCoord(map.current.getPos().x, map.current.getPos().y);
//		setHexCenter(after);
//		Coord<Double> difference = new Coord<Double>(after.x - before.x, after.y - before.y);
//
//		sp.setMov(plnNo, difference.x + speed * cos, difference.y + speed * sin);
//		before.assign(after);
//	}

}
