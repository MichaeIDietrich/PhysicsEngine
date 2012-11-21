package de.engineapp.visual.decor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Path2D;

import de.engine.math.Vector;
import de.engine.objects.ObjectProperties;
import de.engineapp.util.PropertyConnector;
import de.engineapp.visual.IDrawable;

public class Coordinate implements IDrawable
{
    private Color color = Color.BLACK;
    private Color border = Color.WHITE;
    
    private Vector coordinate;
    
    private PropertyConnector<Vector> pConnector;
    
    
    public Coordinate(Vector location)
    {
        coordinate = location;
    }
    
    
    public Coordinate(ObjectProperties object, String propertyName)
    {
        pConnector = new PropertyConnector<>(object, propertyName);
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
        if (pConnector != null)
        {
            coordinate = pConnector.get();
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