package srpg.screen.talk;

import game.GameObjectManager;
import game.Key;
import game.Sprite;

import java.awt.Graphics;

import srpg.screen.ScreenMenu;

public class Question extends ScreenMenu {
	
	private int result;

	public Question(GameObjectManager gom, Sprite sp, int plnNo, int x, int y) {
		super(gom, sp, plnNo, x, y);
		
		sp.setPos(plnNo, TalkDrawOrder.QUESTION.z());
		
		menuCursor = QCursor.class;
		
		width = 170;
		height = 150;
		
		sp.setPos(plnNo, (gom.width - width)/2, (gom.height - height)/2);
		
		offset.x -= 40;
		
	}
	
	void init(String[] strs, boolean[] bs) {
		menuStr = strs;
		activeMenu = bs;
		
		super.init();
	}
	
	int getResult() {
		return result;
	}
	
	@Override
	protected void drawBGRect(int x, int y, int width, int height, Graphics g) {
		Sprite.drawDesignedRect(x, y, width, height, g);
	}


	protected class QCursor extends SMCursor {

		public QCursor(GameObjectManager gom, Sprite sp, int plnNo, int x,
				int y) {
			super(gom, sp, plnNo, x, y);
			
			sp.setPos(plnNo, TalkDrawOrder.Q_CURSOR.z());
		}
		
		@Override
		public void destructor() {
			// 選択肢決定後のセリフ送り。力技
			keyQ.enqueue(Key.SELECT);
			
			super.destructor();
		}

		@Override
		protected void MCAction() {
			/*if(canceled) {
				destructor();
				return;
			}*/

			if(select) {
				destructor();
				result = y;
			}


		}
	}

}
