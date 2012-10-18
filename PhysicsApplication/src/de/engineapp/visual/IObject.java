package de.engineapp.visual;

import java.awt.Color;
import java.awt.Graphics2D;

public interface IObject
{
    
    public Color getColor();
    public void setColor(Color color);
    
    public Color getBorder();
    public void setBorder(Color color);
    
    public void render(Graphics2D g);
}
