import java.io.*;
import java.util.*;
import java.util.StringTokenizer;

class Main { //gfg Dijkstra’s Algorithm modified to be able to find the shortest path from multiple sources


    private static InputReader in;
    private static PrintWriter out;

    static class AdjListNode {

        int vertex, weight;

        AdjListNode(int v, int w)
        {
            vertex = v;
            weight = w;
        }
        int getVertex() { return vertex; }
        int getWeight() { return weight; }
    }
    public static long[] dijkstra(int V, ArrayList<ArrayList<AdjListNode> > graph, int src) {
        long[] distance = new long[V+1];
        for (int i = 0; i <= V; i++)
            distance[i] = Long.MAX_VALUE;
        distance[src] = 0;

        PriorityQueue<AdjListNode> pq = new PriorityQueue<>(
                (v1, v2) -> v1.getWeight() - v2.getWeight());
        pq.add(new AdjListNode(src, 0));

        while (pq.size() > 0) {
            AdjListNode current = pq.poll();

            for (AdjListNode n :
                    graph.get(current.getVertex())) {
                if (distance[current.getVertex()]
                        + n.getWeight()
                        < distance[n.getVertex()]) {
                    distance[n.getVertex()]
                            = n.getWeight()
                            + distance[current.getVertex()];
                    pq.add(new AdjListNode(
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

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int V = in.nextInt(), M = in.nextInt(); //V = benteng (node), M = benteng diserang (source)

        ArrayList<Integer> castles = new ArrayList<>(); //arraylist dan inisiasi benteng diserang
        for (int i = 0; i < M; i++) {
            int F = in.nextInt();
            castles.add(F);
        }

        ArrayList<ArrayList < AdjListNode> > graph = new ArrayList<>(); //inisiasi graph benteng
        for (int i = 0; i <= V; i++) {
            graph.add(new ArrayList<>());
        }

        for (int i = 0; i < M; i++) { //menambahkan benteng kosng (node indeks 0) + jalan menuju benteng diserang untuk memulai algoritma dijkstra
            int x = castles.get(i);
            graph.get(0).add(new AdjListNode(x, 0));
        }

        int source = 0; //beteng awal / indeks awal traversal

        int E = in.nextInt(); //menambahkan jalan untuk traversal berdasarkan input
        for (int i = 0; i < E; i++) {
            int A = in.nextInt(), B = in.nextInt(), W = in.nextInt();
            graph.get(B).add(new AdjListNode(A, W));
        }

        long[] distance = dijkstra(V, graph, source); //inisiasi array distance untuk menampung hasil distance setiap benteng ke benteng diserang

        int Q = in.nextInt(); //loop untuk membandingkan apakah mungkin mengirimkan pasukan dari benteng S ke benteng yang diserang terdekat
        while (Q-- > 0) {
            int S = in.nextInt(), K = in.nextInt();
            if (distance[S] < K) {
                out.println("YES");
            } else {
                out.println("NO");
            }
        }
        out.close();
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

    }
}