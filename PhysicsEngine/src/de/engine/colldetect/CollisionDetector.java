package de.engine.colldetect;

import de.engine.math.Util;
import de.engine.objects.ObjectProperties;


public class CollisionDetector {
	/**
	 * Einfache �berpr�fung ob Kollision zwischen zwei Objekten stattfinden k�nnte.
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
