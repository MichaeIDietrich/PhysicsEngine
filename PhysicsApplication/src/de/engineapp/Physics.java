package de.engineapp;

import de.engine.DebugMonitor;
import de.engineapp.util.AsyncWorker;

public class Physics extends AsyncWorker
{
    // interface to recognize repaints caused by finishing of frame calculation
    public interface FinishedCallback
    {
        public void done();
    }
    
    
    private PresentationModel pModel;
    private long deltaTime;
    private FinishedCallback finishedCallback;
    
    
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
                    
                    pModel.getPhysicsEngine2D().calculateNextFrame( deltaTime / 1000d );
                    
                    // real time that the calculation needed
                    long diffTime = System.currentTimeMillis() - t;
                    DebugMonitor.getInstance().updateMessage("calc", "" + diffTime);
                    
                    finishedCallback.done();
                    
                    // calculation time + repaint time (repaint will now use 
                    // another thread, so it does not matter anymore here)
                    diffTime = System.currentTimeMillis() - t;
                    
                    try
                    {
                        // don't waste cpu power, so sleep rest of the time
                        Thread.sleep(diffTime < deltaTime ? deltaTime - diffTime : 0);
                    }
                    catch (InterruptedException e)
                    {
                        // may occur, but should not be a problem, we just ignore it
                        //e.printStackTrace();
                        
                        // finally we need to set the interrupt flag again, because 
                        // it was rejected by the exception
                        this.interrupt();
                    }
                    
                    
                    fpsCounter++;
                    if (System.currentTimeMillis() >= timeCounter)
                    {
                        timeCounter = System.currentTimeMillis() + 1000;
                        DebugMonitor.getInstance().updateMessage("FPS", "" + fpsCounter);
                        fpsCounter = 0;
                    }
                    
                    DebugMonitor.getInstance().updateMessage("all", "" + (System.currentTimeMillis() - t));
                }
                
                DebugMonitor.getInstance().updateMessage("FPS", "0");
            }
        };
        
        worker.start();
    }
}