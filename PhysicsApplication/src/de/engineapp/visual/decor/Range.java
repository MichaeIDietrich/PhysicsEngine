package de.engineapp.visual.decor;

import java.awt.*;
import java.awt.geom.Ellipse2D;

import de.engine.objects.ObjectProperties;
import de.engineapp.util.PropertyConnector;
import de.engineapp.visual.IDrawable;

public class Range implements IDrawable
{
    private Color color = null;
    private Color border = Color.CYAN;
    
    private ObjectProperties connectedObject;
    private PropertyConnector<Double> pConnector;
    
    
    public Range(ObjectProperties object, String propertyName)
    {
        connectedObject = object;
        pConnector = new PropertyConnector<>(object, propertyName);
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
        double r = (Double) pConnector.getObject();
        double r2 = r * 2;
        double x = connectedObject.getPosition().getX() - r;
        double y = connectedObject.getPosition().getY() - r;
        
        Ellipse2D.Double circle = new Ellipse2D.Double(x, y, r2, r2);
        
        if (color != null)
        {
            g.setColor(color);
            g.fill(circle);
        }
        
        if (border != null)
        {
            g.setColor(border);
            g.draw(circle);
        }
    }
}