package de.engineapp.controls;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JToolBar;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.engineapp.PresentationModel;
import de.engineapp.PresentationModel.StateListener;
import de.engineapp.VUtil;
import de.engineapp.windows.InfoWindow;

public class MainToolBar extends JToolBar implements ActionListener, ChangeListener, StateListener
{
    private static final long serialVersionUID = 4164673212238397915L;
    
    
    private PresentationModel pModel;
    
    private ToolBarButton play;
    private ToolBarButton pause;
    private ToolBarButton reset;
    private ToolBarButton grid;
    private ToolBarButton info;
    
    private ZoomSlider slider;
    
    
    public MainToolBar(PresentationModel model)
    {
        pModel = model;
        
        this.setBorder( BorderFactory.createBevelBorder( BevelBorder.RAISED ) );
        this.setFloatable(false);
        
        play  = new ToolBarButton(VUtil.getIcon("play"),  "play",  this);
        pause = new ToolBarButton(VUtil.getIcon("pause"), "pause", this);
        reset = new ToolBarButton(VUtil.getIcon("reset"), "reset", this);
        grid  = new ToolBarButton(VUtil.getIcon("grid"),  "grid",  this);
        info  = new ToolBarButton(VUtil.getIcon("loupe"), "info",  this);
        
        pause.setEnabled(false);
        reset.setEnabled(false);
        
        slider = new ZoomSlider(pModel.getZoom());
        slider.addChangeListener(this);
        
        
        this.add(play);
        this.add(pause);
        this.add(reset);
        this.addSeparator();
        this.add(grid);
        this.addSeparator();
        this.add(info);
        this.addSeparator();
        this.add(slider);
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        System.out.println(e.getActionCommand());
        switch (e.getActionCommand())
        {
            case "play":
                play.setEnabled( false );
                pause.setEnabled( true );
                pModel.getPhysicsState().start();
                
                break;
                
            case "pause":
                pause.setEnabled( false );
                play.setEnabled(   true );
                pModel.getPhysicsState().pause();
                
                break;
                
            case "reset":
                
                break;
                
            case "grid":
                pModel.toggleState("grid");
                grid.setSelected(pModel.isState("grid"));
                pModel.fireRepaintEvents();
                
                break;
                
            case "info":
                pModel.toggleState("info");
                info.setSelected(pModel.isState("info"));
                InfoWindow.showWindow(pModel.isState("info"));
                
                break;
        }
    }
    
    
    @Override
    public void stateChanged(ChangeEvent e)
    {
        pModel.setZoom(slider.getValue());
        pModel.fireRepaintEvents();
    }
    
    
    @Override
    public void stateChanged(String id, boolean value)
    {
        switch (id)
        {
            case "grid":
                grid.setSelected(value);
                break;
                
            case "info":
                info.setSelected(value);
                InfoWindow.showWindow(value);
        }
    }
}