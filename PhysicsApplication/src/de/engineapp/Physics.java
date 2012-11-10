package de.engineapp;

import static de.engineapp.Constants.*;

public class Physics
{
    // interface to recognize repaints caused by finishing of frame calculation
    public interface FinishedCallback
    {
        public void done();
    }
    
    
    private PresentationModel pModel;
    private long deltaTime;
    private FinishedCallback finishedCallback;
    
    private Thread worker = null;
    
    
    public Physics(PresentationModel model, long deltaTime, FinishedCallback finishedCallback)
    {
        pModel = model;
        this.deltaTime = deltaTime;
        this.finishedCallback = finishedCallback;
    }
    
    
    public void start()
    {
        
        
        worker = new Thread()
        {
            // necessary to count frames per secound
            long fpsCounter = 0;
            long timeCounter = 0;
            
            
            @Override
            public void run()
            {
                while (!this.isInterrupted())
                {
                    long t = System.currentTimeMillis();
                    
                    pModel.getPhysicsEngine2D().calculateNextFrame( deltaTime/1000d );
                    finishedCallback.done();
                    
                    
                    t = System.currentTimeMillis() - t;
                    pModel.setProperty(CALCULATE_TIME, "" + t);
                    
                    try
                    {
                        // don't waste cpu power, so sleep rest of the time
                        Thread.sleep(t < deltaTime ? deltaTime - t : 0);
                    }
                    catch (InterruptedException e)
                    {
                        // may occur, but should be no problem, we just ignore it
                        //e.printStackTrace();
                        
                        // finally we need to set the interrupt flag again, because 
                        // it was rejected by the exception
                        this.interrupt();
                    }
                    
                    
                    fpsCounter++;
                    if (System.currentTimeMillis() >= timeCounter)
                    {
                        timeCounter = System.currentTimeMillis() + 1000;
                        pModel.setProperty(FPS, "" + fpsCounter);
                        fpsCounter = 0;
                    }
                }
                
                pModel.setProperty(FPS, "0");
            }
        };
        
        worker.start();
    }
    
    
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