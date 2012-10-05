package de.engine.objects;

import de.engine.math.Vector;


public abstract class ObjectProperties 
{
	public enum Material {STEEL, ALUMINIUM, NACL, RUBBER};
	
	// translate up to which vector?
	public abstract void translation();
	
	// which fulcrum, about what angle?
	public abstract void rotation();

	public abstract void destroy();
	
	// TODO forces, velocity, momentum should be a vector, because of their direction
	public double      		  mass = Float.MAX_VALUE;
	public Vector  		  velocity = null;
	public Vector  	      momentum = null;
	public Vector     normal_force = null;
	public Vector   downhill_force = null;
	public double   kinetic_energy = 0;
	public double potential_energy = 0;
	public double angular_momentum = 0;
	
	public Vector position 		   = null;
	public Vector moving_direction = null;
	
	public Material surface = Material.STEEL;
	
	public static int id = 0;
	
	// function for collision
	public abstract double getRadius();
}
