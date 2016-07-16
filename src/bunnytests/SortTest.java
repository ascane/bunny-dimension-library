package bunnytests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

import bunny.algo.Sort;
import bunny.data.Data;
import bunny.data.Writer;
import bunny.structure.BasicLinkedList;
import bunny.util.Randomness;

public class SortTest {
	
	@Test
	public void mergeSort_success() {
		Writer w = new Writer(System.out);
		Randomness r = new Randomness();
		for (int i = 0; i < 100; i++) {
			ArrayList<Integer> list = Data.rangeList(i + 1);
			r.permuteList(list);
			BasicLinkedList<Integer> llist = BasicLinkedList.from(list);
			Sort.mergeSort(llist);
			w.println(list);
			w.println(llist);
			w.println("---------------------------------");
			assertEquals(Data.rangeList(i + 1), Data.toArrayList(llist));
		}
		w.close();
	}
}
