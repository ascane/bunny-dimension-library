package bunnytests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;

import bunny.code.CodeMerger;
import bunny.data.Data;
import bunny.data.Reader;
import bunny.data.Writer;
import bunny.structure.HashMultiset;
import bunny.structure.Multiset;
import bunny.structure.Multiset.Entry;
import bunny.structure.Vector;
import bunny.util.Base;
import bunny.util.Combinatorics;
import bunny.util.Lists;
import bunny.util.ModuloCalculation;
import bunny.util.Ordering;
import bunny.util.Primes;
import bunny.util.Randomness;
import bunny.util.SuperFor;
import bunny.util.U;
import bunny.wrap.ListView;
import bunny.wrap.SetWrapper;

public class ClassForTest {

	static Reader r;
	static Writer w;
	
	public static void main(String[] args) {
		w = new Writer(System.out);

		testCombinations();
		testPermutations();
		testRandomness();
		testListView();
		testSuperFor();
		testDeepClone();
		testU();
		testLists();
		testVector();
		testHashMultiset();
		testOrdering();
		//testCodeMerger();

		w.close();
	}
	
	public static void testCombinations() {
		w.println("Testing Combinations...");
		for (List<Integer> a : Combinatorics.generateAllCombinations(Data.rangeList(1, 7), 3)) {
			w.println(a);
		}
		w.println("-----------------------");
		for (Multiset<Integer> a : Combinatorics.generateAllCombinations(new HashMultiset<Integer>(Data.asList(1,2,3,3,4,4)), 3)) {
			w.println(a);
		}
	}
	public static void testPermutations() {
		w.println("Testing Permutations...");
		for (List<Integer> a : Combinatorics.generateAllFullPermutations(Data.rangeList(1, 5))) {
			w.println(a);
		}
		int sum = 0;
		HashSet<Integer> set = new HashSet<Integer>();
		for (List<Integer> list : Combinatorics.generateAllFullPermutations(Data.rangeList(1, 10))) {
			int[] a = Data.toPrimitiveIntArray(list);
			int A = a[0] * 10 + a[1];
			int B = a[2] * 100 + a[3] * 10 + a[4];
			int C = a[5] * 1000 + a[6] * 100 + a[7] * 10 + a[8];
			if (A * B == C && !set.contains(C)) {
				sum += C;
				set.add(C);
				w.println(String.format("%d * %d = %d", A, B, C));
			}
			A = a[0];
			B = a[1] * 1000 + a[2] * 100 + a[3] * 10 + a[4];
			C = a[5] * 1000 + a[6] * 100 + a[7] * 10 + a[8];
			if (A * B == C && !set.contains(C)) {
				sum += C;
				set.add(C);
				w.println(String.format("%d * %d = %d", A, B, C));
			}
		}
		w.println(sum);
	}
	
	public static void testRandomness() {
		w.println("Testing Randomness...");
		Randomness r = new Randomness();
		for (int i = 0; i < 10; i++) {
			w.println(r.nextPermutation(5));
		}
		String[] a = new String[] {"A", "B", "C", "D", "E"};
		for (int i = 0; i < 10; i++) {
			r.permuteArray(a);
			w.println(a);
		}
		for (int i = 0; i < 10; i++) {
			w.println(r.nextUnitVector(3));
		}
		for (int i = 0; i < 10; i++) {
			w.println(r.nextVectorInUnitSphere(2));
		}
	}
	
	public static void testListView() {
		w.println("Testing ListView...");
		ArrayList<Integer> a = Data.rangeList(21);
		w.println(a);
		w.println(ListView.of(a).reverse());
		w.println(ListView.of(a).rotate(-3));
		w.println(ListView.of(a).rotate(23));
		w.println(ListView.of(a).even());
		w.println(ListView.of(a).odd());
		w.println(ListView.of(a).every(0, 3));
		w.println(ListView.of(a).every(2, 3));
		w.println(ListView.of(a).range(2, 8));
		w.println(ListView.of(a).reverse().even().range(2, 8).rotate(2).reverse());
	}
	
	public static void testSuperFor() {
		w.println("Testing SuperFor...");
		for (int[] param : SuperFor.range(2,2,3,4)) {
			w.println(param);
		}
		w.println("------------------------");
		for (int[] param : SuperFor.range(new int[]{1,2,2}, new int[]{4,10,-1}, new int[]{1,2,-1})) {
			w.println(param);
		}
		w.println("------------------------");
		for (int[] param : SuperFor.range(new int[]{1,2,2}, new int[]{4,0,-1}, new int[]{1,2,-1})) {
			w.println(param);
		}
		w.println("------------------------");
		for (int[] param : SuperFor.smallerThanPrevious(5, 4)) {
			w.println(param);
		}
		w.println("------------------------");
		for (int[] param : SuperFor.strictlyGreaterThanPrevious(6, 3)) {
			w.println(param);
		}
		w.println("------------------------");
		for (int[] param : SuperFor.create()
				.addForBlock(3)
				.addForBlockUsingPreviousParameter(0, 0, -1, -2, -1)
				.addForBlockUsingPreviousParameter(0, 1, -6, 0, -1)
				.addForBlock(99, 101)) {
			w.println(param);
		}
		w.println("------------------------");
		int[] aa = new int[]{1,2,3,4,5};
		for (int[] i : SuperFor.fromMultidimensionalArray(aa)) {
			w.print(aa[i[0]] + " ");
		}
		w.println();
		w.println("------------------------");
		int[][][] a = new int[][][]{
			{{1,2,3}, {2,3}, {3}},
			{{2,4,6,8}, {4,6,8}, {6,8}, {8}},
			{{3,6,9,12,15}, {6,9,12,15}, {9,12,15}, {12,15}, {15}},
			null,
			{null, null, {42}}
		};
		a[4][0] = new int[new Randomness().nextInt() % 30 + 30];
		Arrays.fill(a[4][0], -6);
		
		long t1 = System.nanoTime();
		for (int i = 0; i < a.length; i++) {
			if (a[i] != null) {
				for (int j = 0; j < a[i].length; j++) {
					if (a[i][j] != null) {
						for (int k = 0; k < a[i][j].length; k++) {
							w.print(a[i][j][k] + " ");
							//doNothing();
						}
					}
				}
			}
		}
		w.println();
		w.println("------------------------");
		
		long t2 = System.nanoTime();
		for (int[] i : SuperFor.fromMultidimensionalArray(a)) {
			w.print(a[i[0]][i[1]][i[2]] + " ");
			//doNothing();
		}
		w.println();
		
		long t3 = System.nanoTime();
		for (int[] i : SuperFor.fromMultidimensionalArray(a)) {
			w.print(a[i[0]][i[1]][i[2]] + " ");
			//doNothing();
		}
		w.println();
		
		long t4 = System.nanoTime();
		
		w.println((t2 - t1) / 1000000. + "ms", (t3 - t2) / 1000000. + "ms", (t4 - t3) / 1000000. + "ms");
		w.println("------------------------");
		
		ArrayList<ArrayList<ArrayList<Integer>>> list = Data.toArrayList(
				Data.toArrayList(Data.toArrayList(1,2,3), Data.toArrayList(2,3), Data.toArrayList(3)),
				Data.toArrayList(Data.toArrayList(2,4,6,8), Data.toArrayList(4,6,8), Data.toArrayList(6,8), Data.toArrayList(8)),
				Data.toArrayList(Data.toArrayList(3,6,9,12,15), Data.toArrayList(6,9,12,15), Data.toArrayList(9,12,15), Data.toArrayList(12,15), Data.toArrayList(15)),
				null,
				Data.toArrayList(null, null, Data.toArrayList(42)));
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) != null) {
				for (int j = 0; j < list.get(i).size(); j++) {
					if (list.get(i).get(j) != null) {
						for (int k = 0; k < list.get(i).get(j).size(); k++) {
							w.print(list.get(i).get(j).get(k) + " ");
						}
					}
				}
			}
		}
		w.println();
		for (int[] i : SuperFor.fromMultidimensionalList(list)) {
			w.print(list.get(i[0]).get(i[1]).get(i[2]) + " ");
		}
		w.println();
	}
	
	/*public static int o = 0;
	public static void doNothing() {
		o = 5;
	}*/
	
	public static void testDeepClone() {
		w.println("Testing deep clone...");
		int[][][] a = new int[][][]{
			{{1,2,3}, {2,3}, {3}},
			{{2,4,6,8}, {4,6,8}, {6,8}, {8}},
			{{3,6,9,12,15}, {6,9,12,15}, {9,12,15}, {12,15}, {15}},
			null,
			{null, null, {42}}
		};
		int[][][] b = (int[][][]) Data.deepCloneArray(a);
		a[2][2][2] = 42424;
		b[1][1][1] = 99999;
		for (int i = 0; i < a.length; i++) {
			if (a[i] == null) {
				w.print("null ");
			} else {
				for (int j = 0; j < a[i].length; j++) {
					if (a[i][j] == null) {
						w.print("null ");
					} else {
						for (int k = 0; k < a[i][j].length; k++) {
							w.print(a[i][j][k] + " ");
						}
					}
				}
			}
		}
		w.println();
		for (int i = 0; i < b.length; i++) {
			if (b[i] == null) {
				w.print("null ");
			} else {
				for (int j = 0; j < b[i].length; j++) {
					if (b[i][j] == null) {
						w.print("null ");
					} else {
						for (int k = 0; k < b[i][j].length; k++) {
							w.print(b[i][j][k] + " ");
						}
					}
				}
			}
		}
		w.println();
		
		ArrayList<ArrayList<ArrayList<Integer>>> list1 = Data.toArrayList(
				Data.toArrayList(Data.toArrayList(1,2,3), Data.toArrayList(2,3), Data.toArrayList(3)),
				Data.toArrayList(Data.toArrayList(2,4,6,8), Data.toArrayList(4,6,8), Data.toArrayList(6,8), Data.toArrayList(8)),
				Data.toArrayList(Data.toArrayList(3,6,9,12,15), Data.toArrayList(6,9,12,15), Data.toArrayList(9,12,15), Data.toArrayList(12,15), Data.toArrayList(15)),
				null,
				Data.toArrayList(null, null, Data.toArrayList(42)));
		@SuppressWarnings("unchecked")
		List<List<List<Integer>>> list2 = (List<List<List<Integer>>>) Data.deepCloneList(list1);
		list1.get(2).get(2).set(2, 42424);
		list2.get(1).get(1).set(1, 99999);
		for (int i = 0; i < list1.size(); i++) {
			if (list1.get(i) == null) {
				w.print("null ");
			} else {
				for (int j = 0; j < list1.get(i).size(); j++) {
					if (list1.get(i).get(j) == null) {
						w.print("null ");
					} else {
						for (int k = 0; k < list1.get(i).get(j).size(); k++) {
							w.print(list1.get(i).get(j).get(k) + " ");
						}
					}
				}
			}
		}
		w.println();
		for (int i = 0; i < list2.size(); i++) {
			if (list2.get(i) == null) {
				w.print("null ");
			} else {
				for (int j = 0; j < list2.get(i).size(); j++) {
					if (list2.get(i).get(j) == null) {
						w.print("null ");
					} else {
						for (int k = 0; k < list2.get(i).get(j).size(); k++) {
							w.print(list2.get(i).get(j).get(k) + " ");
						}
					}
				}
			}
		}
		w.println();
	}
	
	public static void testU() {
		w.println("Testing U...");
		w.println("min/max:");
		w.println(U.max(5, 100/9, 8, -10/3));
		w.println(U.min(5, 100/9, 8, -10/3));
		w.println(U.max(5L, 100f/9, 8f, -10/3f));
		w.println(U.max(5, 100f/9, 8, -10/3.0));
		w.println(U.max(5, 100d/9, 8, -10/3.0));
		w.println(U.max(5, 999999999999999L, 2, 1, 0));
		w.println(U.max(5, 999999999999999L, 2, 1, 0.));
		w.println(U.max(5, 999999999999999L, 2, 1, 0f));
		w.println(U.maxT("rabbit", "rab", "What?"));
		w.println(U.minT("rabbit", "rab", "What?"));
		w.println(U.maxT(Data.asList("rabbit", "rab", "What?")));
		w.println("mod:");
		w.println(U.mod(127, 10));
		w.println(U.mod(-127, 10));
		w.println(U.mod(-1000000000127L, 10));
		w.println(U.mod(-127.1f, 10));
		w.println(U.mod(-127.1, 10));
		w.println("pow:");
		w.println(U.pow(3, 10));
		w.println(U.pow(3, 1));
		w.println(U.pow(3, 0));
		w.println(U.pow(3L, 32));
		w.println("factorial:");
		w.println(U.factorial(10));
		w.println(U.factorial(0));
		w.println("gcd/lcm:");
		w.println(U.gcd(600, 216));
		w.println(U.lcm(600, 216));
		w.println(U.gcd(100, 137));
		w.println(U.lcm(100, 137));
		w.println(U.gcd(1, 137));
		w.println(U.lcm(1, 137));
		w.println(U.gcd(123456789876543210L, 100));
		w.println(U.lcm(123456789876543210L, 100));
		w.println(U.gcd(600, 270, 135));
		w.println(U.lcm(600, 270, 135));
	}
	
	public static void testLists() {
		w.println("Testing Lists...");
		int[] array = new Randomness().nextPermutation(100);
		w.println(Lists.quickSelect(Data.asList(array), 42));
		array = new Randomness().nextPermutation(100);
		Lists.quickPartialSort(Data.asList(array), 42);
		w.println(array);
	}
	
	public static void testVector() {
		w.println("Testing Vector...");
		Vector v1 = new Vector(1,2,3);
		Vector v2 = new Vector(-1,-2,3);
		w.println(v1.dim());
		w.println(v1.lengthSquared());
		w.println(v1.length());
		w.println(v1.normalized());
		w.println(v1.plus(v2));
		w.println(v1.minus(v2));
		w.println(v1.times(10));
		w.println(v1.times(v2));
		w.println(v1.dot(v2));
		w.println(v1.cross(v2));
		w.println(v1.project(v2));
		w.println(v1.project(v2).cross(v2));
		w.println(v1.projectToPlane(v2));
		w.println(v1.projectToPlane(v2).dot(v2));
		w.println(v1.toArray());
		w.println(Vector.cosAngle(v1, v2));
		w.println(Vector.sinAngle(v1, v2));
		w.println(Vector.angle(v1, v2));
		w.println(Vector.det(v1, v2));
		w.println(Vector.det(v1, v2, new Vector(0,0,1)));
		w.println(Vector.det(v1, v2, new Vector(1,0,0)));
		w.println(v1.cross(v2).dot(new Vector(1,0,0)));
		v1.normalize();
		w.println(v1);
		v1.plusEquals(v2);
		w.println(v1);
		v1.minusEquals(v2);
		w.println(v1);
		v1.timesEquals(10);
		w.println(v1);
		v1.timesEquals(v2);
		w.println(v1);
	}
	
	public static void testHashMultiset() {
		w.println("Testing HashMultiset...");
		HashMultiset<Integer> ms = new HashMultiset<Integer>();
		w.println("size:" + ms.size());
		ms.add(42);
		w.println(ms, "size:" + ms.size());
		ms.add(69);
		w.println(ms, "size:" + ms.size());
		ms.add(42);
		w.println(ms, "size:" + ms.size());
		ms.add(9, 9);
		w.println(ms, "size:" + ms.size());
		ms.add(42, 2);
		w.println(ms, "size:" + ms.size());
		ms.add(42, 0);
		w.println(ms, "size:" + ms.size());
		ms.add(0, 0);
		w.println(ms, "size:" + ms.size());
		ms.remove(9);
		w.println(ms, "size:" + ms.size());
		ms.remove(5);
		w.println(ms, "size:" + ms.size());
		ms.remove(42, 2);
		w.println(ms, "size:" + ms.size());
		ms.remove(9, 81);
		w.println(ms, "size:" + ms.size());
		ms.remove(10, 81);
		w.println(ms, "size:" + ms.size());
		ms.addAll(Data.asList(1,2,3,4,5,5,5,42,42,69));
		w.println(ms, "size:" + ms.size());
		ms.removeAll(Data.asList(1,3,5,7,5,3,1));
		w.println(ms, "size:" + ms.size());
		w.println(ms.contains(42), "size:" + ms.size());
		w.println(ms.contains(1), "size:" + ms.size());
		w.println(ms.containsAll(Data.asList(1,42)), "size:" + ms.size());
		w.println(ms.containsAll(Data.asList(69,42)), "size:" + ms.size());
		w.println(ms.count(42), "size:" + ms.size());
		w.println(ms.count(1), "size:" + ms.size());
		ms.retainAll(Data.asList(42, 69));
		w.println(ms, "size:" + ms.size());
		ms.setCount(69, 6);
		w.println(ms, "size:" + ms.size());
		ms.setCount(0, 0);
		w.println(ms, "size:" + ms.size());
		ms.setCount(0, 1);
		w.println(ms, "size:" + ms.size());
		w.println(new SetWrapper<Multiset.Entry<Integer>, String>(ms.entrySet(), new Function<Multiset.Entry<Integer>, String>() {
			@Override
			public String apply(Entry<Integer> e) {return "[Element:" + e.getElement() + " Count:" + e.getCount() + "]";}
		}));
		w.println(ms.elementSet());
	}
	
	public static void testOrdering() {
		String[] a = new String[]{"aa", "soo", "so", "bik", "l", "", "lol", "ssssss", "sisis", "kk"};
		Function<String, Integer> lengthFunc = new Function<String, Integer>() {
			@Override
			public Integer apply(String t) {return t.length();}
		};
		
		Ordering<String> c = Ordering.natural().onResultOf(lengthFunc).compound(Ordering.natural());
		w.println(c.max(a));
		w.println(c.min(a));
		w.println(c.argmax(a));
		w.println(c.argmin(a));
		
		Arrays.sort(a, Ordering.natural());
		w.println(a);
		Arrays.sort(a, Ordering.natural().reverse());
		w.println(a);
		Arrays.sort(a, Ordering.natural().onResultOf(lengthFunc));
		w.println(a);
		Arrays.sort(a, Ordering.natural().onResultOf(lengthFunc).compound(Ordering.natural()));
		w.println(a);
		Arrays.sort(a, Ordering.natural().onResultOf(lengthFunc).compound(Ordering.natural()).reverse());
		w.println(a);
		Arrays.sort(a, Ordering.natural().onResultOf(lengthFunc).reverse().compound(Ordering.natural()));
		w.println(a);
		Arrays.sort(a, Ordering.arbitrary());
		w.println(a);
		Arrays.sort(a, Ordering.arbitrary().reverse());
		w.println(a);
		
		List<List<String>> b = Data.asList(
			Data.asList("aaa", "soo"),
			Data.asList("so", "bik", "l"), 
			Data.asList("", "lol"), 
			Data.asList("", "lol", "loli"), 
			Data.asList("z", "zz", "zzz"),
			Data.asList(""),
			Data.asList("ssssss"), 
			Data.asList("so", "sisis", "kk"));
		Comparator<Iterable<String>> cp = Ordering.natural().lexigraphical();
		Collections.sort(b, cp);
		for (int i = 0; i < b.size(); i++) {
			w.println(b.get(i).toString());
		}
		w.println("----------");
		Collections.sort(b, Ordering.natural().onResultOf(lengthFunc).compound(Ordering.natural()).lexigraphical());
		for (int i = 0; i < b.size(); i++) {
			w.println(b.get(i).toString());
		}
		w.println("----------");
		Collections.sort(b, Ordering.arbitrary());
		for (int i = 0; i < b.size(); i++) {
			w.print(System.identityHashCode(b.get(i)) + " ");
			w.println(b.get(i).toString());
		}
		w.println("----------");
		String[][] bb = new String[][]{
			{"aaa", "soo"},
			{"so", "bik", "l"}, 
			{"", "lol"}, 
			{"", "lol", "loli"}, 
			{"z", "zz", "zzz"},
			{},
			{"ssssss"}, 
			{"so", "sisis", "kk"}};
		Arrays.sort(bb, Ordering.natural().arrayLexigraphical());
		for (int i = 0; i < bb.length; i++) {
			w.println(bb[i]);
		}
		Arrays.sort(bb, Ordering.usingToString());
		for (int i = 0; i < bb.length; i++) {
			w.print(bb[i] + ": ");
			w.println(bb[i]);
		}
	}
	
	public static void testCodeMerger() {
		w.println("Testing CodeMerger...");
		CodeMerger.merge("src/ClassForTest.java");
	}
	
}
