package srpg;

import game.Sprite;

import java.awt.image.BufferedImage;


public class Field {
	
	public final FieldData fd;
	public final int height;
	public final BufferedImage[] graphic;
	
	public Field(FieldData fd, int height, Sprite sp) {
		this.fd = fd;
		this.height = height;
		graphic = new BufferedImage[3];
		for(int i = 0; i < graphic.length; i++) {
			sp.addGrp(fd.graphicName[i]);
			graphic[i] = sp.getGrp(fd.graphicName[i]);
		}
	}
	
}
