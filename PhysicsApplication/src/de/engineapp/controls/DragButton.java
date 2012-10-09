package de.engineapp.controls;

import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import de.engineapp.controls.dnd.CommandHandler;

public class DragButton extends JLabel
{
    private static final long serialVersionUID = 6946598301964868381L;
    
    private static Border BORDER_NORMAL = new EmptyBorder(2, 2, 2, 2);
    private static Border BORDER_PRESSED = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
    
    private boolean pressed = false;
    // this is need to implement a click event
    private Point clickPosition = null;
    
    
    public DragButton(ImageIcon buttonIcon, String command)
    {
        this(buttonIcon, command, null, null);
    }
    
    public DragButton(ImageIcon buttonIcon, String command, boolean useIconAsImage)
    {
        this(buttonIcon, command, useIconAsImage ? buttonIcon.getImage() : null);
    }
    
    public DragButton(ImageIcon buttonIcon, String command, Point dragImageOffset)
    {
        this(buttonIcon, command, buttonIcon.getImage(), dragImageOffset);
    }
    
    public DragButton(ImageIcon buttonIcon, String command, Image dragImage)
    {
        this(buttonIcon, command, dragImage, new Point(dragImage.getWidth(null) / 2, dragImage.getHeight(null) / 2));
    }
    
    public DragButton(ImageIcon buttonIcon, String command, Image dragImage, Point dragImageOffset)
    {
        super(buttonIcon);
        
        this.setBorder(BORDER_NORMAL);
        
        this.setFocusable(false);
        
        if (dragImage == null)
        {
            this.setTransferHandler(new CommandHandler(command));
        }
        else
        {
            this.setTransferHandler(new CommandHandler(command, dragImage, dragImageOffset));
        }
        
        this.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                clickPosition = e.getPoint();
                
                JComponent comp = (JComponent) e.getSource();
                TransferHandler handler = comp.getTransferHandler();
                handler.exportAsDrag(comp, e, TransferHandler.COPY);
            }
            
            @Override
            public void mouseReleased(MouseEvent e)
            {
                System.out.println("click");
                
                if (SwingUtilities.isLeftMouseButton(e) && 
                        clickPosition.x == e.getX() && clickPosition.y == e.getY())
                {
                    DragButton.this.setPressed(!DragButton.this.pressed);
                    System.out.println(DragButton.this.pressed);
                }
            }
            
        });
        
    }
    
    
    public boolean isPressed()
    {
        return pressed;
    }
    
    public void setPressed(boolean pressed)
    {
        this.pressed = pressed;
        
        if (pressed)
        {
            this.setBorder(BORDER_PRESSED);
        }
        else
        {
            this.setBorder(BORDER_NORMAL);
        }
    }
}