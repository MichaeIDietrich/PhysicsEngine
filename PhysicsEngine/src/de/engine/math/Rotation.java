package de.engine.math;

public class Rotation {
	
	private double angle, cos, sin;

	public Rotation(double angle) {
		setAngle(angle);
	}
	
	public double getAngle() {
		return angle;
	}
	
	public void setAngle(double angle) {
		this.angle = angle;
		cos = Math.cos(angle);
		sin = Math.sin(angle);
	}
	
	public Matrix getMatrix() {
		return new Matrix(cos, -1 * sin, sin, cos);
	}
}
