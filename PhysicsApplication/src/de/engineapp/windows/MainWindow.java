package de.engineapp.windows;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.engine.environment.Scene;
import de.engine.math.PhysicsEngine2D;
import de.engine.math.Util;
import de.engine.math.Vector;
import de.engine.objects.Circle;
import de.engine.objects.Ground;
import de.engine.objects.ObjectProperties;
import de.engineapp.Configuration;
import de.engineapp.Physics;
import de.engineapp.controls.Canvas;
import de.engineapp.controls.DragButton;
import de.engineapp.controls.ZoomSlider;
import de.engineapp.controls.dnd.DragAndDropController;


public class MainWindow extends JFrame
{
    private static final long serialVersionUID = -1405279482198323306L;
    
    
    private Configuration config = Configuration.getInstance();
    
    private final static RenderingHints ANTIALIAS = new RenderingHints( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
     
    private Canvas canvas;
    
    
    /** stores the navigation offset (navigation by the use of the right mouse button) */
    private Point viewPosition = new Point();
    
    private Physics workingThread = null;
    
    private PhysicsEngine2D physicsEngine2D = null;
    private Scene scene = null;
    private ObjectProperties selectedObject = null;
    
    private MessageWindow msgwin;
    
    private int point_1_x = 0;
    private int point_1_y = 0;
    
    private int point_2_x = Integer.MAX_VALUE;
    private int point_2_y = Integer.MAX_VALUE;
    
    private KeyEvent keyEvent = null;
    
    Thread  thread = null;
    Threadski tski = null;
    
    public MainWindow()
    {
        super("Oizys Physics Engine");
        
        // Free objects (if necessary) before this application ends
        this.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                workingThread.pause();
                MainWindow.this.dispose();
                MessageWindow.getInstance().dispose();
                Configuration.save();
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
                if (selectedObject != null)
                {
                    MessageWindow.setData(MessageWindow.VELOCITY, selectedObject.velocity.getX()      + ", " + selectedObject.velocity.getY());
                    MessageWindow.setData(MessageWindow.POSITION, selectedObject.getPosition().getX() + ", " + selectedObject.getPosition().getY());
                    MessageWindow.refresh();
                }
                
                renderObjects();
            }
        });
        
        
        initializeLookAndFeel();
        initializeComponents();
        
        // open message window contains information
        msgwin = new MessageWindow( new Point(this.getLocation().x + this.getWidth(), this.getLocation().y) );
        
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
                config.setShowGrid(!config.isShowGrid());
                grid.setSelected(config.isShowGrid());
                renderObjects();
            }
        });
        
        info.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                config.setShowInfo(!config.isShowInfo());
                info.setSelected(config.isShowInfo());
                MessageWindow.getInstance().showWindow(config.isShowInfo());
            }
        });
        
        grid.setSelected( config.isShowGrid() );
        // disabled - will cause an exception
//        if (config.isShowInfo())
//        {
//            info.setSelected(true);
//            MessageWindow.getInstance().showWindow(true);
//        }
        
        
        final ZoomSlider slider = new ZoomSlider(config.getZoom());
        slider.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                config.setZoom(slider.getValue());
                renderObjects();
            }
        });
        
        
        toolBarMain.add(play);
        toolBarMain.add(pause);
        toolBarMain.add(reset);
        toolBarMain.addSeparator();
        toolBarMain.add(grid);
        toolBarMain.addSeparator();
        toolBarMain.add(info);
        toolBarMain.addSeparator();
        toolBarMain.add(slider);
        
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
        toolBarObjects.setBorder( BorderFactory.createBevelBorder( BevelBorder.RAISED ) );
        
        // set up right toolbar
        JToolBar toolBarProperties = new JToolBar(JToolBar.VERTICAL);
        toolBarProperties.setBorder( BorderFactory.createBevelBorder( BevelBorder.RAISED ) );
        toolBarProperties.setFloatable(false);

                JTextField massInput = new JTextField("Masse");
                JTextField surface   = new JTextField("Material");
                
                massInput.setSize(100,100);
                
                toolBarProperties.add(massInput);
                toolBarProperties.add(surface);
        
        this.add(toolBarProperties, BorderLayout.LINE_END);
        toolBarObjects.setBorder( BorderFactory.createBevelBorder( BevelBorder.RAISED ) );
        
        
        // set up canvas
        canvas = new Canvas(new Canvas.RepaintCallback()
        {
            @Override
            public void repaint()
            {
                renderObjects();
            }
        });
        
        
        // this one is handling all the drag'n'drop stuff
        DragAndDropController dndController = new DragAndDropController(canvas, new DragAndDropController.DropCallback()
        {
            // this method is necessary to recognize object drops from the left toolbar
            @Override
            public void drop(String command, Point location)
            {
                // transform location
                Vector vector = toTransformedVector(location);
                
                switch (command)
                {
                    case "circle":
                        MessageWindow.setData( MessageWindow.ACTION, "Kreis erstellt ["+ location.x +", "+ location.y +"]" );
                        
                        Circle circle = new Circle(vector, 8);
                        circle.mass = 10;
                        circle.velocity.setPoint( 0, 0 );
                        
                        scene.add( circle );
                        selectedObject = circle;
                        
                        renderObjects();
                        break;
                        
                    case "ground":
                        MessageWindow.setData( MessageWindow.ACTION, "Boden erstellt ["+ location.x +", "+ location.y +"]" );
                        
                        scene.setGround(new Ground((int) vector.getY()));
                        
                        renderObjects();
                        break;
                }
            }
        });
        
        
        // stores the mouse offset while dragging
        final Point mouseOffset = new Point();
        
        // implement mouse (motion) listener to make navigating throw the scene
        // and manipulating objects possible
        canvas.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (SwingUtilities.isLeftMouseButton(e))
                {
                    Vector v = toTransformedVector(e.getPoint());
                    
                    if (keyEvent==null) 
                    {
                        // choose an object if the mouse coordinates equals its coordinates
                        selectedObject = scene.getObjectFromPoint(v.getX(), v.getY());
                        MessageWindow.setData( MessageWindow.ACTION, "Auswahl: " + selectedObject );
                        MessageWindow.refresh();
                    }
                    
                    // remember first mouse click
                    point_1_x = (int) v.getX();
                    point_1_y = (int) v.getY();

                    // clean canvas from arrow polygon
                    renderObjects();
                }
            }
            
            
            public void mouseReleased(MouseEvent e) 
            {
                thread = null;
            }
            
            
            @Override
            public void mousePressed(MouseEvent e)
            {
                if (SwingUtilities.isLeftMouseButton(e))
                {
                    Vector v = toTransformedVector(e.getPoint());
                    
                    if (thread == null) {
                        tski   = new Threadski();
                        thread = new Thread( tski );
                        thread.start();
                    }

                    if (keyEvent==null) 
                    {
                        selectedObject = scene.getObjectFromPoint(v.getX(), v.getY());
                    }
                    
                    point_2_x = (int) v.getX();
                    point_2_y = (int) v.getY();
                    
                    renderObjects();
                }
                
                if (SwingUtilities.isRightMouseButton(e))
                {
                    MessageWindow.setData( MessageWindow.ACTION, "Rechte Maustaste gedrückt" );
                    MessageWindow.refresh();
                    
                    mouseOffset.x = e.getPoint().x;
                    mouseOffset.y = e.getPoint().y;
                }
            }
        });
        
        
        canvas.addMouseMotionListener(new MouseMotionAdapter()
        {
            @Override
            public void mouseDragged(MouseEvent e)
            {
                // Changes the length of the force-arrow
                if (SwingUtilities.isLeftMouseButton(e))
                {
                    Vector v = toTransformedVector(e.getPoint());

                    // Non keys are pressed
                    if (keyEvent==null && tski.isTrue()) 
                    {
                        if (selectedObject!=null) selectedObject.world_position.translation.setPoint( v.getX(), v.getY() );
                        MessageWindow.setData( MessageWindow.ACTION, "Auswahl: " + selectedObject );
                        MessageWindow.refresh(); 
                    }
                    
                    point_2_x = (int) v.getX();
                    point_2_y = (int) v.getY();
                    
                    renderObjects();
                }
                
                if (SwingUtilities.isRightMouseButton(e))
                {
                    viewPosition.translate(e.getX() - mouseOffset.x, e.getY() - mouseOffset.y);
                    mouseOffset.x = e.getPoint().x;
                    mouseOffset.y = e.getPoint().y;
                    
                    // refresh canvas
                    renderObjects();
                    
                    MessageWindow.setData( MessageWindow.COORDINATES, toTransformedVector(e.getPoint()) );
                    MessageWindow.refresh();
                }
            }
            
            @Override
            public void mouseMoved(MouseEvent e)
            {
                if (!canvas.hasFocus()) canvas.requestFocusInWindow();
                
                MessageWindow.setData( MessageWindow.COORDINATES, toTransformedVector(e.getPoint()) );
                MessageWindow.refresh();
            }
        });
        
        
        canvas.addKeyListener( new KeyListener() 
        {
            @Override
            public void keyPressed(KeyEvent event )
            {
                setKeyPressed( event );
            }

            @Override
            public void keyReleased(KeyEvent arg0)
            {
                setKeyPressed( null );
            }

            @Override
            public void keyTyped(KeyEvent arg0) {}
        });
        
        dndController.setScene(scene);
        
        canvas.setBackground(Color.WHITE);
        canvas.setBorder( BorderFactory.createBevelBorder( BevelBorder.LOWERED ) );

        this.add(canvas);
    }
    
    
    // TODO - adjust coordinates (negate y + translation) - half work is done, some is left :p
    // TODO - improve this method at all
    private void renderObjects()
    {
        long t = System.currentTimeMillis();
        
        // HINT - always the getGraphics()-Method is called,
        // the background buffer will be cleared automatically
        Graphics2D g = canvas.getGraphics();
        
        g.addRenderingHints( ANTIALIAS );
        
        // translate the scene to the point you have navigated to before
        g.translate(viewPosition.x, viewPosition.y);
        g.scale(config.getZoom(), -config.getZoom());
        g.translate(0, -canvas.getHeight());
        
        
        if (config.isShowGrid())
        {
            // grid will be global later and not only around the origin
            g.setStroke(new BasicStroke(1 / (float) config.getZoom()));
            g.setColor(Color.LIGHT_GRAY);
            for (int i = -15; i < 16; i++)
            {
                g.drawLine(i * 50, -800, i * 50, 800);
                g.drawLine(-800, i * 50, 800, i * 50);
            }
            g.setColor(Color.DARK_GRAY);
            g.setStroke(new BasicStroke(3 / (float) config.getZoom()));
            g.drawLine(0, -800, 0, 800);
            g.drawLine(-800, 0, 800, 0);
            g.setStroke(new BasicStroke(1 / (float) config.getZoom()));
        }
        
        
        if (scene.getGround() != null)
        {
            Ground ground = scene.getGround();
            
            // TODO - this should probably be cached if possible
            Polygon polygon = new Polygon();
            
            for (int i = 0; i < canvas.getWidth() / config.getZoom(); i++)
            {
                int x = i - (int) (viewPosition.x / config.getZoom());
                
                polygon.addPoint(x, ground.function( ground.DOWNHILL, x) + scene.getGround().watermark);
            }
            
            polygon.addPoint((int) ((canvas.getWidth() - viewPosition.x) / config.getZoom()), viewPosition.y);
            polygon.addPoint((int) (-viewPosition.x / config.getZoom()), viewPosition.y);
            
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
        
        
        // Only draw if an object has been chosen
        if ( selectedObject!=null )
        {
            Vector vec = selectedObject.world_position.translation;

            // Begins drawing the resulting velocity arrow vector
            Vector from = new Vector( vec.getX(), vec.getY() );
            Vector to;
            
            if ((keyEvent!=null) && keyEvent.getKeyCode() == KeyEvent.VK_CONTROL && point_2_x != Integer.MAX_VALUE )
            {
                to  = new Vector( point_2_x, point_2_y );
                Vector vel = new Vector( point_2_x-vec.getX(), point_2_y-vec.getY()); 
                selectedObject.velocity.setPoint( vel.getX(), vel.getY());
                
                clearPointingVector();
                
                MessageWindow.setData( MessageWindow.NEWVELODIR, vel );
                MessageWindow.refresh();
            } else {
                to = new Vector( vec.getX()+selectedObject.velocity.getX(), vec.getY()+selectedObject.velocity.getY() );
            } 

            int arrowlength  = (int) Util.distance( from, to );
            Shape poly_arrow = createArrowShape( arrowlength, from, to );
            
            g.setColor( new Color( 180, 120, 20) );
            g.fill( poly_arrow );
            g.setColor( Color.DARK_GRAY );
            g.draw( poly_arrow );
        }
        
        canvas.repaint();
        
        MessageWindow.setData( MessageWindow.DRAWINGTIME, ""+(System.currentTimeMillis() - t) );
    }
    
    
    // this method will transform a local cursor position on the canvas
    // to the internal Physics Engine coordinates
    private Vector toTransformedVector(Point point)
    {
        return new Vector(
                 (point.x - viewPosition.x) / config.getZoom(),
                (-point.y + viewPosition.y) / config.getZoom() + canvas.getHeight()
        );
    }
    
    
    // Defines the look of the polygon arrow
    private Shape createArrowShape( int arrow_length, Vector from,  Vector to )
    {
        if (arrow_length<=6)
        {
            return new Ellipse2D.Float( (float) from.getX()-5f, (float) from.getY()-5f , 10f, 10f );
        } 
            else 
        { 
            Polygon poly_arrow = null;
            
            int max_width  = 6;
            int arrow_peak = 2*max_width;
            
            if (arrow_length<=arrow_peak) arrow_length=arrow_peak;
            
            poly_arrow = new Polygon();
            
            poly_arrow.addPoint(0,0);
            poly_arrow.addPoint(0,                        (int) (max_width/2.5d));
            poly_arrow.addPoint(arrow_length-arrow_peak,  (int) (max_width/2.5d));
            poly_arrow.addPoint(arrow_length-arrow_peak,  (int) (max_width));
            poly_arrow.addPoint(arrow_length,     0);
            poly_arrow.addPoint(arrow_length-arrow_peak,  (int) (-max_width));
            poly_arrow.addPoint(arrow_length-arrow_peak,  (int) (-max_width/2.5d));
            poly_arrow.addPoint(0,                        (int) (-max_width/2.5d));
            
            return polyTransform( poly_arrow, from, to );
        }
    }
    
    
    /**
     * Rotates a polygon by the angle determining the vectors 'from' and 'to'. <br>
     * 
     * @param polygon The polygon which will be transformed.
     * @param from 
     * @param to
     * @return
     */
    private Polygon polyTransform( Polygon polygon, Vector from, Vector to )
    {
        Polygon tmp_polygon = new Polygon();
        double     rotation = Util.getAngle( from, to );
        
        for (int i=0; i<polygon.npoints; i++)
        {
           tmp_polygon.addPoint(
                   (int) (from.getX() + polygon.xpoints[i] * Math.cos(rotation) - (polygon.ypoints[i]) * Math.sin(rotation)),
                   (int) (from.getY() + polygon.xpoints[i] * Math.sin(rotation) + (polygon.ypoints[i]) * Math.cos(rotation)));            
        }
        return tmp_polygon;
    }
    
    
    /**
     * Draws an arrow vector polygon which scales automatically depending on <br>
     * the distance of the vectors 'from' and 'to'.                          <br>
     * 
     * @param Vector from. Where the arrow starts from.
     * @param Vector to. The pointing vector.
     * @return A arrow polygon. 
     */
    private Polygon graphVelocityVector( Vector from, Vector to )
    {
        double direction = Util.getAngle( from, to );
        int       length = (int) Util.distance( from, to );
        double[]  rscale = {0.18, 0.53, 0.6, 1d, 0.6, 0.53, 0.18};
        double[]   angle = {90d, 20d, 35d, 0d, 325d, 340d, 270d};
        
        Polygon polygon = new Polygon();
        
        polygon.addPoint( (int)from.getX(), (int)from.getY() );
        for(int i=0; i<7; i++)
        {
            polygon.addPoint( 
                    (int) (from.getX() + (rscale[i]*length)*Math.cos( direction + Math.toRadians( angle[i] )) ), 
                    (int) (from.getY() + (rscale[i]*length)*Math.sin( direction + Math.toRadians( angle[i] )) ) 
            );
        }
        
        return polygon;
    }
    
    
    // Set the pointing destination of the arrow to unpossible
    public void clearPointingVector()
    {
        this.point_2_x = Integer.MAX_VALUE;
        this.point_2_y = Integer.MAX_VALUE;
    }
    
    
    public void setKeyPressed( KeyEvent event )
    {
        this.keyEvent = event;
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
        public void mousePressed(MouseEvent e)
        {
            msgwin.updateLocation( frame.getLocation().x+frame.getWidth(), frame.getLocation().y );  
        }
        
        @Override
        public void mouseClicked(MouseEvent e) {}

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}

        @Override
        public void mouseReleased(MouseEvent e) {}
    }
    
    
    public class Threadski implements Runnable 
    {
        private Boolean bool = false;
        
        public Boolean isTrue()
        {
            return bool;
        }
        
        public void setFalse()
        {
            bool = false;
        }
        
        @Override
        public void run()
        {
            try
            {
                Thread.sleep(200);
                this.bool = true;
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
