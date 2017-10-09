import java.util.*;
import java.io.*;

public class SDA1718L3B_Selasa {
    //Main program
    public static void main(String[] args) throws IOException {
        //Intialize input
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        //Read all of the inputs
        String[] inps = br.readLine().split(" ");
        int n = Integer.parseInt(inps[0]), k = Integer.parseInt(inps[1]);
        String[] arrs = br.readLine().split(" ");
        int[] arr = new int[n];
        for (int i = 0;i<n;i++){
            arr[i] = Integer.parseInt(arrs[i]);
        }

        //Intialize and populate data structure

        int ans = 0;

        HashSet<Integer> possibleSet = new HashSet<Integer>();
        HashSet<Integer> temp = new HashSet<Integer>();
        possibleSet.add(0);

        for (int i = 0;i<n;i++){
            int cur = arr[i];
            temp.clear();
            for (int past: possibleSet){
                if(past+cur>=k){
                    //pass
                } else {
                    ans = Math.max(ans,past+arr[i]);
                    temp.add(past+arr[i]);
                }
            }
            possibleSet.addAll(temp);
        }

        bw.write("" + ans);
        bw.newLine();
        bw.close();
    }
}
