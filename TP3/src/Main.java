import java.util.ArrayList;
import java.util.PriorityQueue;
import java.io.*;
import java.util.StringTokenizer;

public class Main {
    static FastReader in;
    static PrintWriter out;
    
    static long[][] time = new long[1001][1001];
    
    

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new FastReader();
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int inPost = in.nextInt(); //banyak post
        int inTunn = in.nextInt(); //banyak terowongan
    
        ArrayList < ArrayList < AdjListNode > > graphW = new ArrayList<>(); //inisiasi graph benteng
        ArrayList < ArrayList < AdjListNodeLength > > graphL = new ArrayList<>();
        
        for (int i = 0; i <= inPost; i++) {
            graphW.add(new ArrayList<>());
            graphL.add(new ArrayList<>());
        }
    
        for (int i = 0; i < inTunn; i++) {
            int A = in.nextInt(), B = in.nextInt(), L = in.nextInt(), S = in.nextInt();
            graphW.get(A).add(new AdjListNode(B, S, A));
            graphW.get(B).add(new AdjListNode(A, S, B));
            graphL.get(A).add(new AdjListNodeLength(B, L));
            graphL.get(B).add(new AdjListNodeLength(A, L));
        }
        
//        for (int a = 1; a <= inPost; a++) {
//            for (int b = 0; b < graphW.get(a).size(); b++) {
//                System.out.println(a + " " + graphW.get(a).get(b).getVertex());
//            }
//        }
        
        int inDwarves = in.nextInt();
    
        ArrayList<Integer> dwarves = new ArrayList<>(); //arraylist dan inisiasi benteng diserang
        for (int i = 0; i < inDwarves; i++) {
            int F = in.nextInt();
            dwarves.add(F);
        }
        
        int inQuery = in.nextInt();
        
        for (int i = 0; i < inQuery; i++) {
            String inCMD = in.next();
            if (inCMD.equals("KABUR")) {
                int inFrom = in.nextInt();
                int inTo = in.nextInt();
                
                long[] distance = traversal(inPost, graphW, inFrom, inTo, out);
                
                out.println(distance[inTo]);
                
                for (int j = 0; j <= inPost; j++) {
                    out.print(distance[j] + " ");
                }
                out.println();
                
            
            } else if (inCMD.equals("SIMULASI")) {
                graphL.set(0, new ArrayList<>());
                int inExit = in.nextInt();
                int[] exit = new int[inExit];
                for (int j = 0; j < inExit; j++) {
                    exit[j] = in.nextInt();
                    graphL.get(0).add(new AdjListNodeLength(exit[j], 0));
                }
                
                long[] distance = dijkstra(inPost, graphL, 0);
                long length = 0;
                for (int j = 0; j < inDwarves; j++) {
                    if (distance[dwarves.get(j)] >= length) {
                        length =  distance[dwarves.get(j)];
                    }
                }
                out.println(length);
                
                
            } else {
            
            }
            
            
        }
        
        out.close();
    }

    static class AdjListNode implements Comparable<AdjListNode> { //adj list for the graph

        int vertex, width, last; //attrbutes of the node

        AdjListNode(int v, int w, int l) { //constructor
            vertex = v;
            width = w;
            last = l;
        }
        int getVertex() { return vertex; } //getters
        int getWidth() { return width; }
        
        int getLast() { return last; }

        public int compareTo(AdjListNode other) { //compare methods
            return other.width - this.width;
        }
    }

    static class AdjListNodeLength implements Comparable<AdjListNodeLength> { //adj list for the graph

        int vertex, length; //attrbutes of the node

        AdjListNodeLength(int v, int l) { //constructor
            vertex = v;
            length = l;
        }
        int getVertex() { return vertex; } //getters
        int getLength() { return length; }

        public int compareTo(AdjListNodeLength other) { //compare methods
            return this.length - other.length;
        }
    }

    public static long[] dijkstra(int V, ArrayList<ArrayList<AdjListNodeLength> > graph, int src) { //djikstra for length
        long[] distance = new long[V+1];
        for (int i = 0; i <= V; i++)
            distance[i] = Long.MAX_VALUE;
        distance[src] = 0;

        BinaryHeap<AdjListNodeLength> pq = new BinaryHeap<>(); //width compared
        pq.insert(new AdjListNodeLength(src, 0));

        while (pq.size() > 0) {
            AdjListNodeLength current = pq.remove();

            for (AdjListNodeLength n : graph.get(current.getVertex())) {
                if (distance[current.getVertex()]
                        + n.getLength()
                        < distance[n.getVertex()]) {
                    distance[n.getVertex()]
                            = n.getLength()
                            + distance[current.getVertex()];
                    pq.insert(new AdjListNodeLength(
                            n.getVertex(),
                            (int) distance[n.getVertex()]));
                }
            }
        }
        // If you want to calculate distance from source to
        // a particular target, you can return
        // distance[target]
        return distance;
    }
    
    public static long[] traversal(int V, ArrayList<ArrayList<AdjListNode> > graph, int src, int dst, PrintWriter out) { //djikstra for length
        long[] distance = new long[V+1];
        for (int i = 0; i <= V; i++)
            distance[i] = Long.MAX_VALUE;
        distance[src] = 0;
        
        BinaryHeap<AdjListNode> pq = new BinaryHeap<>(); //width compared
        pq.insert(new AdjListNode(src, 0, 0));
        int x = 0;
        
        while (pq.size() > 0) {
            AdjListNode current = pq.remove();
            
//            out.println(current.getVertex() + " vertex now");
            if (current.getWidth() < distance[current.getLast()] || current.getLast() == src) {
                if (x > 0) {
                    out.println(current.getVertex() + " " + current.getWidth());
                    distance[current.getVertex()] = current.getWidth();
                }
            } else {
                if (x > 0) {
                    out.println(current.getVertex() + " " + distance[current.getLast()]);
                    distance[current.getVertex()] = distance[current.getLast()];
                }
            }
            
            if (current.getVertex() == dst) {
                break;
            }
            
            for (AdjListNode n : graph.get(current.getVertex())) {
//                out.print(n.getWidth() + " " + distance[n.getVertex()] + " if");
//                out.println();
                if (n.getWidth() < distance[n.getVertex()]) {
//                    out.println(n.getVertex() + " add to heap");
                    pq.insert(new AdjListNode(n.getVertex(), n.getWidth(), n.getLast()));
                }
            }
//            out.println();
            x++;
            
        }
        // If you want to calculate distance from source to
        // a particular target, you can return
        // distance[target]
        return distance;
    }
}
class BinaryHeap<T extends Comparable<T>> {
    ArrayList<T> data;

    public BinaryHeap() {
        data = new ArrayList<T>();
    }

    public BinaryHeap(ArrayList<T> arr) {
        data = arr;
        heapify();
    }

    public T peek() {
        if (data.isEmpty())
            return null;
        return data.get(0);
    }

    public void insert(T value) {
        data.add(value);
        percolateUp(data.size() - 1);
    }

    public T remove() {
        T removedObject = peek();

        if (data.size() == 1)
            data.clear();
        else {
            data.set(0, data.get(data.size() - 1));
            data.remove(data.size() - 1);
            percolateDown(0);
        }

        return removedObject;
    }

    private void percolateDown(int idx) {
        T node = data.get(idx);
        int heapSize = data.size();

        while (true) {
            int leftIdx = getLeftChildIdx(idx);
            if (leftIdx >= heapSize) {
                data.set(idx, node);
                break;
            } else {
                int maxChildIdx = leftIdx;
                int rightIdx = getRightChildIdx(idx);
                if (rightIdx < heapSize && data.get(rightIdx).compareTo(data.get(leftIdx)) < 0)
                    maxChildIdx = rightIdx;

                if (node.compareTo(data.get(maxChildIdx)) > 0) {
                    data.set(idx, data.get(maxChildIdx));
                    idx = maxChildIdx;
                } else {
                    data.set(idx, node);
                    break;
                }
            }
        }
    }

    private void percolateUp(int idx) {
        T node = data.get(idx);
        int parentIdx = getParentIdx(idx);
        while (idx > 0 && node.compareTo(data.get(parentIdx)) < 0) {
            data.set(idx, data.get(parentIdx));
            idx = parentIdx;
            parentIdx = getParentIdx(idx);
        }

        data.set(idx, node);
    }

    private int getParentIdx(int i) {
        return (i - 1) / 2;
    }

    private int getLeftChildIdx(int i) {
        return 2 * i + 1;
    }

    private int getRightChildIdx(int i) {
        return 2 * i + 2;
    }

    private void heapify() {
        for (int i = data.size() / 2 - 1; i >= 0; i--)
            percolateDown(i);
    }

    public void sort() {
        int n = data.size();
        while (n > 1) {
            data.set(n - 1, remove(n));
            n--;
        }
    }

    public T remove(int n) {
        T removedObject = peek();

        if (n > 1) {
            data.set(0, data.get(n - 1));
            percolateDown(0, n - 1);
        }

        return removedObject;
    }

    private void percolateDown(int idx, int n) {
        T node = data.get(idx);
        int heapSize = n;

        while (true) {
            int leftIdx = getLeftChildIdx(idx);
            if (leftIdx >= heapSize) {
                data.set(idx, node);
                break;
            } else {
                int maxChildIdx = leftIdx;
                int rightIdx = getRightChildIdx(idx);
                if (rightIdx < heapSize && data.get(rightIdx).compareTo(data.get(leftIdx)) < 0)
                    maxChildIdx = rightIdx;

                if (node.compareTo(data.get(maxChildIdx)) > 0) {
                    data.set(idx, data.get(maxChildIdx));
                    idx = maxChildIdx;
                } else {
                    data.set(idx, node);
                    break;
                }
            }
        }
    }

    public int size() {
        return data.size();
    }

}

class FastReader {
    BufferedReader br;
    StringTokenizer st;

    public FastReader()
    {
        br = new BufferedReader(
                new InputStreamReader(System.in));
    }

    String next()
    {
        while (st == null || !st.hasMoreElements()) {
            try {
                st = new StringTokenizer(br.readLine());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return st.nextToken();
    }

    int nextInt() { return Integer.parseInt(next()); }

    long nextLong() { return Long.parseLong(next()); }

    double nextDouble()
    {
        return Double.parseDouble(next());
    }

    String nextLine()
    {
        String str = "";
        try {
            if(st.hasMoreTokens()){
                str = st.nextToken("\n");
            }
            else{
                str = br.readLine();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }
}