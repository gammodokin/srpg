package srpg.map;

import srpg.ActionType;

public enum UnitType {
	OBSTACLE {
		@Override
		public UnitType[] attackable(ActionType act) {
			switch(act) {
			case ATTACK:
				return new UnitType[]{};
			case SUPPORT:
				return new UnitType[]{};
			}
			return UnitType.values();
		}

	}, FRIEND {
		@Override
		public UnitType[] attackable(ActionType act) {
			switch(act) {
			case ATTACK:
				return new UnitType[]{ENEMY};
			case SUPPORT:
				return new UnitType[]{FRIEND};
			}
			return UnitType.values();
		}
		
	}, ENEMY {
		@Override
		public UnitType[] attackable(ActionType act) {
			switch(act) {
			case ATTACK:
				return new UnitType[]{FRIEND};
			case SUPPORT:
				return new UnitType[]{ENEMY};
			}
			return UnitType.values();
		}

	};
	private Direction dir;
	
	public void setInitDir(Direction dir) {
		this.dir = dir;
	}
	public Direction getInitDir() {
		return dir;
	}
	
	public abstract UnitType[] attackable(ActionType act);
	//public abstract UnitType[] supportable();
	
	public boolean isAttackable(UnitType target, ActionType act) {
		return contains(attackable(act), target);
	}
	
	//public boolean isSupportable(UnitType target) {
	//	return contains(supportable(), target);
	//}
	
	public static boolean contains(UnitType[] uts, UnitType ut) {
		for(UnitType u : uts)
			if(ut == u)
				return true;
		return false;
	}
}
