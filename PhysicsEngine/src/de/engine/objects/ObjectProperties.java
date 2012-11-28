package de.engine.objects;

import de.engine.environment.EnvProps;
import de.engine.math.*;

public abstract class ObjectProperties implements Cloneable
{
    // will get incremented every time it's used, to apply unique id's to each
    // of the new objects
    private static int idCounter = 0;
    
    protected int id = 0;
    
    private double frametime = 0;

    public Transformation world_position;

    protected double radius;
    
    protected double mass = 1;
    protected double moment_of_inertia = 0;
    
    public Vector velocity = null;
    public double angular_velocity;
    
    public Material surface = Material.STEEL;

    public boolean isPinned = false;
    
    public Vector last_intersection;
    public Vector closest_point;
    
    protected ObjectProperties()
    {
        this.id = idCounter++;
        this.last_intersection = new Vector(Double.MAX_VALUE, Double.MAX_VALUE);
        this.closest_point = new Vector(Double.MAX_VALUE, Double.MAX_VALUE);
    }

    public int getId()
    {
        return id;
    }
    
    public double getFrameTime()
    {
        if(isPinned)
            return 0;
        return frametime;
    }
    
    public void setFrameTime(double frametime)
    {
        if(isPinned)
            return;
        this.frametime = frametime;
    }
    
    public double getTime()
    {
        return EnvProps.deltaTime() - frametime;
    }
    
    public double getTime(double time)
    {
        return time - frametime;
    }
    
    public Vector getPosition()
    {
        return world_position.translation;
    }
    
    public Vector getPosition(double time)
    {
        if(isPinned)
            return getPosition();
        double localtime = getTime(time);
        return Util.add(world_position.translation, new Vector(velocity.getX(), (EnvProps.grav_acc() / 2d * localtime + velocity.getY())).scale(localtime));
    }
    
    public Vector getNextPosition()
    {
        return getPosition(EnvProps.deltaTime());
    }
    
    public void setPosition(double x, double y)
    {
        world_position.translation = new Vector(x, y);
    }
    
    public double getX()
    {
        return world_position.translation.getX();
    }
    
    public double getY()
    {
        return world_position.translation.getY();
    }
    
    public double getRotationAngle()
    {
        return world_position.rotation.getAngle();
    }
    
    public void setRotationAngle(double angle)
    {
        world_position.rotation.setAngle(angle);
    }
    
    public double getMass()
    {
        return mass;
    }
    
    public double getMoment_of_inertia()
    {
        return moment_of_inertia;
    }
    
    public void setMoment_of_inertia( double value )
    {
        moment_of_inertia = value;
    }
    
    // calc potential energy: Epot = m*g*h
    public double getPotEnergy()
    {
        return -mass * EnvProps.grav_acc() * EnvProps.getHightToGround(getPosition());
    }

    // calc kinetic energy: Epot = m/2*v²
    public double getKinEnergy()
    {
        return 0.5 * mass * velocity.getLength() * velocity.getLength();
    }
    
    public Vector[] getAABB()
    {
        Vector aabb[] = new Vector[2];
        aabb[0] = new Vector(getPosition().getX() - radius, getPosition().getY() - radius);
        aabb[1] = new Vector(getPosition().getX() + radius, getPosition().getY() + radius);
        return aabb;
    }
    
    private void updateRotation(double time)
    {
        world_position.rotation.addAngle(angular_velocity * time * (1 - (EnvProps.friction() * time)));
    }
    
    private void updateVelocity(double time)
    {
        velocity.add(0, (EnvProps.grav_acc() / 2d * time)).scale(1 - (EnvProps.friction() * time));
    }
    
    private void updateAngVelocity(double time)
    {
        angular_velocity *= (1 - (EnvProps.friction() * time));
    }
    
    public void update()
    {
        if (isPinned)
            return;
        world_position.translation = getNextPosition();
        updateRotation(getTime());
        updateVelocity(getTime());
        updateAngVelocity(getTime());
        
        frametime = 0;
    }
    
    public void update(double time)
    {
        if (isPinned)
            return;
        double localtime = getTime(time);
        world_position.translation = getPosition(time);
        updateRotation(localtime);
        updateVelocity(localtime);
        updateAngVelocity(localtime);
        
        frametime = time;
    }
    
    public abstract boolean contains(double x, double y);
    
    public abstract double getRadius();
    
    public abstract void setRadius(double radius);
    
    public abstract void setMass(double mass);
    
    public abstract Vector[] getAABB(double time);
    
    public abstract Vector[] getNextAABB();
    
    @Override
    public abstract ObjectProperties clone();
    public abstract ObjectProperties clone(boolean cloneId);
}
