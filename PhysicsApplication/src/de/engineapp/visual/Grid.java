package de.engineapp.visual;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import de.engineapp.PresentationModel;

public class Grid
{
    private PresentationModel pModel;
    
    
    public Grid(PresentationModel model)
    {
        pModel = model;
    }
    
    
    public void render(Graphics2D g)
    {
        g.scale(1.0, 1.0);
        g.setColor(Color.BLACK);
        
        double minX = (-pModel.getCanvasWidth() / 2 - pModel.getViewOffsetX()) / pModel.getZoom();
        double maxX = ( pModel.getCanvasWidth() / 2 - pModel.getViewOffsetX()) / pModel.getZoom();
        
        int xFrom = (int) Math.ceil(minX / 50.0);
        int xTo   = (int) Math.floor(maxX / 50.0);
        
        double minY = (-pModel.getCanvasHeight() / 2 + pModel.getViewOffsetY()) / pModel.getZoom();
        double maxY = ( pModel.getCanvasHeight() / 2 + pModel.getViewOffsetY()) / pModel.getZoom();
        
        int yFrom = (int) Math.ceil(minY / 50.0);
        int yTo   = (int) Math.floor(maxY / 50.0);
        
        
        for (int i = xFrom; i <= xTo; i++)
        {
            if (i == 0)
            {
                g.setStroke(new BasicStroke(3));
            }
            else if (i % 5 == 0)
            {
                g.setStroke(new BasicStroke(1.8f));
            }
            else
            {
                g.setStroke(new BasicStroke(1));
            }
            
            g.drawLine(i * 50, (int) minY, i * 50, (int) maxY);
        }
        
        for (int i = yFrom; i <= yTo; i++)
        {
            if (i == 0)
            {
                g.setStroke(new BasicStroke(3));
            }
            else if (i % 5 == 0)
            {
                g.setStroke(new BasicStroke(1.8f));
            }
            else
            {
                g.setStroke(new BasicStroke(1));
            }
            
            g.drawLine((int) minX, i * 50, (int) maxX, i * 50);
        }
    }
}