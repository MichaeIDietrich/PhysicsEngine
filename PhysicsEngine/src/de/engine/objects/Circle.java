package de.engine.objects;

import de.engine.math.Rotation;
import de.engine.math.Transformation;
import de.engine.math.Util;
import de.engine.math.Vector;

public class Circle extends ObjectProperties implements Cloneable
{
    public Circle(Vector position, double radius)
    {
        this.world_position = new Transformation(position, new Rotation(0));
        this.radius = radius;
        
        this.velocity = new Vector();
        this.moment_of_inertia = Math.PI * Math.pow(radius, 4) / 4;
    }
    
    @Override
    public Circle clone()
    {
        Circle newCircle = new Circle(this.getPosition().clone(), this.radius);
        clone(newCircle);
        
        return newCircle;
    }
    
    public void clone(Circle newCircle)
    {
        // TODO - add all properties, that need to be copied
        newCircle.setMass(getMass());
        newCircle.velocity = this.velocity.clone();
        newCircle.id = this.id;
        newCircle.surface = this.surface;
        // ...
    }
    
    @Override
    public double getRadius()
    {
        return radius;
    }
    
    @Override
    public void setRadius(double radius)
    {
        this.moment_of_inertia *= Math.pow(radius / this.radius, 4);
        this.radius = radius;
    }
    
    
    @Override
    public boolean contains(double x, double y)
    {
        return Util.distance(new Vector(x, y), this.getPosition()) <= radius;
    }

    @Override
    public Vector[] getAABB(double time) {
        Vector aabb[] = new Vector[2];
        Vector pos = getPosition(time);
        aabb[0] = new Vector(pos.getX() - radius, pos.getY() - radius);
        aabb[1] = new Vector(pos.getX() + radius, pos.getY() + radius);
        return aabb;
    }

    @Override
    public Vector[] getNextAABB() {
        Vector aabb[] = new Vector[2];
        Vector nextPos = getNextPosition();
        aabb[0] = new Vector(nextPos.getX() - radius, nextPos.getY() - radius);
        aabb[1] = new Vector(nextPos.getX() + radius, nextPos.getY() + radius);
        return aabb;
    }

    @Override
    public void setMass(double mass)
    {
        this.mass = mass;
    }
}
