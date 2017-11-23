import java.io.*;
import java.util.*;

public class SDA1606885864K3B_Selasa {
    public static void main (String[] args) throws IOException {
        //Intialize input
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        //Temporary variables
        String[] inps;

        class ListMantap implements Comparable {
            int head;
            Queue<Integer> list = new ArrayDeque<>();

            void add (int e){
                if (list.isEmpty()) head = e;
                list.offer(e);
            }

            int poll (){
                int ret = list.poll();
                if (!list.isEmpty()) head = list.peek();
                else head = 0;
                return ret;
            }

            boolean isEmpty() {
                return list.isEmpty();
            }

            @Override
            public int compareTo(Object o) {
                if (list.isEmpty()) return 1;
                return head - ((ListMantap) o).head;
            }
        }

        //Read n number of inputs
        int n = Integer.parseInt(br.readLine());
        PriorityQueue<ListMantap> listOfAllPq = new PriorityQueue<ListMantap>(n);
        for (int i = 0; i<n; i++) {
            inps = br.readLine().split(" ");
            ListMantap temp = new ListMantap();
            for (String e:
                    inps) {
                temp.add(Integer.parseInt(e));
            }
            listOfAllPq.add(temp);
        }

        ListMantap first;
        StringBuilder sb = new StringBuilder("");
        while (!listOfAllPq.isEmpty()){
            first = listOfAllPq.poll();
            sb.append(first.poll() + " ");
            if (!first.isEmpty()) listOfAllPq.add(first);
        }
        bw.write(sb.toString());
        bw.flush();
    }
}
