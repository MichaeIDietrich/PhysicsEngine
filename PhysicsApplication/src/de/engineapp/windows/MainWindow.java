package de.engineapp.windows;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.LineBorder;

import de.engineapp.controls.Canvas;
import de.engineapp.controls.CommandHandler;








public class MainWindow extends JFrame
{
    private static final long serialVersionUID = -1405279482198323306L;
    
    private Canvas canvas;
    
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
        
        this.setSize(600, 800);
        this.setLocationRelativeTo(null);
        
        initializeLookAndFeel();
        initializeComponents();
        
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
        toolBarMain.setFloatable(false);
        
        toolBarMain.add(new JButton(new ImageIcon("images/play.png")));
        toolBarMain.add(new JButton(new ImageIcon("images/pause.png")));
        toolBarMain.add(new JButton(new ImageIcon("images/reset.png")));
        
        this.add(toolBarMain, BorderLayout.PAGE_START);
        
        
        // set up left toolbar, enabling drag'n'drop objects
        JToolBar toolBarObjects = new JToolBar(JToolBar.VERTICAL);
        toolBarObjects.setFloatable(false);
        
        JButton button = new JButton(new ImageIcon("images/circle.png"));
        button.setTransferHandler(new CommandHandler("circle"));
        
        button.addMouseListener(new MouseAdapter()
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
        toolBarObjects.add(button);
        toolBarObjects.add(new JButton(new ImageIcon("images/rect.png")));
        
        this.add(toolBarObjects, BorderLayout.LINE_START);
        
        
        // set up canvas
        canvas = new Canvas(new Canvas.DropCallback()
        {
            // this method is necessary to recognize drops from the left toolbar
            @Override
            public void drop(String command)
            {
                switch (command)
                {
                    case "circle":
                        
                        break;
                }
            }
        });
        canvas.setBorder( LineBorder.createBlackLineBorder() );
        
        this.add(canvas);
    }
}
