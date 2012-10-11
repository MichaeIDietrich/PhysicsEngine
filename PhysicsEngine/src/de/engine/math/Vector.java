package de.engine.math;


public class Vector 
{
	private Point p;
	private double length;
	
	public Vector() 
	{
	    p = new Point();
		p.x = 0.0;
		p.y = 0.0;
		length = 0.0;
	}
	
	public Vector(Point p) 
	{
		this.p = p;
		length = Util.distanceToOrigin(p);
	}

	public Vector(double x, double y) 
	{
		p = new Point();
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
	
    public void setPoint( double x, double y) {
        this.p.x = x;
        this.p.y = y;
        length = Util.distanceToOrigin(p);
    }
	
	public double getLength() {
		return length;
	}
}
