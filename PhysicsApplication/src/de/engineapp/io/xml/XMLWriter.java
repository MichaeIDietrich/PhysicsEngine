package de.engineapp.io.xml;

import java.io.*;
import java.util.Stack;


/**
 * Easy xml writer class.
 * 
 * @author Micha
 */
public final class XMLWriter
{
    private enum Elements { NONE, DECLARATION, OPEN_TAG, CLOSED_TAG, STRING, ATTRIBUTE }
    
    private final static String INDENTATION = "  ";
    
    
    private BufferedWriter xmlWriter;
    private int level;
    private Elements lastElement;
    private Stack<String> elementStack;
    
    
    public XMLWriter(String filePath)
    {
        this(new File(filePath));
    }
    
    
    public XMLWriter(File file)
    {
        try
        {
            init(new FileOutputStream(file));
        }
        catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
        }
    }
    
    
    public XMLWriter(OutputStream stream)
    {
        init(stream);
    }
    
    
    private void init(OutputStream stream)
    {
        try
        {
            OutputStreamWriter osw = new OutputStreamWriter(stream, "UTF-8");
            xmlWriter = new BufferedWriter(osw);
        }
        catch (UnsupportedEncodingException ex)
        {
            ex.printStackTrace();
        }
        
        level = 0;
        lastElement = Elements.NONE;
        elementStack = new Stack<>();
    }
    
    
    public void writeDeclaration()
    {
        try
        {
            xmlWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            lastElement = Elements.DECLARATION;
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
    
    
    public void writeStartElement(String name)
    {
        try
        {
            if (lastElement == Elements.OPEN_TAG || lastElement == Elements.ATTRIBUTE)
            {
                xmlWriter.write(">\n");
            }
            else if (lastElement != Elements.STRING)
            {
                xmlWriter.write("\n");
            }
            
            indent();
            
            xmlWriter.write("<" + name);
            elementStack.add(name);
            level++;
            lastElement = Elements.OPEN_TAG;
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
    
    
    public void writeEndElement()
    {
        try
        {
            level--;
            if (lastElement == Elements.OPEN_TAG || lastElement == Elements.ATTRIBUTE)
            {
                xmlWriter.write(" />");
                elementStack.pop();
            }
            else
            {
                if (lastElement != Elements.STRING)
                {
                    xmlWriter.write("\n");
                    indent();
                }
                
                xmlWriter.write("</" + elementStack.pop() + ">");
            }
            lastElement = Elements.CLOSED_TAG;
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
    
    
    public void writeAttribute(String name, String value)
    {
        try
        {
            xmlWriter.write(" " +  name + "=\"" + value + "\"");
            lastElement = Elements.ATTRIBUTE;
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
    
    
    public void writeString(String string)
    {
        try
        {
            xmlWriter.write(string);
            lastElement = Elements.STRING;
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
    
    
    public void writeEnclosedString(String elementName, String string)
    {
        try
        {
            if (lastElement != Elements.STRING)
            {
                xmlWriter.write("\n");
                indent();
            }
            
            xmlWriter.write("<" + elementName + ">" + string + "</" + elementName + ">");
            lastElement = Elements.CLOSED_TAG;
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
    
    
    public void flush()
    {
        try
        {
            xmlWriter.flush();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
    
    
    public void close()
    {
        try
        {
            xmlWriter.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
    
    
    private void indent()
    {
        try
        {
            for (int i = 0; i < level; i++)
            {
                xmlWriter.write(INDENTATION);
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
}