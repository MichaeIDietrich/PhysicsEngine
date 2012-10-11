package de.engine.colldetect;

import java.util.HashMap;
import java.util.Set;

import de.engine.math.Vector;
import de.engine.objects.ObjectProperties;

public class Grid {
	private double cellSize;
	private HashMap<Integer, HashMap<Integer, java.util.Vector<ObjectProperties>>> objectFields;

	public Grid(double cellSize) {
		this.cellSize = cellSize;
		objectFields = new HashMap<>();
	}
	
	public void reset() {
		objectFields = new HashMap<>();
	}

	public void scanFieldsForObject(ObjectProperties op) {
		Vector[] aabb = op.getAABB();
		int minx = (int) (aabb[0].getPoint().x / cellSize) + 1;
		int miny = (int) (aabb[0].getPoint().y / cellSize) + 1;
		int maxx = (int) (aabb[1].getPoint().x / cellSize) + 1;
		int maxy = (int) (aabb[1].getPoint().y / cellSize) + 1;

		for (int i = minx; i <= maxx; i++) {
			for (int j = miny; j <= maxy; j++) {
				if (objectFields.containsKey(i)) {
					if (objectFields.get(i).containsKey(j)) {
						objectFields.get(i).get(j).add(op);
					} else {
						java.util.Vector<ObjectProperties> ops = new java.util.Vector<>();
						ops.add(op);
						objectFields.get(i).put(j, ops);
					}
				} else {
					HashMap<Integer, java.util.Vector<ObjectProperties>> row = new HashMap<Integer, java.util.Vector<ObjectProperties>>();
					java.util.Vector<ObjectProperties> ops = new java.util.Vector<>();
					ops.add(op);
					row.put(j, ops);
					objectFields.put(i, row);
				}
			}

		}
	}

	public java.util.Vector<ObjectProperties[]> getCollisionPairs() {
		java.util.Vector<ObjectProperties[]> collisionPairs = new java.util.Vector<ObjectProperties[]>();
		Set<Integer> xs = objectFields.keySet();
		for (Integer x : xs) {
			Set<Integer> ys = objectFields.get(x).keySet();
			for (Integer y : ys) {
				if(1 < objectFields.get(x).get(y).size()) {
					java.util.Vector<ObjectProperties> ops = objectFields.get(x).get(y);
					for (int i = 0; i < ops.size() - 1; i++) {
						for (int j = i + 1; j < ops.size(); j++) {
							ObjectProperties[] opPair = new ObjectProperties[2];
							opPair[0] = ops.get(i);
							opPair[0] = ops.get(j);
							collisionPairs.add(opPair);
						}
					}
				}
			}
		}
		return collisionPairs;
	}
}
