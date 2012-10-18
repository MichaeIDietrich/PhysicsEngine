package de.engine.math;

public class Matrix {
	
	double d11, d12, d21, d22;

	public Matrix() {
	}

	public Matrix(double d11, double d12, double d21, double d22) {
		this.d11 = d11;
		this.d12 = d12;
		this.d21 = d21;
		this.d22 = d22;
	}
	
	public Vector multVector(Vector vec) {
		return new Vector(d11 * vec.getX() + d12 * vec.getY(),
				d21 * vec.getX() + d22 * vec.getY());
	}
}
