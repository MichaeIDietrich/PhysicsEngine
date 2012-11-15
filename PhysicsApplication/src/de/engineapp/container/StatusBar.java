package de.engineapp.container;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.*;

import de.engine.*;
import de.engine.DebugMonitor.MessageListener;
import de.engine.environment.Scene;
import de.engine.math.Vector;
import de.engine.objects.*;
import de.engineapp.*;
import de.engineapp.PresentationModel.*;
import de.engineapp.controls.*;
import de.engineapp.rec.Recorder;
import de.engineapp.util.Util;

import static de.engineapp.Constants.*;

public class StatusBar extends JPanel implements MouseMotionListener, StorageListener, SceneListener, ChangeListener, MessageListener, ActionListener
{
    private static final long serialVersionUID = 8107903887585331982L;
    
    private static final Border STATUS_BORDER = BorderFactory.createCompoundBorder(
            BorderFactory.createLoweredSoftBevelBorder(), BorderFactory.createEmptyBorder(0, 5, 0, 5));
    
    
    private PresentationModel pModel;
    
    private Box boxMain;
    private Box boxRight;
    private QuickButton discard;
    private JLabel lblObjectCount;
    private JLabel lblCoordinates;
    
    private Map<String, JLabel> debugMessages = null;
    
    
    // this control will represent all the recorded frames
    private SliderEx frames;
    
    
    public StatusBar(PresentationModel model)
    {
        pModel = model;
        
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createRaisedBevelBorder());
        
        model.addMouseMotionListenerToCanvas(this);
        model.addStorageListener(this);
        model.addSceneListener(this);
        
        this.setPreferredSize(new Dimension(0, 30));
        
        boxMain = Box.createHorizontalBox();
        boxMain.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        
        boxRight = Box.createHorizontalBox();
        boxRight.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        
        if (pModel.isState(DEBUG))
        {
            debugMessages = new HashMap<>();
        }
        
        discard = new QuickButton(Util.getIcon("discard"), CMD_DISCARD, this);
        
        lblObjectCount = new JLabel("0 Objects");
        lblObjectCount.setBorder(STATUS_BORDER);
        
        lblCoordinates = new JLabel("(0; 0)");
        lblCoordinates.setBorder(STATUS_BORDER);
        
        boxRight.add(lblObjectCount);
        boxRight.add(lblCoordinates);
        
        this.add(boxMain);
        this.add(boxRight, BorderLayout.LINE_END);
        
        
        frames = new SliderEx(1, 1, 1);
        frames.setGroupSize(30);
//        frames = new JSlider(1, 1);
//        frames.setMinorTickSpacing(30);
//        frames.setPaintTicks(true);
        frames.addChangeListener(this);
        
        DebugMonitor.getInstance().addMessageListener(this);
        
//        SliderEx s = new SliderEx(1, 100, 1);
//        s.setGroupSize(30);
//        this.add(s);
    }
    
    
    @Override
    public void mouseDragged(MouseEvent e)
    {
        Vector coordinates = pModel.toTransformedVector(e.getPoint());
        
        lblCoordinates.setText(String.format("(%s; %s)", (int) coordinates.getX(), (int) coordinates.getY()));
    }
    
    @Override
    public void mouseMoved(MouseEvent e)
    {
        Vector coordinates = pModel.toTransformedVector(e.getPoint());
        
        lblCoordinates.setText(String.format("(%s; %s)", (int) coordinates.getX(), (int) coordinates.getY()));
    }
    
    
    @Override
    public void stateChanged(String id, boolean value)
    {
        if (id.equals(RUN_PHYSICS))
        {
            discard.setEnabled(!value);
            
            if (pModel.getProperty(MODE).equals(CMD_RECORDING_MODE))
            {
                frames.setBackground(value ? Color.RED : this.getBackground());
            }
        }
    }
    
    
    @Override
    public void propertyChanged(String id, String value)
    {
        switch (id)
        {
            case MODE:
                if (value.equals(CMD_PHYSICS_MODE))
                {
                    frames.setBackground(pModel.isState(RUN_PHYSICS) ? Color.RED : this.getBackground());
                    boxMain.remove(frames);
                    boxMain.remove(discard);
                    this.updateUI();
                }
                else if (value.equals(CMD_RECORDING_MODE))
                {
                    frames.setBackground(pModel.isState(RUN_PHYSICS) ? Color.RED : this.getBackground());
                    frames.setEnabled(false);
                    boxMain.add(frames);
                    boxMain.add(discard);
                    this.updateUI();
                }
                else if (value.equals(CMD_PLAYBACK_MODE))
                {
                    frames.setBackground(pModel.isState(RUN_PHYSICS) ? Color.RED : this.getBackground());
                    frames.setEnabled(true);
                    boxMain.add(frames);
                    boxMain.add(discard);
                    this.updateUI();
                }
                break;
                
            case CURRENT_PLAYBACK_FRAME:
                int number = Integer.parseInt(value);
                if (frames.getValue() != number)
                {
                    frames.setValue(number);
                }
                break;
        }
    }
    
    
    @Override
    public void objectAdded(ObjectProperties object)
    {
        lblObjectCount.setText(" " + pModel.getScene().getCount() + " Objects ");
    }
    
    @Override
    public void objectRemoved(ObjectProperties object)
    {
        lblObjectCount.setText(" " + pModel.getScene().getCount() + " Objects ");
    }
    
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
            pModel.setProperty(CURRENT_PLAYBACK_FRAME, "" + recorder.getFrameCount());
            frames.setMaximum(recorder.getFrameCount());
            frames.setValue(recorder.getFrameCount());
        }
    }
    
    
    @Override
    public void stateChanged(ChangeEvent e)
    {
        Recorder recorder = Recorder.getInstance();
        
        if (pModel.getProperty(MODE).equals(CMD_PLAYBACK_MODE) && recorder.getFrameCount() > 0)
        {
            pModel.setProperty(CURRENT_PLAYBACK_FRAME, "" + frames.getValue());
            pModel.setScene(recorder.getFrame(frames.getValue() - 1));
            pModel.fireRepaint();
        }
    }
    
    
    @Override
    public void messageUpdated(String name, String message)
    {
        if (debugMessages != null)
        {
            JLabel label = debugMessages.get(name);
            
            if (message == null)
            {
                if (label != null)
                {
                    boxRight.remove(label);
                    boxRight.updateUI();
                    debugMessages.remove(name);
                }
            }
            else
            {
                if (label == null)
                {
                    label = new JLabel();
                    label.setBorder(STATUS_BORDER);
                    boxRight.add(label, 0);
                    boxRight.updateUI();
                    debugMessages.put(name, label);
                }
                
                label.setText(name + ": " + message);
            }
        }
    }
    
    @Override
    public void objectUpdated(ObjectProperties object) { }
    
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Recorder.getInstance().clear();
        frames.setValue(1);
        frames.setMaximum(1);
    }
}