package de.engine.objects;

import de.engine.math.Rotation;
import de.engine.math.Transformation;
import de.engine.math.Util;
import de.engine.math.Vector;

public class Circle extends ObjectProperties
{
    public Circle(Vector position, double radius)
    {
        this.world_position = new Transformation(position, new Rotation(0));
        this.radius = radius;
        
        this.velocity = new Vector();
    }
    
    @Override
    public void translation()
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void rotation()
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void destroy()
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public Circle copy()
    {
        // TODO - add all properties, that need to be copied
        Circle newCircle = new Circle(this.getPosition(), this.radius);
        newCircle.mass = this.mass;
        // ...
        
        return newCircle;
    }
    
    @Override
    public double getRadius()
    {
        return radius;
    }
    
    
    @Override
    public boolean contains(double x, double y)
    {
        return Util.distance(new Vector(x, y), this.getPosition()) <= radius;
    }
}
