package de.engineapp.visual.decor;

import java.awt.*;
import java.awt.geom.*;

import de.engine.math.Vector;
import de.engine.objects.*;
import de.engine.objects.Polygon;
import de.engineapp.util.PropertyConnector;
import de.engineapp.visual.IDrawable;


/**
 * Visual representation of a the outline of the connected object.
 * 
 * @author Micha
 */
public final class Outline implements IDrawable
{
    private Color color = null;
    private Color border = Color.CYAN;
    private int drawPriority = 5;
    
    private ObjectProperties connectedObject;
    private PropertyConnector<Vector[]> polygonPoints;
    private float borderWidth;
    
    
    public Outline(ObjectProperties object, float borderWidth)
    {
        connectedObject = object;
        
        if (object instanceof Polygon)
        {
            polygonPoints = new PropertyConnector<>(object, "points");
        }
        
        this.borderWidth = borderWidth;
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
    public int getDrawPriority()
    {
        return drawPriority;
    }
    
    @Override
    public void setDrawPriority(int priority)
    {
        drawPriority = priority;
    }
    
    
    @Override
    public void render(Graphics2D g)
    {
        Shape outline;
        
        if (connectedObject instanceof Circle)
        {
            double radius = connectedObject.getRadius();
            double diameter = radius * 2;
            double x = connectedObject.getPosition().getX() - radius;
            double y = connectedObject.getPosition().getY() - radius;
            
            outline = new Ellipse2D.Double(x, y, diameter, diameter);
        }
        else if (connectedObject instanceof Polygon)
        {
            Vector[] points = polygonPoints.get();
            
            Path2D.Double polygon = new Path2D.Double();
            polygon.moveTo(points[points.length - 1].getX(), points[points.length - 1].getY());
            
            for (Vector point : points)
            {
                polygon.lineTo(point.getX(), point.getY());
            }
            
            polygon.transform(AffineTransform.getRotateInstance(connectedObject.getRotationAngle()));
            polygon.transform(AffineTransform.getTranslateInstance(connectedObject.getPosition().getX(), 
                                                                  connectedObject.getPosition().getY()));
            
            outline = polygon;
        }
        else
        {
            // no other stuff supported yet
            return;
        }
        
        if (color != null)
        {
            g.setColor(color);
            g.fill(outline);
        }
        
        if (border != null)
        {
            if (borderWidth == 1.0f)
            {
                g.setColor(border);
                g.draw(outline);
            }
            else
            {
                Stroke oldStroke = g.getStroke();
                double scale = g.getTransform().getScaleX();
                g.setStroke(new BasicStroke((float) (borderWidth / scale)));
                
                g.setColor(border);
                g.draw(outline);
                
                g.setStroke(oldStroke);
            }
        }
    }
}