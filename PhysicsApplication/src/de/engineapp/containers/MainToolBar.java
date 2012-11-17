package de.engineapp.containers;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import de.engine.environment.Scene;
import de.engine.math.Vector;
import de.engine.math.Rotation;
import de.engine.objects.*;
import de.engine.objects.ObjectProperties.Material;
import de.engineapp.*;
import de.engineapp.PresentationModel.StorageListener;
import de.engineapp.PresentationModel.ViewBoxListener;
import de.engineapp.controls.*;
import de.engineapp.rec.Playback;
import de.engineapp.util.*;
import de.engineapp.visual.*;
import de.engineapp.visual.Circle;
import de.engineapp.visual.Square;
import de.engineapp.visual.Ground;
import de.engineapp.windows.*;
import de.engineapp.xml.*;

import static de.engineapp.Constants.*;


public class MainToolBar extends JToolBar implements ActionListener, ChangeListener, StorageListener, ViewBoxListener
{
    private static final long serialVersionUID = 4164673212238397915L;
    
    private final static Localizer LOCALIZER = Localizer.getInstance();
    
    private static final FileFilter SCENE_FILTER = new FileFilter()
    {
        @Override
        public boolean accept(File f)
        {
            return f.getName().endsWith(".scnx");
        }
        
        @Override
        public String getDescription()
        {
            return "Scene Files (*.scnx)";
        }
    };
    
    
    private PresentationModel pModel;
    
    private QuickButton new_;
    private QuickButton open;
    private QuickButton save;
    private QuickButton play;
    private QuickButton pause;
    private QuickButton reset;
    private QuickToggleButton grid;
    private QuickToggleButton showArrows;
    private QuickButton focus;
    private QuickButton settings;
    
    private ZoomSlider slider;
    
    private DropDownButton mode;
    
    private Playback player;
    
    
    public MainToolBar(PresentationModel model)
    {
        pModel = model;
        
        this.setBorder( BorderFactory.createBevelBorder( BevelBorder.RAISED ) );
        this.setFloatable(false);
        
        pModel.addViewBoxListener(this);
        pModel.addStorageListener(this);
        
        new_       = new QuickButton(      Util.getIcon(ICO_NEW),           CMD_NEW,         this);
        open       = new QuickButton(      Util.getIcon(ICO_OPEN),          CMD_OPEN,        this);
        save       = new QuickButton(      Util.getIcon(ICO_SAVE),          CMD_SAVE,        this);
        play       = new QuickButton(      Util.getIcon(ICO_PLAY),          CMD_PLAY,        this);
        pause      = new QuickButton(      Util.getIcon(ICO_PAUSE),         CMD_PAUSE,       this);
        reset      = new QuickButton(      Util.getIcon(ICO_RESET),         CMD_RESET,       this);
        grid       = new QuickToggleButton(Util.getIcon(ICO_GRID),          CMD_GRID,        this);
        showArrows = new QuickToggleButton(Util.getIcon(ICO_OBJECT_ARROWS), CMD_SHOW_ARROWS, this);
        focus      = new QuickButton(      Util.getIcon(ICO_FOCUS),         CMD_FOCUS,       this);
        settings   = new QuickButton(      Util.getIcon(ICO_SETTINGS),      CMD_SETTINGS,    this);
        
        pause.setEnabled(false);
        reset.setEnabled(false);
        
        slider = new ZoomSlider(pModel.getZoom());
        slider.addChangeListener(this);
        
        mode = new DropDownButton(Util.getIcon(ICO_PHYSICS), CMD_NEXT_MODE, this);
        mode.addAction(LOCALIZER.getString(L_PHYSICS_MODE), Util.getIcon(ICO_PHYSICS), CMD_PHYSICS_MODE);
        mode.addAction(LOCALIZER.getString(L_RECORDING_MODE), Util.getIcon(ICO_RECORD), CMD_RECORDING_MODE);
        mode.addAction(LOCALIZER.getString(L_PLAYBACK_MODE), Util.getIcon(ICO_PLAYBACK), CMD_PLAYBACK_MODE);
        
        setToolTips();
        
        this.add(new_);
        this.add(open);
        this.add(save);
        this.addSeparator();
        this.add(play);
        this.add(pause);
        this.add(reset);
        this.addSeparator();
        this.add(grid);
        this.add(showArrows);
        this.addSeparator();
        this.add(focus);
        this.addSeparator();
        this.add(slider);
        this.addSeparator();
        this.add(mode);
        this.addSeparator();
        this.add(settings);
        
        slider.setValue(pModel.getZoom());
        grid.setSelected(pModel.isState(GRID));
        showArrows.setSelected(pModel.isState(SHOW_ARROWS_ALWAYS));
        
        player = new Playback(pModel, 30);
    }
    
    
    private void setToolTips()
    {
        new_.setToolTipText(LOCALIZER.getString(TT_NEW));
        open.setToolTipText(LOCALIZER.getString(TT_OPEN));
        save.setToolTipText(LOCALIZER.getString(TT_SAVE));
        play.setToolTipText(LOCALIZER.getString(TT_PLAY));
        pause.setToolTipText(LOCALIZER.getString(TT_PAUSE));
        reset.setToolTipText(LOCALIZER.getString(TT_RESET));
        grid.setToolTipText(LOCALIZER.getString(TT_GRID));
        showArrows.setToolTipText(LOCALIZER.getString(TT_SHOW_ARROWS));
        focus.setToolTipText(LOCALIZER.getString(TT_FOCUS));
        slider.setToolTipText(LOCALIZER.getString(TT_ZOOM));
        settings.setToolTipText(LOCALIZER.getString(TT_SETTINGS));
        
        switch (pModel.getProperty(MODE))
        {
            case CMD_PHYSICS_MODE:
                mode.setToolTipText(LOCALIZER.getString(L_PHYSICS_MODE));
                break;
                
            case CMD_RECORDING_MODE:
                mode.setToolTipText(LOCALIZER.getString(L_RECORDING_MODE));
                break;
                
            case CMD_PLAYBACK_MODE:
                mode.setToolTipText(LOCALIZER.getString(L_PLAYBACK_MODE));
                break;
        }
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch (e.getActionCommand())
        {
            case CMD_NEW:
                pModel.setScene(new Scene());
                pModel.setZoom(1.0);
                pModel.setViewOffset(0, 0);
                pModel.fireRepaint();
                break;
                
            case CMD_OPEN:
                loadScene();
                if (pModel.isState(SHOW_ARROWS_ALWAYS))
                {
                    pModel.setState(SHOW_ARROWS_ALWAYS, true);
                }
                break;
                
            case CMD_SAVE:
                saveScene();
                break;
                
            case CMD_PLAY:
                pModel.setState(RUN_PHYSICS, true);
                break;
                
            case CMD_PAUSE:
                pModel.setState(RUN_PHYSICS, false);
                break;
                
            case CMD_RESET:
                pModel.restoreScene();
                reset.setEnabled(false);
                if (pModel.isState(SHOW_ARROWS_ALWAYS))
                {
                    pModel.setState(SHOW_ARROWS_ALWAYS, true);
                }
                pModel.fireRepaint();
                break;
                
            case CMD_GRID:
                pModel.toggleState(GRID);
                pModel.fireRepaint();
                break;
                
            case CMD_SHOW_ARROWS:
                pModel.toggleState(SHOW_ARROWS_ALWAYS);
                showArrows.setSelected(pModel.isState(SHOW_ARROWS_ALWAYS));
                break;
                
            case CMD_FOCUS:
                pModel.setViewOffset(0, 0);
                pModel.fireRepaint();
                break;
                
            case CMD_NEXT_MODE:
                switch (pModel.getProperty(MODE))
                {
                    case CMD_PHYSICS_MODE:
                        pModel.setProperty(MODE, CMD_RECORDING_MODE);
                        break;
                        
                    case CMD_RECORDING_MODE:
                        pModel.setProperty(MODE, CMD_PLAYBACK_MODE);
                        break;
                        
                    case CMD_PLAYBACK_MODE:
                        pModel.setProperty(MODE, CMD_PHYSICS_MODE);
                        break;
                        
                }
                break;
                
            case CMD_PHYSICS_MODE:
                pModel.setProperty(MODE, CMD_PHYSICS_MODE);
                break;
                
            case CMD_RECORDING_MODE:
                pModel.setProperty(MODE, CMD_RECORDING_MODE);
                break;
                
            case CMD_PLAYBACK_MODE:
                pModel.setProperty(MODE, CMD_PLAYBACK_MODE);
                break;
                
            case CMD_SETTINGS:
                if (SettingsDialog.showDialog((Window) this.getTopLevelAncestor()))
                {
                    Configuration newConfig = Configuration.getInstance();
                    if (!newConfig.getProperty(LANGUAGE_CODE).equals(pModel.getProperty(LANGUAGE_CODE)))
                    {
                        pModel.setProperty(LANGUAGE_CODE, newConfig.getProperty(LANGUAGE_CODE));
                    }
                    if (newConfig.isState(DBLCLICK_SHOW_PROPERTIES) != pModel.isState(DBLCLICK_SHOW_PROPERTIES))
                    {
                        pModel.setState(DBLCLICK_SHOW_PROPERTIES, newConfig.isState(DBLCLICK_SHOW_PROPERTIES));
                    }
                    LookAndFeelManager.updateControls(this.getTopLevelAncestor());
                }
                break;
        }
    }
    
    
    @Override
    public void stateChanged(ChangeEvent e)
    {
        pModel.setZoom(slider.getValue(), new Point(pModel.getCanvasWidth() / 2, pModel.getCanvasHeight() / 2));
        pModel.fireRepaint();
    }
    
    
    @Override
    public void stateChanged(String id, boolean value)
    {
        switch (id)
        {
            case RUN_PHYSICS:
                if (value)
                {
                    new_.setEnabled(false);
                    open.setEnabled(false);
                    save.setEnabled(false);
                    
                    play.setEnabled( false );
                    pause.setEnabled( true );
                    
                    reset.setEnabled(false);
                    
                    mode.setEnabled(false);
                    
                    // store scene copy, to enable reset function
                    pModel.storeScene();
                    
                    if (pModel.getProperty(MODE).equals(CMD_PLAYBACK_MODE))
                    {
                        player.start();
                    }
                    else
                    {
                        pModel.getPhysicsState().start();
                    }
                }
                else
                {
                    new_.setEnabled(true);
                    open.setEnabled(true);
                    save.setEnabled(true);
                    
                    pause.setEnabled( false );
                    play.setEnabled(   true );
                    
                    mode.setEnabled(true);
                    
                    if (pModel.hasStoredScene())
                    {
                        reset.setEnabled(true);
                    }
                    
                    if (pModel.getProperty(MODE).equals(CMD_PLAYBACK_MODE))
                    {
                        player.pause();
                    }
                    else
                    {
                        pModel.getPhysicsState().pause();
                    }
                }
                
                break;
                
            case GRID:
                grid.setSelected(value);
                break;
        }
    }
    
    @Override
    public void propertyChanged(String id, String value)
    {
        switch (id)
        {
            case LANGUAGE_CODE:
                setToolTips();
                break;
                
            case MODE:
                switch (value)
                {
                    case CMD_PHYSICS_MODE:
                        mode.setIcon(Util.getIcon(ICO_PHYSICS));
                        mode.setToolTipText(LOCALIZER.getString(L_PHYSICS_MODE));
                        break;
                        
                    case CMD_RECORDING_MODE:
                        mode.setIcon(Util.getIcon(ICO_RECORD));
                        mode.setToolTipText(LOCALIZER.getString(L_RECORDING_MODE));
                        break;
                        
                    case CMD_PLAYBACK_MODE:
                        mode.setIcon(Util.getIcon(ICO_PLAYBACK));
                        mode.setToolTipText(LOCALIZER.getString(L_PLAYBACK_MODE));
                        break;
                }
                break;
        }
    }
    
    
    @Override
    public void offsetChanged(int offsetX, int offsetY) { }
    
    @Override
    public void sizeChanged(int width, int height) { }
    
    @Override
    public void zoomChanged(double zoom)
    {
        slider.setValue(pModel.getZoom());
    }
    
    
    private void saveScene()
    {
        File stdSceneDir = new File("scenes");
        
        if (!stdSceneDir.exists())
        {
            stdSceneDir.mkdir();
        }
        
        UIManager.put("FileChooser.cancelButtonText", LOCALIZER.getString(L_CANCEL));
        UIManager.put("FileChooser.cancelButtonToolTipText", LOCALIZER.getString(L_CANCEL));
        UIManager.put("FileChooser.saveButtonText", LOCALIZER.getString(L_SAVE));
        UIManager.put("FileChooser.saveButtonToolTipText", LOCALIZER.getString(L_SAVE));
        
        JFileChooser dlgSave = new JFileChooser("scene");
        dlgSave.setCurrentDirectory(stdSceneDir);
        dlgSave.setSelectedFile(new File("scene.scnx"));
        dlgSave.setFileFilter(SCENE_FILTER);
        dlgSave.setDialogTitle(LOCALIZER.getString(L_TITLE_SAVE));
        
        if (dlgSave.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
        {
            XMLWriter writer = new XMLWriter(dlgSave.getSelectedFile());
            
            Scene scene = pModel.getScene();
            
            writer.writeDeclaration();
            writer.writeStartElement("Scene");
            writer.writeAttribute("version", FILE_VERSION);
            writer.writeAttribute("gravitation", "9.81"); // missing property
            
            if (scene.getGround() != null)
            {
                writer.writeStartElement("Ground");
                writer.writeAttribute("type", "" + scene.getGround().getType());
                writer.writeAttribute("watermark", "" + scene.getGround().getWatermark());
                writer.writeEndElement();
            }
            
            for (ObjectProperties object : scene.getObjects())
            {
                if (object instanceof Circle)
                {
                    writer.writeStartElement("Circle");
                }
                else if (object instanceof Square)
                {
                    writer.writeStartElement("Square");
                }
                
                writer.writeAttribute("name", ((ISelectable) object).getName());
                writer.writeAttribute("color", "" + ((IDrawable) object).getColor().getRGB());
                writer.writeAttribute("material", "" + object.surface);
                writer.writeAttribute("x", "" + object.getPosition().getX());
                writer.writeAttribute("y", "" + object.getPosition().getY());
                writer.writeAttribute("vx", "" + object.velocity.getX());
                writer.writeAttribute("vy", "" + object.velocity.getY());
                writer.writeAttribute("mass", "" + object.getMass());
                writer.writeAttribute("radius", "" + object.getRadius());
                writer.writeAttribute("rotation", "" + object.world_position.rotation.getAngle());
                writer.writeAttribute("pinned", object.isPinned ? "true" : "false");
                
                writer.writeEndElement();
            }
            
            writer.writeEndElement();
            writer.close();
        }
    }
    
    
    private void loadScene()
    {
        File stdSceneDir = new File("scenes");
        
        if (!stdSceneDir.exists())
        {
            stdSceneDir.mkdir();
        }
        
        UIManager.put("FileChooser.cancelButtonText", LOCALIZER.getString(L_CANCEL));
        UIManager.put("FileChooser.cancelButtonToolTipText", LOCALIZER.getString(L_CANCEL));
        UIManager.put("FileChooser.openButtonText", LOCALIZER.getString(L_OPEN));
        UIManager.put("FileChooser.openButtonToolTipText", LOCALIZER.getString(L_OPEN));
        
        JFileChooser dlgOpen = new JFileChooser("scene");
        dlgOpen.setCurrentDirectory(stdSceneDir);
        dlgOpen.setSelectedFile(new File("scene.scnx"));
        dlgOpen.setFileFilter(SCENE_FILTER);
        dlgOpen.setDialogTitle(LOCALIZER.getString(L_TITLE_OPEN));
        
        if (dlgOpen.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
        {
            XMLReader reader = new XMLReader(dlgOpen.getSelectedFile());
            
            Element node = reader.getNode("Scene");
            
            if (node != null)
            {
                String version = node.getAttribute("version");
                
                if (!FILE_VERSION.equals(version))
                {
                    String[] options = new String[] { LOCALIZER.getString(L_OK), LOCALIZER.getString(L_CANCEL) };
                    
                    if (JOptionPane.showOptionDialog(this.getTopLevelAncestor(), 
                            LOCALIZER.getString(L_WRONG_VERSION), LOCALIZER.getString(L_TITLE_IMPORT),
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0])
                            != 0)
                    {
                        return;
                    }
                }
                
                Scene scene = new Scene();
                
                ObjectProperties object = null;
                node = reader.getNode("Scene/Ground");
                
                if (node != null)
                {
                    scene.setGround(new Ground(pModel, getInt(node.getAttribute("type")), getInt(node.getAttribute("watermark"))));
                }
                
                for (Element obj : reader.getNodes("Scene/Circle | Scene/Square"))
                {
                    switch (obj.getName())
                    {
                        case "Circle":
                            object = new Circle(pModel, new Vector(getDouble(obj.getAttribute("x")), getDouble(obj.getAttribute("y"))), 
                                    getDouble(obj.getAttribute("radius")));
                            break;
                            
                        case "Square":
                            object = new Square(pModel, new Vector(getDouble(obj.getAttribute("x")), getDouble(obj.getAttribute("y"))), 
                                    getDouble(obj.getAttribute("radius")));
                            break;
                            
                        default:
                            continue;
                    }
                    
                    ((ISelectable) object).setName(obj.getAttribute("name"));
                    ((IDrawable) object).setColor(new Color(getInt(obj.getAttribute("color"))));
                    object.setMass(getDouble(obj.getAttribute("mass")));
                    object.velocity = new Vector(getDouble(obj.getAttribute("vx")), getDouble(obj.getAttribute("vy")));
                    object.world_position.rotation = new Rotation(getDouble(obj.getAttribute("rotation")));
                    
                    String strMat = obj.getAttribute("material");
                    for (Material mat : Material.values())
                    {
                        if (mat.toString().equals(strMat))
                        {
                            object.surface = mat;
                            break;
                        }
                    }
                    
                    object.isPinned = "true".equals(obj.getAttribute("pinned"));
                    
                    scene.add(object);
                }
                
                pModel.setScene(scene);
                pModel.fireEventListeners(SCENE_LOADED);
                pModel.fireRepaint();
            }
        }
    }
    
    
    private double getDouble(String value)
    {
        if (value == null)
        {
            return 0.0;
        }
        
        return Double.parseDouble(value);
    }
    
    
    private int getInt(String value)
    {
        if (value == null)
        {
            return 0;
        }
        
        return Integer.parseInt(value);
    }
}