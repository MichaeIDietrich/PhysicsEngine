package de.engineapp.visual;

import java.awt.*;
import java.awt.geom.*;
import java.lang.reflect.Field;

import de.engine.math.*;
import de.engine.objects.ObjectProperties;

public class CoordinateIndicator implements IDrawable
{
    private Color color = Color.BLACK;
    private Color border = Color.WHITE;
    
    private Vector coordinate;
    
    private ObjectProperties connectedObject = null;
    private Field connectedField = null;
    
    
    public CoordinateIndicator(Vector location)
    {
        coordinate = location;
    }
    
    
    public CoordinateIndicator(ObjectProperties object, String fieldName)
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
            if (!(connectedField.getType().equals(Vector.class)))
            {
                throw new RuntimeException("Field must be a Vector!");
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
        return color;
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
        if (connectedObject != null)
        {
            try
            {
                coordinate = (Vector) connectedField.get(connectedObject);
            }
            catch (IllegalArgumentException | IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }
        
        Path2D.Double indicator = new Path2D.Double();
        indicator.moveTo(coordinate.getX() - 5, coordinate.getY());
        indicator.lineTo(coordinate.getX() + 5, coordinate.getY());
        indicator.moveTo(coordinate.getX(), coordinate.getY() - 5);
        indicator.lineTo(coordinate.getX(), coordinate.getY() + 5);
        
        Stroke currentStroke = g.getStroke();
        float scale = (float) g.getTransform().getScaleX();
        if (border != null)
        {
            g.setStroke(new BasicStroke(3 / scale));
            g.setColor(border);
            g.draw(indicator);
        }
        if (color != null)
        {
            g.setStroke(new BasicStroke(1 / scale));
            g.setColor(color);
            g.draw(indicator);
        }
        g.setStroke(currentStroke);
    }
    
    
    public Vector getCoordinate()
    {
        return coordinate;
    }
    
    public void setCoordinate(Vector coordinate)
    {
        this.coordinate = coordinate;
    }
}