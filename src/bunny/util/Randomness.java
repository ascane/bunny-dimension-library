package bunny.util;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import bunny.structure.Vector;

public class Randomness extends Random {

	private static final long serialVersionUID = 1L;
	
	public Randomness() {
		super();
	}
	public Randomness(long seed) {
		super(seed);
	}
	
	public int[] nextPermutation(int n) {
		int[] result = new int[n];
		if (n == 0) return result;
		
		result[0] = 0;
		for (int i = 1; i < n; i++) {
			int k = nextInt(i + 1);
			result[i] = result[k];
			result[k] = i;
		}
		return result;
	}
	public <T> void permuteArray(T[] array) {
		permuteList(Arrays.asList(array));
	}
	public <T> void permuteList(List<T> list) {
		int n = list.size();
		int[] p = nextPermutation(n);
		List<T> tempList = new ArrayList<T>(n);
		for (int i = 0; i < n; i++) {
			tempList.add(list.get(p[i]));
		}
		for (int i = 0; i < n; i++) {
			list.set(i, tempList.get(i));
		}
	}
	public Vector nextVectorInUnitCube(int dimension) {
		Vector result = new Vector(dimension);
		for (int i = 0; i < dimension; i++) {
			result.set(i, nextDouble() * 2 - 1);
		}
		return result;
	}
	public Vector nextVectorInUnitSphere(int dimension) {
		Vector result;
		do {
			result = nextVectorInUnitCube(dimension);
		} while (result.lengthSquared() > 1);
		return result;
	}
	public Vector nextUnitVector(int dimension) {
		Vector result = nextVectorInUnitSphere(dimension);
		result.normalize();
		return result;
	}
}
