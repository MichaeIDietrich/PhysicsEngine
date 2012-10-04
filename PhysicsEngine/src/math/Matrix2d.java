package math;

public class Matrix2d {
	
	private double d[][];

	public Matrix2d() {
		d = new double[2][2];
	}

	public Matrix2d(double d11, double d12, double d21, double d22) {
		d = new double[2][2];
		d[1][1] = d11;
		d[1][2] = d12;
		d[2][1] = d21;
		d[2][2] = d22;
	}
	
	public double get(int u, int v) {
		return d[u][v];
	}

	/**
	 * Gibt Rotationsmatrix um den Ursprung (0.0d, 0.0d) zurück.
	 * Das heißt bei einer Rotation muss zur Berechnung der Körper
	 * in seinen lokalen Posionen erst zum Schwerpunkt als Ursprung verschoben werden
	 * und danach wieder Zurück.
	 */
	public static Matrix2d getRotationMatrix(double angle) {
		return new Matrix2d(Math.cos(angle), -1.0d * Math.sin(angle), Math.sin(angle), Math.cos(angle));
	}
	
	public Vector2d mult(Vector2d v) {
		return new Vector2d(d[1][1] * v.getX() + d[1][2] * v.getY(), d[2][1] * v.getX() + d[2][2] * v.getY());
	}
}
