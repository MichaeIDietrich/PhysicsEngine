package de.engineapp.controls;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.View;

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
    HtmlViewer label;
    
    
    public Expander(String text, String doc)
    {
        label = new HtmlViewer(doc);
        
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
        
        label.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        label.setMaximumSize(new Dimension(200, 10000));
        
        
        this.add(button, BorderLayout.PAGE_START);
    }
    
    
    public void setWidth(int width)
    {
        super.validate();
        View view = (View) label.getClientProperty(BasicHTML.propertyKey);
        view.setSize(width, 0.0f);
        float w = view.getPreferredSpan(View.X_AXIS);
        float h = view.getPreferredSpan(View.Y_AXIS);
        label.setSize((int) w, (int) h);
        label.validate();
        label.repaint();
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (button.isSelected())
        {
            this.add(label);
        }
        else
        {
            this.remove(label);
        }
        this.getTopLevelAncestor().validate();
        this.getTopLevelAncestor().repaint();
    }
}