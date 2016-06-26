package javatests;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import structure.BinaryTree;

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
			returned.add(it.next().value);
			if (count > 100) {
				break;
			}
		}
		List<Integer> expected = Arrays.asList(3, 1, 4, 2, 6, 5, 8);
		assertEquals(returned, expected);
	}
}
