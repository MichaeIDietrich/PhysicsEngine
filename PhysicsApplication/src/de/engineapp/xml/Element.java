package de.engineapp.xml;

import java.util.*;

import org.w3c.dom.Node;

public class Element
{
    private Node domNode;
    
    public Element(Node domNode)
    {
        this.domNode = domNode;
    }
    
    public String getName()
    {
        return domNode.getNodeName();
    }
    
    public String getValue()
    {
        return domNode.getTextContent();
    }
    
    public int getAttributeCount()
    {
        return domNode.getAttributes().getLength();
    }
    
    public String getAttribute(String name)
    {
        Node attribute = domNode.getAttributes().getNamedItem(name);
        if (attribute == null)
        {
            return null;
        }
        else
        {
            return attribute.getNodeValue();
        }
    }
    
    public Map<String, String> getAttibutes()
    {
       Map<String, String> attributes = new HashMap<>();
        
        for (int i = 0; i < domNode.getAttributes().getLength(); i++)
        {
            Node attribute = domNode.getAttributes().item(i);
            attributes.put(attribute.getNodeName(), attribute.getNodeValue());
        }
        
        return attributes;
    }
}
