package srpg.map.ui.area;

import game.Sprite;

import java.awt.Color;

import srpg.SRPGGameObjectManager;
import srpg.map.StageMap;

public class MovableArea extends Area {

	public MovableArea(SRPGGameObjectManager gom, Sprite sp, int plnNo, int x, int y, StageMap m) {
		super(gom, sp, plnNo, x, y, m);
		
		color = new Color(0x0000ff);
		
		availables = map.current.getMovableArea();
		
		gom.createMapObject(map, MoveCursor.class, x, y, this);
		
		createHorizon(MoveHA.class);
	}
	
	protected class MoveCursor extends AreaCursor {

		public MoveCursor(SRPGGameObjectManager gom, Sprite sp, int plnNo, int x, int y, StageMap m) {
			super(gom, sp, plnNo, x, y, m);
			
			color = new Color(0x2222ff);
		}
		
		protected void MCAction() {
			if(selected && (map.unitMap[y][x] == null || map.unitMap[y][x] == map.current) && isAvailable(x ,y)) {
				destructor();
				map.current.setMove(x, y);//as.getWay(x, y));
				
				select = true;
			} else if(canceled) {
				destructor();
			}
		}
		
	}
	
	protected class MoveHA extends HorizonArea {

		public MoveHA(SRPGGameObjectManager gom, Sprite sp, int plnNo, int x,
				int y, StageMap m) {
			super(gom, sp, plnNo, x, y, m);
		}
		
	}
}
