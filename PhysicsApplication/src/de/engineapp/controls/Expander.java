package de.engineapp.controls;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Expander extends JPanel implements ActionListener
{
    private static class HtmlViewer extends JLabel
    {
        private static final long serialVersionUID = 8787130155299083869L;
        
        
        public HtmlViewer(String doc)
        {
            super(doc);
            this.setBackground(Color.WHITE);
            this.setOpaque(true);
        }
    }
    
    
    private static final long serialVersionUID = 4480221797736558685L;
    
    
    JToggleButton button;
    JComponent component;
    
    
    public Expander(String text, String doc)
    {
        this(text, new HtmlViewer(doc));
    }
    
    public Expander(String text, JComponent expandableComponent)
    {
        this.setLayout(new BorderLayout());
        
        button = new JToggleButton(text)
        {
            private static final long serialVersionUID = -3330376265192275758L;
            
            @Override
            public void paint(Graphics g)
            {
                super.paint(g);
                
                if (this.isSelected())
                {
                    g.drawString("▼", this.getWidth() - 20, 15);
                }
                else
                {
                    g.drawString("■", this.getWidth() - 20, 15);
                }
            }
        };
        
        button.setFocusPainted(false);
        button.addActionListener(this);
        
        component = expandableComponent;
        component.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        this.add(button, BorderLayout.PAGE_START);
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (button.isSelected())
        {
            this.add(component);
            this.getTopLevelAncestor().validate();
            this.getTopLevelAncestor().repaint();
            this.setMaximumSize(this.getPreferredSize());
        }
        else
        {
            this.remove(component);
            this.getTopLevelAncestor().validate();
            this.getTopLevelAncestor().repaint();
            this.setMaximumSize(this.getPreferredSize());
        }
    }
}