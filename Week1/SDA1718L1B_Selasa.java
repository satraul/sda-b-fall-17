import java.util.*;
import java.io.*;

public class SDA1718L1B_Selasa {
    public static void main (String[] args) throws IOException {
        // Read input n
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());

        // Intialize variables
        int temp, mi, ma;
        mi = 100000000; // Set minimum to large number
        ma = 0; // Set maximum to 0
        for (int i=0; i<n; i++){
            // Read line
            temp = Integer.parseInt(br.readLine());
            // Updates minimum and large
            mi = Math.min(temp, mi);
            ma = Math.max(temp, ma);
        }

        // Prints the maximum possible difference
        System.out.println(Math.abs(mi-ma));
    }
}
