import java.util.*;
import java.io.*;

public class SDA1606885864K3A_Selasa {
    //Main program
    static class BST {
        class Node {
            private int data;
            private Node left;
            private Node right;

            Node (int data){
                this.data = data;
            }
        }

        Node root;
        BST() {
            root = null;
        }

        Node lowestNodeThatContainsTheseTwoChilds(int c1, int c2){
            return recLowestNodeThatContainsTheseTwoChilds(root, c1, c2);
        }

        Node recLowestNodeThatContainsTheseTwoChilds(Node root, int c1, int c2){
            if (root == null) return null;
            if (containsRec(root, c1) && containsRec(root, c2)){
                Node left = recLowestNodeThatContainsTheseTwoChilds(root.left, c1, c2);
                Node right = recLowestNodeThatContainsTheseTwoChilds(root.right, c1, c2);
                if (left==null && right == null){
                    return root;
                } else {
                    return (left!=null)?left:right;
                }
            } else {
                return null;
            }
        }

        boolean containsRec(Node root, int data) {
            if (root == null) return false;
            else if (root.data == data) return true;
            else if (root.data > data) return containsRec(root.left, data);
            else return containsRec(root.right, data);
        }

        Node search(int data){
            return searchRec(root, data);
        }

        Node searchRec(Node root, int data) {
            // Base Cases: root is null or key is present at root
            if (root == null || root.data == data)
                return root;

            // val is greater than root's key
            if (root.data > data)
                return searchRec(root.left, data);

            // val is less than root's key
            return searchRec(root.right, data);
        }

        void insert(int key) {
            root = insertRec(root, key);
        }

        Node insertRec(Node root, int data) {
            if (root == null) {
                root = new Node(data);
                return root;
            }
            if (data < root.data)
                root.left = insertRec(root.left, data);
            else if (data > root.data)
                root.right = insertRec(root.right, data);
            return root;
        }

        List<Integer> listTraverseFromTo(Node from, int target) {
            if (from == null) return null;

            List<Integer> list = new ArrayList<Integer>(from.data);

            while (from != null) {
                list.add(from.data);
                if (from.data == target) {
                    return list;
                } else if (from.data > target) {
                    from = from.left;
                } else {
                    from = from.right;
                }
            }
            System.out.println("fuck");
            return null;
        }
    }

    public static void main (String[] args) throws IOException {
        //Intialize input
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        //Initialize BST
        BST bst = new BST();

        //Temporary variables
        String[] inps;

        //Read n number of inputs
        int n = Integer.parseInt(br.readLine());

        //Read all nodes and insert into tree
        inps = br.readLine().split(" ");
        for (String e:
             inps) {
            bst.insert(Integer.parseInt(e));
        }

        //Read from and target, get max value
        inps = br.readLine().split(" ");
        int x = Integer.parseInt(inps[0]);
        int y = Integer.parseInt(inps[1]);

        BST.Node parent = bst.lowestNodeThatContainsTheseTwoChilds(x, y);
        List<Integer> list1 = bst.listTraverseFromTo(parent, x);
        List<Integer> list2 = bst.listTraverseFromTo(parent, y);
        int max = Integer.MIN_VALUE;
        for (int e:
             list1) {
            max = Math.max(e,max);
        }
        for (int e:
             list2) {
            max = Math.max(e,max);
        }
        System.out.println(max);
    }
}
