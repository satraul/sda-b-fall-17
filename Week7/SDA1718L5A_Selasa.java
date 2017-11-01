import java.util.*;
import java.io.*;

public class SDA1718L5A_Selasa {
    /** Node class for use in tree **/
    static class Node<E extends Comparable<E>> {
        E element;
        Node<E> left;
        Node<E> right;

        public Node(E element) {
            this.element = element;
            this.left = null;
            this.right = null;
        }
    }

    /** Binary search tree class with Node<E> as elements **/
    public static class BinarySearchTree<E extends Comparable<E>> {
        private Node<E> root;

        //Constructor
        BinarySearchTree() {
            root = null;
        }

        //Insert helper method
        void insert(E element){
            root = insert(root, element);
        }

        //Recursive method for insert
        Node<E> insert(Node<E> node, E element){
            if (node == null) {
                node = new Node<E>(element);
            }
            if (element.compareTo(node.element) < 0) {
                node.left = insert(node.left, element);
            } else if (element.compareTo(node.element) > 0) {
                node.right = insert(node.right, element);
            }
            return node;
        }

        //Remove helper method
        void remove(E element){
            if (contains(element))
            root = remove(root, element);
        }

        //Recursive method for remove
        Node<E> remove(Node<E> node, E element){
            if (node == null) {
                return null;
            }
            if (element.compareTo(node.element) < 0) {
                node.left = remove(node.left, element);
            } else if (element.compareTo(node.element) > 0) {
                node.right = remove(node.right, element);
            } else {
                if (node.left != null && node.right != null) {
                    E sucessor = min(node.right);
                    node.element = sucessor;
                    node.right = remove(node.right, sucessor);
                } else if (node.left != null) {
                    return node.left;
                } else if (node.right != null) {
                    return node.right;
                } else {
                    return null;
                }
            }
            return node;
        }

        //Contains helper method
        boolean contains(E element){
            return contains(root, element);
        }

        //Recursive method for contains
        boolean contains(Node<E> node, E element){
            if (node == null){
                return false;
            }
            if (element.compareTo(node.element) < 0) {
                return contains(node.left, element);
            } else
                return element.compareTo(node.element) <= 0 || contains(node.right, element);
        }

        //Loop interation method for contains
        E min(Node<E> node){
            while (node.left != null) node = node.left;
            return node.element;
        }

        //Returns list of tree elements in order
        List<E> inOrder(){
            List<E> list = new ArrayList<E>();
            inOrderTraverse(root, list);
            return list;
        }

        //Traverse tree in order and adds to list
        private void inOrderTraverse(Node<E> node, List<E> list){
            if (node == null) return;
            if (node.left != null) inOrderTraverse(node.left, list);
            list.add(node.element);
            if (node.right != null) inOrderTraverse(node.right, list);
        }

        //Returns list of tree elements pre order
        List<E> preOrder(){
            List<E> list = new ArrayList<E>();
            preOrderTraverse(root, list);
            return list;
        }

        //Traverse tree pre order and adds to list
        private void preOrderTraverse(Node<E> node, List<E> list){
            if (node == null) return;
            list.add(node.element);
            if (node.left != null) preOrderTraverse(node.left, list);
            if (node.right != null) preOrderTraverse(node.right, list);
        }

        //Returns list of tree elements post order
        List<E> postOrder(){
            List<E> list = new ArrayList<E>();
            postOrderTraverse(root, list);
            return list;
        }

        //Traverse tree post order and adds to list
        private void postOrderTraverse(Node<E> node, List<E> list){
            if (node == null) return;
            if (node.left != null) postOrderTraverse(node.left, list);
            if (node.right != null) postOrderTraverse(node.right, list);
            list.add(node.element);
        }
    }

    /** Main program, handles I/O **/
    public static void main (String[] args) throws IOException {
        //Initialize IO
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        String inp;
        String[] inps;
        int element;

        //Initialize BST
        BinarySearchTree<Integer> BST = new BinarySearchTree<Integer>();
        br.readLine();
        inp = br.readLine();
        inps = inp.split(" ");
        for (String s: inps){
            BST.insert(Integer.parseInt(s));
        }

        //Parses user input
        int n = Integer.parseInt(br.readLine());
        for (int q=0;q<n;q++){
            inp = br.readLine();
            inps = inp.split(" ");
            /* Basic tree methods (add, find, delete) */
            if (inps[0].equalsIgnoreCase("add")) {
                element = Integer.parseInt(inps[1]);
                BST.insert(element);
                bw.write(inps[1] + " ditambahkan\n");
            } else if (inps[0].equalsIgnoreCase("find")) {
                element = Integer.parseInt(inps[1]);
                if (BST.contains(element)) bw.write("ADA\n");
                else bw.write("TIDAK ADA\n");
            } else if (inps[0].equalsIgnoreCase("delete")) {
                element = Integer.parseInt(inps[1]);
                if (BST.contains(element)){
                    BST.remove(element);
                    bw.write(inps[1] + " dihapus\n");
                }
                else bw.write(inps[1] + " tidak ada\n");
            }
            /* Tree traversal methods (preoder, inorder, postorder) */
            if (inp.equalsIgnoreCase("preorder")) {
                bw.write("Pre-order:");
                List<Integer> preOrder = BST.preOrder();
                bw.write(prettyPrintList(preOrder));
            } else if (inp.equalsIgnoreCase("inorder")) {
                bw.write("In-order:");
                List<Integer> inOrderAscending = BST.inOrder();
                bw.write(prettyPrintList(inOrderAscending));
            } else if (inp.equalsIgnoreCase("postorder")) {
                bw.write("Post-order:");
                List<Integer> postOrder = BST.postOrder();
                bw.write(prettyPrintList(postOrder));
            }
        }

        bw.flush();
    }

    /* Formats list to string as per request in problemset */
    private static String prettyPrintList(List<Integer> list){
        StringBuilder ret = new StringBuilder();
        if (list.size() > 0) ret.append(" ");
        for (int i = 0, listSize = list.size(); i < listSize; i++) {
            int e = list.get(i);
            if (i<listSize-1) ret.append(e).append(", ");
            else ret.append(e).append("\n");
        }
        return ret.toString();
    }
}
