package de.engineapp.util;

import java.io.*;
import java.util.*;

import de.engineapp.Configuration;
import de.engineapp.io.xml.*;

import static de.engineapp.Constants.*;

public final class Localizer
{
    private static Localizer instance = null;
    
    private String currentLanguageCode;
    private Map<String, String> stringTable;
    
    
    private Localizer()
    {
        stringTable = new HashMap<>();
        
        currentLanguageCode = Configuration.getInstance().getProperty(PRP_LANGUAGE_CODE);
        if (currentLanguageCode == null)
        {
            currentLanguageCode = Locale.getDefault().toLanguageTag();
            Configuration.getInstance().setProperty(PRP_LANGUAGE_CODE, currentLanguageCode);
        }
        
        String resource = getLanguage(currentLanguageCode);
        loadLanguageResource(resource);
    }
    
    
    public static Localizer getInstance()
    {
        if (instance == null)
        {
            instance = new Localizer();
        }
        
        return instance;
    }
    
    
    public String getLanguage(String langCode)
    {
        String langResource = "i18n/" + langCode + ".xml";
        if (GuiUtil.resourceExists(langResource))
        {
            return langResource;
        }
        else
        {
            langResource = "i18n/en-US.xml";
            if (GuiUtil.resourceExists(langResource))
            {
                return langResource;
            }
            else
            {
                System.err.println("Language-File 'i18n/en-US.xml' not found!");
            }
        }
        
        return null;
    }
    
    
    private void loadLanguageResource(String path)
    {
        InputStream stream = GuiUtil.getResource(path);
        
        if (stream != null)
        {
            XMLReader reader = new XMLReader(stream);
            
            stringTable.clear();
            
            for (Element entry : reader.getNodes("Localization/String"))
            {
                String id = entry.getAttribute("id");
                
                stringTable.put(id, entry.getValue());
            }
        }
    }
    
    
    public String getString(String id)
    {
        String entry = stringTable.get(id);
        
        if (entry != null)
        {
            return entry;
        }
        
        return "@" + id;
    }
    
    
    public String[] getAvailableLanguages()
    {
        List<String> languages = new ArrayList<>();
        String langDir = "i18n";
        
        if (GuiUtil.resourceExists(langDir))
        {
            for (String res : GuiUtil.getResources(langDir))
            {
                if (res.endsWith(".xml"))
                {
                    languages.add(res.substring(0, res.lastIndexOf('.')));
                }
            }
        }
        
        return languages.toArray(new String[languages.size()]);
    }
    
    
    public String getCurrentLanguage()
    {
        return currentLanguageCode;
    }
    
    
    public void setCurrentLanguage(String langCode)
    {
        currentLanguageCode = langCode;
        String resource = getLanguage(currentLanguageCode);
        loadLanguageResource(resource);
    }
}