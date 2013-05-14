package srpg.map.obj;

import game.Sprite;
import srpg.SRPGGameObjectManager;
import srpg.map.StageMap;
import srpg.map.UnitType;
import srpg.map.ui.menu.CommandMenu;

public class Friend extends Unit {

	public Friend(SRPGGameObjectManager gom, Sprite sp, int plnNo, int x, int y, StageMap m) {
		super(gom, sp, plnNo, x, y, m, UnitType.FRIEND);
		operator = CommandMenu.class;
	}

}
