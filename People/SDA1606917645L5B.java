import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;


public class SDA1606917645L5B {
	public static void main (String[] args) throws IOException{
		BufferedReader scan = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter print = new PrintWriter(new OutputStreamWriter(System.out));

		String tempInput = null;
		String [] inputs;

		Tree<String, Integer> tree = new Tree<>();

		while((tempInput = scan.readLine()) != null){
			inputs = tempInput.split(";");
			boolean status = false;
			switch(inputs[0]){
				case "REGISTER":
					status = tree.add(inputs[1], Integer.valueOf(inputs[2]));
					if (status){
						print.println(inputs[1] + ":" + inputs[2] + " berhasil ditambahkan");
					} else {
						print.println(inputs[1] + " sudah terdaftar di dalam sistem");
					}
					break;
				case "RESIGN":
					status = tree.remove(inputs[1]);
					if (status){
						print.println(inputs[1] + " mengundurkan diri");
					} else {
						print.println(inputs[1] + " tidak ditemukan di dalam sistem");
					}
					break;
				case "RETEST":
					if (tree.get(inputs[1]) != null){
						tree.get(inputs[1]).val = Integer.valueOf(inputs[2]);
						print.println(inputs[1] + ":" + inputs[2] + " perubahan berhasil");
					} else {
						print.println(inputs[1] + " tidak ditemukan di dalam sistem");
					}
					break;
				case "SMARTEST":
					if (!tree.isEmpty()) {
						ArrayList<String> listOfName = new ArrayList<>();
						int max = tree.get(tree.max()).val;
						for (String name : tree.inOrderAscending()) {
							if(tree.get(name).val == max){
								listOfName.add(name);
							}
						}
						for (int i = 0; i < listOfName.size()-1; i++){
							print.print(listOfName.get(i) + ", ");
						}
						print.println(listOfName.get(listOfName.size()-1) + " : " + max);
					} else {
						print.println("Tidak ada siswa yang terdaftar dalam sistem");
					}
					break;
				case "RANKING":
					if (!tree.isEmpty()) {
						String rank = ranking(tree.inOrderAscending(), tree);
						print.print(rank);
					} else {
						print.println("Tidak ada siswa yang terdaftar dalam sistem");
					}
					break;
			}
		}
		print.flush();
	}

	static String ranking(List<String> names, Tree<String, Integer> tree){
		HashSet<Integer> scores2 = new HashSet<>();
		HashMap<Integer, ArrayList<String>> mapOfNames = new HashMap<>();
		for (String name : names) {
			int tmp = tree.get(name).val;
			scores2.add(tmp);
		}
		ArrayList<Integer> scores = new ArrayList<>();
		for(int num: scores2){
			scores.add(num);
		}
		Collections.sort(scores);
		for (int score : scores){
			mapOfNames.put(score, new ArrayList<String>());
		}
		for (String name : names){
			mapOfNames.get(tree.get(name).val).add(name);
		}
		String str = "";
		for(int i = scores.size() -1; i >= 0; i--){
			str += (scores.size() -i)+ ". ";
			List<String> namesOfScores = mapOfNames.get(scores.get(i));
			for(int j = 0; j < namesOfScores.size()-1; j++){
				str += namesOfScores.get(j) + ", ";
			}
			str += namesOfScores.get(namesOfScores.size()-1);
			str += " : " + scores.get(i) + "\n";
		}
		return str;
	}
}

class Tree<K extends Comparable<K>, V extends Comparable<V>>{

	private Node<K, V> root;
	public Tree(){
		root = null;
	}
	public boolean isEmpty(){
		return root == null;
	}

	public boolean add(K key, V val){
		if (root == null) {
			root = new Node<>(key, val, null, null);
			return true;
		} else {
			return add(key, val, root, null);
		}
	}

	public boolean add(K key, V val, Node<K, V> current, Node<K, V> prev){
		if (current == null) {
			if (key.compareTo(prev.key) < 0)
				prev.left = new Node<>(key, val, null, null);
			else
				prev.right = new Node<>(key, val, null, null);
			return true;
		} else if (key.compareTo(current.key) < 0){
			return add(key, val, current.left, current);
		} else if (key.compareTo(current.key) > 0){
			return add(key, val, current.right, current);
		} else {
			return false;
		} }

	public boolean remove(K elem){
		if (root == null) {
			return false;
		} else {
			return remove(elem, root, null);
		}
	}

	private boolean remove(K elem, Node<K, V> current, Node<K, V> prev){
		if (current == null){
			return false;
		} else if (elem.compareTo(current.key) < 0){
			return remove(elem, current.left, current);
		} else if (elem.compareTo(current.key) > 0){
			return remove(elem, current.right, current);
		} else {
			if (current.key.compareTo(root.key) == 0){
				Node<K, V> tempNode = minNode(current.right, null);
				remove(minNode(current.right, null).key);
				root.key = tempNode.key;
			} else if (current.left == null && current.right == null){
				if(elem.compareTo(prev.key) < 0)
					prev.left = null;
				else
					prev.right = null;
			} else if (current.left == null || current.right == null){
				if(elem.compareTo(prev.key) < 0) {
					prev.left = (current.left != null)?current.left:current.right;
				} else {
					prev.right = (current.left != null)?current.left:current.right;
				}
			} else {
				Node<K, V> tempNode = minNode(current.right, null);
				remove(minNode(current.right, null).key);
				if(elem.compareTo(prev.key) < 0) {
					prev.left.key = tempNode.key;
				} else {
					prev.right.key = tempNode.key;
				}
			}
			return true;
		}
	}

	public Node<K, V> get(K key){
		if (root == null) return null;
		else return get(key, root);
	}

	public Node<K, V> get(K key, Node<K, V> current){
		if (current == null){
			return null;
		} else if (key.compareTo(current.key) < 0){
			return get(key, current.left);
		} else if (key.compareTo(current.key) > 0){
			return get(key, current.right);
		} else {
			return current;
		}
	}

	public K min(){
		if (root == null) return null;
		return minNode(root, null).key;
	}
	private Node<K, V> minNode(Node<K, V> current, Node<K, V> prev){
		if (current == null) return prev;
		else return minNode(current.left, current);
	}
	public K max(){
		if (root == null) return null;
		return maxNode(root, null).key;
	}
	private Node<K, V> maxNode(Node<K, V> current, Node<K, V> prev){
		if(current == null) return prev;
		else return maxNode(current.right, current);
	}
	public boolean contains(K elem){
		if (root == null) return false;
		Node<K, V> current = root;
		boolean found = false;
		while(current != null){
			if (elem.compareTo(current.key) < 0){
				current = current.left;
			} else if(elem.compareTo(current.key) > 0){
				current = current.right;
			} else {
				found = true;
				break;
			}
		}
		return found;
	}
	public List<K> preOrder(){
		if (root == null) return null;
		List<K> list = new LinkedList<>();
		preOrder(root, list);
		return list;
	}
	private void preOrder(Node<K, V> current, List<K> list){
		if (current != null){
			list.add(current.key);
			preOrder(current.left, list);
			preOrder(current.right, list);
		}
	}

	public List<K> postOrder(){
		if (root == null) return null;
		List<K> list = new LinkedList<>();
		postOrder(root, list);
		return list;
	}
	private void postOrder(Node<K, V> current, List<K> list){
		if (current != null){
			postOrder(current.left, list);
			postOrder(current.right, list);
			list.add(current.key);
		}
	}

	public List<K> inOrderAscending(){
		if (root == null) return null;
		List<K> list = new LinkedList<>();
		inOrderAscending(root, list);
		return list;
	}
	private void inOrderAscending(Node<K, V> current, List<K> list){
		if (current != null){
			inOrderAscending(current.left, list);
			list.add(current.key);
			inOrderAscending(current.right, list);
		}
	}

	public List<K> inOrderDescending(){
		if (root == null) return null;
		List<K> list = new LinkedList<>();
		inOrderDescending(root, list);
		return list;
	}
	private void inOrderDescending(Node<K, V> current, List<K> list){
		if (current != null){
			inOrderDescending(current.right, list);
			list.add(current.key);
			inOrderDescending(current.left, list);
		}
	}

	public static class Node<K, V> {
		K key;
		V val;
		Node<K, V> left, right;

		public Node(K key, V val, Node<K, V> left, Node<K, V> right) {
			this.key = key;
			this.val = val;
			this.left = left;
			this.right = right;
		}
	}
}

