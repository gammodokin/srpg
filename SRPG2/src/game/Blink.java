package game;

public class Blink {
	private int count = 0;
	private int span;
	private int time;
	
	// Å‘å’lmaxAtime‰ñ‚Åˆê„‚·‚é
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

