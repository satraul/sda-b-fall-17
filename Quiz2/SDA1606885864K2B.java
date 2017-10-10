import java.io.*;

public class SDA1606885864K2B {
    public static void main (String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        int n = Integer.parseInt(br.readLine());

        bw.write(solve(n));
        bw.flush();
    }

    public static String solve (int n){
        if (n==1) return "*";
        String tengah = repeat(n-1,"*\n")+"*";
        return concatMultiLineLR(solve(n-1),tengah);
    }

    public static String concatMultiLineLR (String one, String two){
        String[] ones = one.split("\n");
        String[] twos = two.split("\n");
        String ret = "";
        String lr;

        for(int i = 0;i<twos.length;i++){
            if (i-1>=0) lr = ones[i-1];
            else lr = repeat(ones[ones.length-1].length()," ");
            ret = ret + lr + twos[i] + lr + "\n";
        }

        return ret;
    }

    public static String repeat(int count, String with) {
        return new String(new char[count]).replace("\0", with);
    }

    public static String repeat(int count) {
        return repeat(count, " ");
    }
}
