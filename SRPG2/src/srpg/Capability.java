package srpg;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import srpg.map.obj.HomingAmuletAE;
import srpg.map.obj.IllusionLaserAE;
import srpg.map.obj.MasterSparkAE;
import srpg.map.obj.MusoufuuinAE;
import srpg.map.obj.NormalAE;

public class Capability {
	public final String name;
	public final String face;
	public final String figure;

	public final int level;	// 未実装
	private int exp;	// 未実装

	public final int strength;
	public final int agility;
	public final int vitality;
	public final int intellect;
	public final int dexterity;
	public final int luck;

	public final int move;
	public final int jump;

	public final int toughness;
	public final int uplift;

	public Action attack;
	public Action[] skill;
	public Item[] item;

	private Capability(String name, String face, String figure,
			int level,
			int strength, int agility, int vitality, int intellect, int dexterity, int luck,
			int touhness, int uplift,
			int move, int jump) {
		//Action attack, Action[] skill, Item[] item) {
		this.name = name;
		this.face = face;
		this.figure = figure;

		this.level = level;

		this.strength = strength;
		this.agility = agility;
		this.vitality = vitality;
		this.intellect = intellect;
		this.dexterity = dexterity;
		this.luck = luck;

		this.toughness = touhness;
		this.uplift = uplift;

		this.move = move;
		this.jump = jump;
	}

	private void initActions(Action attack, Action[] skill, Item[] item) {
		this.attack = attack;
		this.skill = skill;
		this.item = item;
	}

	@Override
	public String toString() {
		return name;
	}

	public List<Action> actionList() {
		List<Action> actList = new LinkedList<Action>();
		actList.add(attack);
		if(skill != null)
			actList.addAll(Arrays.asList(skill));
		if(item != null)
			actList.addAll(Arrays.asList(item));

		return actList;
	}

	static Capability newFFairy() {
		Capability c = new Capability("妖精青", "", "m_fairyb",
				1,
				18, 17, 13, 10, 10, 12,
				10, 30, 5, 2);

		c.initActions(new Action("通常攻撃",
				new Range(RangeType.RAY, 0, 4, 0, 5),
				Range.SIMPLE,
				new Damage(c.strength, 0, 100, 0.9), ActionType.ATTACK,
				new Damage(0, 0, 300, 1.0), NormalAE.class),
				new Action[]{
			new Spell("3WAY",
					new Range(RangeType.RAY, 0, 6, 0, 6),
					new Range(RangeType.ARCH, 0, 1, 0, 2), 
					new Damage(c.strength*3/2, 0, 150, 1.0), ActionType.ATTACK,
					new Damage(0, 4, 300, 1.0), HomingAmuletAE.class),
		}, 
		new Item[]{Item.newCureHP(1), Item.newCureSP(1)});

		return c;

	}

	static Capability newReimu() {
		Capability c = new Capability("霊夢", "f_reimu", "m_reimu",
				1,
				17, 20, 17, 18, 24, 22,
				25, 15, 5, 2);

		c.initActions(new Action("通常攻撃",
				new Range(RangeType.RAY, 0, 4, 0, 5),
				Range.SIMPLE,
				new Damage(c.strength, 0, 100, 0.9), ActionType.ATTACK,
				new Damage(0, 0, 300, 1.0), NormalAE.class),

				new Action[]{
			new Action("ホーミングアミュレット",
					new Range(RangeType.RAY, 2, 6, 2, 6),
					new Range(RangeType.ARCH, 0, 1, 0, 2), 
					new Damage(c.strength*3/2, 0, 150, 1.0), ActionType.ATTACK,
					new Damage(0, 14, 400, 1.0), HomingAmuletAE.class),
			new Spell("夢想封印",
					new Range(RangeType.ARCH, 0, 2, 0, 3),
					new Range(RangeType.ARCH, 0, 6, 0, 4),
					new Damage(c.strength*7/4, 0, 700, 1.0), ActionType.ATTACK,
					new Damage(0, 12, 1000, 1.0), MusoufuuinAE.class)},

				new Item[]{Item.newCureHP(2)});
		
		return c;
	}
	
	static Capability newMarisa() {
		Capability c = new Capability("魔理沙", "f_marisa", "m_marisa",
				1,
				24, 25, 14, 18, 16, 20,
				25, 15, 5, 2);

		c.initActions(new Action("通常攻撃",
				new Range(RangeType.RAY, 0, 4, 0, 5),
				Range.SIMPLE,
				new Damage(c.strength, 0, 100, 0.9), ActionType.ATTACK,
				new Damage(0, 0, 300, 1.0), NormalAE.class),

				new Action[]{
			new Action("イリュージョンレーザー",
					new Range(RangeType.PIERCE, 5, 8, 5, 8),
					new Range(RangeType.INTERVAL, 0, 0, 0, 0),
					new Damage(c.strength*2, 0, 250, 0.9), ActionType.ALL,
					new Damage(0, 16, 300, 1.0), IllusionLaserAE.class),
			new Spell("マスタースパーク",
					new Range(RangeType.PIERCE, 6, 7, 6, 9),
					new Range(RangeType.INTERVAL, 0, 4, 0, 2),
					new Damage(c.strength*9/4, 0, 700, 1.0), ActionType.ALL,
					new Damage(0, 12, 1000, 1.0), MasterSparkAE.class)},

				new Item[]{Item.newCureSP(2)});
		
		return c;
	}


	static Capability newEFairy() {
		Capability c = new Capability("妖精赤", "", "m_fairy",
				10,
				16, 13, 10, 5, 8, 15,
				20, 20, 4, 2);
		
		c.initActions(new Action("通常攻撃",
				new Range(RangeType.RAY, 0, 4, 0, 5),
				Range.SIMPLE,
				new Damage(c.strength, 0, 100, 0.8), ActionType.ATTACK,
				new Damage(0, 0, 300, 1.0), NormalAE.class),

				new Action[]{
			new Spell("3WAY",
					new Range(RangeType.RAY, 0, 6, 0, 6),
					new Range(RangeType.ARCH, 0, 1, 0, 2), 
					new Damage(c.strength*5/4, 0, 150, 1.0), ActionType.ATTACK,
					new Damage(0, 8, 300, 1.0), HomingAmuletAE.class),
		},

		new Item[]{Item.newCureHP(1), Item.newCureSP(1)});
		
		return c;
	}

	static Capability newEFairySniper() {
		Capability c = new Capability("妖精赤(長射程)", "", "m_fairy",
				10,
				16, 10, 8, 7, 6, 15,
				20, 20, 4, 2);

		c.initActions(new Action("通常攻撃", new Range(RangeType.ARCH, 0, 6, 0, 7),
				Range.SIMPLE,
				new Damage(c.strength, 0, 150, 0.6), ActionType.ATTACK,
				new Damage(0, 0, 400, 1.0), NormalAE.class),

				new Action[]{
			new Action("ロングシュート",
					new Range(RangeType.RAY, 2, 6, 2, 6),
					new Range(RangeType.ARCH, 0, 1, 0, 2), 
					new Damage(c.strength*2, 0, 100, 0.8), ActionType.ATTACK,
					new Damage(0, 20, 800, 1.0), HomingAmuletAE.class),
			new Spell("5WAY",
					new Range(RangeType.ARCH, 0, 8, 0, 8),
					new Range(RangeType.ARCH, 0, 1, 0, 2), 
					new Damage(c.strength*5/4, 0, 150, 0.9), ActionType.ATTACK,
					new Damage(0, 16, 600, 1.0), HomingAmuletAE.class),

		},
		new Item[]{});
		
		return c;
	}
}
