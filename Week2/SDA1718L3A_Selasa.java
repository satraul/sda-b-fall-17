import java.io.*;
import java.util.*;

public class SDA1718L3A_Selasa {
    public static void addOne (TreeMap<Integer,Integer> treem, Integer e){
        if(treem.containsKey(e)){
            int current = treem.get(e);
            treem.put(e,current+1);
        } else {
            treem.put(e,1);
        }
    }

    public static void removeOne (TreeMap<Integer,Integer> treem, Integer e){
        int current = treem.get(e);
        if (current == 1){
            treem.remove(e);
        } else {
            treem.put(e,current-1);
        }
    }

    //Main program
    public static void main (String[] args) throws IOException {
        //Intialize input
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        //Temporary variables
        String[] inps;
        String query;

        //Initialize data structures
        ArrayDeque<Integer> arrd = new ArrayDeque<Integer>(); //Queue
        TreeMap<Integer, Integer> treem = new TreeMap<Integer, Integer>(); //Sorted map of height and amount in queue

        //Read n number of inputs
        int n = Integer.parseInt(br.readLine());
        for (int i = 0; i<n; i++) {
            inps = br.readLine().split(" ");
            query = inps[0];
            //Algorithm
            switch (query) {
                case "0":
                    int in = Integer.parseInt(inps[1]);
                    arrd.offer(in);
                    addOne(treem, in);
                    System.out.println(in);
                    break;
                case "1":
                    if(arrd.isEmpty()){
                        System.out.println(-1);
                    } else {
                        int out = arrd.poll();
                        removeOne(treem, out);
                        System.out.println(out);
                    }
                    break;
                case "2":
                    if(arrd.isEmpty()){
                        System.out.println(-1);
                    } else {
                        System.out.println(treem.firstKey());
                    }
                    break;
            }
        }
    }
}
