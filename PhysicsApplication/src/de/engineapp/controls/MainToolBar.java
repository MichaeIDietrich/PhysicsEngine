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
    
    private ToolBarButton open;
    private ToolBarButton save;
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
        pModel.addStorageListener(this);
        
        open  = new ToolBarButton(Util.getIcon("open5"),  "open",  this);
        save  = new ToolBarButton(Util.getIcon("save3"),  "save",  this);
        play  = new ToolBarButton(Util.getIcon("play2"),  "play",  this);
        pause = new ToolBarButton(Util.getIcon("pause2"), "pause", this);
        reset = new ToolBarButton(Util.getIcon("reset2"), "reset", this);
        grid  = new ToolBarButton(Util.getIcon("grid2"),  "grid",  this);
        info  = new ToolBarButton(Util.getIcon("loupe"),  "info",  this);
        focus = new ToolBarButton(Util.getIcon("focus"),  "focus", this);
        
        pause.setEnabled(false);
        reset.setEnabled(false);
        
        slider = new ZoomSlider(pModel.getZoom());
        slider.addChangeListener(this);
        
        
        this.add(open);
        this.add(save);
        this.addSeparator();
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
                pModel.setState("runPhysics", true);
                
                break;
                
            case "pause":
                pModel.setState("runPhysics", false);
                break;
                
            case "reset":
                System.out.println("restore feature currently disabled");
                // TODO - disabled, because it does not work
                //pModel.restoreScene();
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
            case "runPhysics":
                if (value)
                {
                    play.setEnabled( false );
                    pause.setEnabled( true );
                    
                    reset.setEnabled(false);
                    
                    // store scene copy, to enable reset function
                    pModel.storeScene();
                    
                    pModel.getPhysicsState().start();
                }
                else
                {
                    pause.setEnabled( false );
                    play.setEnabled(   true );
                    pModel.getPhysicsState().pause();
                    
                    if (pModel.hasStoredScene())
                    {
                        reset.setEnabled(true);
                    }
                }
                
                break;
                
            case "grid":
                grid.setSelected(value);
                break;
                
            case "info":
                info.setSelected(value);
                InfoWindow.showWindow(value);
                
                break;
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