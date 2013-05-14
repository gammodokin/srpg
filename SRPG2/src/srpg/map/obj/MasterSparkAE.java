package srpg.map.obj;

import myutil.Coord;
import game.Sprite;
import srpg.SRPGGameObjectManager;
import srpg.map.StageMap;

public class MasterSparkAE extends NormalAE {

	private enum Phase{CHARGE, SHOOT}
	
	Phase phase = Phase.CHARGE;
	
	public MasterSparkAE(SRPGGameObjectManager gom, Sprite sp, int plnNo,
			int x, int y, StageMap m) {
		super(gom, sp, plnNo, x, y, m);
		
		sp.setAnimeImg(plnNo, "animation_mcf003_004a", 5);//"animation_star001", 5);
		sp.setTrailCount(plnNo, 1);
		
		Coord<Integer> size = sp.getImgSize(plnNo);
		sp.setMov(plnNo, -size.x/2 - 28, -size.y/2 - 28);
		
		sp.playSE(Sprite.CHARGE_M);
	}

	@Override
	public void action() {
		switch(phase) {
		case CHARGE:
			if(sp.isAnimeClosed(plnNo)) {
				phase = Phase.SHOOT;
				
				sp.initImgs(plnNo);
				initBullet("tama5", 1, 33, Sprite.LASER_M);
				
				
//				sp.playSE(Sprite.LASER_M);
			}
			break;
		case SHOOT:
			super.action();
			break;
		}
	}
	
}
