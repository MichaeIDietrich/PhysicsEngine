package de.engine.objects;

import de.engine.math.Vector;

public class Circle extends ObjectProperties
{
    private double radius;
    
    public Circle(Vector position, double radius)
    {
        this.position = position;
        this.radius = radius;
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
    public double getRadius()
    {
        return radius;
    }
    
    
    @Override
    public Circle copy()
    {
        // TODO - add all properties, that need to be copied
        Circle newCircle = new Circle(this.position, this.radius);
        newCircle.mass = this.mass;
        // ...
        
        return newCircle;
    }

    @Override
    public Vector[] getAABB()
    {
        Vector aabb[] = new Vector[2];
        aabb[0] = new Vector(position.getPoint().x - radius, position.getPoint().y - radius);
        aabb[1] = new Vector(position.getPoint().x + radius, position.getPoint().y + radius);
        return aabb;
    }
}
