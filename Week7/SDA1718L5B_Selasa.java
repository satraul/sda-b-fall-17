import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SDA1718L5B_Selasa {
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

        //Find helper method
        Node<E> find(E element){
            return find(root, element);
        }

        //Recursive method for contains
        Node<E> find(Node<E> node, E element){
            if (node == null){
                return null;
            }
            if (element.compareTo(node.element) < 0) {
                return find(node.left, element);
            } else if (element.compareTo(node.element) > 0) {
                return find(node.right, element);
            } else {
                return node;
            }
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
    }

    static class Hero {
        String nama;
        int stamina, maxStamina, exp = 0, lvl = 1;

        Hero(String nama, int stamina) {
            this.nama = nama;
            this.maxStamina = stamina;
            this.stamina = stamina;
        }

        void clean(Kota kota) throws IOException {
            this.stamina -= kota.stamina;
            addExp(kota.pow);
        }

        void addExp(int exp) throws IOException {
            this.exp += exp;
            int newLvl = (this.exp/1000 + 1);
            if (newLvl > lvl){
                this.maxStamina += (200*(newLvl-lvl));
                this.lvl=newLvl;
                bw.write("LEVEL UP! "+nama+" level "+lvl+". Stamina maksimal: "+maxStamina+"\n");
            }
        }
    }

    static class Kota implements Comparable<Kota> {
        String nama;
        int pow, stamina;

        Kota(String nama, int pow, int stamina) {
            this.nama = nama;
            this.pow = pow;
            this.stamina = stamina;
        }

        @Override
        public int compareTo(Kota o) {
            return this.nama.compareTo(o.nama);
        }
    }

    static BufferedWriter bw;

    /** Main program, handles I/O **/
    public static void main (String[] args) throws IOException {
        //Initialize IO
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        bw = new BufferedWriter(new OutputStreamWriter(System.out));
        String inp;
        String[] inps;

        //Initialize Hero
        inp = br.readLine();
        inps = inp.split(" ");
        Hero hero = new Hero(inps[0],Integer.parseInt(inps[1]));
        bw.write("Welcome to Mobel Legenda! Hero: "+inps[0]+", Stamina awal: "+inps[1]+"\n");

        //Initialize BST
        BinarySearchTree<Kota> BST = new BinarySearchTree<Kota>();
        inp = br.readLine();
        inps = inp.split(" ");
        int m = Integer.parseInt(inps[0]), n = Integer.parseInt(inps[1]);

        //Populate BST with initial Kota
        for (int q=0;q<m;q++){
            inp = br.readLine();
            inps = inp.split(" ");
            BST.insert(new Kota(inps[0],Integer.parseInt(inps[1]),Integer.parseInt(inps[2])));
        }

        //Parses user input
        for (int q=0;q<n;q++){
            inp = br.readLine();
            inps = inp.split(" " );

            /* Basic tree methods: insert (build), find (inspect), and remove (demolish) */
            if (inps[0].equalsIgnoreCase("build")){
                BST.insert(new Kota(inps[1],Integer.parseInt(inps[2]),Integer.parseInt(inps[3])));
                bw.write(inps[1]+" dengan kekuatan "+inps[2]+" dibangun\n");
            } else if (inps[0].equalsIgnoreCase("inspect")){
                Node<Kota> node = BST.find(new Kota(inps[1],0,0));
                if (node!=null){
                    Kota x = node.element;
                    bw.write(x.nama+". Kekuatan: "+ x.pow +". Req. Stamina: " + x.stamina+"\n");
                }
                else bw.write(inps[1]+" tidak ada\n");
            } else if (inps[0].equalsIgnoreCase("demolish")){
                Kota temp = new Kota(inps[1], 0, 0);
                if (BST.contains(temp)){
                    BST.remove(temp);
                    bw.write(inps[1]+" dihancurkan\n");
                } else {
                    bw.write(inps[1]+ " tidak ada\n");
                }
            }

            /* Tree traverse methods: inOrder (world) and preOrder (explore) */
            if (inp.equalsIgnoreCase("world")){
                //Print tree in order
                bw.write("List kota:"+prettyPrintList(BST.inOrder()));
            } else if (inp.equalsIgnoreCase("explore")){
                //Loop through all the towns with pre order
                List<Kota> toVisit = BST.preOrder();
                for (Kota x: toVisit) {
                    //Clean town if can, skip if can't
                    if (hero.stamina >= x.stamina){
                        bw.write(hero.nama+" membersihkan kota "+x.nama+". Sisa stamina: "+(hero.stamina-x.stamina)+". Exp: "+(hero.exp+x.pow)+"\n");
                        hero.clean(x);
                    } else {
                        bw.write(x.nama+" tidak dibersihkan\n");
                    }
                }
                //Reset stamina
                hero.stamina = hero.maxStamina;
            }
        }

        bw.flush();
    }

    /* Formats list to string as per request in problemset */
    private static String prettyPrintList(List<Kota> list){
        StringBuilder ret = new StringBuilder();
        if (list.size() > 0) ret.append("\n");
        for (Kota e : list) {
            ret.append("- ").append(e.nama).append("\n");
        }
        return ret.toString();
    }
}
