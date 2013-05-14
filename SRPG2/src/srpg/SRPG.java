package srpg;

import game.RunnableCanvas;
import game.Sprite;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.net.URL;

import srpg.screen.Title;

public class SRPG extends RunnableCanvas{

	enum Mode{START_INIT, START, INIT, PLAYING};
	
	private Sprite sp;
	private SRPGGameObjectManager gom;
	
	private Mode mode;
	
	//private URL codeBase;
	//private String location = "res/";
	
	public SRPG(int w, int h, Container co, URL codeBase) {
		super(w, h, co, codeBase);
		SPEED = 1000 / 30;
		
		//this.codeBase = codeBase;
		
		
		
		sp = new Sprite(co, codeBase.toString(), this);
		gom = new SRPGGameObjectManager(sp, kb.keyQ, CANVAS_W, CANVAS_H, codeBase.toString());
		
		mode = Mode.START_INIT;
		
		bg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	}

	BufferedImage bg;
	protected void doubleBufPaint(Graphics g) {
		switch(mode) {
		case START_INIT:
			gom.addGO(Title.class, 0, 0);
			mode = Mode.PLAYING;
			break;
		/*case START:
			if(gom.isPlayable())
				gom.play();
			else
				mode = Mode.INIT;
			break;
		case INIT:
			

			mode = Mode.PLAYING;*/
		case PLAYING:
			if(gom.isPlayable())
				gom.play();
			else
				mode = Mode.START_INIT;
		}
		
		
		Graphics2D g2d = bg.createGraphics();
		sp.paint(g2d, bg);
		g2d.dispose();
		
		g.drawImage(bg, 0, 0, null);
	}

}
