package de.engineapp;

public class Task
{
    private boolean done = false;
    private long ms;
    
    
    public Task(long milliseconds)
    {
        ms = milliseconds;
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
            }
        }.start();
    }
    
    
    public boolean isDone()
    {
        return done;
    }
}