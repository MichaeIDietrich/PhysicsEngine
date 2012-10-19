package de.engineapp;

import java.awt.Image;
import java.util.HashMap;

import javax.swing.ImageIcon;

public class VUtil
{
    private static HashMap<String, ImageIcon> iconMap;
    
    static
    {
        iconMap = new HashMap<>();
    }
    
    // private constructor
    private VUtil() { }
    
    
    public static ImageIcon getIcon(String id)
    {
        ImageIcon icon = iconMap.get(id);
        
        if (icon == null)
        {
            icon = new ImageIcon("images/" + id + ".png");
            
            if (icon != null)
            {
                iconMap.put(id, icon);
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
}
