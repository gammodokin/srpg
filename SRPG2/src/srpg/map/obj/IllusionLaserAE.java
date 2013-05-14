package srpg.map.obj;

import game.Sprite;
import srpg.SRPGGameObjectManager;
import srpg.map.StageMap;

public class IllusionLaserAE extends NormalAE {

	public IllusionLaserAE(SRPGGameObjectManager gom, Sprite sp, int plnNo,
			int x, int y, StageMap m) {
		super(gom, sp, plnNo, x, y, m);
		
		initBullet("tama4", 9, 22, Sprite.LASER_S);
		
//		sp.playSE(Sprite.LASER_S);
	}
	
	@Override
	public void action() {
		super.action();
	}

}
