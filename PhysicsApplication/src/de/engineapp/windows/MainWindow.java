package de.engineapp.windows;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;

import de.engine.environment.Scene;
import de.engine.math.PhysicsEngine2D;
import de.engine.math.Vector;
import de.engine.objects.Circle;
import de.engine.objects.Ground;
import de.engine.objects.ObjectProperties;
import de.engineapp.Physics;
import de.engineapp.controls.Canvas;
import de.engineapp.controls.DragButton;
import de.engineapp.controls.dnd.DragAndDropController;


public class MainWindow extends JFrame
{
    private static final long serialVersionUID = -1405279482198323306L;
    
    private RenderingHints antialias = new RenderingHints( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
     
    private Canvas canvas;
    
    /** weather a grid is drawn on the canvas */
    private boolean showGrid = false;
    private boolean showInfo = false;
    
    /** do not change - not yet fully implemented */
    private double zoom = 1.0;
    
    /** stores the navigation offset (navigation by the use of the right mouse button) */
    private Point viewPosition = new Point();
    
    private PhysicsEngine2D physicsEngine2D = null;
    
    private Scene scene = null;
    
    private Physics workingThread = null;
    
    private MessageWindow msgwin;
    
    
    public MainWindow()
    {
        super("Physics Engine");
        
        setDefaultCloseOperation( EXIT_ON_CLOSE );
        
        // Free objects (if necessary) before this application ends
        this.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                workingThread.pause();
                MainWindow.this.dispose();
            }
        });
        
        
        // TODO - move this code to canvas
        // stores the mouse offset while dragging
        final Point mouseOffset = new Point();
        
        // implement mouse (motion) listener to make navigating throw the scene
        // and manipulating objects possible
        this.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                if (SwingUtilities.isRightMouseButton(e))
                {
                    MessageWindow.setData( MessageWindow.ACTION, "Rechte Maustaste gedr�ckt" );
                    
                    mouseOffset.x = e.getPoint().x;
                    mouseOffset.y = e.getPoint().y;
                }
            }
        });
        
        this.addMouseMotionListener(new MouseMotionAdapter()
        {
            @Override
            public void mouseDragged(MouseEvent e)
            {
                if (SwingUtilities.isRightMouseButton(e))
                {
                    viewPosition.translate(e.getX() - mouseOffset.x, e.getY() - mouseOffset.y);
                    mouseOffset.x = e.getPoint().x;
                    mouseOffset.y = e.getPoint().y;
                    
                    // refresh canvas
                    drawObjects();
                    
                    MessageWindow.setData( MessageWindow.COORDINATES, e.getX()+", "+ e.getY() );
                    MessageWindow.refresh();
                }
            }
            
            @Override
            public void mouseMoved(MouseEvent e)
            {
                MessageWindow.setData( MessageWindow.COORDINATES, e.getX()+", "+ e.getY());
                MessageWindow.refresh();
            }
        });
        
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        
        
        physicsEngine2D = new PhysicsEngine2D();
        scene = new Scene();
        physicsEngine2D.setScene( scene );
        
        workingThread = new Physics(physicsEngine2D, 1000L / 30L, new Physics.FinishedCallback()
        {
            
            @Override
            public void done()
            {
                // it seems canvas.repaint() doesn't work here
//              canvas.repaint();
              drawObjects();
            }
        });
        
        
        initializeLookAndFeel();
        initializeComponents();
        
        // open message window contains information
        msgwin = new MessageWindow( new Point(this.getLocation().x+this.getWidth(), this.getLocation().y) );
        
        this.addMouseListener( new MouseController(this) );
        
        this.setVisible(true);
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
        JToolBar toolBarMain = new JToolBar();
        toolBarMain.setBorder( BorderFactory.createBevelBorder( BevelBorder.RAISED ) );
        toolBarMain.setFloatable(false);
        
        final JButton play  = new JButton(new ImageIcon("images/play.png"));
        final JButton pause = new JButton(new ImageIcon("images/pause.png"));
        final JButton reset = new JButton(new ImageIcon("images/reset.png"));
        final JToggleButton grid = new JToggleButton(new ImageIcon("images/grid.png"));
        final JButton info  = new JButton(new ImageIcon("images/loupe.png"));
        
        pause.setEnabled(false);
        reset.setEnabled(false);
        
        play.setFocusable(false);
        pause.setFocusable(false);
        reset.setFocusable(false);
        grid.setFocusable(false);
        info.setFocusable(false);
        
        play.addActionListener( new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                play.setEnabled( false );
                pause.setEnabled( true );
                workingThread.start();
            }
        });
        
        pause.addActionListener( new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                pause.setEnabled( false );
                play.setEnabled(   true );
                workingThread.pause();
            }
        });
        
        grid.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                showGrid = !showGrid;
                grid.setSelected(showGrid);
                drawObjects();
            }
        });
        
        info.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                showInfo = !showInfo;
                info.setSelected(showInfo);
                MessageWindow.getInstance().showWindow(showInfo);
            }
        });
        
        toolBarMain.add(play);
        toolBarMain.add(pause);
        toolBarMain.add(reset);
        toolBarMain.addSeparator();
        toolBarMain.add(grid);
        toolBarMain.addSeparator();
        toolBarMain.add(info);
        
        this.add(toolBarMain, BorderLayout.PAGE_START);
        
        
        // set up left toolbar, enabling drag'n'drop objects
        JToolBar toolBarObjects = new JToolBar(JToolBar.VERTICAL);
        toolBarObjects.setBorder( BorderFactory.createBevelBorder( BevelBorder.RAISED ) );
        toolBarObjects.setFloatable(false);
        
        DragButton circle = new DragButton(new ImageIcon("images/circle.png"), "circle", true);
        DragButton square = new DragButton(new ImageIcon("images/square.png"), "square", true);
        DragButton ground = new DragButton(new ImageIcon("images/ground.png"), "ground", 
                new ImageIcon("images/ruler.png").getImage(), new Point(16, 14));
        
        toolBarObjects.add(circle);
        toolBarObjects.add(square);
        toolBarObjects.add(ground);
        
        this.add(toolBarObjects, BorderLayout.LINE_START);
        
        
        // set up canvas
        canvas = new Canvas(new Canvas.RepaintCallback()
        {
            @Override
            public void repaint()
            {
                drawObjects();
            }
        });
        
        
        // this one is handling all the drag'n'drop stuff
        DragAndDropController dndController = new DragAndDropController(canvas, new DragAndDropController.DropCallback()
        {
            // this method is necessary to recognize object drops from the left toolbar
            @Override
            public void drop(String command, Point location)
            {
                // translate location
                
                location = new Point(location.x - viewPosition.x,
                                    -location.y + viewPosition.y + canvas.getHeight());
                
                switch (command)
                {
                    case "circle":
                        MessageWindow.setData( MessageWindow.ACTION, "Kreis erstellt ["+ location.x +", "+ location.y +"]" );
                        
                        Circle circle = new Circle(new Vector(location.getX(), location.getY()), 8);
                        circle.mass = 10;
                        // is it necessary?
//                        circle.getPosition().setPoint( location.getX(), location.getY() );
                        circle.velocity.setPoint( 0, 0 );
                        
                        scene.add( circle );
                        
                        drawObjects();
                        break;
                        
                    case "ground":
                        MessageWindow.setData( MessageWindow.ACTION, "Boden erstellt ["+ location.x +", "+ location.y +"]" );
                        
                        scene.setGround(new Ground(location.y));
                        
                        drawObjects();
                        break;
                }
            }
        });
        
        dndController.setScene(scene);
        
        canvas.setBackground(Color.WHITE);
        canvas.setBorder( BorderFactory.createBevelBorder( BevelBorder.LOWERED ) );
        
        
        this.add(canvas);
    }
    
    
    // TODO - adjust coordinates (negate y + translation) - half work is done, some is left :p
    // TODO - improve this method at all
    private void drawObjects()
    {
        long t = System.currentTimeMillis();
        
        // HINT - always the getGraphics()-Method is called,
        // the background buffer will be cleared automatically
        Graphics2D g = canvas.getGraphics();
        
        g.addRenderingHints( antialias );
        
        // translate the scene to the point you have navigated to before
        g.translate(viewPosition.x, viewPosition.y);
        g.scale(zoom, -zoom);
        g.translate(0, -canvas.getHeight());
        
        
        if (showGrid)
        {
            // grid will be global later and not only around the origin
            g.setColor(Color.BLACK);
            for (int i = -15; i < 16; i++)
            {
                g.drawLine(i * 50, -800, i * 50, 800);
                g.drawLine(-800, i * 50, 800, i * 50);
            }
            g.setStroke(new BasicStroke(3));
            g.drawLine(0, -800, 0, 800);
            g.drawLine(-800, 0, 800, 0);
            g.setStroke(new BasicStroke(1));
        }
        
        
        if (scene.getGround() != null)
        {
            Ground ground = scene.getGround();
            
            // TODO - this should probably be cached if possible
            Polygon polygon = new Polygon();
            
            for (int i = 0; i < canvas.getWidth(); i++)
            {
                int x = i - viewPosition.x;
                
                polygon.addPoint(x, ground.function( ground.DOWNHILL, x) + scene.getGround().watermark);
            }
            
            polygon.addPoint(canvas.getWidth() - viewPosition.x, viewPosition.y);
            polygon.addPoint(-viewPosition.x, viewPosition.y);
            
            g.setColor( ground.coreColor );
            g.fillPolygon(polygon);
            g.setColor( ground.surfaceColor );
            g.drawPolygon(polygon);
        }
        
        for (ObjectProperties obj : scene.getObjects())
        {
            if (obj instanceof Circle)
            {
                int r = (int) obj.getRadius();
                
                g.setColor( Color.RED );
                g.fillOval( ((int) obj.getPosition().getX()) - r, ((int) obj.getPosition().getY()) - r, r * 2, r * 2);
            }
        }
        canvas.repaint();
        
        MessageWindow.setData( MessageWindow.TIMEFORDRAWING, ""+(System.currentTimeMillis() - t) );
    }
    
    
    
    // The information window shall be docked on the right corner outside of the main window
    public class MouseController implements MouseListener 
    {
        private JFrame frame = null;
        
        public MouseController( JFrame frame )
        {
            this.frame = frame;
        }
        

        @Override
        public void mouseClicked(MouseEvent e)
        {
            
        }

        @Override
        public void mouseEntered(MouseEvent e)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void mouseExited(MouseEvent e) {}

        @Override
        public void mousePressed(MouseEvent e)
        {
            msgwin.updateLocation( frame.getLocation().x+frame.getWidth(), frame.getLocation().y );  
        }

        @Override
        public void mouseReleased(MouseEvent e)
        {
            // TODO Auto-generated method stub
            
        }
    }
}
