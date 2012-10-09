package de.engine.math;

public class Util {
	
	public static double distance(Vector p1, Vector p2) {
		double x = p2.getPoint().x - p1.getPoint().x;
		double y = p2.getPoint().y - p1.getPoint().y;
		return Math.hypot(x, y);
	}

	public static double distanceToOrigin(Point p) {
		return Math.hypot(p.x, p.y);
	}
	
	public static Vector add(Vector vec1, Vector vec2) {
		return new Vector(vec1.getX() + vec2.getX(), vec1.getY() + vec2.getY());
	}

	public static Vector minus(Vector vec1, Vector vec2) {
		return new Vector(vec1.getX() - vec2.getX(), vec1.getY() - vec2.getY());
	}
}
