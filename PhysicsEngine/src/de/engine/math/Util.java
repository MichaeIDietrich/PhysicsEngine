package de.engine.math;

public class Util {
	
	public static double distance(Vector p1, Vector p2) {
		double x = p2.getX() - p1.getX();
		double y = p2.getY() - p1.getY();
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
	
	public static double scalarProduct(Vector vec1, Vector vec2) {
		return vec1.getX() * vec2.getX() + vec1.getY() * vec2.getY();
	}
}
