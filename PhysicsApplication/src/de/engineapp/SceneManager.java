package de.engineapp;

import java.awt.*;
import java.io.*;
import java.util.zip.*;

import javax.swing.*;

import de.engine.environment.Scene;
import de.engine.math.*;
import de.engine.objects.ObjectProperties;
import de.engine.objects.ObjectProperties.Material;
import de.engineapp.util.Localizer;
import de.engineapp.visual.*;
import de.engineapp.xml.*;

import static de.engineapp.Constants.*;

public final class SceneManager
{
    private final static Localizer LOCALIZER = Localizer.getInstance();
    
    private PresentationModel pModel;
    private Component topLevelComponent;
    
    
    public SceneManager(PresentationModel model, Component topLevelComponent)
    {
        pModel = model;
        this.topLevelComponent = topLevelComponent;
    }
    
    
    public Scene loadScene(File file)
    {
        if (file.exists() && file.isFile())
        {
            if (isXmlFile(file))
            {
                try
                {
                    FileInputStream stream = new FileInputStream(file);
                    Scene scene = loadSceneFromStream(stream);
                    stream.close();
                    
                    return scene;
                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                }
            }
            else
            {
                try
                {
                    ZipFile zipFile = new ZipFile(file);
                    ZipEntry entry = zipFile.getEntry("document.xml");
                    
                    if (entry != null)
                    {
                        InputStream stream = zipFile.getInputStream(entry);
                        Scene scene = loadSceneFromStream(stream);
                        stream.close();
                        zipFile.close();
                        
                        return scene;
                    }
                    zipFile.close();
                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                }
            }
        }
        
        return null;
    }
    
    
    public void saveScene(File file, Scene scene)
    {
        try
        {
            FileOutputStream fileStream = new FileOutputStream(file);
            ZipOutputStream zipStream = new ZipOutputStream(fileStream);
            
            ZipEntry entry = new ZipEntry("document.xml");
            zipStream.putNextEntry(entry);
            
            saveSceneToStream(zipStream, scene);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
    
    
    private static boolean isXmlFile(File file)
    {
        try
        {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(fis, "UTF-8");
            
            char[] buffer = new char[5];
            reader.read(buffer);
            reader.close();
            
            return buffer[0] == '<' && buffer[1] == '?' && buffer[2] == 'x' && buffer[3] == 'm' && buffer[4] == 'l';
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        
        return false;
    }
    
    
    private Scene loadSceneFromStream(InputStream stream)
    {
        XMLReader reader = new XMLReader(stream);
        
        Element node = reader.getNode("Scene");
        
        if (node != null)
        {
            String version = node.getAttribute("version");
            
            if (!FILE_VERSION.equals(version))
            {
                String[] options = new String[] { LOCALIZER.getString(L_YES), LOCALIZER.getString(L_NO) };
                
                if (JOptionPane.showOptionDialog(topLevelComponent, LOCALIZER.getString(L_WRONG_VERSION), 
                        LOCALIZER.getString(L_TITLE_IMPORT), JOptionPane.YES_NO_OPTION, 
                        JOptionPane.WARNING_MESSAGE, null, options, options[0]) != 0)
                {
                    return null;
                }
            }
            
            Scene scene = new Scene();
            
            ObjectProperties object = null;
            node = reader.getNode("Scene/Ground");
            
            if (node != null)
            {
                scene.setGround(new Ground(pModel, getInt(node.getAttribute("type")), getInt(node.getAttribute("watermark"))));
            }
            
            for (Element obj : reader.getNodes("Scene/Circle | Scene/Square"))
            {
                switch (obj.getName())
                {
                    case "Circle":
                        object = new Circle(pModel, new Vector(getDouble(obj.getAttribute("x")), getDouble(obj.getAttribute("y"))), 
                                getDouble(obj.getAttribute("radius")));
                        break;
                        
                    case "Square":
                        object = new Square(pModel, new Vector(getDouble(obj.getAttribute("x")), getDouble(obj.getAttribute("y"))), 
                                getDouble(obj.getAttribute("radius")));
                        break;
                        
                    default:
                        continue;
                }
                
                ((ISelectable) object).setName(obj.getAttribute("name"));
                ((IDrawable) object).setColor(new Color(getInt(obj.getAttribute("color"))));
                object.setMass(getDouble(obj.getAttribute("mass")));
                object.velocity = new Vector(getDouble(obj.getAttribute("vx")), getDouble(obj.getAttribute("vy")));
                object.setRotationAngle(getDouble(obj.getAttribute("rotation")));
                
                String strMat = obj.getAttribute("material");
                for (Material mat : Material.values())
                {
                    if (mat.toString().equals(strMat))
                    {
                        object.surface = mat;
                        break;
                    }
                }
                
                object.isPinned = "true".equals(obj.getAttribute("pinned"));
                
                scene.add(object);
            }
            
            return scene;
        }
        
        return null;
    }
    
    
    private void saveSceneToStream(OutputStream stream, Scene scene)
    {
        XMLWriter writer = new XMLWriter(stream);
        
        writer.writeDeclaration();
        writer.writeStartElement("Scene");
        writer.writeAttribute("version", FILE_VERSION);
        writer.writeAttribute("gravitation", "9.81"); // missing property
        
        if (scene.getGround() != null)
        {
            writer.writeStartElement("Ground");
            writer.writeAttribute("type", "" + scene.getGround().getType());
            writer.writeAttribute("watermark", "" + scene.getGround().getWatermark());
            writer.writeEndElement();
        }
        
        for (ObjectProperties object : scene.getObjects())
        {
            if (object instanceof Circle)
            {
                writer.writeStartElement("Circle");
            }
            else if (object instanceof Square)
            {
                writer.writeStartElement("Square");
            }
            
            writer.writeAttribute("name", ((ISelectable) object).getName());
            writer.writeAttribute("color", "" + ((IDrawable) object).getColor().getRGB());
            writer.writeAttribute("material", "" + object.surface);
            writer.writeAttribute("x", "" + object.getPosition().getX());
            writer.writeAttribute("y", "" + object.getPosition().getY());
            writer.writeAttribute("vx", "" + object.velocity.getX());
            writer.writeAttribute("vy", "" + object.velocity.getY());
            writer.writeAttribute("mass", "" + object.getMass());
            writer.writeAttribute("radius", "" + object.getRadius());
            writer.writeAttribute("rotation", "" + object.getRotationAngle());
            writer.writeAttribute("pinned", object.isPinned ? "true" : "false");
            
            writer.writeEndElement();
        }
        
        writer.writeEndElement();
        writer.close();
    }
    
    
    private double getDouble(String value)
    {
        if (value == null)
        {
            return 0.0;
        }
        
        return Double.parseDouble(value);
    }
    
    
    private int getInt(String value)
    {
        if (value == null)
        {
            return 0;
        }
        
        return Integer.parseInt(value);
    }
}