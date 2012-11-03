package de.engineapp.visual;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.lang.reflect.Field;

import de.engine.objects.ObjectProperties;

public class RangeIndicator implements IDrawable
{
    private Color color = null;
    private Color border = Color.CYAN;
    
    private ObjectProperties connectedObject = null;
    private Field connectedField = null;
    
    
    public RangeIndicator(ObjectProperties object, String fieldName)
    {
        if (object == null || fieldName == null)
        {
            throw new RuntimeException("The arguments must not be null!");
        }
        
        try
        {
            connectedObject = object;
            // should be dynamic
            connectedField = ObjectProperties.class.getDeclaredField(fieldName);
            connectedField.setAccessible(true);
            if (!(connectedField.getType().equals(double.class)))
            {
                throw new RuntimeException("Field must be double!");
            }
        }
        catch (NoSuchFieldException | SecurityException e)
        {
            e.printStackTrace();
        }
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
        double r = 0.0;
        try
        {
            r = connectedField.getDouble(connectedObject);
        }
        catch (IllegalArgumentException | IllegalAccessException e)
        {
            e.printStackTrace();
        }
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