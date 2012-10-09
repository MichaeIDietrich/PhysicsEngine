package de.engine.physics;

import de.engine.math.Point;
import de.engine.math.Util;
import de.engine.math.Vector;
import de.engine.objects.Circle;
import de.engine.objects.ObjectProperties;

public class CollisionDetector {
	
	/**
	 * Einfache �berpr�fung ob Kollision zwischen zwei Objekten stattfinden k�nnte.
	 * 
	 * @param pp1
	 * @param pp2
	 * @return
	 */
	public static boolean needCheck(double time, ObjectProperties pp1, ObjectProperties pp2) {
		double distance = Util.distance( Util.add(pp1.getWorldTranslation(), pp1.velocity.scale(time)),
				Util.add(pp2.getWorldTranslation(), pp1.velocity.scale(time)) );
		double min_distance = pp1.getRadius() + pp2.getRadius();
		return (distance <= min_distance) ? true : false;
	}
	
	public CollisionProperties calcCollision(double time, ObjectProperties pp1, ObjectProperties pp2) {
		//if(!needCheck(time, pp1, pp2)) return null;
		if(!((pp1 instanceof Circle) && (pp2 instanceof Circle))) return null;
		return calcCirclesCollision((Circle) pp1, (Circle) pp2);
	}
	
	private CollisionProperties calcCirclesCollision(Circle c1, Circle c2) {
		CollisionProperties cp = new CollisionProperties();
		double distance = c1.getRadius() + c2.getRadius();
		cp.time = Math.sqrt(Math.pow(distance, 2) -
				Math.pow((c2.getWorldTranslation().getX() + c2.velocity.getX()) - (c1.getWorldTranslation().getY() + c1.velocity.getY()), 2) + 
				Math.pow((c2.getWorldTranslation().getY() + c2.velocity.getY()) - (c1.getWorldTranslation().getX() + c1.velocity.getX()), 2));
		cp.collPoints = new Point[1];
		Vector c1Pos = Util.add(c1.getWorldTranslation(), c1.velocity.scale(cp.time));
		Vector c2Pos = Util.add(c2.getWorldTranslation(), c1.velocity.scale(cp.time));
		cp.collPoints[0] = Util.add(c1Pos, Util.minus(c2Pos, c1Pos).scale(distance / c1.getRadius())).getPoint();
		cp.newObject1Velocity = c2.velocity;
		cp.newObject2Velocity = c1.velocity;
		return cp;
	}
}
