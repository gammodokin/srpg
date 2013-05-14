package myutil;
import java.util.LinkedList;


public class Stack<E> extends LinkedList<E> {

	public void push(E e) {
		addFirst(e);
	}

	public E pop() {
		return poll();
	}

}
