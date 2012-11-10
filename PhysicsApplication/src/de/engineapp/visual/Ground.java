package de.engineapp.visual;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;

import de.engineapp.PresentationModel;

public class Ground extends de.engine.objects.Ground implements IDrawable, Cloneable
{
    public final static Color GRASS_GREEN_LIGHT = new Color( 052, 158, 052 );
    public final static Color GRASS_GREEN       = new Color( 032, 128, 032 );
    public final static Color BITTER_ORANGE     = new Color( 228, 168, 040 );
    public final static Color EARTH_BROWN       = new Color( 100,  48, 032 );
    public final static Color FREAKY_GREEN      = new Color( 128, 164, 032 );
    public final static Color DAAAARK_GREY      = new Color(  20,  20,  20 );
    public final static Color LIGHT_BLUE        = new Color(  90, 140, 220 );
    
    
    private PresentationModel pModel = null;
    
    private Color color = FREAKY_GREEN;
    private Color border = GRASS_GREEN;
    
    
    public Ground(PresentationModel model, int watermark)
    {
        super(watermark);
        
        pModel = model;
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
        border = color;
    }
    
    
    @Override
    public void render(Graphics2D g)
    {
        int width = pModel.getCanvasWidth();
        int halfWidth = width / 2;
        int height = pModel.getCanvasHeight();
        int halfHeight = height / 2;
        
        Path2D.Double polygon = new Path2D.Double();
        
        polygon.moveTo((-pModel.getViewOffsetX() - halfWidth) / pModel.getZoom(), (pModel.getViewOffsetY() - halfHeight) / pModel.getZoom());
        
        for (int i = 0; i < width / pModel.getZoom(); i++)
        {
            int x = i - (int) ((pModel.getViewOffsetX() + halfWidth) / pModel.getZoom());
            
            polygon.lineTo(x, this.function( this.DOWNHILL, x));
        }
        
        polygon.lineTo((-pModel.getViewOffsetX() + halfWidth) / pModel.getZoom(), (pModel.getViewOffsetY() - halfHeight) / pModel.getZoom());
        polygon.lineTo((-pModel.getViewOffsetX() - halfWidth) / pModel.getZoom(), (pModel.getViewOffsetY() - halfHeight) / pModel.getZoom());
        
        g.setColor( color );
        g.fill(polygon);
        
        if (border != null)
        {
            g.setColor( border );
            g.draw(polygon);
        }
        
        
        // TODO - this should propably be cached if possible
//        Polygon polygon = new Polygon();
//        
//        for (int i = 0; i < width / pModel.getZoom(); i++)
//        {
//            int x = i - (int) (pModel.getViewOffsetX() / pModel.getZoom());
//            
//            polygon.addPoint(x, this.function( this.DOWNHILL, x) + this.watermark);
//        }
//        
//        polygon.addPoint((int) ((width - pModel.getViewOffsetX()) / pModel.getZoom()), pModel.getViewOffsetY());
//        polygon.addPoint((int) (-pModel.getViewOffsetX() / pModel.getZoom()), pModel.getViewOffsetY());
//        
//        g.setColor( color );
//        g.fillPolygon(polygon);
//        
//        if (border != null)
//        {
//            g.setColor( border );
//            g.drawPolygon(polygon);
//        }
    }
    
    
    @Override
    public Ground clone()
    {
        Ground newGround = new Ground(pModel, this.watermark);
        
        return newGround;
    }
}