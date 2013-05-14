package srpg.map;

import srpg.SRPGGameObjectManager;
import game.Sprite;
import game.UserIF;

public abstract class MapUserIF extends UserIF {
	
	protected final SRPGGameObjectManager gom;
	
	protected final StageMap map;
	
	public MapUserIF(SRPGGameObjectManager gom, Sprite sp, int plnNo, int x, int y, StageMap m) {
		super(gom, sp, plnNo, x, y);
		
		this.gom = gom;
		
		this.map = m;
	}
	
}
