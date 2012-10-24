package de.engineapp.controls;

import java.awt.*;

import javax.swing.*;

public class VerticalBoxPanel extends JPanel
{
    private static final long serialVersionUID = -4990424757171113810L;
    
    
    public VerticalBoxPanel()
    {
        this.setOpaque(false);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
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
        
        if (alignment == Component.RIGHT_ALIGNMENT)
        {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            panel.setOpaque(false);
            panel.add(comp);
            super.add(panel);
        }
        else if (alignment == Component.CENTER_ALIGNMENT)
        {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            panel.setOpaque(false);
            panel.add(comp);
            super.add(panel);
        }
        else
        {
            // stupid, but it seems to be a design issue, because the 
            // setAlignmentX-method is missing im java.awt.Component
            if (comp instanceof JComponent)
            {
                ((JComponent) comp).setAlignmentX(Component.LEFT_ALIGNMENT);
            }
            super.add(comp);
        }
    }
    
    
    public void addGroup(int gap, Component... components)
    {
        JPanel group = new JPanel(new GridLayout(1, components.length, gap, gap));
        group.setOpaque(false);
        
        for (Component comp : components)
        {
            group.add(comp);
        }
        
        super.add(group);
    }
    
    
    public void addGap(int height)
    {
        super.add(Box.createVerticalStrut(height));
    }
    
    
    public void addSeparator()
    {
        super.add(new JSeparator());
    }
}