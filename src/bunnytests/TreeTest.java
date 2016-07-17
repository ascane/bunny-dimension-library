package bunnytests;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import bunny.structure.Tree;

public class TreeTest {
	private static final Tree<Integer> TREE = 
			new Tree<>(3, 
					new Tree<>(1,
							new Tree<>(4,
									new Tree<>(42)),
							new Tree<>(7)),
					new Tree<>(2,
							new Tree<>(8),
							new Tree<>(6,
									new Tree<>(5),
									new Tree<>(55),
									new Tree<>(555))),
					new Tree<>(0));
	
	@Test
	public void toString_success() {
		assertEquals("3(1(4(42),7),2(8,6(5,55,555)),0)", TREE.toString());
	}
	
	@Test
	public void preorderIterator_success() {
		List<Integer> returned = new LinkedList<>();
		int count = 0;
		for (Iterator<Tree<Integer>> it = TREE.getPreorderIterator(); it.hasNext(); count++) {
			returned.add(it.next().getValue());
			if (count > 100) {
				break;
			}
		}
		List<Integer> expected = Arrays.asList(3, 1, 4, 42, 7, 2, 8, 6, 5, 55, 555, 0);
		assertEquals(expected, returned);
	}
	
	@Test
	public void postorderIterator_success() {
		List<Integer> returned = new LinkedList<>();
		int count = 0;
		for (Iterator<Tree<Integer>> it = TREE.getPostorderIterator(); it.hasNext(); count++) {
			returned.add(it.next().getValue());
			if (count > 100) {
				break;
			}
		}
		List<Integer> expected = Arrays.asList(42, 4, 7, 1, 8, 5, 55, 555, 6, 2, 0, 3);
		assertEquals(expected, returned);
	}
	
	@Test
	public void bfsIterator_success() {
		List<Integer> returned = new LinkedList<>();
		int count = 0;
		for (Iterator<Tree<Integer>> it = TREE.getBFSIterator(); it.hasNext(); count++) {
			returned.add(it.next().getValue());
			if (count > 100) {
				break;
			}
		}
		List<Integer> expected = Arrays.asList(3, 1, 2, 0, 4, 7, 8, 6, 42, 5, 55, 555);
		assertEquals(expected, returned);
	}
}
