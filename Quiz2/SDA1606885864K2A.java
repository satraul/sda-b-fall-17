import java.io.*;
import java.util.ArrayDeque;
import java.util.ArrayList;

public class SDA1606885864K2A {
    public static void main (String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        ArrayList<ArrayDeque<Integer>> allQueues = new ArrayList<>();
        int[] waitSum = new int[50000];
        allQueues.add(new ArrayDeque<Integer>());

        String[] inps;
        String inp;
        long waitOfBestQueue = 0;
        int indexOfBestQueue = 0;

        int N = Integer.parseInt(br.readLine());

        for(int n = 0;n<N;n++){
            inp = br.readLine();
            inps = inp.split(" ");

            if (inps[0].equalsIgnoreCase("ADD")){
                int add = Integer.parseInt(inps[1]);
                allQueues.get(indexOfBestQueue).addLast(add);
                bw.write(waitSum[indexOfBestQueue]+"\n");
                waitSum[indexOfBestQueue] += add;
                for (int i = 0;i < allQueues.size(); i++) {
                    if (waitSum[i] <= waitOfBestQueue){
                        waitOfBestQueue = waitSum[i];
                        indexOfBestQueue = i;
                    }
                }
            } else if (inp.equalsIgnoreCase("SERVE")){
                int max = 0;
                for (int i = 0; i < allQueues.size(); i++) {
                    ArrayDeque<Integer> queue = allQueues.get(i);
                    if (!queue.isEmpty()) {
                        int out = queue.pollFirst();
                        max = Math.max(max, out);
                        waitSum[i]-=out;
                    }
                    if (waitSum[i] <= waitOfBestQueue){
                        waitOfBestQueue = waitSum[i];
                        indexOfBestQueue = i;
                    }
                }
                bw.write(max + "\n");
            } else if (inp.equalsIgnoreCase("NEW QUEUE")){
                allQueues.add(new ArrayDeque<Integer>());
                waitOfBestQueue = 0;
                indexOfBestQueue = allQueues.size()-1;
            }

        }
        bw.flush();
    }
}
