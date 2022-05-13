
public class TreeLocatorMap<K extends Comparable<K>> implements LocatorMap<K> {
	Map<K, Location> map;
	Locator<K> locator;
	
	public TreeLocatorMap() {
		map = new BST<K, Location>();
		locator = new TreeLocator<K>();
	}

	@Override
	public Map<K, Location> getMap() {
		
		return map;
	}

	@Override
	public Locator<K> getLocator() {
		
		return locator;
	}

	@Override
	public Pair<Boolean, Integer> add(K k, Location loc) {
		Pair<Boolean, Integer> find = map.find(k); // <true if k exists otherwise false, # of comparisons>
		Boolean inserted = false;
		Integer counter = find.second;
		if (find.first == false) { // k doesn't exist therefore we insert it
			map.insert(k, loc);
			locator.add(k, loc);
			inserted = true;
		}
		return new Pair<Boolean, Integer>(inserted, counter);
	}

	@Override
	public Pair<Boolean, Integer> move(K k, Location loc) {
		Pair<Boolean, Integer> findK = map.find(k); // <true if k exists, # of comparisons>
		Integer counter = findK.second;
		
		if (findK.first == true) { // k exists
			Location oldLoc = map.retrieve();
			Pair<Boolean, Integer> remove = locator.remove(k, oldLoc);
			if (remove.first == false) { // loc doesn't exist or e is not in loc
				return new Pair<Boolean, Integer>(false, counter);
			}
			else {
				map.update(loc);
				counter = locator.add(k, loc);
			}
		}
		else {
			return new Pair<Boolean, Integer>(false, counter); // k doesn't exist
		}
		return new Pair<Boolean, Integer>(true, counter);
	}

	@Override
	public Pair<Location, Integer> getLoc(K k) {
		Pair<Boolean, Integer> findK = map.find(k); // current is k if found
		Integer counter = findK.second;
		
		if (findK.first == true) { // k exists
			Location loc = map.retrieve();
			return new Pair<Location, Integer>(loc, counter);
		}
		return new Pair<Location, Integer>(null, counter); // k doesn't exist
	}

	@Override
	public Pair<Boolean, Integer> remove(K k) {
		Pair<Boolean, Integer> findK = map.find(k);
		Boolean removed = false;
		Integer counter = findK.second;
		
		if (findK.first == true) { // found
			Pair<Boolean, Integer> check = locator.remove(k, map.retrieve());
			if (check.first == true) {
				removed = true;
				map.remove(k);
			}	
		}
		return new Pair<Boolean, Integer>(removed, counter);
	}

	@Override
	public List<K> getAll() {
		
		return map.getAll();
	}

	@Override
	public Pair<List<K>, Integer> getInRange(Location lowerLeft, Location upperRight) {
		List<K> newList = new LinkedList<K>();
		List<Pair<Location, List<K>>> list = locator.inRange(lowerLeft, upperRight).first;
		Integer counter = locator.inRange(lowerLeft, upperRight).second;
		if (!list.empty()) {
			list.findFirst();
			while(!list.last()) {
				if (!list.retrieve().second.empty()) {
					list.retrieve().second.findFirst();
					while (!list.retrieve().second.last()) {
						newList.insert(list.retrieve().second.retrieve());
						list.retrieve().second.findNext();
					}
					newList.insert(list.retrieve().second.retrieve());
				}
				list.findNext();
			}
			if (!list.retrieve().second.empty()) {
				list.retrieve().second.findFirst();
				while(!list.retrieve().second.last()) {
					newList.insert(list.retrieve().second.retrieve());
					list.retrieve().second.findNext();
				}
				newList.insert(list.retrieve().second.retrieve());
			}
		}
		Pair<List<K>, Integer> pair = new Pair<List<K>, Integer>(newList, counter);
		return pair;
	}

}
