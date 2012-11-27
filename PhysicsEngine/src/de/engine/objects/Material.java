package de.engine.objects;

public enum Material
{
    STEEL(0.9), ALUMINIUM(0.6), NACL(0.4), RUBBER(1), WATER(0.05), WOOD(0.3);
    
    private final double elasticity;
    
    private Material(double elasticity)
    {
        this.elasticity = elasticity;
    }
    
    public double elasticity()
    {
        return elasticity;
    }
}
