package de.engine.objects;

import de.engine.objects.ObjectProperties.Material;

public class Ground //extends ObjectProperties
{
    public enum Material
    {
        STEEL(1), ALUMINIUM(1), NACL(1), RUBBER(1), WATER(1);
        
        private final double elasticity;
        
        private Material(double elasticity)
        {
            this.elasticity = elasticity;
        }
        
        public double elasticity()
        {
            return elasticity;
        }
    };
    
    public static final int HILLANDVALLEY = 1;
    public static final int DOWNHILL      = 2;
    public static final int STAIRS        = 3;
    public Material surface  = Material.STEEL;
    
    protected int watermark;
    protected int type;
    
    
    public Ground(int type, int watermark)
    {
        this.type = type;
        this.watermark = watermark;
    }
    
    public double function( double i )
    {
        switch( type ) 
        {
            case 1: return function_hill_valley(i);
            case 2: return function_downhill(i);
            case 3: return function_stairs(i);
            default: return 0;
        }
    }
    
    
    // TODO - will be replaced by something dynamic later
    private double function_hill_valley(double i)
    {
//        this.surfaceColor = Color.GRAY;
//        this.coreColor    = this.BITTER_ORANGE;
        
        // if positive, a hill will drawn; if negative the hill will be a valley
        int phase = -1;
        // what height are you going to go?
        int hill_height = 100;
        
        return Math.sin(phase * i * Math.PI / 300) * hill_height + hill_height + this.watermark;
    }
    
    
    private double function_downhill(double i)
    {
//        this.surfaceColor = this.GRASS_GREEN;
//        this.coreColor    = this.FREAKY_GREEN;
        
        // if positive, a hill will drawn; if negative the hill will be a valley
        int phase = 1;
        // what height are you going to go?
        int hill_height = 100;
        // Grass heigth
        int grass_heigth = 4; 
        
        return hill_height * ( Math.sin(phase * i * Math.PI / 360) + 1) + grass_heigth * Math.sin(i) + this.watermark;
    }
    
    
    private double function_stairs(double i)
    {
//        this.surfaceColor = this.DAAAARK_GREY;
//        this.coreColor    = this.LIGHT_BLUE;

        // stairs 
        int stairs = 12;
        
        double ii = i*i;
        return -0.3*i - (9*i + stairs*i*ii - i)/(i-150d*ii + 200d) * 3d*Math.sin(0.006*i) + this.watermark;
    }
    
    
    @Override
    public Ground clone()
    {
        // TODO - add all properties, that need to be copied
        Ground newGround = new Ground(this.type, this.watermark);
        // ...
        
        return newGround;
    }
    
    
    public int getType()
    {
        return type;
    }
    
    public void setType(int type)
    {
        this.type = type;
    }
    
    
    public int getWatermark()
    {
        return watermark;
    }
    
    public void setWatermark(int watermark)
    {
        this.watermark = watermark;
    }
}