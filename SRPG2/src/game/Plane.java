package game;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import myutil.Coord;

public class Plane {
	
	boolean view = true;
	boolean animation = false;
	boolean repeat = false;
	
	BlendMode mode = BlendMode.NORMAL;
	LinkedList<Trail> trails = new LinkedList<Trail>();
	boolean trail = false;
	
	public Coord<Double> coord = new Coord<Double>(0d, 0d);
	double z;
	
	public List<BufferedImage> imgs = new ArrayList<BufferedImage>();
	int animeCount = 0;

	Draw draw;
	
	public int x() {
		return coord.x.intValue();
	}
	
	public int y() {
		return coord.y.intValue();
	}

	BufferedImage nextImg() {
		if(animation && (repeat || animeCount < imgs.size())) {
			animeCount++;
			if(repeat)
				animeCount %= imgs.size();
			else if(!trail)
				animeCount = Math.min(animeCount, imgs.size() - 1);
		}
		
		BufferedImage img = null;
		if(animeCount < imgs.size())
			img = imgs.get(animeCount);
		
		if(trail) {
			trails.poll();
			
			if(img == null) {
				trails.add(null);
				
				boolean allNull = false;
				if(trails.peekFirst() == null) {
					allNull = true;
					for(Trail trail : trails)
						allNull = allNull && trail == null;
				}
				
				trail = !allNull;
				
				return img;
			} else
				trails.add(new Trail(coord.clone(), img));
			
			AlphaComposite clear = AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f);
			AlphaComposite so = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1 - 1.0f / trails.size());
			for(Trail trail : trails) {
				if(trail == null)
					continue;
				
				BufferedImage dimg = new BufferedImage(trail.img.getWidth(), trail.img.getHeight(), BufferedImage.TYPE_INT_ARGB);
				
				Graphics2D g2D = dimg.createGraphics();
				g2D.setComposite(clear);
				Rectangle2D.Double rect = new Rectangle2D.Double(0,0,dimg.getWidth(),dimg.getHeight()); 
				g2D.fill(rect);
				
				g2D.setComposite(so);
				g2D.drawImage(trail.img, 0, 0, null);
				g2D.dispose();
				
				trail.img = dimg;
			}
		}
		
		return img;
	}
	
	void initTrailCount(int count) {
//		trailCount = count;
		trail = true;
		
		for(int i = 0; i < count; i++)
			trails.add(null);
	}
	
	class Trail {
		Coord<Double> c;
		BufferedImage img;
		
		Trail(Coord<Double> c, BufferedImage img) {
			super();
			this.c = c;
			this.img = img;
		}
		
	}
	
//	public void initImage(int width, int height) {
//		//if(img == null)
//		initImage(0, width, height);
//		//else
//			//Sprite.setTransparent(img, img.createGraphics());
//	}
//	
//	public void initImage(int index, int width, int height) {
//		imgs.add(index, new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));
//	}
//	
//	public Graphics2D getGraphics() {
//		return getGraphics(0);
//	}
//	
//	public Graphics2D getGraphics(int index) {
//		BufferedImage img = imgs.get(index);
//		Graphics2D g2D = img.createGraphics();
//		Sprite.setTransparent(img, g2D);
//		return 	g2D;
//	}
	
}
