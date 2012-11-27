package de.engineapp.xml;

import java.io.*;
import java.util.*;

import javax.xml.parsers.*;
import javax.xml.xpath.*;

import org.w3c.dom.*;
import org.xml.sax.SAXException;


public class XMLReader
{
    Document document = null;
    XPath xPath = null;
    
    private final static List<Element> EMPTY_NODE_LIST = new ArrayList<>();
    
    
    public XMLReader(String xmlFilePath)
    {
        this(new File(xmlFilePath));
    }
    
    public XMLReader(File xmlFile)
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try
        {
            DocumentBuilder builder  = factory.newDocumentBuilder();
                            document = builder.parse(xmlFile);
                            
            XPathFactory xpathfactory = XPathFactory.newInstance();
                                xPath = xpathfactory.newXPath();
        }
        catch (SAXException | IOException | ParserConfigurationException e)
        {
            e.printStackTrace();
        }
    }
    
    
    public XMLReader(InputStream stream)
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try
        {
            DocumentBuilder builder  = factory.newDocumentBuilder();
            document = builder.parse(stream);
            
            XPathFactory xpathfactory = XPathFactory.newInstance();
            xPath = xpathfactory.newXPath();
        }
        catch (SAXException | IOException | ParserConfigurationException e)
        {
            e.printStackTrace();
        }
    }
    
    
    public String getString(String xpath)
    {
        try
        {
            XPathExpression expr = xPath.compile(xpath);
            Object result = expr.evaluate(document, XPathConstants.STRING);
            if (result == null)
            {
                return null;
            }
            else
            {
                return (String) result;
            }
        }
        catch (XPathExpressionException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    
    public List<String> getStrings(String xpath)
    {
        List<String> list = new ArrayList<>();
        
        try
        {
            XPathExpression expr = xPath.compile(xpath);
            Object result = expr.evaluate(document, XPathConstants.NODESET);
            if (result != null)
            {
                NodeList nodeList = (NodeList) result;
                for (int i = 0; i < nodeList.getLength(); i++)
                {
                    list.add(nodeList.item(i).getNodeValue());
                }
            }
        }
        catch (XPathExpressionException e)
        {
            e.printStackTrace();
            return null;
        }
        
        return list;
    }
    
    
    public Element getNode(String xpath)
    {
        try
        {
            XPathExpression expr = xPath.compile(xpath);
            Object result = expr.evaluate(document, XPathConstants.NODE);
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
            XPathExpression expr = xPath.compile(xpath);
            Object result = expr.evaluate(document, XPathConstants.NODESET);
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
                return EMPTY_NODE_LIST;
            }
        }
        catch (XPathExpressionException e)
        {
            e.printStackTrace();
            return EMPTY_NODE_LIST;
        }
    }
}