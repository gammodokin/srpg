package srpg.screen;

import game.GameObjectManager;
import game.Key;
import game.Sprite;
import game.UserIF;

public abstract class Cursor extends UserIF {
	
	protected boolean select, cancel, failure, moveX, moveY, move;

	public Cursor(GameObjectManager gom, Sprite sp, int plnNo, int x, int y) {
		super(gom, sp, plnNo, x, y);
	}
	
	protected boolean selected, canceled;
	public void action() {
		Key key;
		selected = canceled = select = cancel = moveX = moveY = move = false;
		keyInput : 
		while ((key = keyQ.dequeue()) != null) {
			switch (key) {
			case UP:
				y--;
				moveY = true;
				break;
			case DOWN:
				y++;
				moveY = true;
				break;
			case LEFT:
				x--;
				moveX = true;
				break;
			case RIGHT:
				x++;
				moveX = true;
				break;
			case SELECT:
				selected = true;
				break keyInput;
			case CANCEL:
				canceled = true;
				break keyInput;
			}
		}
		
		failure = selected;
		cancel = canceled;
		
		mainAction();
		
		if(cancel)
			sp.playSE(Sprite.Preset.CANCEL);
		else if(select)
			sp.playSE(Sprite.Preset.SELECT);
		else if(failure)
			sp.playSE(Sprite.Preset.FAILURE);
		else if(move)
			sp.playSE(Sprite.Preset.MOVE);
	}
	
	abstract protected void mainAction();
}
