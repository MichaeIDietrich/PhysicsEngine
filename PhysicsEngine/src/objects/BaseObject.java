package objects;

import math.Vector2d;

abstract class BaseObject {

	protected Vector2d world_coord;
	protected double world_angle;
	
	protected double velocity;
	protected double angular_velocity;
	
	protected double acceleration;
	protected double angular_acceleration;
	
	protected double density;

	public abstract double getVolumne();
	
	public double getWeight() {
		return density * getVolumne();
	}

}
