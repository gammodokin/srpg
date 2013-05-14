package srpg.map.ui.menu;

import game.Sprite;
import srpg.Action;
import srpg.SRPGGameObjectManager;
import srpg.map.StageMap;
import srpg.map.ui.DirectionSelect;
import srpg.map.ui.area.AttackableArea;
import srpg.map.ui.area.MovableArea;

public class CommandMenu extends MapObjectMenu {
	enum Item{Move, Attack, Skill, Item, System, Standby, };

	public CommandMenu(SRPGGameObjectManager gom, Sprite sp, int plnNo, int x, int y, StageMap m) {
		super(gom, sp, plnNo, x, y, m);
		menuStr = arr2String(Item.values());
		
		boolean skill = false, item = false;
		
		for(Action act : map.current.getStatus().capa.skill)
			skill = skill || act.usable(map.current.getStatus());
		
		for(Action act : map.current.getStatus().capa.item)
			item = item || act.usable(map.current.getStatus());
		
		activeMenu = new boolean[] {map.current.isMovable(),
				map.current.isAttackable(),
//				map.current.getStatus().capa.skill.length > 0, map.current.getStatus().capa.item.length > 0,
				skill, item,
				true, true};
		width = 110;
		height = 170;
		commandHeight = 10;
		//menu = this;
		menuCursor = CMCursor.class;
		sp.setPos(plnNo, 30, 20);
		
		init();
	}
	
	protected class CMCursor extends MOMenuCursor {

		public CMCursor(SRPGGameObjectManager gom, Sprite sp, int plnNo, int x, int y, StageMap m) {
			super(gom, sp, plnNo, x, y, m);
		}

		boolean afterMove = false;
		protected void MCAction() {
			// SystemMenu‚©‚çQuitGame‚µ‚½ê‡
			if(map.isDestracted()) {
				destructor();
				return;
			}
			
			// ˆÚ“®Œˆ’èŒãAŠÔ”¯‚¢‚ê‚¸‚É•ûŒüw’è
			if(afterMove && !map.current.isMovable()) {
				gom.createMapObject(map, DirectionSelect.class, map.current.getPos().x, map.current.getPos().y);
				afterMove = false;
				return;
			}
			afterMove = false;
			map.setCursorPos(map.current.getPos());
			
			if(!sp.getView(plnNo)) {
				repaint = true;
				setView(true);
			}
			
			activeMenu[Item.Move.ordinal()] = map.current.isMovable();
			activeMenu[Item.Attack.ordinal()] = map.current.isAttackable();
			
			//y = (y + menuStr.length) % menuStr.length;
			//sp.setPos(plnNo, current.x, current.y + span * y + commandHeight * y);
			
			if(!map.current.isAttackable()) {
				selected = true;
				y = Item.Standby.ordinal();
			}
			
			if(canceled) {
				if(!map.current.isMovable() && map.current.isAttackable()) {
					map.current.returnPrevious();
					activeMenu[Item.Move.ordinal()] = map.current.isMovable();
					repaint = true;
					y = Item.Move.ordinal();
				}else {
					destructor();
					return;
				}
			}
			Item item = Item.values()[y];
			if(selected && activeMenu[item.ordinal()]) {
				//select = true;
				
				switch(item) {
				case Move:
					setView(false);
					map.createMapObject(MovableArea.class, map.current.getPos().x, map.current.getPos().y);
					afterMove = true;
					break;
				case Attack:
					setView(false);
					map.current.setCurrentAction(map.current.getStatus().capa.attack);
					map.createMapObject(AttackableArea.class, map.current.getPos().x, map.current.getPos().y);
					break;
				case Skill:
					setView(false);
					gom.createMapObject(map, SkillMenu.class, 0, 0);
					break;
				case Item:
					setView(false);
					gom.createMapObject(map, ItemMenu.class, 0, 0);
					break;
				case System:
					gom.createMapObject(map, SystemMenu.class, 0, 0);
					break;
				case Standby:
					destructor();
					map.current.setCurrentAction(null);
					map.current.setActionable(false);
					break;
				/*case CANCEL:
					
					break;*/
				}
			}
		}
		
	}
	
}
