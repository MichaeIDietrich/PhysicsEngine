package de.engineapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Configuration implements Serializable, Cloneable
{
    private static final long serialVersionUID = -5115836553640371734L;
    
    
    private static Configuration instance = null;
    
    private double zoom = 1.0;
    private boolean showGrid = false;
    private String langCode = null;
    private boolean showInfo = false;
    private boolean maximized = false;
    
    
    public static Configuration getInstance()
    {
        if (instance == null)
        {
            instance = new Configuration();
        }
        
        return instance;
    }
    
    
    /** do not change - not yet fully implemented */
    public double getZoom()
    {
        return zoom;
    }
    
    public void setZoom(double zoom)
    {
        this.zoom = zoom;
    }
    
    
    /** weather a grid is drawn on the canvas */
    public boolean isShowGrid()
    {
        return showGrid;
    }
    
    public void setShowGrid(boolean showGrid)
    {
        this.showGrid = showGrid;
    }
    
    
    public boolean isShowInfo()
    {
        return showInfo;
    }
    
    public void setShowInfo(boolean showInfo)
    {
        this.showInfo = showInfo;
    }
    
    
    // will be propably replaced by some xml file
    public static void load()
    {
        File configFile = new File("config.dat");
        
        if (configFile.exists())
        {
            try (FileInputStream   fis = new FileInputStream(configFile);
                 ObjectInputStream ois = new ObjectInputStream(fis))
            {
                instance = (Configuration) ois.readObject();
            }
            catch (IOException | ClassNotFoundException ex)
            {
                ex.printStackTrace();
            }
            
        }
    }
    
    
    public static void save()
    {
        try (FileOutputStream   fos = new FileOutputStream("config.dat");
             ObjectOutputStream oos = new ObjectOutputStream(fos))
        {
            oos.writeObject(getInstance());
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
    
    
    public String getLangCode()
    {
        return langCode;
    }
    
    public void setLangCode(String langCode)
    {
        this.langCode = langCode;
    }
    
    
    public boolean isMaximized()
    {
        return maximized;
    }
    
    public void setMaximized(boolean maximized)
    {
        this.maximized = maximized;
    }
    
    
    @Override
    public Configuration clone()
    {
        try
        {
            return (Configuration) super.clone();
        }
        catch (CloneNotSupportedException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    
    public static void overrideInstance(Configuration newInstance)
    {
        instance = newInstance;
    }
}