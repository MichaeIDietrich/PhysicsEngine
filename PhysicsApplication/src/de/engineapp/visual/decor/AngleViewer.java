package de.engineapp.visual.decor;

import java.awt.*;
import java.awt.geom.*;

import de.engine.objects.ObjectProperties;
import de.engineapp.visual.IDrawable;

public class AngleViewer implements IDrawable
{
    private static final Color DEFAULT_FILL = new Color(255, 255, 255, 200);
    
    
    private Color color = DEFAULT_FILL;
    private Color border = Color.BLACK;
    
    private ObjectProperties connectedObject;
    private double oldAngle;
    
    
    public AngleViewer(ObjectProperties object)
    {
        connectedObject = object;
//        oldAngle = Math.toDegrees(object.getRotationAngle());
        oldAngle = 0;
    }
    
    
    @Override
    public Color getColor()
    {
        return null;
    }
    
    @Override
    public void setColor(Color color)
    {
        this.color = color;
    }
    
    
    @Override
    public Color getBorder()
    {
        return border;
    }
    
    @Override
    public void setBorder(Color color)
    {
        border = color;
    }
    
    
    @Override
    public void render(Graphics2D g)
    {
        double angle = -Math.toDegrees(connectedObject.getRotationAngle());
        double radius = connectedObject.getRadius();
        double diameter = 2 * radius;
        double x = connectedObject.getX() - radius;
        double y = connectedObject.getY() - radius;
        
        Arc2D.Double arc = new Arc2D.Double(x, y, diameter, diameter, oldAngle, angle, Arc2D.PIE);
        
        if (color != null)
        {
            g.setColor(color);
            g.fill(arc);
        }
        if (border != null)
        {
            g.setColor(border);
            g.draw(arc);
        }
    }
}