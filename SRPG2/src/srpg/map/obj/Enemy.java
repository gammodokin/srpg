package srpg.map.obj;

import game.Sprite;
import srpg.SRPGGameObjectManager;
import srpg.map.StageMap;
import srpg.map.UnitType;
import srpg.map.ui.ai.EnemyAI;

public class Enemy extends Unit {

	//Color color;

	public Enemy(SRPGGameObjectManager gom, Sprite sp, int plnNo, int x, int y, StageMap m) {
		super(gom, sp, plnNo, x, y, m, UnitType.ENEMY);
	}

	@Override
	public void destructor() {
		if(status.hp <= 0)
			map.setMoney(status.capa.level * 200);
		super.destructor();
	}
	
	public void setStrategy(Class<? extends EnemyAI> eai) {
		operator = eai;
	}
	
	

}
