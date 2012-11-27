package de.engineapp.controls.dnd;

import java.awt.Image;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import javax.swing.JComponent;
import javax.swing.TransferHandler;


/**
 * Drag'n'Drop handler for easy command transfer.
 * 
 * @author Micha
 */
public final class CommandHandler extends TransferHandler implements Transferable
{
    private static final long serialVersionUID = -4596146228196882904L;
    
    
    private static final DataFlavor flavors[] = { DataFlavor.stringFlavor, new DataFlavor(JComponent.class, "comp")};
    
    private String command;
    private JComponent component;
    
    public CommandHandler(JComponent source, String command)
    {
        this.command = command;
        this.component = source;
        source.setTransferHandler(this);
    }
    
    public CommandHandler(JComponent source, String command, Image dragImage)
    {
        this.command = command;
        this.component = source;
        
        if (dragImage != null)
        {
            this.setDragImage(dragImage);
            this.setDragImageOffset(new Point(dragImage.getWidth(null) / 2, dragImage.getHeight(null) / 2));
        }
        
        source.setTransferHandler(this);
    }
    
    public CommandHandler(JComponent source, String command, Image dragImage, Point dragImageOffset)
    {
        this.command = command;
        this.component = source;
        
        if (dragImage != null)
        {
            this.setDragImage(dragImage);
            this.setDragImageOffset(dragImageOffset);
        }
        
        source.setTransferHandler(this);
    }
    
    public int getSourceActions(JComponent c)
    {
      return TransferHandler.COPY;
    }
    
    
    @Override
    public boolean canImport(JComponent comp, DataFlavor flavor[])
    {
        return false;
    }
    
    
    @Override
    public boolean importData(JComponent comp, Transferable t)
    {
        return false;
    }
    
    
    @Override
    public Transferable createTransferable(JComponent comp)
    {
        return this;
    }
    
    
    // Transferable
    @Override
    public Object getTransferData(DataFlavor flavor)
    {
        if (flavors[0].equals(flavor))
        {
            return command;
        }
        else if (flavors[1].equals(flavor))
        {
            return component;
        }
        return null;
    }
    
    @Override
    public DataFlavor[] getTransferDataFlavors()
    {
        return flavors;
    }
    
    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor)
    {
        for (DataFlavor flavor_ : flavors)
        {
            if (flavor_.equals(flavor))
            {
                return true;
            }
        }
        return false;
    }
    
    public JComponent getComponent()
    {
        return component;
    }
}