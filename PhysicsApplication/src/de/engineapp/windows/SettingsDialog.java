package de.engineapp.windows;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import de.engineapp.*;
import de.engineapp.controls.*;
import de.engineapp.util.*;

import static de.engineapp.Constants.*;


/**
 * Dialog for some application settings.
 * 
 * @author Micha
 */
public final class SettingsDialog extends JDialog implements ActionListener, ItemListener
{
    private static final long serialVersionUID = 7034070996690341136L;
    private final static Localizer LOCALIZER = Localizer.getInstance();
    
    
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
        super(parent, LOCALIZER.getString(L_SETTINGS));
        
        config = Configuration.getInstance().clone();
        
        this.setModal(true);
        
        this.setSize(250, 260);
        this.setLocationRelativeTo(parent);
        this.setResizable(false);
        
        rootPane = new JPanel(new BorderLayout());
        initializeComponents();
        this.add(rootPane);
        
        this.setVisible(true);
    }
    
    
    private void initializeComponents()
    {
        VerticalBoxPanel container = new VerticalBoxPanel();
        container.add(Box.createHorizontalStrut(220));
        
        container.addGap(15);
        container.add(new JLabel(LOCALIZER.getString(L_LANGUAGE)));
        container.addGap(3);
        
        cboLang = new IconComboBox<String>(LOCALIZER.getAvailableLanguages(), "flags");
        cboLang.setSelectedItem(config.getProperty(PRP_LANGUAGE_CODE));
        cboLang.setPreferredSize(new Dimension(120, cboLang.getPreferredSize().height));
        cboLang.addItemListener(this);
        
        container.add(cboLang, Component.RIGHT_ALIGNMENT);
        
        
        container.addGap(15);
        container.add(new JLabel(LOCALIZER.getString(L_SHOW_PROPERTIES)));
        container.addGap(3);
        
        cboShowProperties = new JComboBox<String>(new String[] { LOCALIZER.getString(L_OBJECT_SELECTED),
                                                                 LOCALIZER.getString(L_DBLCLICK_OBJECT) });
        cboShowProperties.setFocusable(false);
        cboShowProperties.setSelectedIndex(config.isState(STG_DBLCLICK_SHOW_PROPERTIES) ? 1 : 0);
        cboShowProperties.setPreferredSize(new Dimension(120, cboShowProperties.getPreferredSize().height));
        cboShowProperties.addItemListener(this);
        
        container.add(cboShowProperties, Component.RIGHT_ALIGNMENT);
        
        
        container.addGap(15);
        container.add(new JLabel(LOCALIZER.getString(L_LOOKANDFEEL)));
        container.addGap(3);
        
        
        cboLookAndFeels = new JComboBox<String>(LookAndFeelManager.getLookAndFeelNames());
        cboLookAndFeels.setFocusable(false);
        cboLookAndFeels.setSelectedItem(LookAndFeelManager.getCurrentLookAndFeelName());
        cboLookAndFeels.setPreferredSize(new Dimension(120, cboShowProperties.getPreferredSize().height));
        cboLookAndFeels.addItemListener(this);
        
        container.add(cboLookAndFeels, Component.RIGHT_ALIGNMENT);
        
        
        container.addGap(20);
        container.addSeparator();
        
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        pnlButtons.add(new QuickButton(LOCALIZER.getString(L_OK), CMD_OK, this));
        pnlButtons.add(new QuickButton(LOCALIZER.getString(L_CANCEL), CMD_CANCEL, this));
        
        rootPane.add(pnlButtons, BorderLayout.PAGE_END);
        rootPane.add(container);
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch (e.getActionCommand())
        {
            case CMD_OK:
                Configuration.overrideInstance(config);
                successful = true;
                this.dispose();
                break;
                
            case CMD_CANCEL:
                // reset the current language to the unchanged one
                LOCALIZER.setCurrentLanguage(Configuration.getInstance().getProperty(PRP_LANGUAGE_CODE));
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
                config.setProperty(PRP_LANGUAGE_CODE, cboLang.getSelectedItem());
                changeLanguage();
            }
            else if (e.getSource().equals(cboShowProperties))
            {
                config.setState(STG_DBLCLICK_SHOW_PROPERTIES, cboShowProperties.getSelectedIndex() == 1);
            }
            else if (e.getSource().equals(cboLookAndFeels))
            {
                String lookAndFeel = cboLookAndFeels.getSelectedItem().toString();
                config.setProperty(PRP_LOOK_AND_FEEL, lookAndFeel);
                LookAndFeelManager.applyLookAndFeelByName(lookAndFeel);
                LookAndFeelManager.updateControls(this);
            }
        }
    }
    
    
    private void changeLanguage()
    {
        LOCALIZER.setCurrentLanguage(config.getProperty(PRP_LANGUAGE_CODE));
        this.setTitle(LOCALIZER.getString(L_SETTINGS));
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