package de.engine.math;

import de.engine.environment.Scene;
import de.engine.objects.Ground;
import de.engine.objects.ObjectProperties;

public class Util 
{
    private Scene scene;
    
    public void Util(Scene scene)
    {
        this.scene = scene;
    }
    
	public static double distance(Vector p1, Vector p2) {
		double x = p2.getX() - p1.getX();
		double y = p2.getY() - p1.getY();
		return Math.hypot(x, y);
	}

	public static double distanceToOrigin(Vector p) {
		return Math.hypot(p.getX(), p.getY());
	}
	
	public static Vector add(Vector vec1, Vector vec2) {
		return new Vector(vec1.getX() + vec2.getX(), vec1.getY() + vec2.getY());
	}

	public static Vector minus(Vector vec1, Vector vec2) {
		return new Vector(vec1.getX() - vec2.getX(), vec1.getY() - vec2.getY());
	}
	
	public static Vector scale(Vector v, double s)
    {
	    return new Vector(v.getX() * s,  v.getY() * s);
    }
	
	public static double scalarProduct(Vector vec1, Vector vec2) {
		return vec1.getX() * vec2.getX() + vec1.getY() * vec2.getY();
	}
	
    public static double getAngle( Vector vec1, Vector vec2 )
    {
        return Math.atan2( vec2.getY()-vec1.getY(), vec2.getX()-vec1.getX() );
    }
    
    
    // Begin #### Test ####
    public static Vector solveNonLEQ( ObjectProperties object, Ground ground )
    {
        Vector iterx = new Vector();
        iterx.set(0, 0d);
        iterx.set(1, 0d);
        Vector     x = new Vector();
        Matrix    jm = new Matrix();
        int        i = 1;
                   x = x.setUnitVector(x);
                   
        // Abbruchbedingung 'obereschranke' bei x.norm() > 2^(-30) > eps
        Double obereschranke = Double.MIN_VALUE;

        while (( x.norm( Vector.ZEILENSUMMENNORM )).compareTo( obereschranke )==1) 
        {
            i++;

            try
            {
                x = jm.jakobiMatrix( derive(iterx, object, ground) ).solveX( getFunctionsValue(iterx, object, ground) );
                iterx = iterx.addit( x );    
            } 
            catch(ArithmeticException e) {}

            // Greater 50? Have a break! 
            if (i>50) break;
        }

        return iterx;
    }
    
    
    /**
     * Partielles Ableiten aller Funktionen in getFunctionsValue() und speichern
     * aller Werte in einer Matrix, der Jakobimatrix. Der Parameter Vector dient 
     * als Referenz für die Größe der Jakobimatrix und gleichzeitig als Container
     * für die Stelle der Ableitung. 
     * @param vector
     * @return Matrix
     */
    public static Matrix derive( Vector vector, ObjectProperties object, Ground ground )
    {
        Double[] x = new Double[2];
        Double   h = Math.pow(10d, 10d);
        Vector storevalue = new Vector();
        
        for(int i=0; i < 2; i++) x[i]=vector.get(i); 

        Matrix dfunction = new Matrix();
        
        Vector  dqPlus = new Vector();
        Vector dqMinus = new Vector();
        
        for(int i=0; i < 2; i++)
        {
            for(int row=0; row < 2; row++) 
            {
                if (row==i) 
                {
                     dqPlus.set(i, x[i] + 1d/h );
                    dqMinus.set(i, x[i] - 1d/h );
                } 
                else 
                {
                     dqPlus.set(row, 1.23456789123456789);  // Sollte Zufallszahl sein bzw. eine Zahl die keine
                    dqMinus.set(row, 1.23456789123456789);  // Asymptote der unten eingegebenen Funktionen ist!
                } 
            }

            // Berechne df(x,y) = ( f(x + 1/h ) - f(x - 1/h) ) * h/2
            storevalue = getFunctionsValue(dqPlus, object, ground).subtract( getFunctionsValue(dqMinus, object, ground));

            for(int t=0; t < 2; t++) 
            {
                storevalue.set(t, -storevalue.get(t) * h/2d );
                 dfunction.set(t, i, storevalue.get(t));
            }
        }
        return dfunction;
    }
    
    
    /**
     * Trage hier alle nicht-linearen Gleichungssysteme ein.
     * @param vector
     * @return Vector
     */
    public static Vector getFunctionsValue(Vector vector, ObjectProperties object, Ground ground) 
    {
        Vector function = new Vector();
        Double[]      x = vector.toDoubleArray();   // x[0] = x ; x[1] = y ; x[2] = z usw.
        
        //Anstieg
        double m = object.velocity.getY()/object.velocity.getX();
        double n = object.world_position.translation.getY();
        
        // Hier die >> Funktionen << eintragen:
        function.set(   0,  -( m * (x[0]-object.world_position.translation.getX()) + n  +x[1])); 
        function.set(   1,  -( (double) ground.function( ground.ACTUAL_FUNCTION, x[0].intValue()) +x[1]));
        
        return function;            
    }
}
