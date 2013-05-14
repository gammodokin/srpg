package srpg.map;

import game.Draw;
import game.GameObject;
import game.GameObjectManager;
import game.Motionable;
import game.Plane;
import game.Sprite;
import game.UserIF;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import myutil.Coord;
import myutil.Queue;
import srpg.Field;
import srpg.FieldData;
import srpg.Player;
import srpg.SRPGGameObjectManager;
import srpg.UnitStatus;
import srpg.map.obj.Enemy;
import srpg.map.obj.Friend;
import srpg.map.obj.HorizonMap;
import srpg.map.obj.Obstacle;
import srpg.map.obj.Unit;
import srpg.map.ui.TurnCounter;
import srpg.map.ui.TurnInfo;
import srpg.map.ui.info.ActionInfo;
import srpg.map.ui.info.GeoInfo;
import srpg.map.ui.info.TargetInfo;
import srpg.map.ui.info.UnitInfo;
import srpg.screen.Result;


public class StageMap extends UserIF implements Motionable{

	final String designedFont = "PMingLiU";

	private final int hexSize = 48;
	public final double depthRate = 12/16d;
	public final int hexW = hexSize, hexH = (int)Math.round(hexSize * depthRate);
	public final int hexHeight = hexH/4;

	private boolean destract = false;

	private int[][] hexNum, height;
	public Field[][] hex;
	private final int OBSTACLE = -10;
	public static final double YZRate = 1/(double)0xff;
	public Unit unitMap[][];
	public Unit current;
	private Coord<Integer> cursored;

	private TurnInfo ti;
	private Queue<Unit> actionQ = new Queue<Unit>();
	private final int actionUnitCount = 8;

	private TurnCounter tc;
	private int turnCount = 0, turnMax = 0;

	private List<GameObject> gos;

	private Coord<Double> moveTarget;

	private Player player;
	private int income = 0;

	public StageMap(GameObjectManager ggom, Sprite sp, int plnNo, int x, int y) {
		super(ggom, sp, plnNo, x, y);

		cursored = new Coord<Integer>(0, 0);

		sp.setPos(plnNo, MapDrawOrder.MAP.z());
		sp.setDraw(plnNo, new Draw() {
			BufferedImage bi = null;
			public void drawing(Graphics g, Plane pln) {
				if(bi == null) {
					bi = new BufferedImage(gom.width, gom.height, BufferedImage.TYPE_INT_RGB);
					Graphics2D g2 = bi.createGraphics();
					g2.setPaint(new GradientPaint(0, 5, new Color(83, 170, 234),
							0, gom.height - 60, new Color(244, 246, 231)));
					g2.fill(new Rectangle2D.Double(0, 0, gom.width, gom.height));
					g2.dispose();
				}

				g.drawImage(bi, 0, 0, null);

				//g.setColor(Color.WHITE);
				//g.fillRect(0, 0, gom.width, gom.height);
			}
		});

		gom.addMotionable(this);

		ti = createMapObject(TurnInfo.class, 0, 0);
		ti.setElementCount(actionUnitCount);

		tc = createMapObject(TurnCounter.class, 0, 0);
		tc.setDenom(turnMax);

		gos = new LinkedList<GameObject>();
		gos.add(createMapObject(UnitInfo.class, 0, 0));
		gos.add(createMapObject(GeoInfo.class, 0, 0));
		gos.add(createMapObject(ActionInfo.class, 0, 0));
		gos.add(createMapObject(TargetInfo.class, 0, 0));
	}

	public void init(String file, Player player) {

		this.player = player;

		Direction friend = null, enemy = null;

		Sprite.BGM[] stageBGM = new Sprite.BGM[]{null, Sprite.BGM.STAGE1, Sprite.BGM.STAGE2, Sprite.BGM.STAGE3, };

		StageLoader sd = new StageLoader(file);
		// �ｽ�ｽQ�ｽ�ｽ�ｽ�ｽ�ｽv�ｽ�ｽ�ｽb�ｽg
		int[][] field = sd.getStage(0), height = sd.getStage(1), units = sd.getStage(2), direction = sd.getStage(3);
		int bgm = sd.getStage(4)[0][0];

		sp.playBGM(stageBGM[bgm]);

		for(int y = 0; y < field.length; y++)
			for(int x = 0; x < field[y].length; x++)
				if(units[y][x] < 0)
					field[y][x] = units[y][x];

		this.height = height;
		hexNum = field;

		UnitType.FRIEND.setInitDir(friend);
		UnitType.ENEMY.setInitDir(enemy);

		hex = new Field[hexNum.length][hexNum[0].length];
		for(int yy = 0; yy < hexNum.length; yy++)
			for(int xx = 0; xx < hexNum[yy].length; xx++) {
				FieldData fd = null;
				switch(hexNum[yy][xx]){
				case -10:
					fd = FieldData.obstacle;
					break;
				case 1:
					fd = FieldData.heichi;
					break;
				case 2:
					fd = FieldData.sougen;
					break;
				case 3:
					fd = FieldData.arechi;
					break;
				}
				hex[yy][xx] = new Field(fd, height[yy][xx], sp);
			}

		unitMap = new Unit[hexNum.length][hexNum[0].length];
		for(int hy = 0; hy < hexNum.length; hy++){
			gos.add(createMapObject(HorizonMap.class, 0, hy));
		}

		for(int hexY = 0; hexY < hexNum.length; hexY++)
			for(int hexX = 0; hexX < hexNum[hexY].length; hexX++)
				if(hexNum[hexY][hexX] <= OBSTACLE)
					gos.add(createMapObject(Obstacle.class, hexX, hexY));

		Direction[] dirs = Direction.values();
		for(int y = 0; y < units.length; y++)
			for(int x = 0; x < units[y].length; x++) {
				if(units[y][x] <= 0)
					continue;

				Unit unit;

				if(units[y][x] < 50) {
					unit = createMapObject(Friend.class, x, y);
					unit.initStatus(player.party.get(units[y][x] - 1));
				} else {
					unit = createMapObject(Enemy.class, x, y);
					UnitStatus.initEnemy(units[y][x], (Enemy)unit);
				}

				unit.setDir(dirs[direction[y][x]]);
			}
		denominateUnit();
	}

	private class StageLoader {

		private static final int stageLen = 5;
		private final int[][][] stage;

		private final String location = "/res/";
		StageLoader(String file) {
			stage = new int[stageLen][][];

			try {
				BufferedReader br = new BufferedReader(
						new InputStreamReader(getClass().getResourceAsStream(location + file)));

				List<Integer[]> bufY = new LinkedList<Integer[]>();
				String line;
				int i = 0;
				while ((line = br.readLine()) != null) {
					if(line.indexOf('*') == 0) {
						stage[i] = list2Array(bufY);
						bufY = new LinkedList<Integer[]>();
						i++;
						continue;
					}

					String[] nums = line.split("//", -1)[0].split("\t");

					List<Integer> bufX = new LinkedList<Integer>();
					for(String num : nums)
						if(num.length() > 0)
							bufX.add(Integer.parseInt(num.trim()));
					if(bufX.size() > 0)
						bufY.add(bufX.toArray(new Integer[0]));
				}
				br.close();

				stage[i] = list2Array(bufY);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private int[][] list2Array(List<Integer[]> l) {
			int[][] ret = new int[l.size()][l.get(0).length];
			int y = 0;
			for(Integer[] is : l) {
				int x = 0;
				for(int i : is) {
					ret[y][x] = i;
					x++;
				}
				y++;
			}
			return ret;
		}

		int[][] getStage(int i) {
			return stage[i];
		}
	}

	private Queue<Unit> unitQ = new Queue<Unit>();
	private Queue<Unit> actionableQ = new Queue<Unit>();
	@Override
	public void action() {

		unitQ.clear();
		for(Unit[] horizon : unitMap)
			for(Unit unit : horizon){
				if(unit == null)
					continue;

				unitQ.enqueue(unit);
			}

		for(Unit unit : unitQ)
			unit.getStatus().initVirtual();

		actionQ.clear();
		actionQ.enqueue(actionableQ);
		while(actionQ.size() < actionUnitCount) {
			for(Unit unit : unitQ) {
				if(unit.getStatus().timeCourse(true))
					actionQ.enqueue(unit);
			}
		}
		ti.setUnits(actionQ.toArray(new Unit[0]));

		while(actionableQ.isEmpty()) {
			for(Unit unit : unitQ) {
				unit.timeCourse();
				if(unit.isActionable()) {
					actionableQ.enqueue(unit);
				}
			}
			turnCount++;
		}

		tc.setCount(turnCount);

		//System.out.println("actionableQ" + actionableQ);

		Unit unit = actionableQ.dequeue();
		if(unit.getStatus().isAlive())
			unit.setAction();

	}

	public void motion() {
		if(moveTarget != null) {
			Coord<Double> current, move;

			current = sp.getPos(plnNo);

			double distX = moveTarget.x - current.x, distY = moveTarget.y - current.y;
			move = new Coord<Double>(distX * 0.3, distY * 0.3);

			sp.setMov(plnNo, move);
			current = sp.getPos(plnNo);
			x = (int)Math.round(current.x);
			y = (int)Math.round(current.y);

			if(Math.abs(moveTarget.x - current.x) < 1 && Math.abs(moveTarget.y - current.y) < 1)
				moveTarget = null;
		}
	}

	public int processPriority() {
		return 1;
	}

	@Override
	public void destructor() {
		destract = true;

		ti.destructor();
		tc.destructor();

		for(Unit[] units : unitMap)
			for(Unit unit : units)
				if(unit != null)
					unit.destructor();

		for(GameObject go : gos)
			go.destructor();

		super.destructor();
		gom.removeMotionable(this);
	}

	public void setMoney(int m) {
		income += m;
	}

	public boolean isDestracted() {
		return destract;
	}

	private boolean fullDestroyed(UnitType type) {
		for(Unit[] units : unitMap)
			for(Unit unit : units)
				if(unit != null && unit.type == type)
					return false;
		return true;
	}

	private void denominateUnit() {
		HashMap<String, Integer> hm = new HashMap<String, Integer>();
		for(Unit[] units : unitMap)
			for(Unit unit : units)
				if(unit != null) {
					if(!hm.containsKey(unit.getStatus().capa.name))
					hm.put(unit.getStatus().capa.name, 0);
					hm.put(unit.getStatus().capa.name, hm.get(unit.getStatus().capa.name) + 1);
				}

		HashMap<String, Integer> hm2 = new HashMap<String, Integer>();
		for(Unit[] units : unitMap)
			for(Unit unit : units){
				if(unit == null)
					continue;

				if(hm.get(unit.getStatus().capa.name) > 1) {
					if(!hm2.containsKey(unit.getStatus().capa.name))
						hm2.put(unit.getStatus().capa.name, 0);
					unit.unitNumber = "" + (char)('A' + hm2.get(unit.getStatus().capa.name));
					hm2.put(unit.getStatus().capa.name, hm2.get(unit.getStatus().capa.name) + 1);
				}
			}

	}

	public <E extends GameObject> E createMapObject(Class<E> go, int x, int y) {
		return gom.addGO(SRPGGameObjectManager.class, go, x, y, this, null);
	}

	public void moveMap(double x, double y) {
		moveTarget = new Coord<Double>(this.x + x, this.y + y);
	}

	public void setCursorPos(Coord<Integer> pos) {
		setCursorPos(pos.x, pos.y);
	}
	public void setCursorPos(int x, int y) {
		cursored.x = x;
		cursored.y = y;
	}

	public Coord<Integer> getCursored() {
		return cursored;
	}

	public void setUnit(int x, int y, Unit unit) {
		if (x < 0 || unitMap[0].length <= x || y < 0 || unitMap.length <= y) {
			System.out.println("error : out of map in setUnit()");
			return;
		}

		unitMap[y][x] = unit;

		for(int i = 0; i < unitMap.length; i++)
			for(int j = 0; j < unitMap[i].length; j++)
				if(i == y && j == x)
					continue;
				else if(unitMap[i][j] == unit)
					unitMap[i][j] = null;
	}

	public void destroyUnit(Unit unit) {
		unitMap[unit.getPos().y][unit.getPos().x] = null;
		while(actionQ.remove(unit));

		if(fullDestroyed(unit.type)) {
			destructor();

			Result r = null;
			if(unit.type == UnitType.ENEMY) {
				r = gom.addGO(Result.class, 0, 0);
				player.capital += income;
				r.setViewIncome(income);
			} else if(unit.type == UnitType.FRIEND)
				r = gom.addGO(Result.class, -1, 0);
		}
	}

	void drawHex(Graphics g, Coord<Double> coord, int width, int height) {
		drawHex(g, coord.x.intValue(), coord.y.intValue(), width, height);
	}
	public void drawHex(Graphics g, int x, int y, int width, int height) {
		Polygon po = makeHex(width, height);
		po.translate(x, y);
		g.drawPolygon(po);
	}

	void fillHex(Graphics g, Coord<Double> coord, int width, int height) {
		fillHex(g, coord.x.intValue(), coord.y.intValue(), width, height);
	}
	public void fillHex(Graphics g, int x, int y, int width, int height) {
		fillHex(g, x, y, width, height, 1.0f);
	}
	public void fillHex(Graphics g, int x, int y, int width, int height, float alpha) {
		Graphics2D g2D = (Graphics2D)g;

		Polygon po = makeHex(width, height);
		po.translate(x, y);

		g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		g2D.fill(po);
		g2D.setPaintMode();
	}

	private Polygon makeHex(int width, int height) {
		return new Polygon(new int[] { width / 2, width, width, width / 2, 0,
				0 }, new int[] { 0, height / 4, Math.round(height * 3 / 4f), height,
				Math.round(height * 3 / 4f), height / 4 }, 6);
	}

	public Coord<Double> getHexCoord(Coord<?> c) {
		return getHexCoord(c.x.doubleValue(), c.y.doubleValue());
	}
	public Coord<Double> getHexCoord(double x, double y) {
		return getHexCoord(x, y, true);
	}

	Coord<Double> getHexCoord(Coord<?> c, boolean translate) {
		return getHexCoord(c.x.doubleValue(), c.y.doubleValue(), translate);
	}
	public Coord<Double> getHexCoord(double hexX, double hexY, boolean translate) {
		double x = hexX * hexW + (hexY % 2 == 0 ? 0 : hexW / 2);
		double y = hexY * hexH * 3 / 4f;
		if(translate) {
			x += this.x;
			y += this.y;
		}

		y -= height[(int)hexY][(int)hexX] * hexHeight;

		return new Coord<Double>(x, y);
	}

	boolean isInsideMap(Coord<Integer> c) {
		return isInsideMap(c.x, c.y);
	}

	boolean isInsideMap(int x, int y) {
		return 0 <= x && x < hexNum[0].length && 0 <= y && y < hexNum.length;
	}

	// �ｽ}�ｽb�ｽv�ｽﾌ外�ｽﾈゑｿｽnull
	public Coord<Integer>[] neighbors(Coord<Integer> c) {
		return neighbors(c.x, c.y);
	}

	@SuppressWarnings("unchecked")
	public Coord<Integer>[] neighbors(int x, int y) {
		int neighborX = (y % 2 == 0 ? x - 1 : x + 1);
		Coord<Integer>[] all = (Coord<Integer>[])new Coord<?>[]{
				new Coord<Integer>(x + 1, y),
				new Coord<Integer>(x - 1, y),
				new Coord<Integer>(x, y + 1),
				new Coord<Integer>(x, y - 1),
				new Coord<Integer>(neighborX, y + 1),
				new Coord<Integer>(neighborX, y - 1),};
		for(int i = 0; i < all.length; i++)
			if(!isInsideMap(all[i]))
				all[i] = null;
		return all;
	}

	public ArrayList<Unit> searchUnits(boolean[][] area, UnitType ut) {
		ArrayList<Unit> sUnits = new ArrayList<Unit>();

		for(Unit[] units : unitMap)
			for(Unit unit : units) {
				if(unit == null)
					continue;

				if(unit.getType() == ut
						&& area[unit.getPos().x][unit.getPos().y]) {
					sUnits.add(unit);
				}
			}

		return sUnits;
	}

	public boolean[][] newBoolArea() {
		return new boolean[hex[0].length][hex.length];
	}

	public static Coord<Integer> toVectorCoord(Coord<Integer> c) {
		return new Coord<Integer>(toVectorCoord(c.x, c.y), c.y);
	}
	// x�ｽ�ｽ�ｽW�ｽ�ｽﾔゑｿｽ
	static int toVectorCoord(int x, int y) {
		return x - y / 2;
	}

}