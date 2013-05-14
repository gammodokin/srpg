package srpg.map.ui.ai;

import game.Sprite;

import java.util.List;

import myutil.Coord;
import srpg.Action;
import srpg.ActionType;
import srpg.MapSearch;
import srpg.SRPGGameObjectManager;
import srpg.map.StageMap;
import srpg.map.UnitType;
import srpg.map.obj.Unit;

public class EAIActive extends EnemyAI {

	enum StrategyMode{
		ATTACK {
			@Override
			List<Unit> targetList(StageMap map) {
				return searchFriends(map);
			}

			@Override
			List<Unit> attackableTargets(StageMap map, Action act) {
				return searchAttackableFriends(map, act);
			}

			@Override
			UnitType targetType() {
				return UnitType.FRIEND;
			}

			@Override
			ActionType actionType() {
				return ActionType.ATTACK;
			}

			@Override
			double evalAct(Action act, Unit target) {
				return (act.damage.hp + act.damage.sp);// / 0xfff + (0xff - MapSearch.distanse(actor.getPos().x, actor.getPos().y, target.getPos().x, target.getPos().y)) / 0xf;
			}

			@Override
			boolean near() {
				return true;
			}

		},
		SUPPORT {
			@Override
			List<Unit> targetList(StageMap map) {
				return searchDamagedEnemies(map, map.current.moveAndAttackableArea(ActionType.SUPPORT, false));
			}

			@Override
			List<Unit> attackableTargets(StageMap map, Action act) {
				return searchDamagedEnemies(map, map.current.getAttackableArea(map.current.getPos(), act, true));
			}

			@Override
			UnitType targetType() {
				return UnitType.ENEMY;
			}

			@Override
			ActionType actionType() {
				return ActionType.SUPPORT;
			}

			@Override
			double evalAct(Action act, Unit target) {
				double score = 0;
				
				if(target.getStatus().sp < target.getStatus().maxSP / 2)
					score += -act.damage.sp - target.getStatus().sp / (double) 0xfff;
				score = score < 0 ? 0 : score;
				
				if(target.getStatus().hp < target.getStatus().maxHP / 2)
					score += -act.damage.hp * 0xff - target.getStatus().hp / (double)0xfff;

				return score;
			}

			@Override
			boolean near() {
				return false;
			}

		};
		
		private static Unit actor;
		
		static void setActor(Unit u){
			actor = u;
		}

		abstract List<Unit> targetList(StageMap map);
		abstract List<Unit> attackableTargets(StageMap map, Action act);

		abstract UnitType targetType();
		abstract ActionType actionType();

		abstract double evalAct(Action act, Unit target);
		
		abstract boolean near();
	}

	protected StrategyMode smode;

	private final List<Action> acts;

	public EAIActive(SRPGGameObjectManager gom, Sprite sp, int plnNo, int x, int y, StageMap m) {
		super(gom, sp, plnNo, x, y, m);

		acts = map.current.getStatus().usableActionList();
		
		StrategyMode.setActor(map.current);

		smode = StrategyMode.SUPPORT;

		if(!setTarget()) {
			smode = StrategyMode.ATTACK;
			setTarget();
		}
		/*
		 for(Action act : acts)
			 System.out.print(act + ": " + StrategyMode.SUPPORT.evalAct(act, nearest) + ", ");
		 System.out.println();
		 System.out.println(bestAct + ": " + smode.evalAct(bestAct, nearest));
		*/
	}

	Unit nearest;
	Coord<Integer>[] targetWay;
	Action bestAct;

	protected boolean setTarget() {
		MapSearch ms = map.current.initMS(new MapSearch(map.hex), true);
		
		nearest = null;
		targetWay = null;
		bestAct = null;

		List<Unit> targets = smode.targetList(map);
		Unit currentTarget = null;

		while(!targets.isEmpty()) {
			
			if(smode.near() && targetWay != null)
				break;

			int min, dis;
			min = dis = Integer.MAX_VALUE;
			for(Unit target : targets) {
				dis = MapSearch.distanse(map.current.getPos().x, map.current.getPos().y,
						target.getPos().x, target.getPos().y);
				if(min > dis) {
					min = dis;
					currentTarget = target;
				}
			}
			targets.remove(currentTarget);

		//for(Unit currentTarget : targets)
			for(Action act : acts)
				if(act.actType == smode.actionType()
						&& (bestAct == null || smode.evalAct(act, currentTarget) > smode.evalAct(bestAct, nearest))) {
					bestAct = act;
					nearest = currentTarget;

					int lenMin = Integer.MAX_VALUE;
					Coord<Integer> c = new Coord<Integer>(0, 0);
					boolean[][] aaarea = map.current.getAttackableArea(currentTarget.getPos(), bestAct, true);
					for(c.x = 0; c.x < map.hex[0].length; c.x++)
						for(c.y = 0; c.y < map.hex.length; c.y++) {
							if(!aaarea[c.x][c.y])
								continue;

							ms.search(c, Integer.MAX_VALUE, map.current.getStatus().capa.jump);
							Coord<Integer>[] way = ms.getWay(c.x, c.y);
							if(way != null && lenMin > way.length) {
								lenMin = way.length;
								targetWay = way;
							}
						}
				}
		}

		return targetWay != null && bestAct != null && smode.evalAct(bestAct, nearest) > 0;

	}

	protected boolean activation() {
		return true;
	}

	protected void moveStrategy() {
		// ÉxÉXÉgÇ»çUåÇÇÃîÕàÕÇ…Ç∑Ç≈Ç…ìGÇ™Ç¢ÇÍÇŒÅAìÆÇ≠ïKóvÇ»Çµ
		if(!smode.attackableTargets(map, bestAct).isEmpty())
			return;

		boolean[][] movable = map.current.getMovableArea(true);
		Coord<Integer> farthestMove = null, attackableMove = null;
		int max, dis, maxA;
		max = dis = maxA = Integer.MIN_VALUE;

		map.current.setCurrentAction(bestAct);
		if(targetWay != null)
			for(Coord<Integer> c : targetWay) {
				if(!movable[c.x][c.y])
					continue;

				dis = MapSearch.distanse(c.x, c.y, map.current.getPos().x, map.current.getPos().y);

				if(max < dis) {
					max = dis;
					farthestMove = c.clone();
				}
				if(maxA < dis && map.current.getAttackableArea(c, true)[nearest.getPos().x][nearest.getPos().y]) {
					maxA = dis;
					attackableMove = c.clone();
				}

			}

		if(attackableMove != null)
			map.current.setMove(attackableMove);
		else
			map.current.setMove(farthestMove);
	}

	protected void attackStrategy() {
		Action attack = null;
		Unit actTarget = null;
		double maxEffect = Double.MIN_VALUE;

		// ìGÇ…ìÕÇ≠çUåÇÇÃÇ§ÇøÇ≈ÅAÇ‡Ç¡Ç∆Ç‡å¯â ìIÇ»Ç‡ÇÃ
		for(Action act : acts) {
			List<Unit> atts = smode.attackableTargets(map, act);

			if(atts.isEmpty())
				continue;

			Unit target;
			if(atts.contains(nearest))
				target = nearest;
			else
				target = atts.toArray(new Unit[0])[0];


			double effect = smode.evalAct(act, target);
			if(maxEffect < effect) {
				attack = act;
				actTarget = target;
				maxEffect = effect;
			}
		}

		if(attack == null)
			return;

		map.current.setCurrentAction(attack);

		map.current.setAttack(actTarget.getPos().x, actTarget.getPos().y);

		//System.out.println(map.current + " Å® " + attack + " Å® " + actTarget);

		//System.out.println("nearest : " + nearest + " is in AArea : " + atfs.contains(nearest));
		/*
		if(map.current.getAttackableArea()[nearest.getPos().x][nearest.getPos().y]) {
			map.current.setAttack(nearest.getPos().x, nearest.getPos().y);
		}*/
	}

}
