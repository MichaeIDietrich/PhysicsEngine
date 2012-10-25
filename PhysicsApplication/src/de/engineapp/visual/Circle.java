package de.engineapp.visual;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.*;

import de.engine.math.Vector;
import de.engineapp.PresentationModel;

public class Circle extends de.engine.objects.Circle implements IDrawable, ISelectable, IDecorable
{
    private String name;
    private Color color = Color.RED;
    private Color border = null;
    private HashMap<String, IDrawable> decorMap;
    
    
    public Circle(PresentationModel model, Vector position, double radius)
    {
        super(position, radius);
        name ="Object " + this.id;
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
    
    
    public Color getColor()
    {
        return color;
    }
    
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
        double r = this.getRadius() / PresentationModel.RATIO;
        double r2 = r * 2;
        double x = this.getPosition().getX() - r;
        double y = this.getPosition().getY() - r;
        
        Ellipse2D.Double circle = new Ellipse2D.Double(x, y, r2, r2);
        
        g.setColor(color);
        g.fill(circle);
        
        if (border != null)
        {
            g.draw(circle);
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
}