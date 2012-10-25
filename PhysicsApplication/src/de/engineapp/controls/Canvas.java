package de.engineapp.controls;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import de.engine.math.*;
import de.engine.objects.*;
import de.engine.objects.Ground;
import de.engineapp.*;
import de.engineapp.PresentationModel.SceneListener;
import de.engineapp.visual.*;
import de.engineapp.windows.InfoWindow;


public class Canvas extends JComponent implements MouseListener, MouseMotionListener, SceneListener
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
                pModel.fireRepaintEvents();
            }
        });
        
        // implement mouse (motion) listener to make navigating throw the scene
        // and manipulating objects possible
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        pModel.addSceneListener(this);
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
    public void mouseClicked(MouseEvent e)
    {
//        if (SwingUtilities.isLeftMouseButton(e))
//        {
//            Vector v = pModel.toTransformedVector(e.getPoint());
//            pModel.setSelectedObject(pModel.getScene().getObjectFromPoint(v.getX(), v.getY()));
//            
//            pModel.fireRepaintEvents();
//            
//            InfoWindow.setData( InfoWindow.ACTION, "Auswahl: " + pModel.getSelectedObject() );
//            InfoWindow.refresh();
//        }
    }
    
    
    @Override
    public void mouseEntered(MouseEvent e) { }
    
    
    @Override
    public void mouseExited(MouseEvent e) { }
    
    
    @Override
    public void mousePressed(MouseEvent e)
    {
        if (SwingUtilities.isLeftMouseButton(e))
        {
            dragDelay = new Task(200);
            dragDelay.start();
            
            if ((e.getModifiers() & InputEvent.CTRL_MASK) != 0 && pModel.getSelectedObject() != null)
            {
                Vector cursor = pModel.toTransformedVector(e.getPoint());
                // set new velocity
                pModel.getSelectedObject().velocity = Util.minus(cursor, pModel.getSelectedObject().getPosition());
                
                pModel.fireRepaintEvents();
            }
            else
            {
                Vector v = pModel.toTransformedVector(e.getPoint());
                pModel.setSelectedObject(pModel.getScene().getObjectFromPoint(v.getX(), v.getY()));
            }
            
            pModel.fireRepaintEvents();
        }
        else if (SwingUtilities.isRightMouseButton(e))
        {
            InfoWindow.setData( InfoWindow.ACTION, "Rechte Maustaste gedrückt" );
            InfoWindow.refresh();
            
            mouseOffset = new Point(e.getPoint());
        }
    }
    
    
    @Override
    public void mouseReleased(MouseEvent e)
    {
        dragDelay = null;
    }
    
    
    @Override
    public void mouseDragged(MouseEvent e)
    {
        if (SwingUtilities.isLeftMouseButton(e) && pModel.getSelectedObject() != null)
        {
            Vector cursor = pModel.toTransformedVector(e.getPoint());
            
            if (e.isControlDown() && !e.isShiftDown() && !e.isAltDown())
            {
                // set new velocity
                pModel.getSelectedObject().velocity = Util.minus(cursor, pModel.getSelectedObject().getPosition());
                pModel.fireRepaintEvents();
            }
            else if (dragDelay != null && dragDelay.isDone() && !e.isControlDown() && !e.isShiftDown() && !e.isAltDown())
            {
                pModel.getSelectedObject().world_position.translation = cursor;
                pModel.fireRepaintEvents();
            }
        }
        else if (SwingUtilities.isRightMouseButton(e))
        {
            pModel.moveViewOffset(e.getX() - mouseOffset.x, e.getY() - mouseOffset.y);
            mouseOffset.x = e.getPoint().x;
            mouseOffset.y = e.getPoint().y;
            
            // refresh canvas
            pModel.fireRepaintEvents();
            
            InfoWindow.setData( InfoWindow.COORDINATES, pModel.toTransformedVector(e.getPoint()) );
            InfoWindow.refresh();
        }
    }
    
    
    @Override
    public void mouseMoved(MouseEvent e)
    {
        InfoWindow.setData( InfoWindow.COORDINATES, pModel.toTransformedVector(e.getPoint()) );
        InfoWindow.refresh();
    }
    
    
    @Override
    public void objectAdded(ObjectProperties object) { }
    
    @Override
    public void objectRemoved(ObjectProperties object) { }
    
    
    @Override
    public void groundAdded(Ground ground) { }
    
    @Override
    public void groundRemoved(Ground ground) { }
    
    
    @Override
    public void objectSelected(ObjectProperties object)
    {
        Arrow arrow = new Arrow(object, "velocity");
        ((ISelectable) object).setArrow(arrow);
    }
    
    @Override
    public void objectUnselected(ObjectProperties object)
    {
        ((ISelectable) object).setArrow(null);
    }
}