package de.engineapp.windows;

import static de.engineapp.Constants.CMD_OK;
import static de.engineapp.Constants.ICO_MAIN_256;
import static de.engineapp.Constants.L_ABOUT;
import static de.engineapp.Constants.L_ABOUT_TEXT;
import static de.engineapp.Constants.L_OK;
import static de.engineapp.Constants.MAJOR;
import static de.engineapp.Constants.MINOR;
import static de.engineapp.Constants.REVISION;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import de.engineapp.controls.QuickButton;
import de.engineapp.util.GuiUtil;
import de.engineapp.util.Localizer;

public final class AboutDialog extends JDialog implements ActionListener
{
    private static final long serialVersionUID = -666810517612909816L;
    
    private final static Localizer LOCALIZER = Localizer.getInstance();
    
    
    public AboutDialog(Window parent)
    {
        super(parent);
        
        this.setModal(true);
        
        this.setTitle(LOCALIZER.getString(L_ABOUT));
        
        JLabel label = new JLabel(GuiUtil.getIcon(ICO_MAIN_256));
        label.setBorder(new EmptyBorder(5, 5, 5, 10));
        label.setText(String.format(LOCALIZER.getString(L_ABOUT_TEXT), MAJOR, MINOR, REVISION));
        
        
        QuickButton okButton = new QuickButton(LOCALIZER.getString(L_OK), CMD_OK, this);
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.add(okButton);
        
        this.add(label);
        this.add(panel, BorderLayout.PAGE_END);
        
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.pack();
        
        this.setLocationRelativeTo(parent);
        
        this.setVisible(true);
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        this.dispose();
    }
}