package srpg.map;

import game.Motionable;
import game.Sprite;

import java.awt.Graphics;
import java.awt.Graphics2D;

import myutil.Coord;
import srpg.SRPGGameObjectManager;

public abstract class HorizonHex extends MapObject implements Motionable{

	private Coord<Integer> maxHex;
	private int maxHeight = Integer.MIN_VALUE, minHeight = Integer.MAX_VALUE;
	protected Coord<Double> offset;
	
	private int startX = 0, endX = map.hex[y].length;
	
	// rightXÇÕç≈å„ÇÃÉ}ÉXÇÃàÍÇ¬âE
	private int leftX = 0, rightX = 0;
	private boolean[] drawArea;
	
	protected int bottomOffset = 0;
	
	protected boolean repaint = true;
	
	public HorizonHex(SRPGGameObjectManager gom, Sprite sp, int plnNo, int x,
			int hy, StageMap m) {
		super(gom, sp, plnNo, x, hy, m);
		
		init();
		
		initImg();
		
		motion();
//		
//		sp.setDraw(plnNo, new Draw() {
//				//private final int fontSize = 16;
//				public void drawing(Graphics g, Plane pln) {
////					if(repaint || pln.img == null) {
////						int width = map.hexW * (endX - startX) + map.hexW/2 + 1;// map.hexW * map.hex[y].length + map.hexW/2 + 1;
////						int height = map.hexH*2 + map.hexHeight * (maxHeight - minHeight) + bottomOffset;
////						//if(pln.img == null)
////							pln.initImage(width, height);
////						Graphics gbg = pln.getGraphics();
////						
////						for(int x = startX; x < endX; x++)
////								if(drawArea == null || drawArea[x])
////									drawEachHex(gbg, x);
////						gbg.dispose();
////						
////						repaint = false;
////					}
//				}
//			
//		});
		
		gom.addMotionable(this);
	}
	
	public void destructor() {
		gom.removeMotionable(this);
		super.destructor();
	}

	public void motion() {
		sp.setPos(plnNo, map.getHexCoord(0, 0));
		sp.setMov(plnNo, offset.x, offset.y + map.hex[0][0].height * map.hexHeight);
		if(repaint) {
			drawImg();
			repaint = false;
		}
	}
	
	public int processPriority() {
		return 0;
	}
	
	private void init() {
		if(drawArea != null) {
			startX = leftX;
			endX = rightX;
		}
		
		for(int hx = startX; hx < endX; hx++) {
			if(maxHeight < map.hex[y][hx].height && (drawArea == null || drawArea[hx])) {
				maxHeight = map.hex[y][hx].height;
				maxHex = new Coord<Integer>(hx, y);
			}
		}
		for(int hx = startX; hx < endX; hx++) {
			if(minHeight > map.hex[y][hx].height && (drawArea == null || drawArea[hx])) {
				minHeight = map.hex[y][hx].height;
			}
		}
		 
		offset = new Coord<Double>(map.getHexCoord(leftX, 0, false).x, map.getHexCoord(maxHex, false).y);
	}
	
	void setDrawArea(boolean[] da) {
		if(da.length != map.hex.length) {
			System.out.println("HrizonHexError : illegal format drawArea");
			return;
		}
		
		drawArea = da;
		for(int i = 0; i < da.length; i++)
			if(da[i])
				leftX = i;
		for(int i = da.length - 1; i >= 0; i--)
			if(da[i])
				rightX = i + 1;
		
		if(leftX - rightX <= 0)
			sp.setView(plnNo, false);
		
		init();
	}
	
	private void initImg() {
		int width = map.hexW * (endX - startX) + map.hexW/2 + 1;// map.hexW * map.hex[y].length + map.hexW/2 + 1;
		int height = map.hexH*2 + map.hexHeight * (maxHeight - minHeight) + bottomOffset;
		sp.initImgs(plnNo, 1, width, height);
	}
	
	private void drawImg() {
//			int width = map.hexW * (endX - startX) + map.hexW/2 + 1;// map.hexW * map.hex[y].length + map.hexW/2 + 1;
//			int height = map.hexH*2 + map.hexHeight * (maxHeight - minHeight) + bottomOffset;
			//if(pln.img == null)
//				sp.initImage(width, height);
//			Graphics gbg = pln.getGraphics();
		
		sp.setTransparent(plnNo);
		
		Graphics2D g = sp.createGraphics(plnNo);
		for(int x = startX; x < endX; x++)
			if(drawArea == null || drawArea[x])
				drawEachHex(g, x);
		g.dispose();

	}
	
	protected abstract void drawEachHex(Graphics g, int x);
	
}
