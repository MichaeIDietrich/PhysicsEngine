package de.engine.physics;

import de.engine.math.Util;
import de.engine.objects.ObjectProperties;


public class CollisionDetector {
	/**
	 * Einfache Überprüfung ob Kollision zwischen zwei Objekten stattfinden könnte.
	 * 
	 * @param pp1
	 * @param pp2
	 * @return
	 */
	public static boolean needCheck(ObjectProperties pp1, ObjectProperties pp2) {
		double distance = Util.distance( pp1.position, pp2.position );
		double min_distance = pp1.getRadius() + pp2.getRadius();
		return (distance <= min_distance) ? true : false;
	}
}
