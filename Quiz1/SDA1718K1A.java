import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class SDA1718K1A {
    //Main program
    public static void main (String[] args) throws IOException {
        //Intialize input
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        //Temporary variables
        String[] inps;

        //Initialize data structures
        ArrayDeque<Integer> queue = new ArrayDeque<Integer>(); //Traditional array
        int winnerCount = 0;
        Integer peeker = null;
        Integer peeked;

        //Populate data structures with initial data

        //Read n number of inputs
        br.readLine();
        inps = br.readLine().split(" ");
        for (String inp : inps) {
            queue.addLast(Integer.parseInt(inp));
        }

        //After all input read
        while(!queue.isEmpty()){
            peeker = queue.pollFirst();
            if (queue.isEmpty()){
                winnerCount+=1;
                break;
            }
            peeked = queue.peekFirst();

            if (peeker.equals(peeked)){
                winnerCount+=2;
                queue.pollFirst();
            } else if (peeker>peeked){
                winnerCount+=1;
                queue.pollFirst();
            }
        }
        System.out.println(winnerCount);
    }
}
