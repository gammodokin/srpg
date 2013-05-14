package srpg.map.ui;

import game.Blink;
import game.Motionable;
import game.Sprite;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import srpg.SRPGGameObjectManager;
import srpg.map.Direction;
import srpg.map.MapObject;
import srpg.map.StageDrawOrder;
import srpg.map.StageMap;


public class DirectionSelect extends MapObject implements Motionable{

	private static StageMap m = null;
	DirectionSelect ds;
	
	public DirectionSelect(SRPGGameObjectManager gom, Sprite sp, int plnNo,
			int x, int y, StageMap m) {
		super(gom, sp, plnNo, x, y, m);
		DirectionSelect.m = m;
		ds = this;
		//sp.setPos(plnNo, map.getHexCoord(x, y));
		//motion();
		sp.setPos(plnNo, StageDrawOrder.MENU.z());
		initImg();
		//		sp.setDraw(plnNo, new Draw() {
//
//			public void drawing(Graphics g, Plane pln) {
//				if(pln.img == null) {
//					pln.initImage(map.hexW, map.hexW);
//					Graphics2D gbg = pln.getGraphics();
//					gbg.setColor(new Color(0xccccccff, true));
//					//gbg.drawRect(0, 0, map.hexW-1, map.hexW-1);
//					
//					for(Tri t : Tri.values()) {
//						t.draw(gbg);
//					}
//					
//					BufferedImage buf = pln.img;
//					pln.initImage(map.hexW, map.hexH);
//					gbg = pln.getGraphics();
//					gbg.drawImage(buf, AffineTransform.getScaleInstance(1, map.depthRate), null);
//					
//					gbg.dispose();
//				}
//			}
//
//		});
		
		gom.addMotionable(this);
		gom.createMapObject(map, DirCursor.class, x, y, this);
	}
	
	private void initImg() {
		sp.initImg(plnNo, map.hexW, map.hexH);
		
		BufferedImage buf = new BufferedImage(map.hexW, map.hexW, BufferedImage.TYPE_INT_ARGB);
		Sprite.setTransparent(buf);
		Graphics2D bufg = buf.createGraphics();
		
		bufg.setColor(new Color(0xccccccff, true));		
		for(Tri t : Tri.values()) {
			t.draw(bufg);
		}
		bufg.dispose();
		
		Graphics2D g = sp.createGraphics(plnNo);
		g.drawImage(buf, AffineTransform.getScaleInstance(1, map.depthRate), null);
		g.dispose();
	}
	
	public void destructor() {
		gom.removeMotionable(this);
		super.destructor();
	}

	public void motion() {
		sp.setPos(plnNo, map.getHexCoord(x, y));
		sp.setMov(plnNo, 0, -map.hexH);
	}
	
	public int processPriority() {
		return 0;
	}
	
	enum Tri{
		UPRIGHT(Direction.UPRIGHT) {
			protected void mainDraw(Graphics2D g){
				at = AffineTransform.getTranslateInstance(m.hexW*3/4, m.hexW/8);
				at.rotate(Math.PI / 6);
			}
		},
		RIGHT(Direction.RIGHT) {
			protected void mainDraw(Graphics2D g) {
				at = AffineTransform.getTranslateInstance(m.hexW, m.hexW/2);
				at.rotate(Math.PI / 2);
			}
		},
		DOWNRIGHT(Direction.DOWNRIGHT) {
			protected void mainDraw(Graphics2D g) {
				at = AffineTransform.getTranslateInstance(m.hexW*3/4, m.hexW*7/8);
				at.rotate(Math.PI * 5 / 6);
			}
		},
		DOWNLEFT(Direction.DOWNLEFT) {
			protected void mainDraw(Graphics2D g) {
				at = AffineTransform.getTranslateInstance(m.hexW/4, m.hexW*7/8);
				at.rotate(-Math.PI * 5 / 6);
			}
		},
		LEFT(Direction.LEFT) {
			protected void mainDraw(Graphics2D g) {
				at = AffineTransform.getTranslateInstance(0, m.hexW/2);
				at.rotate(-Math.PI / 2);
			}
		},
		UPLEFT(Direction.UPLEFT) {
			protected void mainDraw(Graphics2D g) {
				at = AffineTransform.getTranslateInstance(m.hexW/4, m.hexW/8);
				at.rotate(-Math.PI / 6);
			}
		};
		
		// è„ÇÃí∏ì_Ç™å¥ì_
		static final int width = 16;
		static final int height = 8;
		static final Polygon triangle = new Polygon(new int[]{0, -width/2, width/2}, new int[]{0, height, height}, 3);
		
		AffineTransform at, def;
		final Direction dir;
		
		Tri(Direction dir) {
			this.dir = dir;
		}
		
		protected abstract void mainDraw(Graphics2D g);

		void draw(Graphics2D g) {
			def = g.getTransform();
			mainDraw(g);
			g.transform(at);
			g.fillPolygon(triangle);
			g.setTransform(def);
		}
		
	}
	
	protected class DirCursor extends MapObjectCursor {
		
		Tri select = null;

		public DirCursor(SRPGGameObjectManager gom, Sprite sp, int plnNo,
				int x, int y, StageMap m) {
			super(gom, sp, plnNo, m.current.getDir().ordinal(), 0, m);
			
			sp.setPos(plnNo, map.getHexCoord(ds.x, ds.y));
			sp.setPos(plnNo, StageDrawOrder.MENU_CURSOR.z());
			
			initImg();
//			sp.setDraw(plnNo, new Draw() {
//
//				//ChangeTracker<Tri> ct = new ChangeTracker<Tri>();
//				Blink bl = new Blink(256, 20);
//				public void drawing(Graphics g, Plane pln) {
//					//ct.update(select);
//					//if(ct.isChanged()) {
////						pln.initImage(map.hexW, map.hexW);
////						Graphics2D gbg = pln.getGraphics();
//					BufferedImage buf = new BufferedImage(map.hexW, map.hexW, BufferedImage.TYPE_INT_ARGB);
//					Graphics2D bufg = buf.createGraphics();
//					bufg.setColor(new Color(0xff, 0x99, 0x99, 0xff + bl.blinking()));
//						
//					select.draw(bufg);
//					
////							pln.initImage(map.hexW, map.hexH);
////					gbg = pln.getGraphics();
//					bufg.dispose();
//					Graphics2D gr = pln.imgs.get(0).createGraphics();
//					gr.drawImage(buf, AffineTransform.getScaleInstance(1, map.depthRate), null);
//					gr.dispose();
////					g.dispose();
//					//}
//				}
//
//			});
			
			select();
		}
		
		private void initImg() {
			sp.initImg(plnNo, map.hexW, map.hexH);
		}
		
		Blink bl = new Blink(256, 20);
		private void drawImg() {
			BufferedImage buf = new BufferedImage(map.hexW, map.hexW, BufferedImage.TYPE_INT_ARGB);
			Graphics2D bufg = buf.createGraphics();
			bufg.setColor(new Color(0xff, 0x99, 0x99, 0xff + bl.blinking()));

			select.draw(bufg);

			bufg.dispose();
			
			sp.setTransparent(plnNo);
			Graphics2D gr = sp.createGraphics(plnNo);
			gr.drawImage(buf, AffineTransform.getScaleInstance(1, map.depthRate), null);
			gr.dispose();
		}
		
		private void select() {
			move = moveX || moveY;
			
			x = (x + y + 6) % 6;
			for(Tri t : Tri.values())
				if(x == t.ordinal()) {
					select = t;
					map.current.setDir(select.dir);
				}
			
			y = 0;
		}

		@Override
		protected void mainAction() {
			sp.setPos(plnNo, sp.getPos(ds.plnNo));
			
			select();
			
			drawImg();
			
			if(selected) {
				destructor();
				ds.destructor();
				
				super.select = true;
			}else if(canceled) {
				map.current.returnPrevious();
				destructor();
				ds.destructor();
			}
		}
		
	}

}
