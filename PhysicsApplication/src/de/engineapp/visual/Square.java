package de.engineapp.visual;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.*;
import java.util.*;

import de.engine.math.Vector;
import de.engineapp.PresentationModel;

public class Square extends de.engine.objects.Square implements IDrawable, ISelectable, IDecorable, Cloneable
{
    private String name;
    private Color color = Color.GREEN;
    private Color border = null;
    private HashMap<String, IDrawable> decorMap;
    
    public Square(PresentationModel model, Vector position, Vector corner)
    {
        super(position, corner);
        name = "Object " + this.id;
        decorMap = new HashMap<>();
    }
    
    public Square(PresentationModel model, Vector position, double radius)
    {
        super(position, new Vector(radius * Math.cos(Math.PI / 4), radius * Math.sin(Math.PI / 4)));
        name = "Object " + this.id;
        decorMap = new HashMap<>();
    }
    
    @Override
    public String getName()
    {
        return name;
    }
    
    @Override
    public void setName(String name)
    {
        this.name = name;
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
        this.border = color;
    }
    
    @Override
    public void render(Graphics2D g)
    {
        Path2D.Double square = new Path2D.Double();
        square.moveTo(this.points[3].getX(), this.points[3].getY());
        
        for (Vector point : this.points)
        {
            square.lineTo(point.getX(), point.getY());
        }
        
        square.transform(AffineTransform.getRotateInstance(this.world_position.rotation.getAngle()));
        square.transform(AffineTransform.getTranslateInstance(this.getPosition().getX(), this.getPosition().getY()));
        
        g.setColor(color);
        g.fill(square);
        
        if (border != null)
        {
            g.setColor(border);
            g.draw(square);
        }
    }
    
    @Override
    public void putDecor(String key, IDrawable decor)
    {
        decorMap.put(key, decor);
    }
    
    @Override
    public IDrawable getDecor(String key)
    {
        return decorMap.get(key);
    }
    
    @Override
    public void removeDecor(String key)
    {
        decorMap.remove(key);
    }
    
    @Override
    public Collection<IDrawable> getDecorSet()
    {
        return decorMap.values();
    }
    
    
    @Override
    public Square clone()
    {
        return clone(true);
    }
    
    @Override
    public Square clone(boolean cloneId)
    {
        Square newSquare = new Square(null, this.getPosition(), this.getRadius());
        newSquare.name = this.name;
        newSquare.color = this.color;
        newSquare.border = this.border;
        newSquare.decorMap = this.decorMap;
        
        super.clone(newSquare, cloneId);
        
        return newSquare;
    }
}