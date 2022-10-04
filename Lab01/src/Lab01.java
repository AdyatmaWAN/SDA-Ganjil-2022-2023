import java.io.*;
import java.util.StringTokenizer;

public class Lab01 {
    private static InputReader in;
    private static PrintWriter out;

    static int getTotalDeletedLetters(int N, char[] x) {
        //save how many time the letter appears
        int so = 0;
        int of = 0;
        int fi = 0;
        int it = 0;
        int ta = 0;
        int as = 0;

        //how many times all letter appears
        int count = 0;

        //loop to check the combination and sequence of sofita
        for (int j = 0; j < N; j++) {
            count++;
            if (x[j] == 'S' ) {
                so++;
            }
            if (x[j] == 'O' & so >= 1 & of < so) {
                of++;
            }
            if (x[j] == 'F' & of >= 1 & fi < of) {
                fi++;
            }
            if (x[j] == 'I' & fi >= 1 & it < fi) {
                it++;
            }
            if (x[j] == 'T' & it >= 1 & ta < it) {
                ta++;
            }
            if (x[j] == 'A' & ta >= 1 & as < ta) {
                as++;
            }
            if (as == 1){ //if there is a complete sofita sequence, delete 6 letter
                count = count - 6;
                so -= 1;
                of -= 1;
                fi -= 1;
                it -= 1;
                ta -= 1;
                as -= 1;
            }
        }

        return count;
    }

    public static void main(String[] args) throws IOException {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // Read value of N
        int N = in.nextInt();

        // Read value of x
        char[] x = new char[N];
        for (int i = 0; i < N; ++i) {
            x[i] = in.next().charAt(0);
        }

        int ans = getTotalDeletedLetters(N, x);
        out.println(ans);

        // don't forget to close/flush the output
        out.close();
    }

    // taken from https://codeforces.com/submissions/Petr
    // together with PrintWriter, these input-output (IO) is much faster than the usual Scanner(System.in) and System.out
    // please use these classes to avoid your fast algorithm gets Time Limit Exceeded caused by slow input-output (IO)
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

    }
}