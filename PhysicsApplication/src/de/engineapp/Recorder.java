package de.engineapp;

import java.util.*;

import de.engine.environment.Scene;
//import de.engine.objects.ObjectProperties;
//import de.engine.objects.Ground;

public final class Recorder
{
    private static Recorder instance = null;
    
    
    private List<Scene> frames;
    
    
    public static Recorder getInstance()
    {
        if (instance == null)
        {
            instance = new Recorder();
        }
        
        return instance;
    }
    
    
    private Recorder()
    {
        frames = new ArrayList<>();
    }
    
    
    public int getFrameCount()
    {
        return frames.size();
    }
    
    
    public void addFrame(Scene scene)
    {
        frames.add(scene.clone());
    }
    
    
    public Scene getFrame(int index)
    {
        return frames.get(index);
    }
    
    
    public void shrinkToFrame(int index)
    {
        while (frames.size() > index + 1)
        {
            frames.remove(frames.size() - 1);
        }
    }
    
    
    public void clear()
    {
        frames.clear();
    }
}