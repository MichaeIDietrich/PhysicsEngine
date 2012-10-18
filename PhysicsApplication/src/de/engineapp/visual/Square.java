package de.engineapp.visual;

import java.awt.Color;
import java.awt.Graphics2D;

import de.engine.math.Vector;
import de.engineapp.PresentationModel;

public class Square extends de.engine.objects.Square implements IObject
{
    private Color color = Color.GREEN;
    private Color border = null;
    
    
    public Square(PresentationModel model, Vector position, Vector corner)
    {
        super(position, corner);
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
        // TODO Auto-generated method stub
    }
    
}
