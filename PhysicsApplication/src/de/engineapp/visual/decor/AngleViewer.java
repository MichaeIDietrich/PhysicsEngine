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
    
    
    public AngleViewer(ObjectProperties object)
    {
        connectedObject = object;
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
        double angle = Math.toDegrees(connectedObject.getRotationAngle());
        angle = angle < 0 ? 360 + angle : angle;
        double radius = connectedObject.getRadius();
        double diameter = 2 * radius;
        double x = connectedObject.getX() - radius;
        double y = connectedObject.getY() - radius;
        
        Arc2D.Double arc = new Arc2D.Double(x, y, diameter, diameter, 0, -angle, Arc2D.PIE);
        
        String text =  Math.round(angle) + "°";
        float textX = (float) (connectedObject.getX() + radius + 5);
        float textY = (float) connectedObject.getY();
        
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
        
        drawString(g, text, textX, textY);
    }
    
    
    private void drawString(Graphics2D g, String text, float x, float y)
    {
        Path2D.Double textRect = new Path2D.Double(g.getFontMetrics().getStringBounds(text, g));
        textRect.transform(AffineTransform.getTranslateInstance(x, -y));
        
        AffineTransform oldTransform = g.getTransform();
        g.scale(1, -1);
        
        g.setColor(Color.WHITE);
        g.fill(textRect);
        
        g.setColor(Color.BLACK);
        g.draw(textRect);
        g.drawString(text, x, -y);
        
        g.setTransform(oldTransform);
    }
}