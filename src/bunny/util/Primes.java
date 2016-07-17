package bunny.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import bunny.algo.Lists;
import bunny.structure.HashMultiset;
import bunny.structure.Multiset;
import bunny.wrap.ListView;

public class Primes {
	
	private static ArrayList<Integer> primes = new ArrayList<>(Arrays.asList(new Integer[]{2, 3}));
	
	public static int get(int i) {
		updatePrimes(i + 1);
		return primes.get(i);
	}
	public static List<Integer> getPrimeList(int n) {
		updatePrimes(n);
		return ListView.of(primes).range(0, n).readOnly();
	}
	public static List<Integer> getPrimeListBelow(int number) {
		updatePrimesUpTo(number);
		int n = getIndexOfGreatestPrimeSmallerThan(number) + 1;
		return ListView.of(primes).range(0, n).readOnly();
	}
	public static int getIndexOfSmallestPrimeGreaterThan(int number) {
		updatePrimesUpTo(number);
		return Lists.binarySearchSmallestGreaterThan(primes, number);
	}
	public static int getSmallestPrimeGreaterThan(int number) {
		return primes.get(getIndexOfSmallestPrimeGreaterThan(number));
	}
	public static int getIndexOfGreatestPrimeSmallerThan(int number) {
		updatePrimesUpTo(number);
		return Lists.binarySearchGreatestSmallerThan(primes, number);
	}
	public static int getGreatestPrimeSmallerThan(int number) {
		return primes.get(getIndexOfGreatestPrimeSmallerThan(number));
	}
	public static boolean isPrime(long number) {
		if (number < 2) return false;
		long current = 0;
		for (int i = 0; i < primes.size(); i++) {
			current = primes.get(i);
			if (number % current == 0) return false;
			if (current * current > number) return true;
		}
		for (current += 2;; current += 2) {
			if (number % current == 0) return false;
			if (current * current > number) return true;
		}
	}
	public static boolean isPrimeAndUpdateTable(int number) {
		updatePrimesUpTo(number);
		return Collections.binarySearch(primes, number) >= 0;
	}
	public static Multiset<Integer> factorize(int number) {
		Multiset<Integer> result = new HashMultiset<>();
		if (number <= 1) return result;
		int remain = number;
		int i = 0;
		while (true) {
			int p = get(i);
			if (p * p > remain) {
				result.add(remain);
				break;
			}
			if (remain % p == 0) {
				remain /= p;
				result.add(p);
			} else {
				i++;
			}
		}
		return result;
	}
	public static int getDivisorCount(int number) {
		return getDivisorCount(factorize(number));
	}
	public static int getDivisorCount(Multiset<Integer> factorization) {
		int result = 1;
		for (Multiset.Entry<Integer> entry : factorization.entrySet()) {
			result *= entry.getCount() + 1;
		}
		return result;
	}
	public static int[] getDivisors(int number) {
		return getDivisors(factorize(number));
	}
	public static int[] getDivisors(Multiset<Integer> factorization) {
		int[] result = new int[getDivisorCount(factorization)];
		if (result.length == 1) {
			result[0] = 1;
			return result;
		}
		int n = factorization.elementSet().size();
		int[] primeDivisors = new int[n];
		int[] powers = new int[n];
		int[] currentPowers = new int[n];
		int i = 0;
		for (Multiset.Entry<Integer> entry : factorization.entrySet()) {
			primeDivisors[i] = entry.getElement();
			powers[i] = entry.getCount();
			i++;
		}
		i = 0;
		out:
		while (true) {
			result[i] = 1;
			for (int k = 0; k < n; k++) {
				result[i] *= U.pow(primeDivisors[k], currentPowers[k]);
			}
			i++;
			
			currentPowers[0]++;
			int j = 0;
			while (currentPowers[j] > powers[j]) {
				if (j == n - 1) break out;
				currentPowers[j] = 0;
				currentPowers[++j]++;
			}
		}
		return result;
	}
	

	private static void updatePrimes(int n) {
		if (n <= primes.size()) return;
		int current = primes.get(primes.size() - 1) + 2;
		for (int count = primes.size(); count < n;) {
			boolean isPrime = true;
			for (int i = 0; i < count; i++) {
				if (current % primes.get(i) == 0) {
					isPrime = false;
					break;
				}
				if (current < primes.get(i) * primes.get(i)) {
					break;
				}
			}
			if (isPrime) {
				primes.add(current);
				count++;
			}
			current += 2;
		}
	}
	
	private static void updatePrimesUpTo(int number) {
		if (number <= primes.get(primes.size() - 1)) return;
		int current = primes.get(primes.size() - 1) + 2;
		while (primes.get(primes.size() - 1) < number) {
			boolean isPrime = true;
			for (int i = 0; i < primes.size(); i++) {
				if (current % primes.get(i) == 0) {
					isPrime = false;
					break;
				}
				if (current < primes.get(i) * primes.get(i)) {
					break;
				}
			}
			if (isPrime) {
				primes.add(current);
			}
			current += 2;
		}
	}

}
