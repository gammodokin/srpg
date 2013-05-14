package myutil;
import java.util.LinkedList;


public class Queue<E> extends LinkedList<E> {

	public E enqueue(E element) {
		add(element);
		return element;
	}
	
	public Queue<E> enqueue(Queue<E> q) {
		for(E e : q)
			add(e);
		return q;
	}

	public E dequeue() {
		return poll();
	}
}
