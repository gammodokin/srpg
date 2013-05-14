package srpg.map.ui.info;

import game.Sprite;
import srpg.Field;
import srpg.SRPGGameObjectManager;
import srpg.map.StageMap;

public class GeoInfo extends MapInformation {
	
	int realWidth;

	public GeoInfo(SRPGGameObjectManager gom, Sprite sp, int plnNo, int x, int y, StageMap m) {
		super(gom, sp, plnNo, x, y, m);
		
		int offX = -25;
		init(gom.width * 2/5 - offX, 35);
		realWidth = width + offX;
		title = "        GeoInfo";
		//sds = initSds();
		
		sp.setPos(plnNo, offX, gom.height - height - 80);
	}
	
	private StrData[] initSds() {
		return new StrData[]{
				new StrData("à⁄ìÆ : ", realWidth * 3 / 16, height * 2 / 3),
				new StrData("çÇÇ≥ : ", realWidth * 7 / 16, height * 2 / 3),
				new StrData("îÌÉ_ÉÅ : ", realWidth * 10 / 16 + 5, height * 2 / 3),};
	}
	
	public void mainMotion() {
		sds = initSds();
		Field f = map.hex[current.y][current.x];
		
		sds[0].str += f.fd.cost < 0 ? "--" : f.fd.cost;
		sds[1].str += f.height;
		sds[2].str += "Å~" + f.fd.damageEffect;
	}
	
}
