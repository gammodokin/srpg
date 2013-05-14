package srpg.map.obj;

import game.Sprite;
import srpg.SRPGGameObjectManager;
import srpg.map.StageMap;

public class CureAE extends AttackEffect {

//	private int count = 0;
//	private int countMax = 8;
	
	//BufferedImage img;

	public CureAE(SRPGGameObjectManager gom, Sprite sp, int plnNo, int xx, int yy, StageMap m) {
		super(gom, sp, plnNo, xx, yy, m);

		String file = "animation_heal";//"cure";
		sp.setAnimeImg(plnNo, file, 3);
		
		sp.setTrailCount(plnNo, 2);

		//		sp.addGrp(file);
//		sp.setImg(plnNo, file);
		//img = sp.getGrp(file);
		
//		sp.setDraw(plnNo, new Draw() {
//
//			public void drawing(Graphics g, Plane pln) {
//				Graphics2D g2D = (Graphics2D)g;
//				g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f-count/(float)countMax));
//				g2D.drawImage(img, pln.coord.x.intValue(), pln.coord.y.intValue(), null);
//
//				g2D.setPaintMode();
//			}
//		});
		
		sp.setPos(plnNo, map.getHexCoord(xx, yy));
		sp.setMov(plnNo, -map.hexW - 7, -map.hexH*2);
		
		sp.playSE(Sprite.CURE);
	}

	@Override
	public void action() {
//		count++;
		sp.setPos(plnNo, map.getHexCoord(x, y));
		sp.setMov(plnNo, -map.hexW - 7, -map.hexH*2);
		if(sp.isAnimeClosed(plnNo)) {//count >= countMax) {
			destructor();
			damage();
		}
	}

}
