package srpg;

import srpg.map.obj.AttackEffect;
import srpg.map.obj.CureAE;

public class Item extends Action {
	
	int count;

	Item(String name, Range range, Range area,
			Damage damage, ActionType act, Damage cost, Class<? extends AttackEffect> effect, int count) {
		super(name, range, area, damage, act, cost, effect);
		
		this.count = count;
	}
	
	@Override
	public boolean usable(UnitStatus s) {
		return count > 0 && super.usable(s);
	}
	
	@Override
	public void use(UnitStatus s) {
		count--;
	}
	
	@Override
	public String toString() {
		return name + "  x" + count;
	}
	
	static Item newCureHP(int count) {
		return new Item("cure HP",
				new Range(RangeType.ARCH, 0, 2, 0, 3),
				Range.SIMPLE,
				new Damage(-50, 0, 0, 1.0), ActionType.SUPPORT,
				new Damage(0, 0, 0, 1.0), CureAE.class, count);

	}
	
	static Item newCureSP(int count) {
		return new Item("cure SP",
				new Range(RangeType.ARCH, 0, 2, 0, 3),
				Range.SIMPLE,
				new Damage(0, -30, 0, 1.0), ActionType.SUPPORT,
				new Damage(0, 0, 0, 1.0), CureAE.class, count);
	}
}
