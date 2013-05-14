package srpg.map.ui.area;

import game.Draw;
import game.Plane;
import game.Sprite;

import java.awt.Color;
import java.awt.Graphics;

import myutil.Coord;
import srpg.SRPGGameObjectManager;
import srpg.map.HorizonHex;
import srpg.map.MapDrawOrder;
import srpg.map.MapObject;
import srpg.map.StageMap;
import srpg.map.ui.MapCursor;

public abstract class Area extends MapObject{
	
	protected Area area;
	protected int range;
	protected boolean[][] availables;
	protected Color color;
	
	HorizonArea[] ha;

	public Area(SRPGGameObjectManager gom, Sprite sp, int plnNo, int x, int y, StageMap m) {
		super(gom, sp, plnNo, x, y, m);
		//this.availables = availables;
		
		area = this;
		
		ha = new HorizonArea[map.hex.length];
		
		sp.setDraw(plnNo, new Draw() {public void drawing(Graphics g, Plane pln) {}});}
	
	public void destructor() {
		for(HorizonArea h : ha)
			h.destructor();
		super.destructor();
	}
	
	protected boolean isAvailable(int x, int y) {
		if(x < 0 || availables.length <= x 
				|| y < 0 || availables[x].length <= y)
			return false;
		return availables[x][y];
	}
	
	protected void createHorizon(Class<? extends HorizonArea> h) {
		for(int i = 0; i < map.hex.length; i++)
			ha[i] = gom.createMapObject(map, h, 0, i, area);
	}
	
	// パッケージプライベートだとリフレクションできないっぽい
	protected class HorizonArea extends HorizonHex{

		public HorizonArea(SRPGGameObjectManager gom, Sprite sp, int plnNo, int x,
				int hy, StageMap m) {
			super(gom, sp, plnNo, x, hy, m);
			
			sp.setPos(plnNo, MapDrawOrder.AREA.z() + y * StageMap.YZRate);
		}

		@Override
		protected void drawEachHex(Graphics g, int x) {
			g.setColor(color);

			if(availables != null && availables[x][y]) {
				Coord<Double> hc = map.getHexCoord(x, y, false);
				map.fillHex(g, hc.x.intValue(), (int)(hc.y - offset.y) - 1, map.hexW, map.hexH, 1/4f);
			}
		}
		
	}
	
	abstract protected class AreaCursor extends MapCursor {

		public AreaCursor(SRPGGameObjectManager gom, Sprite sp, int plnNo,
				int x, int y, StageMap m) {
			super(gom, sp, plnNo, x, y, m);
		}

		@Override
		public void destructor() {
			super.destructor();
			area.destructor();
		}
		
	}
}
