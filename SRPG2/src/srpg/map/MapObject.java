package srpg.map;

import srpg.SRPGGameObjectManager;
import game.GameObject;
import game.Sprite;

public class MapObject extends GameObject {
	
	protected final SRPGGameObjectManager gom;
	
	protected final StageMap map;

	public MapObject(SRPGGameObjectManager gom, Sprite sp, int plnNo, int x, int y, StageMap m) {
		super(gom, sp, plnNo, x, y);
		
		this.gom = gom;
		
		map = m;
	}
	
}
