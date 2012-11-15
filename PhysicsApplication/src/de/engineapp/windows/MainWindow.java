package de.engineapp.windows;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import de.engine.*;
import de.engine.environment.Scene;
import de.engineapp.*;
import de.engineapp.Renderer;
import de.engineapp.container.*;
import de.engineapp.controls.Canvas;
import de.engineapp.controls.dnd.DragAndDropController;
import de.engineapp.util.*;

import static de.engineapp.Constants.*;


public final class MainWindow extends JFrame
{
    private static final long serialVersionUID = -1405279482198323306L;
    
    private final static Localizer LOCALIZER = Localizer.getInstance();
    
     
    private PresentationModel pModel = null;
    
    private Canvas canvas;
    private Renderer renderer;
    
    
    public MainWindow()
    {
        super(LOCALIZER.getString("APP_NAME"));
        
        pModel = new PresentationModel();
        pModel.setProperty(MODE, CMD_PHYSICS_MODE);
        
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
                pModel.fireSceneUpdated();
                renderer.pushScene(pModel.getScene());
            }
        }));
        
        
        initializeLookAndFeel();
        initializeComponents();
        
        renderer = new Renderer(pModel, canvas);
        
        
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
        
        if (pModel.isState(MAXIMIZED))
        {
            this.setExtendedState(this.getExtendedState() | MAXIMIZED_BOTH);
        }
    }
}