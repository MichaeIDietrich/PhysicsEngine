package de.engineapp.visual;

import java.awt.*;
import java.awt.geom.*;
import java.lang.reflect.Field;

import de.engine.math.*;
import de.engine.objects.ObjectProperties;

public class Arrow implements IDrawable
{
    private Color color = new Color(180, 120, 20);
    private Color border = Color.DARK_GRAY;
    
    private Vector from;
    private Vector to;
    
    private ObjectProperties connectedObject = null;
    private Field connectedField = null;
    
    
    public Arrow(Vector location)
    {
        to = from = location;
    }
    
    public Arrow(Vector location, Vector direction)
    {
        from = location;
        to = Util.add(location, direction);
    }
    
    
    public Arrow(ObjectProperties object, String fieldName)
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
        if (color != null)
        {
            this.color = color;
        }
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
            from = connectedObject.getPosition();
            try
            {
                to = Util.add(from, (Vector) connectedField.get(connectedObject));
            }
            catch (IllegalArgumentException | IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }
        
        Shape polygon = createArrowShape();
        
        g.setColor(color);
        g.fill(polygon);
        
        if (border != null)
        {
            g.setColor(border);
            g.draw(polygon);
        }
    }
    
    
    public Vector getFrom()
    {
        return from;
    }
    
    public void setFrom(Vector from)
    {
        this.from = from;
    }
    
    
    public Vector getTo()
    {
        return to;
    }
    
    public void setTo(Vector to)
    {
        this.to = to;
    }
    
    
    private Shape createArrowShape()
    {
        double arrow_length = Util.distance(from, to);
        double     rotation = Util.getAngle(from, to);
        
        if (arrow_length <= 6.0)
        {
            return new Ellipse2D.Double( from.getX() - 5.0, from.getY() - 5.0 , 10.0, 10.0 );
        }
        else
        {
            int max_width  = 6;
            double arrow_peak = 2 * max_width;
            
            if (arrow_length <= arrow_peak) arrow_length = arrow_peak;
            
            Path2D.Double poly_arrow = new Path2D.Double();
            
            poly_arrow.moveTo(0.0, 0.0);
            poly_arrow.lineTo(0.0,                        max_width / 2.5);
            poly_arrow.lineTo(arrow_length - arrow_peak,  max_width / 2.5);
            poly_arrow.lineTo(arrow_length - arrow_peak,  max_width);
            poly_arrow.lineTo(arrow_length,               0.0);
            poly_arrow.lineTo(arrow_length - arrow_peak,  -max_width);
            poly_arrow.lineTo(arrow_length - arrow_peak,  -max_width / 2.5);
            poly_arrow.lineTo(0.0,                        -max_width / 2.5);
            poly_arrow.lineTo(0.0, 0.0);
            
            poly_arrow.transform(AffineTransform.getRotateInstance(rotation));
            poly_arrow.transform(AffineTransform.getTranslateInstance(from.getX(), from.getY()));
            
            return poly_arrow;
        }
    }
}