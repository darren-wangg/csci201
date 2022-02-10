
/** **************************************************************************
 *                     The  generic Binary Search Tree class.
 *
 *****************************************************************************/

import java.util.*;
import java.io.*;
import java.net.*;

public class BST<T extends Comparable<T>> implements Cloneable, Iterable<T> {
	private Node<T> root;

	public Node<T> insertHelper(Node<T> node, T data) {
		// set root initially
		if (this.root == null) {
			this.root = new Node(data);
			return this.root;
		}

		if (node == null) {
			node = new Node(data);
			return node;
		}

		// less-than: search through left sub-tree & add
		if (data.compareTo(node.data) < 0) {
			node.left = this.insertHelper(node.left, data);
		}

		// greater-than: search through right sub-tree & add
		else {
			node.right = this.insertHelper(node.right, data);
		}

		return node;
	}

	/*****************************************************
	 * INSERT
	 ******************************************************/
	public void insert(T data) {
		// edge case
		if (data == null)
			return;

		// recursive insert helper
		this.insertHelper(this.root, data);
	}

	public boolean searchHelper(Node<T> node, T toSearch) {
		if (node == null)
			return false;

		// found: just return true
		if (node.data.equals(toSearch)) {
			return true;
		}

		// less-than: search left sub-tree
		if (toSearch.compareTo(node.data) < 0) {
			return searchHelper(node.left, toSearch);
		}
		// greater-than: search right sub-tree
		else {
			return searchHelper(node.right, toSearch);
		}
	}

	/*****************************************************
	 * SEARCH
	 ******************************************************/
	public boolean search(T toSearch) {
		// edge cases
		if (this.root == null || toSearch == null)
			return false;

		// root
		if (this.root.data == toSearch) {
			return true;
		}

		// recursive tree traversal
		return searchHelper(this.root, toSearch);
	}

	public void cloneHelper(Node<T> node, BST<T> clonedTree) {
		// base case
		if (node != null) {
			clonedTree.insert(node.data);
			// clone rest of tree
			this.cloneHelper(node.left, clonedTree);
			this.cloneHelper(node.right, clonedTree);
		}
	}

	/*****************************************************
	 * CLONE
	 ******************************************************/

	public BST<T> clone() {
		// edge case
		if (this.root == null)
			return null;

		BST<T> clonedTree = new BST<T>();

		this.cloneHelper(this.root, clonedTree);

		return clonedTree;
	}

	/*****************************************************
	 * TREE ITERATOR
	 ******************************************************/
	public Iterator<T> iterator() {
		return new BSTIterator(this.root);
	}

	private class BSTIterator implements Iterator<T> {
		Stack<Node<T>> stack;

		// cotr
		public BSTIterator(Node<T> node) {
			// stack instance var
			stack = new Stack<Node<T>>();
			Node<T> curr = node;

			// populate stack
			while (curr != null) {
				stack.push(curr);
				curr = curr.left;
			}
		}

		@Override
		public T next() {
			Node<T> output = stack.peek();
			Node<T> tmp = stack.pop().right;

			while (tmp != null) {
				stack.push(tmp);
				tmp = tmp.left;
			}

			return output.data;
		}

		@Override
		public boolean hasNext() {
			return !stack.isEmpty();
		}

		@Override
		public void remove() {
			// don't need this?
			throw new UnsupportedOperationException();
		}
	}

	/*****************************************************
	 * HEIGHT
	 ******************************************************/

	// Returns the height of the tree
	public int height() {
		return height(root);
	}

	private int height(Node<T> p) {
		return (p == null) ? -1 : 1 + Math.max(height(p.left), height(p.right));
	}

	/*****************************************************
	 * Node class
	 ******************************************************/

	private class Node<T> {
		private T data;
		private Node<T> left, right;

		/**
		 * Constructor builds the Node<T> with the supplied parameter
		 */
		public Node(T data) {
			left = right = null;
			this.data = data;
		}

		/**
		 * Constructor builds the Node<T> with the supplied parameter
		 */
		public Node(T data, Node<T> l, Node<T> r) {
			left = l;
			right = r;
			this.data = data;
		}

		/**
		 * Returns string representation of the object
		 */
		public String toString() {
			return data.toString();
		}
	}

	public static void main(String[] args) throws IOException {
		BST<Integer> tree = new BST<Integer>();
		int[] ar = { 6, 5, 1, 4, 3, 2, 9, 8 };
		for (int x : ar)
			tree.insert(Integer.valueOf(x));
		BST<Integer> twin = (BST<Integer>) tree.clone();
		tree.insert(new Integer(201));
		for (Integer x : tree)
			System.out.print(x + ",");
		System.out.println();
		for (Integer x : twin)
			System.out.print(x + ",");
		System.out.println();
		System.out.println("the tree height is " + tree.height());
		URL url = new URL("https://viterbi-web.usc.edu/~adamchik/dictionary.txt");
		Scanner sc = new Scanner(url.openStream());
		BST<String> dict = new BST<String>();
		while (sc.hasNext())
			dict.insert(sc.next());
		System.out.println(dict.search("integer"));
		System.out.println(dict.search("Integer"));
		int count = 0;
		for (String str : dict)
			count++;
		System.out.println("the dictionary size is " + count);
		System.out.println("the dictionary height is " + dict.height());
	}
}
