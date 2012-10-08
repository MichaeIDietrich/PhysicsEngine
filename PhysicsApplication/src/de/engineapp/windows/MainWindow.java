package de.engineapp.windows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;

import de.engine.math.Vector;
import de.engine.objects.Circle;
import de.engine.objects.Ground;
import de.engine.objects.ObjectProperties;
import de.engineapp.controls.Canvas;
import de.engineapp.controls.CommandHandler;


public class MainWindow extends JFrame
{
    private static final long serialVersionUID = -1405279482198323306L;
    
    private Canvas canvas;
    
    // stores the navigation offset (navigation by the use of the right mouse button)
    private Point viewPosition = new Point();
    
    
    private List<ObjectProperties> objects;
    private RenderingHints antialias = new RenderingHints( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    
    public MainWindow()
    {
        super("Physics Engine");
        
        
        // Free objects (if necessary) before this application ends
        this.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
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
                    System.out.println("right mouse button pressed");
                    
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
                }
            }
        });
        
        this.setSize(600, 800);
        this.setLocationRelativeTo(null);
        
        initializeLookAndFeel();
        initializeComponents();
        
        objects = new ArrayList<>();
        
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
        
        JButton play  = new JButton(new ImageIcon("images/play.png"));
        JButton pause = new JButton(new ImageIcon("images/pause.png"));
        JButton reset = new JButton(new ImageIcon("images/reset.png"));
        
        pause.setEnabled(false);
        reset.setEnabled(false);
        
        play.setFocusable(false);
        pause.setFocusable(false);
        reset.setFocusable(false);
        
        toolBarMain.add(play);
        toolBarMain.add(pause);
        toolBarMain.add(reset);
        
        this.add(toolBarMain, BorderLayout.PAGE_START);
        
        
        // set up left toolbar, enabling drag'n'drop objects
        JToolBar toolBarObjects = new JToolBar(JToolBar.VERTICAL);
        toolBarObjects.setBorder( BorderFactory.createBevelBorder( BevelBorder.RAISED ) );
        toolBarObjects.setFloatable(false);
        
        JButton circle = new JButton(new ImageIcon("images/circle.png"));
        circle.setFocusable(false);
        circle.setTransferHandler(new CommandHandler("circle"));
        circle.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                JComponent comp = (JComponent) e.getSource();
                TransferHandler handler = comp.getTransferHandler();
                handler.setDragImage(new ImageIcon("images/circle.png").getImage());
                handler.setDragImageOffset(new Point(10, 10));
                handler.exportAsDrag(comp, e, TransferHandler.COPY);
            }
            
        });
        
        JButton square = new JButton(new ImageIcon("images/rect.png"));
        square.setFocusable(false);
        square.setTransferHandler(new CommandHandler("square"));
        square.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                JComponent comp = (JComponent) e.getSource();
                TransferHandler handler = comp.getTransferHandler();
                handler.setDragImage(new ImageIcon("images/rect.png").getImage());
                handler.setDragImageOffset(new Point(10, 10));
                handler.exportAsDrag(comp, e, TransferHandler.COPY);
            }
            
        });
        
        JButton ground = new JButton(new ImageIcon("images/ground.png"));
        ground.setFocusable(false);
        ground.setTransferHandler(new CommandHandler("ground"));
        ground.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                JComponent comp = (JComponent) e.getSource();
                TransferHandler handler = comp.getTransferHandler();
                handler.setDragImage(new ImageIcon("images/ground.png").getImage());
                handler.setDragImageOffset(new Point(10, 10));
                handler.exportAsDrag(comp, e, TransferHandler.COPY);
            }
            
        });
        
        toolBarObjects.add(circle);
        toolBarObjects.add(square);
        toolBarObjects.add(ground);
        
        this.add(toolBarObjects, BorderLayout.LINE_START);
        
        
        // set up canvas
        canvas = new Canvas(new Canvas.DropCallback()
        {
            // this method is necessary to recognize object drops from the left toolbar
            @Override
            public void drop(String command, Point location)
            {
                switch (command)
                {
                    case "circle":
                        System.out.println("Dropped a Circle at " + location);
                        
                        de.engine.math.Point position = new de.engine.math.Point();
                        position.x = location.x - 10 - viewPosition.x; position.y = location.y - 10 - viewPosition.y;
                        objects.add(new Circle(new Vector(position), 8));
                        
                        drawObjects();
                        break;
                        
                    case "ground":
                        System.out.println("Add ground at " + location.y);
                        
                        ObjectProperties ground = null;
                        
                        for (ObjectProperties obj : objects)
                        {
                            if (obj instanceof Ground)
                            {
                                ground = obj;
                                break;
                            }
                        }
                        objects.remove(ground);
                        objects.add(new Ground(location.y));
                        
                        drawObjects();
                        break;
                }
            }
        }, new Canvas.RepaintCallback()
        {
            @Override
            public void repaint()
            {
                drawObjects();
            }
        });
        
        canvas.setBackground(Color.WHITE);
        canvas.setBorder( BorderFactory.createBevelBorder( BevelBorder.LOWERED ) );
        
        
        this.add(canvas);
    }
    
    
    // TODO - adjust coordinates (negate y + translation)
    // TODO - improve this method at all
    private void drawObjects()
    {
        // HINT - always the getGraphics()-Method is called,
        // the background buffer will be cleared automatically
        Graphics2D g = canvas.getGraphics();
        
        g.addRenderingHints( antialias );
        
        g.translate(viewPosition.x, viewPosition.y);
        
        for (ObjectProperties obj : objects)
        {
            if (obj instanceof Circle)
            {
                g.setColor( Color.RED );
                g.fillOval( (int) obj.position.getPoint().x, (int) obj.position.getPoint().y, 
                        (int) obj.getRadius() * 2, (int) obj.getRadius() * 2);
            }
            if (obj instanceof Ground)
            {
                
                Ground ground = (Ground) obj;
                
                
                // TODO - this should maybe cached if possible
                Polygon polygon = new Polygon();
                
                for (int i = 0; i < canvas.getWidth(); i++)
                {
//                    g.setColor(Color.GRAY);
//                    g.drawLine(i, function(i), i, function(i));
//                    
//                    g.setColor(Color.ORANGE);
//                    g.drawLine(i, function(i) + 1, i, canvas.getHeight());
                    
                    polygon.addPoint(i, function(i) + ground.watermark);
                }
                
                polygon.addPoint(canvas.getWidth(), canvas.getHeight());
                polygon.addPoint(0, canvas.getHeight());
                
                g.setColor(Color.ORANGE);
                g.fillPolygon(polygon);
                g.setColor(Color.GRAY);
                g.drawPolygon(polygon);
            }
        }
        
        canvas.repaint();
    }
    
    
    // TODO - will be replaced by something dynamic later
    private int function(int i) 
    {
        // if positive, a hill will drawn; if negative the hill will be a valley
        int phase = -1;
        // what height are you going to go?
        int hill_height = 100;
        
        return (int) (Math.sin(phase * i * Math.PI / 300) * hill_height); // + canvas.getHeight() - 200);
    }
}