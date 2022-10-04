import java.io.*;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main_3 {
    private static InputReader in;
    private static PrintWriter out;

    public static char[] A;
    public static int N;
    public static int[] memo;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // Inisialisasi Array Input
        N = in.nextInt();
        A = new char[N];

        // Membaca File Input
        for (int i = 0; i < N; i++) {
            A[i] = in.nextChar();
        }

        memo = new int[N];

        // Run Solusi
        int solution = getMaxRedVotes(0, N - 1);
        out.print(solution);

        // Tutup OutputStream
        out.close();
    }

    public static int getMaxRedVotes(int start, int end) {
        // TODO : Implementasikan solusi rekursif untuk mendapatkan skor vote maksimal
        // untuk RED pada subarray A[start ... end] (inklusif)
        if (start == end) {
            if (A[start] == 'R') {
                return 1;
            } else {
                return 0;
            }
        } else if (memo[start] != 0) {
            return memo[start];
        } else {
            int max = 0;
            for (int i = start; i <= end; i++) {
                int tmp = dp(start, i);
                if (tmp > max) {
                    max = tmp;
                }
            }
            return max;
        }
    }
    public static int dp(int start, int index) {
        if (index == start) {
            if (A[index] == 'R') {
                return 1;
            } else {
                return 0;
            }
        } else if (memo[index] != 0) {
            return memo[index];
        } else {
            int voteRed = 0;
            int voteRedwithBlue = 0;
            int voteBlue = 0;
            int count = 0;
            int blueToRed = 0;
            for (int i = start; i <= index; i++) {
                int tmp = 0;
                tmp = dp(i, i);
                if (tmp == 0){
                    voteBlue++;
                    if (voteRed > voteBlue) {
                        voteRedwithBlue++;
                    }
                    else {
                        blueToRed++;
                    }
                }
                else {
                    voteRed += tmp;
                    voteRedwithBlue += tmp;
                    if (voteRed > voteBlue && blueToRed > 0) {
                        voteRedwithBlue++;
                        blueToRed--;
                    }
                }
                count++;
            }
            //out.println("voteRedwithBlue: " + voteRedwithBlue + " voteRed: " + voteRed+ " voteBlue: " + voteBlue + " count: " + count );

            memo[index] = voteRedwithBlue;
            return voteRedwithBlue;

        }
    }



    // taken from https://codeforces.com/submissions/Petr
    // together with PrintWriter, these input-output (IO) is much faster than the
    // usual Scanner(System.in) and System.out
    // please use these classes to avoid your fast algorithm gets Time Limit
    // Exceeded caused by slow input-output (IO)
    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

        public char nextChar() {
            return next().equals("R") ? 'R' : 'B';
        }
    }
}