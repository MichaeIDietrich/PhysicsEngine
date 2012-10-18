package de.engineapp;

import java.util.HashSet;
import java.util.Set;

import de.engine.environment.Scene;
import de.engine.math.PhysicsEngine2D;
import de.engine.objects.Ground;
import de.engine.objects.ObjectProperties;

public class PresentationModel
{
    public interface SceneListener
    {
        public void objectAdded(ObjectProperties object);
        public void objectRemoved(ObjectProperties object);
        public void groundAdded(Ground ground);
        public void groundRemoved(Ground ground);
        public void objectSelected(ObjectProperties object);
        public void objectUnselected(ObjectProperties object);
    }
    
    public interface ViewBoxListener
    {
        public void offsetChanged(int offsetX, int offsetY);
        public void zoomChanged(double zoom);
    }
    
    /** Listeners */
    private Set<ViewBoxListener> viewBoxListeners = null;
    private Set<SceneListener> sceneListeners = null;
    
    
    
    /** stores the navigation offset (navigation by the use of the right mouse button) */
    private int viewOffsetX = 0;
    private int viewOffsetY = 0;
    
    private double zoom = 1.0;
    
    private Physics physicsState = null;
    
    /** Instance of the PhysicsEngine */
    private PhysicsEngine2D physicsEngine2D = null;
    /** Active Scene */
    private Scene scene = null;
    /** Selected object in the active Scene */
    private ObjectProperties selectedObject = null;
    
    
    public PresentationModel()
    {
        viewBoxListeners = new HashSet<>();
        sceneListeners = new HashSet<>();
    }
    
    public void addViewBoxListener(ViewBoxListener listener)
    {
        viewBoxListeners.add(listener);
    }
    
    public void removeViewBoxListener(ViewBoxListener listener)
    {
        viewBoxListeners.add(listener);
    }
    
    private void fireViewBoxOffsetEvents()
    {
        for (ViewBoxListener listener : viewBoxListeners)
        {
            listener.offsetChanged(viewOffsetX, viewOffsetY);
        }
    }
    
    
    public void addSceneListener(SceneListener listener)
    {
        sceneListeners.add(listener);
    }
    
    public void removeSceneListener(SceneListener listener)
    {
        sceneListeners.add(listener);
    }
    
    
    
    public int getViewOffsetX()
    {
        return viewOffsetX;
    }
    
    public void setViewOffsetX(int viewOffsetX)
    {
        this.viewOffsetX = viewOffsetX;
        
        fireViewBoxOffsetEvents();
    }
    
    
    public int getViewOffsetY()
    {
        return viewOffsetY;
    }
    
    public void setViewOffsetY(int viewOffsetY)
    {
        this.viewOffsetY = viewOffsetY;
        
        fireViewBoxOffsetEvents();
    }
    
    
    public void setViewOffsetY(int viewOffsetX, int viewOffsetY)
    {
        this.viewOffsetX = viewOffsetX;
        this.viewOffsetY = viewOffsetY;
        
        fireViewBoxOffsetEvents();
    }
    
    
    public void moveViewOffset(int x, int y)
    {
        this.viewOffsetX += x;
        this.viewOffsetY += y;
        
        fireViewBoxOffsetEvents();
    }
    
    
    public double getZoom()
    {
        return zoom;
    }
    
    public void setZoom(double zoom)
    {
        this.zoom = zoom;
        
        for (ViewBoxListener listener : viewBoxListeners)
        {
            listener.zoomChanged(zoom);
        }
    }
    
    
    public Physics getPhysicsState()
    {
        return physicsState;
    }
    
    public void setPhysicsState(Physics physicsState)
    {
        this.physicsState = physicsState;
    }
    
    
    public PhysicsEngine2D getPhysicsEngine2D()
    {
        return physicsEngine2D;
    }
    
    public void setPhysicsEngine2D(PhysicsEngine2D physicsEngine2D)
    {
        this.physicsEngine2D = physicsEngine2D;
    }
    
    
    public Scene getScene()
    {
        return scene;
    }
    
    public void setScene(Scene scene)
    {
        this.scene = scene;
        
        if (physicsEngine2D != null)
        {
            physicsEngine2D.setScene(scene);
        }
    }
    
    
    public ObjectProperties getSelectedObject()
    {
        return selectedObject;
    }
    
    public void setSelectedObject(ObjectProperties object)
    {
        if (selectedObject != null)
        {
            for (SceneListener listener : sceneListeners)
            {
                listener.objectUnselected(selectedObject);
            }
        }
        
        this.selectedObject = object;
        
        if (object != null)
        {
            for (SceneListener listener : sceneListeners)
            {
                listener.objectSelected(selectedObject);
            }
        }
    }
    
    
    public void addObject(ObjectProperties object)
    {
        if (scene != null && object != null)
        {
            setSelectedObject(object);
            
            scene.add(object);
            
            for (SceneListener listener : sceneListeners)
            {
                listener.objectAdded(object);
            }
        }
    }
    
    public void removedObject(ObjectProperties object)
    {
        if (scene != null && object != null)
        {
            if (selectedObject == object)
            {
                for (SceneListener listener : sceneListeners)
                {
                    listener.objectUnselected(object);
                }
            }
            
            scene.remove(object);
            
            for (SceneListener listener : sceneListeners)
            {
                listener.objectRemoved(object);
            }
        }
    }
    
    
    public void setGround(Ground ground)
    {
        if (scene != null && ground != null)
        {
            scene.setGround(ground);
            
            for (SceneListener listener : sceneListeners)
            {
                listener.groundAdded(ground);
            }
        }
    }
    
    public void removeGround(Ground ground)
    {
        if (scene != null && ground != null)
        {
            scene.setGround(null);
            
            for (SceneListener listener : sceneListeners)
            {
                listener.groundRemoved(ground);
            }
        }
    }
    
}