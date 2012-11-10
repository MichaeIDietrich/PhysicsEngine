package de.engineapp.windows;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import de.engineapp.*;
import de.engineapp.controls.*;

public final class SettingsDialog extends JDialog implements ActionListener, ItemListener
{
    private static final long serialVersionUID = 7034070996690341136L;
    private final static Localizer LOCALIZER = Localizer.getInstance();
    
//    private Font fontCombo;
    
    private JPanel rootPane;
    private IconComboBox<String> cboLang;
    private JComboBox<String> cboShowProperties;
    private JComboBox<String> cboLookAndFeels;
    
    private boolean successful = false;
    
    private Configuration config;
    
    
    public static boolean showDialog(Window parent)
    {
        SettingsDialog dlg = new SettingsDialog(parent);
        return dlg.wasSuccessful();
    }
    
    private SettingsDialog(Window parent)
    {
        super(parent, LOCALIZER.getString("SETTINGS"));
        
//        fontCombo = this.getFont().deriveFont(12f);
        
        config = Configuration.getInstance().clone();
        
        this.setModal(true);
        
        this.setSize(250, 250);
        this.setLocationRelativeTo(parent);
        this.setResizable(false);
        
        rootPane = new JPanel(new BorderLayout());
        initializeComponents();
        this.add(rootPane);
        
        this.setVisible(true);
    }
    
    
    private void initializeComponents()
    {
        VerticalBoxPanel container = new VerticalBoxPanel(false);
        container.add(Box.createHorizontalStrut(220));
        
        container.addGap(15);
        container.add(new JLabel(LOCALIZER.getString("LANGUAGE")));
        container.addGap(3);
        
        cboLang = new IconComboBox<String>(LOCALIZER.getAvailableLanguages(), "flags");
        cboLang.setSelectedItem(config.getLangCode());
        cboLang.setPreferredSize(new Dimension(120, cboLang.getPreferredSize().height));
        cboLang.addItemListener(this);
        
        container.add(cboLang, Component.RIGHT_ALIGNMENT);
        
        
        container.addGap(15);
        container.add(new JLabel(LOCALIZER.getString("SHOWPROPERTIES")));
        container.addGap(3);
        
        cboShowProperties = new JComboBox<String>(new String[] { LOCALIZER.getString("OBJECT_SELECTED"),
                                                                 LOCALIZER.getString("DBLCLICK_OBJECT") });
//        cboShowProperties.setFont(fontCombo);
        cboShowProperties.setFocusable(false);
        cboShowProperties.setSelectedIndex(config.isDblClickShowProperties() ? 1 : 0);
        cboShowProperties.setPreferredSize(new Dimension(120, cboShowProperties.getPreferredSize().height));
        cboShowProperties.addItemListener(this);
        
        container.add(cboShowProperties, Component.RIGHT_ALIGNMENT);
        
        
        container.addGap(15);
        container.add(new JLabel(LOCALIZER.getString("LOOKANDFEEL")));
        container.addGap(3);
        
        String[] lookAndFeels = new String[UIManager.getInstalledLookAndFeels().length];
        for (int i = 0; i < lookAndFeels.length; i++)
        {
            lookAndFeels[i] = UIManager.getInstalledLookAndFeels()[i].getName();
        }
        
        
        cboLookAndFeels = new JComboBox<String>(lookAndFeels);
//        cboLookAndFeels.setFont(fontCombo);
        cboLookAndFeels.setFocusable(false);
        cboLookAndFeels.setSelectedItem(UIManager.getLookAndFeel().getName());
        cboLookAndFeels.setPreferredSize(new Dimension(120, cboShowProperties.getPreferredSize().height));
        cboLookAndFeels.addItemListener(this);
        
        container.add(cboLookAndFeels, Component.RIGHT_ALIGNMENT);
        
        
        container.addGap(20);
        container.addSeparator();
        
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        pnlButtons.add(new EasyButton(LOCALIZER.getString("OK"), "ok", this));
        pnlButtons.add(new EasyButton(LOCALIZER.getString("CANCEL"), "cancel", this));
        
        rootPane.add(pnlButtons, BorderLayout.PAGE_END);
        rootPane.add(container);
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch (e.getActionCommand())
        {
            case "ok":
                Configuration.overrideInstance(config);
                successful = true;
                this.dispose();
                break;
                
            case "cancel":
                // reset the current language to the unchanged one
                LOCALIZER.setCurrentLanguage(Configuration.getInstance().getLangCode());
                this.dispose();
                break;
        }
    }
    
    
    @Override
    public void itemStateChanged(ItemEvent e)
    {
        if (e.getStateChange() == ItemEvent.SELECTED)
        {
            if (e.getSource().equals(cboLang))
            {
                config.setLangCode(cboLang.getSelectedItem());
                changeLanguage();
            }
            else if (e.getSource().equals(cboShowProperties))
            {
                config.setDblClickShowProperties(cboShowProperties.getSelectedIndex() == 1);
            }
            else if (e.getSource().equals(cboLookAndFeels))
            {
                config.setLookAndFeel(cboLookAndFeels.getSelectedItem().toString());
            }
        }
    }
    
    
    private void changeLanguage()
    {
        LOCALIZER.setCurrentLanguage(config.getLangCode());
        this.setTitle(LOCALIZER.getString("SETTINGS"));
        rootPane.removeAll();
        initializeComponents();
        rootPane.getRootPane().validate();
        rootPane.getRootPane().repaint();
    }
    
    
    public boolean wasSuccessful()
    {
        return successful;
    }
}