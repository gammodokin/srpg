package game;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import myutil.Coord;
import myutil.SortedMap;

public class Sprite {

	private final boolean JAR = false;

	private final String imageLocation = "/res/";
	private final String imageExtention = "png";

	private String codeBase;
	private RunnableCanvas canvas;

	private SortedMap<Integer, Plane> plns;
	private int plnID;
	private Comparator<Plane> compPln;
	private Map<String, BufferedImage> grps;
	private MediaTracker tracker;

	private static final String soundLocation = "/res/", musicLocation = "/res/";
	private static final String soundExtention = "wav", musicExtention = "mp3";

	private Map<String, AudioClip> adcs;
	private Map<String, Music> musics;
	private String bgm;

	public enum Preset{
		SELECT("poi"), CANCEL("poire"), FAILURE("po2"), MOVE("mokin");

		private final String file;

		Preset(String file) {
			this.file = file;
		}

		public String getFile() {
			return file;
		}
	}

	public enum BGM {
		TITLE("free0341"), STAGE1("free17"), STAGE2("free0563"), STAGE3("free0320"), WIN("free0227"), LOSE("free0303");

		private final String file;

		BGM(String file) {
			this.file = file;
		}

		public String getFile() {
			return file;
		}
	}

	public static final String SHOOT = "tm2r_gun18_d", DAMAGE = "bashi2", SUPPORT = "power35",
		EXPLODE_S = "tm2_bom001", EXPLODE_M = "tm2r_bom31_b", MISS = "fin", LASER_S = "tm2_laser000", LASER_M = "tm2_laser001",
		CHARGE_S = "eco00_r", CHARGE_M = "eco03", CURE = "tm2_power000";


	private class Music implements Runnable{

		private Thread thread = null;

		private final String file;
		private SequentialPlayer player = null;
		private BufferedInputStream stream;

		Music(String file) {
			this.file = file;
		}

		private void init() throws FileNotFoundException, JavaLayerException {
//			System.out.println(getClass().getResourceAsStream(Sprite.musicLocation + file + "." + Sprite.musicExtention));
			stream = new BufferedInputStream(getClass().getResourceAsStream(Sprite.musicLocation + file + "." + Sprite.musicExtention));//new FileInputStream(codeBase.substring(6) + Sprite.soundLocation + file + "." + Sprite.musicExtention));
			player = new SequentialPlayer(stream);
		}

		public boolean isPlaying() {
			return thread != null;
		}

		public void play() {
			if(thread == null)
				thread = new Thread(this);
			thread.start();
		}

		public void stop() {
			player.close();

			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			thread = null;
		}

		public void run() {
			boolean finish = false;
			while(thread != null) {
				try {
					if(player == null || finish)
						init();

					finish = !player.decodeFrame();
				} catch (JavaLayerException e) {
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}

		private class SequentialPlayer extends Player{

			public SequentialPlayer(InputStream arg0) throws JavaLayerException {
				super(arg0);
			}

			// decodeFrame�ｽﾌ可趣ｿｽ�ｽ�ｽ�ｽ�ｽpublic�ｽﾉゑｿｽ�ｽ�ｽ
			@Override
			public boolean decodeFrame() throws JavaLayerException {
				return super.decodeFrame();
			}

		}
	}

	public Sprite(Container con, String codeBase, RunnableCanvas rc) {
		init();

		this.codeBase = codeBase;
		this.canvas = rc;

		compPln = new Comparator<Plane>() {

			public int compare(Plane arg0, Plane arg1) {
				return (int)Math.signum((arg0 == null ? 0 : arg0.z) - (arg1 == null ? 0 : arg1.z));
			}

		};

		grps = new HashMap<String, BufferedImage>();
		tracker = new MediaTracker(con);

		adcs = new HashMap<String, AudioClip>();

		for(Preset ps : Preset.values())
			addSound(ps.getFile());

		addSound(SHOOT);
		addSound(DAMAGE);
		addSound(SUPPORT);
		addSound(EXPLODE_S);
		addSound(EXPLODE_M);
		addSound(MISS);
		addSound(LASER_S);
		addSound(LASER_M);
		addSound(CHARGE_S);
		addSound(CHARGE_M);
		addSound(CURE);

		musics = new HashMap<String, Music>();
		for(BGM bgm : BGM.values())
			addMusic(bgm);
	}

	public void init() {
		plnID = 0;
		plns = new SortedMap<Integer, Plane>(compPln);//new Plane[250];
	}

	public boolean addGrp(String file) {
		if(grps.containsKey(file))
			return true;

		//System.out.println(codeBase + imageLocation + file + "." + imageExtention);
		try {
			if(JAR)
			// "jar:"�ｽｪに付�ｽ�ｽ�ｽﾈゑｿｽ�ｽﾆ、jar�ｽ�ｽ�ｽﾌ画像�ｽ�ｽﾇみ搾ｿｽ�ｽﾟなゑｿｽ
				grps.put(file, ImageIO.read(new URL("jar:" + codeBase + "srpg.jar!" + imageLocation + file + "." + imageExtention)));
			else
				grps.put(file, ImageIO.read(new URL(codeBase + imageLocation + file + "." + imageExtention)));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		tracker.addImage((Image)(grps.get(file)), 1);
		waitLoad();
		return true;
	}

	public void switchTraceView() {
		canvas.traceView = !canvas.traceView;
	}

	public void setPaintMode(RunnableCanvas.PaintMode pm) {
		canvas.setPaintMode(pm);
	}

	public RunnableCanvas.PaintMode getPaintMode() {
		return canvas.getPaintMode();
	}

	public void switchPaintMode() {
		if(canvas.getPaintMode() == canvas.BUFFER_STRATEGY)
			canvas.setPaintMode(canvas.SELF_BUFFER);
		else
			canvas.setPaintMode(canvas.BUFFER_STRATEGY);
	}

	public boolean waitLoad() {
		try {
			tracker.waitForID(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public BufferedImage getGrp(String file) {
		return grps.get(file);
	}

	public boolean addSound(String file) {
		if(adcs.containsKey(file))
			return true;

		try {
			if(JAR)
				adcs.put(file, Applet.newAudioClip(new URL("jar:" + codeBase + "srpg.jar!" + soundLocation + file + "." + soundExtention)));
			else
				adcs.put(file, Applet.newAudioClip(new URL(codeBase + soundLocation + file + "." + soundExtention)));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}//getClass().getResource(file + "." + soundExtention)));
		return true;
	}

	public boolean addMusic(BGM bgm) {
		return addMusic(bgm.getFile());
	}
	public boolean addMusic(String file) {
		if(musics.containsKey(file))
			return true;

		musics.put(file, new Music(file));
		return true;
	}

	public void switchBGM() {
		if(musics.get(bgm).isPlaying())
			stopBGM();
		else
			playBGM();
	}

	public void playBGM(BGM bgm) {
		playBGM(bgm.getFile());
	}
	public void playBGM(String file) {
		if(bgm != null && bgm.equals(file))
			return;

		stopBGM();
		bgm = file;
		playBGM();
	}
	public void playBGM() {
		musics.get(bgm).play();
	}

	public void stopBGM() {
//		Music music = musics.get(bgm);
		if(bgm != null)
			musics.get(bgm).stop();
	}

	public void playSE(Preset ps) {
		playSE(ps.getFile());
	}
	public void playSE(String file) {
		adcs.get(file).play();
	}

	public int newPln() {
		Plane p = new Plane();

		plnID++;
		plns.put(plnID, p);

		return plnID;
	}

	public void delPln(int plnNo) {
		plns.remove(plnNo);//plns[plnNo] = null;
	}

	public void initImg(int plnNo, int width, int height) {
		initImgs(plnNo, 1, width, height);
	}

	public void initImgs(int plnNo, int count, int width, int height) {
		Plane pln = plns.get(plnNo);

		for(int i = 0; i < count; i++) {
			BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			setTransparent(img);
			pln.imgs.add(img);
		}
	}
	public void initImgs(int plnNo) {
		Plane pln = plns.get(plnNo);
		pln.imgs.clear();

		pln.animation = false;
		pln.animeCount = 0;
	}

	public void setTransparent(int plnNo) {
		setTransparent(plnNo, 0);
	}
	public void setTransparent(int plnNo, int index) {
		setTransparent(plns.get(plnNo).imgs.get(index));
	}

	public void setImg(int plnNo, String file) {
		setImg(plnNo, 0, file);

		plns.get(plnNo).animation = false;
	}
	public void setImg(int plnNo, int index, String file) {
		if(!plns.containsKey(plnNo)) {//if (plns[plnNo] == null) {
			printError("setImg()");
			return;
		}

		if(file == null)
			plns.get(plnNo).imgs.add(index, null);
		else
			plns.get(plnNo).imgs.add(index, grps.get(file));
	}

	public void setAnimeCount(int plnNo, int count) {
		plns.get(plnNo).animeCount = count;
	}

	public void setAnimeImg(int plnNo, String file, int rowCount) {
		setAnimeImg(plnNo, file, rowCount, 5);
	}
	public void setAnimeImg(int plnNo, String file, int rowCount, int colCount) {
		Plane pln = plns.get(plnNo);

		setAnime(plnNo, true);

		addGrp(file);
		BufferedImage img = grps.get(file);

		int w = img.getWidth(), h = img.getHeight();
		int ew = w/colCount, eh = h/rowCount;

		for(int row = 0; row < rowCount; row++)
			for(int col = 0; col < colCount; col++) {
				pln.imgs.add(img.getSubimage(col * ew, row * eh, ew, eh));
			}
	}

	public void setAnime(int plnNo, boolean anime) {
		plns.get(plnNo).animation = anime;
	}

	public void setPos(int plnNo, Coord<Double> cood) {
		setPos(plnNo, cood.x, cood.y);
	}
	public void setPos(int plnNo, double x, double y) {
		if (!plns.containsKey(plnNo)) {
			printError("setPos(x, y)");
			return;
		}

		plns.get(plnNo).coord.x = x;
		plns.get(plnNo).coord.y = y;
	}

	public void setPos(int plnNo, double z) {
		if (!plns.containsKey(plnNo)) {
			printError("setPos(z)");
			return;
		}

		plns.get(plnNo).z = z;
		plns.update(plnNo);
	}

	public void setMov(int plnNo, Coord<Double> cood) {
		setMov(plnNo, cood.x, cood.y);
	}
	public void setMov(int plnNo, double x, double y) {
		if (!plns.containsKey(plnNo)) {
			printError("setMove()");
			return;
		}

		plns.get(plnNo).coord.x += x;
		plns.get(plnNo).coord.y += y;
	}

	public void setDraw(int plnNo, Draw draw) {
		if (!plns.containsKey(plnNo)) {
			printError("setDraw()");
			return;
		}

		plns.get(plnNo).draw = draw;
		plns.get(plnNo).view = true;
	}

	public void setView(int plnNo, boolean view) {
		if (!plns.containsKey(plnNo)) {
			printError("setView()");
			return;
		}
		plns.get(plnNo).view = view;
	}

	public void setBlendMode(int plnNo, BlendMode bm) {
		plns.get(plnNo).mode = bm;
	}

	public void setTrailCount(int plnNo, int count) {
		plns.get(plnNo).initTrailCount(count);
	}

	public Coord<Double> getPos(int plnNo) {
		if (!plns.containsKey(plnNo)) {
			printError("getPos()");
			throw new AssertionError();
		}
		return plns.get(plnNo).coord.clone();
	}

	public Draw getDraw(int plnNo) {
		if (!plns.containsKey(plnNo)) {
			printError("getDraw()");
			return null;
		}
		return plns.get(plnNo).draw;
	}

	public boolean getView(int plnNo) {
		if (!plns.containsKey(plnNo)) {
			printError("getView()");
			return false;
		}
		return plns.get(plnNo).view;
	}

	public boolean isAnimeClosed(int plnNo) {
		Plane pln = plns.get(plnNo);

		return (pln.animeCount >= pln.imgs.size() - 1 || !pln.animation) && !pln.trail;
	}

	public Coord<Integer> getImgSize(int plnNo) {
		return getImgSize(plnNo, 0);
	}
	public Coord<Integer> getImgSize(int plnNo, int index) {
		BufferedImage img = plns.get(plnNo).imgs.get(index);
		return new Coord<Integer>(img.getWidth(), img.getHeight());
	}

	public Graphics2D createGraphics(int plnNo) {
		return createGraphics(plnNo, 0);
	}
	public Graphics2D createGraphics(int plnNo, int index) {
		return plns.get(plnNo).imgs.get(index).createGraphics();
	}

	public void resizeImg(int plnNo, int width, int height) {
		int size = plns.get(plnNo).imgs.size();
		for(int i = 0; i < size; i++)
			resizeImg(plnNo, i, width, height);
	}
	public void resizeImg(int plnNo, int index, int width, int height) {
		Plane pln = plns.get(plnNo);
		BufferedImage img = pln.imgs.get(index);

		if(width <= 0)
			width = img.getWidth() * height / img.getHeight();
		else if(height <= 0)
			height = img.getHeight() * width / img.getWidth();

		BufferedImage newImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		Graphics g = newImg.createGraphics();
		g.drawImage(img, 0, 0, width, height, null);
		g.dispose();
		pln.imgs.set(index, newImg);
	}


	public void flipImg(int plnNo) {
		flipImg(plnNo, 0);
	}
	public void flipImg(int plnNo, int index) {
		Plane pln = plns.get(plnNo);
		BufferedImage img = pln.imgs.get(index);

		BufferedImage newImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);

		Graphics g = newImg.createGraphics();
		g.drawImage(img, 0, 0, img.getWidth(), img.getHeight(), img.getWidth(), 0, 0, img.getHeight(), null);
		g.dispose();
		pln.imgs.set(index, newImg);
	}

	public void paint(Graphics2D g, BufferedImage bg) {
		BlendMode.setBaseImg(bg);
		for (Plane pln : plns.getSorted()) {
			if (pln == null)
				continue;

//			if (pln.draw == null) {
//				System.out.println("draw is null : " + pln.z);
//				continue;
//			}
			if(pln.view) {
				if(!pln.imgs.isEmpty()) {
					BufferedImage img = pln.nextImg();
					if(img != null)
						pln.mode.drawImage(img, pln.x(), pln.y(), g);
				}

				if(pln.trail)
					for(Plane.Trail trail : pln.trails) {
						if(trail != null)
							pln.mode.drawImage(trail.img, trail.c.x.intValue(), trail.c.y.intValue(), g);
					}
//				if(pln.imgs.size() > 0)
//					pln.mode.drawImage(pln.imgs.get((int)pln.animeCount++ % pln.imgs.size()), pln.x(), pln.y(), g);
				if(pln.draw != null)
					pln.draw.drawing(g, pln);
			}
		}
	}

	public static void drawShadedString(String str, int x, int y, Graphics g) {
		drawShadedString(str, x, y, Color.LIGHT_GRAY, g);
	}

	public static void drawShadedString(String str, int x, int y, Color shade, Graphics g) {
		Color c = g.getColor();
		g.setColor(shade);
		g.drawString(str, x + 1, y + 1);
		g.setColor(c);
		g.drawString(str, x, y);
	}

	public static void drawDesignedRect(int x, int y, int width, int height, Graphics g) {
		int offX = 20, offY = 8;
		Graphics2D g2D = (Graphics2D)g;

		g2D.setColor(new Color(0x99ccccff, true));
		g2D.fillRoundRect(x, y, width, height, 40, 20);

		g.setColor(new Color(0xbbaaaaee, true));
		g2D.drawRoundRect(x, y, width, height, 40, 20);
		g2D.drawRoundRect(x + offX, y + offY, width - offX*2, height - offY*2, 8, 8);

		g2D.setColor(new Color(0x44ffffff, true));
		g2D.fillRoundRect(x + offX, y + offY, width - offX*2, height - offY*2, 8, 8);
	}

	private void printError(String str) {
		System.out.println("error : no Plane in " + str);
	}

	public static void setTransparent(BufferedImage img) {
		Graphics2D g2D = img.createGraphics();
		g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f));
		Rectangle2D.Double rect = new Rectangle2D.Double(0,0,img.getWidth(),img.getHeight());
		g2D.fill(rect);
		g2D.dispose();
//		g2D.setPaintMode();
	}

}

