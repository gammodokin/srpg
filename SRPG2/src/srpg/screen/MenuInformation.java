package srpg.screen;

import game.DrawOrder;
import game.GameObjectManager;
import game.Sprite;

import java.awt.Color;
import java.awt.Graphics;

abstract class MenuInformation extends Information {

	public MenuInformation(GameObjectManager gom, Sprite sp, int plnNo, int x,
			int y) {
		super(gom, sp, plnNo, x, y);

		sp.setPos(plnNo, DrawOrder.INFO.z());
	}

	@Override
	protected void drawBGRect(int x, int y, int width, int height, Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(x, y, width ,height);
	}
	/*
	//protected Coord<Integer> currentUpdate;
	@Override
	protected void update() {
		current.assign(currentUpdate);
	}

	//protected void setCurrent(Coord<Integer> c) {
	//	currentUpdate.assign(c);
	//}
	 */
}
