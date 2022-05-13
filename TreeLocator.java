
public class TreeLocator<T> implements Locator<T> {
	TLNode<T> root, current;
	
	public TreeLocator() {
		root = current = null;
	}

	@Override
	public int add(T e, Location loc) {
		Pair<Boolean, Integer> pair = searchLoc(loc);
		TLNode<T> newNode = new TLNode<T>(loc, e), tmp = current;
		if (root == null) { // Tree locator is empty
			root = current = newNode;
			return pair.second;
		}
		if (pair.first == false) { // Location doesn't exist, so create it
			if (loc.x < tmp.location.x && loc.y <= tmp.location.y)
				tmp.c1 = newNode;
			else if (loc.x <= tmp.location.x && loc.y > tmp.location.y)
				tmp.c2 = newNode;
			else if (loc.x > tmp.location.x && loc.y >= tmp.location.y)
				tmp.c3 = newNode;
			else if (loc.x >= tmp.location.x && loc.y < tmp.location.y)
				tmp.c4 = newNode;
			tmp = newNode;
		}
		else { // Location exists, so add the data to the list
			tmp.data.insert(e);
		}
		current = tmp;
		return pair.second;
	}
	
	private Pair<Boolean, Integer> searchLoc(Location loc) { // The first pair returns true if loc doesn't exist and false otherwise
		Pair<Boolean, Integer> pair;
		Integer counter = 0;
		Boolean check = false;
		TLNode<T> p = root, q = root;
		if (root == null) {
			pair = new Pair<Boolean, Integer>(check, counter);
			return pair;
		}
		else {
			while (p != null) {
				q = p;
				counter++;
				if (loc.x == p.location.x && loc.y == p.location.y) {
					current = p;
					check = true;
					pair = new Pair<Boolean, Integer>(check, counter);
					return pair;
				}
				if (loc.x < p.location.x && loc.y <= p.location.y)
					p = p.c1;
				else if (loc.x <= p.location.x && loc.y > p.location.y)
					p = p.c2;
				else if (loc.x > p.location.x && loc.y >= p.location.y)
					p = p.c3;
				else if (loc.x >= p.location.x && loc.y < p.location.y)
					p = p.c4;
			}
			current = q;
		}
		pair = new Pair<Boolean, Integer>(check, counter);
		return pair;
	}

	@Override
	public Pair<List<T>, Integer> get(Location loc) {
		Pair<Boolean, Integer> pair = searchLoc(loc);
		Integer counter = pair.second;
		List<T> cars = new LinkedList<T>();
		Pair<List<T>, Integer> list;
		if (pair.first == false) { // no cars
			list = new Pair<List<T>, Integer>(cars, counter);
			return list;
		}
		else {
			if (!current.data.empty()) {
				current.data.findFirst();
				while (!current.data.last()) {
					cars.insert(current.data.retrieve());
					current.data.findNext();
				}
				cars.insert(current.data.retrieve());
			}
		}
		list = new Pair<List<T>, Integer>(cars, counter);
		return list;
	}

	@Override
	public Pair<Boolean, Integer> remove(T e, Location loc) { 
		TLNode<T> curr = current;
		Pair<Boolean, Integer> pair = searchLoc(loc);
		Boolean check = false;
		Integer counter = pair.second;
		TLNode<T> tmp = current;
		if (pair.first == true) { // Location exists
			if (!tmp.data.empty()) {
				tmp.data.findFirst();
				while (!tmp.data.last()) {
					if (e.equals(tmp.data.retrieve())) {
						tmp.data.remove();
						check = true;
						break;
					}
					tmp.data.findNext();
				}
				if (e.equals(tmp.data.retrieve())) {
					tmp.data.remove();
					check = true;
				}
			}
		}
		else current = curr;
		
		return new Pair<Boolean, Integer>(check, counter);
	}

	@Override
	public List<Pair<Location, List<T>>> getAll() {
		List<Pair<Location, List<T>>> list = new LinkedList<Pair<Location, List<T>>>();
		if (root != null) {
			traverse(root, list);
		}
		return list;
	}
	
	private void traverse(TLNode<T> node, List<Pair<Location, List<T>>> l){
		Pair<Location, List<T>> p = new Pair<Location, List<T>>(node.location, node.data);
		if (node == null)
			return;
		l.insert(p);
		if (node.c1 != null)
		    traverse(node.c1, l);
		if (node.c2 != null)
		    traverse(node.c2, l);
		if (node.c3 != null)
		    traverse(node.c3, l);
		if (node.c4 != null)
		    traverse(node.c4, l);	
	}

	@Override
	public Pair<List<Pair<Location, List<T>>>, Integer> inRange(Location lowerLeft, Location upperRight) {
		List<Pair<Location, List<T>>> list = new LinkedList<Pair<Location, List<T>>>();
		Location upperLeft = new Location(lowerLeft.x, upperRight.y);
		Location lowerRight = new Location(upperRight.x, lowerLeft.y);
		Integer comparisons = inRange_rec(root, list, lowerLeft, upperRight, upperLeft, lowerRight);
		Pair<List<Pair<Location, List<T>>>, Integer> pair = new Pair<List<Pair<Location, List<T>>>, Integer>(list, comparisons);
		return pair;
	}

	private Integer inRange_rec(TLNode<T> node, List<Pair<Location, List<T>>> l, Location ll, Location ur, Location ul, Location lr) {
		Pair<Location, List<T>> pair = new Pair<Location, List<T>>(node.location, node.data);
		Integer counter = 0;
		if (node == null)
			return 0;
		
		else { 
			counter++;
			
			if ((ll.x <= node.location.x  && node.location.x <= ur.x ) && ( ll.y <= node.location.y  && node.location.y <= ur.y))// Satisfy the condition
				l.insert(pair);
			
			if ((node.c1 != null) && (((ll.x < node.location.x) && (ll.y <= node.location.y)) || ((ur.x < node.location.x) && (ur.y <= node.location.y))
					|| ((ul.x < node.location.x) && (ul.y <= node.location.y)) || ((lr.x < node.location.x) && (lr.y <= node.location.y)))) // Check first child
				counter += inRange_rec(node.c1, l, ll, ur, ul, lr);
			
			if ((node.c2 != null) && (((ll.x <= node.location.x) && (ll.y > node.location.y)) || ((ur.x <= node.location.x) && (ur.y > node.location.y))
					|| ((ul.x <= node.location.x) && (ul.y > node.location.y)) || ((lr.x <= node.location.x) && (lr.y > node.location.y))))
				counter += inRange_rec(node.c2, l, ll, ur, ul, lr);
			
			if ((node.c3 != null) && (((ll.x > node.location.x) && (ll.y >= node.location.y)) || ((ur.x > node.location.x) && (ur.y >= node.location.y))
					|| ((ul.x > node.location.x) && (ul.y >= node.location.y)) || ((lr.x > node.location.x) && (lr.y >= node.location.y))))
				counter += inRange_rec(node.c3, l, ll, ur, ul, lr);
			
			if ((node.c4 != null) && (((ll.x >= node.location.x) && (ll.y < node.location.y)) || ((ur.x >= node.location.x) && (ur.y < node.location.y))
					|| ((ul.x >= node.location.x) && (ul.y < node.location.y)) || ((lr.x >= node.location.x) && (lr.y < node.location.y))))
				counter += inRange_rec(node.c4, l, ll, ur, ul, lr);
			
			return counter;	
		}
	}
	
}
