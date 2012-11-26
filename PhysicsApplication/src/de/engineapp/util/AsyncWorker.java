package de.engineapp.util;


/**
 * Class interface for threaded processes.
 * 
 * @author Micha
 */
public abstract class AsyncWorker
{
    protected Thread worker;
    
    
    public abstract void start();
    
    
    public void pause()
    {
        if (worker != null && worker.isAlive())
        {
            worker.interrupt();
        }
    }
    
    
    public boolean isRunning()
    {
        return worker != null && worker.isAlive();
    }
}