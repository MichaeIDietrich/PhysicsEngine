package de.engineapp.rec;

import de.engineapp.PresentationModel;
import de.engineapp.util.AsyncWorker;

import static de.engineapp.Constants.*;


/**
 * Class for playing back recorded animation.
 * 
 * @author Micha
 */
public final class Playback extends AsyncWorker
{
    private PresentationModel pModel;
    private long deltaTime;
    
    
    public Playback(final PresentationModel model, int deltaTime)
    {
        pModel = model;
        this.deltaTime = deltaTime;
    }
    
    
    @Override
    public void start()
    {
        worker = new Thread()
        {
            @Override
            public void run()
            {
                while (!this.isInterrupted())
                {
                    int currentFrame = Integer.parseInt(pModel.getProperty(PRP_CURRENT_PLAYBACK_FRAME));
                    
                    if (++currentFrame >= Recorder.getInstance().getFrameCount())
                    {
                        currentFrame = 1;
                    }
                    pModel.setProperty(PRP_CURRENT_PLAYBACK_FRAME, "" + currentFrame);
                    
                    try
                    {
                        Thread.sleep(deltaTime);
                    }
                    catch (InterruptedException e)
                    {
                        // may occur, but should not be a problem, we just ignore it
                        //e.printStackTrace();
                        
                        // finally we need to set the interrupt flag again, because 
                        // it was rejected by the exception
                        this.interrupt();
                    }
                }
            }
        };
        
        worker.start();
    }
}