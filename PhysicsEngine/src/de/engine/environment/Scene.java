package de.engine.environment;

import java.util.ArrayList;

import de.engine.objects.Ground;
import de.engine.objects.ObjectProperties;

public class Scene implements Cloneable
{
    
    protected ArrayList<ObjectProperties> objects;
    
    // ground is unique thats why it has it's own property
    protected Ground ground;
    
    
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
        return ground;
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
        // TODO - add all properties, that need to be copied
        Scene newScene = new Scene();
        
        newScene.setGround(this.getGround() != null ? this.getGround().clone() : null);
        
        for (ObjectProperties object : this.getObjects())
        {
            newScene.add(object.clone());
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

    public ObjectProperties getObject( ObjectProperties selectedObject )
    {
        return objects.get( selectedObject.getId());
    }
}