public class VehicleHiringManager {
	
	LocatorMap<String> loc_map;

	public VehicleHiringManager() {
		loc_map = new TreeLocatorMap<String>();
	}

	// Returns the locator map.
	public LocatorMap<String> getLocatorMap() {
		
		return loc_map;
	}

	// Sets the locator map.
	public void setLocatorMap(LocatorMap<String> locatorMap) {
		
		List<String> list = locatorMap.getMap().getAll();
		
		if (!list.empty()) {
			list.findFirst();
			while (!list.last()) {
				loc_map.add(list.retrieve(), locatorMap.getLoc(list.retrieve()).first);
				list.findNext();
			}
			loc_map.add(list.retrieve(), locatorMap.getLoc(list.retrieve()).first);
		}
	}

	// Inserts the vehicle id at location loc if it does not exist and returns true.
	// If id already exists, the method returns false.
	public boolean addVehicle(String id, Location loc) {
		Pair<Location, Integer> findLoc = loc_map.getLoc(id);
		Pair<Boolean, Integer> inserted;
		
		if (findLoc.first == null) {
			inserted = loc_map.add(id, loc);
			return inserted.first;
		}
		return false;
	}

	// Moves the vehicle id to location loc if id exists and returns true. If id not
	// exist, the method returns false.
	public boolean moveVehicle(String id, Location loc) {
		Pair<Location, Integer> findLoc = loc_map.getLoc(id); // returns the location if id exists or null otherwise
		
		if (findLoc.first != null) { // id exists
			Pair<Boolean, Integer> move = loc_map.move(id, loc);
			if (move.first == true)
				return true;
		}
		return false;
	}

	// Removes the vehicle id if it exists and returns true. If id does not exist,
	// the method returns false.
	public boolean removeVehicle(String id) {
		Pair<Location, Integer> findLoc = loc_map.getLoc(id);
		
		if (findLoc.first != null) {
			Pair<Boolean, Integer> remove = loc_map.remove(id);
			if (remove.first == true)
				return true;
		}
		return false;
	}

	// Returns the location of vehicle id if it exists, null otherwise.
	public Location getVehicleLoc(String id) {
		Pair<Location, Integer> findLoc = loc_map.getLoc(id);
		return findLoc.first;
	}

	// Returns all vehicles located within a square of side 2*r centered at loc
	// (inclusive of the boundaries).
	public List<String> getVehiclesInRange(Location loc, int r) {
		Pair<List<String>, Integer> list = loc_map.getInRange(new Location(loc.x - r, loc.y - r), new Location(loc.x + r, loc.y + r));
		
		return list.first;
	}
}
