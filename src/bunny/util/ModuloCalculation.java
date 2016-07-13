package bunny.util;

public class ModuloCalculation {
	// When working with these method, always supply numbers between 0 and modulo - 1
	
	// modulo should be a prime, otherwise division and combinatorics will not work
	public static long modulo = Long.MAX_VALUE - 24; 
	
	public static long mod(long a) {
		return a >= 0 ? a % modulo : a % modulo + modulo;
	}
	public static long add(long a, long b) {
		if (Long.MAX_VALUE - a >= b) {
			return mod(a + b);
		} else {
			return mod(a + b - modulo);
		}
	}
	public static long sub(long a, long b) {
		return mod(a - b);
	}
	public static long mult(long a, long b) {
		if (a == 0 || Long.MAX_VALUE / a >= b) {
			return mod(a * b);
		} else {
			long res = 0;
			String bb = "";
			while (b > 0) {
				bb = (b % 2) + bb;
				b >>= 1;
			}
			for (int i = 0; i < bb.length(); i++) {
				res = add(res, res);
				if (bb.charAt(i) == '1') {
					res = add(res, a);
				}
			}
			return res;
		}
	}
	public static long div(long a, long b) {
		return mult(a, inv(b));
	}
	public static long inv(long a) {
		return pow(a, modulo - 2);
	}
	public static long pow(long a, long p) {
		if (p == 0) {
			return 1;
		} else if (p < 0) {
			return pow(inv(a), -p);
		} else {
			long res = 1;
			String pp = Long.toString(p, 2);
			for (int i = 0; i < pp.length(); i++) {
				res = mult(res, res);
				if (pp.charAt(i) == '1') {
					res = mult(res, a);
				}
			}
			return res;
		}
	}
	public static long factorial(int n) {
		return factorial(n, n);
	}
	private static long factorial(int n, int count) {
		long res = 1;
		for (int i = n; i > n - count; i--) {
			res = mult(res, i);
		}
		return res;
	}
	public static long combination(int n, int c) {
		return mult(factorial(n, c), inv(factorial(c)));
	}
	public static long combinationWithRepetitions(int n, int c) {
		return combination(n + c - 1, c);
	}
	public static long permutation(int n, int c) {
		return factorial(n, c);
	}
	public static long permutationWithRepetitions(int n, int c) {
		return pow(n, c);
	}
	public static long permutationFullMultiset(int... multiplicities) {
		if (multiplicities.length == 0) return 1;
		int n = 0;
		long res = 1;
		for (int i : multiplicities) {
			n += i;
			res = div(res, factorial(i));
		}
		res = mult(res, factorial(n));
		return res;
	}
}
