package de.engine.colldetect;

import java.util.HashMap;
import java.util.Set;

import de.engine.environment.Scene;
import de.engine.math.Vector;
import de.engine.objects.ObjectProperties;

public class Grid {
	private double cellSize = 8;
	public Scene scene;
	private HashMap<Integer, HashMap<Integer, java.util.Vector<Integer>>> objectFields;

	public Grid(Scene scene) {
		this.scene = scene;
		objectFields = new HashMap<>();
	}

	public void scanScene() {
		this.reset();
		for (int i = 0; i < scene.getCount(); i++) {
			this.scanFieldsForObject(scene.getObject(i), i);
		}
	}

	public void reset() {
		objectFields = new HashMap<>();
	}

	public void scanFieldsForObject(ObjectProperties op, Integer id) {
		Vector[] aabb = op.getAABB();
		int minx = (int) (aabb[0].getPoint().x / cellSize) + 1;
		int miny = (int) (aabb[0].getPoint().y / cellSize) + 1;
		int maxx = (int) (aabb[1].getPoint().x / cellSize) + 1;
		int maxy = (int) (aabb[1].getPoint().y / cellSize) + 1;

		for (int i = minx; i <= maxx; i++) {
			for (int j = miny; j <= maxy; j++) {
				if (objectFields.containsKey(i)) {
					if (objectFields.get(i).containsKey(j)) {
						objectFields.get(i).get(j).add(id);
					} else {
						java.util.Vector<Integer> ops = new java.util.Vector<Integer>();
						ops.add(id);
						objectFields.get(i).put(j, ops);
					}
				} else {
					HashMap<Integer, java.util.Vector<Integer>> row = new HashMap<Integer, java.util.Vector<Integer>>();
					java.util.Vector<Integer> ops = new java.util.Vector<Integer>();
					ops.add(id);
					row.put(j, ops);
					objectFields.put(i, row);
				}
			}

		}
	}

	public java.util.Vector<Integer[]> getCollisionPairs() {
		java.util.Vector<Integer[]> collisionPairs = new java.util.Vector<Integer[]>();
		Set<Integer> xs = objectFields.keySet();
		for (Integer x : xs) {
			Set<Integer> ys = objectFields.get(x).keySet();
			for (Integer y : ys) {
				if (1 < objectFields.get(x).get(y).size()) {
					java.util.Vector<Integer> ops = objectFields.get(x).get(y);
					for (int i = 0; i < ops.size() - 1; i++) {
						for (int j = i + 1; j < ops.size(); j++) {
							Integer[] opPair = new Integer[2];
							if (ops.get(i) < ops.get(j)) {
								opPair[0] = ops.get(i);
								opPair[1] = ops.get(j);
							} else {
								opPair[0] = ops.get(j);
								opPair[1] = ops.get(i);
							}
							if (!collisionPairs.contains(opPair))
								collisionPairs.add(opPair);
						}
					}
				}
			}
		}
		return collisionPairs;
	}
}
