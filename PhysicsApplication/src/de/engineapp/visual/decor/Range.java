package de.engineapp.visual.decor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

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
        
        String text =  "" + Math.round(r);
        float textX = (float) (connectedObject.getX() + r);
        float textY = (float) connectedObject.getY();
        
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
        
        drawString(g, text, textX, textY);
    }
    
    
    private void drawString(Graphics2D g, String text, float x, float y)
    {
        AffineTransform oldTransform = g.getTransform();
        Stroke oldStroke = g.getStroke();
        
        double scale = oldTransform.getScaleX();
        x *= scale;
        y *= -scale;
        x += 5;
        
        g.scale(1 / scale, -1 / scale);
        g.setStroke(new BasicStroke(1));
        
        Rectangle2D textRect = g.getFontMetrics().getStringBounds(text, g);
        textRect.add(textRect.getMaxX() + 2, textRect.getCenterY());
        textRect.add(textRect.getMinX() - 2, textRect.getCenterY());
        Path2D.Double textPath = new Path2D.Double(textRect);
        textPath.transform(AffineTransform.getTranslateInstance(x, y));
        
        g.setColor(Color.WHITE);
        g.fill(textPath);
        
        g.setColor(Color.BLACK);
        g.draw(textPath);
        g.drawString(text, x, y);
        
        g.setStroke(oldStroke);
        g.setTransform(oldTransform);
    }
}