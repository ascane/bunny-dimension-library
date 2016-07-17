package bunnytests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;

import org.junit.Test;

import bunny.structure.HashMultiset;
import bunny.util.Data;
import bunny.util.Primes;

public class PrimesTest {
	@Test
	public void primes_success() {
		assertEquals(11, Primes.get(4));
		assertEquals(Data.asList(2,3,5,7,11,13,17,19,23,29), Primes.getPrimeList(10));
		assertEquals(Data.asList(2,3,5,7,11,13,17,19,23,29,31,37), Primes.getPrimeListBelow(40));
		assertEquals(53, Primes.getSmallestPrimeGreaterThan(50));
		assertEquals(53, Primes.getSmallestPrimeGreaterThan(53));
		assertEquals(97, Primes.getGreatestPrimeSmallerThan(100));
		assertEquals(107, Primes.getGreatestPrimeSmallerThan(107));
		assertFalse(Primes.isPrime(100007));
		assertTrue(Primes.isPrime(100019));
		assertFalse(Primes.isPrimeAndUpdateTable(100007));
		assertTrue(Primes.isPrimeAndUpdateTable(100019));
		assertEquals(100019, Primes.get(Primes.getIndexOfSmallestPrimeGreaterThan(100007)));
		assertEquals(100019, Primes.getSmallestPrimeGreaterThan(100007));
		assertTrue(Primes.isPrime(37124508045065437L));
		assertEquals(24, Primes.getDivisorCount(600));
		assertEquals(
				new HashSet<Integer>(Data.asList(1,2,3,4,5,6,8,10,12,15,20,24,25,30,40,50,60,75,100,120,150,200,300,600)), 
				new HashSet<Integer>(Data.asList(Primes.getDivisors(600))));
		assertEquals(new HashMultiset<Integer>(Data.asList(2,2,2,3,5,5)), Primes.factorize(600));
	}
}
