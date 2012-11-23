package de.engineapp.rec;

import java.util.*;

import de.engine.environment.Scene;


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
    
    
    public static Recorder newInstance()
    {
        return new Recorder();
    }
    
    
    public static void overrideInstance(Recorder newInstance)
    {
        instance = newInstance;
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