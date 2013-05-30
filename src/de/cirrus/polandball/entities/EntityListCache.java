package de.cirrus.polandball.entities;

import java.util.ArrayList;
import java.util.List;

public class EntityListCache {
	private static List<ArrayList<Entity>> cache = new ArrayList<>();

	private static int cc = 0;

	public static List<Entity> get() {
		if (cc == cache.size()) {
			cache.add(new ArrayList<Entity>());
		}

		List<Entity> el = cache.get(cc++);
		el.clear();
		return el;
	}

	public static void reset() {
		cc = 0;
	}
}
