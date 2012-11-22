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
    
    private PropertyConnector<Vector[]> pConnector;
    
    
    public Box(ObjectProperties object, String propertyName)
    {
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
        Vector[] aabb = pConnector.get();
        
        double x1 = aabb[0].getX();
        double y1 = aabb[0].getY();
        double x2 = aabb[1].getX();
        double y2 = aabb[1].getY();
        
        double x = x1 < x2 ? x1 : x2;
        double y = y1 < y2 ? y1 : y2;
        double w = Math.abs(x2 - x1);
        double h = Math.abs(y2 - y1);
        
        Rectangle2D.Double square = new Rectangle2D.Double(x, y, w, h);
        
        if (color != null)
        {
            g.setColor(color);
            g.fill(square);
        }
        
        if (border != null)
        {
            g.setColor(border);
            g.draw(square);
        }
        
        renderVector(g, aabb[0]);
        renderVector(g, aabb[1]);
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
}