package srpg.map.ui;

import game.Draw;
import game.Motionable;
import game.Plane;
import game.Sprite;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

import srpg.SRPGGameObjectManager;
import srpg.map.MapDrawOrder;
import srpg.map.StageMap;

import myutil.Coord;

public abstract class MapCursor extends MapObjectCursor implements Motionable{
	
	protected Color color;
	
	protected boolean actionFlag = false;
	
	public MapCursor(SRPGGameObjectManager gom, Sprite sp, int plnNo, int x, int y, StageMap m) {
		super(gom, sp, plnNo, x, y, m);
		
		color = new Color(0x00ff00);

		sp.setPos(plnNo, MapDrawOrder.MAP_CURSOR.z() + y * StageMap.YZRate);
		sp.setDraw(plnNo, new Draw() {

			int count = 0;
			int c = 1;
			public void drawing(Graphics g, Plane pln) {
				if(actionFlag) {
					actionFlag = false;
					g.setColor(Color.WHITE);
					int offY = -map.hexH * 3/2, w = 18 * count/10, h = 12;
					Polygon po = new Polygon(new int[]{(map.hexW - w)/2, (map.hexW + w)/2, map.hexW/2},
							new int[]{0, 0, h}, 3);
					po.translate(pln.coord.x.intValue(), pln.coord.y.intValue() + offY);
					g.fillPolygon(po);
					g.setColor(Color.GRAY);
					g.drawPolygon(po);
				}
				
				g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 84 - 5 * count));
				count += c;
				if(count <= 0 || count >= 10)
					c *= -1;

				int over = 2;
				map.fillHex(g, (int) Math.round(pln.coord.x) - over,
						(int) Math.round(pln.coord.y) - over, map.hexW + over * 2, map.hexH + over * 2);
				
				

				g.setColor(Color.BLACK);
			}

		});
		
		sp.setPos(plnNo, map.getHexCoord(x, y));
		
		map.setCursorPos(x, y);
		
		gom.addMotionable(this);
	}
	
	private final int leftOffset = 200, rightOffset = 220, topOffset = 160, bottomOffset = 190;
	protected void mainAction() {
		move = moveX || moveY;
		
		actionFlag = true;
		x = (x + map.hex[0].length) % map.hex[0].length;
		y = (y + map.hex.length) % map.hex.length;
		//sp.setPos(plnNo, getHexCood(x, y));
		map.setCursorPos(x, y);
		
		Coord<Double> currentC = map.getHexCoord(x, y);
		currentC.x += map.hexW/2f;
		currentC.y += map.hexH/4f;
		//System.out.println("    cursor : (" + currentC.x + ", " + currentC.y + ")\n");
		Coord<Double> move = new Coord<Double>(0d, 0d);
		if(currentC.x < leftOffset) {
			move.x += leftOffset - currentC.x + 1;
		}else if(gom.width - rightOffset < currentC.x) {
			move.x += gom.width - rightOffset - currentC.x - 1;
		}
		
		if(currentC.y < topOffset) {
			move.y += topOffset - currentC.y + 1;
		}else if(gom.height - bottomOffset < currentC.y) {
			move.y += gom.height - bottomOffset - currentC.y - 1;
		}
		map.moveMap(move.x, move.y);
		
		MCAction();
	}
	
	public void motion() {
		sp.setPos(plnNo, map.getHexCoord(x, y));
		sp.setPos(plnNo, MapDrawOrder.MAP_CURSOR.z() + y * StageMap.YZRate);
	}
	
	public int processPriority() {
		return 0;
	}

	protected abstract void MCAction();

	public void destructor() {
		super.destructor();
		gom.removeMotionable(this);
	}
}
