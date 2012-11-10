package de.engineapp;

import java.io.*;
import java.util.*;

import de.engineapp.xml.*;

public final class Localizer
{
    private static class XMLFilter implements FilenameFilter
    {
        @Override
        public boolean accept(File dir, String name)
        {
            return name.endsWith(".xml");
        }
    }
    
    
    private static Localizer instance = null;
    
    private String currentLanguageCode;
    private Map<String, String> stringTable;
    
    
    private Localizer()
    {
        stringTable = new HashMap<>();
        
        currentLanguageCode = Configuration.getInstance().getLangCode();
        if (currentLanguageCode == null)
        {
            currentLanguageCode = Locale.getDefault().toLanguageTag();
            Configuration.getInstance().setLangCode(currentLanguageCode);
        }
        
        File file = getLanguage(currentLanguageCode);
        loadLanguageFile(file);
    }
    
    
    public static Localizer getInstance()
    {
        if (instance == null)
        {
            instance = new Localizer();
        }
        
        return instance;
    }
    
    
    public File getLanguage(String langCode)
    {
        File langFile = new File("data/i18n/" + langCode + ".xml");
        if (langFile.exists())
        {
            //System.out.println("loaded '" + langCode + "'");
            return langFile;
        }
        else
        {
            langFile = new File("data/i18n/en-US.xml");
            if (langFile.exists())
            {
                return langFile;
            }
            else
            {
                System.err.println("Language-File 'data/i18n/en-US.xml' not found!");
            }
        }
        
        return null;
    }
    
    
    private void loadLanguageFile(File file)
    {
        if (file != null)
        {
            XMLReader reader = new XMLReader(file);
            
            stringTable.clear();
            
            for (Element entry : reader.getNodes("Localization/String"))
            {
                String id = entry.getAttribute("id");
                
                stringTable.put(id, entry.getValue());
            }
            System.out.println("loaded: " + file.getName());
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
        File langDir = new File("data/i18n");
        
        if (langDir.exists() && langDir.isDirectory())
        {
            for (File file : langDir.listFiles(new XMLFilter()))
            {
                languages.add(file.getName().substring(0, file.getName().lastIndexOf('.')));
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
        File file = getLanguage(currentLanguageCode);
        loadLanguageFile(file);
    }
}