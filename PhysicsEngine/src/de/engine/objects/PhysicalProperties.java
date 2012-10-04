package de.engine.objects;

import de.engine.math.Point;

public abstract class PhysicalProperties 
{
	public enum Material {STEEL, ALUMINIUM, NACL, RUBBER};
	
	// translate up to which vector?
	public abstract void translation();
	
	// which fulcrum, about what angle?
	public abstract void rotation();

	
	public abstract void destroy();
	
	public float      		 mass = Float.MAX_VALUE;
	public float  		 velocity = 0;
	public float  	     momentum = 0;
	public float     normal_force = 0;
	public float   downhill_force = 0;
	public float   kinetic_energy = 0;
	public float potential_energy = 0;
	public float angular_momentum = 0;
	
	public Point position = null;
	
	public Material surface  = Material.STEEL;
}
