package srpg.screen;

import game.DrawOrder;
import game.GameObjectManager;
import game.Sprite;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import myutil.Coord;

public abstract class ScreenMenu extends Menu {

	private BufferedImage img;

	public ScreenMenu(GameObjectManager gom, Sprite sp, int plnNo, int x,
			int y) {
		super(gom, sp, plnNo, x, y);
		
		width = gom.width;
		height = gom.height;
		commandHeight = 10;

		sp.setPos(plnNo, DrawOrder.SCREEN_MENU.z());
		sp.setPos(plnNo, 0, 0);

		offset = new Coord<Integer>(70, 0);
		
		String file = "shade_marisa";
		sp.addGrp(file);
		img = sp.getGrp(file);
	}

	protected void createCursor(Coord<Double> current) {
		gom.addGO(menuCursor,
				current.x.intValue() + offset.x + commandHeight, current.y.intValue() + offset.y + span, this);
	}

	@Override
	protected void drawBGRect(int x, int y, int width, int height, Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(x, y, width, height);
		
		g.drawImage(img, x + width - img.getWidth(), y - 50, null);
	}

	protected abstract class SMCursor extends MenuCursor {

		public SMCursor(GameObjectManager gom, Sprite sp, int plnNo, int x,
				int y) {
			super(gom, sp, plnNo, x, y);
			sp.setPos(plnNo, DrawOrder.CURSOR.z());
		}

	}
}