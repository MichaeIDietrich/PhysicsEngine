package de.engineapp.visual;

import java.awt.Color;
import java.awt.Graphics2D;

import de.engine.math.Vector;
import de.engineapp.PresentationModel;

public class Circle extends de.engine.objects.Circle implements IObject
{
    private Color color = Color.RED;
    private Color border = null;
    
    
    public Circle(PresentationModel model, Vector position, double radius)
    {
        super(position, radius);
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
        int r = (int) this.getRadius();
        int r2 = r * 2;
        int x = (int) this.getPosition().getX() - r;
        int y = (int) this.getPosition().getY() - r;
        
        g.setColor( color );
        g.fillOval( x, y, r2, r2);
        
        if (border != null)
        {
            System.out.println("render");
            g.drawOval(x, y, r2, r2);
        }
    }
}
