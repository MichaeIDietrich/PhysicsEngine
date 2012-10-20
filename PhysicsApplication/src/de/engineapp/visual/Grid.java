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
        g.scale(1 / PresentationModel.RATIO, 1 / PresentationModel.RATIO);
        g.setColor(Color.BLACK);
        
        int maxX = (int) Math.ceil(pModel.getCanvasWidth() / 2 / pModel.getZoom() / 50);
        int minX = (int) Math.floor(pModel.getViewOffsetX() / 50) * 50;
        
        int maxY = (int) Math.ceil(pModel.getCanvasHeight() / 2 / pModel.getZoom() / 50);
        int minY = (int) Math.floor(-pModel.getViewOffsetY() / 50) * 50;
        
        int ii = (int) Math.floor(pModel.getViewOffsetX() / 50);
        int iii = (int) Math.floor(pModel.getViewOffsetY() / 50);
        
        for (int i = -maxX; i < maxX; i++)
        {
            if (i - ii == 0)
            {
                g.setStroke(new BasicStroke(3));
            }
            else if ((i - ii) % 5 == 0)
            {
                g.setStroke(new BasicStroke(1.8f));
            }
            else
            {
                g.setStroke(new BasicStroke(1));
            }
            
            g.drawLine(-minX + i * 50, (int) ((-pModel.getCanvasHeight() / 2 + pModel.getViewOffsetY()) / pModel.getZoom()), 
                    -minX + i * 50, (int) ((pModel.getCanvasHeight() / 2 + pModel.getViewOffsetY()) / pModel.getZoom()));
        }
        
        for (int i = -maxY; i < maxY; i++)
        {
            if (i + iii == 0)
            {
                g.setStroke(new BasicStroke(3));
            }
            else if ((i + iii) % 5 == 0)
            {
                g.setStroke(new BasicStroke(1.8f));
            }
            else
            {
                g.setStroke(new BasicStroke(1));
            }
            
            g.drawLine((int) ((-pModel.getCanvasWidth() / 2 - pModel.getViewOffsetX()) / pModel.getZoom()), -minY + i * 50, 
                    (int) ((pModel.getCanvasWidth() / 2 - pModel.getViewOffsetX()) / pModel.getZoom()), -minY + i * 50);
        }
        
        g.scale(PresentationModel.RATIO, PresentationModel.RATIO);
    }
}
