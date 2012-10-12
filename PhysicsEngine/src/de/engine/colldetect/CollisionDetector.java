package de.engine.colldetect;

import de.engine.environment.Scene;
import de.engine.math.Util;
import de.engine.objects.ObjectProperties;

public class CollisionDetector {

	private Grid grid;

	public CollisionDetector(Scene scene) {
		grid = new Grid(scene);
	}
	
	/**
	 * Einfache Überprüfung ob Kollision zwischen zwei Objekten stattfinden
	 * könnte.
	 * 
	 * @param pp1
	 * @param pp2
	 * @return
	 */
	public static boolean needCheck(ObjectProperties pp1, ObjectProperties pp2) {
		double distance = Util.distance(pp1.getPosition(), pp2.getPosition());
		double min_distance = pp1.getRadius() + pp2.getRadius();
		return (distance <= min_distance) ? true : false;
	}

	public void checkScene() {
		grid.scanScene();
		for (Integer[] ops : grid.getCollisionPairs()) {
			if (needCheck(grid.scene.getObject(ops[0]), grid.scene.getObject(ops[1])))
				System.out.println("collision");
		}
	}
}
