package srpg.map.ui.menu;

import game.Sprite;

import java.awt.Graphics;

import myutil.Coord;
import srpg.SRPGGameObjectManager;
import srpg.map.StageDrawOrder;
import srpg.map.StageMap;
import srpg.screen.Menu;

public class MapObjectMenu extends Menu {
	
	protected final StageMap map;
	protected final SRPGGameObjectManager gom;

	public MapObjectMenu(SRPGGameObjectManager gom, Sprite sp, int plnNo,
			int x, int y, StageMap m) {
		super(gom, sp, plnNo, x, y);
		this.gom = gom;
		map = m;
		
		sp.setPos(plnNo, StageDrawOrder.MENU.z());
	}
	
	@Override
	protected void createCursor(Coord<Double> current) {
		gom.createMapObject(map, menuCursor,
				current.x.intValue() + offset.x + commandHeight, current.y.intValue() + offset.y + span, this);
	}
	
	// パッケージプライベートだとリフレクションできない
	abstract protected class MOMenuCursor extends MenuCursor {
		
		protected final StageMap map;
		protected final SRPGGameObjectManager gom;

		public MOMenuCursor(SRPGGameObjectManager gom, Sprite sp, int plnNo,
				int x, int y, StageMap m) {
			super(gom, sp, plnNo, x, y);
			this.gom = gom;
			map = m;
			
			sp.setPos(plnNo, StageDrawOrder.MENU_CURSOR.z());
		}
		
	}

	@Override
	protected void drawBGRect(int x, int y, int width, int height, Graphics g) {
		Sprite.drawDesignedRect(x, y, width, height, g);
	}
	
}
