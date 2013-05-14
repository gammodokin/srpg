package srpg;

public class Damage {
	public static final Damage ZERO = new Damage(0, 0, 0, 0);
	// ê≥Ç»ÇÁå∏ÇÈ
	public final int hp;
	// ê≥Ç»ÇÁå∏ÇÈ
	public final int sp;
	final int res;
	public final double hit;
	
	Damage(int hp, int sp, int res, double hit) {
		this.hp = hp;
		this.sp = sp;
		this.res = res;
		this.hit = hit;
	}
	
	public Damage effect(double e) {
		return new Damage((int)(hp * e), (int)(sp * e), (int)(res * e), hit);
	}
	
	public Damage hitEffect(double e) {
		return new Damage(hp, sp, res, hit * e);
	}
}
