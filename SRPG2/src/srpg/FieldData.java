package srpg;

public class FieldData {
	public static FieldData heichi = new FieldData(new String[]{"heichi", "heichi_kabe1", "sougen_kabe2"}, 1, 1),
					sougen = new FieldData(new String[]{"sougen", "sougen_kabe1", "sougen_kabe2"}, 1.2, 0.9),
					arechi = new FieldData(new String[]{"arechi", "arechi_kabe1", "sougen_kabe2"}, 2, 0.8);
	public static FieldData obstacle = new FieldData(new String[]{"sougen", "sougen_kabe1", "sougen_kabe2"}, -10, 1);
	// length3で0が地表、1が壁、2が繰り返し用壁
	final String[] graphicName;
	public final double cost;
	public final double damageEffect;
	
	FieldData(String[] graphicName, double cost, double damageEffect) {
		this.graphicName = graphicName;
		this.cost = cost;
		this.damageEffect = damageEffect;
	}
	
	
}
