package de.engineapp.controls;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JToolBar;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.engine.environment.Scene;
import de.engineapp.*;
import de.engineapp.PresentationModel.StorageListener;
import de.engineapp.PresentationModel.ViewBoxListener;
import de.engineapp.windows.*;

public class MainToolBar extends JToolBar implements ActionListener, ChangeListener, StorageListener, ViewBoxListener
{
    private static final long serialVersionUID = 4164673212238397915L;
    
    
    private PresentationModel pModel;
    
    private EasyButton new_;
    private EasyButton open;
    private EasyButton save;
    private EasyButton play;
    private EasyButton pause;
    private EasyButton reset;
    private EasyButton grid;
    private EasyButton info;
    private EasyButton focus;
    private EasyButton settings;
    
    private ZoomSlider slider;
    
    
    public MainToolBar(PresentationModel model)
    {
        pModel = model;
        
        this.setBorder( BorderFactory.createBevelBorder( BevelBorder.RAISED ) );
        this.setFloatable(false);
        
        pModel.addViewBoxListener(this);
        pModel.addStorageListener(this);
        
        new_     = new EasyButton(Util.getIcon("new"),      "new",      this);
        open     = new EasyButton(Util.getIcon("open5"),    "open",     this);
        save     = new EasyButton(Util.getIcon("save3"),    "save",     this);
        play     = new EasyButton(Util.getIcon("play2"),    "play",     this);
        pause    = new EasyButton(Util.getIcon("pause2"),   "pause",    this);
        reset    = new EasyButton(Util.getIcon("reset2"),   "reset",    this);
        grid     = new EasyButton(Util.getIcon("grid2"),    "grid",     this);
        info     = new EasyButton(Util.getIcon("loupe"),    "info",     this);
        focus    = new EasyButton(Util.getIcon("focus"),    "focus",    this);
        settings = new EasyButton(Util.getIcon("settings"), "settings", this);
        
        pause.setEnabled(false);
        reset.setEnabled(false);
        
        slider = new ZoomSlider(pModel.getZoom());
        slider.addChangeListener(this);
        
        
        this.add(new_);
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
        this.add(settings);
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch (e.getActionCommand())
        {
            case "new":
                pModel.setScene(new Scene());
                pModel.setZoom(1.0);
                pModel.setViewOffset(0, 0);
                break;
                
            case "open":
                
                break;
                
            case "save":
                
                break;
                
            case "play":
                pModel.setState("runPhysics", true);
                
                break;
                
            case "pause":
                pModel.setState("runPhysics", false);
                break;
                
            case "reset":
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
                pModel.setViewOffset(0, 0);
                pModel.fireRepaintEvents();
                
                break;
                
            case "settings":
                new SettingsDialog((Window) this.getTopLevelAncestor());
                break;
        }
    }
    
    
    @Override
    public void stateChanged(ChangeEvent e)
    {
        pModel.setZoom(slider.getValue(), new Point(pModel.getCanvasWidth() / 2, pModel.getCanvasHeight() / 2));
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
                    // TODO - disabled, because it does not work
                    //pModel.storeScene();
                    
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