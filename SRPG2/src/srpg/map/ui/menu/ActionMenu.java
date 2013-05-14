package srpg.map.ui.menu;

import game.Sprite;
import srpg.Action;
import srpg.SRPGGameObjectManager;
import srpg.map.StageMap;
import srpg.map.ui.area.AttackableArea;

public class ActionMenu extends MapObjectMenu {
	
	protected Action[] action;

	public ActionMenu(SRPGGameObjectManager gom, Sprite sp, int plnNo, int x, int y, StageMap m) {
		super(gom, sp, plnNo, x, y, m);

	}
	
	protected void init(Action[] a, Class<? extends AMCursor> cursor) {
		action = a;
		menuStr = arr2String(action);
		activeMenu = new boolean[menuStr.length];
		for(int i = 0; i < activeMenu.length; i++)
			activeMenu[i] = action[i].usable(map.current.getStatus());
		width = 200;
		height = 150;
		commandHeight = 10;
		//menu = this;
		menuCursor = cursor;
		
		sp.setPos(plnNo, (gom.width - width)/2, (gom.height - height)/2);
		
		init();
	}
	
	protected class AMCursor extends MOMenuCursor {

		public AMCursor(SRPGGameObjectManager gom, Sprite sp, int plnNo, int x, int y, StageMap m) {
			super(gom, sp, plnNo, x, y, m);
		}

		protected void MCAction() {
			setView(true);
			
			if(!map.current.isAttackable())
				canceled = true;
			
			if(canceled) {
				map.current.setCurrentAction(null);
				destructor();
				return;
			}
			map.current.setCurrentAction(action[y]);
			if(selected && activeMenu[y]) {
				setView(false);
				map.createMapObject(AttackableArea.class, map.current.getPos().x, map.current.getPos().y);
			}
		}
		
	}
}
