package de.engineapp.util;

import java.awt.*;
import java.util.HashMap;

import javax.swing.ImageIcon;

public final class Util
{
    private static HashMap<String, ImageIcon> iconMap;
    private static String path;
    private static String root;
    
    static
    {
        iconMap = new HashMap<>();
        root = "data/images/";
        path = "default/";
    }
    
    // hidden constructor
    private Util() { }
    
    
    public static ImageIcon getIcon(String id)
    {
        ImageIcon icon = iconMap.get(id);
        
        if (icon == null)
        {
            icon = new ImageIcon(root + id + ".png");
            
            if (icon.getImageLoadStatus() == MediaTracker.COMPLETE)
            {
                iconMap.put(id, icon);
            }
            else
            {
                icon = new ImageIcon(root + path + id + ".png");
                
                if (icon.getImageLoadStatus() == MediaTracker.COMPLETE)
                {
                    iconMap.put(id, icon);
                }
                else if (!id.equals("not_available"))
                {
                    icon = getIcon("not_available");
                }
            }
        }
        
        return icon;
    }
    
    
    public static Image getImage(String id)
    {
        ImageIcon icon = getIcon(id);
        
        if (icon != null)
        {
            return icon.getImage();
        }
        
        return null;
    }
    
    
    public static String getSourcePath()
    {
        return path;
    }
    
    public static void setSourcePath(String path)
    {
        Util.path = path.endsWith("/") ? path : path + "/";
    }
    
    
    public static String getRootPath()
    {
        return root;
    }
    
    public static void setRootPath(String path)
    {
        Util.root = path.endsWith("/") ? path : path + "/";
    }
}
