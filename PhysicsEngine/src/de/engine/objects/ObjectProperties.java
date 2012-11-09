package de.engine.objects;

import de.engine.environment.EnvProps;
import de.engine.math.Transformation;
import de.engine.math.Util;
import de.engine.math.Vector;

public abstract class ObjectProperties 
{
    public enum Material 
    {
		STEEL, ALUMINIUM, NACL, RUBBER
	};

	private boolean updated;
	
	// will get incremented every time it's used, to apply unique id's to each
	// of the new objects
	public static int idCounter = 0;

	// translate up to which vector?
	public abstract void translation();

	// which fulcrum, about what angle?
	public abstract void rotation();

	public abstract void destroy();

	public Transformation world_position;

	public double last_intersection;
	
	public Vector getPosition() {
		return world_position.translation;
	}

	public Vector getPosition(double time) {
		return Util.add(world_position.translation, new Vector(
				(velocity.getX() * time), ((EnvProps.grav_acc()
						/ 2d * time + velocity.getY()) * time)));
	}

	public Vector getNextPosition() {
		return Util.add(world_position.translation,
				new Vector((velocity.getX() * EnvProps.deltaTime()),
						((EnvProps.grav_acc() / 2d
								* EnvProps.deltaTime() + velocity.getY())
								* EnvProps.deltaTime())));
	}

	// TODO forces, velocity, momentum should be a vector, because of their
	// direction
	public double mass = Float.MAX_VALUE;
	public Vector velocity = null;
	public double angular_velocity;
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

	public int id = 0;

	protected double radius;

	// function for collision
	public abstract double getRadius();
	public abstract void setRadius(double radius);

	public Vector[] getAABB() {
		Vector aabb[] = new Vector[2];
		aabb[0] = new Vector(getPosition().getX() - radius, getPosition()
				.getY() - radius);
		aabb[1] = new Vector(getPosition().getX() + radius, getPosition()
				.getY() + radius);
		return aabb;
	}
	
	public abstract Vector[] getAABB(double time);

	public abstract Vector[] getNextAABB();

	protected ObjectProperties() {
		this.id = idCounter++;
	}

	public int getId() {
		return id;
	}
	
	public void update() {
		if(updated)
			updated = false;
		else {
			world_position.translation = getNextPosition();
			//obj.getPosition().setY(
			    //	-9.81 / 2d * deltaTime + obj.velocity.getY()
			    //		* deltaTime + obj.getPosition().getY());
			velocity.add(0, EnvProps.grav_acc() / 2d * EnvProps.deltaTime());
			
			// calc potential energy: Epot = m*g*h  (mass * grav_const * y-coordinate)
			potential_energy = -mass * EnvProps.grav_acc() * world_position.translation.getY();
			
			// calc kinetic energy: Epot = m/2*v²  (mass * grav_const * y-coordinate)
		}
	}
	
	public void update(double time) {
		updated = true;
		world_position.translation = getPosition(time);
		velocity.add(0, EnvProps.grav_acc() / 2d * time);
	}

	public abstract ObjectProperties copy();

    public abstract boolean contains(double x, double y);
}
