package de.engineapp.windows;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import de.engineapp.Localizer;
import de.engineapp.controls.*;

public final class SettingsDialog extends JDialog implements ActionListener
{
    private static final long serialVersionUID = 7034070996690341136L;
    private final static Localizer LOCALIZER = Localizer.getInstance();
    
    
    public SettingsDialog(Window parent)
    {
        super(parent, LOCALIZER.getString("SETTINGS"));
        this.setModal(true);
        
        this.setSize(250, 250);
        this.setLocationRelativeTo(parent);
        
        initializeComponents();
        
        this.setVisible(true);
    }
    
    private void initializeComponents()
    {
        VerticalBoxPanel container = new VerticalBoxPanel();
        container.add(Box.createHorizontalStrut(220));
        
        container.addGap(15);
        container.add(new JLabel(LOCALIZER.getString("LANGUAGE")));
        
        IconComboBox<String> cboLang = new IconComboBox<String>(LOCALIZER.getAvailableLanguages(), "flags");
        cboLang.setPreferredSize(new Dimension(100, cboLang.getPreferredSize().height));
        cboLang.setEditable(false);
        cboLang.addItemListener(new ItemListener()
        {
            
            @Override
            public void itemStateChanged(ItemEvent e)
            {
                if (e.getID() == ItemEvent.SELECTED)
                {
                    System.out.println("item: " + e.getItem());
                }
            }
        });
        
        container.add(cboLang, Component.RIGHT_ALIGNMENT);
        
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        pnlButtons.add(new EasyButton(LOCALIZER.getString("OK"), "ok", this));
        pnlButtons.add(new EasyButton(LOCALIZER.getString("CANCEL"), "cancel", this));
        
        this.add(pnlButtons, BorderLayout.PAGE_END);
        this.add(container);
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch (e.getActionCommand())
        {
            case "ok":
                
                break;
                
            case "cancel":
                
                break;
        }
    }
}