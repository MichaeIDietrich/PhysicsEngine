package de.engineapp.controls;

import java.awt.Point;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.BevelBorder;

import de.engineapp.PresentationModel;
import de.engineapp.Util;

public class ObjectToolBar extends JToolBar implements MouseListener
{
    private static final long serialVersionUID = -5441109476332523959L;
    
    
    PresentationModel pModel;
    
    private DragButton circle;
    private DragButton square;
    private DragButton ground;
    
    public ObjectToolBar(PresentationModel model)
    {
        super(JToolBar.VERTICAL);
        
        pModel = model;
        
        this.setBorder( BorderFactory.createBevelBorder( BevelBorder.RAISED ) );
        this.setFloatable(false);
        
        circle = new DragButton(Util.getIcon("circle2"), "circle", true);
        square = new DragButton(Util.getIcon("square2"), "square", true);
        ground = new DragButton(Util.getIcon("ground2"), "ground", Util.getImage("ruler"), new Point(16, 14));
        
        circle.addMouseListener(this);
        square.addMouseListener(this);
        ground.addMouseListener(this);
        
        
        this.add(circle);
        this.add(square);
        this.add(ground);
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        if (SwingUtilities.isLeftMouseButton(e))
        {
            if (e.getComponent().equals(circle))
            {
                square.setPressed(false);
                ground.setPressed(false);
                if (circle.isPressed())
                {
                    pModel.setProperty("ObjectMode", "circle");
                }
                else
                {
                    pModel.setProperty("ObjectMode", null);
                }
            }
            else if (e.getComponent().equals(square))
            {
                circle.setPressed(false);
                ground.setPressed(false);
                if (square.isPressed())
                {
                    pModel.setProperty("ObjectMode", "aquare");
                }
                else
                {
                    pModel.setProperty("ObjectMode", null);
                }
            }
            else if (e.getComponent().equals(ground))
            {
                circle.setPressed(false);
                square.setPressed(false);
                pModel.setProperty("ObjectMode", "ground");
                if (ground.isPressed())
                {
                    pModel.setProperty("ObjectMode", "ground");
                }
                else
                {
                    pModel.setProperty("ObjectMode", null);
                }
            }
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
    }
    
    @Override
    public void mouseReleased(MouseEvent e)
    {
    }
}