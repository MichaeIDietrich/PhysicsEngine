package de.engineapp;

import static de.engineapp.Constants.STG_GRID;

import java.awt.*;

import de.engine.DebugMonitor;
import de.engine.environment.Scene;
import de.engine.objects.ObjectProperties;
import de.engineapp.PresentationModel.*;
import de.engineapp.controls.Canvas;
import de.engineapp.util.AsyncWorker;
import de.engineapp.visual.*;


/**
 * This class is the connection between the scene object and the visual
 * representation of these objects within the canvas component.
 * 
 * @author Micha
 */
public final class Renderer extends AsyncWorker implements PaintListener, StorageListener
{
    private final static RenderingHints ANTIALIAS = new RenderingHints( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    
    
    private boolean singleThreaded;
    
    private Canvas canvas;
    private PresentationModel pModel;
    
    private RenderingSorter orderedObjects;
    
    private Scene currentScene = null;
    private Scene cachedScene = null;
    
    private Grid grid = null;
    
    
    public Renderer(PresentationModel model, Canvas canvas)
    {
        pModel = model;
        this.canvas = canvas;
        
        System.out.print("Available Processors: " + Runtime.getRuntime().availableProcessors());
        if (Runtime.getRuntime().availableProcessors() == 1)
        {
            System.out.println(" => calculation and rendering will be single threaded");
            singleThreaded = true;
        }
        else
        {
            System.out.println(" => rendering will be threaded");
            singleThreaded = false;
        }
        
        if (pModel.isState(STG_GRID))
        {
            grid = new Grid(pModel);
        }
        
        model.addPaintListener(this);
        model.addStorageListener(this);
        
        orderedObjects = new RenderingSorter();
    }
    
    
    @Override
    public void start()
    {
        worker = new Thread()
        {
            @Override
            public void run()
            {
                while (!this.isInterrupted() && currentScene != null)
                {
                    renderScene(currentScene);
                    
                    currentScene = cachedScene;
                    cachedScene = null;
                }
            }
        };
        
        if (singleThreaded)
        {
            worker.run();
        }
        else
        {
            worker.start();
        }
    }
    
    
    public void pushScene(Scene scene)
    {
        currentScene = scene;
        
//        if (currentScene == null)
//        {
//            currentScene = scene.clone();
//        }
//        else
//        {
//            cachedScene = scene.clone();
//        }
        
        if (!this.isRunning())
        {
            start();
        }
    }
    
    
    private void renderScene(Scene scene)
    {
        long t = System.currentTimeMillis();
        
        // HINT - always the getGraphics()-Method is called,
        // the background buffer will be cleared automatically
        Graphics2D g = canvas.getGraphics();
        
        // enable anti aliasing
        g.addRenderingHints( ANTIALIAS );
        
        // define the origin
        g.translate(canvas.getWidth() / 2, canvas.getHeight() / 2);
        // translate the scene to the point you have navigated to before
        g.translate(pModel.getViewOffsetX(), pModel.getViewOffsetY());
        // zoom into the scene + invert y-axis
        g.scale(pModel.getZoom(), -pModel.getZoom());
        
        // scale the stroke to ensure its contour has a one pixel width
        g.setStroke(new BasicStroke(1 / (float) pModel.getZoom()));
        
        
        if (grid != null)
        {
            grid.render(g);
        }
        
        
        if (scene.getGround() != null)
        {
            if (scene.getGround() instanceof IDrawable)
            {
                ((IDrawable) scene.getGround()).render(g);
            }
            else
            {
                System.err.println("cannot render " + scene.getGround());
            }
        }
        
        orderedObjects.clear();
        
        for (IDrawable decor : canvas.getDecorSet())
        {
            orderedObjects.add(decor);
        }
        
        for (ObjectProperties obj : scene.getObjects())
        {
            if (obj instanceof IDrawable)
            {
                ((IDrawable) obj).render(g);
            }
            else
            {
                System.err.println("Cannot render object: " + obj);
            }
            
            if (obj instanceof IDecorable)
            {
                orderedObjects.addAll(((IDecorable) obj).getDecorSet());
            }
        }
        
        IDrawable decor;
        
        while (!orderedObjects.isEmpty())
        {
            decor = orderedObjects.poll();
            decor.render(g);
        }
        
        canvas.switchBuffers();
        canvas.repaint();
        
        
        DebugMonitor.getInstance().updateMessage("repaint", "" + (System.currentTimeMillis() - t));
    }
    
    
    @Override
    public void stateChanged(String id, boolean value)
    {
        System.out.println(id + ": " + value);
        switch (id)
        {
            case STG_GRID:
                if (value)
                {
                    grid = new Grid(pModel);
                }
                else
                {
                    grid = null;
                }
                break;
        }
    }
    
    @Override
    public void propertyChanged(String id, String value) { }
    
    
    @Override
    public void repaintCanvas()
    {
        renderScene(pModel.getScene());
    }
}