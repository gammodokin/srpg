package srpg.map.obj;

import myutil.Coord;
import game.Sprite;
import srpg.SRPGGameObjectManager;
import srpg.map.StageMap;

public class MusoufuuinAE extends AttackEffect {

	private enum Phase{CHARGE, EXPLODE}
	
	Phase phase = Phase.CHARGE;
	
	public MusoufuuinAE(SRPGGameObjectManager gom, Sprite sp, int plnNo, int xx, int yy, StageMap m) {
		super(gom, sp, plnNo, xx, yy, m);

//		sp.setDraw(plnNo, new Draw() {

//			public void drawing(Graphics g, Plane pln) {
//
//				g.setColor(new Color(0xff, 0xff, 0xee, 0xff * (countMax - count) / countMax));
//				int sizeW = map.hexW * (map.current.getCurrentAction().area.max + 1) * count / countMax;
//				int sizeH = (int)Math.round(sizeW * map.depthRate);
//				g.fillOval(pln.coord.x.intValue() - sizeW, pln.coord.y.intValue() - sizeH, sizeW * 2, sizeH * 2);
//
//				g.setColor(Color.BLACK);
//			}
//		});

//		target = map.getHexCoord(x, y);
//		setHexCenter(target);
//		sp.setPos(plnNo, target);
//		size = sp.getImgSize(plnNo);
//		sp.setMov(plnNo, -size.x/2, -size.y/2);

		sp.setAnimeImg(plnNo, "animation_explosion010rot", 4);
		sp.setTrailCount(plnNo, 1);
		
		sp.setPos(plnNo, map.getHexCoord(map.current.getPos()));
		size = sp.getImgSize(plnNo);
		sp.setMov(plnNo, -size.x/2 + 25, -size.y/2);
		
		sp.playSE(Sprite.CHARGE_S);
	}

	Coord<Integer> size;

	@Override
	public void action() {
		switch(phase) {
		case CHARGE:
			if(sp.isAnimeClosed(plnNo)) {
				phase = Phase.EXPLODE;
				
				sp.initImgs(plnNo);
				
				String file = "explosion1";//"cure";
				sp.setAnimeImg(plnNo, file, 5);
				//sp.setAnimeImg(plnNo, "animation_explosion006", 2);	
				
				sp.setTrailCount(plnNo, 1);
				
				target = map.getHexCoord(x, y);
//				setHexCenter(target);
				sp.setPos(plnNo, target);
				sp.setMov(plnNo, -size.x/2 - 80, -size.y/2 - 85);
				
				sp.playSE(Sprite.EXPLODE_M);
			}
			break;
		case EXPLODE:
			target = map.getHexCoord(x, y);
	//		setHexCenter(target);
			sp.setPos(plnNo, target);
			sp.setMov(plnNo, -size.x/2 - 80, -size.y/2 - 85);

			if(sp.isAnimeClosed(plnNo)) {
				destructor();
				damage();
			}
			break;
		}
//		count++;
//		if(count >= countMax) {
//			destructor();
//			damage();
//		}
	}

}
