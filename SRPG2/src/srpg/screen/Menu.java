package srpg.screen;

import game.Draw;
import game.DrawOrder;
import game.GameObject;
import game.GameObjectManager;
import game.Plane;
import game.Sprite;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

import myutil.Coord;

public abstract class Menu extends GameObject {

	protected final Menu menu;

	protected Coord<Integer> offset;
	protected int width, height;
	protected String[] menuStr;
	protected boolean[] activeMenu;
	protected int commandHeight;
	protected Class<? extends MenuCursor> menuCursor;
	protected boolean repaint = false;

	protected int span;

	public Menu(GameObjectManager gom, Sprite sp, int plnNo, int x, int y) {
		super(gom, sp, plnNo, x, y);

		menu = this;

		//sp.setPos(plnNo, StageDrawOrder.MENU.z());
//		sp.setDraw(plnNo, new Draw() {
//
//			//int offset = 30;
//			int x, y;
//			public void drawing(Graphics g, Plane pln) {
//				if(pln.img == null || repaint) {
//					repaint = false;
//					if(pln.img == null)
//						pln.initImage(width + 1, height + 1);
//					Graphics gbg = pln.getGraphics();
//					x = 0;
//					y = 0;
//
//					//g.setColor(new Color(0x99ccccff, true));
//					//g.fillRoundRect(x, y, width, height, 30, 10);
//					//Sprite.drawDesignedRect(x, y, width, height, gbg);
//					drawBGRect(x, y, width, height, gbg);
//					for(int i = 0; i < activeMenu.length; i++) {
//						if(activeMenu[i])
//							gbg.setColor(Color.BLACK);
//						else
//							gbg.setColor(Color.GRAY);
//						Sprite.drawShadedString(menuStr[i],
//								x + offset.x + 30,
//								y + offset.y + (span + commandHeight) * (i + 1), gbg);
//					}
//
//					gbg.dispose();
//				}
//			}
//
//		});

		offset = new Coord<Integer>(0, 0);
	}
	
	private void initImg() {
		sp.initImg(plnNo, width + 1, height + 1);
		repaint = true;
		drawImg();
	}
	
	private void drawImg() {
		if(!repaint)
			return;
		
		repaint = false;
		
		sp.setTransparent(plnNo);
		
		Graphics gbg = sp.createGraphics(plnNo);
		x = 0;
		y = 0;

		drawBGRect(x, y, width, height, gbg);
		for(int i = 0; i < activeMenu.length; i++) {
			if(activeMenu[i])
				gbg.setColor(Color.BLACK);
			else
				gbg.setColor(Color.GRAY);
			Sprite.drawShadedString(menuStr[i],
					x + offset.x + 30,
					y + offset.y + (span + commandHeight) * (i + 1), gbg);
		}

		gbg.dispose();

	}

	protected void init() {
		span = (height - menuStr.length * commandHeight) / (menuStr.length + 1);

		createCursor(sp.getPos(plnNo));
		
		initImg();
	}

	abstract protected void createCursor(Coord<Double> current);

	abstract protected void drawBGRect(int x, int y, int width, int height, Graphics g);

	protected String[] arr2String(Object[] o) {
		String[] s = new String[o.length];
		for(int i = 0; i < o.length; i++)
			s[i] = o[i].toString();
		return s;
	}

	protected abstract class MenuCursor extends Cursor {

		protected int size = commandHeight + 4;
		protected Coord<Integer> current;

		public MenuCursor(GameObjectManager gom, Sprite sp, int plnNo, int x, int y) {
			super(gom, sp, plnNo, x, y);

			current = new Coord<Integer>(x, y);

			this.x = this.y = 0;

			sp.setPos(plnNo, DrawOrder.CURSOR.z());
			sp.setDraw(plnNo, new Draw() {

				int count = 0;
				int x, y;
				Polygon po;
				public void drawing(Graphics g, Plane pln) {

					g.setColor(new Color(0xaa, 0xff, 0xaa, 0xff - count * 16));

					x = pln.coord.x.intValue();
					y = pln.coord.y.intValue();
					po = new Polygon(new int[] {x, x + size ,x},
							new int[] {y, y + size/2, y + size}, 3);
					g.fillPolygon(po);

					g.setColor(new Color(0x44, 0x88, 0x44, 0xff - count * 16));
					g.drawPolygon(po);

					count++;
					count %= 0xff / 20;
				}

			});
			

			//setView(true);
		}

		@Override
		public void destructor() {
			super.destructor();
			menu.destructor();
		}

		protected void mainAction(){
			menu.drawImg();
			
			move = moveY;

			// 稀にマイナスになる可能性あり
			y = (y + menuStr.length * 10) % menuStr.length;

			if(selected && activeMenu[y])
				select = true;

			sp.setPos(plnNo, current.x, current.y + span * y + commandHeight * y);

			MCAction();
		}

		protected void setView(boolean view) {
			sp.setView(plnNo, view);
			sp.setView(menu.plnNo, view);
		}

		abstract protected void MCAction();
	}

}
