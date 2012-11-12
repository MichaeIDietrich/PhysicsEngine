package de.engineapp.windows;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import de.engine.environment.Scene;
import de.engine.math.PhysicsEngine2D;
import de.engine.objects.ObjectProperties;
import de.engineapp.*;
import de.engineapp.PresentationModel.PaintListener;
import de.engineapp.PresentationModel.StorageListener;
import de.engineapp.controls.*;
import de.engineapp.controls.Canvas;
import de.engineapp.controls.dnd.DragAndDropController;
import de.engineapp.visual.*;

import static de.engineapp.Constants.*;


public class MainWindow extends JFrame implements PaintListener, StorageListener
{
    private static final long serialVersionUID = -1405279482198323306L;
    
    
//    private Configuration config = Configuration.getInstance();
    private final static Localizer LOCALIZER = Localizer.getInstance();
    
    private final static RenderingHints ANTIALIAS = new RenderingHints( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
     
    private PresentationModel pModel = null;
    
    private Canvas canvas;
    
    private Grid grid = null;
    
    
    public MainWindow()
    {
        super(LOCALIZER.getString("APP_NAME"));
        
        pModel = new PresentationModel();
        pModel.setProperty(MODE, CMD_PHYSICS_MODE);
        
        pModel.addPaintListener(this);
        pModel.addStorageListener(this);
        
        // Free objects (if necessary) before this application ends
        this.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                pModel.getPhysicsState().pause();
                MainWindow.this.dispose();
                Configuration.save();
            }
        });
        this.addWindowStateListener(new WindowStateListener()
        {
            @Override
            public void windowStateChanged(WindowEvent e)
            {
                if ((MainWindow.this.getExtendedState() & MAXIMIZED_BOTH) != 0)
                {
                    pModel.setState(MAXIMIZED, true);
                }
                else
                {
                    pModel.setState(MAXIMIZED, false);
                }
            }
        });
        
        
        initializeWindow();
        
        
        pModel.setPhysicsEngine2D(new PhysicsEngine2D());
        pModel.setScene(new Scene());
        
        pModel.setPhysicsState(new Physics(pModel, 1000L / 30L, new Physics.FinishedCallback()
        {
            
            @Override
            public void done()
            {
                pModel.fireSceneUpdateEvents();
                pModel.fireRepaintEvents(true);
            }
        }));
        
        
        initializeLookAndFeel();
        initializeComponents();
        
        
        this.setVisible(true);
    }
    
    
    private void initializeLookAndFeel()
    {
        try
        {
            if (Configuration.getInstance().getProperty(LOOK_AND_FEEL) != null)
            {
                for (int i = 0; i < UIManager.getInstalledLookAndFeels().length; i++)
                {
                    if (Configuration.getInstance().getProperty(LOOK_AND_FEEL).equals(
                            UIManager.getInstalledLookAndFeels()[i].getName()))
                    {
                        UIManager.setLookAndFeel(UIManager.getInstalledLookAndFeels()[i].getClassName());
                        return;
                    }
                }
            }
            
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            for (int i = 0; i < UIManager.getInstalledLookAndFeels().length; i++)
            {
                if (UIManager.getSystemLookAndFeelClassName().equals(
                        UIManager.getInstalledLookAndFeels()[i].getClassName()))
                {
                    Configuration.getInstance().setProperty(LOOK_AND_FEEL, 
                            UIManager.getInstalledLookAndFeels()[i].getName());
                    return;
                }
            }
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            System.out.println("Cannot apply Look And Feel!");
            e.printStackTrace();
        }
    }
    
    
    private void initializeComponents()
    {
        // initiate controls
        canvas = new Canvas(pModel);
        MainToolBar toolBarMain = new MainToolBar(pModel);
        StatusBar statusBar = new StatusBar(pModel);
        ObjectToolBar toolBarObjects = new ObjectToolBar(pModel);
        PropertiesPanel panelProperties = new PropertiesPanel(pModel);
        
        
        // set up upper toolbar
        this.add(toolBarMain, BorderLayout.PAGE_START);
        
        
        // set up statusbar
        this.add(statusBar, BorderLayout.PAGE_END);
        
        
        // set up left toolbar, enabling drag'n'drop objects
        this.add(toolBarObjects, BorderLayout.LINE_START);
        
        
        // set up right panel
//        this.add(new JScrollPane(panelProperties), BorderLayout.LINE_END);
        this.add(panelProperties, BorderLayout.LINE_END);
        panelProperties.setVisible(false);
        
        
        // this one is handling all the drag'n'drop stuff
        new DragAndDropController(pModel, canvas);
        
        
        canvas.setBackground(new Color(250, 250, 250, 255));
        
        
        // set up canvas
        this.add(canvas);
    }
    
    
    private void initializeWindow()
    {
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        
        ArrayList<Image> iconList = new ArrayList<>();
        iconList.add(Util.getImage("main/256"));
        iconList.add(Util.getImage("main/128"));
        iconList.add(Util.getImage("main/64"));
        iconList.add(Util.getImage("main/48"));
        iconList.add(Util.getImage("main/32"));
        iconList.add(Util.getImage("main/24"));
        iconList.add(Util.getImage("main/16"));
        
        this.setIconImages(iconList);
        
        if (pModel.isState(GRID))
        {
            grid = new Grid(pModel);
        }
        if (pModel.isState(MAXIMIZED))
        {
            this.setExtendedState(this.getExtendedState() | MAXIMIZED_BOTH);
        }
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
        
        if (grid != null)
        {
            grid.render(g);
        }
        
        
        if (pModel.getScene().getGround() != null)
        {
            if (pModel.getScene().getGround() instanceof IDrawable)
            {
                ((IDrawable) pModel.getScene().getGround()).render(g);
            }
            else
            {
                System.err.println("cannot render " + pModel.getScene().getGround());
            }
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
        
        
        pModel.setProperty(REPAINT_TIME, "" + (System.currentTimeMillis() - t));
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
            case GRID:
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
}