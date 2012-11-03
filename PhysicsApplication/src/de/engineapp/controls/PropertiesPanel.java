package de.engineapp.controls;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import de.engineapp.PresentationModel;

public class PropertiesPanel extends JPanel
{
    private static final long serialVersionUID = 8656904964293251249L;
    
    
    private PresentationModel pModel;
    
    
    public PropertiesPanel(PresentationModel model)
    {
        pModel = model;
        
        this.setBorder( BorderFactory.createBevelBorder( BevelBorder.RAISED ) );
        
        JTextField massInput = new JTextField("Masse");
        JTextField surface   = new JTextField("Material");
        
        massInput.setSize(100,100);
        
        
        this.add(massInput);
        this.add(surface);
    }
}