package de.engineapp.visual;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

import de.engine.math.Vector;
import de.engineapp.*;


/**
 * Visual representation of a physics engine circle object.
 * 
 * @author Micha
 */
public final class Circle extends de.engine.objects.Circle implements IDrawable, ISelectable, IDecorable, Cloneable
{
    private String name;
    private Color color = Color.RED;
    private Color border = null;
    private int drawPriority = 1;
    private HashMap<String, IDrawable> decorMap;
    
    
    public Circle(PresentationModel model, Vector position, double radius)
    {
        super(position, radius);
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
        Shape circle = getShape();
        
        g.setColor(color);
        g.fill(circle);
        
        if (border != null)
        {
            g.draw(circle);
        }
        
        
        if (Configuration.getInstance().isState("debug"))
        {
            double r = this.getRadius();
            double r2 = r * 2;
            double x = this.getPosition().getX() - r;
            double y = this.getPosition().getY() - r;
            
            double angle = -Math.toDegrees(this.getRotationAngle());
            
            Arc2D.Double arc = new Arc2D.Double(x, y, r2, r2, angle, -90, Arc2D.PIE);
            
            g.setColor(Color.BLACK);
            g.fill(arc);
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
    
    
      //////////////////////////
     //////    caching    /////
    //////////////////////////
    
    private Shape cachedShape = null;
    private double lastX = 0.0;
    private double lastY = 0.0;
    private double lastRadius = 0.0;
    
    
    private Shape getShape()
    {
        if (cachedShape == null || lastX != this.getPosition().getX()
                || lastY != this.getPosition().getY() || lastRadius != this.getRadius())
        {
            cachedShape = createNewShape();
            lastX = this.getPosition().getX();
            lastY = this.getPosition().getY();
            lastRadius = this.getRadius();
        }
        
        return cachedShape;
    }
    
    private Shape createNewShape()
    {
        double radius = this.getRadius();
        double diameter = radius * 2;
        double x = this.getPosition().getX() - radius;
        double y = this.getPosition().getY() - radius;
        
        Ellipse2D.Double circle = new Ellipse2D.Double(x, y, diameter, diameter);
        
        return circle;
    }
    
    
    @Override
    public Circle clone()
    {
        return clone(true);
    }
    
    @Override
    public Circle clone(boolean cloneId)
    {
        Circle newCircle = new Circle(null, this.getPosition(), this.getRadius());
        newCircle.name = this.name;
        newCircle.color = this.color;
        newCircle.border = this.border;
//        newCircle.decorMap = this.decorMap; // bad idea :p
        
        super.clone(newCircle, true);
        
        return newCircle;
    }
}