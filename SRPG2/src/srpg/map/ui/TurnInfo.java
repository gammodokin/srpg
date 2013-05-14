package srpg.map.ui;

import game.Sprite;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import srpg.SRPGGameObjectManager;
import srpg.map.MapObject;
import srpg.map.StageDrawOrder;
import srpg.map.StageMap;
import srpg.map.obj.Unit;

public class TurnInfo extends MapObject {
	
	private int width, height;
	private int elementCount;
	private Unit[] units;

	public TurnInfo(SRPGGameObjectManager gom, Sprite sp, int plnNo, int x, int y, StageMap m) {
		super(gom, sp, plnNo, x, y, m);
		
		int topOffset = 90, bottomOffset = 75;
		width = 120;
		height = gom.height - topOffset - bottomOffset;
		sp.setPos(plnNo, gom.width - width, topOffset);
		sp.setPos(plnNo, StageDrawOrder.INFO.z());
		
		//initImg();
//		sp.setDraw(plnNo, new Draw() {
//
//			BufferedImage bgImg;
//			double eachHeight = (double)height / elementCount;
//
//			private void initBG() {
//				bgImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
//				Graphics2D gbg = bgImg.createGraphics();
//				Sprite.setTransparent(bgImg);
//				
//				gbg.setColor(new Color(0x66aaaaff, true));
//				gbg.fillRect(0, 0, width, height);
//				
//				for(int i = 0; i < elementCount; i++) {
//					int rectY = (int)(i * eachHeight);
//					gbg.setColor(new Color(0x66444444, true));
//					gbg.drawRect(0, rectY, width, (int)eachHeight);
//				}
//				
//				gbg.dispose();
//
//			}
//			
//			//int count;
////			Graphics2D gbg;
//			boolean first = true;
//			ChangeTracker<Unit> ct = new ChangeTracker<Unit>();
//			public void drawing(Graphics g, Plane pln) {
//				if(units == null) // 1ÉãÅ[Évñ⁄Ç…unitsÇ™null
//					return;
//				
//				if(first) {
//					initBG();
//					first = false;
//				}
//				
//				//double eachHeight = (double)height / elementCount;
//				
////				if(pln.img == null) {
////					bgImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
////					gbg = bgImg.createGraphics();
////					Sprite.setTransparent(bgImg);
////					
////					gbg.setColor(new Color(0x66aaaaff, true));
////					gbg.fillRect(0, 0, width, height);
////					
////					for(int i = 0; i < elementCount; i++) {
////						int rectY = (int)(i * eachHeight);
////						gbg.setColor(new Color(0x66444444, true));
////						gbg.drawRect(0, rectY, width, (int)eachHeight);
////					}
////					
////					gbg.dispose();
////					
////					pln.initImage(width, height);
////				}
//				if(ct.isChanged(units[0])) {
//					Graphics2D gbg = pln.imgs.get(0).createGraphics();
//					gbg.drawImage(bgImg, 0, 0, null);
//					
//					int offsetX = 14;
//					//int offsetY = 6;
//					for(int i = 0; i < elementCount; i++) {
//						int rectY = (int)(i * eachHeight);
//						gbg.setClip(offsetX, rectY, width, (int)eachHeight);
//						gbg.drawImage(units[i].face, offsetX, rectY - map.hexH/2 + 5, null);
//						/*
//						gbg.setColor(units[i].color);
//						gbg.fillRect(20, rectY + 5, 22, 22);*/
//					}
//					gbg.setClip(0, 0, width, height);
//					
//					gbg.setColor(new Color(0xff, 0xff, 0xff, 0xff));// - 3 * count));
//					//count++;
//					//count %= 32;
//					gbg.drawRect(0, 0, width, (int)eachHeight);
//					
//					//System.out.println(count);
//					
//					gbg.setColor(new Color(0x44ffffff, true));
//					gbg.fillRect(0, (int)eachHeight, width, height - (int)eachHeight);
//					
//					gbg.setColor(Color.BLACK);
//					for(int i = 0; i < elementCount; i++) {
//						int rectY = (int)(i * eachHeight);
//						Sprite.drawShadedString(units[i].getName(), 15, rectY + (int)eachHeight - 5, gbg);
//					}
//					
//					gbg.setColor(Color.BLACK);
//					
//					gbg.dispose();
//				}
//			}
//			
//		});
	}
	
	BufferedImage bgImg;
	double eachHeight;
	private void initImg() {
		sp.initImg(plnNo, width, height);
		
		eachHeight = (double)height / elementCount;
		
		bgImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D gbg = bgImg.createGraphics();
		Sprite.setTransparent(bgImg);

		gbg.setColor(new Color(0x66aaaaff, true));
		gbg.fillRect(0, 0, width, height);

		for(int i = 0; i < elementCount; i++) {
			int rectY = (int)(i * eachHeight);
			gbg.setColor(new Color(0x66444444, true));
			gbg.drawRect(0, rectY, width, (int)eachHeight);
		}

		gbg.dispose();
		
	}
	
	private void drawImg() {
		sp.setTransparent(plnNo);
		
		Graphics2D g = sp.createGraphics(plnNo);
		g.drawImage(bgImg, 0, 0, null);
		
		int offsetX = 14;
		//int offsetY = 6;
		for(int i = 0; i < elementCount; i++) {
			int rectY = (int)(i * eachHeight);
			
			int x = 0, y = rectY;
			if(units[i].face.getWidth() <= map.hexW + 5) {
				x = offsetX;
				y -= map.hexH/2 - 5;
			}
			
			g.setClip(x, rectY, width, (int)eachHeight);
						
			g.drawImage(units[i].face, x, y, null);
			/*
			gbg.setColor(units[i].color);
			gbg.fillRect(20, rectY + 5, 22, 22);*/
		}
		g.setClip(0, 0, width, height);
		
		g.setColor(new Color(0xff, 0xff, 0xff, 0xff));// - 3 * count));
		//count++;
		//count %= 32;
		g.drawRect(0, 0, width, (int)eachHeight);
		
		//System.out.println(count);
		
		g.setColor(new Color(0x44ffffff, true));
		g.fillRect(0, (int)eachHeight, width, height - (int)eachHeight);
		
		g.setColor(Color.BLACK);
		for(int i = 0; i < elementCount; i++) {
			int rectY = (int)(i * eachHeight);
			Sprite.drawShadedString(units[i].getName(), 15, rectY + (int)eachHeight - 5, g);
		}
		
		g.setColor(Color.BLACK);
		
		g.dispose();

	}
	
	public void setUnits(Unit[] units) {
		this.units = units;
		
		drawImg();
	}
	
	public void setElementCount(int ec) {
		elementCount = ec;
		
		initImg();
	}
	
}
