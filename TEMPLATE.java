import java.util.*;
import java.io.*;

public class TEMPLATE {
    //Main program
    public static void main (String[] args) throws IOException {
        //Intialize input
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        //Temporary variables
        String[] inps;
        String inp, query;

        //Initialize data structures
        int[] arr = new int[100]; //Traditional array
        ArrayList<String> arrl = new ArrayList<>(); //Array list
        ArrayDeque<String> arrd = new ArrayDeque<String>(); //Stack or queue
        LinkedList<String> linl = new LinkedList<String>(); //Linked list
        Set<String> trees = new TreeSet<String>(); //Sorted set
        Set<String> hashs = new HashSet<String>(); //Hash set
        Map<String,Integer> treem = new TreeMap<String,Integer>(); //Sorted map
        Map<String,Integer> hashm = new HashMap<String,Integer>(); //Hash map

        //Populate data structures with initial data

        //Read n number of inputs
        int n = Integer.parseInt(br.readLine());
        for (int i = 0; i<n; i++) {
            inps = br.readLine().split(" ");
            query = inps[0];
            //Algorithm
            switch (query) {
                case "A":
                    break;
                case "B":
                    break;
                case "C":
                    break;
                default:
                    break;
            }
        }

        //Read until EOF
        while ((inp = br.readLine()) != null) {
            inps = inp.split(" ");
            query = inps[0];
            switch (query) {
                case "A":
                    break;
                case "B":
                    break;
                case "C":
                    break;
                default:
                    break;
            }
        }

        //After all input read
    }
}
