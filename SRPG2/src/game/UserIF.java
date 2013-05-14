package game;

import myutil.Queue;


public abstract class UserIF extends GameObject {

	protected static Queue<Key> keyQ;

	public UserIF(GameObjectManager gom, Sprite sp, int plnNo, int x,
			int y) {
		super(gom, sp, plnNo, x, y);

		gom.setUIF(this);
	}

	public abstract void action();

	public void destructor() {
		gom.removeUIF(this);
		super.destructor();
	}

	static void setKeyQ(Queue<Key> keyQ) {
		UserIF.keyQ = keyQ;
	}

}