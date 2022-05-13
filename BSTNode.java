
public class BSTNode<K extends Comparable<K>, T> {
	public K key;
	public T data;
	public BSTNode<K, T> left, right;

	public BSTNode() {
		left = right = null;
	}
	public BSTNode(K k, T e) {
		key = k;
		data = e;
		left = null;
		right = null;
	}

}
