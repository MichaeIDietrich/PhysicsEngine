package de.engineapp.controls;

import java.awt.Point;

import javax.swing.BorderFactory;
import javax.swing.JToolBar;
import javax.swing.border.BevelBorder;

import de.engineapp.PresentationModel;
import de.engineapp.Util;

public class ObjectToolBar extends JToolBar
{
    private static final long serialVersionUID = -5441109476332523959L;
    
    
    private DragButton circle;
    private DragButton square;
    private DragButton ground;
    
    public ObjectToolBar(PresentationModel model)
    {
        super(JToolBar.VERTICAL);
        
        this.setBorder( BorderFactory.createBevelBorder( BevelBorder.RAISED ) );
        this.setFloatable(false);
        
        circle = new DragButton(Util.getIcon("circle"), "circle", true);
        square = new DragButton(Util.getIcon("square"), "square", true);
        ground = new DragButton(Util.getIcon("ground"), "ground", Util.getImage("ruler"), new Point(16, 14));
        
        
        this.add(circle);
        this.add(square);
        this.add(ground);
    }
}