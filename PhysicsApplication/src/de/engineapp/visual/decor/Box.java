package de.engineapp.visual.decor;

import java.awt.*;
import java.awt.geom.*;

import de.engine.math.Vector;
import de.engine.objects.ObjectProperties;
import de.engineapp.util.PropertyConnector;
import de.engineapp.visual.IDrawable;

public class Box implements IDrawable
{
    private Color color = null;
    private Color border = Color.CYAN;
    private int drawPriority = 4;
    
    private Vector point1;
    private Vector point2;
    private PropertyConnector<Vector[]> pConnector;
    private boolean showPoints;
    
    
    public Box(Vector point1, Vector point2)
    {
        this.point1 = point1;
        this.point2 = point2;
        pConnector = null;
        showPoints = false;
    }
    
    
    public Box(ObjectProperties object, String propertyName)
    {
        pConnector = new PropertyConnector<>(object, propertyName);
        showPoints = false;
    }
    
    
    public Box(Vector point1, Vector point2, boolean showPoints)
    {
        this.point1 = point1;
        this.point2 = point2;
        pConnector = null;
        this.showPoints = showPoints;
    }
    
    
    public Box(ObjectProperties object, String propertyName, boolean showPoints)
    {
        pConnector = new PropertyConnector<>(object, propertyName);
        this.showPoints = showPoints;
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
        if (pConnector != null)
        {
            Vector[] aabb = pConnector.get();
            
            point1 = new Vector(aabb[0].getX(), aabb[0].getY());
            point2 = new Vector(aabb[1].getX(), aabb[1].getY());
        }
        
        double x1 = point1.getX();
        double y1 = point1.getY();
        double x2 = point2.getX();
        double y2 = point2.getY();
        
        double x = x1 < x2 ? x1 : x2;
        double y = y1 < y2 ? y1 : y2;
        double w = Math.abs(x2 - x1);
        double h = Math.abs(y2 - y1);
        
        Rectangle2D.Double rect = new Rectangle2D.Double(x, y, w, h);
        
        if (color != null)
        {
            g.setColor(color);
            g.fill(rect);
        }
        
        if (border != null)
        {
            g.setColor(border);
            g.draw(rect);
        }
        
        if (showPoints)
        {
            renderVector(g, point1);
            renderVector(g, point2);
        }
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
    
    
    public void renderVector(Graphics2D g, Vector v)
    {
        Path2D.Double indicator = new Path2D.Double();
        indicator.moveTo(v.getX() - 5, v.getY());
        indicator.lineTo(v.getX() + 5, v.getY());
        indicator.moveTo(v.getX(), v.getY() - 5);
        indicator.lineTo(v.getX(), v.getY() + 5);
        
        Stroke currentStroke = g.getStroke();
        float scale = (float) g.getTransform().getScaleX();
        
        g.setStroke(new BasicStroke(3 / scale));
        g.setColor(Color.WHITE);
        g.draw(indicator);
        
        g.setStroke(new BasicStroke(1 / scale));
        g.setColor(Color.BLUE);
        g.draw(indicator);
        
        g.setStroke(currentStroke);
    }
    
    
    public Vector getPoint1()
    {
        return point1;
    }
    
    public void setPoint1(Vector point1)
    {
        this.point1 = point1;
    }
    
    
    public Vector getPoint2()
    {
        return point2;
    }
    
    public void setPoint2(Vector point2)
    {
        this.point2 = point2;
    }
}