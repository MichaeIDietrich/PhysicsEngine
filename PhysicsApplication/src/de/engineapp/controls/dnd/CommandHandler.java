package de.engineapp.controls.dnd;

import java.awt.Image;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

public class CommandHandler extends TransferHandler implements Transferable
{
    private static final long serialVersionUID = -4596146228196882904L;
    
    private static final DataFlavor flavors[] = { DataFlavor.stringFlavor };
    
    private String command;
    
    public CommandHandler(String command)
    {
        this.command = command;
    }
    
    public CommandHandler(String command, Image dragImage)
    {
        this.command = command;
        
        if (dragImage != null)
        {
            this.setDragImage(dragImage);
            this.setDragImageOffset(new Point(dragImage.getWidth(null) / 2, dragImage.getHeight(null) / 2));
        }
    }
    
    public CommandHandler(String command, Image dragImage, Point dragImageOffset)
    {
        this.command = command;
        
        if (dragImage != null)
        {
            this.setDragImage(dragImage);
            this.setDragImageOffset(dragImageOffset);
        }
    }
    
    public int getSourceActions(JComponent c)
    {
      return TransferHandler.COPY;
    }
    
    
    @Override
    protected void exportDone(JComponent source, Transferable data, int action)
    {
//        System.out.println(source);
//        if (source instanceof DragButton)
//        {
//            System.out.println(true);
//            source.setVisible(false);
//            source.setVisible(true);
////            ((DragButton) source).setSelected(true);
//            ((DragButton) source).setSelected(false);
//        }
    };
    
    
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
        if (isDataFlavorSupported(flavor))
        {
            return command;
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
        return flavors[0].equals(flavor);
    }
}