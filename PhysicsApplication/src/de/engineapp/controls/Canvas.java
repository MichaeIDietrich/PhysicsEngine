package de.engineapp.controls;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import de.engine.environment.Scene;
import de.engine.math.*;
import de.engine.math.Util;
import de.engine.objects.*;
import de.engineapp.*;
import de.engineapp.PresentationModel.*;
import de.engineapp.visual.*;
import de.engineapp.visual.Circle;
import de.engineapp.visual.Ground;
import de.engineapp.visual.Square;
import de.engineapp.visual.decor.*;

import static de.engineapp.Constants.*;


public class Canvas extends JComponent implements MouseListener, MouseMotionListener, SceneListener, KeyListener, MouseWheelListener, StorageListener
{
    private static final long serialVersionUID = -5320479580417617983L;
    
    
    private PresentationModel pModel;
    
    // secondary buffer, which will copied to the front buffer
    // after all draw operations are finished
    private BufferedImage buffer = null;
    
    // stores the mouse offset while dragging
    private Point mouseOffset;
    
    // this objects causes a little delay till an object can be dragged
    // this should avoid drags, if an object should only be selected
    Task dragDelay = null;
    
    
    public Canvas(PresentationModel model)
    {
        pModel = model;
        model.setCanvas(this);
        
        this.addComponentListener(new ComponentAdapter()
        {
            // resize back buffer
            @Override
            public void componentResized(ComponentEvent e)
            {
                buffer = new BufferedImage(Canvas.this.getWidth(), Canvas.this.getHeight(), BufferedImage.TYPE_INT_RGB);
                
                // fire resize
                pModel.resizeCanvas(Canvas.this.getWidth(), Canvas.this.getHeight());
                
                // fire repaint
                pModel.fireRepaint();
            }
        });
        
        // implement mouse (motion) listener to make navigating throw the scene
        // and manipulating objects possible
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addKeyListener(this);
        this.addMouseWheelListener(this);
        pModel.addSceneListener(this);
        pModel.addStorageListener(this);
    }
    
    
    public void clearBuffer(Graphics2D g)
    {
        g.setBackground(this.getBackground());
        
        g.clearRect(0, 0, this.getWidth(), this.getHeight());
    }
    
    
    public Graphics2D getGraphics()
    {
        Graphics2D g = (Graphics2D) buffer.getGraphics();
        
        // automatically clear buffer, may be removed later
        clearBuffer(g);
        
        return g;
    }
    
    
    @Override
    public void paint(Graphics g)
    {
        g.drawImage(buffer, 0, 0, null);
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
        this.requestFocusInWindow();
        
        if (e.isShiftDown() && pModel.getSelectedObject() != null)
        {
            Range range = new Range(pModel.getSelectedObject(), "radius");
            ((IDecorable) pModel.getSelectedObject()).putDecor(DECOR_RANGE, range);
            pModel.fireRepaint();
        }
        
        if (SwingUtilities.isLeftMouseButton(e))
        {
            dragDelay = new Task(200);
            dragDelay.start();
            
            if (e.isControlDown() && !e.isShiftDown() && !e.isAltDown() && pModel.getSelectedObject() != null)
            {
                Vector cursor = pModel.toTransformedVector(e.getPoint());
                // set new velocity
                pModel.getSelectedObject().velocity = Util.minus(cursor, pModel.getSelectedObject().getPosition());
                
                pModel.fireObjectUpdated(pModel.getSelectedObject());
                pModel.fireRepaint();
            }
            else if (!e.isControlDown() && !e.isShiftDown() && !e.isAltDown())
            {
                Vector v = pModel.toTransformedVector(e.getPoint());
                ObjectProperties object = pModel.getScene().getObjectFromPoint(v.getX(), v.getY());
                
                if (object == null && !pModel.getPhysicsState().isRunning() && pModel.getProperty(OBJECT_MODE) != null)
                {
                    createObject(pModel.getProperty(OBJECT_MODE), e.getPoint());
                }
                else
                {
                    pModel.setSelectedObject(object);
                }
            }
            else if (!e.isControlDown() && e.isShiftDown() && !e.isAltDown() && pModel.getSelectedObject() != null)
            {
                Range range = new Range(pModel.getSelectedObject(), "radius");
                ((IDecorable) pModel.getSelectedObject()).putDecor(DECOR_RANGE, range);
            }
            
            pModel.fireRepaint();
        }
        else if (SwingUtilities.isRightMouseButton(e))
        {
            mouseOffset = new Point(e.getPoint());
        }
        else if (SwingUtilities.isMiddleMouseButton(e) && pModel.getSelectedObject() != null)
        {
            Range range = new Range(pModel.getSelectedObject(), "radius");
            ((IDecorable) pModel.getSelectedObject()).putDecor(DECOR_RANGE, range);
            pModel.fireRepaint();
        }
    }
    
    
    @Override
    public void mouseReleased(MouseEvent e)
    {
        if (SwingUtilities.isLeftMouseButton(e))
        {
            dragDelay = null;
            
            if (pModel.getSelectedObject() != null)
            {
                ((IDecorable) pModel.getSelectedObject()).removeDecor(DECOR_RANGE);
                pModel.fireRepaint();
            }
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
        Vector cursor = pModel.toTransformedVector(e.getPoint());
        
        if (SwingUtilities.isLeftMouseButton(e) && !e.isShiftDown() && pModel.getSelectedObject() != null)
        {
            if (e.isControlDown() && !e.isShiftDown() && !e.isAltDown())
            {
                // set new velocity
                pModel.getSelectedObject().velocity = Util.minus(cursor, pModel.getSelectedObject().getPosition());
                pModel.fireObjectUpdated(pModel.getSelectedObject());
                pModel.fireRepaint();
            }
            else if (dragDelay != null && dragDelay.isDone() && !e.isControlDown() && !e.isShiftDown() && !e.isAltDown())
            {
                pModel.getSelectedObject().world_position.translation = cursor;
                pModel.fireObjectUpdated(pModel.getSelectedObject());
                pModel.fireRepaint();
            }
        }
        else if (SwingUtilities.isRightMouseButton(e))
        {
            pModel.moveViewOffset(e.getX() - mouseOffset.x, e.getY() - mouseOffset.y);
            mouseOffset.x = e.getPoint().x;
            mouseOffset.y = e.getPoint().y;
            
            // refresh canvas
            pModel.fireRepaint();
        }
        else if ((SwingUtilities.isMiddleMouseButton(e) || (SwingUtilities.isLeftMouseButton(e) && e.isShiftDown())) && pModel.getSelectedObject() != null)
        {
            pModel.getSelectedObject().setRadius(Util.distance(pModel.getSelectedObject().getPosition(), cursor));
            pModel.fireObjectUpdated(pModel.getSelectedObject());
            pModel.fireRepaint();
        }
    }
    
    
    @Override
    public void mouseMoved(MouseEvent e) { }
    
    
    @Override
    public void objectAdded(ObjectProperties object) { }
    
    @Override
    public void objectRemoved(ObjectProperties object) { }
    
    
    @Override
    public void groundAdded(de.engine.objects.Ground ground) { }
    
    @Override
    public void groundRemoved(de.engine.objects.Ground ground) { }
    
    
    @Override
    public void objectSelected(ObjectProperties object)
    {
        IDecorable decorableObject = (IDecorable) object;
        
        Arrow arrow = new Arrow(object, "velocity");
        decorableObject.putDecor(DECOR_ARROW, arrow);
        
        Coordinate coord = new Coordinate( object, "last_intersection" );
        decorableObject.putDecor(DECOR_COORDINATE, coord);
        
        Coordinate closestPoint = new Coordinate( object, "closest_point" );
        closestPoint.setColor(Color.BLUE);
        decorableObject.putDecor(DECOR_CLOSEST_POINT, closestPoint);
        
        if (pModel.isState(SHOW_ARROWS_ALWAYS))
        {
            decorableObject.removeDecor(DECOR_MULTIPLE_ARROW);
        }
    }
    
    @Override
    public void objectDeselected(ObjectProperties object)
    {
        IDecorable decorableObject = (IDecorable) object;
        
        decorableObject.removeDecor(DECOR_ARROW);
        decorableObject.removeDecor(DECOR_COORDINATE);
        decorableObject.removeDecor(DECOR_CLOSEST_POINT);
        
        if (pModel.isState(SHOW_ARROWS_ALWAYS))
        {
            attachVelocityArrow(object);
        }
    }
    
    
    @Override
    public void keyPressed(KeyEvent e) { }
    
    @Override
    public void keyReleased(KeyEvent e) { }
    
    @Override
    public void keyTyped(KeyEvent e)
    {
        // 127 = Delete Key
        if (e.getKeyChar() == 127 && pModel.getSelectedObject() != null)
        {
            pModel.removedObject(pModel.getSelectedObject());
            pModel.fireRepaint();
        }
    }
    
    
    @Override
    public void mouseWheelMoved(MouseWheelEvent e)
    {
        int wheel;
        
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
    
    
    public void createObject(String id, Point location)
    {
        Vector sceneLocation = pModel.toTransformedVector(location);
        
        switch (id)
        {
            case OBJ_CIRCLE:
                Circle circle = new Circle(pModel, sceneLocation, 8);
                circle.setMass(10);
                pModel.addObject( circle );
                
                if (pModel.isState(SHOW_ARROWS_ALWAYS))
                {
                    attachVelocityArrow(circle);
                }
                break;
                
            case OBJ_SQUARE:
                Square square = new Square(pModel, sceneLocation, 12);
                square.setMass(10);
                pModel.addObject( square );
                
                if (pModel.isState(SHOW_ARROWS_ALWAYS))
                {
                    attachVelocityArrow(square);
                }
                break;
                
            case OBJ_GROUND:
                pModel.setGround(new Ground(pModel, Ground.DOWNHILL, (int) sceneLocation.getY()));
                break;
                
            default:
                return;
        }
        
        pModel.fireRepaint();
    }
    
    
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
        if (id.equals(SHOW_ARROWS_ALWAYS))
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
}