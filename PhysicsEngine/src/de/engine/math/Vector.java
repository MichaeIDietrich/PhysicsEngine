package de.engine.math;


public class Vector extends Point
{
	private double length;
	
	public Vector() 
	{
		x = 0.0;
		y = 0.0;
		length = 0.0;
	}
	
	public Vector(Point p) 
	{
		x = p.x;
		y = p.y;
		length = Util.distanceToOrigin(this);
	}

	public Vector(double x, double y) 
	{
		this.x = x;
		this.y = y;
		length = Util.distanceToOrigin(this);
	}
	
	public void setPoint(Point p) {
		x = p.x;
		y = p.y;
		length = Util.distanceToOrigin(p);
	}
	
    public void setPoint( double x, double y) {
        this.x = x;
        this.y = y;
        length = Util.distanceToOrigin(this);
    }
	
	public double getLength() {
		return length;
	}
	
	public Vector scale(double s) {
		return new Vector(x * s, y * s);
	}
	
	public Vector getNormalVector() {
		return new Vector(-1 * y, x);
	}
	
	public Vector getUnitVector() {
		double scale = 1 / length;
		return this.scale(scale);
	}
}
