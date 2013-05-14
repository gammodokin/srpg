package srpg.map.ui.menu;

import game.Sprite;
import srpg.SRPGGameObjectManager;
import srpg.map.StageMap;

public class ItemMenu extends ActionMenu {

	public ItemMenu(SRPGGameObjectManager gom, Sprite sp, int plnNo, int x, int y, StageMap m) {
		super(gom, sp, plnNo, x, y, m);
		
		init(map.current.getStatus().capa.item, IMCursor.class);
	}
	
	protected class IMCursor extends AMCursor {

		public IMCursor(SRPGGameObjectManager gom, Sprite sp, int plnNo, int x,
				int y, StageMap m) {
			super(gom, sp, plnNo, x, y, m);
		}
		
	}
}
