package de.engineapp.controls;

import java.awt.*;
import java.awt.dnd.*;
import java.awt.event.*;

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

public class DragButton extends JLabel implements MouseListener, DropTargetListener
{
    private static final long serialVersionUID = 6946598301964868381L;
    
    private static Border BORDER_NORMAL = new EmptyBorder(2, 2, 2, 2);
    private static Border BORDER_PRESSED = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
    
    private boolean pressed = false;
    // this is need to implement a click event
    private Point clickPosition = null;
    
    // colors for hovering
    private Color hoveredColor = new Color(168, 251, 255);
    private Color normalColor;
    
    
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
        
        this.setOpaque(true);
        normalColor = this.getBackground();
        
        this.addMouseListener(this);
        new DropTarget(this, this);
        
        if (dragImage == null)
        {
            this.setTransferHandler(new CommandHandler(command));
        }
        else
        {
            this.setTransferHandler(new CommandHandler(command, dragImage, dragImageOffset));
        }
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
    
    @Override
    public void mouseClicked(MouseEvent e) { }
    
    @Override
    public void mouseEntered(MouseEvent e)
    {
        this.setBackground(hoveredColor);
    }
    
    @Override
    public void mouseExited(MouseEvent e)
    {
        this.setBackground(normalColor);
    }
    
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
        if (SwingUtilities.isLeftMouseButton(e) && 
                clickPosition.x == e.getX() && clickPosition.y == e.getY())
        {
            DragButton.this.setPressed(!DragButton.this.pressed);
            System.out.println(DragButton.this.pressed);
        }
        
    }
    
    
    @Override
    public void dragEnter(DropTargetDragEvent dtde) { }
    
    @Override
    public void dragExit(DropTargetEvent dte)
    {
        this.setBackground(normalColor);
    }
    
    @Override
    public void dragOver(DropTargetDragEvent dtde) { }
    
    @Override
    public void drop(DropTargetDropEvent dtde) { }
    
    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) { }
}