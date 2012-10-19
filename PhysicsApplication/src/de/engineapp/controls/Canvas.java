package de.engineapp.controls;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import de.engine.math.Vector;
import de.engineapp.PresentationModel;
import de.engineapp.windows.InfoWindows;


public class Canvas extends JComponent implements MouseListener, MouseMotionListener
{
    private static final long serialVersionUID = -5320479580417617983L;
    
    
    private PresentationModel pModel;
    
    // secondary buffer, which will copied to the front buffer
    // after all draw operations are finished
    private BufferedImage buffer = null;
    
    // stores the mouse offset while dragging
    private Point mouseOffset;
    
    
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
                pModel.fireRedrawSceneEvents();
            }
        });
        
        // implement mouse (motion) listener to make navigating throw the scene
        // and manipulating objects possible
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
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
        if (SwingUtilities.isLeftMouseButton(e))
        {
            Vector v = pModel.toTransformedVector(e.getPoint());
            pModel.setSelectedObject(pModel.getScene().getObjectFromPoint(v.getX(), v.getY()));
            InfoWindows.setData( InfoWindows.ACTION, "Auswahl: " + pModel.getSelectedObject() );
            InfoWindows.refresh();
            System.out.println(v.getX() + "; " +v.getY());
            
            // remember first mouse click
//            point_1_x = (int) v.getX();
//            point_1_y = (int) v.getY();
            
            // clean canvas from arrow polygon
            pModel.fireRedrawSceneEvents();
        }
    }
    
    
    @Override
    public void mouseEntered(MouseEvent e)
    {
        
    }
    
    
    @Override
    public void mouseExited(MouseEvent e)
    {
        
    }
    
    
    @Override
    public void mousePressed(MouseEvent e)
    {
        if (SwingUtilities.isRightMouseButton(e))
        {
            InfoWindows.setData( InfoWindows.ACTION, "Rechte Maustaste gedrückt" );
            InfoWindows.refresh();
            
            mouseOffset = new Point(e.getPoint());
        }
    }
    
    
    @Override
    public void mouseReleased(MouseEvent e)
    {
        
    }
    
    
    @Override
    public void mouseDragged(MouseEvent e)
    {
        // Changes the length of the force-arrow
        if (SwingUtilities.isLeftMouseButton(e))
        {
//            Vector v = pModel.toTransformedVector(e.getPoint());
//            point_2_x = (int) v.getX();
//            point_2_y = (int) v.getY();
            
            pModel.fireRedrawSceneEvents();
        }
        
        if (SwingUtilities.isRightMouseButton(e))
        {
            pModel.moveViewOffset(e.getX() - mouseOffset.x, e.getY() - mouseOffset.y);
            mouseOffset.x = e.getPoint().x;
            mouseOffset.y = e.getPoint().y;
            
            // refresh canvas
            pModel.fireRedrawSceneEvents();
            
            InfoWindows.setData( InfoWindows.COORDINATES, pModel.toTransformedVector(e.getPoint()) );
            InfoWindows.refresh();
        }
    }
    
    
    @Override
    public void mouseMoved(MouseEvent e)
    {
        InfoWindows.setData( InfoWindows.COORDINATES, pModel.toTransformedVector(e.getPoint()) );
        InfoWindows.refresh();
    }
}