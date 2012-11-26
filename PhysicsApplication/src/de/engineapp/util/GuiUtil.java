package de.engineapp.util;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.zip.*;

import javax.imageio.ImageIO;
import javax.swing.*;


/**
 * Util class, the provides easy methods for GUI usage.
 * 
 * @author Micha
 */
public final class GuiUtil
{
    private static HashMap<String, ImageIcon> iconMap;
    private static String path;
    private static String root;
    
    static
    {
        iconMap = new HashMap<>();
        root = "images/";
        path = "default/";
    }
    
    // hidden constructor
    private GuiUtil() { }
    
    
    public static ImageIcon getIcon(String id)
    {
        ImageIcon icon = iconMap.get(id);
        
        if (icon == null)
        {
            icon = loadImage(root + id + ".png");
            
            if (icon != null)
            {
                iconMap.put(id, icon);
            }
            else
            {
                icon = loadImage(root + path + id + ".png");
                
                if (icon != null)
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
    
    
    private static ImageIcon loadImage(String path)
    {
        try
        {
            InputStream stream = getResource(path);
            
            if (stream != null)
            {
                ImageIcon icon = new ImageIcon(ImageIO.read(stream));
                
                if (icon.getImageLoadStatus() == MediaTracker.COMPLETE)
                {
                    return icon;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return null;
    }
    
    
    public static String getImageSourcePath()
    {
        return path;
    }
    
    public static void setImageSourcePath(String path)
    {
        GuiUtil.path = path.endsWith("/") ? path : path + "/";
    }
    
    
    public static String getImageRootPath()
    {
        return root;
    }
    
    public static void setImageRootPath(String path)
    {
        GuiUtil.root = path.endsWith("/") ? path : path + "/";
    }
    
    
    public static InputStream getResource(String name)
    {
        if (name.charAt(0) != '/')
        {
            name = "/" + name;
        }
        
        return GuiUtil.class.getResourceAsStream(name);
    }
    
    
    public static boolean resourceExists(String name)
    {
        if (name.charAt(0) != '/')
        {
            name = "/" + name;
        }
        
        return GuiUtil.class.getResource(name) != null;
    }
    
    
    public static String[] getResources(String path)
    {
        if (path == null)
        {
            path = new String();
        }
        
        if (path.length() > 0 && path.charAt(0) == '/')
        {
            path = path.substring(1);
        }
        
        try
        {
            String classPath = System.getProperty("java.class.path");
//            String classPath = "C:\\Users\\Micha\\Desktop\\pe.jar";
            
            if (classPath.endsWith(".jar"))
            {
                ZipFile zipFile = new ZipFile(classPath);
                
                List<String> entries = new ArrayList<>();
                
                for (ZipEntry entry : Collections.list(zipFile.entries()))
                {
                    String name = entry.getName();
                    if (name.startsWith(path))
                    {
                        String res = name.substring(path.length() + 1);
                        int i = res.indexOf('/');
                        
                        if (i == -1)
                        {
                            if (res.length() > 0)
                            {
                                entries.add(res);
                            }
                        }
                        else
                        {
                            res = res.substring(0, i);
                            
                            if (!entries.contains(res))
                            {
                                entries.add(res);
                            }
                        }
                    }
                }
                
                zipFile.close();
                
                return entries.toArray(new String[entries.size()]);
            }
            else
            {
                return new File("resources/" + path).list();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return null;
    }
    
    
    public static String getHtmlImage(String id)
    {
        URL url = GuiUtil.class.getResource("/" + root + path + id + ".png");
        
        if (url != null)
        {
            return "<image src=\"" + url.toString() + "\">";
        }
        
        return "<image src=\"\">";
    }
    
    
    public static boolean isLeftButton(MouseEvent e, boolean ctrlDown, boolean shiftDown, boolean altDown)
    {
        return SwingUtilities.isLeftMouseButton(e) && e.isControlDown() == ctrlDown &&
                e.isShiftDown() == shiftDown && e.isAltDown() == altDown;
    }
    
    public static boolean isRightButton(MouseEvent e, boolean ctrlDown, boolean shiftDown, boolean altDown)
    {
        return SwingUtilities.isRightMouseButton(e) && e.isControlDown() == ctrlDown &&
                e.isShiftDown() == shiftDown && e.isAltDown() == altDown;
    }
    
    public static boolean isMiddleButton(MouseEvent e, boolean ctrlDown, boolean shiftDown, boolean altDown)
    {
        return SwingUtilities.isMiddleMouseButton(e) && e.isControlDown() == ctrlDown &&
                e.isShiftDown() == shiftDown && e.isAltDown() == altDown;
    }
}