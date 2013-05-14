package srpg.screen;

import game.GameObjectManager;
import game.Sprite;

import java.util.Arrays;
import java.util.List;

import srpg.Capability;
import srpg.UnitStatus;

public class Status extends ScreenMenu {

	private List<Capability> party;
	private int cursored;

	private final Information info;

	public Status(GameObjectManager gom, Sprite sp, int plnNo, int x, int y) {
		super(gom, sp, plnNo, x, y);

		info = gom.addGO(SInfo.class, 0, 0, this);
	}

	void initParty(List<Capability> party) {
		this.party = party;

		menuStr = arr2String(party.toArray());
		activeMenu = new boolean[party.size()];
		Arrays.fill(activeMenu, true);
		menuCursor = SCursor.class;

		init();
	}

	@Override
	public void destructor() {
		info.destructor();
		super.destructor();
	}

	protected class SCursor extends SMCursor {

		public SCursor(GameObjectManager gom, Sprite sp, int plnNo, int x,
				int y) {
			super(gom, sp, plnNo, x, y);
		}

		@Override
		protected void MCAction() {
			setView(true);
			cursored = y;

			if(canceled) {
				destructor();
			}
			/*
			Item item = Item.values()[y];
			if(select) {
				switch(item) {
				case Stage_Select:
					setView(false);
					((StageSelect)gom.addGO(StageSelect.class.getCanonicalName(), 0, 0)).setParty(party);

					break;
				case Status:
					setView(false);
					//gom.addGO(Status.class.getCanonicalName(), 0, 0);
					break;
				}
			}*/
		}


	}

	protected class SInfo extends MenuInformation {

		private int realHeight;

		public SInfo(GameObjectManager gom, Sprite sp, int plnNo, int x, int y) {
			super(gom, sp, plnNo, x, y);
			init(gom.width * 2/5, height = 100);
			realHeight = 100;
			title = "";
			//sds = initSds();

			sp.setPos(plnNo, gom.width - width, 60);
		}

		protected StrData[] initSds() {
			return new StrData[]{
					new StrData("", width / 8, realHeight / 4 + 5),
					new StrData("HP : ", width / 8, realHeight * 2 / 4 + 5),
					new StrData("SP : ", width / 8, realHeight * 3 / 4 + 5),
					new StrData("Str : ", width * 8 / 16 - 6, realHeight * 2 / 4 + 5), 
					new StrData("Agi : ", width * 8 / 16 - 6, realHeight * 3 / 4 + 5),
					new StrData("Hit : ", width * 11 / 16, realHeight * 2 / 4 + 5), 
					new StrData("Flee : ", width * 11 / 16, realHeight * 3 / 4 + 5),};
		}

		protected void setData(Capability cap) {
			UnitStatus st = new UnitStatus(cap);
			sds[0].str += cap.name;
			sds[1].str += st.hp + " / " + st.maxHP;
			sds[2].str += st.sp + " / " + st.maxSP;
			sds[3].str += st.capa.strength;
			sds[4].str += st.capa.agility;
			sds[5].str += st.hit;
			sds[6].str += st.flee;
		}

		@Override
		public void mainMotion() {
			sp.setView(plnNo, true);
			sds = initSds();

			setData(party.get(cursored));

		}

		@Override
		protected void update() {
			current.y = cursored;
		}
	}

}
