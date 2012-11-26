package de.engineapp.visual;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

import de.engineapp.PresentationModel;


/**
 * Visual class for the rendering a grid within the scene.
 * 
 * @author Micha
 */
public final class Grid
{
    private static final int GAP_SIZE = 50;
    
    
    private PresentationModel pModel;
    
    
    public Grid(PresentationModel model)
    {
        pModel = model;
    }
    
    
    public void render(Graphics2D g)
    {
        // - store the active transformation
        // - use the stored tranformation to transform the cache image
        // - render the cached image without transformation
        // - restore the trasformation for the following rendering
        AffineTransform activeTransformation = g.getTransform();
        g.setTransform(AffineTransform.getTranslateInstance(0, 0));
        g.drawImage(getImage(activeTransformation), 0, 0, null);
        g.setTransform(activeTransformation);
    }
    
    
    /////////////////////////
   /////    caching    /////
  /////////////////////////
  
  private Image cachedImage = null;
  private double lastZoom = 0.0;
  private int lastOffsetX = 0;
  private int lastOffsetY = 0;
  private int lastCanvasWidth = 0;
  private int lastCanvasHeight = 0;
  
  
  private Image getImage(AffineTransform transformation)
  {
      if (cachedImage == null || lastZoom != pModel.getZoom() || lastOffsetX != pModel.getViewOffsetX() || lastOffsetY != pModel.getViewOffsetY()
              || lastCanvasWidth != pModel.getCanvasWidth() || lastCanvasHeight != pModel.getCanvasHeight())
      {
          cachedImage = createNewImage(transformation);
          lastZoom = pModel.getZoom();
          lastOffsetX = pModel.getViewOffsetX();
          lastOffsetY = pModel.getViewOffsetY();
          lastCanvasWidth = pModel.getCanvasWidth();
          lastCanvasHeight = pModel.getCanvasHeight();
      }
      
      return cachedImage;
  }
  
  private final static RenderingHints ANTIALIAS = new RenderingHints( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
  
  private Image createNewImage(AffineTransform transformation)
  {
      int width = pModel.getCanvasWidth();
      int height = pModel.getCanvasHeight();
      
      Stroke bigStroke, mediumStroke, smallStroke;
      
      if (pModel.getZoom() > 1.0)
      {
          bigStroke = new BasicStroke((float) (3.0 / pModel.getZoom()));
          mediumStroke = new BasicStroke((float) (1.8 / pModel.getZoom()));
          smallStroke = new BasicStroke((float) (1.0 / pModel.getZoom()));
      }
      else
      {
          bigStroke = new BasicStroke(3.0f);
          mediumStroke = new BasicStroke(1.8f);
          smallStroke = new BasicStroke(1.0f);
      }
      
      // could be switch to TYPE_INT_RGB from TYPE_INT_ARGB later
      BufferedImage buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
      Graphics2D g = (Graphics2D) buffer.getGraphics();
      
      // TODO - need to be fixed, must be the background color of the canvas
//      g.setBackground(Color.WHITE);
//      g.clearRect(0, 0, width, height);
      
      g.setRenderingHints(ANTIALIAS);
      g.setTransform(transformation);
      
      g.scale(1.0, 1.0);
      g.setColor(Color.BLACK);
      
      double minX = (-width / 2 - pModel.getViewOffsetX()) / pModel.getZoom();
      double maxX = ( width / 2 - pModel.getViewOffsetX()) / pModel.getZoom();
      
      int xFrom = (int) Math.ceil(minX / GAP_SIZE);
      int xTo   = (int) Math.floor(maxX / GAP_SIZE);
      
      double minY = (-height / 2 + pModel.getViewOffsetY()) / pModel.getZoom();
      double maxY = ( height / 2 + pModel.getViewOffsetY()) / pModel.getZoom();
      
      int yFrom = (int) Math.ceil(minY / GAP_SIZE);
      int yTo   = (int) Math.floor(maxY / GAP_SIZE);
      
      
      for (int i = xFrom; i <= xTo; i++)
      {
          if (i == 0)
          {
              g.setStroke(bigStroke);
          }
          else if (i % 5 == 0)
          {
              g.setStroke(mediumStroke);
          }
          else
          {
              g.setStroke(smallStroke);
          }
          
          g.drawLine(i * GAP_SIZE, (int) minY, i * GAP_SIZE, (int) maxY);
      }
      
      for (int i = yFrom; i <= yTo; i++)
      {
          if (i == 0)
          {
              g.setStroke(bigStroke);
          }
          else if (i % 5 == 0)
          {
              g.setStroke(mediumStroke);
          }
          else
          {
              g.setStroke(smallStroke);
          }
          
          g.drawLine((int) minX, i * GAP_SIZE, (int) maxX, i * GAP_SIZE);
      }
      
      return buffer;
  }
}