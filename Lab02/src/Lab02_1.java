//handle kue maks hanya 3
/* test case
solusi : geser kiri :)
4
4
16
0 3 2 1
1 0 3 2
2 1 0 3
3 2 1 0
BELI_RASA 1
BELI_RASA 2
BELI_RASA 3
BELI_RASA 0
BELI_RASA 2
BELI_RASA 3
BELI_RASA 0
BELI_RASA 1
BELI_RASA 3
BELI_RASA 0
BELI_RASA 1
BELI_RASA 2
BELI_RASA 0
BELI_RASA 1
BELI_RASA 2
BELI_RASA 3
 */

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.StringTokenizer;

public class Lab02_1 {
    // TODO : Silahkan menambahkan struktur data yang diperlukan
    private static InputReader in;
    private static PrintWriter out;

    private static int count;

    static int geserKanan(int N, List<Stack<Integer>> toples) {
        // TODO : Implementasi fitur geser kanan conveyor belt
        count++;
        if (count == N){
            count = 0;
        }
        return toples.get(count).peek();
    }

    static int beliRasa(int rasa, int N, List<Stack<Integer>> toples) {
        // TODO : Implementasi fitur beli rasa, manfaatkan fitur geser kanan
        int displacement = 0;
        int displacement_save = 0;

        int count_save = count;

        while (true){
            if (toples.get(count).isEmpty()){
            }
            else if (toples.get(count).peek() == rasa){
                count_save = count;
                if (displacement == 0){
                    count = count_save;
                    toples.get(count).pop();
                    return 0;
                }
                else if (displacement_save < displacement){

                    displacement_save = displacement;
                }
            }

            if (displacement < N){
                displacement++;
                count--;
                if (count == -1){
                    count = N-1;
                }
            }
            else if (displacement == N){
                count = count_save;
                if (displacement_save == 0){
                    return -1;
                } else {
                    toples.get(count).pop();
                    return displacement_save;
                }
            }
        }
    }

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int N = in.nextInt();
        int X = in.nextInt();
        int C = in.nextInt();

        count = N-1;

        List<Stack<Integer>> toples = new ArrayList<>(N);

        for (int i = 0; i < N; ++i) {

            // TODO: Inisiasi toples ke-i
            toples.add(new Stack<Integer>());

            for (int j = 0; j < X; j++) {

                // TODO: Inisiasi kue ke-j ke dalam toples ke-i
                toples.get(i).push(in.nextInt());

            }
        }

        for (int i = 0; i < C; i++) {
            String perintah = in.next();
            if (perintah.equals("GESER_KANAN")) {
                out.println(geserKanan(N, toples));

            } else if (perintah.equals("BELI_RASA")) {
                int namaRasa = in.nextInt();
                out.println(beliRasa(namaRasa, N, toples));
            }
        }
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