package game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.net.URL;

import myutil.Queue;

public abstract class RunnableCanvas extends Canvas implements Runnable {
	
	protected Keyboard kb;
	
	protected int SPEED = 1000 / 30;

	protected int CANVAS_W, CANVAS_H;
	
	private Thread thread = null;
	
	abstract class PaintMode{
		
		abstract void init();
		
		abstract void destructor();
		
		abstract void paint(Graphics g);
		
		abstract void update(Graphics g);
		
		abstract void painting();
	}
	final PaintMode BUFFER_STRATEGY = new PaintMode(){
		
		private static final int NUM_BUFFERS = 2;
		BufferStrategy bs = null;
		
		@Override
		void init() {
			if(bs == null) {
				initBS();
			}
		}

		@Override
		void paint(Graphics g) {
			superPaint(g);
		}

		@Override
		void painting() {
			render();
			screenUpdate();
		}

		@Override
		void update(Graphics g) {
			superUpdate(g);
		}

		@Override
		void destructor() {
			bs = null;
		}
		
		private void initBS() {
			createBufferStrategy(NUM_BUFFERS);
			bs = getBufferStrategy();
		}
		
		private void render() {
			Graphics g = bs.getDrawGraphics();
			//clear(g);
			doubleBufPaint(g);
			drawTrace(g);
			
			g.dispose();
		}
		
		private void screenUpdate() {
			if (!bs.contentsLost()) {
				bs.show();
			} else {
				System.out.println("BufferStrategy : Contents Lost");
				initBS();
			}
			Toolkit.getDefaultToolkit().sync();
		}
		
		@Override
		public String toString() {
			return "BufferStrategy";
		}
		
	}, SELF_BUFFER = new PaintMode(){
		@Override
		void init() {}
		
		@Override
		void paint(Graphics g) {
			update(g);
		}

		@Override
		void painting() {
			repaint();
		}

//		private Image backGrp;
		@Override
		void update(Graphics g) {
//			Graphics gbg;
//			if (backGrp == null)
//				backGrp = createImage(CANVAS_W, CANVAS_H);
//			gbg = backGrp.getGraphics();
//			//clear(gbg);
//			doubleBufPaint(gbg);
//			drawTrace(gbg);
//			gbg.dispose();
//			g.drawImage(backGrp, 0, 0, null);
			
			doubleBufPaint(g);
			drawTrace(g);
		}

		@Override
		void destructor() {}
		
		@Override
		public String toString() {
			return "SelfBuffer";
		}
	};
	
	PaintMode pm;
	
	boolean traceView = false;

	public RunnableCanvas(int w, int h, Container co, URL codaBase) {
		
		CANVAS_W = w;
		CANVAS_H = h;

		pm = SELF_BUFFER;
		
		kb = new Keyboard();
	}
	
	public void setTraceView(boolean b) {
		traceView = b;
	}
	
	public void setPaintMode(PaintMode pm) {
		//this.pm.destructor();
		pm.init();
		this.pm = pm;
	}
	
	public PaintMode getPaintMode() {
		return pm;
	}
	
	public void start() {
		if(thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}
	
	public void stop() {
		thread = null;
		//bs = null;
	}

	long sleepTime = 0, pt = 0;
	int count = 0;
	public void run() {
		long processTime = 0;
		while (thread != null) {
			pm.init();
			processTime = System.currentTimeMillis();
			pm.painting();
			
			pt = processTime = System.currentTimeMillis() - processTime;
			sleepTime = SPEED - processTime;
			if (sleepTime >= 0) {
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				count = 0;
			} else if(processTime > 200) {
				count++;
				if(count > 10) {
					pm = SELF_BUFFER;
				}
			}
			
		}
	}
	
	@Override
	public void update(Graphics g) {
		pm.update(g);
	}
	
	private void superUpdate(Graphics g) {
		super.update(g);
	}

	@Override
	public void paint(Graphics g) {
		pm.paint(g);
		requestFocus();
	}
	
	private void superPaint(Graphics g) {
		super.paint(g);
	}

	protected void clear(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, CANVAS_W, CANVAS_H);
		g.setColor(Color.BLACK);
	}
	
	private void drawTrace(Graphics g) {
		if(traceView) {
			g.setColor(Color.BLACK);
			g.drawString("sleepTime : " + sleepTime + "\n", 0, 10);
			g.drawString("processTime : " + pt + "\n", 120, 10);
			g.drawString("PaintMode : " + pm, 240, 10);
		}
	}

	protected abstract void doubleBufPaint(Graphics g);
	
	
	public class Keyboard implements KeyListener{
		
		public final Queue<Key> keyQ;
		private final int KEY_DELAY = 2;
		private final int KEY_SPEED = 100;
		
		boolean up, down, left, right;
		
		private KeyRepeater kr;
		
		Keyboard() {
			addKeyListener(this);
			keyQ = new Queue<Key>();
			kr = new KeyRepeater();
			kr.start();
		}
	
		public void keyTyped(KeyEvent e) {}
	
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_UP:
				if(!up)
					keyQ.enqueue(Key.UP);
				up = true;
				break;
			case KeyEvent.VK_DOWN:
				if(!down)
					keyQ.enqueue(Key.DOWN);
				down = true;
				break;
			case KeyEvent.VK_LEFT:
				if(!left)
					keyQ.enqueue(Key.LEFT);
				left = true;
				break;
			case KeyEvent.VK_RIGHT:
				if(!right)
					keyQ.enqueue(Key.RIGHT);
				right = true;
				break;
			}
		}
	
		public void keyReleased(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_UP:
			case KeyEvent.VK_KP_UP:
				up = false;
				break;
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_KP_DOWN:
				down = false;
				break;
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_KP_LEFT:
				left = false;
				break;
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_KP_RIGHT:
				right = false;
				break;
			case KeyEvent.VK_ENTER:
			case KeyEvent.VK_SPACE:
				keyQ.enqueue(Key.SELECT);
				break;
			case KeyEvent.VK_ESCAPE:
			case KeyEvent.VK_BACK_SPACE:
			case KeyEvent.VK_DELETE:
				keyQ.enqueue(Key.CANCEL);
				break;
			}
		}
	
		class KeyRepeater extends Thread {
			
			public void run() {
				long processTime;
				long upCount, downCount, leftCount, rightCount;
				upCount = downCount = leftCount = rightCount = 0;
				
				while(true) {
					processTime = System.currentTimeMillis();
					if(up) {
						if(upCount > KEY_DELAY)
							keyQ.enqueue(Key.UP);
						else
							upCount++;
					} else
						upCount = 0;
					
					if(down) {
						if(downCount > KEY_DELAY)
							keyQ.enqueue(Key.DOWN);
						else
							downCount++;
					} else
						downCount = 0;
					
					if(left) {
						if(leftCount > KEY_DELAY)
							keyQ.enqueue(Key.LEFT);
						else
							leftCount++;
					} else
						leftCount = 0;
					
					if(right) {
						if(rightCount > KEY_DELAY)
							keyQ.enqueue(Key.RIGHT);
						else
							rightCount++;
					} else
						rightCount = 0;
					
					processTime = System.currentTimeMillis() - processTime;
					if(KEY_SPEED - processTime > 0)
						try {
							sleep(KEY_SPEED - processTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
				}
			}
			
		}
	}
}

/*
public abstract class RunnableApplet extends Applet implements Runnable {
	
	protected int SPEED = 1000 / 30;

	protected int CANVAS_W, CANVAS_H;
	
	private Thread thread = null;
	
	private static final int NUM_BUFFERS = 2; // BufferStrategyのバッファの数

	public void init() {
		String val = "";
		try {
			val = System.setProperty("Dsun.java2d.opengl", "True");
		} catch(Exception e){
			System.out.println("OpenGLを利用するためのシステムプロパティの設定に失敗しました");
			e.printStackTrace();
		}
		//System.out.println(val);
		
		CANVAS_W = getSize().width;
		CANVAS_H = getSize().height;
		
		//createBufferStrategy(NUM_BUFFERS);
	}
	
	public void start() {
		if(thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}
	
	public void stop() {
		thread = null;
	}

	public void run() {
		long processTime = 0;
		while (thread != null) {
			processTime = System.currentTimeMillis();
			repaint();
			processTime = System.currentTimeMillis() - processTime;
			if (SPEED - processTime >= 0)
				try {
					Thread.sleep(SPEED - processTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		}
	}

	private Image backGrp;
	public void update(Graphics g) {
		Graphics gbg;
		if (backGrp == null)
			backGrp = createImage(CANVAS_W, CANVAS_H);
		gbg = backGrp.getGraphics();

		doubleBufPaint(gbg);

		gbg.dispose();
		g.drawImage(backGrp, getInsets().top, getInsets().left, this);
	}

	public void paint(Graphics g) {
		update(g);
		requestFocus();
	}

	protected void clear(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, CANVAS_W, CANVAS_H);
		g.setColor(Color.BLACK);
	}

	protected abstract void doubleBufPaint(Graphics g);
}
*/
