package bunnytests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import bunny.structure.BinaryTree;
import bunny.structure.Tree;

public class BinaryTreeTest {
	private static final BinaryTree<Integer> TREE = 
			new BinaryTree<>(3, 
					new BinaryTree<>(1,
							null,
							new BinaryTree<>(4)),
					new BinaryTree<>(2,
							new BinaryTree<>(6,
									new BinaryTree<>(5),
									null),
							new BinaryTree<>(8)));
	
	@Test
	public void toString_success() {
		assertEquals("3(1(,4),2(6(5,),8))", TREE.toString());
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
	
	@Test
	public void toTree_success() {
		assertEquals("3(1(4),2(6(5),8))", TREE.toTree().toString());
	}
	
	@Test
	public void asTree_success() {
		BinaryTree<Integer> btree = TREE.clone();
		Tree<Integer> tree = btree.asTree();
		assertEquals(tree.toString(), "3(1(4),2(6(5),8))");
		ArrayList<Tree<Integer>> nodes = new ArrayList<>();
		for (Iterator<Tree<Integer>> it = tree.getPostorderIterator(); it.hasNext();) {
			Tree<Integer> t = it.next();
			t.setValue(2 * t.getValue());
			if (t.getChildren().size() > 1) {
				nodes.add(t);
			}
		}
		for (Tree<Integer> node : nodes) {
			node.getChildren().remove(0);
		}
		assertEquals("6(4(16))", tree.toString());
		assertEquals("6(,4(,16))", btree.toString());
	}
}
