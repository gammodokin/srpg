package srpg.map;

import myutil.Coord;

public enum Direction{
	UPRIGHT(1, -1) {
		@Override
		public boolean isBehind(Coord<Integer> current, Coord<Integer> next) {
			setXY(current, next);
			return LEFT.equals(x, y) || DOWNLEFT.equals(x, y) || DOWNRIGHT.equals(x, y);
		}

	}, RIGHT(1, 0) {
		@Override
		public boolean isBehind(Coord<Integer> current, Coord<Integer> next) {
			setXY(current, next);
			return UPLEFT.equals(x, y) || LEFT.equals(x, y) || DOWNLEFT.equals(x, y);
		}

	}, DOWNRIGHT(0, 1) {
		@Override
		public boolean isBehind(Coord<Integer> current, Coord<Integer> next) {
			setXY(current, next);
			return UPRIGHT.equals(x, y) || UPLEFT.equals(x, y) || LEFT.equals(x, y);
		}

	}, DOWNLEFT(-1, 1) {
		@Override
		public boolean isBehind(Coord<Integer> current, Coord<Integer> next) {
			setXY(current, next);
			return UPLEFT.equals(x, y) || UPRIGHT.equals(x, y) || RIGHT.equals(x, y);
		}

	}, LEFT(-1, 0) {
		@Override
		public boolean isBehind(Coord<Integer> current, Coord<Integer> next) {
			setXY(current, next);
			return UPRIGHT.equals(x, y) || RIGHT.equals(x, y) || DOWNRIGHT.equals(x, y);
		}

	}, UPLEFT(0, -1) {
		@Override
		public boolean isBehind(Coord<Integer> current, Coord<Integer> next) {
			setXY(current, next);
			return RIGHT.equals(x, y) || DOWNRIGHT.equals(x, y) || DOWNLEFT.equals(x, y);
		}

	};
	
	int difX, difY;
	Direction(int x, int y) {
		difX = x;
		difY = y;
		
		
	}
	
	protected int x, y;
	
	public boolean isFront(Coord<Integer> current, Coord<Integer> next) {
		setXY(current, next);
		return equals(x, y);
	}
	
	public abstract boolean isBehind(Coord<Integer> current, Coord<Integer> next);
	
	boolean equals(int x, int y) {
		return difX == x && difY == y;
	}
	
	/*boolean isFront(Coord<Integer> current, Coord<Integer> next) {
		int x = StageMap.toVectorCoord(next).x - StageMap.toVectorCoord(current).x;
		int y = next.y - current.y;
		
		return Math.abs(difX - x) + Math.abs(difY - y) == 0;
		//return dir(current, next, 1);
	}
	
	boolean isFronts(Coord<Integer> current, Coord<Integer> next) {
		int x = StageMap.toVectorCoord(next).x - StageMap.toVectorCoord(current).x;
		int y = next.y - current.y;
		
		return Math.abs(difX - x) + Math.abs(difY - y) <= 1;
		//return dir(current, next, 1);
	}
	
	boolean isBehind(Coord<Integer> current, Coord<Integer> next) {
		/*Direction behind = null;
		for(Direction d : Direction.values())
			if(d.dir(current, next, -1))
				behind = d;
		
		return behind == null ? false : distance(behind) <= 1;
		return !isFronts(current, next);
	}*/
	
	/*private int distance(Direction d) {
		return Math.abs(difX - d.difX) + Math.abs(difY - d.difY);
	}*/
	
	protected void setXY(Coord<Integer> current, Coord<Integer> next) {
		x = StageMap.toVectorCoord(next).x - StageMap.toVectorCoord(current).x;
		y = next.y - current.y;
		
	}
}
