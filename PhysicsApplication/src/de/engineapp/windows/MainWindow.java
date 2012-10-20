package de.engineapp.windows;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import de.engine.environment.Scene;
import de.engine.math.PhysicsEngine2D;
import de.engine.math.Util;
import de.engine.math.Vector;
import de.engine.objects.ObjectProperties;
import de.engineapp.Configuration;
import de.engineapp.Physics;
import de.engineapp.PresentationModel;
import de.engineapp.PresentationModel.PaintListener;
import de.engineapp.controls.Canvas;
import de.engineapp.controls.MainToolBar;
import de.engineapp.controls.ObjectToolBar;
import de.engineapp.controls.PropertiesPanel;
import de.engineapp.controls.dnd.DragAndDropController;
import de.engineapp.visual.Circle;
import de.engineapp.visual.Grid;
import de.engineapp.visual.Ground;
import de.engineapp.visual.IObject;


public class MainWindow extends JFrame implements PaintListener
{
    private static final long serialVersionUID = -1405279482198323306L;
    
    
//    private Configuration config = Configuration.getInstance();
    
    private final static RenderingHints ANTIALIAS = new RenderingHints( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
     
    private PresentationModel pModel = null;
    
    private Canvas canvas;
    
    
    private int point_1_x = 0;
    private int point_1_y = 0;
    
    private int point_2_x = Integer.MAX_VALUE;
    private int point_2_y = Integer.MAX_VALUE;
    
    
    public MainWindow()
    {
        super("Physics Engine");
        
        pModel = new PresentationModel();
        
        pModel.addPaintListener(this);
        
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
                
                renderScene();
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
        DragAndDropController dndController = new DragAndDropController(canvas, new DragAndDropController.DropCallback()
        {
            // this method is necessary to recognize object drops from the left toolbar
            @Override
            public void drop(String command, Point location)
            {
                // transformed location
                Vector vector = pModel.toTransformedVector(location);
                
                switch (command)
                {
                    case "circle":
                        InfoWindow.setData( InfoWindow.ACTION, "Kreis erstellt ["+ location.x +", "+ location.y +"]" );
                        
                        Circle circle = new Circle(pModel, vector, 8);
                        circle.mass = 10;
                        circle.velocity.setPoint( 0, 0 );
                        
                        pModel.addObject( circle );
                        
                        clearPointingVector();
                        
                        renderScene();
                        break;
                        
                    case "ground":
                        InfoWindow.setData( InfoWindow.ACTION, "Boden erstellt ["+ location.x +", "+ location.y +"]" );
                        
                        pModel.setGround(new Ground(pModel, (int) vector.getY()));
                        
                        renderScene();
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
        
        // declare the origin
        g.translate(canvas.getWidth() / 2, canvas.getHeight() / 2);
        // translate the scene to the point you have navigated to before
        g.translate(pModel.getViewOffsetX(), pModel.getViewOffsetY());
        // zoom into the scene
        g.scale(pModel.getZoom(), -pModel.getZoom());
        g.scale(PresentationModel.RATIO, PresentationModel.RATIO);
        
        // scale the stroke to ensure its contour has a one pixel width
        g.setStroke(new BasicStroke(1 / (float) pModel.getZoom()));
        
        if (pModel.isState("grid"))
        {
            // grid will be global later and not only around the origin
//            g.setColor(Color.BLACK);
//            for (int i = -15; i < 16; i++)
//            {
//                g.drawLine(i * 50, -800, i * 50, 800);
//                g.drawLine(-800, i * 50, 800, i * 50);
//            }
//            g.setStroke(new BasicStroke(3 / (float) pModel.getZoom()));
//            g.drawLine(0, -800, 0, 800);
//            g.drawLine(-800, 0, 800, 0);
//            g.setStroke(new BasicStroke(1 / (float) pModel.getZoom()));
            
            (new Grid(pModel)).render(g);
        }
        
        
        if (pModel.getScene().getGround() != null)
        {
            ((IObject) pModel.getScene().getGround()).render(g);
        }
        
        
        for (ObjectProperties obj : pModel.getScene().getObjects())
        {
            if (obj instanceof IObject)
            {
                ((IObject) obj).render(g);
            }
            else
            {
                System.err.println("Cannot render object: " + obj);
            }
        }
        
        
        if ( pModel.getSelectedObject()!=null && (point_2_x != Integer.MAX_VALUE) && (point_2_y != Integer.MAX_VALUE))
        {
            Vector vec = pModel.getSelectedObject().world_position.translation;
            
            // Begins drawing the force-arrow
            Vector from = new Vector( vec.getX(), vec.getY() );
            Vector   to = new Vector( vec.getX()+pModel.getSelectedObject().velocity.getX(), vec.getY()+pModel.getSelectedObject().velocity.getY() );
//            Vector   to = new Vector( point_2_x, point_2_y );
            
            System.out.println( pModel.getSelectedObject().velocity.getX() );
            
            int arrowlength    = (int) Util.distance( from, to );
            Polygon poly_arrow = createArrowPolygon( arrowlength );
            
            g.setColor( new Color( 180, 120, 20) );
            g.fillPolygon( polyTransform( poly_arrow, from, to ));
            g.setColor( Color.DARK_GRAY );
            g.drawPolygon( polyTransform( poly_arrow, from, to ));
        }
        
        
        // DEBUG
        g.scale(1 / pModel.getZoom(), 1 / -pModel.getZoom());
        g.scale(1 / PresentationModel.RATIO, 1 / PresentationModel.RATIO);
        g.setStroke(new BasicStroke(1));
        g.setColor(Color.BLUE);
        g.drawRect(-pModel.getViewOffsetX() - pModel.getCanvasWidth() / 2, -pModel.getViewOffsetY() - pModel.getCanvasHeight() / 2, 
                pModel.getCanvasWidth(), pModel.getCanvasHeight());
        g.drawString("80% Sichtgrenzen - noch leicht buggy", -pModel.getViewOffsetX() - pModel.getCanvasWidth() / 2, 
                -pModel.getViewOffsetY() - pModel.getCanvasHeight() / 2 - 3);
        
        
        canvas.repaint();
        
        InfoWindow.setData( InfoWindow.TIMEFORDRAWING, ""+(System.currentTimeMillis() - t) );
    }
    
    
    // Defines the look of the polygon arrow
    private Polygon createArrowPolygon( int arrow_length)
    {
        int arrowthickness = 2;
        
        Polygon poly_arrow = new Polygon();
        
        poly_arrow.addPoint(0,0);
        poly_arrow.addPoint(0,arrowthickness);
        poly_arrow.addPoint(arrow_length-20,    arrowthickness);
        poly_arrow.addPoint(arrow_length-20,  3*arrowthickness);
        poly_arrow.addPoint(arrow_length,     0);
        poly_arrow.addPoint(arrow_length-20, -3*arrowthickness);
        poly_arrow.addPoint(arrow_length-20,   -arrowthickness);
        poly_arrow.addPoint(0,                 -arrowthickness);
        
        return poly_arrow;
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
    
    
    @Override
    public void repaintCanvas()
    {
        renderScene();
    }
}