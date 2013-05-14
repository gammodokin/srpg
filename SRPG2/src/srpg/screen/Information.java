package srpg.screen;

import game.ChangeTracker;
import game.GameObject;
import game.GameObjectManager;
import game.Motionable;
import game.Sprite;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import myutil.Coord;

public abstract class Information extends GameObject implements Motionable {

	protected int width;
	protected int height;
	protected String title;
	protected StrData[] sds;

	protected Coord<Integer> current;

	protected ChangeTracker<Coord<Integer>> ct = new ChangeTracker<Coord<Integer>>();

	public Information(GameObjectManager gom, Sprite sp, int plnNo, int x, int y) {
		super(gom, sp, plnNo, x, y);

//		sp.setDraw(plnNo,new Draw() {
//
//			public void drawing(Graphics g, Plane pln) {
//				if(sds == null) // 1ÉãÅ[Évñ⁄Ç…ï\é¶Ç≥ÇÍÇƒÇµÇ‹Ç§ÇÃÇâÒî
//					return;
//
//				if(ct.isChanged(current.clone())) {// || pln.img == null) {
////					pln.initImage(width + 1, height + 3 + 1);
//					Graphics gbg = pln.imgs.get(0).createGraphics();
//					int x = 0;
//					int y = 4;
//					//Sprite.drawDesignedRect(x, y, width, height, gbg);
//					drawBGRect(x, y, width, height, gbg);
//
//					gbg.setColor(Color.BLACK);
//					Sprite.drawShadedString(title, x + 20, y + 5, gbg);
//					for(StrData sd : sds) {
//						gbg.setColor(sd.c);
//						Sprite.drawShadedString(sd.str, x + sd.x, y + sd.y, gbg);
//					}
//
//					gbg.dispose();
//				}
//			}
//
//		});

		current = new Coord<Integer>(0, 0);

		gom.addMotionable(this);
	}

	@Override
	public void destructor() {
		gom.removeMotionable(this);
		super.destructor();
	}

	protected void init(int width, int height) {
		this.width = width;
		this.height = height;

		initImg();
	}

	BufferedImage bgRect;
	private void initImg() {
		sp.initImg(plnNo, width + 1, height + 3 + 1);

		bgRect = new BufferedImage(width + 1, height + 1, BufferedImage.TYPE_INT_ARGB);
		Sprite.setTransparent(bgRect);
		Graphics2D g = bgRect.createGraphics();
		drawBGRect(0, 0, width, height, g);
		g.dispose();
	}

	private void drawImg() {
		if(sds == null)
			return;

		sp.setTransparent(plnNo);

		int x = 0;
		int y = 4;

		Graphics2D g = sp.createGraphics(plnNo);

		g.drawImage(bgRect, 0, 3, null);

		g.setColor(Color.BLACK);
		Sprite.drawShadedString(title, x + 20, y + 5, g);
		for(StrData sd : sds) {
			g.setColor(sd.c);
			Sprite.drawShadedString(sd.str, x + sd.x, y + sd.y, g);
		}

		g.dispose();

	}

	abstract protected void drawBGRect(int x, int y, int width, int height, Graphics g);

	protected class StrData {

		public String str;
		int x, y;
		public Color c = Color.BLACK;

		public StrData(String str, int x, int y) {
			this.str = str;
			this.x = x;
			this.y = y;
		}

		@Override
		public String toString() {
			return str;
		}

	}

	public void motion() {
		ct.isChanged(current.clone());

		//current.assign(map.getCursored());
		update();
		mainMotion();

		if(ct.isChanged(current.clone()))
			drawImg();
	}

	protected abstract void update();

	public abstract void mainMotion();

	public int processPriority() {
		return -1;
	}

}
