import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class SDA1718K1B {
    //Main program
    public static void main (String[] args) throws IOException {
        //Intialize input
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        //Temporary variables
        String[] inps;
        String inp, name, namePrev = null;
        int strength, playerno, strengthPrev = 0;

        //Initialize data structures
        Set<String> winners = new TreeSet<String>(); //Sorted set
        Map<String,Integer> bodyguards = new TreeMap<String,Integer>(); //Sorted map

        //Read until EOF
        while ((inp = br.readLine()) != null) {
            inps = inp.split(" ");
            name = inps[0];
            strength = Integer.parseInt((inps[1]));
            bodyguards.put(name,strength);
        }

        //After all input read
        playerno = 1;
        for(Map.Entry<String,Integer> entry : bodyguards.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            if (playerno==1){
                namePrev = key;
                strengthPrev = value;
                playerno+=1;
            } else {
                if (strengthPrev==value){
                    if(namePrev.compareTo(key)<0){
                        winners.add(namePrev);
                    } else {
                        winners.add(key);
                    }
                }
                else if (strengthPrev>value) winners.add(namePrev);
                else winners.add(key);
                playerno=1;
            }
        }

        ArrayList<Integer> n = new ArrayList<>(bodyguards.values());

        ArrayDeque<String> winnerStack = new ArrayDeque<String>();

        for (String s : winners) {
            winnerStack.add(s);
        }
        while (!winnerStack.isEmpty()){
            System.out.println(winnerStack.pollLast());
        }
    }
}
