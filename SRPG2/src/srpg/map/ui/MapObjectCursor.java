package srpg.map.ui;

import game.Sprite;
import srpg.SRPGGameObjectManager;
import srpg.map.StageMap;
import srpg.screen.Cursor;

public abstract class MapObjectCursor extends Cursor {
	
	protected final SRPGGameObjectManager gom;
	protected final StageMap map;

	public MapObjectCursor(SRPGGameObjectManager gom, Sprite sp, int plnNo, int x,
			int y, StageMap m) {
		super(gom, sp, plnNo, x, y);
		
		this.gom = gom;
		map = m;
	}
	
}
