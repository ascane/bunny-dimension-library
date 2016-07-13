package bunny.util;

import java.util.Iterator;

//Short for "Util", which it-self is short for "Utility"
public class U {
	public static int sq(int n) {
		return n * n;
	}
	public static long sq(long n) {
		return n * n;
	}
	public static float sq(float n) {
		return n * n;
	}
	public static double sq(double n) {
		return n * n;
	}
	
	public static int min(int... n) {
		int result = n[0];
		for (int i = 1; i < n.length; i++) {
			result = Math.min(result, n[i]);
		}
		return result;
	}
	public static long min(long... n) {
		long result = n[0];
		for (int i = 1; i < n.length; i++) {
			result = Math.min(result, n[i]);
		}
		return result;
	}
	public static float min(float... n) {
		float result = n[0];
		for (int i = 1; i < n.length; i++) {
			result = Math.min(result, n[i]);
		}
		return result;
	}
	public static double min(double... n) {
		double result = n[0];
		for (int i = 1; i < n.length; i++) {
			result = Math.min(result, n[i]);
		}
		return result;
	}
	@SafeVarargs
	public static <T extends Comparable<? super T>> T minT(T... n) {
		T result = n[0];
		for (int i = 1; i < n.length; i++) {
			result = n[i].compareTo(result) < 0 ? n[i] : result;
		}
		return result;
	}
	public static <T extends Comparable<? super T>> T minT(Iterable<T> it) {
		Iterator<T> itor = it.iterator();
		T result = itor.next();
		for (;itor.hasNext();) {
			T next = itor.next();
			result = next.compareTo(result) < 0 ? next : result;
		}
		return result;
	}
	
	public static int argmin(int... n) {
		int result = n[0];
		int index = 0;
		for (int i = 1; i < n.length; i++) {
			if (n[i] < result) {
				result = n[i];
				index = i;
			}
		}
		return index;
	}
	public static int argmin(long... n) {
		long result = n[0];
		int index = 0;
		for (int i = 1; i < n.length; i++) {
			if (n[i] < result) {
				result = n[i];
				index = i;
			}
		}
		return index;
	}
	public static int argmin(float... n) {
		float result = n[0];
		int index = 0;
		for (int i = 1; i < n.length; i++) {
			if (n[i] < result) {
				result = n[i];
				index = i;
			}
		}
		return index;
	}
	public static int argmin(double... n) {
		double result = n[0];
		int index = 0;
		for (int i = 1; i < n.length; i++) {
			if (n[i] < result) {
				result = n[i];
				index = i;
			}
		}
		return index;
	}
	@SafeVarargs
	public static <T extends Comparable<? super T>> int argminT(T... n) {
		T result = n[0];
		int index = 0;
		for (int i = 1; i < n.length; i++) {
			if (n[i].compareTo(result) < 0) {
				result = n[i];
				index = i;
			}
		}
		return index;
	}
	public static <T extends Comparable<? super T>> int argminT(Iterable<T> it) {
		Iterator<T> itor = it.iterator();
		T result = itor.next();
		int index = 0;
		for (int i = 0; itor.hasNext();) {
			T next = itor.next();
			i++;
			if (next.compareTo(result) < 0) {
				result = next;
				index = i;
			}
		}
		return index;
	}
	
	public static int max(int... n) {
		int result = n[0];
		for (int i = 1; i < n.length; i++) {
			result = Math.max(result, n[i]);
		}
		return result;
	}
	public static long max(long... n) {
		long result = n[0];
		for (int i = 1; i < n.length; i++) {
			result = Math.max(result, n[i]);
		}
		return result;
	}
	public static float max(float... n) {
		float result = n[0];
		for (int i = 1; i < n.length; i++) {
			result = Math.max(result, n[i]);
		}
		return result;
	}
	public static double max(double... n) {
		double result = n[0];
		for (int i = 1; i < n.length; i++) {
			result = Math.max(result, n[i]);
		}
		return result;
	}
	@SafeVarargs
	public static <T extends Comparable<? super T>> T maxT(T... n) {
		T result = n[0];
		for (int i = 1; i < n.length; i++) {
			result = n[i].compareTo(result) > 0 ? n[i] : result;
		}
		return result;
	}
	public static <T extends Comparable<? super T>> T maxT(Iterable<T> it) {
		Iterator<T> itor = it.iterator();
		T result = itor.next();
		for (;itor.hasNext();) {
			T next = itor.next();
			result = next.compareTo(result) > 0 ? next : result;
		}
		return result;
	}
	
	public static int argmax(int... n) {
		int result = n[0];
		int index = 0;
		for (int i = 1; i < n.length; i++) {
			if (n[i] > result) {
				result = n[i];
				index = i;
			}
		}
		return index;
	}
	public static int argmax(long... n) {
		long result = n[0];
		int index = 0;
		for (int i = 1; i < n.length; i++) {
			if (n[i] > result) {
				result = n[i];
				index = i;
			}
		}
		return index;
	}
	public static int argmax(float... n) {
		float result = n[0];
		int index = 0;
		for (int i = 1; i < n.length; i++) {
			if (n[i] > result) {
				result = n[i];
				index = i;
			}
		}
		return index;
	}
	public static int argmax(double... n) {
		double result = n[0];
		int index = 0;
		for (int i = 1; i < n.length; i++) {
			if (n[i] > result) {
				result = n[i];
				index = i;
			}
		}
		return index;
	}
	@SafeVarargs
	public static <T extends Comparable<? super T>> int argmaxT(T... n) {
		T result = n[0];
		int index = 0;
		for (int i = 1; i < n.length; i++) {
			if (n[i].compareTo(result) > 0) {
				result = n[i];
				index = i;
			}
		}
		return index;
	}
	public static <T extends Comparable<? super T>> int argmaxT(Iterable<T> it) {
		Iterator<T> itor = it.iterator();
		T result = itor.next();
		int index = 0;
		for (int i = 0; itor.hasNext();) {
			T next = itor.next();
			i++;
			if (next.compareTo(result) > 0) {
				result = next;
				index = i;
			}
		}
		return index;
	}
	
	public static int mod(int a, int b) {
		return a >= 0 ? a % b : a % b + b;
	}
	public static long mod(long a, long b) {
		return a >= 0 ? a % b : a % b + b;
	}
	public static float mod(float a, float b) {
		return a >= 0 ? a % b : a % b + b;
	}
	public static double mod(double a, double b) {
		return a >= 0 ? a % b : a % b + b;
	}
	public static int pow(int a, int p) {
		if (p == 0) {
			return 1;
		} else {
			int res = 1;
			String pp = Integer.toString(p, 2);
			for (int i = 0; i < pp.length(); i++) {
				res *= res;
				if (pp.charAt(i) == '1') {
					res *= a;
				}
			}
			return res;
		}
	}
	public static long pow(long a, int p) {
		if (p == 0) {
			return a;
		} else {
			long res = 1;
			String pp = Integer.toString(p, 2);
			for (int i = 0; i < pp.length(); i++) {
				res *= res;
				if (pp.charAt(i) == '1') {
					res *= a;
				}
			}
			return res;
		}
	}
	public static long factorial(int n) {
		long result = 1;
		for (int i = 2; i <= n; i++) {
			result *= i;
		}
		return result;
	}
	public static int gcd(int... n) {
		if (n.length == 2) {
			if (n[1] == 0) {
				return n[0];
			} else {
				return gcd(n[1], n[0] % n[1]);
			}
		} else {
			int res = n[0];
			for (int i = 1; i < n.length; i++) {
				res = gcd(res, n[i]);
			}
			return res;
		}
	}
	public static long gcd(long... n) {
		if (n.length == 2) {
			if (n[1] == 0) {
				return n[0];
			} else {
				return gcd(n[1], n[0] % n[1]);
			}
		} else {
			long res = n[0];
			for (int i = 1; i < n.length; i++) {
				res = gcd(res, n[i]);
			}
			return res;
		}
	}
	public static int lcm(int... n) {
		int res = n[0];
		for (int i = 1; i < n.length; i++) {
			res = res / gcd(res, n[i]) * n[i];
		}
		return res;
	}
	public static long lcm(long... n) {
		long res = n[0];
		for (int i = 1; i < n.length; i++) {
			res = res / gcd(res, n[i]) * n[i];
		}
		return res;
	}

}
