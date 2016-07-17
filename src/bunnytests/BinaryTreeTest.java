package bunnytests;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import bunny.structure.BinaryTree;

public class BinaryTreeTest {
	private static final BinaryTree<Integer> TREE = 
			new BinaryTree<Integer>(3, 
					new BinaryTree<Integer>(1,
							null,
							new BinaryTree<Integer>(4)),
					new BinaryTree<Integer>(2,
							new BinaryTree<Integer>(6,
									new BinaryTree<Integer>(5),
									null),
							new BinaryTree<Integer>(8)));
	
	@Test
	public void toString_success() {
		assertEquals(TREE.toString(), "3(1(,4),2(6(5,),8))");
	}
	
	@Test
	public void preorderIterator_success() {
		List<Integer> returned = new LinkedList<>();
		int count = 0;
		for (Iterator<BinaryTree<Integer>> it = TREE.getPreorderIterator(); it.hasNext(); count++) {
			returned.add(it.next().getValue());
			if (count > 100) {
				break;
			}
		}
		List<Integer> expected = Arrays.asList(3, 1, 4, 2, 6, 5, 8);
		assertEquals(expected, returned);
	}
	
	@Test
	public void inorderIterator_success() {
		List<Integer> returned = new LinkedList<>();
		int count = 0;
		for (Iterator<BinaryTree<Integer>> it = TREE.getInorderIterator(); it.hasNext(); count++) {
			returned.add(it.next().getValue());
			if (count > 100) {
				break;
			}
		}
		List<Integer> expected = Arrays.asList(1, 4, 3, 5, 6, 2, 8);
		assertEquals(expected, returned);
	}
	
	@Test
	public void postorderIterator_success() {
		List<Integer> returned = new LinkedList<>();
		int count = 0;
		for (Iterator<BinaryTree<Integer>> it = TREE.getPostorderIterator(); it.hasNext(); count++) {
			returned.add(it.next().getValue());
			if (count > 100) {
				break;
			}
		}
		List<Integer> expected = Arrays.asList(4, 1, 5, 6, 8, 2, 3);
		assertEquals(expected, returned);
	}
	
	@Test
	public void bfsIterator_success() {
		List<Integer> returned = new LinkedList<>();
		int count = 0;
		for (Iterator<BinaryTree<Integer>> it = TREE.getBFSIterator(); it.hasNext(); count++) {
			returned.add(it.next().getValue());
			if (count > 100) {
				break;
			}
		}
		List<Integer> expected = Arrays.asList(3, 1, 2, 4, 6, 8, 5);
		assertEquals(expected, returned);
	}
}
