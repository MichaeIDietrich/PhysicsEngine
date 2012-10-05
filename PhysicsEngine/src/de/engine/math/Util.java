package de.engine.math;

public class Util {
	
	public static double distance(Point p1, Point p2) {
		double x = p2.x - p1.x;
		double y = p2.y - p1.y;
		return Math.sqrt((x * x) + (y * y));
	}

	public static double distanceToOrigin(Point p) {
		return Math.sqrt((p.x * p.x) + (p.y * p.y));
	}
}
