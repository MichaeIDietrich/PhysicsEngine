package de.engineapp.controls;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

/**
 * An easy-to-use Panel, which organizes its contained components into rows, depending
 * on the used constraints.
 * 
 * @author Micha
 */
public class VerticalBoxPanel extends JPanel
{
    private static final long serialVersionUID = -4990424757171113810L;
    
    private Box box;
    
    
    /**
     * Create a new <code>VerticalBoxPanel</code>, which will align components in row.
     */
    public VerticalBoxPanel()
    {
        box = Box.createVerticalBox();
        box.setOpaque(false);
        box.setBorder(new EmptyBorder(5, 5, 5, 5));
        super.add(box);
    }
    
    
    /**
     * Adds a component to the panel, whereas every component will be placed in its own row.
     * The component will be aligned left.
     */
    @Override
    public Component add(Component comp)
    {
        add(comp, null);
        return comp;
    }
    
    
    /**
     * Adds a component to the panel, whereas every component will be placed in its own row.
     * Use LEFT_ALIGNMENT, CENTER_ALIGNMENT or RIGHT_ALIGNMENT to align a component.
     * LEFT_ALIGNMENT is default.
     */
    @Override
    public void add(Component comp, Object constraints)
    {
        // special case for JScrollPane
//        if (constraints != null && constraints instanceof String)
//        {
//            super.add(comp, constraints);
//            return;
//        }
        
        float alignment;
        
        if (comp == null)
        {
            throw new NullPointerException("component can't be null");
        }
        
        if (constraints == null)
        {
            alignment = comp.getAlignmentX();
        }
        else if (constraints instanceof Float)
        {
            alignment = (float) constraints;
        }
        else
        {
            throw new RuntimeException("constraints must be either LEFT_ALIGNMENT, CENTER_ALIGNMENT or RIGHT_ALIGNMENT");
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
    
    
    /**
     * Adds multiple components in one row to the panel.
     * 
     * @param gap - size of the gap between these components
     * @param components - array of components to be added
     */
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
    
    
    /**
     * Adds a vertical gap between the last and the next row.
     * 
     * @param height - size of the gap between these rows
     */
    public void addGap(int height)
    {
        box.add(Box.createVerticalStrut(height));
    }
    
    
    /**
     * Adds a separating line after the last row.
     */
    public void addSeparator()
    {
        box.add(new JSeparator());
    }
    
    
    @Override
    public void removeAll()
    {
        box.removeAll();
    }
}