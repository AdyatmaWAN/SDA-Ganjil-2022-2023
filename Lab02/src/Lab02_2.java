import java.io.*;
import java.util.LinkedList;
import java.util.Deque;
import java.util.Stack;
import java.util.StringTokenizer;

/*
In memorial of the four second limit
*/

public class Lab02_2 {
    private static InputReader in;
    private static PrintWriter out;

    //declare global variable
    private static Deque<Stack<Integer>> toples = new LinkedList<Stack<Integer>>();
    private static Stack<Integer> last_toples = new Stack<Integer>();
    private static Stack<Integer> first_toples = new Stack<Integer>();

    static int geserKanan() {

        //move last stack in toples to first in toples
        last_toples = toples.pollLast();
        toples.addFirst(last_toples);
        if (toples.getFirst().isEmpty()) { //if first stack is empty, print -1
            return -1;
        } else {
            if (toples.getFirst().peek() < 0 || toples.getFirst().peek() > 3){
                return -1;
            }
            return toples.getFirst().peek(); //if first stack is not empty, print first stack peek
        }
    }

    static void geserKiri() {

        //move first stack in toples to last in toples
        first_toples = toples.pollFirst();
        toples.addLast(first_toples);

    }

    static int beliRasa(int rasa) {

        int displacement = 0; //initialize displacement for counting how many stack has been moved

        if ((rasa < 0 || rasa > 3)) { //if rasa is not in range, print -1
            return -1;
        }

        while (true) { //while true, move stack until rasa is found
            if (toples.getFirst().isEmpty()) {//if the first stack is empty, move stack

            } else if (toples.getFirst().peek() == rasa) { //if the first stack peek is equal to rasa, print displacement
                toples.getFirst().pop(); //pop the first stack peek
                return displacement;
            }

            displacement++; //counting how many stack has been moved
            geserKiri(); //move stack

            if (displacement== toples.size()) { //if displacement(how much the stack moved) is equal to toples size, print -1
                return -1;
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

        for (int i = 0; i < N; ++i) {

            toples.addFirst(new Stack<Integer>());

            for (int j = 0; j < X; j++) {

                int rasaKeJ = in.nextInt();

                toples.getFirst().push(rasaKeJ);
            }
        }

        for (int i = 0; i < C; i++) {
            String perintah = in.next();
            if (perintah.equals("GESER_KANAN")) {
                out.println(geserKanan());

            } else if (perintah.equals("BELI_RASA")) {
                int namaRasa = in.nextInt();
                out.println(beliRasa(namaRasa));
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