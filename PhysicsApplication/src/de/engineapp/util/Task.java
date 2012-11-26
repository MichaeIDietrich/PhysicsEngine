package de.engineapp.util;


/**
 * Util class to implement delayed actions easily.
 * 
 * @author Micha
 * @author Cartsten
 */
public final class Task
{
    private boolean done = false;
    private long ms;
    private Runnable callback;
    
    
    public Task(long milliseconds)
    {
        ms = milliseconds;
        callback = null;
    }
    
    
    public Task(long milliseconds, Runnable callback)
    {
        ms = milliseconds;
        this.callback = callback;
    }
    
    
    public void start()
    {
        new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    Thread.sleep(ms);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                
                done = true;
                
                if (callback != null)
                {
                    callback.run();
                }
            }
        }.start();
    }
    
    
    public boolean isDone()
    {
        return done;
    }
}