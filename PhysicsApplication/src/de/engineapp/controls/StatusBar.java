package de.engineapp.controls;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import de.engine.math.Vector;
import de.engineapp.PresentationModel;

public class StatusBar extends JPanel implements MouseMotionListener
{
    private static final long serialVersionUID = 8107903887585331982L;
    
    
    private PresentationModel pModel;
    
    private JLabel lblCoordinates;
    
    
    public StatusBar(PresentationModel model)
    {
        pModel = model;
        
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createRaisedBevelBorder());
        
        model.addMouseMotionListenerToCanvas(this);
        
        this.setPreferredSize(new Dimension(0, 30));
        
        lblCoordinates = new JLabel("(0; 0) ");
        this.add(lblCoordinates, BorderLayout.LINE_END);
    }
    
    
    @Override
    public void mouseDragged(MouseEvent e) { }
    
    @Override
    public void mouseMoved(MouseEvent e)
    {
        Vector coordinates = pModel.toTransformedVector(e.getPoint());
        
        lblCoordinates.setText(String.format("(%s; %s) ", (int) coordinates.getX(), (int) coordinates.getY()));
    }
}