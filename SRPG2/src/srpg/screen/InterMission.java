package srpg.screen;

import game.GameObjectManager;
import game.Sprite;

import java.util.ArrayList;

import srpg.Capability;
import srpg.Player;
import srpg.UnitStatus;
import srpg.screen.talk.Talk;


public class InterMission extends ScreenMenu {

	enum Item {Stage_Select, Status, Equip, About}
	
	private Player player;
	
	public InterMission(GameObjectManager gomm, Sprite sp, int plnNo, int x,
			int y) {
		super(gomm, sp, plnNo, x, y);
		
		menuStr = arr2String(Item.values());
		activeMenu = new boolean[] {true, false, false, true};
		//menu = this;
		menuCursor = IMCursor.class;
		
		init();
		
		player = new Player(new ArrayList<Capability>(), 0);
		
		UnitStatus.initParty(player.party);
	}
	
	protected class IMCursor extends SMCursor {

		public IMCursor(GameObjectManager gom, Sprite sp, int plnNo, int x,
				int y) {
			super(gom, sp, plnNo, x, y);
		}
		
		@Override
		protected void MCAction() {
			setView(true);
			sp.playBGM(Sprite.BGM.TITLE);
			
			Item item = Item.values()[y];
			if(select) {
				switch(item) {
				case Stage_Select:
					setView(false);
					gom.addGO(StageSelect.class, 0, 0).setParty(player);
					break;
				case Status:
					setView(false);
					gom.addGO(Status.class, 0, 0).initParty(player.party);
					break;
				case About:
					gom.addGO(Talk.class, 0, 0);
				}
			}
		}
		
	}
	
}
