package de.engine.objects;

import de.engine.math.Point;
import de.engine.math.Util;
import de.engine.math.Vector;

public class Square extends ObjectProperties
{
    private Point corner;
    
    public Square(Vector position)
    {
        this.position = position;
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
        return Util.distanceToOrigin(corner);
    }
    
    
    @Override
    public Square copy()
    {
     // TODO - add all properties, that need to be copied
        Square newSquare = new Square(this.position);
        newSquare.mass = this.mass;
        // ...
        
        return newSquare;
    }

    @Override
    public Vector[] getAABB()
    {
        double x = ((corner.x > 0) ? 1 : -1) * corner.x;
        double y = ((corner.y > 0) ? 1 : -1) * corner.y;
        
        Vector aabb[] = new Vector[2];
        aabb[0] = new Vector(position.getPoint().x - x, position.getPoint().y - y);
        aabb[1] = new Vector(position.getPoint().x + x, position.getPoint().y + y);
        return aabb;
    }
}
