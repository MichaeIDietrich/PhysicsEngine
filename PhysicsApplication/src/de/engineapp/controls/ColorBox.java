package de.engineapp.controls;

import java.awt.*;

import javax.swing.*;
import javax.swing.event.*;


/**
 * Small component for displaying a color.
 * 
 * @author Micha
 */
public final class ColorBox extends JComponent
{
    private static final long serialVersionUID = 3922749468925958953L;
    
    
    public ColorBox()
    {
        this.setPreferredSize(new Dimension(20, 20));
        this.setBorder(BorderFactory.createLoweredBevelBorder());
        this.setOpaque(true);
    }
    
    
    @Override
    public void setForeground(Color fg)
    {
        super.setForeground(fg);
        
        for (ChangeListener listener : listenerList.getListeners(ChangeListener.class))
        {
            listener.stateChanged(new ChangeEvent(this));
        }
    }
    
    
    public void addChangeListener(ChangeListener listener)
    {
        listenerList.add(ChangeListener.class, listener);
    }
    
    
    public void removeChangeListener(ChangeListener listener)
    {
        listenerList.remove(ChangeListener.class, listener);
    }
    
    
    public ChangeListener[] getChangeListeners()
    {
        return listenerList.getListeners(ChangeListener.class);
    }
    
    
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        
        if (this.isEnabled())
        {
            g.setColor(this.getForeground());
        }
        else
        {
            g.setColor(Color.DARK_GRAY);
        }
        g.fillRect(2, 2, this.getWidth() - 4, this.getHeight() - 4);
    }
}