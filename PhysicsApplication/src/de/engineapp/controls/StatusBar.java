package de.engineapp.controls;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import de.engine.environment.Scene;
import de.engine.math.Vector;
import de.engine.objects.*;
import de.engineapp.*;
import de.engineapp.PresentationModel.*;

import static de.engineapp.Constants.*;

public class StatusBar extends JPanel implements MouseMotionListener, StorageListener, SceneListener, ChangeListener
{
    private static final long serialVersionUID = 8107903887585331982L;
    
    
    private PresentationModel pModel;
    
    private JLabel lblCalcTime;
    private JLabel lblRepaintTime;
    private JLabel lblFPS;
    private JLabel lblCoordinates;
    
    
    // this control will represent all the recorded frames
    private JSlider frames;
    
    
    public StatusBar(PresentationModel model)
    {
        pModel = model;
        
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createRaisedBevelBorder());
        
        model.addMouseMotionListenerToCanvas(this);
        model.addStorageListener(this);
        model.addSceneListener(this);
        
        this.setPreferredSize(new Dimension(0, 30));
        
        Box boxRight = Box.createHorizontalBox();
        boxRight.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        
        if (pModel.isState(DEBUG))
        {
            lblCalcTime = new JLabel(" ");
            lblCalcTime.setBorder(BorderFactory.createLoweredSoftBevelBorder());
            
            lblRepaintTime = new JLabel(" ");
            lblRepaintTime.setBorder(BorderFactory.createLoweredSoftBevelBorder());
            
            lblFPS = new JLabel(" 0 FPS ");
            lblFPS.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        }
        
        lblCoordinates = new JLabel(" (0; 0) ");
        lblCoordinates.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        
        
        if (pModel.isState(DEBUG))
        {
            boxRight.add(lblCalcTime);
            boxRight.add(lblRepaintTime);
            boxRight.add(lblFPS);
        }
        boxRight.add(lblCoordinates);
        
        this.add(boxRight, BorderLayout.LINE_END);
        
        
        frames = new JSlider(1, 1);
        frames.setMinorTickSpacing(30);
        frames.setPaintTicks(true);
        frames.addChangeListener(this);
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
                if (lblFPS != null)
                {
                    lblFPS.setText(" " + value + " FPS ");
                }
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
                
            case MODE:
                if (value.equals(CMD_PHYSICS_MODE))
                {
                    this.remove(frames);
                    this.updateUI();
                }
                else if (value.equals(CMD_RECORDING_MODE))
                {
                    frames.setEnabled(false);
                    this.add(frames);
                    this.updateUI();
                }
                else if (value.equals(CMD_PLAYBACK_MODE))
                {
                    frames.setEnabled(true);
                    this.add(frames);
                    this.updateUI();
                }
        }
    }
    
    
    @Override
    public void objectAdded(ObjectProperties object) { }
    
    @Override
    public void objectRemoved(ObjectProperties object) { }
    
    @Override
    public void groundAdded(Ground ground) { }
    
    @Override
    public void groundRemoved(Ground ground) { }
    
    @Override
    public void objectSelected(ObjectProperties object) { }
    
    @Override
    public void objectDeselected(ObjectProperties object) { }
    
    @Override
    public void sceneUpdated(Scene scene)
    {
        if (pModel.getProperty(MODE).equals(CMD_RECORDING_MODE))
        {
            Recorder recorder = Recorder.getInstance();
            recorder.addFrame(scene);
            frames.setMaximum(recorder.getFrameCount());
            frames.setValue(recorder.getFrameCount());
        }
    }
    
    
    @Override
    public void stateChanged(ChangeEvent e)
    {
        if (pModel.getProperty(MODE).equals(CMD_PLAYBACK_MODE))
        {
            pModel.setScene(Recorder.getInstance().getFrame(frames.getValue() - 1));
            pModel.fireRepaintEvents();
        }
    }
}