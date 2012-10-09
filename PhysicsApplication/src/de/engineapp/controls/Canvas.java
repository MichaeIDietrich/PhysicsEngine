package de.engineapp.controls;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;


public class Canvas extends JComponent
{
    // interface to recognize repaints caused by component resizing
    public interface RepaintCallback
    {
        public void repaint();
    }
    
    
    private static final long serialVersionUID = -5320479580417617983L;
    
    private BufferedImage buffer = null;
    
    
    public Canvas(final RepaintCallback repaintCallback)
    {
        this.addComponentListener(new ComponentAdapter()
        {
            // resize back buffer
            @Override
            public void componentResized(ComponentEvent e)
            {
                buffer = new BufferedImage(Canvas.this.getWidth(), Canvas.this.getHeight(), BufferedImage.TYPE_INT_RGB);
                
                // fire repaint
                repaintCallback.repaint();
            }
        });
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
}