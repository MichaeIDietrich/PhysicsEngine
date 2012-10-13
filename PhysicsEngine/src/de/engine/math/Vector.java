package de.engine.math;

public class Vector
{
    private double length;
    
    private double x;
    private double y;
    
    
    public Vector()
    {
        x = 0.0;
        y = 0.0;
        length = 0.0;
    }
    
    public Vector(double x, double y)
    {
        this.x = x;
        this.y = y;
        length = Util.distanceToOrigin(this);
    }
    
    public double getX()
    {
        return x;
    }
    
    public double getY()
    {
        return y;
    }
    
    public void setX(double x)
    {
        this.x = x;
        
    }
    
    public void setY(double y)
    {
        this.y = y;
    }
    
    public void setPoint(double x, double y)
    {
        this.x = x;
        this.y = y;
        length = Util.distanceToOrigin(this);
    }
    
    public double getLength()
    {
        return length;
    }
    
    public Vector scale(double s)
    {
        return new Vector(x * s, y * s);
    }
    
    public Vector getNormalVector()
    {
        return new Vector(-1 * y, x);
    }
    
    public Vector getUnitVector()
    {
        double scale = 1 / length;
        return this.scale(scale);
    }
}