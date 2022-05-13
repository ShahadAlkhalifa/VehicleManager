
public class TLNode<T> {
	Location location;
	List<T> data = new LinkedList<T>();
	TLNode<T> c1, c2, c3, c4;

	public TLNode() {
		location = null;
		data = null;
		c1 = c2 = c3 = c4 = null;
	}
	
	public TLNode(Location l, T d, TLNode<T> ch1, TLNode<T> ch2, TLNode<T> ch3, TLNode<T> ch4) {
		location = l;
		data.insert(d);
		c1 = ch1;
		c2 = ch2;
		c3 = ch3;
		c4 = ch4;
	}
	
	public TLNode(Location l, T d) {
		location = l;
		data.insert(d);
		c1 = c2 = c3 = c4 = null;
	}

}
