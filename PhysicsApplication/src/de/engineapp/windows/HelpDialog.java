package de.engineapp.windows;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

import de.engineapp.controls.*;
import de.engineapp.util.*;

import static de.engineapp.Constants.*;


/**
 * Dialog that provides html documentation about the usage of this application.
 * @author Micha
 */
public final class HelpDialog extends JDialog
{
    private static final long serialVersionUID = -5599465139356687661L;
    
    private final static Localizer LOCALIZER = Localizer.getInstance();
    
    private static boolean opened;
    
    private List<DocumentExpander> topics;
    private JPanel container;
    private JScrollPane scrollPane;
    
    
    public HelpDialog(Window parent)
    {
        super(parent);
        
        opened = true;
        
        this.setTitle(LOCALIZER.getString(L_HELP));
        
        this.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                opened = false;
                HelpDialog.this.dispose();
            }
        });
        
        this.setSize(600, 400);
        this.setLocationRelativeTo(parent);
        
        initializeComponents();
        
        this.addComponentListener(new ComponentAdapter()
        {
            @Override
            public void componentResized(ComponentEvent e)
            {
                for (DocumentExpander expander : topics)
                {
                    expander.setWidth(scrollPane.getViewport().getWidth() - 10);
                }
            }
        });
        
        this.setVisible(true);
    }
    
    
    private void initializeComponents()
    {
        container = new JPanel(new GridBagLayout());
        topics = new ArrayList<>();
        
        addTopics(topics);
        
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.weightx = 1;
        constraints.weighty = 0;
        
        for (DocumentExpander expander : topics)
        {
            constraints.gridy = container.getComponentCount();
            
            container.add(expander, constraints);
        }
        
        constraints.gridy = container.getComponentCount();
        constraints.weighty = 1;
        container.add(new JLabel(), constraints);
        
        
        scrollPane = new JScrollPane(container, 
                                     JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
                                     JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        
        this.add(scrollPane);
    }
    
    
    public static boolean isAlreadyOpened()
    {
        return opened;
    }
    
    
    private String getDocument(String resourceName, String... images)
    {
        try
        {
            InputStream is = GuiUtil.getResource(resourceName);
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            
            StringBuilder builder = new StringBuilder();
            String line;
            
            while ((line = br.readLine()) != null)
            {
                builder.append(line);
            }
            
            br.close();
            
            if (images == null)
            {
                return builder.toString();
            }
            else
            {
                return String.format(builder.toString(), (Object[]) images);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        
        return null;
    }
    
    
    // currently German only (inlined)
    private void addTopics(List<DocumentExpander> topics)
    {
        topics.add(new DocumentExpander("Steuerleiste", getDocument("doc/toolbar.html", 
                GuiUtil.getHtmlImage(ICO_NEW), GuiUtil.getHtmlImage(ICO_OPEN), GuiUtil.getHtmlImage(ICO_SAVE),
                GuiUtil.getHtmlImage(ICO_PLAY), GuiUtil.getHtmlImage(ICO_PAUSE), GuiUtil.getHtmlImage(ICO_RESET),
                GuiUtil.getHtmlImage(ICO_GRID), GuiUtil.getHtmlImage(ICO_OBJECT_ARROWS), GuiUtil.getHtmlImage(ICO_FOCUS),
                GuiUtil.getHtmlImage(ICO_BLANK), GuiUtil.getHtmlImage(ICO_BLANK),
                GuiUtil.getHtmlImage(ICO_PHYSICS), GuiUtil.getHtmlImage(ICO_RECORD), GuiUtil.getHtmlImage(ICO_PLAYBACK),
                GuiUtil.getHtmlImage(ICO_SETTINGS), GuiUtil.getHtmlImage(ICO_HELP), GuiUtil.getHtmlImage(ICO_ABOUT))));
        topics.add(new DocumentExpander("Objektbereich", getDocument("doc/object-panel.html", 
                GuiUtil.getHtmlImage(ICO_SQUARE), GuiUtil.getHtmlImage(ICO_CIRCLE), GuiUtil.getHtmlImage(ICO_GROUND))));
        topics.add(new DocumentExpander("Objekteigenschaftsbereich", String.format(getDocument("doc/object-properties-panel.html"))));
        topics.add(new DocumentExpander("Statusleiste", getDocument("doc/statusbar.html", 
                GuiUtil.getHtmlImage(ICO_DISCARD))));
        topics.add(new DocumentExpander("Szenebereich", getDocument("doc/scene.html")));
        topics.add(new DocumentExpander("Unterstützte Funktionen", getDocument("doc/supported-features.html")));
        topics.add(new DocumentExpander("Noch nicht unterstützte Funktionen", getDocument("doc/unsupported-features.html")));
    }
}