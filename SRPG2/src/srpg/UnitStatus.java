package srpg;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import srpg.map.obj.Enemy;
import srpg.map.ui.ai.EAIActive;
import srpg.map.ui.ai.EAIPassive;

public class UnitStatus {
	protected static Random rn;
	
	public Capability capa;
	
	public int hp;
	public final int maxHP;
	public int sp;
	public final int maxSP;
	final int basicRes = 1000;
	public int resCharge;
	// •S•ª—¦
	public double tension = 0.5;
	
	public int flee;
	public int hit;
	
	public UnitStatus(Capability capa) {
		this.capa = capa;
		maxHP = hp = capa.vitality * 8;
		maxSP = sp = capa.intellect * 5;
		flee = capa.agility * 2 + capa.luck/2;
		hit = capa.dexterity * 2 + capa.luck/2;
		
		if(rn == null)
			rn = new Random();
		
		int param = 1000 / 2;
		resCharge = (int)(rn.nextGaussian() * param / 2.5 + param);
		resCharge = Math.abs(resCharge) % 1000;
	}
	
	public List<Action> usableActionList() {
		List<Action> acts = capa.actionList();
		
		for(Iterator<Action> i = acts.iterator();i.hasNext();)
			if(!i.next().usable(this))
				i.remove();
		
		return acts;
	}
	
	private void initRes() {
		if(resCharge < 0)
			resCharge += basicRes;
	}
	
	private void cutoff() {
		if(hp > maxHP)
			hp = maxHP;
		if(sp > maxSP)
			sp = maxSP;
		if(tension > 1)
			tension = 1;
		else if(tension < 0)
			tension = 0;
	}
	
	public boolean isAlive() {
		return hp > 0;
	}
	
	public void timeCourse() {
		timeCourse(false);
	}
	
	int virtualRC = 0;
	public void initVirtual() {
		virtualRC = resCharge;
	}
	
	public boolean timeCourse(boolean virtual) {
		if(virtual) {
			if(virtualRC < 0)
				virtualRC += basicRes;
				
			virtualRC -= capa.agility; 
				return virtualRC < 0;
		}
		
		initRes();
		
		resCharge -= capa.agility;
		
		tension += capa.uplift / 6000f;
		cutoff();
		
		return false;
	}
	
	public boolean isActionable() {
		return resCharge < 0;
	}
	
	public boolean damaging(Damage d) {
		if(rn.nextDouble() < d.hit) {
			hp -= d.hp;
			sp -= d.sp;
			tensionDamage((double)d.hp / maxHP * 2 + (double)d.sp / maxSP * 4);
			cutoff();
			
			initRes();
			resCharge += d.res;
			
			return true;
		} else {
			tensionDamage(-((double)d.hp / maxHP + (double)d.sp / maxSP) * 2);
			
			return false;
		}
	}
	
	public void endamage(Damage d) {
		tensionDamage(-((double)d.hp / maxHP + (double)d.sp / maxSP) * 1.5);
	}
	
	public void tensionDamage(double d) {
		if(d > 0)
			tension -= d * (100 - capa.toughness) / 100f;
		else
			tension -= d * (100 + capa.uplift) / 100f;
		
		cutoff();
	}
	
	public static List<Capability> initParty(List<Capability> party) {
		party.add(Capability.newReimu());
		party.add(Capability.newMarisa());
		party.add(Capability.newFFairy());
		party.add(Capability.newFFairy());
		party.add(Capability.newFFairy());
		
		return party;
	}
	
	public static void initEnemy(int id, Enemy e) {
		switch(id) {
		case 50:
			e.initStatus(Capability.newEFairy());
			e.setStrategy(EAIPassive.class);
			break;
		case 51:
			e.initStatus(Capability.newEFairy());
			e.setStrategy(EAIActive.class);
			break;
		case 52:
			e.initStatus(Capability.newEFairySniper());
			e.setStrategy(EAIPassive.class);
			break;
		case 53:
			e.initStatus(Capability.newEFairySniper());
			e.setStrategy(EAIActive.class);
			break;
		}
	}
}