package srpg.map.ui.menu;

import game.Sprite;
import srpg.SRPGGameObjectManager;
import srpg.map.StageMap;

public class SkillMenu extends ActionMenu {

	public SkillMenu(SRPGGameObjectManager gom, Sprite sp, int plnNo, int x, int y, StageMap m) {
		super(gom, sp, plnNo, x, y, m);
		
		init(map.current.getStatus().capa.skill, SMCursor.class);
	}
	
	protected class SMCursor extends AMCursor {

		public SMCursor(SRPGGameObjectManager gom, Sprite sp, int plnNo, int x,
				int y, StageMap m) {
			super(gom, sp, plnNo, x, y, m);
		}
		
	}
}
