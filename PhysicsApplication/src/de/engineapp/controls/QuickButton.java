package de.engineapp.controls;

import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;

public class QuickButton extends JButton
{
    private static final long serialVersionUID = 4928796962220540321L;
    
    
    public QuickButton()
    {
        this(null, null, null, null);
    }
    
    public QuickButton(String text)
    {
        this(text, null, null, null);
    }
    
    public QuickButton(Icon icon)
    {
        this(null, icon, null, null);
    }
    
    public QuickButton(String text, Icon icon)
    {
        this(text, icon, null, null);
    }
    
    public QuickButton(String text, String command, ActionListener listener)
    {
        this(text, null, command, listener);
    }
    
    public QuickButton(Icon icon, String command, ActionListener listener)
    {
        this(null, icon, command, listener);
    }
    
    public QuickButton(String text, Icon icon, String command, ActionListener listener)
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