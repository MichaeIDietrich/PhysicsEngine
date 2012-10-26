package de.engineapp.windows;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import de.engine.environment.Scene;
import de.engine.math.PhysicsEngine2D;
import de.engine.math.Vector;
import de.engine.objects.ObjectProperties;
import de.engineapp.*;
import de.engineapp.PresentationModel.PaintListener;
import de.engineapp.PresentationModel.StateListener;
import de.engineapp.controls.Canvas;
import de.engineapp.controls.MainToolBar;
import de.engineapp.controls.ObjectToolBar;
import de.engineapp.controls.PropertiesPanel;
import de.engineapp.controls.dnd.DragAndDropController;
import de.engineapp.visual.*;


public class MainWindow extends JFrame implements PaintListener, StateListener
{
    private static final long serialVersionUID = -1405279482198323306L;
    
    
//    private Configuration config = Configuration.getInstance();
    
    private final static RenderingHints ANTIALIAS = new RenderingHints( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
     
    private PresentationModel pModel = null;
    
    private Canvas canvas;
    
    private Grid grid = null;
    
    
    public MainWindow()
    {
        super("Physics Engine");
        
        pModel = new PresentationModel();
        
        pModel.addPaintListener(this);
        pModel.addStateListener(this);
        
        // Free objects (if necessary) before this application ends
        this.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                pModel.getPhysicsState().pause();
                MainWindow.this.dispose();
                InfoWindow.dispose();
                Configuration.save();
            }
        });
        
        
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        
        
        pModel.setPhysicsEngine2D(new PhysicsEngine2D());
        pModel.setScene(new Scene());
        
        pModel.setPhysicsState(new Physics(pModel.getPhysicsEngine2D(), 1000L / 30L, new Physics.FinishedCallback()
        {
            
            @Override
            public void done()
            {
                ObjectProperties selObject = pModel.getSelectedObject();
                
                if (selObject != null)
                {
                    InfoWindow.setData(InfoWindow.VELOCITY, selObject.velocity.getX() + ", " + selObject.velocity.getY());
                    InfoWindow.setData(InfoWindow.POSITION, selObject.getPosition().getX() + ", " + selObject.getPosition().getY());
                    InfoWindow.refresh();
                }
                
                pModel.fireRepaintEvents(true);
            }
        }));
        
        
        initializeLookAndFeel();
        initializeComponents();
        
        
        this.setVisible(true);
        
        InfoWindow.attachToFrame(this);
    }
    
    
    private void initializeLookAndFeel()
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            System.out.println("Cannot apply Look And Feel!");
            e.printStackTrace();
        }
    }
    
    
    private void initializeComponents()
    {
        // set up upper toolbar
        MainToolBar toolBarMain = new MainToolBar(pModel);
        this.add(toolBarMain, BorderLayout.PAGE_START);
        
        
        // set up left toolbar, enabling drag'n'drop objects
        ObjectToolBar toolBarObjects = new ObjectToolBar(pModel);
        this.add(toolBarObjects, BorderLayout.LINE_START);
        
        
        // set up right panel
        PropertiesPanel panelProperties = new PropertiesPanel(pModel);
        this.add(panelProperties, BorderLayout.LINE_END);
        
        
        // set up canvas
        canvas = new Canvas(pModel);
        
        
        // this one is handling all the drag'n'drop stuff
        new DragAndDropController(canvas, new DragAndDropController.DropCallback()
        {
            // this method is necessary to recognize object drops from the left toolbar
            @Override
            public void drop(String command, Point location)
            {
                // transformed location
                Vector sceneLocation = pModel.toTransformedVector(location);
                
                switch (command)
                {
                    case "circle":
                        InfoWindow.setData( InfoWindow.ACTION, "Kreis erstellt [" + location.x + ", " + location.y + "]" );
                        
                        Circle circle = new Circle(pModel, sceneLocation, 8);
                        circle.mass = 10;
                        
                        pModel.addObject( circle );
                        
                        pModel.fireRepaintEvents();
                        break;
                        
                    case "square":
                        InfoWindow.setData( InfoWindow.ACTION, "Quadrat erstellt [" + location.x + ", " + location.y + "]" );
                        
                        Square square = new Square(pModel, sceneLocation, 8);
                        square.mass = 10;
                        
                        pModel.addObject( square );
                        
                        pModel.fireRepaintEvents();
                        break;
                        
                    case "ground":
                        InfoWindow.setData( InfoWindow.ACTION, "Boden erstellt [" + location.x + ", " + location.y + "]" );
                        
                        pModel.setGround(new Ground(pModel, (int) sceneLocation.getY()));
                        
                        pModel.fireRepaintEvents();
                        break;
                }
            }
        });
        
        
        canvas.setBackground(new Color(250, 250, 250, 255));
        
        
        this.add(canvas);
    }
    
    
    // TODO - improve this method at all
    private void renderScene()
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
        
        if (pModel.isState("grid"))
        {
            grid.render(g);
        }
        
        
        if (pModel.getScene().getGround() != null)
        {
            ((IDrawable) pModel.getScene().getGround()).render(g);
        }
        
        Collection<IDrawable> decorSet = new ArrayList<>();
        
        for (ObjectProperties obj : pModel.getScene().getObjects())
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
                decorSet.addAll(((IDecorable) obj).getDecorSet());
            }
        }
        
        for (IDrawable decor : decorSet)
        {
            decor.render(g);
        }
        
        
        canvas.repaint();
        
        InfoWindow.setData( InfoWindow.TIMEFORDRAWING, "" + (System.currentTimeMillis() - t) );
    }
    
    
    @Override
    public void repaintCanvas()
    {
        renderScene();
    }
    
    
    @Override
    public void stateChanged(String id, boolean value)
    {
        System.out.println(id + ": " + value);
        switch (id)
        {
            case "grid":
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
}