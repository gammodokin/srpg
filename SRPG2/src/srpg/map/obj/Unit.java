package srpg.map.obj;

import game.ChangeTracker;
import game.GameObject;
import game.Motionable;
import game.Sprite;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.List;

import myutil.Coord;
import srpg.Action;
import srpg.ActionType;
import srpg.Capability;
import srpg.Damage;
import srpg.MapSearch;
import srpg.SRPGGameObjectManager;
import srpg.UnitStatus;
import srpg.map.Direction;
import srpg.map.MapDrawOrder;
import srpg.map.MapUserIF;
import srpg.map.StageMap;
import srpg.map.UnitType;
import srpg.map.ui.SelectCursor;
import srpg.map.ui.ShowDamage;

public abstract class Unit extends MapUserIF implements Motionable{

	public String unitNumber;
	public BufferedImage face;
	protected BufferedImage[] figure = new BufferedImage[3];

	private SelectCursor cursor;
	public Class<? extends GameObject> operator;

	final public UnitType type;
	protected Coord<Integer> current, target, previous;
	protected Direction dir, dirPrevious;
	protected MapSearch ms;
	protected Coord<Integer>[] way;
	UnitStatus status;
	protected boolean actionable, movable, attackable;
	protected Action currentAction;

	enum Mode{IDLE, MOVE, ATTACK}
	protected Mode mode = Mode.IDLE;

	protected BufferedImage damageImg;
	protected boolean damagedFlag = false;

	private Gage gage;

	public Unit(SRPGGameObjectManager gom, Sprite sp, int plnNo, int x, int y, StageMap m, UnitType type) {
		super(gom, sp, plnNo, x, y, m);

		this.type = type;
		this.dir = type.getInitDir();

		sp.setPos(plnNo, map.getHexCoord(x, y));
		sp.setPos(plnNo, MapDrawOrder.UNIT.z());

		String file = "damage";
		sp.addGrp(file);
		damageImg = sp.getGrp(file);

		initImg();
//		sp.setDraw(plnNo, new Draw() {
//
////			private int span = 30;
////			private int count = (int) (span * Math.random());
////			private boolean high = false;
//
//			public void drawing(Graphics g, Plane pln) {
//				//pln.coord.y -= map.hexH * 3 / 4;
//				drawGages(g, pln.coord.x, pln.coord.y);
//
//				/*double shadow = 2/3f;
//				g.setColor(new Color(0x33554466, true));
//				g.fillOval(pln.coord.x.intValue() + (int)Math.round(map.hexW * (1 - shadow) /2),
//						pln.coord.y.intValue() + map.hexH*3/4 + (int)Math.round(map.hexH * (1 - shadow) /2),
//						(int)Math.round(map.hexW * shadow), (int)Math.round(map.hexH * shadow));
//				//System.out.println("x = " + (int)(map.hexW * shadow) + " / " + (int)(map.hexH * shadow));
//				 */
////				int offset;
////				if(0 == (count %= span))
////					high = !high;
////				if(high)
////					offset = 0;
////				else
////					offset = -1;
////
////				pln.coord.y += offset;
////				drawUnit(pln, pln.coord.x, pln.coord.y);
////
////				g.setColor(Color.BLACK);
////
////				count++;
//			}
//
//			ChangeTracker<Direction> ctDir = new ChangeTracker<Direction>();
//			ChangeTracker<Boolean> ctDmg = new ChangeTracker<Boolean>(damagedFlag);
//			void drawUnit(Plane pln, double x, double y) {
//				//ctDir.update(dir);
//				//ctDmg.update(damagedFlag);
//				if(ctDir.isChanged(dir) || ctDmg.isChanged(damagedFlag) || pln.img == null) {
//					if(pln.img == null)
//						pln.initImage(map.hexW, map.hexH*2);
//					Graphics gbg = pln.getGraphics();
//
//					//double shadow = 1;
//					//gbg.setColor(new Color(0x33554466, true));
//					//gbg.fillOval((int)(map.hexW * (1 - shadow) /2f), map.hexH + (int)(map.hexH * (1 - shadow) /2f), (int)(map.hexW * shadow), (int)(map.hexH * shadow));
//
//					AffineTransform at;
//					BufferedImage img = null;
//
//					switch(dir) {
//					case UPRIGHT:
//					case UPLEFT:
//						img = figure[2];
//						break;
//					case RIGHT:
//					case LEFT:
//						img = figure[1];
//						break;
//					case DOWNRIGHT:
//					case DOWNLEFT:
//						img = figure[0];
//						break;
//					default:
//						System.out.println("drawingError : illegal direction");
//					}
//					switch(dir) {
//					case UPRIGHT:
//					case RIGHT:
//					case DOWNRIGHT:
//						break;
//					case UPLEFT:
//					case LEFT:
//					case DOWNLEFT:
//						at = AffineTransform.getScaleInstance(-1, 1);
//						at.translate(-img.getWidth(), 0);
//						img = (new AffineTransformOp(at, null)).filter(img, null);
//						break;
//					default:
//						System.err.println("drawingError : illegal direction");
//					}
//					gbg.drawImage(img, 0, 0, null);
//					drawDamage(gbg, 0, 0);
//
//					gbg.dispose();
//
//					face = img;
//				}
//			}
//
//			void drawDamage(Graphics g, double x, double y) {
//				if(damagedFlag)
//					g.drawImage(damageImg, (int)x, (int)y - map.hexH * 1/4, null);
//				damagedFlag = false;
//			}

//		});

		map.setUnit(x, y, this);
		current = new Coord<Integer>(x, y);
		previous = new Coord<Integer>(0, 0);

		gage = map.createMapObject(Gage.class, x, y);

		gom.removeUIF(this);

		gom.addMotionable(this);
	}

	@Override
	public void destructor() {
		gom.delGO(plnNo);// destructor();
		//gom.removeUIF(this);
		gom.removeMotionable(this);
		if(status.hp <= 0)
			map.destroyUnit(this);

		if(cursor != null)
			cursor.destructor();

		gage.destructor();

		super.destructor();
		//gom.removeMotionable(this);
	}

	boolean high = false;
	int motionCount = (int) (30 * Math.random());
	public void motion() {
		sp.setPos(plnNo, map.getHexCoord(current.x, current.y));
		sp.setMov(plnNo, 0, -2 - map.hexH * 3/4);
		sp.setPos(plnNo, MapDrawOrder.UNIT.z() + current.y * StageMap.YZRate);
		map.setUnit(current.x, current.y, this);

		int offset;
		if(0 == (motionCount %= 30))
			high = !high;
		if(high)
			offset = 0;
		else
			offset = -1;
		motionCount++;

		sp.setMov(plnNo, 0, offset);

		if(ctDir.isChanged(dir) || ctDmg.isChanged(damagedFlag))
			drawImg();
	}

	public int processPriority() {
		return 0;
	}

	@Override
	public String toString() {
		return getName();
	}

	private void initImg() {
		//int span = 30;
		sp.initImg(plnNo, map.hexW, map.hexH*2);
//		sp.setAnimeCount(plnNo, (int) (span * Math.random()));
	}

	ChangeTracker<Direction> ctDir = new ChangeTracker<Direction>();
	ChangeTracker<Boolean> ctDmg = new ChangeTracker<Boolean>(damagedFlag);
	private void drawImg() {
		//sp.setMov(plnNo, 0, -map.hexH * 3 / 4);

		sp.setTransparent(plnNo);

		Graphics2D g = sp.createGraphics(plnNo);

		AffineTransform at;
		BufferedImage img = null;

		switch(dir) {
		case UPRIGHT:
		case UPLEFT:
			img = figure[2];
			break;
		case RIGHT:
		case LEFT:
			img = figure[1];
			break;
		case DOWNRIGHT:
		case DOWNLEFT:
			img = figure[0];
			break;
		default:
			System.out.println("drawingError : illegal direction");
		}
		switch(dir) {
		case UPRIGHT:
		case RIGHT:
		case DOWNRIGHT:
			break;
		case UPLEFT:
		case LEFT:
		case DOWNLEFT:
			at = AffineTransform.getScaleInstance(-1, 1);
			at.translate(-img.getWidth(), 0);
			img = (new AffineTransformOp(at, null)).filter(img, null);
			break;
		default:
			System.err.println("drawingError : illegal direction");
		}
		g.drawImage(img, 0, 0, null);
		drawDamage(g, 0, 0);

		g.dispose();

		if(face.getWidth() < map.hexW +5)
			face = img;
	}


	void drawDamage(Graphics g, double x, double y) {
		if(damagedFlag) {
			g.drawImage(damageImg, (int)x, (int)y - map.hexH * 1/4, null);
			damagedFlag = false;
		}
	}

	public void initStatus(Capability capa) {
		status = new UnitStatus(capa);

		for (int i = 0; i < 3; i++) {
			String grp = capa.figure + (i + 1);
			sp.addGrp(grp);
			figure[i] = sp.getGrp(grp);
		}

		if(capa.face.equals(""))
			face = figure[0];
		else {
			sp.addGrp(capa.face);
			face = sp.getGrp(capa.face);
		}

	}

	public String getName() {
		return status == null ? super.toString() : status.capa.name + (unitNumber == null ? "" : "::" + unitNumber);
	}

	public void setDir(Direction dir) {
		this.dir = dir;
	}

	public Direction getDir() {
		return dir;
	}

	public void timeCourse() {
		status.timeCourse();
		actionable = status.isActionable();
	}

	public boolean isActionable() {
		return actionable;
	}

	public void setActionable(boolean actionable) {
		this.actionable = actionable;

		if(!actionable) {
			cursor.destructor();
			cursor = null;
		}
	}

	public boolean isMovable() {
		return movable;
	}

	public boolean isAttackable() {
		return attackable;
	}

	public MapSearch initMS(MapSearch ms, boolean practical) {
		ms.setCurrentPos(current);

		for(Unit[] units : map.unitMap)
			for(Unit unit : units)
				if(unit != null && (practical || unit.getType() != type)) {
					ms.plotObject(unit.getPos().x, unit.getPos().y);

					boolean enemy = false;
					for(UnitType uta : unit.getType().attackable(ActionType.ATTACK))
						enemy = enemy || uta == type;

					if(enemy)
						for(Coord<Integer> c : map.neighbors(unit.getPos().x, unit.getPos().y))
							if(c != null)
								ms.plotZOC(c);
				}

		return ms;
	}

	public boolean[][] getMovableArea() {
		return getMovableArea(false);
	}

	public boolean[][] getMovableArea(boolean practical) {
		boolean[][] availables = new boolean[map.hex[0].length][map.hex.length];

		ms = initMS(new MapSearch(map.hex), practical);

		for(int y = 0; y < map.hex.length; y++)
			for(int x = 0; x < map.hex[y].length; x++)
				availables[x][y] = ms.search(new Coord<Integer>(x, y), status.capa.move, status.capa.jump);

		return availables;
	}

	public void setCurrentAction(Action action) {
		currentAction = action;
	}

	public Action getCurrentAction() {
		return currentAction;
	}

	public boolean[][] getAttackableArea() {
		return getAttackableArea(current, false);
	}

	public boolean[][] getAttackableArea(Coord<Integer> current, boolean virtual) {
		return getAttackableArea(current, currentAction, virtual);
	}

	public boolean[][] getAttackableArea(Coord<Integer> current, Action act, boolean virtual) {
		//boolean[][] availables = new boolean[map.hex[0].length][map.hex.length];

		MapSearch ms = new MapSearch(map.hex);
		if(!virtual)
			this.ms = ms;
		ms.setCurrentPos(current);

		for(Unit[] units : map.unitMap)
			for(Unit unit : units)
				if(unit != null)
					ms.plotObject(unit.getPos().x, unit.getPos().y);
		//for(int y = 0; y < map.hex.length; y++)
		//	for(int x = 0; x < map.hex[y].length; x++)
		return ms.search(act.range, null);

		//return availables;
	}

	public boolean[][] getAreaAttackableArea(Coord<Integer> current) {
		MapSearch ms = new MapSearch(map.hex);
		ms.setCurrentPos(current);

		for(Unit[] units : map.unitMap)
			for(Unit unit : units)
				if(unit != null)
					ms.plotObject(unit.getPos().x, unit.getPos().y);

		return ms.search(currentAction.area, this.current);
	}

	public boolean[][] moveAndAttackableArea() {
		return moveAndAttackableArea(ActionType.ATTACK, true);
	}
	public boolean[][] moveAndAttackableArea(ActionType at, boolean areaAttack) {
		boolean[][] area = map.newBoolArea();
		Action ca = currentAction;

		List<Action> actList = status.usableActionList();

		for(Iterator<Action> i = actList.iterator(); i.hasNext();)
			if(i.next().actType != at)
				i.remove();

		boolean[][] movable = getMovableArea();
		Coord<Integer> m = new Coord<Integer>(0, 0);
		for(m.x = 0; m.x < movable.length; m.x++)
			for(m.y = 0; m.y < movable[m.x].length; m.y++){
				if(!movable[m.x][m.y])
					continue;

				for(Action act : actList) {
					//boolean[][] aarea = areaAttack ? attackAndAreaAttackableArea(m, act) : getAttackableArea(m, act, true);
					area = or(area, attackAndAreaAttackableArea(m, act));
				}
			}
		currentAction = ca;

		return area;
	}

	public boolean[][] attackAndAreaAttackableArea(Action act) {
		return attackAndAreaAttackableArea(current, act);
	}

	public boolean[][] attackAndAreaAttackableArea(Coord<Integer> current, Action act) {
		currentAction = act;
		boolean[][] attackable = getAttackableArea(current, true);

		boolean[][] areaAttack = map.newBoolArea();
		Coord<Integer> a = new Coord<Integer>(0, 0);
		for(a.x = 0; a.x < attackable.length; a.x++)
			for(a.y = 0; a.y < attackable[a.x].length; a.y++)
				if(attackable[a.x][a.y])
					areaAttack = or(areaAttack, getAreaAttackableArea(a));

		return areaAttack;
	}

	private static boolean[][] or(boolean[][] b1, boolean[][] b2) {
		for(int x = 0; x < b1.length; x++)
			for(int y = 0; y < b1[x].length; y++)
				b1[x][y] = b1[x][y] || b2[x][y];

		return b1;
	}

	boolean isBehind(int x, int y) {
		return isBehind(new Coord<Integer>(x, y));
	}

	boolean isBehind(Coord<Integer> next) {
		return dir.isBehind(current, next);
	}

	public void setAction() {
		mode = Mode.IDLE;
		gom.setUIF(this);
		//map.current = this;
		movable = attackable = true;
	}

	public void setMove(Coord<Integer> c) {
		setMove(c.x, c.y);
	}
	public void setMove(int x, int y) {
		//target = new Cood<Integer>(x, y);
		movable = false;
		previous.assign(current);
		dirPrevious = dir;
		this.way = ms.getWay(x, y);
		mode = Mode.MOVE;
		gom.setUIF(this);
	}

	public void returnPrevious() {
		current.assign(previous);
		dir = dirPrevious;
		movable = true;
	}

	public void setAttack(int x, int y) {
		attackable = false;
		actionable = false;
		target = new Coord<Integer>(x, y);
		mode = Mode.ATTACK;
		//currentAction = status.attack;
		gom.setUIF(this);
	}

	// �����̃^�[���ł͌Ă΂�Ȃ�
	public Damage calcDamage(Damage damage) {
		int c = isBehind(map.current.getPos()) ? 2 : 1;
		//System.out.println(c);
		return damage.effect(c).effect(map.hex[current.y][current.x].fd.damageEffect).hitEffect(c > 1 ? 1.5 : 1)
		.hitEffect(1 + Math.atan(map.current.getStatus().hit / (double)status.flee) / Math.PI/2);
	}

	void enemyDamage(Damage damage) {
		showDamage(calcDamage(damage));
	}

	void showDamage(Damage damage) {
		damagedFlag = status.damaging(damage);

		ShowDamage sd = (ShowDamage)gom.createMapObject(map, ShowDamage.class, current.x, current.y);
		sd.setDamage(damage, damagedFlag);

		damagedFlag = damagedFlag && damage.hp > 0;

		if(damagedFlag)
			map.current.damageReport(damage);
	}

	void damageReport(Damage damage) {
		status.endamage(damage);
	}

	// ShowDamage����Ă΂��ׂ�
	public void damaging() {

		if(!status.isAlive()) {
			destructor();
			/*gom.delGO(plnNo);// destructor();
			gom.removeUIF(this);
			gom.removeMotionable(this);
			map.destroyUnit(this);*/
		}

	}

	// Map����W�l�ŕԂ��B
	public Coord<Integer> getPos() {
		return current;
	}

	public UnitType getType() {
		return type;
	}

	public UnitStatus getStatus() {
		return status;
	}

	int count = 0;
	@Override
	public void action() {
		switch(mode) {
		case IDLE:
			gom.removeUIF();

			//if(status.isAlive()) {
			actionable = true;
			map.current = this;
			cursor = gom.createMapObject(map, SelectCursor.class, current.x, current.y);
			//}
			break;
		case MOVE:
			//if(current.x == target.x && current.y == target.y) {
			if(count == way.length) {
				count = 0;
				mode = Mode.IDLE;
				gom.removeUIF();

				return;
			}

			if(count >= 0) {
				dir = null;
				for(Direction d : Direction.values())
					if(d.isFront(current, way[count]))
						dir = d;
				//else
				//	System.out.println("Unit Error : illegal direction");
			}

			current = way[count];
			count++;
			//sp.setPos(plnNo, getHexCood(current.x, current.y));
			break;
		case ATTACK:
			mode = Mode.IDLE;
			gom.removeUIF();
			currentAction.use(status);
			gom.createMapObject(map, currentAction.effect, target.x, target.y);
			//currentAction = null;
			break;
		}
	}

}
