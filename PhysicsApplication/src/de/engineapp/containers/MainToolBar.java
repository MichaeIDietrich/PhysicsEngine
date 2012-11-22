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
import de.engineapp.*;
import de.engineapp.PresentationModel.StorageListener;
import de.engineapp.PresentationModel.ViewBoxListener;
import de.engineapp.controls.*;
import de.engineapp.rec.Playback;
import de.engineapp.util.*;
import de.engineapp.windows.*;

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
            return f.getName().endsWith(".scnx") || f.isDirectory();
        }
        
        @Override
        public String getDescription()
        {
            return "Scene Files (*.scnx)";
        }
    };
    
    
    private PresentationModel pModel;
    
    private QuickButton newButton;
    private QuickButton openButton;
    private QuickButton saveButton;
    private QuickButton playButton;
    private QuickButton pauseButton;
    private QuickButton resetButton;
    private QuickToggleButton showGridButton;
    private QuickToggleButton showArrowsButton;
    private QuickButton focusButton;
    private QuickButton settingsButton;
    private QuickButton helpButton;
    private QuickButton aboutButton;
    
    private ZoomSlider zoomSlider;
    
    private DropDownButton modeButton;
    
    private Playback player;
    
    private SceneManager sceneManager;
    
    
    public MainToolBar(PresentationModel model)
    {
        pModel = model;
        
        this.setBorder( BorderFactory.createBevelBorder( BevelBorder.RAISED ) );
        this.setFloatable(false);
        
        pModel.addViewBoxListener(this);
        pModel.addStorageListener(this);
        
        newButton        = new QuickButton(      GuiUtil.getIcon(ICO_NEW),           CMD_NEW,         this);
        openButton       = new QuickButton(      GuiUtil.getIcon(ICO_OPEN),          CMD_OPEN,        this);
        saveButton       = new QuickButton(      GuiUtil.getIcon(ICO_SAVE),          CMD_SAVE,        this);
        playButton       = new QuickButton(      GuiUtil.getIcon(ICO_PLAY),          CMD_PLAY,        this);
        pauseButton      = new QuickButton(      GuiUtil.getIcon(ICO_PAUSE),         CMD_PAUSE,       this);
        resetButton      = new QuickButton(      GuiUtil.getIcon(ICO_RESET),         CMD_RESET,       this);
        showGridButton   = new QuickToggleButton(GuiUtil.getIcon(ICO_GRID),          CMD_GRID,        this);
        showArrowsButton = new QuickToggleButton(GuiUtil.getIcon(ICO_OBJECT_ARROWS), CMD_SHOW_ARROWS, this);
        focusButton      = new QuickButton(      GuiUtil.getIcon(ICO_FOCUS),         CMD_FOCUS,       this);
        settingsButton   = new QuickButton(      GuiUtil.getIcon(ICO_SETTINGS),      CMD_SETTINGS,    this);
        helpButton       = new QuickButton(      GuiUtil.getIcon(ICO_HELP),          CMD_HELP,        this);
        aboutButton      = new QuickButton(      GuiUtil.getIcon(ICO_ABOUT),         CMD_ABOUT,       this);
        
        pauseButton.setEnabled(false);
        resetButton.setEnabled(false);
        
        zoomSlider = new ZoomSlider(pModel.getZoom());
        zoomSlider.addChangeListener(this);
        
        modeButton = new DropDownButton(GuiUtil.getIcon(ICO_PHYSICS), CMD_NEXT_MODE, this);
        modeButton.addAction(LOCALIZER.getString(L_PHYSICS_MODE),   GuiUtil.getIcon(ICO_PHYSICS),  CMD_PHYSICS_MODE);
        modeButton.addAction(LOCALIZER.getString(L_RECORDING_MODE), GuiUtil.getIcon(ICO_RECORD),   CMD_RECORDING_MODE);
        modeButton.addAction(LOCALIZER.getString(L_PLAYBACK_MODE),  GuiUtil.getIcon(ICO_PLAYBACK), CMD_PLAYBACK_MODE);
        
        setToolTips();
        
        this.add(newButton);
        this.add(openButton);
        this.add(saveButton);
        this.addSeparator();
        this.add(playButton);
        this.add(pauseButton);
        this.add(resetButton);
        this.addSeparator();
        this.add(showGridButton);
        this.add(showArrowsButton);
        this.addSeparator();
        this.add(focusButton);
        this.addSeparator();
        this.add(zoomSlider);
        this.addSeparator();
        this.add(modeButton);
        this.addSeparator();
        this.add(settingsButton);
        this.add(helpButton);
        this.add(aboutButton);
        
        zoomSlider.setValue(pModel.getZoom());
        showGridButton.setSelected(pModel.isState(STG_GRID));
        showArrowsButton.setSelected(pModel.isState(STG_SHOW_ARROWS_ALWAYS));
        
        player = new Playback(pModel, 30);
        
        sceneManager = new SceneManager(pModel, this.getTopLevelAncestor());
    }
    
    
    private void setToolTips()
    {
        newButton.setToolTipText(       LOCALIZER.getString(TT_NEW));
        openButton.setToolTipText(      LOCALIZER.getString(TT_OPEN));
        saveButton.setToolTipText(      LOCALIZER.getString(TT_SAVE));
        playButton.setToolTipText(      LOCALIZER.getString(TT_PLAY));
        pauseButton.setToolTipText(     LOCALIZER.getString(TT_PAUSE));
        resetButton.setToolTipText(     LOCALIZER.getString(TT_RESET));
        showGridButton.setToolTipText(  LOCALIZER.getString(TT_GRID));
        showArrowsButton.setToolTipText(LOCALIZER.getString(TT_SHOW_ARROWS));
        focusButton.setToolTipText(     LOCALIZER.getString(TT_FOCUS));
        zoomSlider.setToolTipText(      LOCALIZER.getString(TT_ZOOM));
        settingsButton.setToolTipText(  LOCALIZER.getString(TT_SETTINGS));
        helpButton.setToolTipText(      LOCALIZER.getString(TT_HELP));
        aboutButton.setToolTipText(     LOCALIZER.getString(TT_ABOUT));
        
        switch (pModel.getProperty(PRP_MODE))
        {
            case CMD_PHYSICS_MODE:
                modeButton.setToolTipText(LOCALIZER.getString(L_PHYSICS_MODE));
                break;
                
            case CMD_RECORDING_MODE:
                modeButton.setToolTipText(LOCALIZER.getString(L_RECORDING_MODE));
                break;
                
            case CMD_PLAYBACK_MODE:
                modeButton.setToolTipText(LOCALIZER.getString(L_PLAYBACK_MODE));
                break;
        }
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch (e.getActionCommand())
        {
            case CMD_NEW:
                pModel.setProperty(PRP_CURRENT_FILE, null);
                pModel.setScene(new Scene());
                pModel.setZoom(1.0);
                pModel.setViewOffset(0, 0);
                pModel.fireRepaint();
                break;
                
            case CMD_OPEN:
                loadScene();
                if (pModel.isState(STG_SHOW_ARROWS_ALWAYS))
                {
                    pModel.setState(STG_SHOW_ARROWS_ALWAYS, true);
                }
                break;
                
            case CMD_SAVE:
                saveScene();
                break;
                
            case CMD_PLAY:
                pModel.setState(STG_RUN_PHYSICS, true);
                break;
                
            case CMD_PAUSE:
                pModel.setState(STG_RUN_PHYSICS, false);
                break;
                
            case CMD_RESET:
                pModel.restoreScene();
                resetButton.setEnabled(false);
                if (pModel.isState(STG_SHOW_ARROWS_ALWAYS))
                {
                    pModel.setState(STG_SHOW_ARROWS_ALWAYS, true);
                }
                pModel.fireRepaint();
                break;
                
            case CMD_GRID:
                pModel.toggleState(STG_GRID);
                pModel.fireRepaint();
                break;
                
            case CMD_SHOW_ARROWS:
                pModel.toggleState(STG_SHOW_ARROWS_ALWAYS);
                showArrowsButton.setSelected(pModel.isState(STG_SHOW_ARROWS_ALWAYS));
                break;
                
            case CMD_FOCUS:
                pModel.setViewOffset(0, 0);
                pModel.fireRepaint();
                break;
                
            case CMD_NEXT_MODE:
                switch (pModel.getProperty(PRP_MODE))
                {
                    case CMD_PHYSICS_MODE:
                        pModel.setProperty(PRP_MODE, CMD_RECORDING_MODE);
                        break;
                        
                    case CMD_RECORDING_MODE:
                        pModel.setProperty(PRP_MODE, CMD_PLAYBACK_MODE);
                        break;
                        
                    case CMD_PLAYBACK_MODE:
                        pModel.setProperty(PRP_MODE, CMD_PHYSICS_MODE);
                        break;
                        
                }
                break;
                
            case CMD_PHYSICS_MODE:
                pModel.setProperty(PRP_MODE, CMD_PHYSICS_MODE);
                break;
                
            case CMD_RECORDING_MODE:
                pModel.setProperty(PRP_MODE, CMD_RECORDING_MODE);
                break;
                
            case CMD_PLAYBACK_MODE:
                pModel.setProperty(PRP_MODE, CMD_PLAYBACK_MODE);
                break;
                
            case CMD_SETTINGS:
                if (SettingsDialog.showDialog((Window) this.getTopLevelAncestor()))
                {
                    Configuration newConfig = Configuration.getInstance();
                    if (!newConfig.getProperty(PRP_LANGUAGE_CODE).equals(pModel.getProperty(PRP_LANGUAGE_CODE)))
                    {
                        pModel.setProperty(PRP_LANGUAGE_CODE, newConfig.getProperty(PRP_LANGUAGE_CODE));
                    }
                    if (newConfig.isState(STG_DBLCLICK_SHOW_PROPERTIES) != pModel.isState(STG_DBLCLICK_SHOW_PROPERTIES))
                    {
                        pModel.setState(STG_DBLCLICK_SHOW_PROPERTIES, newConfig.isState(STG_DBLCLICK_SHOW_PROPERTIES));
                    }
                    LookAndFeelManager.updateControls(this.getTopLevelAncestor());
                }
                break;
                
            case CMD_HELP:
                if (!HelpDialog.isAlreadyOpened())
                {
                    new HelpDialog((Window) this.getTopLevelAncestor());
                }
                break;
                
            case CMD_ABOUT:
                new AboutDialog((Window) this.getTopLevelAncestor());
                break;
        }
    }
    
    
    @Override
    public void stateChanged(ChangeEvent e)
    {
        pModel.setZoom(zoomSlider.getValue(), new Point(pModel.getCanvasWidth() / 2, pModel.getCanvasHeight() / 2));
        pModel.fireRepaint();
    }
    
    
    @Override
    public void stateChanged(String id, boolean value)
    {
        switch (id)
        {
            case STG_RUN_PHYSICS:
                if (value)
                {
                    newButton.setEnabled(false);
                    openButton.setEnabled(false);
                    saveButton.setEnabled(false);
                    
                    playButton.setEnabled( false );
                    pauseButton.setEnabled( true );
                    
                    resetButton.setEnabled(false);
                    
                    modeButton.setEnabled(false);
                    
                    // store scene copy, to enable reset function
                    pModel.storeScene();
                    
                    if (pModel.getProperty(PRP_MODE).equals(CMD_PLAYBACK_MODE))
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
                    newButton.setEnabled(true);
                    openButton.setEnabled(true);
                    saveButton.setEnabled(true);
                    
                    pauseButton.setEnabled( false );
                    playButton.setEnabled(   true );
                    
                    modeButton.setEnabled(true);
                    
                    if (pModel.hasStoredScene())
                    {
                        resetButton.setEnabled(true);
                    }
                    
                    if (pModel.getProperty(PRP_MODE).equals(CMD_PLAYBACK_MODE))
                    {
                        player.pause();
                    }
                    else
                    {
                        pModel.getPhysicsState().pause();
                    }
                }
                
                break;
                
            case STG_GRID:
                showGridButton.setSelected(value);
                break;
        }
    }
    
    @Override
    public void propertyChanged(String id, String value)
    {
        switch (id)
        {
            case PRP_LANGUAGE_CODE:
                setToolTips();
                break;
                
            case PRP_MODE:
                switch (value)
                {
                    case CMD_PHYSICS_MODE:
                        modeButton.setIcon(GuiUtil.getIcon(ICO_PHYSICS));
                        modeButton.setToolTipText(LOCALIZER.getString(L_PHYSICS_MODE));
                        break;
                        
                    case CMD_RECORDING_MODE:
                        modeButton.setIcon(GuiUtil.getIcon(ICO_RECORD));
                        modeButton.setToolTipText(LOCALIZER.getString(L_RECORDING_MODE));
                        break;
                        
                    case CMD_PLAYBACK_MODE:
                        modeButton.setIcon(GuiUtil.getIcon(ICO_PLAYBACK));
                        modeButton.setToolTipText(LOCALIZER.getString(L_PLAYBACK_MODE));
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
        zoomSlider.setValue(pModel.getZoom());
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
        
        if (pModel.getProperty(PRP_CURRENT_FILE) != null)
        {
            dlgSave.setSelectedFile(new File(pModel.getProperty(PRP_CURRENT_FILE)));
        }
        else
        {
            dlgSave.setSelectedFile(new File("scene.scnx"));
        }
        
        dlgSave.setFileFilter(SCENE_FILTER);
        dlgSave.setDialogTitle(LOCALIZER.getString(L_TITLE_SAVE));
        
        if (dlgSave.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
        {
            if (dlgSave.getSelectedFile().exists())
            {
                String[] options = new String[] { LOCALIZER.getString(L_YES), LOCALIZER.getString(L_NO) };
                
                if (JOptionPane.showOptionDialog(this.getTopLevelAncestor(), LOCALIZER.getString(L_OVERWRITE_FILE), 
                        LOCALIZER.getString(L_TITLE_IMPORT), JOptionPane.YES_NO_OPTION, 
                        JOptionPane.WARNING_MESSAGE, null, options, options[0]) != 0)
                {
                    return;
                }
            }
            
            sceneManager.saveScene(dlgSave.getSelectedFile(), pModel.getScene());
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
            File sceneFile = dlgOpen.getSelectedFile();
            
            Scene scene = sceneManager.loadScene(sceneFile);
            
            if (scene != null)
            {
                pModel.setProperty(PRP_CURRENT_FILE, sceneFile.getPath());
                
                pModel.setScene(scene);
                pModel.fireEventListeners(EVT_SCENE_LOADED);
                pModel.fireRepaint();
            }
        }
    }
}