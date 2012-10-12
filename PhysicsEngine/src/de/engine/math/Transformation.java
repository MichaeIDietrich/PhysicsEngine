package de.engine.math;

public class Transformation {
	
	public Vector translation;
	public Rotation rotation;
	
	public Transformation(Vector trans, Rotation rot) {
		translation = trans;
		rotation = rot;
	}
	
	public Vector getPostion(Vector vec) {
		Vector rot = rotation.getMatrix().multVector(vec);
		return new Vector(rot.getX() + translation.getX(),
				rot.getY() + translation.getY());
	}
}
