import java.util.ArrayList;
import java.util.PriorityQueue;
import java.io.*;
import java.util.StringTokenizer;

public class Main {
    static FastReader in;
    static PrintWriter out;
    

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new FastReader();
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int inPost = in.nextInt(); //banyak post
        int inTunn = in.nextInt(); //banyak terowongan
    
        ArrayList < ArrayList < AdjListNode > > graphW = new ArrayList<>(); //inisiasi graph benteng
        ArrayList < ArrayList < AdjListNodeLength > > graphL = new ArrayList<>();
        ArrayList < AdjListNodeLength > list = new ArrayList<>(); //arraylist isi semua tunnel
        
        for (int i = 0; i <= inPost; i++) { //inisiasi graph agar bisa memiliki menyimpan tunnel
            graphW.add(new ArrayList<>());
            graphL.add(new ArrayList<>());
        }
    
        for (int i = 0; i < inTunn; i++) { //input tunnel
            int A = in.nextInt(), B = in.nextInt(), L = in.nextInt(), S = in.nextInt();
            graphW.get(A).add(new AdjListNode(B, S, A));
            graphW.get(B).add(new AdjListNode(A, S, B));
            graphL.get(A).add(new AdjListNodeLength(B, L));
            graphL.get(B).add(new AdjListNodeLength(A, L));
            list.add(new AdjListNodeLength(A, L, 0, B));
        }
        
        int inDwarves = in.nextInt(); //inisiasi penyimpanan kurcaci
    
        ArrayList<Integer> dwarves = new ArrayList<>();
        for (int i = 0; i < inDwarves; i++) {
            int F = in.nextInt();
            dwarves.add(F);
        }
        
        int inQuery = in.nextInt(); //inisiasi banyak query
        
        for (int i = 0; i < inQuery; i++) {
            String inCMD = in.next();
            if (inCMD.equals("KABUR")) { //jika query kabur maka akan melakukan traversal sampai ketemu node tujuan
                int inFrom = in.nextInt();
                int inTo = in.nextInt();
                
                long[] distance = traversal(inPost, graphW, inFrom, inTo, out);
                
                out.println(distance[inTo]);
            
            } else if (inCMD.equals("SIMULASI")) { //jika melakukan simulasi maka akan mencari shortest dari node 0 yang terhubung ke semua exit
                graphL.set(0, new ArrayList<>());
                int inExit = in.nextInt();
                int[] exit = new int[inExit];
                for (int j = 0; j < inExit; j++) { //inisiasi exit
                    exit[j] = in.nextInt();
                    graphL.get(0).add(new AdjListNodeLength(exit[j], 0));
                }
                
                long[] distance = dijkstra(inPost, graphL, 0); //djikstra shortest path
                long length = 0;
                for (int j = 0; j < inDwarves; j++) { //mencari kurcaci dengan waktu terlamam untuk kelaur
                    if (distance[dwarves.get(j)] >= length) {
                        length =  distance[dwarves.get(j)];
                    }
                }
                out.println(length);
                
                
            } else { //jika query super maka akan melakukan djikstra dari asal, tujuan 1 dan tujuan 2 kurcaci lalu
                int inSrc = in.nextInt();
                int inDest1 = in.nextInt();
                int inDest2 = in.nextInt();
    
                long dist1 = Long.MAX_VALUE;
                long dist2 = Long.MAX_VALUE;
    
                long[] distanceN1 = dijkstra(inPost, graphL, inSrc);
                long[] distanceN2 = dijkstra(inPost, graphL, inDest1);
                long[] distanceN3 = dijkstra(inPost, graphL, inDest2);
    
                for (AdjListNodeLength n : list) { //membandingkan semua tunnel yang akan di skip dan dicari yang terkecil
                    if (dist1 > distanceN1[n.getLast()] + distanceN2[n.getVertex()]) {
                        dist1 = distanceN1[n.getLast()] + distanceN2[n.getVertex()];
                    }
                    if (dist1 > distanceN1[n.getVertex()] + distanceN2[n.getLast()]) {
                        dist1 = distanceN1[n.getVertex()] + distanceN2[n.getLast()];
                    }
                    if (dist2 > distanceN2[n.getLast()] + distanceN3[n.getVertex()]) {
                        dist2 = distanceN2[n.getLast()] + distanceN3[n.getVertex()];
                    }
                    if (dist2 > distanceN2[n.getVertex()] + distanceN3[n.getLast()]) {
                        dist2 = distanceN2[n.getVertex()] + distanceN3[n.getLast()];
                    }
                }
                
                //membandingkan mana yang lebih kecil, yang di skip sebelum destinasi 1 atau setelah destinasi 1
                if (dist1 + distanceN3[inDest1] < dist2 + distanceN1[inDest1]) {
                    out.println(dist1 + distanceN3[inDest1]);
                } else {
                    out.println(dist2 + distanceN1[inDest1]);
                }
            }
            
        }
        
        out.close();
    }

    static class AdjListNode implements Comparable<AdjListNode> { //adj list for the graph (width)

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

    static class AdjListNodeLength implements Comparable<AdjListNodeLength> { //adj list for the graph (length)

        int vertex, length, isSuper, last; //attrbutes of the node

        AdjListNodeLength(int v, int l) { //constructor
            vertex = v;
            length = l;
            isSuper = 0;
        }
    
        AdjListNodeLength(int v, int l, int s, int ls) { //constructor
            vertex = v;
            length = l;
            isSuper = s;
            last = ls;
        }
        
        int getVertex() { return vertex; } //getters
        int getLength() { return length; }
        int getIsSuper() { return isSuper; }
        int getLast() { return last; }

        public int compareTo(AdjListNodeLength other) { //compare methods
            return this.length - other.length;
        }
    }

    
    //https://www.geeksforgeeks.org/dijkstras-algorithm-for-adjacency-list-representation-greedy-algo-8/
    //djikstra shortest path algorithm modified from gfg
    public static long[] dijkstra(int V, ArrayList<ArrayList<AdjListNodeLength> > graph, int src) { //djikstra shortest path to get distance to all node
        long[] distance = new long[V+1]; //initiate distance array
        for (int i = 0; i <= V; i++)
            distance[i] = Long.MAX_VALUE;
        distance[src] = 0;

        BinaryHeap<AdjListNodeLength> pq = new BinaryHeap<>(); //heap for the priority queue
        pq.insert(new AdjListNodeLength(src, 0)); //insert adjlistnode to the heap

        while (pq.size() > 0) { //while heap is not empty bakal menambah adjlistnode ke heap dan mengupdate distance dari setiap node yang dilalui
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
        return distance; //return distance array
    }
    
    //modified from previous dijkstra to update when entering node and stop when reaching destination
    public static long[] traversal(int V, ArrayList<ArrayList<AdjListNode> > graph, int src, int dst, PrintWriter out) { //djikstra for length
        long[] distance = new long[V+1]; //initiate array
        for (int i = 0; i <= V; i++)
            distance[i] = Long.MAX_VALUE;
        distance[src] = 0;
        
        BinaryHeap<AdjListNode> pq = new BinaryHeap<>(); //initiate heap
        pq.insert(new AdjListNode(src, 0, 0));
        int x = 0;
        
        while (pq.size() > 0) {
            AdjListNode current = pq.remove();
            //modifikasi ada pada jika node awal maka akan mengupdate dengan width yang lebih kecil
            //jika node baru memiliki width yang lebih besar maka akan diisi dengan width node sebelumnya
            //namun jika node sebelumnya adalah source maka  widthnya tidak akan dirubah
            if (current.getWidth() < distance[current.getLast()] || current.getLast() == src) {
                if (x > 0) {
                    distance[current.getVertex()] = current.getWidth();
                }
            } else {
                if (x > 0) {
                    distance[current.getVertex()] = distance[current.getLast()];
                }
            }
            
            if (current.getVertex() == dst) {
                break;
            }
            
            for (AdjListNode n : graph.get(current.getVertex())) {
                if (n.getWidth() < distance[n.getVertex()]) {
                    pq.insert(new AdjListNode(n.getVertex(), n.getWidth(), n.getLast()));
                }
            }
            x++;
            
        }
        return distance;
    }
    
}
class BinaryHeap<T extends Comparable<T>> {
    ArrayList<T> data; //arraylist to store the heap

    public BinaryHeap() {
        data = new ArrayList<T>();
    } //initiate heap

    public BinaryHeap(ArrayList<T> arr) { //initiate heap
        data = arr;
        heapify();
    }

    public T peek() { //see the top of the heap
        if (data.isEmpty())
            return null;
        return data.get(0);
    }

    public void insert(T value) { //insert value to the heap
        data.add(value);
        percolateUp(data.size() - 1);
    }

    public T remove() { //remove the top of the heap
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

    private void percolateDown(int idx) { //percolate down the heap
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

    private void percolateUp(int idx) { // percolate up the heap
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