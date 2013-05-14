package srpg.screen.talk;

import game.Draw;
import game.DrawOrder;
import game.GameObjectManager;
import game.Plane;
import game.Sprite;
import game.ZOrder;

import java.awt.Color;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import myutil.Coord;
import srpg.screen.Screen;

enum TalkDrawOrder implements ZOrder {
	BG, PERSON, TALK, QUESTION, Q_CURSOR;

	public double z() {
		return DrawOrder.TALK.z() + ordinal() / (double)0xff;
	}

}

public class Talk extends Screen {

	private String[] text;
	private final BufferedReader br;
	private String file = "talk1.txt";

	private String speaker = "";
	private final Hashtable<String, Person> persons;

	private final BackGround bg;

	public Talk(GameObjectManager gomm, Sprite sp, int plnNo, int x, int y) {
		super(gomm, sp, plnNo, x, y);

		sp.setPos(plnNo, TalkDrawOrder.TALK.z());
		sp.setPos(plnNo, 0, 0);

		sp.setDraw(plnNo, new Draw() {

			public void drawing(Graphics g, Plane pln) {

				Sprite.drawDesignedRect(0, gom.height * 3/4, gom.width, gom.height/4 + 10, g);

				g.setColor(Color.BLACK);

				Sprite.drawShadedString(speaker, 60, gom.height * 3/4 + 3, g);

				for(int i = 0; i < text.length; i++) {
					Sprite.drawShadedString(text[i], 50, gom.height * 3/4 + 35 + i * 30, g);
				}
			}

		});

		br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/res/" + file)));

		persons = new Hashtable<String, Person>();

		bg = gom.addGO(BackGround.class, 0, 0);

		text = nextText();
	}

	@Override
	public void destructor() {
		for(Person p : persons.values())
			p.destructor();
		bg.destructor();
		super.destructor();
	}

	@Override
	protected void mainAction() {
		if(selected) {
			text = nextText();

			if(text.length < 1)
				destructor();
		}
	}

	Func TALKING = new Func("talk") {
		@Override
		void proc(String[] args) {
			speaker = args[0];
		}
	}, IN = new Func("in") {
		@Override
		void proc(String[] args) {
			Person p;
			if(args.length > 2)
				p = gom.addGO(Person.class, Integer.parseInt(args[1]), Integer.parseInt(args[2]));
			else
				p = gom.addGO(Person.class, 0, 0);
			p.setChara(Person.Chara.getByName(args[0]));
			persons.put(args[0], p);

			if(args.length == 2)
				p.setPos(Person.Position.getByName(args[1]));
			/*
			if(args.length > 3 && args[3].equals("flip"))
				p.flip();*/
		}
	}, OUT = new Func("out") {
		@Override
		void proc(String[] args) {
			Person p = persons.remove(args[0]);
			if(p == null)
				System.out.println("out falure. no such person : " + args[0]);
			else
				p.destructor();
		}
	}, FLIP = new Func("flip") {
		@Override
		void proc(String[] args) {
			persons.get(args[0]).flip();
		}
	}, POS = new Func("pos") {
		@Override
		void proc(String[] args) {
			Person p = persons.get(args[0]);
			if(args.length > 2)
				p.setPos(new Coord<Double>((double)Integer.parseInt(args[1]), (double)Integer.parseInt(args[2])));
			else
				p.setPos(Person.Position.getByName(args[1]));
		}
	}, QUESTION = new Func("question") {
		@Override
		void proc(String[] args) {
			q = gom.addGO(Question.class, 0, 0);
			boolean[] bs = new boolean[args.length];
			Arrays.fill(bs, true);
			q.init(args, bs);
		}
	}, ANS = new Func("ans") {
		@Override
		void proc(String[] args) {
			if(Integer.parseInt(args[0]) != q.getResult())
				skip = true;
		}
	}, BG = new Func("bg") {
		@Override
		void proc(String[] args) {
			bg.setImg(args[0]);
		}
	};

	Func[] funcs = new Func[]{TALKING, IN, OUT, FLIP, POS, QUESTION, ANS, BG, };

	private abstract class Func {

		String word;

		private Func(String word) {
			this.word = word;
		}

		abstract void proc(String[] args);
	}


	boolean end = false;
	boolean skip = false;
	Question q = null;
	private String[] nextText() {
		List<String> texts = new LinkedList<String>();

		// 直前に\endtalkがあったら読み込み終了
		if(end)
			return texts.toArray(new String[0]);

		try {
			String text;
			ReadLine :
			while(texts.size() < 3 && (text = br.readLine()) != null) {
				String[] tokens = text.split("//", -1)[0].split(" ", -1);

				for(int i = 0; i < tokens.length; i++)
					tokens[i] = tokens[i].trim();

				String funcStr = tokens[0];
				String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);

				if(funcStr.equals("\\" + "endans")) {
					skip = false;
					continue;
				} else if(skip)
					continue;

				if(funcStr.equals("\\" + "endtalk")) {
					end = true;
					break;
				} else if(funcStr.equals("\\" + "next")) {
					// ファイルが終端に達していなければさらに次のテキスト読む
					br.mark(64);
					int r = br.read();
					br.reset();

					if(r > -1 && texts.size() < 1)
							texts = Arrays.asList(nextText());
					break;
				}

				for(Func func : funcs) {
					if(funcStr.equals("\\" + func.word)) {
						func.proc(args);
						continue ReadLine;
					}
				}

				if(tokens[0].equals(""))
					continue;

				texts.add(tokens[0]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return texts.toArray(new String[0]);
	}

}
