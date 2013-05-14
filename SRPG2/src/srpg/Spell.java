package srpg;

import srpg.map.obj.AttackEffect;

public class Spell extends Action {

	public Spell(String name, Range range, Range area, Damage damage,
			ActionType act, Damage cost, Class<? extends AttackEffect> effect) {
		super(name, range, area, damage, act, cost, effect);
		
		
	}
	
	@Override
	public boolean usable(UnitStatus s) {
		return super.usable(s) && s.tension >= 1;
	}
	
	@Override
	public void use(UnitStatus s) {
		s.tension = 0;
		super.use(s);
	}

}
