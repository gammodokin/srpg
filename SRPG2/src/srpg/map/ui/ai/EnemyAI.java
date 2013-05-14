package srpg.map.ui.ai;

import game.Draw;
import game.Plane;
import game.Sprite;

import java.awt.Graphics;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import srpg.Action;
import srpg.SRPGGameObjectManager;
import srpg.map.MapUserIF;
import srpg.map.StageMap;
import srpg.map.UnitType;
import srpg.map.obj.Unit;

public abstract class EnemyAI extends MapUserIF {

	public EnemyAI(SRPGGameObjectManager gom, Sprite sp, int plnNo, int x, int y, StageMap m) {
		super(gom, sp, plnNo, x, y, m);

		sp.setDraw(plnNo, new Draw() {
			public void drawing(Graphics g, Plane pln) {} });
	}

	protected Random rnd = new Random();
	protected enum Mode{MOVE, ATTACK, STANDBY}
	protected Mode mode = Mode.STANDBY;
	@Override
	public void action() {
		switch(mode) {
		case STANDBY:
			if(activation())
				mode = Mode.MOVE;
			else
				destructor();
			break;
		case MOVE:
			moveStrategy();
			mode = Mode.ATTACK;
			break;
		case ATTACK:
			destructor();
			attackStrategy();
			break;
		}
	}

	protected static List<Unit> searchFriends(StageMap map) {
		boolean[][] area = map.newBoolArea();
		for(boolean[] flags : area)
			Arrays.fill(flags, true);
		return map.searchUnits(area, UnitType.FRIEND);
	}
	
	protected static List<Unit> searchAttackableFriends(StageMap map, Action act) {
		return map.searchUnits(map.current.getAttackableArea(map.current.getPos(), act, true), UnitType.FRIEND);
	}

	protected List<Unit> searchMoveAttackableFriends() {
		return map.searchUnits(map.current.moveAndAttackableArea(), UnitType.FRIEND);
	}
	
	protected static List<Unit> searchDamagedEnemies(StageMap map, boolean[][] area) {
		List<Unit> enemys = map.searchUnits(area, UnitType.ENEMY);
		
		for(Iterator<Unit> i = enemys.iterator(); i.hasNext();) {
			Unit e = i.next();
			if(!isDamaged(e))
				i.remove();
		}
		
		return enemys;
	}
	
	protected static boolean isDamaged(Unit unit) {
		return unit.getStatus().hp < unit.getStatus().maxHP / 2 || unit.getStatus().sp < unit.getStatus().maxSP / 2;
	}

	abstract protected boolean activation();

	abstract protected void moveStrategy();

	abstract protected void attackStrategy();

	public void destructor() {
		super.destructor();
		map.current.setActionable(false);
	}
}
