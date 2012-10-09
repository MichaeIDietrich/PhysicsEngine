package de.engine.math;


public class Vector {
	private Point p;
	private double length;
	
	public Vector() {
		p.x = 0.0;
		p.y = 0.0;
		length = 0.0;
	}
	
	public Vector(Point p) {
		this.p = p;
		length = Util.distanceToOrigin(p);
	}

	public Vector(double x, double y) {
		p.x = x;
		p.y = y;
		length = Util.distanceToOrigin(p);
	}
	
	public Point getPoint() {
		return p;
	}
	
	public void setPoint(Point p) {
		this.p = p;
		length = Util.distanceToOrigin(p);
	}
	
	public double getX() {
		return p.x;
	}

	public double getY() {
		return p.y;
	}
	
	public double getLength() {
		return length;
	}
	
	public Vector scale(double s) {
		return new Vector(p.x * s, p.y * s);
	}
	
	public Vector getNormalVector() {
		return new Vector(-1 * p.y, p.x);
	}
	
	public Vector getUnitVector() {
		double scale = 1 / length;
		return this.scale(scale);
	}
}
