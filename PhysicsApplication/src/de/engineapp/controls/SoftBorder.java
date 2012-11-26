package de.engineapp.controls;

import java.awt.*;

import javax.swing.border.AbstractBorder;


/**
 * Unused border class for soft component borders.
 * 
 * @author Micha
 */
public final class SoftBorder extends AbstractBorder
{
    private static final long serialVersionUID = -3310455406832647655L;
    
    
    private static final Color COLOR_1 = new Color(168, 168, 168);
    private static final Color COLOR_2 = new Color(208, 208, 208);
    private static final Color COLOR_3 = new Color(240, 240, 240);
    
    
    @Override
    public Insets getBorderInsets(Component c)
    {
        return new Insets(2, 2, 2, 2);
    }
    
    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
    {
        g.setColor(COLOR_1);
        
        g.drawLine(2, 0, width - 3, 0);
        g.drawLine(2, height - 1, width - 3, height - 1);
        g.drawLine(0, 2, 0, height - 3);
        g.drawLine(width - 1, 2,  width - 1, height - 3);
        
        g.drawLine(1, 1, 1, 1);
        g.drawLine(width - 2, 1, width - 2, 1);
        g.drawLine(1, height - 2 , 1, height - 2);
        g.drawLine(width - 2, height - 2, width - 2, height - 2);
        
        g.setColor(COLOR_2);
        
        g.drawLine(0, 1, 1, 0);
        g.drawLine(width - 1, 1, width - 2, 0);
        g.drawLine(0, height - 2 , 1, height - 1);
        g.drawLine(width - 1, height - 2, width - 2, height - 1);
        
        g.setColor(COLOR_3);
        
        g.drawLine(0, 0, 0, 0);
        g.drawLine(width - 1, 1, width - 1, 0);
        g.drawLine(0, height - 1 , 1, height - 1);
        g.drawLine(width - 1, height - 1, width - 1, height - 1);
    }
}