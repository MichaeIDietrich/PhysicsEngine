package de.engineapp;

import java.awt.Point;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import de.engine.environment.Scene;
import de.engine.math.PhysicsEngine2D;
import de.engine.math.Vector;
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
        public void sizeChanged(int width, int height);
        public void zoomChanged(double zoom);
    }
    
    public interface PaintListener
    {
        public void repaintCanvas();
    }
    
    public interface StateListener
    {
        public void stateChanged(String id, boolean value);
    }
    
    
//    public final static double RATIO = 10.0;
    
    
    /** Listeners */
    private Set<ViewBoxListener> viewBoxListeners = null;
    private Set<SceneListener> sceneListeners = null;
    private Set<PaintListener> paintListeners = null;
    private Set<StateListener> stateListeners = null;
    
    
    
    /** stores the navigation offset (navigation by the use of the right mouse button) */
    private int viewOffsetX = 0;
    private int viewOffsetY = 0;
    private int canvasWidth = 0;
    private int canvasHeight = 0;
    
    private double zoom = 1.0;
    
    /** stores all boolean states, e.g. show grid, show info */
    private HashMap<String, Boolean> stateMap;
    
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
        paintListeners = new HashSet<>();
        stateListeners = new HashSet<>();
        
        stateMap = new HashMap<>();
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
    
    private void fireResizeCanvasEvents()
    {
        for (ViewBoxListener listener : viewBoxListeners)
        {
            listener.sizeChanged(canvasWidth, canvasHeight);
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
    
    
    public void addPaintListener(PaintListener listener)
    {
        paintListeners.add(listener);
    }
    
    public void removePaintListener(PaintListener listener)
    {
        paintListeners.add(listener);
    }
    
    
    public void addStateListener(StateListener listener)
    {
        stateListeners.add(listener);
    }
    
    public void removeToggletListener(StateListener listener)
    {
        stateListeners.add(listener);
    }
    
    private void fireStateListeners(String id)
    {
        Boolean value = stateMap.get(id);
        
        if (value != null)
        {
            for (StateListener listener : stateListeners)
            {
                listener.stateChanged(id, value);
            }
        }
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
    
    
    public int getCanvasWidth()
    {
        return (int) canvasWidth;
    }
    
    public void setCanvasWidth(int canvasWidth)
    {
        this.canvasWidth = canvasWidth;
        
        fireResizeCanvasEvents();
    }
    
    
    public int getCanvasHeight()
    {
        return (int) canvasHeight;
    }
    
    public void setCanvasHeight(int canvasHeight)
    {
        this.canvasHeight = canvasHeight;
        
        fireResizeCanvasEvents();
    }
    
    public void resizeCanvas(int width, int height)
    {
        this.canvasWidth = width;
        this.canvasHeight = height;
        
        fireResizeCanvasEvents();
    }
    
    
    /**
     * If the PhysicsEngine is not running, this method will cause a repaint of the canvas and its content.
     */
    public void fireRepaintEvents()
    {
        fireRepaintEvents(false);
    }
    
    /**
     * This method will cause a repaint of the canvas and its content.
     * 
     * @param force - if this parameter is true, the canvas will be repainted even if the PhysicsEngine is running.
     */
    public void fireRepaintEvents(boolean force)
    {
        if (force || !physicsState.isRunning())
        {
            for (PaintListener listener : paintListeners)
            {
                listener.repaintCanvas();
            }
        }
    }
    
    
    public boolean isState(String id)
    {
        return Boolean.TRUE.equals(stateMap.get(id));
    }
    
    public void setState(String id, boolean value)
    {
        stateMap.put(id, value);
        
        fireStateListeners(id);
    }
    
    public void toggleState(String id)
    {
        stateMap.put(id, !isState(id));
        
        fireStateListeners(id);
    }
    
    
    // this method will transform a local cursor position on the canvas
    // to the internal Physics Engine coordinates
    public Vector toTransformedVector(Point point)
    {
        return new Vector(
                 (point.x - viewOffsetX - canvasWidth  / 2) /  zoom,
                 (point.y - viewOffsetY - canvasHeight / 2) / -zoom
        );
    }
}