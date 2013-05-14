package srpg.map.ui.info;

import game.Motionable;
import game.Sprite;

import java.awt.Graphics;

import srpg.SRPGGameObjectManager;
import srpg.map.StageDrawOrder;
import srpg.map.StageMap;
import srpg.screen.Information;

public abstract class MapInformation extends Information implements Motionable {
	
	protected final SRPGGameObjectManager gom;
	protected final StageMap map;
	
	public MapInformation(SRPGGameObjectManager gom, Sprite sp, int plnNo, int x,
			int y, StageMap m) {
		super(gom, sp, plnNo, x, y);
		
		sp.setPos(plnNo, StageDrawOrder.INFO.z());
		
		this.gom = gom;
		this.map = m;
	}
	
	@Override
	public void destructor() {
		gom.removeMotionable(this);
		super.destructor();
	}

	@Override
	protected void drawBGRect(int x, int y, int width, int height, Graphics g) {
		Sprite.drawDesignedRect(x, y, width, height, g);
	}

	@Override
	protected void update() {
		current.assign(map.getCursored());
	}
	
	/*
	int width, height;
	String title;
	StrData[] sds;
	
	Coord<Integer> current;
	
	ChangeTracker<Coord<Integer>> ct = new ChangeTracker<Coord<Integer>>();
	
	public MapInformation(SRPGGameObjectManager gom, Sprite sp, int plnNo, int x, int y, StageMap m) {
		super(gom, sp, plnNo, x, y, m);
		
		sp.setPos(plnNo, StageDrawOrder.INFO.z());
		sp.setDraw(plnNo,new Draw() {

			public void drawing(Graphics g, Plane pln) {
				if(sds == null) // 1ÉãÅ[Évñ⁄Ç…ï\é¶Ç≥ÇÍÇƒÇµÇ‹Ç§ÇÃÇâÒî
					return;
				
				if(ct.isChanged(current.clone()) || pln.img == null) {
					pln.initImage(width + 1, height + 3 + 1);
					Graphics gbg = pln.getGraphics();
					int x = 0;
					int y = 4;
					Sprite.drawDesignedRect(x, y, width, height, gbg);

					gbg.setColor(Color.BLACK);
					Sprite.drawShadedString(title, x + 20, y + 5, gbg);
					for(StrData sd : sds) {
						gbg.setColor(sd.c);
						Sprite.drawShadedString(sd.str, x + sd.x, y + sd.y, gbg);
					}
					
					gbg.dispose();
				}
			}

		});
		
		current = new Coord<Integer>(0, 0);
		
		gom.addMotionable(this);
	}
	
	protected class StrData {
		
		String str;
		int x, y;
		Color c = Color.BLACK;
		
		StrData(String str, int x, int y) {
			this.str = str;
			this.x = x;
			this.y = y;
		}
		
		@Override
		public String toString() {
			return str;
		}
		
	}
	
	@Override
	public void motion() {
		ct.isChanged(current.clone());
		
		current.assign(map.getCursored());
		mainMotion();
	}
	
	public abstract void mainMotion();

	public int processPriority() {
		return -1;
	}
	*/
}
