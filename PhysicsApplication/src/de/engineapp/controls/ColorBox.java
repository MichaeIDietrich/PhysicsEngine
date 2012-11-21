package de.engineapp.controls;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ColorBox extends JComponent
{
    private static final long serialVersionUID = 3922749468925958953L;
    
    
    private List<ChangeListener> changeListeners;
    
    
    public ColorBox()
    {
        this.setPreferredSize(new Dimension(20, 20));
        this.setBorder(BorderFactory.createLoweredBevelBorder());
        this.setOpaque(true);
        
        changeListeners = new ArrayList<>();
    }
    
    
    @Override
    public void setForeground(Color fg)
    {
        super.setForeground(fg);
        
        for (ChangeListener listener : changeListeners)
        {
            listener.stateChanged(new ChangeEvent(this));
        }
    }
    
    
    public void addChangeListener(ChangeListener listener)
    {
        changeListeners.add(listener);
    }
    
    
    public void removeChangeListener(ChangeListener listener)
    {
        changeListeners.remove(listener);
    }
    
    
    public ChangeListener[] getChangeListeners()
    {
        return changeListeners.toArray(new ChangeListener[changeListeners.size()]);
    }
    
    
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        
        g.setColor(this.getForeground());
        g.fillRect(2, 2, this.getWidth() - 4, this.getHeight() - 4);
    }
}