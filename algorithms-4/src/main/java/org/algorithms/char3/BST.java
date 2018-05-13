package org.algorithms.char3;

import java.util.Arrays;

import org.algorithms.util.Utils;

/**
 * 二叉树
 * 
 * @author gbs
 *
 * @param <Key>
 * @param <Value>
 */
public class BST<Key extends Comparable<Key>, Value> {

	private Node root;

	private class Node {
		private Key key; // 键
		private Value val; // 值
		private Node left, right; // 子树链接
		private int n; // 该节点子树中的节点总数

		public Node(Key key, Value val, int n) {
			this.key = key;
			this.val = val;
			this.n = n;
		}
	}

	public int size() {
		return size(root);
	}

	private int size(Node x) {
		if (x == null) {
			return 0;
		} else {
			return x.n;
		}
	}

	public Value get(Key key) {
		return get(root, key);
	}

	private Value get(Node x, Key key) {
		if (x == null) {
			return null;
		}

		int result = key.compareTo(x.key);
		if (result < 0) {
			return get(x.left, key);
		} else if (result > 0) {
			return get(x.right, key);
		} else {
			return x.val;
		}
	}

	public void put(Key key, Value val) {
		root = put(root, key, val);
	}

	/**
	 * 将简直对存入表中
	 * 
	 * @param x
	 * @param key
	 * @param val
	 * @return
	 */
	private Node put(Node x, Key key, Value val) {
		if (x == null) {
			return new Node(key, val, 1);
		}
		int result = key.compareTo(x.key);
		if (result < 0) {
			x.left = put(x.left, key, val);
		} else if (result > 0) {
			x.right = put(x.right, key, val);
		} else {
			x.val = val;
		}
		x.n = size(x.left) + size(x.right) + 1;
		return x;
	}

	public Key min() {
		return min(root).key;
	}

	/**
	 * 最小值
	 * 
	 * @param x
	 * @return
	 */
	private Node min(Node x) {
		if (x.left == null) {
			return x;
		}
		return min(x.left);
	}

	public Key max() {
		return max(root).key;
	}

	private Node max(Node x) {
		if (x.right == null) {
			return x;
		}
		return max(x.right);
	}

	/**
	 * 小于等于key的最大值
	 * 
	 * @param key
	 * @return
	 */
	public Key floor(Key key) {
		Node x = floor(root, key);
		if (x == null) {
			return null;
		}
		return x.key;
	}

	private Node floor(Node x, Key key) {
		if (x == null) {
			return null;
		}
		int result = key.compareTo(x.key);
		if (result == 0) {
			return x;
		} else if (result < 0) {
			return floor(x.left, key);
		}

		Node t = floor(x.right, key);
		if (t == null) {
			return x;
		}
		return t;
	}

	public Key ceiling(Key key) {
		Node x = ceiling(root, key);
		if (x == null) {
			return null;
		}
		return x.key;
	}

	private Node ceiling(Node x, Key key) {
		if (x == null) {
			return null;
		}
		int result = key.compareTo(x.key);
		if (result == 0) {
			return x;
		} else if (result > 0) {
			return ceiling(x.right, key);
		}

		Node t = ceiling(x.left, key);
		if (t == null) {
			return x;
		}
		return t;
	}

	public void deleteMin() {
		root = deleteMin(root);
	}

	private Node deleteMin(Node x) {
		if (x.left == null) {
			return x.right;
		}
		x.left = deleteMin(x.left);
		x.n = size(x.left) + size(x.right) + 1;
		return x;
	}

	public void delete(Key key) {
		root = delete(root, key);
	}

	private Node delete(Node x, Key key) {
		if (x == null) {
			return null;
		}
		int result = key.compareTo(x.key);
		if (result < 0) {
			x.left = delete(x.left, key);
		} else if (result > 0) {
			x.right = delete(x.right, key);
		} else {
			if (x.right == null) {
				return x.left;
			}
			if (x.left == null) {
				return x.right;
			}
			Node t = x;
			x = min(t.right);
			x.left = t.left;
			x.right = deleteMin(t.right);

		}
		x.n = size(x.left) + size(x.right) + 1;
		return x;
	}
	
	public void print(){
		print(root);
	}
	
	private void print(Node x){
		if(x==null)return;
		print(x.left);
		System.out.print(x.key+" ");
		print(x.right);
	}
	
	public static void main(String[] args) {
		BST<String, Integer> bst = new BST<String, Integer>();
		String[] allStrings = Utils.readAllStrings("tinyST.txt");
		System.out.println(Arrays.toString(allStrings));
		for(int i = 0;i<allStrings.length;i++){
			bst.put(allStrings[i], i);
		}
		bst.print();
	}

}
