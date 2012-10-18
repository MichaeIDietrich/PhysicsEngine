package de.engine.colldetect;

import de.engine.math.Util;
import de.engine.objects.Circle;

public class CollisionTimer {
	
	public static double getCirclesCollTime(Circle c1, Circle c2) {
		double distance = Util.minus(c1.getPosition(), c2.getPosition()).getLength();
		double min_distance = c1.getRadius() + c2.getRadius();
		double velocity = Util.minus(c1.velocity, c2.velocity).getLength();
		return (distance - min_distance) / velocity;
	}
}
