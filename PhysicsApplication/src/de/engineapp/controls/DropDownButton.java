package de.engineapp.controls;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public final class DropDownButton extends JButton implements MouseListener, ActionListener
{
    private static final long serialVersionUID = 4047868024547576930L;
    
    
    private JPopupMenu dropDownMenu;
    private String command = null;
    private boolean drawSeparator = false;
    
    
    public DropDownButton()
    {
        this(null, null, null, null);
    }
    
    public DropDownButton(String text)
    {
        this(text, null, null, null);
    }
    
    public DropDownButton(Icon icon)
    {
        this(null, icon, null, null);
    }
    
    public DropDownButton(String text, Icon icon)
    {
        this(text, icon, null, null);
    }
    
    public DropDownButton(String text, String command, ActionListener listener)
    {
        this(text, null, command, listener);
    }
    
    public DropDownButton(Icon icon, String command, ActionListener listener)
    {
        this(null, icon, command, listener);
    }
    
    public DropDownButton(String text, Icon icon, String command, ActionListener listener)
    {
        if (text == null)
        {
            this.setText("▾");
        }
        else
        {
            this.setText(text + " ▾");
        }
        
        if (icon != null)
        {
            this.setIcon(icon);
        }
        
        if (command != null)
        {
            this.command = command;
        }
        
        if (listener != null)
        {
            this.addActionListener(listener);
        }
        
        this.setFocusable(false);
        
        dropDownMenu = new JPopupMenu();
        
        this.addMouseListener(this);
    }
    
    
    public void addAction(String text, String command)
    {
        addAction(text, null, command, null);
    }
    
    public void addAction(Icon icon, String command)
    {
        addAction(null, icon, command, null);
    }
    
    public void addAction(String text, Icon icon, String command)
    {
        addAction(text, icon, command, null);
    }
    
    public void addAction(String text, ActionListener listener)
    {
        addAction(text, null, null, listener);
    }
    
    public void addAction(Icon icon, ActionListener listener)
    {
        addAction(null, icon, null, listener);
    }
    
    public void addAction(String text, Icon icon, ActionListener listener)
    {
        addAction(text, icon, null, listener);
    }
    
    public void addAction(String text, String command, ActionListener listener)
    {
        addAction(text, null, command, listener);
    }
    
    public void addAction(Icon icon, String command, ActionListener listener)
    {
        addAction(null, icon, command, listener);
    }
    
    public void addAction(String text, Icon icon, String command, ActionListener listener)
    {
        JMenuItem item = new JMenuItem();
        
        if (text != null)
        {
            item.setText(text);
        }
        if (icon != null)
        {
            item.setIcon(icon);
        }
        if (command != null)
        {
            item.setActionCommand(command);
        }
        if (listener == null)
        {
            item.addActionListener(this);
        }
        else
        {
            item.addActionListener(listener);
        }
        
        dropDownMenu.add(item);
    }
    
    
    @Override
    public void mouseClicked(MouseEvent e)
    {
        if (SwingUtilities.isLeftMouseButton(e))
        {
            if (e.getX() > this.getWidth() - 14)
            {
                if (dropDownMenu.getComponentCount() > 0)
                {
                    dropDownMenu.show(this, 0, this.getHeight());
                }
            }
            else
            {
                this.fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, command));
            }
        }
    }
    
    @Override
    public void mousePressed(MouseEvent e) { }
    
    @Override
    public void mouseReleased(MouseEvent e) { }
    
    @Override
    public void mouseEntered(MouseEvent e)
    {
        drawSeparator = true;
    }
    
    @Override
    public void mouseExited(MouseEvent e)
    {
        drawSeparator = false;
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        this.fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, e.getActionCommand()));
    }
    
    
    @Override
    protected void fireActionPerformed(ActionEvent event)
    {
        if (event.getActionCommand() != null)
        {
            super.fireActionPerformed(event);
        }
    }
    
    
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        
        if (drawSeparator)
        {
            g.setColor(Color.GRAY);
            g.drawLine(this.getWidth() - 14, 3, this.getWidth() -  14, this.getHeight() - 4);
        }
    }
}