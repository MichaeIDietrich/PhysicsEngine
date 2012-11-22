package de.engineapp.visual.decor;

import java.awt.*;
import java.awt.geom.*;

import de.engine.objects.ObjectProperties;
import de.engineapp.util.PropertyConnector;
import de.engineapp.visual.IDrawable;

public class Range implements IDrawable
{
    private Color color = null;
    private Color border = Color.CYAN;
    private int drawPriority = 5;
    
    private ObjectProperties connectedObject;
    private PropertyConnector<Double> pConnector;
    private int borderWidth;
    private boolean showTextBox;
    
    
    public Range(ObjectProperties object, String propertyName)
    {
        connectedObject = object;
        pConnector = new PropertyConnector<>(object, propertyName);
        showTextBox = false;
    }
    
    public Range(ObjectProperties object, String propertyName, int borderWidth)
    {
        connectedObject = object;
        pConnector = new PropertyConnector<>(object, propertyName);
        this.borderWidth = borderWidth;
        showTextBox = false;
    }
    
    public Range(ObjectProperties object, String propertyName, boolean showTextBox)
    {
        connectedObject = object;
        pConnector = new PropertyConnector<>(object, propertyName);
        this.showTextBox = showTextBox;
    }
    
    public Range(ObjectProperties object, String propertyName, int borderWidth, boolean showTextBox)
    {
        connectedObject = object;
        pConnector = new PropertyConnector<>(object, propertyName);
        this.borderWidth = borderWidth;
        this.showTextBox = showTextBox;
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
            if (borderWidth == 1)
            {
                g.setColor(border);
                g.draw(circle);
            }
            else
            {
                Stroke oldStroke = g.getStroke();
                double scale = g.getTransform().getScaleX();
                g.setStroke(new BasicStroke((float) (borderWidth / scale)));
                
                g.setColor(border);
                g.draw(circle);
                
                g.setStroke(oldStroke);
            }
        }
        
        if (showTextBox)
        {
            String text =  "" + Math.round(r);
            float textX = (float) (connectedObject.getX() + r);
            float textY = (float) connectedObject.getY();
            
            drawString(g, text, textX, textY);
        }
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