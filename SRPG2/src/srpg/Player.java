package srpg;

import java.util.List;

public class Player {
	
	public List<Capability> party;
	public int capital;
	
	public Player(List<Capability> party, int capital) {
		super();
		this.party = party;
		this.capital = capital;
	}
}
