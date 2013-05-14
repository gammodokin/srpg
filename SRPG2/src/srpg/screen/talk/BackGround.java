package srpg.screen.talk;

import game.GameObject;
import game.GameObjectManager;
import game.Sprite;

public class BackGround extends GameObject {

	public BackGround(GameObjectManager gomm, Sprite sp, int plnNo, int x, int y) {
		super(gomm, sp, plnNo, x, y);
		
		sp.setPos(plnNo, TalkDrawOrder.BG.z());
		
//		sp.setDraw(plnNo, new Draw() {
//
//			@Override
//			public void drawing(Graphics g, Plane pln) {
//				g.setColor(Color.WHITE);
//				g.fillRect(0, 0, gom.width, gom.height);
//			}
//			
//		});
		
		
	}
	
	void setImg(String file) {
		sp.addGrp(file);
		sp.setImg(plnNo, file);
	}

}
