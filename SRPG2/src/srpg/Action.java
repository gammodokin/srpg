package srpg;

import srpg.map.obj.AttackEffect;

public class Action {
	final String name;
	final public Range range;
	final public Range area;
	
	public final Damage damage;
	//int nextRes;
	public final Damage cost;
	final public Class<? extends AttackEffect> effect;
	public final ActionType actType;
	
	Action(String name, Range range, Range area, Damage damage, ActionType act, Damage cost, Class<? extends AttackEffect> effect) {
		this.name = name;
		this.range = range;
		this.area = area;
		this.damage = damage;
		this.cost = cost;
		//this.nextRes = nextRes;
		this.effect = effect;
		this.actType = act;
	}
	
	public boolean usable(UnitStatus s) {
		return s.hp > cost.hp && s.sp >= cost.sp;
	}
	
	public void use(UnitStatus s) {
		s.damaging(cost);
	}
	
	
	
	@Override
	public String toString() {
		return name;
	}
}
