package srpg.screen;

import game.Blink;
import game.Draw;
import game.GameObjectManager;
import game.Plane;
import game.Sprite;

import java.awt.Color;
import java.awt.Graphics;

public class Result extends Screen {

	private String str;
	private int income = 0;
	//private final int WIDTH, HEIGHT;
	
	public Result(GameObjectManager gom, Sprite sp, int plnNo, int x, int y) {
		super(gom, sp, plnNo, 0, 0);
		
		if(x == 0) {
			str = "Mission complete.";
			sp.playBGM(Sprite.BGM.WIN);
		} else {
			str = "Game over.";
			sp.playBGM(Sprite.BGM.LOSE);
		}
		
		/*WIDTH = gom.width;
		HEIGHT = gom.height;*/
		//sp.setPos(plnNo, 1000);
		sp.setDraw(plnNo, new Draw() {

			Blink blink = new Blink(255, 24);
			public void drawing(Graphics g, Plane pln) {
				clear(g);
				/*g.setColor(new Color(0x66ffffff, true));
				g.fillRect(0, 0, WIDTH, HEIGHT);*/
				
				g.setColor(Color.BLACK);
				drawCenterString(str, pln.x(), pln.y(), g);
				//drawCenterString("Income : " + income, pln.x(), pln.y() + 30, g);
				
				g.setColor(new Color(0, 0, 0, 255 + blink.blinking()));
				drawCenterString("Press SelectKey", pln.coord.x.intValue(), pln.coord.y.intValue() + 150, g);
			}
			
		});
		
		sp.setPos(plnNo, gom.width / 2 - 20, gom.height / 2 - 35);
	}
	
	@Override
	protected void mainAction() {
		if(selected) {
			sp.playSE(Sprite.Preset.SELECT);
			//gom.init();
			destructor();
		}
	}
	
	public void setViewIncome(int income) {
		this.income = income;
	}
}
