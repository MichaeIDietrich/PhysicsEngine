package de.engineapp.controls;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;


public class SliderEx extends JComponent implements MouseListener, MouseMotionListener
{
    private static final long serialVersionUID = 7474353843456397216L;
    
    
    private int min;
    private int max;
    private int value;
    private int groupSize;
    
    
    public SliderEx(int minimum, int maximum, int value)
    {
        this.min = minimum;
        this.max = maximum;
        this.value = value;
        this.groupSize = 1;
        
        this.setPreferredSize(new Dimension(20, 100));
        
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }
    
    
    @Override
    public void paint(Graphics g)
    {
        g.setColor(this.getBackground());
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        
        Color colBorder1, colBorder2, colBack1, colBack2, colSlider, colSliderBorder;
        
        if (this.isEnabled())
        {
            colBorder1 = new Color(50, 50, 50);
            colBorder2 = Color.BLACK;
            colBack1 = new Color(150, 150, 150);
            colBack2 = new Color(200, 200, 200);
            colSlider = new Color(210, 210, 210);
            colSliderBorder = new Color(100, 100, 100);
        }
        else
        {
            colBorder1 = new Color(120, 120, 120);
            colBorder2 = new Color(100, 100, 100);
            colBack1 = new Color(150, 150, 150);
            colBack2 = new Color(170, 170, 170);
            colSlider = new Color(140, 140, 140);
            colSliderBorder = new Color(100, 100, 100);
        }
        
        int tx = 5;
        int ty = 7;
        
        Graphics2D g2d = (Graphics2D) g;
        
        int drawWidth = this.getWidth() - 10;
        int drawHeight = this.getHeight() - 14;
        
        g2d.setColor(colBorder1);
        g2d.draw3DRect(tx - 2, ty - 2, drawWidth + 3, drawHeight + 3, false);
        g2d.setColor(colBorder2);
        g2d.draw3DRect(tx - 1, ty - 1, drawWidth + 1, drawHeight + 1, false);
        
        int steps = max - min + 1;
        
        for (int i = 0; i < steps; i += groupSize)
        {
            if (i / groupSize % 2 == 0)
            {
                g2d.setColor(colBack1);
            }
            else
            {
                g2d.setColor(colBack2);
            }
            
            int     x = drawWidth * i / steps;
            int width = drawWidth * Math.min(i + groupSize, max) / steps - x;
            
            g2d.fillRect(tx + x, ty, width, drawHeight);
        }
        
        int v = value - min;
        int x  = drawWidth * v / steps;
        int x2 = drawWidth * (v + 1) / steps;
        x = (x + x2) / 2;
        
        g2d.setColor(colSlider);
        g2d.fill3DRect(tx + x - 4, 2, 8, this.getHeight() - 5, true);
        
        g2d.setColor(colSliderBorder);
        g2d.draw3DRect(tx + x - 5, 2, 9, this.getHeight() - 5, true);
    }
    
    
    public int getMinimum()
    {
        return min;
    }
    
    public void setMinimum(int min)
    {
        this.min = min;
        this.repaint();
    }
    
    
    public int getMaximum()
    {
        return max;
    }
    
    public void setMaximum(int max)
    {
        this.max = max;
        this.repaint();
    }
    
    
    public int getValue()
    {
        return value;
    }
    
    public void setValue(int value)
    {
        this.value = value;
        this.repaint();
        
        for (ChangeListener listener : listenerList.getListeners(ChangeListener.class))
        {
            listener.stateChanged(new ChangeEvent(this));
        }
    }
    
    
    public int getGroupSize()
    {
        return groupSize;
    }
    
    public void setGroupSize(int groupSize)
    {
        this.groupSize = groupSize;
        this.repaint();
    }
    
    
    @Override
    public void mouseDragged(MouseEvent e)
    {
        if (SwingUtilities.isLeftMouseButton(e) && this.isEnabled())
        {
            calculateValue(e.getX());
        }
    }
    
    @Override
    public void mouseMoved(MouseEvent e) { }
    
    
    @Override
    public void mouseClicked(MouseEvent e) { }
    
    @Override
    public void mousePressed(MouseEvent e)
    {
        if (SwingUtilities.isLeftMouseButton(e) && this.isEnabled())
        {
            calculateValue(e.getX());
        }
    }
    
    @Override
    public void mouseReleased(MouseEvent e) { }
    
    @Override
    public void mouseEntered(MouseEvent e) { }
    
    @Override
    public void mouseExited(MouseEvent e) { }
    
    
    private void calculateValue(int mouseX)
    {
        int steps = max - min + 1;
        int v = (mouseX - 5) * steps / (this.getWidth() - 10) + min;
        v = Math.max(Math.min(v, max), min);
        setValue(v);
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
}