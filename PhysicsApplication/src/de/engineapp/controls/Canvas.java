package de.engineapp.controls;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;

import javax.swing.*;

import de.engine.environment.Scene;
import de.engine.math.Vector;
import de.engine.objects.*;
import de.engineapp.*;
import de.engineapp.PresentationModel.*;
import de.engineapp.util.*;
import de.engineapp.visual.*;
import de.engineapp.visual.Circle;
import de.engineapp.visual.Ground;
import de.engineapp.visual.Square;
import de.engineapp.visual.decor.*;
import de.engineapp.visual.decor.Box;

import static de.engineapp.Constants.*;


public class Canvas extends JComponent implements MouseListener, MouseMotionListener, SceneListener, KeyListener, 
                                                  MouseWheelListener, StorageListener, ActionListener, IDecorable
{
    private static class ActionMapper
    {
        private JComponent component;
        private ActionListener al;
        
        public ActionMapper(JComponent component, ActionListener al)
        {
            this.component = component;
            this.al = al;
        }
        
        
        public void addAction(KeyStroke keyStroke, final String command)
        {
            component.getInputMap().put(keyStroke, command);
            component.getActionMap().put(command, new AbstractAction()
            {
                private static final long serialVersionUID = -3408826290894454284L;
                
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    al.actionPerformed(new ActionEvent(e.getSource(), ActionEvent.ACTION_PERFORMED, command));
                }
            });
        }
    }
    
    
    private static final long serialVersionUID = -5320479580417617983L;
    
    
    private PresentationModel pModel;
    
    // secondary buffer, which will copied to the front buffer
    // after all draw operations are finished
    private BufferedImage frontBuffer = null;
    private BufferedImage backBuffer  = null;
    
    // all visual objects without physical properties
    private Map<String, IDrawable> sceneDecor;
    
    // this object helps to handle multiple objects and to change their
    // properties
    private ObjectManipulator objectManipulator;
    
    // stores the mouse offset while dragging
    private Point mouseOffset;
    
    // this objects causes a little delay till an object can be dragged
    // this should avoid drags, if an object should only be selected
    private Task dragDelay = null;
    
    /**
     * Create a new Canvas to represent objects.
     * 
     * @param model - Global PresentationModel for the application
     */
    public Canvas(PresentationModel model)
    {
        pModel = model;
        model.setCanvas(this);
        
        sceneDecor = new HashMap<>();
        objectManipulator = new ObjectManipulator(model);
        
        this.addComponentListener(new ComponentAdapter()
        {
            // resize back buffer
            @Override
            public void componentResized(ComponentEvent e)
            {
                frontBuffer = new BufferedImage(Canvas.this.getWidth(), Canvas.this.getHeight(), 
                                                BufferedImage.TYPE_INT_RGB);
                backBuffer  = new BufferedImage(Canvas.this.getWidth(), Canvas.this.getHeight(), 
                                                BufferedImage.TYPE_INT_RGB);
                
                // fire resize
                pModel.resizeCanvas(Canvas.this.getWidth(), Canvas.this.getHeight());
                
                // fire repaint
                pModel.fireRepaint();
            }
        });
        
        
        ActionMapper actionMapper = new ActionMapper(this, this);
        actionMapper.addAction(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK), CMD_COPY);
        actionMapper.addAction(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK), CMD_PASTE);
        
        // implement mouse (motion) listener to make navigating throw the scene
        // and manipulating objects possible
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addKeyListener(this);
        this.addMouseWheelListener(this);
        pModel.addSceneListener(this);
        pModel.addStorageListener(this);
    }
    
    
    /**
     * Cleares the buffer related to the Graphics object.
     * 
     * @param g - Graphics object
     */
    public void clearBuffer(Graphics2D g)
    {
        g.setBackground(this.getBackground());
        
        g.clearRect(0, 0, this.getWidth(), this.getHeight());
    }
    
    
    /**
     * Get the Graphics object, to render on the backbuffer.
     * The backbuffer will automatically cleared.
     */
    public Graphics2D getGraphics()
    {
        Graphics2D g = (Graphics2D) backBuffer.getGraphics();
        
        clearBuffer(g);
        
        return g;
    }
    
    /**
     * Swaps back- and frontbuffer.
     * 
     * After each rerendering, the buffers should be swapped 
     * to make the new buffer valid.
     */
    public void switchBuffers()
    {
        BufferedImage tmp = frontBuffer;
        frontBuffer = backBuffer;
        backBuffer = tmp;
    }
    
    
    // if a repaint event is fired, just paint the frontbuffer
    @Override
    public void paint(Graphics g)
    {
        g.drawImage(frontBuffer, 0, 0, null);
    }
    
    
    @Override
    public void mouseClicked(MouseEvent e) { }
    
    
    @Override
    public void mouseEntered(MouseEvent e) { }
    
    
    @Override
    public void mouseExited(MouseEvent e) { }
    
    
    @Override
    public void mousePressed(MouseEvent e)
    {
        // get in-scene cursor position
        Vector cursor = pModel.toTransformedVector(e.getPoint());
        boolean hasSelection = pModel.hasSelectedObject();
        
        if (SwingUtilities.isLeftMouseButton(e))
        {
            // start a timer, to make moving objects possible after a short delay
            dragDelay = new Task(200);
            dragDelay.start();
        }
        
        // right mouse button initializes the move the view offset
        else if (SwingUtilities.isRightMouseButton(e))
        {
            mouseOffset = new Point(e.getPoint());
        }
        
        // show radius if shift is pressed
        if (hasSelection && GuiUtil.isLeftButton(e, false, true, false))
        {
            objectManipulator.initScaleObjects();
        }
        
        // show angle if alt is pressed
        else if (hasSelection && (GuiUtil.isLeftButton(e, false, false, true) || 
                GuiUtil.isLeftButton(e, true, true, false)))
        {
            objectManipulator.initRotateObjects();
        }
        
        // if ctrl is pressed modify the velocity vector
        else if (hasSelection && GuiUtil.isLeftButton(e, true, false, false))
        {
            // set new velocity
            objectManipulator.speedObjects(cursor);
            
            pModel.fireObjectUpdated(pModel.getSelectedObject());
        }
        
        // if no modifier key is pressed either selected an object or create new objects
        else if (GuiUtil.isLeftButton(e, false, false, false))
        {
            ObjectProperties object = pModel.getScene().getObjectFromPoint(cursor.getX(), cursor.getY());
            
            if (pModel.getMultipleSelectionObjects().contains(object))
            {
                pModel.setSelectedObject(object, true);
            }
            else
            {
                pModel.setSelectedObject(object);
            }
            
            // create new objects if
            // 1. there is no object under the cursor
            // 2. the engine is not running
            // 3. the mode for placing objects is enabled
            // 4. currently the application is not in playback mode
            if (object == null && !pModel.getPhysicsState().isRunning() && pModel.getProperty(PRP_OBJECT_MODE) != null
                    && !pModel.getProperty(PRP_MODE).equals(CMD_PLAYBACK_MODE))
            {
                createObject(pModel.getProperty(PRP_OBJECT_MODE), e.getPoint());
            }
            
            // span a rectangle to select multiple objects
            else if (object == null)
            {
                Box selectionRect = new Box(cursor, cursor);
                selectionRect.setColor(new Color(255, 200, 200, 100));
                selectionRect.setBorder(new Color(255, 100, 100, 255));
                this.putDecor(DECOR_SELECTION_RECT, selectionRect);
            }
        }
        
        // if middle mouse button is pressed, show the object radius
        else if (hasSelection && SwingUtilities.isMiddleMouseButton(e))
        {
            objectManipulator.initScaleObjects();
        }
        
        pModel.fireRepaint();
    }
    
    
    // unload everything that is not needed anymore as long no mouse button is pressed
    @Override
    public void mouseReleased(MouseEvent e)
    {
        if (SwingUtilities.isLeftMouseButton(e))
        {
            dragDelay = null;
            
            if (pModel.getSelectedObject() != null)
            {
                ((IDecorable) pModel.getSelectedObject()).removeDecor(DECOR_RANGE);
                ((IDecorable) pModel.getSelectedObject()).removeDecor(DECOR_ANGLE_VIEWER);
            }
            
            if (getDecor(DECOR_SELECTION_RECT) != null)
            {
                Box selectionRect = (Box) getDecor(DECOR_SELECTION_RECT);
                
                double x1 = selectionRect.getPoint1().getX();
                double y1 = selectionRect.getPoint1().getY();
                double x2 = selectionRect.getPoint2().getX();
                double y2 = selectionRect.getPoint2().getY();
                
                double minX = x1 < x2 ? x1 : x2;
                double minY = y1 < y2 ? y1 : y2;
                double maxX = minX + Math.abs(x2 - x1);
                double maxY = minY + Math.abs(y2 - y1);
                
                for (ObjectProperties object : pModel.getScene().getObjects())
                {
                    if (object.getX() >= minX && object.getX() <= maxX && 
                        object.getY() >= minY && object.getY() <= maxY)
                    {
                        // set the first found object as main selected object
                        if (pModel.hasSelectedObject())
                        {
                            pModel.addMultiSelectionObject(object);
                        }
                        else
                        {
                            pModel.setSelectedObject(object);
                        }
                    }
                }
                
                removeDecor(DECOR_SELECTION_RECT);
                
            }
            pModel.fireRepaint();
        }
        else if (SwingUtilities.isMiddleMouseButton(e) && pModel.getSelectedObject() != null)
        {
            ((IDecorable) pModel.getSelectedObject()).removeDecor(DECOR_RANGE);
            pModel.fireRepaint();
        }
    }
    
    
    @Override
    public void mouseDragged(MouseEvent e)
    {
        // get in-scene cursor position
        Vector cursor = pModel.toTransformedVector(e.getPoint());
        boolean hasSelection = pModel.hasSelectedObject();
        
        // if ctrl is pressed modify the velocity vector (except in playback mode)
        if (hasSelection && GuiUtil.isLeftButton(e, true, false, false) && 
                !pModel.getProperty(PRP_MODE).equals(CMD_PLAYBACK_MODE))
        {
            // set new velocity
            objectManipulator.speedObjects(cursor);
            
            pModel.fireObjectUpdated(pModel.getSelectedObject());
            pModel.fireRepaint();
        }
        
        // if alt is pressed modify the object's angle (except in playback mode)
        else if (hasSelection && (GuiUtil.isLeftButton(e, false, false, true) || 
                GuiUtil.isLeftButton(e, true, true, false)) && 
                !pModel.getProperty(PRP_MODE).equals(CMD_PLAYBACK_MODE))
        {
            objectManipulator.rotateObjects(cursor);
            
            pModel.fireObjectUpdated(pModel.getSelectedObject());
            pModel.fireRepaint();
        }
        
        // else modify the object's position in the scene (except in playback mode)
        else if (hasSelection && dragDelay != null && dragDelay.isDone() && 
                GuiUtil.isLeftButton(e, false, false, false) && 
                !pModel.getProperty(PRP_MODE).equals(CMD_PLAYBACK_MODE))
        {
            objectManipulator.translateObjects(cursor);
            
            pModel.fireObjectUpdated(pModel.getSelectedObject());
            pModel.fireRepaint();
        }
        
        // update selection rectangle
        else if (!hasSelection && GuiUtil.isLeftButton(e, false, false, false))
        {
            Box selectionRect = (Box) getDecor(DECOR_SELECTION_RECT);
            selectionRect.setPoint2(cursor);
            pModel.fireRepaint();
        }
        
        // middle mouse button or left mouse + shift button modifies the object's radius
        // (except in playback mode)
        else if (hasSelection && (GuiUtil.isLeftButton(e, false, true, false) ||
                SwingUtilities.isMiddleMouseButton(e)) && !pModel.getProperty(PRP_MODE).equals(CMD_PLAYBACK_MODE))
        {
            objectManipulator.scaleObjects(cursor);
            
            // send an update for the selected object, needs some tweaks maybe
            pModel.fireObjectUpdated(pModel.getSelectedObject());
            pModel.fireRepaint();
        }
        
        // right mouse button moves the view offset
        else if (SwingUtilities.isRightMouseButton(e))
        {
            pModel.moveViewOffset(e.getX() - mouseOffset.x, e.getY() - mouseOffset.y);
            mouseOffset.x = e.getPoint().x;
            mouseOffset.y = e.getPoint().y;
            
            // refresh canvas
            pModel.fireRepaint();
        }
        
    }
    
    
    @Override
    public void mouseMoved(MouseEvent e) { }
    
    
    // show decors if an object is added
    @Override
    public void objectAdded(ObjectProperties object)
    {
        if (pModel.isState(STG_SHOW_ARROWS_ALWAYS))
        {
            attachVelocityArrow(object);
        }
        
//        if (pModel.isState(STG_DEBUG))
//        {
//            Box aabb = new Box(object, "getAABB", true);
//            ((IDecorable) object).putDecor(DECOR_AABB, aabb);
//        }
    }
    
    @Override
    public void objectRemoved(ObjectProperties object) { }
    
    
    @Override
    public void groundAdded(de.engine.objects.Ground ground) { }
    
    @Override
    public void groundRemoved(de.engine.objects.Ground ground) { }
    
    
    // show decors if an object is selected
    @Override
    public void objectSelected(ObjectProperties object)
    {
        System.out.println("objectSelected: " + ((ISelectable) object).getName());
        
        // get the decor functionality of on object
        IDecorable decorableObject = (IDecorable) object;
        
        // attach velocity arrow
        Arrow arrow = new Arrow(object, "velocity");
        decorableObject.putDecor(DECOR_ARROW, arrow);
        
        if (pModel.isState(STG_DEBUG))
        {
            // attach calculated intersection point with the ground
            Coordinate coord = new Coordinate( object, "last_intersection" );
            decorableObject.putDecor(DECOR_COORDINATE, coord);
            
            // attach calculated closest point to the ground
            Coordinate closestPoint = new Coordinate( object, "closest_point" );
            closestPoint.setColor(Color.BLUE);
            decorableObject.putDecor(DECOR_CLOSEST_POINT, closestPoint);
        }
        
        // show object radius
        Range selection = new Range(object, "radius", 3);
        selection.setBorder(new Color(100, 100, 255, 200));
        decorableObject.putDecor(DECOR_SELECTION, selection);
        
        // remove world velocity arrow, because it has already another one
        // which can be modified by the user
        if (pModel.isState(STG_SHOW_ARROWS_ALWAYS))
        {
            decorableObject.removeDecor(DECOR_MULTIPLE_ARROW);
        }
    }
    
    // remove all the decor stuff if an object is deselected
    @Override
    public void objectDeselected(ObjectProperties object)
    {
        System.out.println("objectDeselected: " + ((ISelectable) object).getName());
        
        IDecorable decorableObject = (IDecorable) object;
        
        decorableObject.removeDecor(DECOR_ARROW);
        decorableObject.removeDecor(DECOR_COORDINATE);
        decorableObject.removeDecor(DECOR_CLOSEST_POINT);
        decorableObject.removeDecor(DECOR_SELECTION);
        
        
        if (pModel.isState(STG_SHOW_ARROWS_ALWAYS))
        {
            attachVelocityArrow(object);
        }
    }
    
    
    @Override
    public void multipleObjectsSelected(ObjectProperties object)
    {
        System.out.println("multipleObjectsSelected: " + ((ISelectable) object).getName());
        
        Range selection = new Range(object, "radius", 1.5f);
        selection.setBorder(new Color(100, 100, 255, 200));
        ((IDecorable) object).putDecor(DECOR_SELECTION, selection);
    }
    
    @Override
    public void multipleObjectsDeselected(ObjectProperties object)
    {
        System.out.println("multipleObjectsDeselected: " + ((ISelectable) object).getName());
        
        ((IDecorable) object).removeDecor(DECOR_SELECTION);
    }
    
    
    @Override
    public void keyPressed(KeyEvent e) { }
    
    @Override
    public void keyReleased(KeyEvent e) { }
    
    @Override
    public void keyTyped(KeyEvent e)
    {
        // remove an object when the delete key is pressed (127 = Delete Key)
        if (e.getKeyChar() == 127 && pModel.getSelectedObject() != null)
        {
            
            if (pModel.hasMultiSelectionObjects())
            {
                for (ObjectProperties object : pModel.getMultipleSelectionObjects())
                {
                    if (object != pModel.getSelectedObject())
                    {
                        pModel.removeObject(object);
                    }
                }
                pModel.clearMultiSelectionObjects();
            }
            pModel.removeObject(pModel.getSelectedObject());
            
            pModel.fireRepaint();
        }
    }
    
    
    // the user can use the mouse wheel to zoom within the scene
    @Override
    public void mouseWheelMoved(MouseWheelEvent e)
    {
        int wheel;
        // zooming isn't linear so there are some calculations needed
        if (pModel.getZoom() < 2.0)
        {
            wheel = (int) (pModel.getZoom() * 10.0 - 10.0);
        }
        else
        {
            wheel = (int) (pModel.getZoom() + 8.0);
        }
        
        
        wheel -= e.getWheelRotation();
        
        if (wheel < -9)
        {
            // set min (0.1x zoom)
            wheel = -9;
        }
        else if (wheel > 18)
        {
            // set max (10x zoom)
            wheel = 18;
        }
        
        
        if (wheel < 10)
        {
            pModel.setZoom(wheel / 10.0 + 1.0, e.getPoint());
        }
        else
        {
            pModel.setZoom(wheel - 8.0, e.getPoint());
        }
        
        pModel.fireRepaint();
    }
    
    
    @Override
    public void objectUpdated(ObjectProperties object) { }
    
    @Override
    public void sceneUpdated(Scene scene) { }
    
    
    /**
     * Creates new objects and adds them to the current scene
     * 
     * @param id - defines the object type
     * @param location - position where the object will be created
     */
    public void createObject(String id, Point location)
    {
        Vector sceneLocation = pModel.toTransformedVector(location);
        
        switch (id)
        {
            case OBJ_CIRCLE:
                Circle circle = new Circle(pModel, sceneLocation, 8);
                circle.setMass(10);
                pModel.addObject( circle );
                
                break;
                
            case OBJ_SQUARE:
                Square square = new Square(pModel, sceneLocation, 12);
                square.setMass(10);
                pModel.addObject( square );
                
                break;
                
            case OBJ_GROUND:
                pModel.setGround(new Ground(pModel, Ground.STAIRS, (int) sceneLocation.getY()));
                break;
                
            default:
                return;
        }
        
        pModel.fireRepaint();
    }
    
    
    // this is a helper function to create the global velocity arrows
    // because this code will be invoked multiple times
    private void attachVelocityArrow(ObjectProperties object)
    {
        Arrow arrow = new Arrow(object, "velocity");
        arrow.setColor(new Color(128, 128, 255));
        arrow.setBorder(new Color(0, 0, 255));
        ((IDecorable) object).putDecor(DECOR_MULTIPLE_ARROW, arrow);
    }
    
    
    @Override
    public void stateChanged(String id, boolean value)
    {
        // wether the global velocity arrow should be attached or not
        if (id.equals(STG_SHOW_ARROWS_ALWAYS))
        {
            for (ObjectProperties object : pModel.getScene().getObjects())
            {
                if (value)
                {
                    if (object != pModel.getSelectedObject())
                    {
                        attachVelocityArrow(object);
                    }
                }
                else
                {
                    ((IDecorable) object).removeDecor(DECOR_MULTIPLE_ARROW);
                }
            }
            pModel.fireRepaint();
        }
    }
    
    @Override
    public void propertyChanged(String id, String value) { }
    
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch (e.getActionCommand())
        {
            case CMD_COPY:
                if (pModel.getSelectedObject() != null)
                {
                    pModel.setCopiedObject(pModel.getSelectedObject());
                }
                break;
                
            case CMD_PASTE:
                if (pModel.getCopiedObject() != null)
                {
                    ObjectProperties object = pModel.getCopiedObject().clone(false);
                    object.setPosition(object.getX() + object.getRadius(), object.getY() + object.getRadius());
                    pModel.setCopiedObject(object);
                    pModel.addObject(object);
                    pModel.fireRepaint();
                }
                break;
        }
    }
    
    
    @Override
    public void putDecor(String key, IDrawable decor)
    {
        sceneDecor.put(key, decor);
    }
    
    @Override
    public IDrawable getDecor(String key)
    {
        return sceneDecor.get(key);
    }
    
    @Override
    public void removeDecor(String key)
    {
        sceneDecor.remove(key);
    }
    
    @Override
    public Collection<IDrawable> getDecorSet()
    {
        return sceneDecor.values();
    }
}