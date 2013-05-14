package srpg;

public class Range {
	static final Range SIMPLE = new Range(RangeType.ARCH, 0, 0, 0, 0);
	
	final RangeType at;
	
	final int min;
	public final int max;
	final int minH;
	final int maxH;
	
	Range(RangeType at, int min, int max, int minH, int maxH) {
		this.at = at;
		this.min = min;
		this.max = max;
		this.minH = minH;
		this.maxH = maxH;
	}

}
