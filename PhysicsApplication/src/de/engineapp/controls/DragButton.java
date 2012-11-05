package de.engineapp.controls;

import java.awt.*;
import java.awt.dnd.*;
import java.awt.event.*;

import javax.swing.*;

import de.engineapp.controls.dnd.CommandHandler;

public class DragButton extends JButton implements MouseListener, MouseMotionListener, DropTargetListener
{
    private static final long serialVersionUID = 6946598301964868381L;
    
    
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
        
        this.setFocusable(false);
        
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
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
        return this.isSelected();
    }
    
    public void setPressed(boolean pressed)
    {
        this.setSelected(pressed);
    }
    
    @Override
    public void mouseClicked(MouseEvent e)
    {
        if (SwingUtilities.isLeftMouseButton(e))
        {
            this.setPressed(!this.isPressed());
        }
    }
    
    @Override
    public void mouseEntered(MouseEvent e) { }
    
    @Override
    public void mouseExited(MouseEvent e) { }
    
    @Override
    public void mousePressed(MouseEvent e) { }
    
    @Override
    public void mouseReleased(MouseEvent e) { }
    
    
    @Override
    public void dragEnter(DropTargetDragEvent dtde) { }
    
    @Override
    public void dragExit(DropTargetEvent dte)
    {
        // fire a mouseExited event to the target component
        
        int absX = MouseInfo.getPointerInfo().getLocation().x;
        int absY = MouseInfo.getPointerInfo().getLocation().y;
        int x = absX - dte.getDropTargetContext().getComponent().getLocationOnScreen().x;
        int y = absY - dte.getDropTargetContext().getComponent().getLocationOnScreen().y;
        this.dispatchEvent(new MouseEvent(this, MouseEvent.MOUSE_EXITED, System.currentTimeMillis(), 
                MouseEvent.BUTTON1_DOWN_MASK, x, y, absX, absY, 1, false, MouseEvent.BUTTON1));
        this.dispatchEvent(new MouseEvent(this, MouseEvent.MOUSE_RELEASED, System.currentTimeMillis(), 
                MouseEvent.BUTTON1_DOWN_MASK, x, y, absX, absY, 1, false, MouseEvent.BUTTON1));
    }
    
    @Override
    public void dragOver(DropTargetDragEvent dtde) { }
    
    @Override
    public void drop(DropTargetDropEvent dtde) { }
    
    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) { }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        if (SwingUtilities.isLeftMouseButton(e))
        {
            JComponent comp = (JComponent) e.getSource();
            TransferHandler handler = comp.getTransferHandler();
            handler.exportAsDrag(comp, e, TransferHandler.COPY);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) { }
}