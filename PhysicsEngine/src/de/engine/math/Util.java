package de.engine.math;

import de.engine.environment.Scene;
import de.engine.objects.Ground;
import de.engine.objects.ObjectProperties;

public class Util 
{
    private Scene scene;
    private static de.engine.math.Vector iterx = new de.engine.math.Vector();
    private static de.engine.math.Vector     x = new de.engine.math.Vector();
    private static de.engine.math.Matrix    jm = new de.engine.math.Matrix();
    

	public static double distance(Vector p1, Vector p2) 
	{
		double x = p2.getX() - p1.getX();
		double y = p2.getY() - p1.getY();
		return Math.hypot(x, y);
	}

	public static double distanceToOrigin(Vector p) 
	{
		return Math.hypot(p.getX(), p.getY());
	}
	
	public static Vector add(Vector vec1, Vector vec2) 
	{
		return new Vector(vec1.getX() + vec2.getX(), vec1.getY() + vec2.getY());
	}

	public static Vector minus(Vector vec1, Vector vec2) {
		return new Vector(vec1.getX() - vec2.getX(), vec1.getY() - vec2.getY());
	}
	
	public static Vector scale(Vector v, double s)
    {
	    return new Vector(v.getX() * s,  v.getY() * s);
    }
	
	public static double scalarProduct(Vector vec1, Vector vec2) 
	{
		return vec1.getX() * vec2.getX() + vec1.getY() * vec2.getY();
	}
	
    public static double getAngle( Vector vec1, Vector vec2 )
    {
        return Math.atan2( vec2.getY()-vec1.getY(), vec2.getX()-vec1.getX() );
    }
    
    
    public static Double newtonIteration( ObjectProperties object, Ground ground )
    {
        double xn = object.getPosition().getX();
        
        for(int i=0; i < 3; i++) 
        {
            xn = xn - getFunctionsValue( xn, object, ground) / derive1D( xn, object, ground);
        } 
        return xn;
    }
    
    
    public static Double derive1D( Double x, ObjectProperties object, Ground ground )
    {
        Double h = Math.pow(10d, 12d);

        // Berechne df(x) = ( f(x + 1/h ) - f(x - 1/h) ) * h/2
        Double storevalue = (getFunctionsValue( x+h, object, ground) - getFunctionsValue( x-h, object, ground)) * 2d/h;

        return storevalue;
    }
    
    
    public static Double getFunctionsValue( Double x, ObjectProperties object, Ground ground ) 
    {
        Vector function = new Vector();
        
        // m = slope, n = shift (in y), linear function 
        double m = object.velocity.getY()/object.velocity.getX();
        double n = object.getPosition().getY();

        function.set(   0,           m * (x-object.getPosition().getX()) + n                ); 
        function.set(   1,  (double) ground.function( ground.ACTUAL_FUNCTION, x.intValue()) );
        
        return function.get(1) - function.get(0);            
    }
}
