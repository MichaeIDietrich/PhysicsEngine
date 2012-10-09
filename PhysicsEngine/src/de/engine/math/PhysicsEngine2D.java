package de.engine.math;


import de.engine.environment.Scene;


public class PhysicsEngine2D implements Runnable
{
    private Scene  scene;
    public boolean semaphore = true;

    
    public PhysicsEngine2D()
    {
        
    }
    
    
	public void Rotation()
	{
		
	}
	
	
	public void Translation()
	{
		
	}

	
	public void setScene( Scene scene )
	{
	    this.scene = scene;
	}

    
    @Override
    public void run()
    {
        // Will be changed soon!
        while( true ) 
        {
            System.out.println( "running..." );
            
            while( semaphore ) 
            {      
                try
                {
                    // do a break for 1/30 second
                    Thread.sleep( 33 );
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                
                // Checks collision between circles and ground
                if (scene.getGround()!=null) 
                {
//                    System.out.println( scene.getGround().function( scene.getGround().DOWNHILL, e.getLocation().x ) +" | "+ e.getLocation().y );
                }
            }
            
            try
            {
                // do a another break for 1/30 second
                Thread.sleep( 33 );
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            
            
            // Collision detection
            
            // Repaint
        }
    }
    
}
