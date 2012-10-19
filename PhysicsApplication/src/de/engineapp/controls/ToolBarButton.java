package de.engineapp.controls;

import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;

public class ToolBarButton extends JButton
{
    private static final long serialVersionUID = 4928796962220540321L;
    
    
    public ToolBarButton(Icon icon)
    {
        this(icon, null, null);
    }
    
    public ToolBarButton(Icon icon, String command, ActionListener listener)
    {
        super(icon);
        
        if (command != null && listener != null)
        {
            this.setActionCommand(command);
            this.addActionListener(listener);
        }
        
        this.setFocusable(false);
    }
}