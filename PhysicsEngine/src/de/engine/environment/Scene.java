package de.engine.environment;

import java.util.ArrayList;

import javax.swing.JComponent;

import de.engine.math.PhysicsEngine2D;
import de.engine.objects.Ground;
import de.engine.objects.ObjectProperties;


public class Scene extends EnvironmentProperties
{
    // HINT - isn't the right place for this, belongs to the application window
    // How much pixels are one meter? The ratio could be pixel/meter:
    double ratio = 2d/1d;
    
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
        
    public Scene copy()
    {
     // TODO - add all properties, that need to be copied
        Scene newScene = new Scene();
        
        newScene.setGround(this.getGround() != null ? this.getGround().copy() : null);
        
        for (ObjectProperties object : this.getObjects())
        {
            newScene.add(object.copy());
        }
        
        return newScene;
    }
}