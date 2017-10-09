import java.util.*;
import java.io.*;

public class SDA1718TUGAS1 {
    //Main program
    public static void main (String[] args) throws IOException {
        //Intialize input
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        //Temporary variables
        String[] inp;
        String query, cat;

        //Initialize data structures
        ArrayList<ArrayDeque<String>> tabs = new ArrayList<ArrayDeque<String>>(); //Stack
        Set<String> allHistory = new TreeSet<String>(); //Tree so it's lexicographically sorted

        //Create default first tab and set as active
        tabs.add(new ArrayDeque<String>());
        ArrayDeque<String> activeTab = tabs.get(0);

        //Read n number of inputs
        int n = Integer.parseInt(br.readLine());
        for (int i = 0; i<n; i++){
            inp = br.readLine().split(";");
            query = inp[0];
            //Switch for each possible query
            switch (query){
                case "VIEW":
                    cat = inp[1]+":"+inp[2];
                    //Add cat (e.g garong:kampung) to tab and history
                    activeTab.add(cat);
                    allHistory.add(cat);
                    //Print result
                    System.out.printf("VIEWING([%s])\n",cat);
                    break;
                case "BACK":
                    //Pop from stack
                    if (!activeTab.isEmpty()) activeTab.pollLast();
                    //Print result
                    if (activeTab.isEmpty()) System.out.println("EMPTY TAB");
                    else System.out.printf("VIEWING([%s])\n",activeTab.getLast());
                    break;
                case "NEWTAB":
                    tabs.add(new ArrayDeque<String>());
                    break;
                case "CHANGETAB":
                    int j = Integer.parseInt(inp[1]);
                    //Switch activateTab and print result
                    if (j<tabs.size()){
                        activeTab = tabs.get(j);
                        System.out.println("TAB: " + j);
                    } else {
                        System.out.println("NO TAB");
                    }
                    break;
                case "HISTORY":
                    //Print all elements in history if exists
                    if (allHistory.isEmpty()){
                        System.out.println("NO RECORD");
                    } else {
                        for (String history:
                                allHistory) {
                            System.out.println(history);
                        }
                    }
                    break;
            }
        }
    }
}
