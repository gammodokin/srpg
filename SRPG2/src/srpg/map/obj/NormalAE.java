package srpg.map.obj;

import game.Sprite;
import myutil.Coord;
import srpg.SRPGGameObjectManager;
import srpg.map.StageMap;

public class NormalAE extends AttackEffect {

	private Coord<Double> before, after;

	private double dir;
	private double cos, sin;
	
//	private BufferedImage img;

	public NormalAE(SRPGGameObjectManager gom, Sprite sp, int plnNo, int x, int y, StageMap m) {
		super(gom, sp, plnNo, x, y, m);

		initBullet("tama3", 3, 15, Sprite.SHOOT);
	}
	
	private int offX, offY;

	private int speed;
	@Override
	public void action() {
		if(bulletAction()) {
			destructor();
			damage();
		}
	}
	
	protected void initBullet(String file, int trail, int speed, String se) {
		sp.addGrp(file);
		sp.setImg(plnNo, file);
		sp.setTrailCount(plnNo, trail);
		
		before = map.getHexCoord(map.current.getPos());
		setHexCenter(before);
		sp.setPos(plnNo, before);
		
		Coord<Integer> size = sp.getImgSize(plnNo);
		//sp.setMov(plnNo, -size.x/2, -size.y/2 - map.hexH/2);
		offX = -size.x/2;
		offY = -size.y/2 - map.hexH/2;
		sp.setMov(plnNo, offX, offY);
		
		dir = Math.atan2(target.y - before.y, target.x - before.x);
		cos = Math.cos(dir);
		sin = Math.sin(dir);
				
		current = sp.getPos(plnNo);
		
		this.speed = speed;
		
		sp.playSE(se);
	}
	
	protected boolean bulletAction() {
		if(sp.isAnimeClosed(plnNo)) {
			return true;
		}
		
		sp.setMov(plnNo, -offX, -offY);

		target = map.getHexCoord(x, y);
		setHexCenter(target);

		after = map.getHexCoord(map.current.getPos().x, map.current.getPos().y);
		setHexCenter(after);
		Coord<Double> difference = new Coord<Double>(after.x - before.x, after.y - before.y);

		sp.setMov(plnNo, difference.x + speed * cos, difference.y + speed * sin);
		before.assign(after);

		current = sp.getPos(plnNo);
		if((cos > 0 && current.x > target.x) ||
				(cos < 0 && current.x < target.x) ||
				(sin > 0 && current.y > target.y) ||
				(sin < 0 && current.y < target.y)) {
			
			sp.setImg(plnNo, null);
		}
		
		sp.setMov(plnNo, offX, offY);
		
		return false;
	}
}
