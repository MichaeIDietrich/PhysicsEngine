package de.engineapp.visual;

import java.awt.*;

public interface IDrawable
{
    public Color getColor();
    public void setColor(Color color);
    
    public Color getBorder();
    public void setBorder(Color color);
    
    public void render(Graphics2D g);
}
