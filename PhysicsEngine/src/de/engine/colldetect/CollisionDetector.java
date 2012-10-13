package de.engine.colldetect;

import de.engine.environment.Scene;
import de.engine.math.Util;
import de.engine.math.Vector;
import de.engine.objects.Circle;
import de.engine.objects.ObjectProperties;
import de.engine.physics.PhysicsCalcer;

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
			if (needCheck(grid.scene.getObject(ops[0]),
					grid.scene.getObject(ops[1]))) {
				if (grid.scene.getObject(ops[0]) instanceof Circle
						&& grid.scene.getObject(ops[1]) instanceof Circle) {
					PhysicsCalcer.calcCicles(
							(Circle) grid.scene.getObject(ops[0]),
							(Circle) grid.scene.getObject(ops[1]),
							CollisionTimer.getCirclesCollTime(
									(Circle) grid.scene.getObject(ops[0]),
									(Circle) grid.scene.getObject(ops[1])));
					System.out.println("collision");
				}
			}
		}
	}

	private CollisionData collCircles(Circle c1, Circle c2) {
		Vector pos1 = c1.getNextPosition();
		Vector pos2 = c2.getNextPosition();
		Vector distance = Util.minus(pos1, pos2);
		Vector normal = distance.scale(1.0d / distance.getLength());
		CollisionData cd = new CollisionData();
		cd.contacts = new CollisionData.Contact[1];
		cd.contacts[0].normal = normal;
		cd.contacts[0].point = Util.add(
				pos1,
				distance.scale((distance.getLength() - c2.getRadius())
						/ distance.getLength()));
		cd.contacts[0].penetration = (c1.getRadius() + c2.getRadius() - distance
				.getLength()) / 2.0;
		return cd;
	}
}
