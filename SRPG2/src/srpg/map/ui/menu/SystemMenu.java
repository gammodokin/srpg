package srpg.map.ui.menu;

import game.Sprite;
import srpg.SRPGGameObjectManager;
import srpg.map.StageMap;

public class SystemMenu extends MapObjectMenu {
	enum Item{
		SwitchBGM {
			@Override
			public String toString() {return "BGM ON/OFF";}
		}, 
		QuiteGame {
			@Override
			public String toString() {return "Quite Game";}
		},
		ChangePaintMode {
			@Override
			public String toString() {return "Change PaintMode";}
		}, 
		ViewTrace {
			@Override
			public String toString() {return "ViewTrace ON/OFF";}
		}, 
		Cancel, }

	public SystemMenu(SRPGGameObjectManager gom, Sprite sp, int plnNo, int x, int y, StageMap m) {
		super(gom, sp, plnNo, x, y, m);
		
		menuStr = arr2String(Item.values());
		activeMenu = new boolean[] {true, true, true ,true, true};
		width = 170;
		height = 150;
		//menuCount = menuStr.length;
		commandHeight = 10;
		//menu = this;
		menuCursor = SMCursor.class;
		
		sp.setPos(plnNo, (gom.width - width)/2, (gom.height - height)/2);
		
		init();
	}
	
	protected class SMCursor extends MOMenuCursor {

		public SMCursor(SRPGGameObjectManager gom, Sprite sp, int plnNo, int x, int y, StageMap m) {
			super(gom, sp, plnNo, x, y, m);
		}

		protected void MCAction() {
			
			if(canceled) {
				y = Item.Cancel.ordinal();
				selected = true;
			}
			Item item = Item.values()[y];
			if(selected) {
				//select = true;
				switch(item) {
				case SwitchBGM:
					sp.switchBGM();
					break;
				case QuiteGame:
					destructor();
					// TODO
					//gom.init();
					map.destructor();
					break;
				case ChangePaintMode:
					sp.switchPaintMode();
					break;
				case ViewTrace:
					sp.switchTraceView();
					break;
				case Cancel:
					destructor();
					break;
				}
			}
		}
		
	}
	
}
