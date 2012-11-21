package de.engineapp;

import java.awt.Point;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;

import javax.swing.event.EventListenerList;

import de.engine.PhysicsEngine2D;
import de.engine.environment.Scene;
import de.engine.math.Vector;
import de.engine.objects.Ground;
import de.engine.objects.ObjectProperties;
import de.engineapp.controls.Canvas;

/**
 * The PresentationModel handles the whole communication between the application GUI and
 * provides a lot of functionality and event-handling to cover all the communication stuff.
 * 
 * @author Michael
 */
public class PresentationModel
{
    /**
     * The SceneListener recognizes changes to the scene and objects within the scene.
     * 
     * @author Michael
     */
    public interface SceneListener extends java.util.EventListener
    {
        public void objectAdded(ObjectProperties object);
        public void objectRemoved(ObjectProperties object);
        public void groundAdded(Ground ground);
        public void groundRemoved(Ground ground);
        public void objectSelected(ObjectProperties object);
        public void objectDeselected(ObjectProperties object);
        public void objectUpdated(ObjectProperties object);
        public void sceneUpdated(Scene scene);
    }
    
    /**
     * The ViewBoxListener recognizes changes to the representation of the scene.
     * 
     * @author Michael
     */
    public interface ViewBoxListener extends java.util.EventListener
    {
        public void offsetChanged(int offsetX, int offsetY);
        public void sizeChanged(int width, int height);
        public void zoomChanged(double zoom);
    }
    
    /**
     * The PaintListener recognizes and provides an event to tell other
     * program parts wether the scene was repainted
     * 
     * @author Michael
     */
    public interface PaintListener extends java.util.EventListener
    {
        public void repaintCanvas();
    }
    
    /**
     * The storageListener provides listeners for modified states and properties. 
     * 
     * @author Michael
     */
    public interface StorageListener extends java.util.EventListener
    {
        public void stateChanged(String id, boolean value);
        public void propertyChanged(String id, String value);
    }
    
    /**
     * The EventListener provides a listener for small events to avoid
     * unnecessary code
     * 
     * @author Michael
     */
    public interface EventListener extends java.util.EventListener
    {
        public void eventFired(String name);
    }
    
    
    /** list of registered listeners */
    private EventListenerList listenerList;
    
    
    // stores the navigation offset (navigation by the use of the right mouse button)
    private int viewOffsetX = 0;
    private int viewOffsetY = 0;
    private int canvasWidth = 0;
    private int canvasHeight = 0;
    
    private double zoom = 1.0;
    
    /** stores all boolean states, e.g. show grid, show info */
    private HashMap<String, Boolean> stateMap;
    /** stores all String properties */
    private HashMap<String, String> propertyMap;
    
    private Physics physicsState = null;
    
    /** Instance of the PhysicsEngine */
    private PhysicsEngine2D physicsEngine2D = null;
    /** Active Scene */
    private Scene scene = null;
    /** contains a scene which can be restored */
    private Scene storedScene = null;
    /** Selected object in the active Scene */
    private ObjectProperties selectedObject = null;
    /** Canvas control */
    private Canvas canvas = null;
    /** instance of the last copied object */
    private ObjectProperties copiedObject = null;
    
    
    /**
     * Creates a new Instance of the PresentationModel.
     */
    public PresentationModel()
    {
        listenerList = new EventListenerList();
        
        stateMap = new HashMap<>();
        propertyMap = new HashMap<>();
        
        // attach this model to the configuration to load the settings
        // on startup and save on shutdown
        Configuration.getInstance().attachPresentationModel(this);
    }
    
    public void addViewBoxListener(ViewBoxListener listener)
    {
        listenerList.add(ViewBoxListener.class, listener);
    }
    
    public void removeViewBoxListener(ViewBoxListener listener)
    {
        listenerList.remove(ViewBoxListener.class, listener);
    }
    
    private void fireViewBoxOffsetEvents()
    {
        for (ViewBoxListener listener : listenerList.getListeners(ViewBoxListener.class))
        {
            listener.offsetChanged(viewOffsetX, viewOffsetY);
        }
    }
    
    private void fireResizeCanvasEvents()
    {
        for (ViewBoxListener listener : listenerList.getListeners(ViewBoxListener.class))
        {
            listener.sizeChanged(canvasWidth, canvasHeight);
        }
    }
    
    
    public void addSceneListener(SceneListener listener)
    {
        listenerList.add(SceneListener.class, listener);
    }
    
    public void removeSceneListener(SceneListener listener)
    {
        listenerList.remove(SceneListener.class, listener);
    }
    
    
    public void addPaintListener(PaintListener listener)
    {
        listenerList.add(PaintListener.class, listener);
    }
    
    public void removePaintListener(PaintListener listener)
    {
        listenerList.remove(PaintListener.class, listener);
    }
    
    
    public void addStorageListener(StorageListener listener)
    {
        listenerList.add(StorageListener.class, listener);
    }
    
    public void removeStorageListener(StorageListener listener)
    {
        listenerList.remove(StorageListener.class, listener);
    }
    
    private void fireStateListeners(String id)
    {
        Boolean value = stateMap.get(id);
        
        if (value != null)
        {
            for (StorageListener listener : listenerList.getListeners(StorageListener.class))
            {
                listener.stateChanged(id, value);
            }
        }
    }
    
    private void firePropertyListeners(String id)
    {
        for (StorageListener listener : listenerList.getListeners(StorageListener.class))
        {
            listener.propertyChanged(id, propertyMap.get(id));
        }
    }
    
    
    public void addEventListener(EventListener listener)
    {
        listenerList.add(EventListener.class, listener);
    }
    
    public void removeEventListener(EventListener listener)
    {
        listenerList.remove(EventListener.class, listener);
    }
    
    public void fireEventListeners(String eventName)
    {
        for (EventListener listener : listenerList.getListeners(EventListener.class))
        {
            listener.eventFired(eventName);
        }
    }
    
    
    public void addMouseListenerToCanvas(MouseListener listener)
    {
        canvas.addMouseListener(listener);
    }
    
    public void addMouseMotionListenerToCanvas(MouseMotionListener listener)
    {
        canvas.addMouseMotionListener(listener);
    }
    
    
    public void createObjectOnCanvas(String id, Point location)
    {
        canvas.createObject(id, location);
    }
    
    
    ////////////////////////
    /// GETTER // SETTER ///
    ////////////////////////
    
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
    
    
    public void setViewOffset(int viewOffsetX, int viewOffsetY)
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
        Configuration.getInstance().setZoom(zoom);
        
        for (ViewBoxListener listener : listenerList.getListeners(ViewBoxListener.class))
        {
            listener.zoomChanged(zoom);
        }
    }
    
    public void setZoom(double zoom, Point point)
    {
        Configuration.getInstance().setZoom(zoom);
        
        Vector center = toTransformedVector(point);
        this.zoom = zoom;
        this.setViewOffset((int) (-center.getX() * zoom -  canvasWidth / 2 + point.x), 
                           (int) ( center.getY() * zoom - canvasHeight / 2 + point.y));
        
        
//        this.setViewOffset((int) (-center.getX() * zoom + center.getX() + viewOffsetX * zoom), (int) (center.getY() * zoom - center.getY() - viewOffsetX / zoom));
        
        for (ViewBoxListener listener : listenerList.getListeners(ViewBoxListener.class))
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
        setSelectedObject(null);
        this.scene = scene;
        
        if (physicsEngine2D != null)
        {
            physicsEngine2D.setScene(scene);
        }
    }
    
    
    public void storeScene()
    {
        storedScene = scene.clone();
    }
    
    public void restoreScene()
    {
        setSelectedObject(null);
        scene = storedScene;
        physicsEngine2D.setScene(scene);
    }
    
    public boolean hasStoredScene()
    {
        return storedScene != null;
    }
    
    
    public ObjectProperties getSelectedObject()
    {
        return selectedObject;
    }
    
    public void setSelectedObject(ObjectProperties object)
    {
        if (selectedObject != object)
        {
            if (selectedObject != null)
            {
                for (SceneListener listener : listenerList.getListeners(SceneListener.class))
                {
                    listener.objectDeselected(selectedObject);
                }
            }
            
            this.selectedObject = object;
            
            if (object != null)
            {
                for (SceneListener listener : listenerList.getListeners(SceneListener.class))
                {
                    listener.objectSelected(selectedObject);
                }
            }
        }
    }
    
    
    public void setCanvas(Canvas canvas)
    {
        this.canvas = canvas;
    }
    
    
    public void addObject(ObjectProperties object)
    {
        if (scene != null && object != null)
        {
            setSelectedObject(object);
            
            scene.add(object);
            
            for (SceneListener listener : listenerList.getListeners(SceneListener.class))
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
                for (SceneListener listener : listenerList.getListeners(SceneListener.class))
                {
                    listener.objectDeselected(object);
                }
            }
            
            scene.remove(object);
            
            for (SceneListener listener : listenerList.getListeners(SceneListener.class))
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
            
            for (SceneListener listener : listenerList.getListeners(SceneListener.class))
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
            
            for (SceneListener listener : listenerList.getListeners(SceneListener.class))
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
    public void fireRepaint()
    {
        fireRepaint(false);
    }
    
    /**
     * This method will cause a repaint of the canvas and its content.
     * 
     * @param force - if this parameter is true, the canvas will be repainted even if the PhysicsEngine is running.
     */
    public void fireRepaint(boolean force)
    {
        if (force || !physicsState.isRunning())
        {
            for (PaintListener listener : listenerList.getListeners(PaintListener.class))
            {
                listener.repaintCanvas();
            }
        }
    }
    
    
    public void fireSceneUpdated()
    {
        for (SceneListener listener : listenerList.getListeners(SceneListener.class))
        {
            listener.sceneUpdated(scene);
        }
    }
    
    
    public void fireObjectUpdated(ObjectProperties object)
    {
        for (SceneListener listener : listenerList.getListeners(SceneListener.class))
        {
            listener.objectUpdated(object);
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
        setState(id, !isState(id));
    }
    
    
    public String getProperty(String id)
    {
        return propertyMap.get(id);
    }
    
    public void setProperty(String id, String value)
    {
        propertyMap.put(id, value);
        
        firePropertyListeners(id);
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
    
    
    public ObjectProperties getCopiedObject()
    {
        return copiedObject;
    }
    
    public void setCopiedObject(ObjectProperties copiedObject)
    {
        this.copiedObject = copiedObject;
    }
}