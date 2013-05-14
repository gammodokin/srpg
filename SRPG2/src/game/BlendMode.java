package game;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public enum BlendMode {
	NORMAL {
		@Override
		void drawImage(BufferedImage img, int x, int y, Graphics2D g) {
			g.drawImage(img, x, y, null);
		}

		@Override
		int blend(int alpha, int d, int s) {
			return 0;
		}
	}, ADDITIVE {
		@Override
		protected int blend(int alpha, int d, int s) {
			d &= 0xff;
			int r = d + (0xff & s) * alpha/0xff;
			r = overflow(r);
			
			return r;
		}
	}, SCREEN {
		@Override
		int blend(int alpha, int d, int s) {
			d &= 0xff;
			int r = d + (0xff & s) * d * alpha/0xff/0xff;
			r = overflow(r);
			
			return r;
		}
	}, DODGE {
		@Override
		int blend(int alpha, int d, int s) {
			d &= 0xff;
			s &= 0xff;
//			s = (0xff - s * alpha/0xff);
			int r = d * 0x100 / (0xff - s * alpha/0xff + 1);
			r = overflow(r);
			
			return r;
		}
	};
	
	static protected BufferedImage dimg;
	//private int[] baseRGBs;
	
	// ï`âÊëŒè€ÉCÉÅÅ[ÉW
	static void setBaseImg(BufferedImage img) {
		dimg = img;
		//bimg.getRGB(0, 0, bimg.getWidth(), bimg.getHeight(), baseRGBs, 0, baseRGBs.length);
	}
	
	void drawImage(BufferedImage img, int offX, int offY, Graphics2D graphics) {
		int width = img.getWidth(), height = img.getHeight();

		if(offX + width < 0 || dimg.getWidth() < offX
				|| offY + height < 0 || dimg.getHeight() < offY)
			return;

		int clipOffX = offX < 0 ? 0 : offX;
		int clipOffY = offY < 0 ? 0 : offY;
		int clipW, clipH;
		
		clipW = offX < 0 ? width + offX : width;
		clipH = offY < 0 ? height + offY : height;
		
		clipW = offX + width > dimg.getWidth() ? dimg.getWidth() - clipOffX : clipW;
		clipH = offY + height > dimg.getHeight() ? dimg.getHeight() - clipOffY : clipH;
		
		clipW = Math.min(dimg.getWidth(), clipW);
		clipH = Math.min(dimg.getHeight(), clipH);
		
		offX = offX < 0 ? -offX : 0;
		offY = offY < 0 ? -offY : 0;
		width = width - offX;
		height = height - offY;
		
		int[] srcRGBs = new int[clipW * clipH];
		img.getRGB(offX, offY, clipW, clipH, srcRGBs, 0, clipW);
		
		int[] destRGBs = new int[clipW * clipH];
		dimg.getRGB(clipOffX, clipOffY, clipW, clipH, destRGBs, 0, clipW);
		
		for(int i = 0; i < clipW * clipH; i++) {
			int srcRGB = srcRGBs[i];
			
			int alpha = (srcRGB >>> 6*4) & 0xff;
			
			int r = blend(alpha, destRGBs[i] >>> 4*4, srcRGB >>> 4*4);
			int g = blend(alpha, destRGBs[i] >>> 2*4, srcRGB >>> 2*4);
			int b = blend(alpha, destRGBs[i], srcRGB);
			
			destRGBs[i] = 0xff000000 | r << 4*4 | g << 2*4 | b;
		}
		
		dimg.setRGB(clipOffX, clipOffY, clipW, clipH, destRGBs, 0, clipW);
	
	}
	
	protected int overflow(int param) {
		return param > 0xff ? 0xff : param;
	}
	
	abstract int blend(int alpha, int d, int s);
}
