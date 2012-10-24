package de.engineapp.controls;

import java.awt.*;

import javax.swing.*;

public class VerticalBoxPanel extends JPanel
{
    private static final long serialVersionUID = -4990424757171113810L;
    
    
    private Box box;
    
    public VerticalBoxPanel()
    {
        box = Box.createVerticalBox();
        box.setOpaque(false);
        super.add(box);
    }
    
    
    @Override
    public Component add(Component comp)
    {
        add(comp, null);
        return comp;
    }
    
    
    @Override
    public void add(Component comp, Object contraints)
    {
        float alignment;
        
        if (comp == null)
        {
            throw new NullPointerException("component can't be null");
        }
        
        if (contraints == null)
        {
            alignment = comp.getAlignmentX();
        }
        else if (contraints instanceof Float)
        {
            alignment = (float) contraints;
        }
        else
        {
            throw new RuntimeException("contraints must be either LEFT_ALIGNMENT, CENTER_ALIGNMENT or RIGHT_ALIGNMENT");
        }
        
        JPanel panel;
        
        if (alignment == Component.RIGHT_ALIGNMENT)
        {
            panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        }
        else if (alignment == Component.CENTER_ALIGNMENT)
        {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        }
        else
        {
            panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        }
        
        panel.setOpaque(false);
        panel.add(comp);
        box.add(panel);
    }
    
    
    public void addGroup(int gap, Component... components)
    {
        JPanel group = new JPanel(new GridLayout(1, components.length, gap, gap));
        group.setOpaque(false);
        
        for (Component comp : components)
        {
            group.add(comp);
        }
        
        box.add(group);
    }
    
    
    public void addGap(int height)
    {
        box.add(Box.createVerticalStrut(height));
    }
    
    
    public void addSeparator()
    {
        box.add(new JSeparator());
    }
}