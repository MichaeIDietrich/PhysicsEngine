package de.engineapp.windows;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

import de.engineapp.controls.*;
import de.engineapp.util.*;

import static de.engineapp.Constants.*;

public class HelpDialog extends JDialog
{
    private static final long serialVersionUID = -5599465139356687661L;
    
    private final static Localizer LOCALIZER = Localizer.getInstance();
    
    
    public HelpDialog(Window parent)
    {
        super(parent);
        this.setTitle(LOCALIZER.getString(L_HELP));
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        this.setSize(600, 400);
        this.setLocationRelativeTo(parent);
        
        initializeComponents();
        
        this.setVisible(true);
    }
    
    
    private void initializeComponents()
    {
        JPanel viewPort = new JPanel(new BorderLayout());
        Box container = Box.createVerticalBox();
        
        List<Expander> topics = new ArrayList<>();
        addTopics(topics);
        
        for (Expander topic : topics)
        {
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(topic, BorderLayout.PAGE_START);
            container.add(panel);
        }
        
        viewPort.add(container, BorderLayout.PAGE_START);
        JScrollPane scrollPane = new JScrollPane(viewPort, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        this.add(scrollPane);
    }
    
    
    private String getDocument(String resourceName)
    {
        try
        {
            InputStream is = GuiUtil.getResource(resourceName);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            
            StringBuilder builder = new StringBuilder();
            String line;
        
            while ((line = br.readLine()) != null)
            {
                builder.append(line);
            }
            
            br.close();
            
            return builder.toString();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        
        return null;
    }
    
    
    private void addTopics(List<Expander> topics)
    {
        // TODO - add all topics
        topics.add(new Expander("titel", getDocument("doc/topic1.html")));
        topics.add(new Expander("titel", getDocument("doc/topic1.html")));
        topics.add(new Expander("titel", getDocument("doc/topic1.html")));
    }
}