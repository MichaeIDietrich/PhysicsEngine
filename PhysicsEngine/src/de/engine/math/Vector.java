package de.engine.math;

import java.text.MessageFormat;

public class Vector implements Cloneable
{
    public static final Integer ZEILENSUMMENNORM = 10; 
    public static final Integer EUKLIDISCHENORM  = 11;
    
    public static final Vector UNDEFINED = new Vector(Double.MAX_VALUE, Double.MAX_VALUE);
    
    private boolean calcLength;
    private double length;
    
    private double x;
    private double y;
  
    public Vector()
    {
        x = 0.0;
        y = 0.0;
        
        calcLength = true;
        length = 0.0;
    }
    
    public Vector(double x, double y)
    {
        this.x = x;
        this.y = y;
        
        calcLength = false;
    }
    
    public Vector(Vector vector)
    {
        this(vector.getX(), vector.getY());
    }
    
    public double getX()
    {
        return x;
    }
    
    public double getY()
    {
        return y;
    }
    
    public void setX(double x)
    {
        this.x = x;
        calcLength = false;
        
    }
    
    public void setY(double y)
    {
        this.y = y;
        calcLength = false;
    }
    
    public void setPoint(double x, double y)
    {
        this.x = x;
        this.y = y;
        calcLength = false;
    }
    
    public double getLength()
    {
        if (!calcLength)
        {
            length = Util.distanceToOrigin(this);
            calcLength = true;
        }
        return length;
    }
    
    public Vector add(Vector v)
    {
        this.x += v.x;
        this.y += v.y;
        calcLength = false;
        return this;
    }
    
    public Vector add(double x, double y)
    {
        this.x += x;
        this.y += y;
        calcLength = false;
        return this;
    }
    
    public Vector minus(Vector v)
    {
        this.x -= v.x;
        this.y -= v.y;
        calcLength = false;
        return this;
    }
    
    public Vector minus(double x, double y)
    {
        this.x -= x;
        this.y -= y;
        calcLength = false;
        return this;
    }
    
    public Vector scale(double s)
    {
        x *= s;
        y *= s;
        return this;
    }
    
    public Vector getNormalVector()
    {
        return new Vector(-1 * y, x);
    }
    
    public Vector getUnitVector()
    {
        double scale = 1 / getLength();
        return Util.scale(this, scale);
    }
    
    
    
    
    // An other form calculating with vectors -- maybe rewrite the old variant?
    // import from numeric project is major essential for jakobiMatrix calculation
    public Double get(int index)
    {
        if(index == 0)
            return x;
        else if(index == 1)
            return y;
        else
            return null;
    }
    
    public void set(int index, Double value)
    {
        if(index == 0)
            x = value;
        else if(index == 1)
            y = value;
    }
    
    public Double norm( Integer n ) throws RuntimeException 
    {
        if( n==10 ) return zsnorm();
        if( n==11 ) return eunorm();
        
        throw new RuntimeException( MessageFormat.format("'MathLib.getNorm()' liefert den Wert {0}, für welche es keine Normimplementierung für Vektoren gibt.", n ));
    }
    
    private Double zsnorm() 
    {
        Double sum = 0d;
        Double max = 0d;
        sum = Math.abs(x);
        if ( max.compareTo( sum ) == -1 ) max = sum;
        sum = Math.abs(y);
        if ( max.compareTo( sum ) == -1 ) max = sum;
        return max;
    }
    
    
    private Double eunorm() 
    {
        return getLength();
        /*Double euklidNorm = 0d;
        Double        sum = 0d;
        
        for(int i=0; i < 2; i++) sum = sum + get(i)*get(i);
        
        euklidNorm = Math.sqrt( sum );
        
        return euklidNorm;*/
    }
    
    
    public Vector setUnitVector(Vector vector) 
    {
        vector.x = 1;
        vector.y = 1;
        
        return vector;
    }
    
    
    @Override
    public Vector clone()
    {
        Vector copy = new Vector(this.x, this.y);
        
        copy.calcLength = this.calcLength;
        copy.length = this.length;
        
        return copy;
    }
    
    
    public Double[] toDoubleArray() 
    {
        Double x[] = new Double[2];
        x[0] = this.x;
        x[1] = this.y;
        
        return x;
    }
    
    
    public Vector subtract(Vector vector)
    {
        return addit( vector.multi( -1d ));
    }
    
    
    public Vector multi( Double value)
    {
        return new Vector(this.x * value, this.y * value);
    }
    
    public Vector addit(Vector vector)
    {
        return new Vector(this.x + vector.x, this.y + vector.y);
    }
    
    /**
     * Tauscht die Reihen eines Vektors durch zwei Indizes
     * @param rowIndex1 erster Index
     * @param rowIndex2 zweiter Index
     */
    public Vector swapRows(int rowIndex1, int rowIndex2)
    {
        Vector tempVector = clone();
        
        Double tempBigDecimal = get(rowIndex1);
        tempVector.set(rowIndex1, get(rowIndex2));
        tempVector.set(rowIndex2, tempBigDecimal);
        
        return tempVector;
    }
}