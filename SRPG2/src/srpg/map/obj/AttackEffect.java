package srpg.map.obj;

import game.BlendMode;
import game.Sprite;
import myutil.Coord;
import srpg.Damage;
import srpg.SRPGGameObjectManager;
import srpg.map.MapUserIF;
import srpg.map.StageDrawOrder;
import srpg.map.StageMap;
import srpg.map.UnitType;

public abstract class AttackEffect extends MapUserIF {
	
	protected Coord<Double> current, target;
	protected Coord<Integer> targetCoord;
//	protected Action action;

//	protected int power;

	public AttackEffect(SRPGGameObjectManager gom, Sprite sp, int plnNo, int x, int y, StageMap m) {
		super(gom, sp, plnNo, x, y, m);

		sp.setPos(plnNo, StageDrawOrder.EFFECT.z());
		sp.setBlendMode(plnNo, BlendMode.DODGE);
		//sp.setTrailCount(plnNo, 3);
		
		current = map.getHexCoord(map.current.getPos());
		setHexCenter(current);
		//sp.setPos(plnNo, current);
		
		targetCoord = new Coord<Integer>(x, y);
		target = map.getHexCoord(x, y);
		setHexCenter(target);
		
//		power = map.current.getCurrentAction().damage;
	}
	
	protected void damage() {
		Unit current = map.current;
		for(UnitType ut : current.getType().attackable(current.getCurrentAction().actType))
			for(Unit unit : map.searchUnits(current.getAreaAttackableArea(targetCoord), ut)) {
				Damage d = current.getCurrentAction().damage;
				switch(current.getCurrentAction().actType) {
				case ALL:
				case ATTACK:
					unit.enemyDamage(d);
					break;
				case SUPPORT:
					unit.showDamage(d);
					break;
				}
			}
	}

	/*void setPower(int power) {
		this.power = power;
	}*/

	protected void setHexCenter(Coord<Double> cood) {
		cood.x += map.hexW/2f;
		cood.y += map.hexH/2f;
	}
}
