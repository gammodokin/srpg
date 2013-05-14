package srpg.map.ui;

import game.Sprite;
import srpg.SRPGGameObjectManager;
import srpg.map.StageMap;
import srpg.map.ui.area.MoveAttackableArea;

public class SelectCursor extends MapCursor {

	public SelectCursor(SRPGGameObjectManager gom, Sprite sp, int plnNo, int x, int y, StageMap m) {
		super(gom, sp, plnNo, x, y, m);
		//gom.addGO(CommandMenu.class.getCanonicalName(), 0, 0, map);
	}
	
	public void destructor() {
		if(maa != null)
			maa.destructor();
		
		super.destructor();
	}
	
	boolean first = true, actioning = false;
	MoveAttackableArea maa = null;
	protected void MCAction() {
		/*if(!map.current.isActionable() || map.isDestracted()) {
			destructor();
			return;
		}*/
		/*
		if(!map.current.isActionable())
			destructor();*/
		
		if(!first && maa == null) {
			maa = map.createMapObject(MoveAttackableArea.class, 0, 0);
			maa.setUnit(null);
		}
		
		actioning = true;
		if(first || canceled) {// || (selected && map.unitMap[y][x] == map.current)) {
			first = false;
			
			actioning = false;
			
			if(maa != null)
				maa.setUnit(null);
			
			gom.createMapObject(map, map.current.operator, 0, 0);
			
			return;
		} else if(selected) {
			maa.setUnit(map.unitMap[y][x]);
			select = true;
		}
		
	}
	
	public void motion() {
		if(!actioning) {
			x = map.current.getPos().x;
			y = map.current.getPos().y;
		}
		super.motion();
	}

	public int processPriority() {
		return 0;
	}
	
}
