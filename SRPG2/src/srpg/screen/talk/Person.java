package srpg.screen.talk;

import game.Draw;
import game.GameObject;
import game.GameObjectManager;
import game.Plane;
import game.Sprite;

import java.awt.Graphics;

import myutil.Coord;

public class Person extends GameObject {

	enum Chara{
		REIMU("reimu", "óÏñ≤"), MARISA("marisa", "ñÇóùçπ");

		final String img;
		final String name;
		//Coord<Integer> pos;

		private Chara(String img, String name) {
			this.img = img;
			this.name = name;
		}
		/*
		void setPos(Coord<Integer> pos) {
			this.pos = pos;
		}*/

		@Override
		public String toString() {
			return name;
		}

		static Chara getByName(String name) {
			for(Chara chara : Chara.values())
				if(name.equals(chara.name))
					return chara;
			return null;
		}
	}

	enum Position {
		left(0.2), center(0.5), right(0.8);

		final double rate;

		Position(double rate) {
			this.rate = rate;
		}

		static Position getByName(String name) {
			for(Position pos : Position.values())
				if(name.equals(pos.toString()))
					return pos;
			return null;
		}

	}

	private Chara chara;
	private Coord<Integer> pos;

	public Person(GameObjectManager gom, Sprite sp, int plnNo, int x, int y) {
		super(gom, sp, plnNo, x, y);

		sp.setPos(plnNo, TalkDrawOrder.PERSON.z());

		sp.setDraw(plnNo, new Draw() {

			public void drawing(Graphics g, Plane pln) {/*
				g.setColor(Color.BLACK);
				g.drawString(chara.name, pln.coord.x.intValue(), pln.coord.y.intValue());
			*/}

		});

	}

	void setChara(Chara chara) {
		this.chara = chara;
		sp.addGrp(chara.img);
		sp.setImg(plnNo, chara.img);

//		sp.resizeImg(plnNo, 0, gom.height * 3/2);
	}

	void flip() {
		sp.flipImg(plnNo);
	}

	void setPos(Coord<Double> pos) {
		sp.setPos(plnNo, pos);
	}

	void setPos(Position pos) {
		sp.setPos(plnNo, gom.width * pos.rate - sp.getImgSize(plnNo).x / 2f, 0);
	}

	@Override
	public boolean equals(Object o) {
		return chara == ((Person)o).chara;
	}

	@Override
	public int hashCode() {
		return chara.hashCode();
	}

}
