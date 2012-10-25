package de.engineapp.visual;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.*;

import de.engine.math.Vector;
import de.engineapp.PresentationModel;

public class Square extends de.engine.objects.Square implements IDrawable, ISelectable
{
    private String name;
    private Color color = Color.GREEN;
    private Color border = null;
    private Arrow arrow = null;
    
    
    public Square(PresentationModel model, Vector position, Vector corner)
    {
        super(position, corner);
        name = "Objekt " + this.id;
    }
    
    
    public Square(PresentationModel model, Vector position, double diagonal)
    {
        super(position, new Vector(diagonal / 2.0, diagonal / 2.0));
        name = "Objekt " + this.id;
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
        square.moveTo(this.getPosition().getX() + this.points[3].getX(), this.getPosition().getY() + this.points[3].getY());
        
        for (Vector point : this.points)
        {
            square.lineTo(this.getPosition().getX() + point.getX(), this.getPosition().getY() + point.getY());
        }
        
        square.transform(AffineTransform.getRotateInstance(this.world_position.rotation.getAngle()));
        
        g.setColor(color);
        g.fill(square);
        
        if (border != null)
        {
            g.setColor(border);
            g.draw(square);
        }
    }
    
    
    @Override
    public Arrow getArrow()
    {
        return arrow;
    }
    
    @Override
    public void setArrow(Arrow arrow)
    {
        this.arrow = arrow;
    }
}