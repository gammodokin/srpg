package srpg.screen;

import game.Draw;
import game.GameObjectManager;
import game.Plane;
import game.Sprite;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Title extends Screen {

	private BufferedImage img;

	public Title(GameObjectManager ggom, Sprite sp, int plnNo, int xx, int yy) {
		super(ggom, sp, plnNo, xx, yy);
		
		String file = "shade_reimu";
		sp.addGrp(file);

		img = sp.getGrp(file);
		//sp.setImg(plnNo, file);
		
		
		//sp.setPos(plnNo, 1000);
		sp.setDraw(plnNo, new Draw() {

			int count = 0;
			public void drawing(Graphics g, Plane pln) {
				clear(g);
				
				g.drawImage(img, - 40, -60, null);
				
				Font current = g.getFont();
				
				g.setFont(new Font(current.getFontName(), Font.BOLD | Font.ITALIC, 24));
				g.setColor(Color.BLACK);
				drawCenterString("東方SRPG風アプレット", pln.coord.x.intValue() + 20, pln.coord.y.intValue(), g);
				
				g.setFont(current);
				count++;
				count %= 0xff / 20;
				
				g.setColor(new Color(0x00, 0x00, 0x00, 0xff - count * 16));
				drawCenterString("Press Enter", pln.coord.x.intValue() + 80, pln.coord.y.intValue() + 120, g);
				
				g.setColor(Color.BLACK);
				g.drawString("BGM素材：", gom.width - 200, gom.height - 40);
				g.drawString("SENTIVE.NET http://sentive.net/", gom.width - 200, gom.height - 25);
				
				g.drawString("Programed by sturm", gom.width - 200, gom.height - 5);
				
				g.drawString("エフェクト素材：", 15, gom.height - 70);
				g.drawString("SPIERAL WIND http://hiropiro-may.hp.infoseek.co.jp/owner/", 15, gom.height - 55);
				
				g.drawString("効果音素材：", 15, gom.height - 35);
				g.drawString("ザ･マッチメイカァズ http://osabisi.sakura.ne.jp/m2/", 15, gom.height - 20);
				g.drawString("ULTIMATEゲーム事業部 http://utm-game-web.hp.infoseek.co.jp/", 15, gom.height - 5);
			}
			
		});
		
		sp.setPos(plnNo, gom.width / 2, gom.height / 3);
		//sp.setPos(plnNo, 0, 0);
		
		y = 2;
		
		sp.playBGM(Sprite.BGM.TITLE);
	}

	@Override
	protected void mainAction() {
		if(selected) {
			sp.playSE(Sprite.Preset.SELECT);
			
			destructor();
			
			gom.addGO(InterMission.class, 0, 0);
		}
	}
	
}