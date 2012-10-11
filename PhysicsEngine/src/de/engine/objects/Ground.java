package de.engine.objects;

import java.awt.Color;

public class Ground //extends ObjectProperties
{
    public int     watermark;
    public Color surfaceColor;
    public Color   coreColor;
    
    public final int HILLANDVALLEY = 1;
    public final int DOWNHILL      = 2;
    public final int STAIRS        = 3;
   
    public final Color GRASS_GREEN_LIGHT = new Color( 052, 158, 052 );
    public final Color GRASS_GREEN       = new Color( 032, 128, 032 );
    public final Color BITTER_ORANGE     = new Color( 228, 168, 040 );
    public final Color EARTH_BROWN       = new Color( 100, 48, 032 );
    public final Color FREAKY_GREEN      = new Color( 128, 164, 032 );
    public final Color DAAAARK_GREY      = new Color( 20, 20, 20 );
    public final Color LIGHT_BLUE        = new Color( 90, 140, 220 );
    
    
    public Ground(int watermark)
    {
        this.surfaceColor = Color.GRAY;
        this.coreColor   = this.BITTER_ORANGE;
        this.watermark = watermark;
    }
    
    
    public int function( int typ, int i )
    {
        switch( typ ) 
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
        this.surfaceColor = Color.GRAY;
        this.coreColor    = this.BITTER_ORANGE;
        
        // if positive, a hill will drawn; if negative the hill will be a valley
        int phase = -1;
        // what height are you going to go?
        int hill_height = 100;
        
        return (int) (Math.sin(phase * i * Math.PI / 300) * hill_height + hill_height);
    }
    
    
    private int function_downhill(int i)
    {
        this.surfaceColor = this.GRASS_GREEN;
        this.coreColor    = this.FREAKY_GREEN;
        
        // if positive, a hill will drawn; if negative the hill will be a valley
        int phase = -1;
        // what height are you going to go?
        int hill_height = 100;
        // Grass heigth
        int grass_heigth = 4;
        
        return (int) (Math.sin(phase * i * Math.PI / 300) * hill_height + hill_height + grass_heigth*Math.sin(i));
    }
    
    
    private int function_stairs(int i)
    {
        this.surfaceColor = this.DAAAARK_GREY;
        this.coreColor    = this.LIGHT_BLUE;

        // stairs 
        int stairs = 12;
        
        return (int) (-0.3*i - (9*i*i + stairs*i*i*i - i)/(i-150*i*i + 200) + 50 + 4d*Math.sin(0.04*i) );
    }
    
    
    public Ground copy()
    {
     // TODO - add all properties, that need to be copied
        Ground newGround = new Ground(this.watermark);
        // ...
        
        return newGround;
    }
}