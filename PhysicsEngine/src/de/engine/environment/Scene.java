package de.engine.environment;

import java.util.ArrayList;

import de.engine.objects.Ground;
import de.engine.objects.ObjectProperties;
import de.engine.physics.colldetect.CollisionData;

/**
 * a scene contains all objects and defines the environment
 *
 */
public class Scene implements Cloneable
{
    
    protected ArrayList<ObjectProperties> objects;
    
    // ground is unique thats why it has it's own property
    protected Ground ground;

    public ArrayList<CollisionData> restingContacts;
    
    public double gravitational_acceleration = -9.80665; // m/s²
    
    private double environment_friction = 0.02;
    
    public boolean enable_env_friction = true;
    
    
    public Scene()
    {
        this.objects = new ArrayList<ObjectProperties>();
        this.restingContacts = new ArrayList<CollisionData>();
    }
    
    public void add(ObjectProperties object)
    {
        this.objects.add(object);
    }
    
    public ObjectProperties getObject(int index)
    {
        return this.objects.get(index);
    }
    
    public Iterable<ObjectProperties> getObjects()
    {
        return this.objects;
    }
    
    public int getCount()
    {
        return this.objects.size();
    }
    
    public void remove(ObjectProperties object)
    {
        this.objects.remove(object);
    }
    
    public void setGround(Ground ground)
    {
        this.ground = ground;
    }
    
    public Ground getGround()
    {
        return ground;
    }
    
    public boolean existGround()
    {
        return (ground != null);
    }
    
    public void removeGround()
    {
       ground = null;
    }
    
    public void removeAll()
    {
        this.objects.clear();
        ground = null;
    }
    
    
    @Override
    public Scene clone()
    {
        return clone(false);
    }
    
    public Scene clone(boolean cloneObjectIds)
    {
        // TODO - add all properties, that need to be copied
        Scene newScene = new Scene();
        
        newScene.gravitational_acceleration = this.gravitational_acceleration;
        newScene.environment_friction = this.environment_friction;
        newScene.enable_env_friction = this.enable_env_friction;
        
        newScene.setGround(this.getGround() != null ? this.getGround().clone() : null);
        
        for (ObjectProperties object : this.getObjects())
        {
            newScene.add(object.clone(cloneObjectIds));
        }
        
        return newScene;
    }
    
    public ObjectProperties getObjectFromPoint(double x, double y)
    {
        for (ObjectProperties object : objects)
        {
            if (object.contains(x, y))
            {
                return object;
            }
        }
        
        return null;
    }

    public double getEnvironmentFriction()
    {
        return environment_friction;
    }

    public void setEnvironmentFriction(double environment_friction)
    {
        if(environment_friction > 1)
            this.environment_friction = 1;
        else if(environment_friction < 0)
            this.environment_friction = 0;
        else
            this.environment_friction = environment_friction;
    }
}