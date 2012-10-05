package de.engine.math;

public class Util {
	
	public static double distance(Vector p1, Vector p2) {
		double x = p2.getPoint().x - p1.getPoint().x;
		double y = p2.getPoint().y - p1.getPoint().y;
		return Math.sqrt((x * x) + (y * y));
	}

	public static double distanceToOrigin(Point p) {
		return Math.sqrt((p.x * p.x) + (p.y * p.y));
	}
}
