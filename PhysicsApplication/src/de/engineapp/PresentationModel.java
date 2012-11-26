package de.engineapp;

import java.awt.Point;
import java.awt.event.*;
import java.util.*;

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
        public void multipleObjectsSelected(ObjectProperties object);
        public void multipleObjectsDeselected(ObjectProperties object);
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
    
    private PhysicsConnector physicsState = null;
    
    /** Instance of the PhysicsEngine */
    private PhysicsEngine2D physicsEngine2D = null;
    /** Active Scene */
    private Scene scene = null;
    /** contains a scene which can be restored */
    private Scene storedScene = null;
    /** Selected object in the active Scene */
    private ObjectProperties selectedObject = null;
    /** Selected objects in the active Scene if multiple objects were selected  */
    private List<ObjectProperties> selectedObjects = null;
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
        
        selectedObjects = new ArrayList<>();
        
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
    
    
    /**
     * Creates a new scene obejct depended on the given object id.
     * 
     * @param id - object id
     * @param location - location where the new scene object will be created
     */
    public void createObjectOnCanvas(String id, Point location)
    {
        canvas.createObject(id, location);
    }
    
    
    ////////////////////////
    /// GETTER // SETTER ///
    ////////////////////////
    
    /**
     * Retrieves the x-coordinate of the current scene offset.
     * 
     * @return - x-coordinate
     */
    public int getViewOffsetX()
    {
        return viewOffsetX;
    }
    
    /**
     * Changes the x-coordinate of the scene offset.
     * 
     * @param x - x-coordinate
     * @param y - y-coordinate
     */
    public void setViewOffsetX(int viewOffsetX)
    {
        this.viewOffsetX = viewOffsetX;
        
        fireViewBoxOffsetEvents();
    }
    
    
    /**
     * Retrieves the y-coordinate of the current scene offset.
     * 
     * @return - y-coordinate
     */
    public int getViewOffsetY()
    {
        return viewOffsetY;
    }
    
    /**
    * Changes the y-coordinate of the scene offset.
    * 
    * @param x - x-coordinate
    * @param y - y-coordinate
    */
    public void setViewOffsetY(int viewOffsetY)
    {
        this.viewOffsetY = viewOffsetY;
        
        fireViewBoxOffsetEvents();
    }
    
    
    /**
    * Moves the scene offset to a specific point.
    * 
    * @param x - x-coordinate
    * @param y - y-coordinate
    */
    public void setViewOffset(int viewOffsetX, int viewOffsetY)
    {
        this.viewOffsetX = viewOffsetX;
        this.viewOffsetY = viewOffsetY;
        
        fireViewBoxOffsetEvents();
    }
    
    
    /**
     * Moves the scene offset for a specific distance.
     * 
     * @param x - x-coordinate
     * @param y - y-coordinate
     */
    public void moveViewOffset(int x, int y)
    {
        this.viewOffsetX += x;
        this.viewOffsetY += y;
        
        fireViewBoxOffsetEvents();
    }
    
    
    /**
     * Navigates the scene offset to a specific scene location.
     * 
     * @param pos - location to navigate to
     */
    public void navigateTo(Vector pos)
    {
        
    }
    
    
    /**
     * Returns the current zoom.
     * 
     * @return - current zoom
     */
    public double getZoom()
    {
        return zoom;
    }
    
    /**
     * Sets the current zoom.
     * 
     * @param zoom - new zoom
     */
    public void setZoom(double zoom)
    {
        this.zoom = zoom;
        Configuration.getInstance().setZoom(zoom);
        
        for (ViewBoxListener listener : listenerList.getListeners(ViewBoxListener.class))
        {
            listener.zoomChanged(zoom);
        }
    }
    
    /**
     * Sets the current zoom at a point in the canvas.
     * 
     * @param zoom - new zoom
     * @param point - point on canvas
     */
    public void setZoom(double zoom, Point point)
    {
        Configuration.getInstance().setZoom(zoom);
        
        Vector center = toTransformedVector(point);
        this.zoom = zoom;
        this.setViewOffset((int) (-center.getX() * zoom -  canvasWidth / 2 + point.x), 
                           (int) ( center.getY() * zoom - canvasHeight / 2 + point.y));
        
        for (ViewBoxListener listener : listenerList.getListeners(ViewBoxListener.class))
        {
            listener.zoomChanged(zoom);
        }
    }
    
    
    /**
     * Returns the the entry point to the physics engine.
     * 
     * @return - physics engine enty point
     */
    public PhysicsConnector getPhysicsState()
    {
        return physicsState;
    }
    
    /**
     * Sets a new physics engine entry point.
     * 
     * @param physicsState - new physics engine entry point
     */
    public void setPhysicsState(PhysicsConnector physicsState)
    {
        this.physicsState = physicsState;
    }
    
    
    /**
     * Gets the physics engine.
     * 
     * @return - physics engine
     */
    public PhysicsEngine2D getPhysicsEngine2D()
    {
        return physicsEngine2D;
    }
    
    /**
     * Sets a new physics engine.
     * 
     * @param physicsEngine2D - physics engine
     */
    public void setPhysicsEngine2D(PhysicsEngine2D physicsEngine2D)
    {
        this.physicsEngine2D = physicsEngine2D;
    }
    
    
    /**
     * Gets the current scene.
     * 
     * @return - current scene
     */
    public Scene getScene()
    {
        return scene;
    }
    
    /**
     * Sets the current scene
     * 
     * @param scene - new scene
     */
    public void setScene(Scene scene)
    {
        setSelectedObject(null);
        this.scene = scene;
        
        if (physicsEngine2D != null)
        {
            physicsEngine2D.setScene(scene);
        }
    }
    
    
    /**
     * Stores a copy of the current scene, to make a reset possible.
     */
    public void storeScene()
    {
        storedScene = scene.clone();
    }
    
    /**
     * Restores the previously stored scene copy.
     */
    public void restoreScene()
    {
        setSelectedObject(null);
        scene = storedScene;
        physicsEngine2D.setScene(scene);
    }
    
    /**
     * Checks, wether a scene copy is stored.
     * 
     * @return - true, if a scene copy is stored
     */
    public boolean hasStoredScene()
    {
        return storedScene != null;
    }
    
    
    /**
     * Checks, wether an object within the scene is selected.
     * 
     * @return - true, if there is a selected scene object
     */
    public boolean hasSelectedObject()
    {
        return selectedObject != null;
    }
    
    /**
     * Gets the selected scene object.
     * 
     * @return  selected scene object, or null if there is no selected
     */
    public ObjectProperties getSelectedObject()
    {
        return selectedObject;
    }
    
    /**
     * Sets the selected scene object.
     * 
     * @param object - scene object that will be selected
     */
    public void setSelectedObject(ObjectProperties object)
    {
        setSelectedObject(object, false);
    }
    
    /**
     * Sets the selected scene object. Preserves the multi selection list, if the second parameter is true.
     * 
     * @param object - scene object that will be selected
     * @param preserveSelectionList - wether the multi selection list should be preserved
     */
    public void setSelectedObject(ObjectProperties object, boolean preserveSelectionList)
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
            
            if (object == null)
            {
                clearMultiSelectionObjects();
            }
            else if (preserveSelectionList)
            {
                
                if (!selectedObjects.contains(object))
                {
                    selectedObjects.add(0, object);
                }
                else
                {
                    for (SceneListener listener : listenerList.getListeners(SceneListener.class))
                    {
                        listener.multipleObjectsDeselected(object);
                    }
                }
                
                if (selectedObject != null)
                {
                    for (SceneListener listener : listenerList.getListeners(SceneListener.class))
                    {
                        listener.multipleObjectsSelected(selectedObject);
                    }
                }
            }
            else
            {
                clearMultiSelectionObjects();
                selectedObjects.add(object);
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
    
    
    /**
     * Checks, wether there are multiple objects selected.
     * 
     * @return - true, if there is more than one object selected
     */
    public boolean hasMultiSelectionObjects()
    {
        return selectedObjects.size() > 1;
    }
    
    /**
     * Gets the list of all selected scene objects.
     * 
     * @return - list of scene objects
     */
    public List<ObjectProperties> getMultipleSelectionObjects()
    {
        return selectedObjects;
    }
    
    /**
     * Adds a new scene object to the selected objects list, as long as it is not already in the list.
     * 
     * @param object - scene object
     */
    public void addMultiSelectionObject(ObjectProperties object)
    {
        if (!selectedObjects.contains(object))
        {
            selectedObjects.add(object);
            
            for (SceneListener listener : listenerList.getListeners(SceneListener.class))
            {
                listener.multipleObjectsSelected(object);
            }
        }
    }
    
    /**
     * Removes an object from the selected objects list.
     * 
     * @param object - scene object to be removed
     */
    public void removeMultiSelectionObject(ObjectProperties object)
    {
        selectedObjects.remove(object);
        
        for (SceneListener listener : listenerList.getListeners(SceneListener.class))
        {
            listener.multipleObjectsDeselected(object);
        }
    }
    
    /**
     * Clears the selected obejcts list.
     */
    public void clearMultiSelectionObjects()
    {
        for (ObjectProperties object : selectedObjects)
        {
            if (object != selectedObject)
            {
                for (SceneListener listener : listenerList.getListeners(SceneListener.class))
                {
                    listener.multipleObjectsDeselected(object);
                }
            }
        }
        selectedObjects.clear();
    }
    
    
    /**
     * Sets the canvas on which the scene will be drawn.
     * 
     * @param canvas - canvas object
     */
    public void setCanvas(Canvas canvas)
    {
        this.canvas = canvas;
    }
    
    
    /**
     * Adds an object to the current scene.
     * 
     * @param object - new scene object
     */
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
    
    /**
     * Removes an object from the current scene.
     * 
     * @param object - scene object to be removed
     */
    public void removeObject(ObjectProperties object)
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
    
    
    /**
     * Sets the groundof the current scene.
     * 
     * @param ground - new ground
     */
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
    
    /**
     * Removes the ground from the current scene.
     * 
     * @param ground - ground to be removed
     */
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
    
    
    /**
     * Gets the width of the canvas component.
     * 
     * @return - width of the canvas component
     */
    public int getCanvasWidth()
    {
        return (int) canvasWidth;
    }
    
    /**
     * Sets a new width for the canvas component.
     * 
     * @param canvasWidth - new width
     */
    public void setCanvasWidth(int canvasWidth)
    {
        this.canvasWidth = canvasWidth;
        
        fireResizeCanvasEvents();
    }
    
    
    /**
     * Gets the height of the canvas component.
     * 
     * @return - height of the canvas component
     */
    public int getCanvasHeight()
    {
        return (int) canvasHeight;
    }
    
    /**
     * Sets a new height for the canvas component.
     * 
     * @param canvasHeight - new height
     */
    public void setCanvasHeight(int canvasHeight)
    {
        this.canvasHeight = canvasHeight;
        
        fireResizeCanvasEvents();
    }
    
    /**
     * Resizes the canvas component.
     * 
     * @param width - new width
     * @param height - new height
     */
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
    
    
    /**
     * Fires an event to all registered scene listeners that the scene has been updated.
     */
    public void fireSceneUpdated()
    {
        for (SceneListener listener : listenerList.getListeners(SceneListener.class))
        {
            listener.sceneUpdated(scene);
        }
    }
    
    
    /**
     * Fires an event to all registered scene listeners that a scene object has been updated.
     * 
     * @param object
     */
    public void fireObjectUpdated(ObjectProperties object)
    {
        for (SceneListener listener : listenerList.getListeners(SceneListener.class))
        {
            listener.objectUpdated(object);
        }
    }
    
    
    /**
     * Checks, wether a state has been set.
     * 
     * @param id - id of the state
     * @return - true, if the state is set
     */
    public boolean isState(String id)
    {
        return Boolean.TRUE.equals(stateMap.get(id));
    }
    
    /**
     * Sets a state.
     * 
     * @param id - id of the state
     * @param value - new boolean value of the state
     */
    public void setState(String id, boolean value)
    {
        stateMap.put(id, value);
        
        fireStateListeners(id);
    }
    
    /**
     * Toggles a state.
     * 
     * @param id - id of the state
     */
    public void toggleState(String id)
    {
        setState(id, !isState(id));
    }
    
    
    /**
     * Retrieves the string value of a property.
     * 
     * @param id - id of the property
     * @return - string value of the property
     */
    public String getProperty(String id)
    {
        return propertyMap.get(id);
    }
    
    /**
     * Sets the string value of a property.
     * 
     * @param id - id of the property
     * @param value - new string value of the property
     */
    public void setProperty(String id, String value)
    {
        propertyMap.put(id, value);
        
        firePropertyListeners(id);
    }
    
    /**
     * This method will transform a local cursor position on the canvas 
     * to the internal scene coordinates.
     * 
     * @param point - point on the canvas
     * @return - position within the scene
     */
    public Vector toTransformedVector(Point point)
    {
        return new Vector(
                 (point.x - viewOffsetX - canvasWidth  / 2) /  zoom,
                 (point.y - viewOffsetY - canvasHeight / 2) / -zoom
        );
    }
    
    
    /**
     * Gets the copied scene object.
     * 
     * @return - scene object that has been copied, or null if there is not one
     */
    public ObjectProperties getCopiedObject()
    {
        return copiedObject;
    }
    
    /**
     * Sets a scene object, that will be copied.
     * 
     * @param copiedObject - scene object to copy
     */
    public void setCopiedObject(ObjectProperties copiedObject)
    {
        this.copiedObject = copiedObject;
    }
}