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
	
	public double      		 mass = Float.MAX_VALUE;
	public double  		 velocity = 0;
	public double  	     momentum = 0;
	public double     normal_force = 0;
	public double   downhill_force = 0;
	public double   kinetic_energy = 0;
	public double potential_energy = 0;
	public double angular_momentum = 0;
	
	public Point position = null;
	
	public Material surface  = Material.STEEL;
	
	//Funktion für Kollision
	public abstract double getRadius();
}
