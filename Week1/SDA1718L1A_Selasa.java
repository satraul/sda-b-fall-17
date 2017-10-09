import java.util.*;
import java.io.*;

public class SDA1718L1A_Selasa {
    // Function to decrement integer without going negative
    public static int decPositive(int n){
        if (n==0) return 0;
        else {
            return n-1;
        }
    }

    public static void main (String[] args) throws IOException {
        // Read input n
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());

        // Initialize variables, one for each possible name
        int lala,momo,nana,rito;
        lala = 0;
        momo = 0;
        nana = 0;
        rito = 0;
        int maks = 0;
        String temp;

        //Read n line of inputs and increment variable according to name
        for (int i=0; i<n; i++){
            temp = br.readLine();
            if (temp.equals("Lala")) {
                lala+=1;
            } else if (temp.equals("Momo")) {
                momo+=1;
            } else if (temp.equals("Nana")) {
                nana+=1;
            } else {
                // Use decPositive on all girls when "Rito" is called
                lala = decPositive(lala);
                momo = decPositive(momo);
                nana = decPositive(nana);
            }
        }

        // Get the most called number of calls
        maks = Math.max(maks,lala);
        maks = Math.max(maks,momo);
        maks = Math.max(maks,nana);

        // Check for draw
        boolean a = lala==maks;
        boolean b = momo==maks;
        boolean c = nana==maks;
        String ans;
        if ((a&&b)||(b&&c)||(a&&c)){
            System.out.println("DRAW "+(maks));
        } else {
            // If not a draw
            if (a) ans = "LALA";
            else if (b) ans = "MOMO";
            else ans = "NANA";
            // Prints answer and number of calls
            System.out.println(ans+" "+(maks));
        }
    }
}
