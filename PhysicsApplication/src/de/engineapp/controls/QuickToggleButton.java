package de.engineapp.controls;

import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JToggleButton;

public class QuickToggleButton extends JToggleButton
{
    private static final long serialVersionUID = 4928796962220540321L;
    
    
    public QuickToggleButton()
    {
        this(null, null, null, null);
    }
    
    public QuickToggleButton(String text)
    {
        this(text, null, null, null);
    }
    
    public QuickToggleButton(Icon icon)
    {
        this(null, icon, null, null);
    }
    
    public QuickToggleButton(String text, Icon icon)
    {
        this(text, icon, null, null);
    }
    
    public QuickToggleButton(String text, String command, ActionListener listener)
    {
        this(text, null, command, listener);
    }
    
    public QuickToggleButton(Icon icon, String command, ActionListener listener)
    {
        this(null, icon, command, listener);
    }
    
    public QuickToggleButton(String text, Icon icon, String command, ActionListener listener)
    {
        if (text != null)
        {
            this.setText(text);
        }
        
        if (icon != null)
        {
            this.setIcon(icon);
        }
        
        if (command != null && listener != null)
        {
            this.setActionCommand(command);
            this.addActionListener(listener);
        }
        
        this.setFocusable(false);
    }
}