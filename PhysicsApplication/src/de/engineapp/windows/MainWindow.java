package de.engineapp.windows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class MainWindow extends JFrame
{
    
    private JPanel canvas;
    
    public MainWindow()
    {
        super("Physics Engine");
        
        
        // Free objects (if necessary) bevor this application ends
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
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.add(new JButton("Play"));
        toolBar.add(new JButton("Pause"));
        
        
        this.add(toolBar, BorderLayout.PAGE_START);
        
        canvas = new JPanel();
        canvas.setBackground(Color.BLACK);
        this.add(canvas);
    }
}
