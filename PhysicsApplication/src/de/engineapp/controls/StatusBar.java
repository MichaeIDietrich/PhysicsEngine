package de.engineapp.controls;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import de.engine.math.Vector;
import de.engineapp.PresentationModel;
import de.engineapp.PresentationModel.StorageListener;

import static de.engineapp.Constants.*;

public class StatusBar extends JPanel implements MouseMotionListener, StorageListener
{
    private static final long serialVersionUID = 8107903887585331982L;
    
    
    private PresentationModel pModel;
    
    private JLabel lblCalcTime;
    private JLabel lblRepaintTime;
    private JLabel lblFPS;
    private JLabel lblCoordinates;
    
    
    public StatusBar(PresentationModel model)
    {
        pModel = model;
        
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createRaisedBevelBorder());
        
        model.addMouseMotionListenerToCanvas(this);
        model.addStorageListener(this);
        
        this.setPreferredSize(new Dimension(0, 30));
        
        Box boxRight = Box.createHorizontalBox();
        boxRight.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        
        if (pModel.isState(DEBUG))
        {
            lblCalcTime = new JLabel(" ");
            lblCalcTime.setBorder(BorderFactory.createLoweredBevelBorder());
            
            lblRepaintTime = new JLabel(" ");
            lblRepaintTime.setBorder(BorderFactory.createLoweredBevelBorder());
            
            lblFPS = new JLabel(" 0 FPS ");
            lblFPS.setBorder(BorderFactory.createLoweredBevelBorder());
        }
        
        lblCoordinates = new JLabel(" (0; 0) ");
        lblCoordinates.setBorder(BorderFactory.createLoweredBevelBorder());
        
        
        if (pModel.isState(DEBUG))
        {
            boxRight.add(lblCalcTime);
            boxRight.add(lblRepaintTime);
            boxRight.add(lblFPS);
        }
        boxRight.add(lblCoordinates);
        
        this.add(boxRight, BorderLayout.LINE_END);
    }
    
    
    @Override
    public void mouseDragged(MouseEvent e)
    {
        Vector coordinates = pModel.toTransformedVector(e.getPoint());
        
        lblCoordinates.setText(String.format( "(%s; %s) ", (int) coordinates.getX(), (int) coordinates.getY()));
    }
    
    @Override
    public void mouseMoved(MouseEvent e)
    {
        Vector coordinates = pModel.toTransformedVector(e.getPoint());
        
        lblCoordinates.setText(String.format(" (%s; %s) ", (int) coordinates.getX(), (int) coordinates.getY()));
    }
    
    
    @Override
    public void stateChanged(String id, boolean value) { }
    
    
    @Override
    public void propertyChanged(String id, String value)
    {
        switch (id)
        {
            case FPS:
                lblFPS.setText(" " + value + " FPS ");
                break;
                
            case CALCULATE_TIME:
                if (lblCalcTime != null)
                {
                    lblCalcTime.setText(" calculation: " + value + "ms ");
                }
                break;
                
            case REPAINT_TIME:
                if (lblRepaintTime != null)
                {
                    lblRepaintTime.setText(" repaint: " + value + "ms ");
                }
                break;
        }
    }
}