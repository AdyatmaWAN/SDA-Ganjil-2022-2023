import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

// TODO: Lengkapi class ini
class Saham implements Comparable<Saham>{
    public int id;
    public int harga;
    public int index;
    public int minmax;

    public Saham(int id, int harga) {
        this.id = id;
        this.harga = harga;
    }
    public int compareTo(Saham o) {
        if (this == null && o == null) {
            return 0;
        } else if (this == null && o != null) {
            return -1;
        } else if (o == null && this != null) {
            return 1;
        } else {
            if (this.harga == o.harga) {
                return this.id - o.id;
            }
            return this.harga - o.harga;
        }
    }
}

// TODO: Lengkapi class ini
class Heap { //GFG https://www.geeksforgeeks.org/max-heap-in-java/ and https://www.geeksforgeeks.org/min-heap-in-java/
    Saham[] idToSaham;
    int heap_size;
    Saham[] maxHeap;
    int maxHeapSize;
    Saham[] minHeap;
    int minHeapSize;
    int median;

    public Heap() {
        idToSaham = new Saham[400000];
        heap_size = 0;
        maxHeap = new Saham[400000];
        maxHeapSize = 0;
        minHeap = new Saham[400000];
        minHeapSize = 0;
        median = 0;

    }

    public int parent(int pos) {
        return pos/2;
    }

    public int leftChild(int pos) {
        return pos*2;
    }

    public int rightChild(int pos) {
        return pos*2+1;
    }

    public boolean isLeaf(int pos)
    {
        if (pos > (maxHeapSize / 2) && pos <= maxHeapSize) {
            return true;
        }
        return false;
    }

    private void maxHeapify(int pos)
    {
        if (isLeaf(pos))
            return;

        if (maxHeap[pos].compareTo(maxHeap[leftChild(pos)]) < 0
                || maxHeap[pos].compareTo(maxHeap[rightChild(pos)]) < 0 ) {

            if (maxHeap[leftChild(pos)].compareTo(maxHeap[rightChild(pos)]) > 0 ) {
                swap(pos, leftChild(pos), maxHeap);
                maxHeapify(leftChild(pos));
            }
            else {
                swap(pos, rightChild(pos), maxHeap);
                maxHeapify(rightChild(pos));
            }
        }
    }

    private void minHeapify(int pos)
    {
        if(!isLeaf(pos)){
            int swapPos= pos;
            // swap with the minimum of the two children
            // to check if right child exists. Otherwise default value will be '0'
            // and that will be swapped with parent node.
            if(rightChild(pos)<=minHeapSize)
                swapPos = minHeap[leftChild(pos)].compareTo(minHeap[rightChild(pos)]) < 0 ?leftChild(pos):rightChild(pos);
            else
                swapPos= leftChild(pos);

            if(minHeap[pos].compareTo(minHeap[leftChild(pos)]) > 0 || minHeap[pos].compareTo(minHeap[rightChild(pos)]) > 0){
                swap(pos,swapPos, minHeap);
                minHeapify(swapPos);
            }

        }
    }

    public Saham extractMax()
    {
        Saham popped = maxHeap[0];
        maxHeap[0] = maxHeap[--maxHeapSize];
        maxHeapify(0);
        return popped;
    }

    public Saham extractMin()
    {

        Saham popped = minHeap[0];
        minHeap[0] = minHeap[--minHeapSize];
        minHeapify(0);

        return popped;
    }

    public void swap(int fpos, int spos, Saham[] saham)
    {

        Saham tmp;
        tmp = saham[fpos];

        saham[fpos] = saham[spos];
        saham[spos] = tmp;
    }

    public void insertMin(Saham saham, Saham[] arrSaham, int size)
    {

        arrSaham[size] = saham;
        int current = size;

        while (arrSaham[current].compareTo(arrSaham[parent(current)]) < 0) {
            swap(current, parent(current), arrSaham);
            current = parent(current);
        }
        saham.index = current;
        saham.minmax = 0;
    }

    public void insertMax(Saham saham, Saham[] arrSaham, int size)
    {

        arrSaham[size] = saham;
        int current = size;

        while (arrSaham[current].compareTo(arrSaham[parent(current)]) > 0) {
            swap(current, parent(current), arrSaham);
            current = parent(current);
        }
        saham.index = current;
        saham.minmax = 1;
    }

    void add(Saham saham, PrintWriter out) {
//        out.println("diff " + (maxHeapSize-minHeapSize));
//        out.println(maxHeapSize);
//        out.println(minHeapSize);
        if (maxHeapSize - minHeapSize == 1){
            if (saham.harga > maxHeap[0].harga) {
                insertMin(saham, minHeap, minHeapSize);
                minHeapSize++;
            } else if (saham.harga <= maxHeap[0].harga) {
                insertMax(saham, maxHeap, maxHeapSize);
                maxHeapSize++;
                Saham temp = extractMax();
                insertMin(temp, minHeap, minHeapSize);
                minHeapSize++;
            }
            median = minHeap[0].id;
        } else if (maxHeapSize - minHeapSize == 0) {
            if (minHeap[0] != null) {
                if (saham.harga <= minHeap[0].harga) {
                    insertMax(saham, maxHeap, maxHeapSize);
                    maxHeapSize++;
                } else if (saham.harga > minHeap[0].harga) {
                    insertMin(saham, minHeap, minHeapSize);
                    minHeapSize++;
                    Saham temp = extractMin();
                    insertMax(temp, maxHeap, maxHeapSize);
                    maxHeapSize++;
                }
                median = minHeap[0].id;
            } else {
                insertMax(saham, maxHeap, maxHeapSize);
                maxHeapSize++;
                median = maxHeap[0].id;
            }
        }
        idToSaham[heap_size] = saham;
        heap_size++;
    }

    public void Maxprint(PrintWriter out) {
        for (int i = 1; i <= maxHeapSize / 2; i++) {

            // Printing the parent and both childrens
            out.print(
                    " PARENT+ : " + maxHeap[i]
                            + " LEFT CHILD : " + maxHeap[2 * i]
                            + " RIGHT CHILD :" + maxHeap[2 * i + 1]);

            // By here new line is required
            out.println();
        }
    }

    public void Minprint(PrintWriter out) {
        for (int i = 1; i <= minHeapSize / 2; i++) {

            // Printing the parent and both childrens
            out.print(
                    " PARENT- : " + minHeap[i]
                            + " LEFT CHILD : " + minHeap[2 * i]
                            + " RIGHT CHILD :" + minHeap[2 * i + 1]);

            // By here new line is required
            out.println();
        }
    }

    void change(Saham saham, PrintWriter out) {
        int index = saham.index;
        int minmax = saham.minmax;

        if (minmax == 1 && saham.harga < maxHeap[0].harga){
            swap(index, 0, maxHeap);
            int curr1 = index;
            int curr2 = 0;
        }
    }

}

public class Main {

    private static InputReader in;
    private static PrintWriter out;
    static Heap saham;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int N = in.nextInt();
        saham = new Heap();

        // TODO
        for (int i = 1; i <= N; i++) {
            int harga = in.nextInt();
            saham.add(new Saham(i, harga), out);
//            saham.Minprint(out);
//            saham.Maxprint(out);
        }

        int Q = in.nextInt();

        // TODO
        for (int i = 0; i < Q; i++) {
            String q = in.next();

            if (q.equals("TAMBAH")) {
                int harga = in.nextInt();
                saham.add(new Saham(saham.heap_size, harga), out);
                saham.heap_size++;
                out.println(saham.median);
//                saham.Minprint(out);
//                saham.Maxprint(out);


            } else if (q.equals("UBAH")) {
                int nomorSeri = in.nextInt();
                int harga = in.nextInt();
                saham.idToSaham[nomorSeri-1].harga = harga;
                saham.change(saham.idToSaham[nomorSeri], out);
            }
        }
        out.flush();
    }

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

        public long nextLong() {
            return Long.parseLong(next());
        }
    }
}