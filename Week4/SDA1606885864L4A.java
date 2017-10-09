import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SDA1606885864L4A {
    public static void main (String[] args) throws IOException {
        //Initialize input
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        //Read n
        int n = Integer.parseInt(br.readLine());

        //Read input array
        String[] arrs = br.readLine().split(" ");
        int[] arr = new int[arrs.length];
        for (int i = 0;i<arrs.length;i++){
            arr[i] = Integer.parseInt(arrs[i]);
        }

        //Call 3-fold merge sort on array
        mergeSort3(arr, 0, arr.length-1);

        //Print sorted array
        for (int i = 0; i < arr.length; i++) {
            if (i<arr.length-1){
                System.out.print(arr[i] + " ");
            } else {
                System.out.println(arr[i]);
            }
        }
    }

    private static void merge3 (int[] arr, int indexKiri, int pembagi, int pembagi2, int indexKanan) {
        //Memasukkan data di dataArr ke array temp baru (L, M, dan R)
        int[] L = new int[pembagi-indexKiri+1];
        for (int i = 0; i < L.length; i++) {
            L[i] = arr[i + indexKiri];
        }
        int[] M = new int[pembagi2-pembagi];
        for (int j = 0; j < M.length; j++) {
            M[j] = arr[j + pembagi + 1];
        }
        int[] R = new int[indexKanan-pembagi2];
        for (int k = 0; k < R.length; k++) {
            R[k] = arr[k + pembagi2 + 1];
        }

        //Melakukan sorting data
        int i = 0, j = 0, k = 0, l = indexKiri;

        while ((i < L.length) && (j < M.length) && (k < R.length)) {
            //Pilih nilai minimum antar L, M, dan R
            if (L[i] < M[j]) {
                if (L[i] < R[k])
                    arr[l++] = L[i++];
                else
                    arr[l++] = R[k++];
            } else {
                if (M[j] < R[k])
                    arr[l++] = M[j++];
                else
                    arr[l++] = R[k++];
            }
        }

        //Case: L dan M masih ada sisa
        while ((i < L.length) && (j < M.length)) {
            if (L[i] < M[j])
                arr[l++] = L[i++];
            else
                arr[l++] = M[j++];
        }

        //Case: M dan R masih ada sisa
        while ((j < M.length) && (k < R.length)) {
            if (M[j] < R[k])
                arr[l++] = M[j++];
            else
                arr[l++] = R[k++];
        }

        //Case: L dan R masih ada sisa
        while ((i < L.length) && (k < R.length)) {
            if (L[i] < R[k])
                arr[l++] = L[i++];
            else
                arr[l++] = R[k++];
        }

        //Masukkan sisa dari L, M dan R
        while (i < L.length) arr[l++] = L[i++];
        while (j < M.length) arr[l++] = M[j++];
        while (k < R.length) arr[l++] = R[k++];
    }

    private static void mergeSort3(int[] dataArr, int indexKiri, int indexKanan) {
        if (indexKiri < indexKanan) {
            //Isi nilai pembagi dan pembagi2 sebagai indeks pembatas untuk membagi data menjadi tiga bagian sama banyak
            int pembagi = indexKiri + ((indexKanan-indexKiri)/3);
            int pembagi2 = indexKiri + 2 * ((indexKanan-indexKiri)/3)+1;

            //Lakukan rekursif dengan memanggil kembali method ini untuk 3 data yang sudah dipecah
            mergeSort3(dataArr,indexKiri,pembagi);
            mergeSort3(dataArr,pembagi+1,pembagi2);
            mergeSort3(dataArr,pembagi2+1,indexKanan);

            //Panggil method merge() untuk menggabungkan 3 data dan mengurutkannya
            merge3(dataArr,indexKiri,pembagi,pembagi2,indexKanan);
        }
    }
}