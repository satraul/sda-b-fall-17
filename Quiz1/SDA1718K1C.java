import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class SDA1718K1C {
    //Main program
    public static void main (String[] args) throws IOException {
        //Intialize input
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        //Temporary variables
        String[] inps, arrs, query;

        //Initialize data structures
        ArrayList<Long> arrl = new ArrayList<Long>(); //Array list

        //Read N, Q and array
        inps = br.readLine().split(" ");
        arrs = br.readLine().split(" ");
        long temp;
        long sum = 0;
        arrl.add((long) 0);
        for (String inp : arrs) {
            temp = Long.parseLong(inp);
            sum += temp;
            arrl.add(sum);
        }

        //Read n number of queries

        for (int q = 0; q<Integer.parseInt(inps[1]); q++) {
            query = br.readLine().split(" ");
            int i = Integer.parseInt(query[0]);
            int j = Integer.parseInt(query[1]);
            int ans = 0;
            //Algorithm
            System.out.println(arrl.get(j)-arrl.get(i-1));
        }
    }
}
