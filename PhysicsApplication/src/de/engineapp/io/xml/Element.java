package de.engineapp.io.xml;

import java.util.*;

import javax.xml.xpath.*;

import org.w3c.dom.*;


/**
 * XML-Node class for easier xml usage.
 * 
 * @author Micha
 */
public final class Element
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
        if (domNode.getChildNodes().getLength() == 1 && domNode.getChildNodes().item(0).getNodeType() == Node.TEXT_NODE)
        {
            return domNode.getTextContent();
        }
        else
        {
            StringBuilder sb = new StringBuilder();
            nodeToText(sb, domNode.getChildNodes());
            
            return xmlTrim(sb.toString());
        }
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
    
    
    public Element getNode(String xpath)
    {
        try
        {
            XPathFactory xpathfactory = XPathFactory.newInstance();
            XPath xPath = xpathfactory.newXPath();
            XPathExpression expr = xPath.compile(xpath);
            Object result = expr.evaluate(domNode, XPathConstants.NODE);
            if (result != null)
            {
                return new Element((Node) result);
            }
            else
            {
                return null;
            }
        }
        catch (XPathExpressionException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    
    public List<Element> getNodes(String xpath)
    {
        try
        {
            XPathFactory xpathfactory = XPathFactory.newInstance();
            XPath xPath = xpathfactory.newXPath();
            XPathExpression expr = xPath.compile(xpath);
            Object result = expr.evaluate(domNode, XPathConstants.NODESET);
            if (result != null)
            {
                NodeList nodeList = (NodeList) result;
                List<Element> list = new ArrayList<>();
                
                for (int i = 0; i < nodeList.getLength(); i++)
                {
                    list.add(new Element(nodeList.item(i)));
                }
                
                return list;
            }
            else
            {
                return XMLReader.EMPTY_NODE_LIST;
            }
        }
        catch (XPathExpressionException ex)
        {
            ex.printStackTrace();
            return XMLReader.EMPTY_NODE_LIST;
        }
    }
    
    
    private static void nodeToText(StringBuilder sb, NodeList nodeList)
    {
        for (int i = 0; i < nodeList.getLength(); i++)
        {
            Node node = nodeList.item(i);
            
            if (node.getNodeType() == Node.TEXT_NODE)
            {
                sb.append(node.getTextContent());
            }
            else
            {
                if (node.hasChildNodes())
                {
                    sb.append("<" + node.getNodeName());
                    appendAttributes(sb, node);
                    sb.append(">");
                    
                    nodeToText(sb, node.getChildNodes());
                    
                    sb.append("</" + node.getNodeName() + ">");
                }
                else
                {
                    sb.append("<" + node.getNodeName());
                    appendAttributes(sb, node);
                    sb.append("/>");
                }
            }
        }
    }
    
    
    private static void appendAttributes(StringBuilder sb, Node node)
    {
        for (int i = 0; i < node.getAttributes().getLength(); i++)
        {
            Node attr = node.getAttributes().item(i);
            sb.append(" " + attr.getNodeName() + "=\"" + attr.getTextContent() + "\"");
        }
    }
    
    
    private static String xmlTrim(String text)
    {
        int start = -1, end = text.length();
        
        while (start < text.length() && isWhitespace(text.charAt(++start)));
        while (end > -1 && isWhitespace(text.charAt(--end)));
        
        if (end != -1)
        {
            return text.substring(start, end + 1);
        }
        else
        {
            return "";
        }
    }
    
    
    private static boolean isWhitespace(char c)
    {
        return c == ' ' || c == '\t' || c == '\r' || c == '\n';
    }
}