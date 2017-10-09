import java.io.*;

public class SDA1606885864L4B {
    public static void main (String[] args) throws IOException {
        //Initialize input
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        String[] inps;

        //Read n
        inps = br.readLine().split(" ");
        int N = Integer.parseInt(inps[0]), K = Integer.parseInt(inps[1]);

        //Read kucings
        Kucing[] kucings = new Kucing[N];
        for (int n = 0; n < N; n++) {
            inps = br.readLine().split(" ");
            kucings[n] = new Kucing(inps[0], Integer.parseInt(inps[1]));
        }

        //Read and process queries
        int mati = 0;
        for (int k = 0; k < K; k++) {
            String inp = br.readLine();
            inps = inp.split(" ");
            if (inp.equalsIgnoreCase("PRINT")) {
                //Call mergeSort (with sort by integer mode)
                sort(kucings, 0, kucings.length - 1, false);

                //Prints results
                if (mati >= kucings.length) {
                    bw.write("Semua Kucing Mati\n");
                } else {
                    for (Kucing kucing : kucings) {
                        if (kucing.isAlive()) bw.write(kucing + "\n");
                    }
                }
            } else if (inps[0].equalsIgnoreCase("DISERANG") && inps.length==2) {
                //Call mergeSort (with sort by name mode)
                sort(kucings, 0, kucings.length - 1, true);

                //Intialize variables
                int serangan = Integer.parseInt(inps[1]);
                int index = 0;

                //Distribute serangan to cats
                while (serangan > 0 && index < kucings.length) {
                    Kucing kucing = kucings[index];
                    if (kucing.isAlive()){
                        kucing.serang(serangan--);
                        if (!kucing.isAlive()) mati++;
                    } else {
                        kucing.serang(serangan--);
                    }
                    index++;
                }
            } else if (inp.equalsIgnoreCase("JUMLAH KUCING MATI")) {
                bw.write(mati+"\n");
            }
        }
        bw.flush();
    }

    static class Kucing {
        String nama;
        int darah;
        private Boolean alive = true;

        Kucing(String nama, int darah){
            this.nama = nama;
            this.darah = darah;
        }

        @Override
        public String toString() {
            return this.nama +  " " + this.darah;
        }

        int compareTo(Kucing o, boolean byString) {
            if (!byString){
                //Sort by integer mode
                if (this.darah == o.darah){
                    return o.nama.compareTo(this.nama);
                } else {
                    return this.darah - o.darah;
                }
            } else {
                //Sort by lexicography mode
                return o.nama.compareTo(this.nama);
            }
        }

        void serang(int serangan) {
            this.darah -= serangan;
            update();
        }

        void update(){
            if (isAlive()){
                if (darah<=0) setDead();
            }
        }

        Boolean isAlive() {
            return alive;
        }

        void setDead() {
            this.alive = false;
        }
    }

    // Fungsi mergesort dari http://www.geeksforgeeks.org di modifikasi untuk soal ini
    private static void merge(Kucing[] arr, int l, int m, int r, boolean byString) {
        Kucing L[] = new Kucing[m - l + 1];
        Kucing R[] = new Kucing[r - m];

		/* Copy data to temp arrays */
        System.arraycopy(arr, l, L, 0, L.length);
        for (int j = 0; j < R.length; ++j)
            R[j] = arr[m + 1 + j];

		/* Merge the temp arrays */

        // Initial indexes of first and second subarrays
        int i = 0, j = 0;

        // Initial index of merged subarry array
        int k = l;
        while (i < L.length && j < R.length) {
            if (L[i].compareTo(R[j], byString) > 0) {
                arr[k] = L[i];
                i++;
            } else {
                arr[k] = R[j];
                j++;
            }
            k++;
        }

		/* Copy remaining elements of L[] if any */
        while (i < L.length) {
            arr[k] = L[i];
            i++;
            k++;
        }

		/* Copy remaining elements of R[] if any */
        while (j < R.length) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }

    private static void sort(Kucing[] arr, int l, int r, boolean byString) {
        if (l < r) {
            // Find the middle point
            int m = (l + r) / 2;

            // Sort first and second halves
            sort(arr, l, m, byString);
            sort(arr, m + 1, r, byString);

            // Merge the sorted halves
            merge(arr, l, m, r, byString);
        }
    }
}