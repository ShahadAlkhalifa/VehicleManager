public class BST<K extends Comparable<K>, T> implements Map<K, T> {
	BSTNode<K, T> root, current, prevCurrent;
	
	public BST() {
		root = current = null;
	}
	
	@Override
	public boolean empty() {
		return root == null;
	}

	@Override
	public boolean full() {
		return false;
	}

	@Override
	public T retrieve() {
		return current.data;
	}

	@Override
	public void update(T e) {
		current.data = e;
	}

	@Override
	public Pair<Boolean, Integer> find(K key) {
		Integer counter = 0;
		Boolean check = false;
		BSTNode<K, T> p = root;
		Pair<Boolean, Integer> pair;
		if (root == null) {
			pair = new Pair<Boolean, Integer>(check, counter);
			return pair;
		}
		while (p != null) {
			prevCurrent = p;
			counter++;
			if (key.compareTo(p.key) == 0) {
				current = p;
				check = true;
				pair = new Pair<Boolean, Integer>(check, counter);
				return pair;
			}
			else if (key.compareTo(p.key) < 0)
				p = p.left;
			else
				p = p.right;
		}
		pair = new Pair<Boolean, Integer>(check, counter); //If the key doesn't exist it will return <false, # of comparisons>
		return pair;
	}

	@Override
	public Pair<Boolean, Integer> insert(K key, T data) {
		Pair<Boolean, Integer> pair;
		Integer counter = 0;
		Boolean check = false;
		BSTNode<K, T> newNode, tmp = current;
		if (find(key).first == true) { //returns true if element exists and false otherwise
			current = tmp;
			counter = find(key).second;
			pair = new Pair<Boolean, Integer>(check, counter);
			return pair;
		}
		newNode = new BSTNode<K, T>(key, data);
		if (root == null) {
			root = current = prevCurrent = newNode;
			check = true;
			pair = new Pair<Boolean, Integer>(check, counter);
			return pair;
		}
		else {
			if (key.compareTo(prevCurrent.key) > 0) {
				prevCurrent.right = newNode;
				counter = find(key).second - 1;
			}
				
			else {
				prevCurrent.left = newNode;
				counter = find(key).second - 1;

			}
			current = prevCurrent = newNode;
			check = true;
			pair = new Pair<Boolean, Integer>(check, counter);
			return pair;
		}	
	}

	@Override
	public Pair<Boolean, Integer> remove(K key) {
		Pair<Boolean, Integer> pair, count = find(key);
		Boolean removed = false;
		Integer counter = count.second;
		BSTNode<K, T> p;
		if (count.first == true) {
			p = remove_rec(key, root);
			removed = true;
		}
		pair = new Pair<Boolean, Integer>(removed, counter);
		return pair;
	}
	
	private BSTNode<K, T> remove_rec(K key, BSTNode<K, T> p){
		BSTNode<K,T> q, child = null;
		if (p == null)
			return null;
		if (key.compareTo(p.key) > 0)
			p.right = remove_rec(key, p.right);
		else if (key.compareTo(p.key) < 0)
			p.left = remove_rec(key, p.left);
		else { //equal
			if (p.left != null && p.right != null) {
				q = find_min(p.right);
				p.key = q.key;
				p.data = q.data;
				p.right = remove_rec(q.key, p.right);
			}
			else {
				if (p.right == null)
					child = p.left;
				else if (p.left == null)
					child = p.right;
				return child;
			}
		}
		return p;
	}
	
	private BSTNode<K, T> find_min(BSTNode<K, T> node) {
		if (node == null)
			return null;
		while(node.left != null) {
			node = node.left;
		}
		return node;
	}


	@Override
	public List<K> getAll() {
		List<K> list = new LinkedList<K>();
		inOrderTraverse(root, list);
		return list;
	}
	
	private void inOrderTraverse(BSTNode<K, T> node, List<K> l) {
		if (node == null)
			return;
		inOrderTraverse(node.left, l);
		l.insert(node.key);
		inOrderTraverse(node.right, l);
	}
	

}
