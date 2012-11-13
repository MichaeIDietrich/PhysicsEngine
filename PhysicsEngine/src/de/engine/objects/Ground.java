package de.engine.objects;

public class Ground //extends ObjectProperties
{
    public static final int HILLANDVALLEY = 1;
    public static final int DOWNHILL      = 2;
    public static final int STAIRS        = 3;
    
    
    protected int watermark;
    protected int type;
    
    public Ground(int type, int watermark)
    {
        this.type = type;
        this.watermark = watermark;
    }
    
    
    public int function( int i )
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
    private int function_hill_valley(int i)
    {
//        this.surfaceColor = Color.GRAY;
//        this.coreColor    = this.BITTER_ORANGE;
        
        // if positive, a hill will drawn; if negative the hill will be a valley
        int phase = -1;
        // what height are you going to go?
        int hill_height = 100;
        
        return (int) (Math.sin(phase * i * Math.PI / 300) * hill_height + hill_height) + this.watermark;
    }
    
    
    private int function_downhill(int i)
    {
//        this.surfaceColor = this.GRASS_GREEN;
//        this.coreColor    = this.FREAKY_GREEN;
        
        // if positive, a hill will drawn; if negative the hill will be a valley
        int phase = 1;
        // what height are you going to go?
        int hill_height = 100;
        // Grass heigth
        int grass_heigth = 4;
        
        return (int) (Math.sin(phase * i * Math.PI / 360) * hill_height + hill_height + grass_heigth*Math.sin(i)) + this.watermark;
    }
    
    
    private int function_stairs(int i)
    {
//        this.surfaceColor = this.DAAAARK_GREY;
//        this.coreColor    = this.LIGHT_BLUE;

        // stairs 
        int stairs = 12;
        
        return (int) (-0.3*i - (9*i*i + stairs*i*i*i - i)/(i-150*i*i + 200) + 50 + 4d*Math.sin(0.04*i)) + this.watermark;
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