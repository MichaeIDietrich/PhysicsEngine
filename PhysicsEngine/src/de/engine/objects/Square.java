package de.engine.objects;

import de.engine.math.Util;
import de.engine.math.Vector;

public class Square extends Polygon implements Cloneable
{
    
    public Square(Vector position, Vector corner)
    {
        super(position);
        this.points = new Vector[4];
        this.points[0] = corner;
        this.points[1] = new Vector( corner.getX(), -corner.getY());
        this.points[2] = new Vector(-corner.getX(), -corner.getY());
        this.points[3] = new Vector(-corner.getX(),  corner.getY());
        this.radius = Util.distanceToOrigin(corner);
        
        this.velocity = new Vector();
        calcMomentOfInertia();
        // this.moment_of_inertia = this.getMass() * (Math.pow((corner.getX() * 2), 2) + Math.pow((corner.getY() * 2), 2)) / 12;
    }
    
    
    public void setSize(double width, double height)
    {
        double halfWidth = width / 2;
        double halfHeight = height / 2;
        
        this.points[0] = new Vector( halfWidth,  halfHeight);
        this.points[1] = new Vector( halfWidth, -halfHeight);
        this.points[2] = new Vector(-halfWidth, -halfHeight);
        this.points[3] = new Vector(-halfWidth,  halfHeight);
    }
    
    @Override
    public Square clone()
    {
        Square newSquare = new Square(this.getPosition().clone(), this.points[0].clone());
        super.clone(newSquare);
        
        return newSquare;
    }
}