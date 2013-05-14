package srpg.map;

import game.ZOrder;

public enum StageDrawOrder implements ZOrder{
	MAP_OBJECT, EFFECT, INFO, MENU, MENU_CURSOR;
	
	public double z() {
		return ordinal();
	}
}
