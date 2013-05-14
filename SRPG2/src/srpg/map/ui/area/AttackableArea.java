package srpg.map.ui.area;

import game.ChangeTracker;
import game.Sprite;

import java.awt.Color;

import myutil.Coord;
import srpg.ActionType;
import srpg.SRPGGameObjectManager;
import srpg.map.StageMap;
import srpg.map.UnitType;

public class AttackableArea extends Area {
	
	Area aaarea;
	
	//boolean support;
	ActionType act;
	
	public AttackableArea(SRPGGameObjectManager gom, Sprite sp, int plnNo, int x, int y, StageMap m) {
		super(gom, sp, plnNo, x, y, m);
		act = map.current.getCurrentAction().actType;
		//support = map.current.getCurrentAction().damage.hp < 0
		//	|| map.current.getCurrentAction().damage.sp < 0;
		
		switch(act) {
		case ALL:
		case ATTACK:
			color = new Color(0xff0000);
			break;
		case SUPPORT:
			color = new Color(0x00ffff);
			break;
		}
		
		availables = map.current.getAttackableArea();
		
		gom.createMapObject(map, AttackCursor.class, x, y, this);
		
		createHorizon(AttackHA.class);
		
		gom.createMapObject(map, AreaAttackArea.class, x, y, this);
	}
	@Override
	public
	void destructor() {
		aaarea.destructor();
		super.destructor();
	}

	
	protected class AttackCursor extends AreaCursor {
		
		UnitType[] target;

		public AttackCursor(SRPGGameObjectManager gom, Sprite sp, int plnNo, int x, int y, StageMap m) {
			super(gom, sp, plnNo, x, y, m);
			switch(act) {
			case ALL:
				act = ActionType.ATTACK;
			case ATTACK:
				color = new Color(0xff2222);
				break;
			case SUPPORT:
				color = new Color(0x22ffff);
				break;
			}
			
			target = map.current.getType().attackable(act);
		}
		
		protected void MCAction() {
			boolean existTarget = false;
			boolean[][] areaAttack = map.current.getAreaAttackableArea(map.getCursored());
			
			for(int x = 0; x < areaAttack.length; x++)
				for(int y = 0; y < areaAttack[x].length; y++) {
					existTarget = existTarget || (areaAttack[x][y] && map.unitMap[y][x] != null && UnitType.contains(target, map.unitMap[y][x].getType()));
				}
			
			if(selected
					&& existTarget
//					&& map.unitMap[y][x] != null
//					&& map.unitMap[y][x].getType() == UnitType.ENEMY
					&& isAvailable(x, y)) {
				destructor();
				map.current.setAttack(x, y);
				
				select = true;
			} else if(canceled) {
				map.current.setCurrentAction(null);
				destructor();
			}
			
			
		}
		
	}
	
	protected class AreaAttackArea extends Area {

		public AreaAttackArea(SRPGGameObjectManager gom, Sprite sp, int plnNo,
				int x, int y, StageMap m) {
			super(gom, sp, plnNo, x, y, m);
			
			color = new Color(0xffff22);
			
			//availables = map.current.getAreaAttackableArea();
			
			createHorizon(AreaAttackHA.class);
			
			aaarea = this;
		}
		
		protected class AreaAttackHA extends HorizonArea {

			public AreaAttackHA(SRPGGameObjectManager gom, Sprite sp, int plnNo,
					int x, int hy, StageMap m) {
				super(gom, sp, plnNo, x, hy, m);
				
				availables = map.current.getAreaAttackableArea(map.getCursored());
			}
			
			ChangeTracker<Coord<Integer>> ct = new ChangeTracker<Coord<Integer>>(null);
			
			@Override
			public void motion() {
				if(ct != null && ct.isChanged(map.getCursored().clone())) {
					repaint = true;
					availables = map.current.getAreaAttackableArea(map.getCursored());
				}
				super.motion();
			}
			
		}
		
	}
	
	protected class AttackHA extends HorizonArea {

		public AttackHA(SRPGGameObjectManager gom, Sprite sp, int plnNo, int x,
				int y, StageMap m) {
			super(gom, sp, plnNo, x, y, m);
		}
		
	}
}
