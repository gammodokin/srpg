package srpg.screen;

import game.GameObjectManager;
import game.Sprite;
import srpg.Player;
import srpg.map.StageMap;

public class StageSelect extends ScreenMenu {

	enum Item {
		Stage1("stage1.txt"), Stage2("stage2.txt"), Stage3("stage3.txt");//, Debug("debugstage.txt");
		
		String file;
		
		Item(String file) {
			this.file = file;
		}
	}

	private Player player;

	public StageSelect(GameObjectManager gom, Sprite sp, int plnNo, int x,
			int y) {
		super(gom, sp, plnNo, x, y);
		menuStr = arr2String(Item.values());
		activeMenu = new boolean[] {true, true, true,};// true};
		//menu = this;
		menuCursor = SSCursor.class;

		init();
		
	}

	void setParty(Player player) {
		this.player = player;
	}

	protected class SSCursor extends SMCursor {

		public SSCursor(GameObjectManager gom, Sprite sp, int plnNo, int x, int y) {
			super(gom, sp, plnNo, x, y);
		}

		@Override
		protected void MCAction() {
			if(canceled) {
				destructor();
				return;
			}

			Item item = Item.values()[y];

			if(select) {
				destructor();
				
				gom.addGO(StageMap.class, 20, 20).init(item.file, player);
			}


		}
	}
}
