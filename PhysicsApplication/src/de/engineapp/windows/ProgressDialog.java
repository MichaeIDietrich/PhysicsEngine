package de.engineapp.windows;

import java.awt.*;

import javax.swing.*;

public final class ProgressDialog
{
    private JDialog dialog;
    private JProgressBar progressBar;
    
    
    public ProgressDialog(Window parent, String title, int min, int max)
    {
        dialog = new JDialog(parent, title);
        
        dialog.setModal(true);
        
        progressBar = new JProgressBar(min, max);
        progressBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        dialog.add(progressBar);
        
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        
        dialog.setVisible(true);
    }
    
    
    public int getValue()
    {
        return progressBar.getValue();
    }
    
    public void setValue(int value)
    {
        progressBar.setValue(value);
        dialog.repaint();
        progressBar.repaint();
    }
    
    
    public int getMaximum()
    {
        return progressBar.getMaximum();
    }
    
    
    public int getMinimum()
    {
        return progressBar.getMinimum();
    }
    
    
    public void dispose()
    {
        dialog.dispose();
    }
}