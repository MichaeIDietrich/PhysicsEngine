package de.engine.objects;

import de.engine.math.Transformation;
import de.engine.math.Vector;

public abstract class ObjectProperties {
	public enum Material {
		STEEL, ALUMINIUM, NACL, RUBBER
	};

	// will get incremented every time it's used, to apply unique id's to each
	// of the new objects
	public static int idCounter = 0;

	// translate up to which vector?
	public abstract void translation();

	// which fulcrum, about what angle?
	public abstract void rotation();

	public abstract void destroy();

	public Transformation world_position;

	public Vector getPosition() {
		return world_position.translation;
	}

	// TODO forces, velocity, momentum should be a vector, because of their
	// direction
	public double mass = Float.MAX_VALUE;
	public Vector velocity = null;
	public Vector momentum = null;
	public Vector normal_force = null;
	public Vector downhill_force = null;
	public double kinetic_energy = 0;
	public double potential_energy = 0;
	public double angular_momentum = 0;

	// Schwerpunkt relativ zum Objektursprung
	public Vector centroid = null;
	public Vector moving_direction = null;

	public Material surface = Material.STEEL;

	public static int id = 0;

	protected double radius;

	// function for collision
	public abstract double getRadius();

	public Vector[] getAABB() {
		Vector aabb[] = new Vector[2];
		aabb[0] = new Vector(getPosition().getX() - radius, getPosition().getY() - radius);
		aabb[1] = new Vector(getPosition().getX() + radius, getPosition().getY() + radius);
		return aabb;
	}

	protected ObjectProperties() {
		this.id = idCounter++;
	}

	public int getId() {
		return id;
	}

	public abstract ObjectProperties copy();
}
