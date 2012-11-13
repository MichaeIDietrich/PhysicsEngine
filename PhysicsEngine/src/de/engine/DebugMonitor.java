package de.engine;

import java.util.*;

public final class DebugMonitor
{
    public interface MessageListener
    {
        public void messageUpdated(String name, String message);
    }
    
    
    private static DebugMonitor instance = null;
    
    private List<MessageListener> listeners;
    
    
    public static DebugMonitor getInstance()
    {
        if (instance == null)
        {
            instance = new DebugMonitor();
        }
        
        return instance;
    }
    
    
    private DebugMonitor()
    {
        listeners = new ArrayList<>();
    }
    
    
    private void fireUpdatesMessages(String name, String message)
    {
        for (MessageListener listener : listeners)
        {
            listener.messageUpdated(name, message);
        }
    }
    
    
    public void updateMessage(String name, String message)
    {
        fireUpdatesMessages(name, message);
    }
    
    
    public void removeMessage(String name)
    {
        fireUpdatesMessages(name, null);
    }
    
    
    public void addMessageListener(MessageListener listener)
    {
        listeners.add(listener);
    }
    
    
    public void removeMessageListener(MessageListener listener)
    {
        listeners.remove(listener);
    }
    
    
    public MessageListener[] getMessageListeners()
    {
        return listeners.toArray(new MessageListener[listeners.size()]);
    }
}