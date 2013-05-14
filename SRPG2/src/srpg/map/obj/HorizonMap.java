package srpg.map.obj;

import game.Sprite;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import myutil.Coord;
import srpg.SRPGGameObjectManager;
import srpg.map.HorizonHex;
import srpg.map.MapDrawOrder;
import srpg.map.StageMap;

public class HorizonMap extends HorizonHex {
	
	static final int stageHeight = 4;
	
	//BufferedImage[] heichi, sougen, arechi;

	public HorizonMap(SRPGGameObjectManager gom, Sprite sp, int plnNo, int x,
			int hy, StageMap m) {
		super(gom, sp, plnNo, x, hy, m);
		
		bottomOffset = map.hexHeight*2;
		
		sp.setPos(plnNo, MapDrawOrder.MAP.z() + y * StageMap.YZRate);
	}

	int fontSize = 16;
	@Override
	protected
	void drawEachHex(Graphics g, int x) {

		Coord<Double> hc = map.getHexCoord(x, y, false);
		int hy = (int)(hc.y - offset.y);
		
		g.setColor(new Color(0x779966));
		int basicHeight = map.hex[y][x].height + stageHeight;
		int heightDifL, heightDifR;
		if(y + 1 < map.hex.length) {
			if(y % 2 == 0) {
				heightDifL = x - 1 >= 0 ? map.hex[y][x].height - map.hex[y + 1][x - 1].height : basicHeight;
				heightDifR = x < map.hex[y + 1].length ? map.hex[y][x].height - map.hex[y + 1][x].height :basicHeight;
			} else {
				heightDifL = x >= 0 ? map.hex[y][x].height - map.hex[y + 1][x].height : basicHeight;
				heightDifR = x + 1 < map.hex[y + 1].length ? map.hex[y][x].height - map.hex[y + 1][x + 1].height : basicHeight;
			}
		} else {
			heightDifL = heightDifR = basicHeight;
		}
		
		BufferedImage[] field = map.hex[y][x].graphic;
		int offsetY = hy + map.hexH*3/4;
		if(heightDifL > 0) {
			int offsetX = hc.x.intValue();
			g.drawImage(field[1],
					offsetX, 							offsetY,
					offsetX + field[1].getWidth()/2, 	offsetY + field[1].getHeight(),
					0, 									0,
					field[1].getWidth()/2, 				field[1].getHeight(),
					null);
			
			for(int i = 1; i < heightDifL; i++)
				g.drawImage(field[2],
						offsetX, 							map.hexHeight * i + offsetY,
						offsetX + field[2].getWidth()/2, 	map.hexHeight * i + offsetY + field[2].getHeight(),
						0, 									0,
						field[2].getWidth()/2, 				field[2].getHeight(),
						null);
		}
		
		if(heightDifR > 0) {
			int offsetX = hc.x.intValue() + map.hexW/2;
			g.drawImage(field[1],
					offsetX, 							offsetY,
					offsetX + field[1].getWidth()/2, 	offsetY + field[1].getHeight(),
					field[1].getWidth()/2, 				0,
					field[1].getWidth(), 				field[1].getHeight(),
					null);
			
			for(int i = 1; i < heightDifR; i++)
				g.drawImage(field[2],
						offsetX, 							map.hexHeight * i + offsetY,
						offsetX + field[2].getWidth()/2, 	map.hexHeight * i + offsetY + field[2].getHeight(),
						field[2].getWidth()/2, 				0,
						field[2].getWidth(), 				field[2].getHeight(),
						null);
		}
		
		g.drawImage(field[0], hc.x.intValue(), hy, map.hexW + 1, map.hexH + 1, null);
		g.setColor(Color.GRAY);
		map.drawHex(g, hc.x.intValue(), hy, map.hexW, map.hexH);
		
		// À•W•\Ž¦—p
		//g.drawString("(" + x + ", " + y + ")", hc.x.intValue() + 11, hy + 22);
		//g.setFont(defaultFont);
	}

}
