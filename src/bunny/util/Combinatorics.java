package bunny.util;

import java.util.ArrayList;
import java.util.List;

import bunny.structure.HashMultiset;
import bunny.structure.Multiset;

public class Combinatorics {
	public static long combination(int n, int c) {
		long result = 1;
		for (int i = 1; i <= c; i++) {
			result *= n + 1 - i;
			result /= i;
		}
		return result;
	}
	public static long combinationWithRepetitions(int n, int c) {
		return combination(n + c - 1, c);
	}
	public static long permutation(int n, int c) {
		long result = 1;
		for (int i = n; i > n - c; i++) {
			result *= i;
		}
		return result;
	}
	public static long permutationWithRepetitions(int n, int c) {
		return U.pow(n, c);
	}
	public static long permutationFullMultiset(int... multiplicities) {
		if (multiplicities.length == 0) return 1;
		int n = 0;
		long res = 1;
		for (int i : multiplicities) {
			n += i;
		}
		for (int i : multiplicities) {
			for (int j = 1; j <= i; j++) {
				res *= n--;
				res /= j;
			}
		}
		return res;
	}
	
	public static <T> Iterable<List<T>> generateAllCombinations(List<T> array, int k) {
		if (k < 0 || k > array.size()) throw new IllegalArgumentException("k should be between 0 and array.size()");
		return new CombinationIterable<T>(array, k);
	}
	public static <T> Iterable<Multiset<T>> generateAllCombinations(Multiset<T> multiset, int k) {
		if (k < 0 || k > multiset.size()) throw new IllegalArgumentException("k should be between 0 and multiset.size()");
		return new CombinationMultisetIterable<T>(multiset, k);
	}
	
	public static <T> Iterable<List<T>> generateAllFullPermutations(List<T> array) {
		return new FastFullPermutationIterable<T>(array);
	}
	
	
	
	private static class CombinationIterable<T> extends SimpleIterable<List<T>> {

		private List<T> list;
		private List<T> output;
		private int k;
		private int[] c;
		
		public CombinationIterable(List<T> list, int k) {
			this.list = list;
			this.k = k;
			output = new ArrayList<T>(k);
			c = new int[k];
			for (int i = 0; i < k; i++) {
				output.add(list.get(i));
				c[i] = i;
			}
		}
		
		@Override
		protected List<T> nextValue(List<T> last) {
			if (last == null) {
				return output;
			} else {
				int pos = k - 1;
				if (pos < 0) return null;
				c[pos]++;
				while (c[pos] > pos + list.size() - k) {
					pos--;
					if (pos < 0) return null;
					c[pos]++;
				}
				output.set(pos, list.get(c[pos]));
				while (pos < k - 1) {
					pos++;
					c[pos] = c[pos - 1] + 1;
					output.set(pos, list.get(c[pos]));
				}
				return output;
			}
		}
	}
	
	private static class CombinationMultisetIterable<T> extends SimpleIterable<Multiset<T>> {

		private Multiset<T> set;
		private ArrayList<T> setList;
		private Multiset<T> output;
		private ArrayList<T> outputList;
		private int k;
		private int[] c;
		
		public CombinationMultisetIterable(Multiset<T> set, int k) {
			this.set = set;
			this.k = k;
			setList = new ArrayList<T>(set);
			output = new HashMultiset<T>();
			outputList = new ArrayList<T>(k);
			c = new int[k];
			for (int i = 0; i < k; i++) {
				output.add(setList.get(i));
				outputList.add(setList.get(i));
				c[i] = i;
			}
		}
		
		@Override
		protected Multiset<T> nextValue(Multiset<T> last) {
			if (last == null) {
				return output;
			} else {
				int pos = k - 1;
				if (pos < 0) return null;
				T current = outputList.get(pos);
				while (c[pos] < set.size() && setList.get(c[pos]).equals(current)) c[pos]++;
				while (c[pos] > pos + set.size() - k) {
					output.remove(outputList.get(pos));
					pos--;
					if (pos < 0) return null;
					current = outputList.get(pos);
					while (c[pos] <= pos + set.size() - k && setList.get(c[pos]).equals(current)) c[pos]++;
				}
				output.remove(outputList.get(pos));
				outputList.set(pos, setList.get(c[pos]));
				output.add(outputList.get(pos));
				while (pos < k - 1) {
					pos++;
					c[pos] = c[pos - 1] + 1;
					outputList.set(pos, setList.get(c[pos]));
					output.add(outputList.get(pos));
				}
				return output;
			}
		}
	}
	
	private static class FastFullPermutationIterable<T> extends SimpleIterable<List<T>> {

		private List<T> list;
		private int[] c;
		
		public FastFullPermutationIterable(List<T> list) {
			this.list = new ArrayList<T>(list);
			c = new int[list.size()];
		}
		
		@Override
		protected List<T> nextValue(List<T> last) {
			if (last == null) {
				return list;
			} else {
				int i = 0;
				c[0]++;
				while (c[i] > i + 1) {
					c[i++] = 0;
					if (i >= list.size() - 1) {
						break;
					}
					c[i]++;
				}
				if (i >= list.size() - 1) {
					return null;
				} else {
					swap(i % 2 == 1 ? 0 : c[i] - 1, i + 1);
					return list;
				}
			}
		}
		
		private void swap(int i1, int i2) {
			T temp = list.get(i1);
			list.set(i1, list.get(i2));
			list.set(i2, temp);
		}
		
	}

}

