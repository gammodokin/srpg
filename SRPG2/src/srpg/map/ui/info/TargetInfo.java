package srpg.map.ui.info;

import srpg.SRPGGameObjectManager;
import srpg.map.StageMap;
import game.Sprite;

public class TargetInfo extends UnitInfo {

	public TargetInfo(SRPGGameObjectManager gom, Sprite sp, int plnNo, int x, int y, StageMap m) {
		super(gom, sp, plnNo, x, y, m);
		
		title = "TargetInfo";
		
		sp.setPos(plnNo, gom.width - width, gom.height - realHeight);
	}
	
	public void mainMotion() {
		currentUnit = map.unitMap[current.y][current.x];
		if(currentUnit == null || currentUnit == map.current) {
			sp.setView(plnNo, false);
			return;
		}
		sp.setView(plnNo, true);
		sds = initSds();
		
		setData(currentUnit);
		
	}
	
}
