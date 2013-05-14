package myutil;


public class Coord<E extends Number> implements Cloneable {

	public E x, y;
	
	public Coord(E x ,E y) {
		this.x = x;
		this.y = y;
	}
	
	public void assign(Coord<E> co) {
		x = co.x;
		y = co.y;
	}
	
	@Override
	public Coord<E> clone() {
		return new Coord<E>(x, y);
	}
	
	@Override
	public boolean equals(Object o) {
		Coord<E> co = (Coord<E>)o;
		return co.x == x && co.y == y;
	}
	
	@Override
	public int hashCode() {
		return (int)(x.doubleValue() * 0xffff + y.doubleValue());
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
}