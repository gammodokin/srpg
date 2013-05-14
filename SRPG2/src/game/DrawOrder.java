package game;

public enum DrawOrder implements ZOrder {
	SCREEN_MENU, CURSOR, INFO, SCREEN, TALK;

	public double z() {
		return 0xff + ordinal();
	}

}