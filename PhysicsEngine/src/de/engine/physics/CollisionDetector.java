package de.engine.physics;

import de.engine.math.*;
import de.engine.objects.*;

public class CollisionDetector {

	/**
	 * Einfache Überprüfung ob Kollision zwischen zwei Objekten stattfinden
	 * könnte.
	 * 
	 * @param pp1
	 * @param pp2
	 * @return
	 */
	public static boolean needCheck(double time, ObjectProperties pp1,
			ObjectProperties pp2) {
		double distance = Util.distance(
				Util.add(pp1.getWorldTranslation(), pp1.velocity.scale(time)),
				Util.add(pp2.getWorldTranslation(), pp1.velocity.scale(time)));
		double min_distance = pp1.getRadius() + pp2.getRadius();
		return (distance <= min_distance) ? true : false;
	}

	public CollisionProperties calcCollision(double time, ObjectProperties pp1,
			ObjectProperties pp2) {
		// if(!needCheck(time, pp1, pp2)) return null;
		if (pp1 instanceof Circle && pp2 instanceof Circle)
			return calcCirclesCollision((Circle) pp1, (Circle) pp2);
		else if (pp1 instanceof Polygon && pp2 instanceof Polygon)
			return calcPolygonsCollision((Polygon) pp1, (Polygon) pp2);
		else
			return null;
	}

	private CollisionProperties calcCirclesCollision(Circle c1, Circle c2) {
		CollisionProperties cp = new CollisionProperties();
		double distance = c1.getRadius() + c2.getRadius();
		cp.time = Math
				.sqrt(Math.pow(distance, 2)
						- Math.pow(
								(c2.getWorldTranslation().getX() + c2.velocity
										.getX())
										- (c1.getWorldTranslation().getY() + c1.velocity
												.getY()), 2)
						+ Math.pow(
								(c2.getWorldTranslation().getY() + c2.velocity
										.getY())
										- (c1.getWorldTranslation().getX() + c1.velocity
												.getX()), 2));
		cp.collPoints = new Point[1];
		Vector c1Pos = Util.add(c1.getWorldTranslation(),
				c1.velocity.scale(cp.time));
		Vector c2Pos = Util.add(c2.getWorldTranslation(),
				c1.velocity.scale(cp.time));
		cp.collPoints[0] = Util.add(c1Pos,
				Util.minus(c2Pos, c1Pos).scale(distance / c1.getRadius()))
				.getPoint();
		cp.newObject1Velocity = c2.velocity;
		cp.newObject2Velocity = c1.velocity;
		return cp;
	}

	private CollisionProperties calcPolygonsCollision(Polygon p1, Polygon p2) {
		//separating Axis Algorithmus
		CollisionProperties cp = new CollisionProperties();
		Polygon[] polys = new Polygon[2];
		polys[0] = p1;
		polys[1] = p2;
		for (Polygon polygon : polys) {
			for (int i = 0; i < polygon.points.length; i++) {
				int i2 = ((i + 1) < polygon.points.length) ? i + 1 : 0;
				Vector normale = Util
						.minus(polygon.getWorldPointPos(i2),
								polygon.getWorldPointPos(i)).getNormalVector()
						.getUnitVector();
				double min[] = new double[2];
				double max[] = new double[2];
				for (int j = 0; j < 2; j++) {
					for (int k = 0; k < polys[j].points.length; k++) {
						double scale = Util.scalarProduct(
								polygon.getWorldPointPos(k), normale);
						if (k == 0) {
							min[j] = scale;
							max[j] = scale;
						} else {
							if (scale < min[j])
								min[j] = scale;
							if (scale > max[j])
								max[j] = scale;
						}
					}
				}
				// wenn keine Überschneidung
				if (min[0] > max[1] || min[1] > max[0])
					return null;
			}
		}
		// wenn kein vorheriger Abbruch dann Überschneidung
		return cp;
	}
}
