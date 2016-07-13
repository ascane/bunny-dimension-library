package bunny.structure;

import java.util.Arrays;

import bunny.util.U;

public class Vector {
	private double[] values;
	public Vector(int dimension) {
		values = new double[dimension];
	}
	public Vector(double... values) {
		this.values = values;
	}
	
	public double get(int d) {
		return values[d];
	}
	public void set(int d, double value) {
		values[d] = value;
	}
	
	public int dim() {
		return values.length;
	}
	
	public double length() {
		return Math.sqrt(lengthSquared());
	}
	public double lengthSquared() {
		double result = 0;
		for (int i = 0; i < values.length; i++) {
			result += values[i] * values[i];
		}
		return result;
	}
	
	public Vector normalized() {
		double l = length();
		if (l > 0) {
			return this.times(1/l);
		} else {
			Vector result = new Vector(dim());
			result.set(0, 1);
			return result;
		}
	}
	public double normalize() {
		double l = length();
		if (l > 0) {
			this.timesEquals(1/l);
		} else {
			this.set(0, 1);
		}
		return l;
	}
	
	public Vector plus(Vector other) {
		Vector result = new Vector(values.length);
		for (int i = 0; i < values.length; i++) {
			result.set(i, get(i) + other.get(i));
		}
		return result;
	}
	public void plusEquals(Vector other) {
		for (int i = 0; i < values.length; i++) {
			values[i] += other.get(i);
		}
	}
	
	public Vector minus(Vector other) {
		Vector result = new Vector(values.length);
		for (int i = 0; i < values.length; i++) {
			result.set(i, get(i) - other.get(i));
		}
		return result;
	}
	public void minusEquals(Vector other) {
		for (int i = 0; i < values.length; i++) {
			values[i] -= other.get(i);
		}
	}
	
	public Vector times(double k) {
		Vector result = new Vector(values.length);
		for (int i = 0; i < values.length; i++) {
			result.set(i, get(i) * k);
		}
		return result;
	}
	public void timesEquals(double k) {
		for (int i = 0; i < values.length; i++) {
			values[i] *= k;
		}
	}
	
	public Vector times(Vector other) {
		Vector result = new Vector(values.length);
		for (int i = 0; i < values.length; i++) {
			result.set(i, get(i) * other.get(i));
		}
		return result;
	}
	public void timesEquals(Vector other) {
		for (int i = 0; i < values.length; i++) {
			values[i] *= other.get(i);
		}
	}
	
	public double dot(Vector other) {
		double result = 0;
		for (int i = 0; i < values.length; i++) {
			result += get(i) * other.get(i);
		}
		return result;
	}
	
	public Vector cross(Vector other) {
		Vector result = new Vector(3);
		result.set(0, get(1) * other.get(2) - get(2) * other.get(1));
		result.set(1, get(2) * other.get(0) - get(0) * other.get(2));
		result.set(2, get(0) * other.get(1) - get(1) * other.get(0));
		return result;
	}
	
	public Vector project(Vector direction) {
		double l = direction.lengthSquared();
		return direction.times(this.dot(direction) / l);
	}
	public Vector projectToPlane(Vector normal) {
		return this.minus(this.project(normal));
	}
	
	public double[] toArray() {
		return values.clone();
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append('(');
		for (int i = 0; i < values.length; i++) {
			if (i > 0) str.append(',');
			str.append(values[i]);
		}
		str.append(')');
		return str.toString();
	}
	
	public static double cosAngle(Vector v1, Vector v2) {
		return v1.dot(v2) / v1.length() / v2.length();
	}
	public static double sinAngle(Vector v1, Vector v2) {
		return Math.sqrt(1 - U.sq(v1.dot(v2)) / v1.lengthSquared() / v2.lengthSquared());
	}
	public static double angle(Vector v1, Vector v2) {
		return Math.atan2(sinAngle(v1, v2), cosAngle(v1, v2));
	}
	
	public static double det(Vector... vs) {
		if (vs.length < 2) throw new IllegalArgumentException("Determinant operation needs at least 2 vectors!");
		if (vs.length == 2) {
			return vs[0].get(0) * vs[1].get(1) - vs[0].get(1) * vs[1].get(0);
		} else {
			double result = 0;
			int sign = (vs.length % 2) * 2 - 1; 
			Vector[] sub = Arrays.copyOf(vs, vs.length - 1);
			
			int i = vs.length - 1;
			result += vs[i].get(vs.length - 1) * det(sub) * sign;
			while (i > 0) {
				i--;
				sub[i] = vs[i + 1];
				sign = -sign;
				result += vs[i].get(vs.length - 1) * det(sub) * sign;
			}
			return result;
		}
	}
}
