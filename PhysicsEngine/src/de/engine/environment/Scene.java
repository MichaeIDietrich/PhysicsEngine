package de.engine.environment;

import java.util.ArrayList;

import de.engine.math.PhysicsEngine2D;
import de.engine.objects.Ground;
import de.engine.objects.ObjectProperties;

public class Scene extends EnvironmentProperties
{
    
    // How much pixels are one meter? The ratio could be pixel/meter:
    double ratio = 2d/1d;
    
    private PhysicsEngine2D physicsEngine2D = null;
    
    
    public Scene()
    {
        this.objects = new ArrayList<ObjectProperties>();
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
        return this.ground;
    }
    
    
    public void removeGround()
    {
        this.ground = null;
    }
    
    
    public void removeAll()
    {
        this.objects.clear();
        this.ground = null;
    }
    
    
    public void setPhysicsEngine2D( PhysicsEngine2D pysicsEngine2D )
    {
        this.physicsEngine2D = pysicsEngine2D;
    }
    
    
    public PhysicsEngine2D getPhysicsEngine2D()
    {
        return this.physicsEngine2D;
    }
}