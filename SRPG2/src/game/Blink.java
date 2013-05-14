package game;

public class Blink {
	private int count = 0;
	private int span;
	private int time;
	
	// 最大値max、time回で一巡する
	public Blink(int max, int time) {
		span = max / time;
		this.time = time;
	}
	
	public int blinking() {
		count %= time;
		count++;
		return -count * span;
	}
}

