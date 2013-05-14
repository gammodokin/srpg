package srpg.map.ui.info;

import game.Sprite;
import srpg.SRPGGameObjectManager;
import srpg.UnitStatus;
import srpg.map.StageMap;
import srpg.map.obj.Unit;

public class UnitInfo extends MapInformation {
	
	protected int realHeight;

	public UnitInfo(SRPGGameObjectManager gom, Sprite sp, int plnNo, int x, int y, StageMap m) {
		super(gom, sp, plnNo, x, y, m);
		
		init(gom.width * 2/5, 100);
		realHeight = 80;
		title = "UnitInfo";
		//sds = initSds();
		
		sp.setPos(plnNo, 0, gom.height - realHeight);
	}
	
	protected StrData[] initSds() {
		return new StrData[]{
				new StrData("", width / 8, realHeight / 4 + 5),
				new StrData("HP : ", width / 8, realHeight * 2 / 4 + 5),
				new StrData("SP : ", width / 8, realHeight * 3 / 4 + 5),
				new StrData("Str : ", width * 8 / 16 - 6, realHeight * 2 / 4 + 5), 
				new StrData("Agi : ", width * 8 / 16 - 6, realHeight * 3 / 4 + 5),
				new StrData("Hit : ", width * 11 / 16, realHeight * 2 / 4 + 5), 
				new StrData("Flee : ", width * 11 / 16, realHeight * 3 / 4 + 5),};
	}
	
	protected void setData(Unit unit) {
		UnitStatus st = unit.getStatus();
		sds[0].str += unit.getName();
		sds[1].str += st.hp + " / " + st.maxHP;
		sds[2].str += st.sp + " / " + st.maxSP;
		sds[3].str += st.capa.strength;
		sds[4].str += st.capa.agility;
		sds[5].str += st.hit;
		sds[6].str += st.flee;
	}
	
	protected Unit currentUnit;
	public void mainMotion() {
		currentUnit = map.current;
		if(currentUnit == null) {
			sp.setView(plnNo, false);
			return;
		}
		sp.setView(plnNo, true);
		sds = initSds();
		
		setData(currentUnit);
		
	}
	
}
