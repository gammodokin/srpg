package game;

public class ChangeTracker<E> {
	
	private E previous = null;
	
	public ChangeTracker() {}
	
	public ChangeTracker(E e) {
		previous = e;
	}
	
	/*public void update(E current) {
		previous = this.current;
		this.current = current;
	}*/
	
	public boolean isChanged(E e) {
		boolean b;
		if(previous != null && e != null) {
			b = !previous.equals(e);
		} else {
			b = previous != e;
		}
		
		//System.out.println(previous + "\t-> " + e + "\t: " + b);
		
		previous = e;
		return b;
	}
}
