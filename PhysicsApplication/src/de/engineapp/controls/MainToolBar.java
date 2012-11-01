package de.engineapp.controls;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JToolBar;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.engineapp.*;
import de.engineapp.PresentationModel.StorageListener;
import de.engineapp.PresentationModel.ViewBoxListener;
import de.engineapp.windows.InfoWindow;

public class MainToolBar extends JToolBar implements ActionListener, ChangeListener, StorageListener, ViewBoxListener
{
    private static final long serialVersionUID = 4164673212238397915L;
    
    
    private PresentationModel pModel;
    
    private ToolBarButton play;
    private ToolBarButton pause;
    private ToolBarButton reset;
    private ToolBarButton grid;
    private ToolBarButton info;
    private ToolBarButton focus;
    
    private ZoomSlider slider;
    
    
    public MainToolBar(PresentationModel model)
    {
        pModel = model;
        
        this.setBorder( BorderFactory.createBevelBorder( BevelBorder.RAISED ) );
        this.setFloatable(false);
        
        pModel.addViewBoxListener(this);
        
        play  = new ToolBarButton(Util.getIcon("play"),  "play",  this);
        pause = new ToolBarButton(Util.getIcon("pause"), "pause", this);
        reset = new ToolBarButton(Util.getIcon("reset"), "reset", this);
        grid  = new ToolBarButton(Util.getIcon("grid"),  "grid",  this);
        info  = new ToolBarButton(Util.getIcon("loupe"), "info",  this);
        focus = new ToolBarButton(Util.getIcon("focus"), "focus", this);
        
        pause.setEnabled(false);
        reset.setEnabled(false);
        
        slider = new ZoomSlider(pModel.getZoom());
        slider.addChangeListener(this);
        
        
        this.add(play);
        this.add(pause);
        this.add(reset);
        this.addSeparator();
        this.add(grid);
        this.add(info);
        this.add(focus);
        this.addSeparator();
        this.add(slider);
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
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
                
            case "focus":
                pModel.setViewOffsetY(0, 0);
                pModel.fireRepaintEvents();
                
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
    
    @Override
    public void propertyChanged(String id, String value) { }
    
    
    @Override
    public void offsetChanged(int offsetX, int offsetY) { }
    
    @Override
    public void sizeChanged(int width, int height) { }
    
    @Override
    public void zoomChanged(double zoom)
    {
        slider.setValue(pModel.getZoom());
    }
}