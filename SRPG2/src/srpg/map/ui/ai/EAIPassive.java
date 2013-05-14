package srpg.map.ui.ai;

import game.Sprite;
import srpg.SRPGGameObjectManager;
import srpg.map.StageMap;

public class EAIPassive extends EAIActive {
	
	public EAIPassive(SRPGGameObjectManager gom, Sprite sp, int plnNo, int x, int y, StageMap m) {
		super(gom, sp, plnNo, x, y, m);
	}
	
	@Override
	protected boolean activation() {
		return !map.searchUnits(map.current.moveAndAttackableArea(smode.actionType(), false), smode.targetType()).isEmpty()
			|| map.current.getStatus().hp < map.current.getStatus().maxHP;
	}
	
	/*
	Unit nearest;
	boolean attackable;
	protected boolean activation() {
		List<Unit> friends = searchMoveAttackableFriends();

		attackable = !map.searchUnits(map.current.getAttackableArea(), UnitType.FRIEND).isEmpty();

		int min, dis;
		min = dis = Integer.MAX_VALUE;

		for(Unit friend : friends) {
			dis = MapSearch.distanse(map.current.getPos().x, map.current.getPos().y,
					friend.getPos().x, friend.getPos().y);
			if(min > dis) {
				min = dis;
				nearest = friend;
			}
		}

		UnitStatus status = map.current.getStatus();
		return !friends.isEmpty() || status.hp < status.maxHP;
	}

	protected void moveStrategy() {
		if(attackable)
			return;

		boolean[][] movable = map.current.getMovableArea(true);
		Coord<Integer> nearestMove = null, attackableMove = null;
		int min, dis, minA;
		min = dis = minA = Integer.MAX_VALUE;

		for(int x = 0; x < movable.length; x++)
			for(int y = 0; y < movable[x].length; y++) {
				if(!movable[x][y])
					continue;

				dis = MapSearch.distanse(x, y, nearest.getPos().x, nearest.getPos().y);
				if(min > dis) {
					min = dis;
					nearestMove = new Coord<Integer>(x, y);
				}
				if(minA > dis && map.current.getAttackableArea(new Coord<Integer>(x, y), true)[nearest.getPos().x][nearest.getPos().y]) {
					minA = dis;
					attackableMove = new Coord<Integer>(x, y);
				}

			}

		if(attackable = attackableMove != null)
			map.current.setMove(attackableMove);
		else
			map.current.setMove(nearestMove);

	}

	protected void attackStrategy() {
		if(map.current.getAttackableArea()[nearest.getPos().x][nearest.getPos().y]) {
			map.current.setAttack(nearest.getPos().x, nearest.getPos().y);
		}
	}

*/
}
