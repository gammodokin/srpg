package srpg.map.ui.info;

import game.Sprite;

import java.awt.Color;

import myutil.Coord;
import srpg.Action;
import srpg.Damage;
import srpg.SRPGGameObjectManager;
import srpg.map.StageMap;
import srpg.map.UnitType;
import srpg.map.obj.Unit;

public class ActionInfo extends MapInformation {
	
	int realHeight;

	public ActionInfo(SRPGGameObjectManager gom, Sprite sp, int plnNo, int x, int y, StageMap m) {
		super(gom, sp, plnNo, x, y, m);
		
		init(gom.width * 1/5, 100);
		realHeight = 80;
		title = "       ＞＞＞＞";
		//sds = initSds();
		
		sp.setPos(plnNo, gom.width * 2/5, gom.height - realHeight);
	}
	
	private StrData[] initSds() {
		int xOff = width * 3 / 16 + 2, scale = width * 2/16;
		return new StrData[]{
				new StrData("HIT:", xOff, realHeight / 4 + 5),
				new StrData("HP :", xOff, realHeight * 2 / 4 + 5),
				new StrData("SP :", xOff, realHeight * 3 / 4 + 5),
				new StrData("", xOff + scale * 5/2, realHeight / 4 + 5),
				new StrData("", xOff + scale * 3/2, realHeight * 2 / 4 + 5),
				new StrData("", xOff + scale * 3/2, realHeight * 3 / 4 + 5),
				new StrData("|", xOff + scale * 3, realHeight * 2 / 4 + 5),
				new StrData("|", xOff + scale * 3, realHeight * 3 / 4 + 5),
				new StrData("", xOff + scale * 7/2, realHeight * 2 / 4 + 5),
				new StrData("", xOff + scale * 7/2, realHeight * 3 / 4 + 5),};
	}
	

	public void mainMotion() {
		if(map.current == null) // 1ループ目回避用
			return;
		
		Action act = map.current.getCurrentAction();
		if(act == null || map.current.getType() != UnitType.FRIEND) {
			sp.setView(plnNo, false);
			return;
		}
		
		sp.setView(plnNo, true);
		sds = initSds();
		
		setColorData(sds[4], act.cost.hp);
		setColorData(sds[5], act.cost.sp);
		
		Unit currentUnit = map.unitMap[current.y][current.x];
		if(currentUnit != null && map.current.getType().isAttackable(currentUnit.getType(), act.actType)) {
			Damage d = null;
			switch(act.actType) {
			case ATTACK:
				d = currentUnit.calcDamage(act.damage);
				break;
			case SUPPORT:
				d = act.damage;
				break;
			case ALL:
				d = currentUnit.calcDamage(act.damage);
				break;
			}
			//System.out.println(d.hp);
			sds[3].str += (int)(d.hit < 0 ? 0 : d.hit < 1.0 ? d.hit * 100 : 100) + " %";
			setColorData(sds[8], d.hp);
			setColorData(sds[9], d.sp);
		}
		
		current = new Coord<Integer>(act.hashCode(), 0); // スキルが変更されたときに再描画したい
	}
	
	private void setColorData(StrData sd, int data) {
		if(data > 0) {
			sd.c = new Color(0xcc3322);
			sd.str += "-";
		} else if(data < 0) {
			sd.c = new Color(0x00aa33);
			sd.str += "+";
		} else {
			sd.c = Color.BLACK;
			sd.str += "±";
		}
		
		sd.str += Math.abs(data);
	}
	
}
