package de.engineapp.controls;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.engine.environment.Scene;
import de.engine.math.Transformation;
import de.engine.math.Vector;
import de.engine.objects.Ground;
import de.engine.objects.ObjectProperties;
import de.engine.objects.ObjectProperties.Material;
import de.engineapp.PresentationModel;
import de.engineapp.PresentationModel.SceneListener;
import de.engineapp.visual.ISelectable;

public class PropertiesPanel extends VerticalBoxPanel implements SceneListener, ActionListener, ChangeListener
{
    private static final long serialVersionUID = 8656904964293251249L;
    
    private PresentationModel pModel;
    
    //Label erstellen
    private JLabel nameLabel;
    private JLabel materialLabel;
    private JLabel xCordinateLabel;
    private JLabel yCordinateLabel;
    private JLabel xSpeedLabel;
    private JLabel ySpeedLabel;
    private JLabel massLabel;
    
    private JLabel LabelPotE;
    private JLabel LabelKinE;
    
    //Buttons erstellen
    private JButton del;
    private JButton close;
    
    //Namensfeld erstellen
    private JTextField name;
    
    //ComboBox und CheckBox erstellen
    private JCheckBox fix;
    
    private javax.swing.JComboBox<Material> MaterialCombo;
    
    //Spinner erstellen
    private SpinnerNumberModel snmMasse;
    private SpinnerNumberModel snmX;
    private SpinnerNumberModel snmY;
    private SpinnerNumberModel snmVx;
    private SpinnerNumberModel snmVy;
    
    private PropertySpinner massInput;
    private PropertySpinner xCord;    
    private PropertySpinner yCord;    
    private PropertySpinner vx;       
    private PropertySpinner vy;       
    
    private JPanel lp;
    
    //Variablen
    private String objectName;
    
    public PropertiesPanel(PresentationModel model)
    {
        pModel = model;
        
        //Labels
        nameLabel         = new JLabel("Name des Objektes");
        materialLabel     = new JLabel("Objektmaterial");
        xCordinateLabel   = new JLabel("x-Koordinate");
        yCordinateLabel   = new JLabel("y-Koordinate");
        xSpeedLabel       = new JLabel("vx in m/s");
        ySpeedLabel       = new JLabel("vy in m/s");
        massLabel        = new JLabel("Masse in kg");
        
        LabelPotE         = new JLabel("Potentielle Energie: ");
        LabelKinE         = new JLabel("kinetische Energie: ");

        //Buttons
        del          = new JButton("Entfernen");
        close        = new JButton("Schließen");
        
        //Namensfeld
        name         = new JTextField();
        
        //Combobox + CheckBox
        fix          = new JCheckBox("fixiert");


         //+++++++++++++++++++++++++++++++++++++++++++++++++//
        //================= Konfiguration =================//
       //+++++++++++++++++++++++++++++++++++++++++++++++++//
        
        //Buttons
          del.addActionListener(this);
        close.addActionListener(this);
          del.setActionCommand("del");
        close.setActionCommand("close");
        
        //Namensfeld konfigurieren
        name.setEditable(isEnabled());
        name.setToolTipText("Objektname");
        name.addFocusListener(new FocusAdapter()
            {
                @Override
                public void focusGained(FocusEvent e)
                {
                    name.selectAll();
                }
            }
        );
        
        String objectName   = new String(name.getText());
        
        
        this.setBorder( BorderFactory.createBevelBorder( BevelBorder.RAISED ) );
        

        pModel.addSceneListener(this);
        
    }

    @Override
    public void objectAdded(ObjectProperties object)
    {
        // TODO Auto-generated method stub
        
    }


    @Override
    public void objectRemoved(ObjectProperties object)
    {
        // TODO Auto-generated method stub
        
    }


    @Override
    public void groundAdded(Ground ground)
    {
        // TODO Auto-generated method stub
        
    }


    @Override
    public void groundRemoved(Ground ground)
    {
        // TODO Auto-generated method stub
        
    }


    @Override
    public void objectSelected(ObjectProperties object)
    {
        System.out.println("Objekt ausgewählt" + " " + this);
        massInput    = new PropertySpinner(object.mass,1,1000,1,this);
        xCord        = new PropertySpinner(object.getPosition().getX(),-100000.0,100000,10,this);
        yCord        = new PropertySpinner(object.getPosition().getY(),-100000,100000,10,this);
        vx           = new PropertySpinner(object.velocity.getX(),-1000,1000,10,this);
        vy           = new PropertySpinner(object.velocity.getY(),-1000,1000,10,this);
        
        System.out.println("---MINUS");
        
        double pot = object.potential_energy;
        double kin = object.kinetic_energy;
        
        MaterialCombo = new JComboBox<Material>(Material.values());
//        MaterialCombo - mit Objektoberfläche initiieren??

        massInput.setValue(object.mass);
        xCord.setValue(object.getPosition().getX()); 
        yCord.setValue(object.getPosition().getY());
        vx.setValue(object.velocity.getX());
        vy.setValue(object.velocity.getY());

        name.setText(((ISelectable)object).getName());
        
        
//        Material sf = object.surface;
        
        
        //Hinzufügen
        
        this.add(nameLabel, CENTER_ALIGNMENT);
        this.addGroup(5,name);
        
        this.add(del, RIGHT_ALIGNMENT);
        
        this.add(materialLabel);
        this.add(MaterialCombo);
        this.addGroup(5, xCordinateLabel, yCordinateLabel);
        this.addGroup(5, xCord, yCord);
        
        this.addGroup(5, xSpeedLabel,ySpeedLabel);
        this.addGroup(5, vx, vy);
        
        this.add(massLabel, LEFT_ALIGNMENT);
        this.add(massInput, LEFT_ALIGNMENT);
        
        this.add(fix, LEFT_ALIGNMENT);
        
        this.addGroup(5,LabelPotE, new JLabel(String.valueOf(pot)));
        this.addGroup(5,LabelKinE, new JLabel(String.valueOf(kin)));
        
        this.add(close, RIGHT_ALIGNMENT);
        

        this.updateUI();
        this.setVisible(true);
        System.out.println();
        
    }


    @Override
    public void objectDeselected(ObjectProperties object)
    {
        System.out.println("Objekt abgewählt");
        this.setVisible(false);
        this.removeAll();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch(e.getActionCommand())
        {
            case "del":
                pModel.removedObject(pModel.getSelectedObject());
                pModel.fireRepaintEvents();
                break;
                
            case "close":
                this.setVisible(false);
                this.removeAll();
                break;
        }
        
    }

    @Override
    public void stateChanged(ChangeEvent e)
    {
        System.out.println("Grüner Adler, flieg!");
        System.out.println(xCord.getValue().getClass());
        pModel.getSelectedObject().world_position.translation.setX(xCord.getValue());
        pModel.getSelectedObject().world_position.translation.setY(yCord.getValue());
        pModel.getSelectedObject().velocity.setX(vx.getValue());
        pModel.getSelectedObject().velocity.setY(vy.getValue());
        pModel.getSelectedObject().mass = massInput.getValue();
        
        pModel.fireRepaintEvents();
//        massInput.setValue(massInput.getValue());
//        xCord.setValue(xCord.getValue());
//        yCord.setValue(yCord.getValue());
//        vx.setValue(vx.getValue());
//        vy.setValue(vy.getValue());
//        this.updateUI();
    }

    @Override
    public void sceneUpdated(Scene scene)
    {
    }

}