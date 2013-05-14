package srpg.map.ui.area;

import game.ChangeTracker;
import game.Sprite;

import java.awt.Color;

import srpg.SRPGGameObjectManager;
import srpg.map.StageMap;
import srpg.map.obj.Unit;

public class MoveAttackableArea extends Area {

	public MoveAttackableArea(SRPGGameObjectManager gom, Sprite sp, int plnNo,
			int x, int y, StageMap m) {
		super(gom, sp, plnNo, x, y, m);
		
		color = new Color(0xff6600);
		
		createHorizon(MoveAttackHA.class);
		
		availables = map.newBoolArea();
	}

	public void setUnit(Unit unit) {
		//if(!ct.isChanged(unit))
		//	return;
		
		if(unit == null)
			availables = map.newBoolArea();
		else
			availables = unit.moveAndAttackableArea();
		//System.out.println(Arrays.deepToString(availables));
		
	}
	
	protected class MoveAttackHA extends HorizonArea {

		public MoveAttackHA(SRPGGameObjectManager gom, Sprite sp, int plnNo,
				int x, int hy, StageMap m) {
			super(gom, sp, plnNo, x, hy, m);
		}
		
		ChangeTracker<boolean[][]> ct = new ChangeTracker<boolean[][]>(null);
		
		@Override
		public void motion() {
			super.motion();
			if(ct != null) // スーパークラスのコストラクタで呼ばれた時のため
				repaint = ct.isChanged(availables);
		}
	}
}
