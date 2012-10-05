package de.engineapp.controls;

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
    
    public int getSourceActions(JComponent c) {
      return TransferHandler.COPY;
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