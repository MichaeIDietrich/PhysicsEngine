package de.engineapp.util;

import java.awt.Component;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;


public final class LookAndFeelManager
{
    private LookAndFeelManager() { }
    
    
    public static boolean applyLookAndFeelByName(String name)
    {
        try
        {
            if (name != null)
            {
                for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
                {
                    if (name.equals(info.getName()))
                    {
                        UIManager.setLookAndFeel(info.getClassName());
                        return true;
                    }
                }
            }
            
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            System.out.println("Cannot apply Look And Feel!");
            e.printStackTrace();
            return false;
        }
        
        return false;
    }
    
    
    public static boolean applySystemLookAndFeel()
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            System.out.println("Cannot apply Look And Feel!");
            e.printStackTrace();
            return false;
        }
        
        return true;
    }
    
    
    public static String getCurrentLookAndFeelName()
    {
        String activeLafClassName = UIManager.getLookAndFeel().getClass().getName();
        
        for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
        {
            if (activeLafClassName.equals(info.getClassName()))
            {
                return info.getName();
            }
        }
        
        return null;
    }
    
    
    public static String[] getLookAndFeelNames()
    {
        String[] lookAndFeels = new String[UIManager.getInstalledLookAndFeels().length];
        
        for (int i = 0; i < lookAndFeels.length; i++)
        {
            lookAndFeels[i] = UIManager.getInstalledLookAndFeels()[i].getName();
        }
        
        return lookAndFeels;
    }
    
    
    public static void updateControls(Component topLevelControl)
    {
        SwingUtilities.updateComponentTreeUI(topLevelControl);
    }
}