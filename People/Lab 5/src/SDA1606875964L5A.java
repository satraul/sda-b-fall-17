import java.util.LinkedList;
import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by fari.qodri on 10/27/2017.
 */
public class SDA1606875964L5A {
	public static void main(String[] args) throws IOException{
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String input;
		BSTree<String> tree = new BSTree<>();
		while((input = in.readLine()) != null) {
			String[] arr = input.split(";");
			switch(arr[0]) {
                case "ADD" :
                    if(tree.add(arr[1]) == true)
                        System.out.println(arr[1] + " berhasil ditambahkan ke dalam tree");
                    else System.out.println(arr[1] + " sudah dimasukkan sebelumnya");
                    break;

                case "REMOVE" :
                    if(tree.remove(arr[1]) != null) {
                        System.out.println(arr[1] + " berhasil dihapus dari tree");
                    }
                    else System.out.println(arr[1] + " tidak ditemukan");
                    break;

                case "CONTAINS" :
                    if(tree.contains(arr[1]) == true) {
                        System.out.println(arr[1] + " terdapat pada tree");
                    }
                    else System.out.println(arr[1] + " tidak terdapat pada tree");
                    break;

                case "PREORDER" :
                    if(!tree.isEmpty()) {
                        List<String> lst = tree.preOrder();
                        String names = "";
                        for(String s : lst) {
                            names += s + ";";
                        }
                        names = names.substring(0, names.length()-1);
                        System.out.println(names);
                    }
                    else System.out.println("Tidak ada elemen pada tree");
                    break;

                case "POSTORDER" :
                    if(!tree.isEmpty()) {
                        List<String> lst = tree.postOrder();
                        String names = "";
                        for(String s : lst) {
                            names += s + ";";
                        }
                        names = names.substring(0, names.length()-1);
                        System.out.println(names);
                    }
                    else System.out.println("Tidak ada elemen pada tree");
                    break;

                case "ASCENDING" :
                    if(!tree.isEmpty()) {
                        List<String> lst = tree.inOrderAscending();
                        String names = "";
                        for(String s : lst) {
                            names += s + ";";
                        }
                        names = names.substring(0, names.length()-1);
                        System.out.println(names);
                    }
                    else System.out.println("Tidak ada elemen pada tree");
                    break;

                case "DESCENDING" :
                    if(!tree.isEmpty()) {
                        List<String> lst = tree.inOrderDescending();
                        String names = "";
                        for(String s : lst) {
                            names += s + ";";
                        }
                        names = names.substring(0, names.length()-1);
                        System.out.println(names);
                    }
                    else System.out.println("Tidak ada elemen pada tree");
                    break;

			    case "MAX" :
                    if(!tree.isEmpty()) System.out.println(tree.max() + " merupakan elemen dengan nilai tertinggi");
                    else System.out.println("Tidak ada elemen pada tree");
                    break;

                case "MIN" :
                    if(!tree.isEmpty()) System.out.println(tree.min() + " merupakan elemen dengan nilai terendah");
                    else System.out.println("Tidak ada elemen pada tree");
                    break;
            }
		}
	}
}


/**
 *
 * Kelas Binary Search Tree
 * Mahasiswa tidak diwajibkan menggunakan template ini, namun sangat disarankan menggunakan template ini
 * Pada template ini, diasumsikan kelas yang ingin dipakai mengimplementasikan (implements) interface Comparable
 * NOTE : Tidak semua method yang dibutuhkan sudah disediakan templatenya pada kelas ini sehingga mahasiswa harus menambahkan sendiri method yang dianggap perlu
 * @author Jahns Christian Albert
 *
 */
class BSTree<E extends Comparable<? super E>>{

    /**
     *
     * Kelas yang merepresentasikan node pada tree
     * @author Jahns Christian Albert
     *
     */
    private static class Node<E> {

        E elem;
        Node<E> left;
        Node<E> right;
        Node<E> parent;

        /**
         *
         * Constructor
         *  elemen pada node
         * node kiri
         * param node kanan
         * param node parent
         *
         */
        public Node(E elem, Node<E> left, Node<E> right, Node<E> parent){

            this.elem = elem;
            this.left = left;
            this.right = right;
            this.parent = parent;

        }
    }

    private Node<E> root;

    /**
     *
     * Constructor Kelas Binary Search Tree
     *
     */
    public BSTree(){

        root = null;

    }

    /**
     *
     * Mengetahui apakah tree kosong atau tidak
     * @return true jika kosong, false jika sebaliknya
     *
     */
    public boolean isEmpty(){

        return root == null;

    }

    /**
     *
     * Menambahkan objek ke dalam tree
     * param elemen yang ingin ditambahkan
     * return true jika elemen berhasil ditambahkan, false jika elemen sudah terdapat pada tree
     *
     */
    public boolean add(E elem){

        //boolean res = false;

        if(root == null){

            // TO DO : Lengkapi bagian ini
            root = new Node<E>(elem, null, null,null);
            return true;

        } else {

            Node<E> prev = null;
            Node<E> current = root;
            while(current != null){

                E currElem = current.elem;
                if(elem.compareTo(currElem) < 0){

                    // TO DO : Lengkapi bagian ini
                	prev = current;
                    current = current.left;
                } else if(elem.compareTo(currElem) > 0){

                    // TO DO : Lengkapi bagian ini
                    prev = current;
                	current = current.right;

                } else {
                    // TO DO : Lengkapi bagian ini
                    return false;
                }
            }
            // TO DO : Lengkapi bagian ini
            if(elem.compareTo(prev.elem) < 0) {
                prev.left = new Node<E>(elem,null, null, prev);
            }
            else prev.right = new Node<E>(elem, null, null, prev);
        }
        return true;
    }

    /**
     *
     * Mendapatkan node dengan elemen tertentu
     * param elemen yang ingin dicari nodenya
     * return node dari elemen pada parameter, null jika tidak ditemukan
     *
     */
    private Node<E> find(E elem){

        Node<E> res = null;

        if(root != null){

            Node<E> current = root;
            boolean found = false;
            while(!found && current != null){

                E currElem = current.elem;
                if(elem.compareTo(currElem) < 0){
                    // TO DO : Lengkapi bagian ini
                    current  = current.left;

                } else if(elem.compareTo(currElem) > 0){
                    // TO DO : Lengkapi bagian ini
                    current = current.right;

                } else {
                    // TO DO : Lengkapi bagian ini
                    res = current;
                    found = true;
                }

            }

        }

        return res;

    }

    /**
     *
     * Menghapus objek dari tree, menggunakan successor inorder untuk menghapus elemen yang memiliki left node dan right node
     * Manfaatkan method minNode(Node<E> node) untuk mencari successor inorder
     * param elemen yang ingin dihapus
     * return true jika elemen ditemukan dan berhasil dihapus, false jika elemen tidak ditemukan
     *
     */
    public Node<E> remove(E elem){

        boolean res = false;

        Node<E> node = find(elem);

        if(node.left != null && node.right != null) {
            Node<E> successor = minNode(node.right);
            node.elem = successor.elem;
            node.right = remove(successor.elem);
        }
        else {
            if(node.left != null) {
                return node.left;
            }
            else if(node.right != null) {
                return node.right;
            }
            else {
                return null;
            }
        }
        return node;

    }

    /**
     *
     * Mencari elemen dengan nilai paling kecil pada tree
     * @return elemen dengan nilai paling kecil pada tree
     *
     */
    public E min(){

        E res = null;
        Node<E> minNode = minNode(root);

        if(minNode != null){

            res = minNode.elem;

        }

        return res;

    }

    /**
     *
     * Method untuk mengembalikan node dengan elemen terkecil pada suatu subtree
     * Hint : Manfaatkan struktur dari binary search tree
     * @param node root dari subtree yang ingin dicari elemen terbesarnya
     * @return node dengan elemen terkecil dari subtree yang diinginkan
     *
     */
    private Node<E> minNode(Node<E> node){

        Node<E> res = null;
        if(node != null){

            Node<E> current = node;
            // TO DO : Lengkapi bagian ini
            while(current.left != null) {
                current = current.left;
            }
            res = current;
        }
        return res;

    }

    /**
     *
     * Mencari elemen dengan nilai paling besar pada tree
     * @return elemen dengan nilai paling besar pada tree
     *
     */
    public E max(){

        E res = null;
        Node<E> maxNode = maxNode(root);

        if(maxNode != null){

            res = maxNode.elem;

        }

        return res;

    }

    /**
     *
     * Method untuk mengembalikan node dengan elemen terbesar pada suatu subtree
     * Hint : Manfaatkan struktur dari binary search tree
     * @param node root dari subtree yang ingin dicari elemen terbesarnya
     * @return node dengan elemen terbesar dari subtree yang diinginkan
     *
     */
    private Node<E> maxNode(Node<E> node){

        Node<E> res = null;
        if(node != null){
            Node<E> current = node;
            // TO DO : Lengkapi bagian ini
            while(current.right != null) {
                current = current.right;
            }
            res = current;
        }
        return res;

    }

    /**
     *
     * Mengetahui apakah sebuah objek sudah terdapat pada tree
     * Asumsikan jika elem.compareTo(otherElem) == 0, maka elem dan otherElem merupakan objek yang sama
     * Hint : Manfaatkan method find
     * param elemen yang ingin diketahui keberadaannya dalam tree
     * @return true jika elemen ditemukan, false jika sebaliknya
     *
     */
    public boolean contains(E elem){

        // TO DO : Lengkapi method ini
        return find(elem) != null;

    }

    /**
     * Mengembalikan tree dalam bentuk pre-order
     * @return tree dalam bentuk pre-order sebagai list of E
     */
    public List<E> preOrder(){

        List<E> list = new LinkedList<>(); // default menggunakan LinkedList, silahkan menggunakan List yang sesuai dengan Anda
        list = preOrder(root,list);
        return list;
    }

    /**
     *
     * Method helper dari preOrder()
     * @param node pointer
     * @param list sebagai akumulator
     * @return kumpulan elemen dari subtree yang rootnya adalah node parameter dengan urutan pre-order
     *
     */
    private List<E> preOrder(Node<E> node, List<E> list){

        // TO DO : Lengkapi method ini
    	if(node != null) {
        	list.add(node.elem);
        	preOrder(node.left, list);
        	preOrder(node.right, list);
    	}
    	return list;
    }

    /**
     * Mengembalikan tree dalam bentuk post-order
     * @return tree dalam bentuk post-order sebagai list of E
     */
    public List<E> postOrder(){

        List<E> list = new LinkedList<>(); // default menggunakan LinkedList, silahkan menggunakan List yang sesuai dengan Anda
        list = postOrder(root,list);
        return list;
    }

    /**
     *
     * Method helper dari postOrder()
     * @param node pointer
     * @param list sebagai akumulator
     * @return kumpulan elemen dari subtree yang rootnya adalah node parameter dengan urutan post-order
     *
     */
    private List<E> postOrder(Node<E> node, List<E> list){

        // TO DO : Lengkapi method ini
    	if(node != null) {
        	postOrder(node.left, list);
        	postOrder(node.right, list);
        	list.add(node.elem);
    	}
        return list;
    }


    /**
     * Mengembalikan tree dalam bentuk in-order secara ascending
     * @return tree dalam bentuk in-order secara ascending sebagai list of E
     */
    public List<E> inOrderAscending(){

        List<E> list = new LinkedList<>(); // default menggunakan LinkedList, silahkan menggunakan List yang sesuai dengan Anda
        list = inOrderAscending(root,list);
        return list;
    }

    /**
     *
     * Method helper dari inOrderAscending()
     * @param node pointer
     * @param list sebagai akumulator
     * @return kumpulan elemen dari subtree yang rootnya adalah node parameter dengan urutan in-order secara ascending
     *
     */
    private List<E> inOrderAscending(Node<E> node, List<E> list){

        // TO DO : Lengkapi method ini
    	if(node != null) {
        	inOrderAscending(node.left, list);
        	list.add(node.elem);
        	inOrderAscending(node.right, list);
    	}
        return list;
    }


    /**
     * Mengembalikan tree dalam bentuk in-order secara descending
     * @return tree dalam bentuk in-order secara descending sebagai list of E
     */
    public List<E> inOrderDescending(){

        List<E> list = new LinkedList<>(); // default menggunakan LinkedList, silahkan menggunakan List yang sesuai dengan Anda
        list = inOrderDescending(root,list);
        return list;

    }

    /**
     *
     * Method helper dari inOrderDescending()
     * @param node pointer
     * @param list sebagai akumulator
     * @return kumpulan elemen dari subtree yang rootnya adalah node parameter dengan urutan in-order descending
     *
     */
    private List<E> inOrderDescending(Node<E> node, List<E> list){

        // TO DO : Lengkapi method ini
    	if(node != null) {
    		inOrderDescending(node.right, list);
        	list.add(node.elem);
        	inOrderDescending(node.left, list);
    	}
    	return list;
    }

}