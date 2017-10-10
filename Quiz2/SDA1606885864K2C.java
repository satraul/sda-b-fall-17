import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SDA1606885864K2C {
    public static void main (String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        String[] inps = br.readLine().split(" ");
        ArrayList<Integer> arr = new ArrayList<>();
        ArrayList<ArrayList<Integer>> tersegmen = new ArrayList<>();
        int n = Integer.parseInt(br.readLine());
        int banyakBagian = inps.length/n + (inps.length%n);
        String ans = "";

        for (int i = 0, inpsLength = inps.length; i < inpsLength; i++) {
            arr.add(Integer.parseInt(inps[i]));
        }
        int awal = 0;
        for(int i = 0;i<banyakBagian;i++){
            int awaln = Math.min(inps.length,awal+n);
            ArrayList<Integer> ar = new ArrayList<>();

            sort(arr,awal,awaln-1);
            for (int j=awal;j<awaln;j++){
                int k = arr.get(j);
                ar.add(k);
            }
            tersegmen.add(ar);
            awal+=n;
        }

        Collections.sort(tersegmen,aturan);
        boolean awa = true;
        for (int i = 0, tersegmenSize = tersegmen.size(); i < tersegmenSize; i++) {
            ArrayList<Integer> a = tersegmen.get(i);
            for (int j:
                 a) {
                if (!awa)
                    ans = ans + " " + j;
                else {
                    ans = j + "";
                    awa = false;
                }
            }
        }
        bw.write(ans);
        bw.flush();
    }

    private static final Comparator aturan = new Comparator() {
        public int compare(Object o1, Object o2) {
            ArrayList<Integer> a1 = (ArrayList<Integer>) o1;
            ArrayList<Integer> a2 = (ArrayList<Integer>) o2;
            for(int i = 0; i<a1.size();i++){
                int e1 = a1.get(i);
                int e2 = a2.get(i);
                if (e1!=e2){
                    return e1-e2;
                }
            }
            return a1.size()-a2.size();
        }
    };

    // Fungsi mergesort dari http://www.geeksforgeeks.org di modifikasi untuk soal ini
    private static void merge(List<Integer> arr, int l, int m, int r) {
        Integer L[] = new Integer[m - l + 1];
        Integer R[] = new Integer[r - m];

		/* Copy data to temp arrays */
		for (int i = 0; i < L.length; ++i){
		    L[i] = arr.get(i + l);
        }
        for (int j = 0; j < R.length; ++j) {
            R[j] = arr.get(m + 1 + j);
        }

		/* Merge the temp arrays */

        // Initial indexes of first and second subarrays
        int i = 0, j = 0;

        // Initial index of merged subarry array
        int k = l;
        while (i < L.length && j < R.length) {
            if (L[i].compareTo(R[j]) < 0) {
                arr.set(k, L[i]);
                i++;
            } else {
                arr.set(k, R[j]);
                j++;
            }
            k++;
        }

		/* Copy remaining elements of L[] if any */
        while (i < L.length) {
            arr.set(k, L[i]);
            i++;
            k++;
        }

		/* Copy remaining elements of R[] if any */
        while (j < R.length) {
            arr.set(k, R[j]);
            j++;
            k++;
        }
    }


    private static void sort(List<Integer> arr, int l, int r) {
        if (l < r) {
            // Find the middle point
            int m = (l + r) / 2;

            // Sort first and second halves
            sort(arr, l, m);
            sort(arr, m + 1, r);

            // Merge the sorted halves
            merge(arr, l, m, r);
        }
    }
}
