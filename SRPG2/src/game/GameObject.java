package game;

public class GameObject {
	
	protected final int plnNo;
	protected int x, y;
	protected final GameObjectManager gom;
	protected final Sprite sp;

	public GameObject(GameObjectManager gom, Sprite sp, int plnNo, int x,
			int y) {
		this.plnNo = plnNo;
		this.gom = gom;
		this.sp = sp;
		this.x = x;
		this.y = y;
		sp.setPos(plnNo, x, y);
	}

	public void destructor() {
		gom.delGO(plnNo);
	}

}