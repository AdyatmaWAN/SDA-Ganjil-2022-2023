import java.io.*;
import java.util.StringTokenizer;

public class Main {

    static FastReader in;
    static PrintWriter out;

    static Mesin mesin_pertama;
    static Mesin mesin_terakhir;
    static Mesin mesin_now;
    static Mesin[] arr_mesin;
    static int rank;
    static long score;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new FastReader();
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int n = in.nextInt(); // jumlah mesin
        arr_mesin = new Mesin[n];
        for (int i = 0; i < n; i++) { // loop sebanyak jumlah mesin
            if (i == 0) { // membuat linked list
                mesin_pertama = new Mesin(i+1); //membuat mesin pertama
                mesin_now = mesin_pertama;
                mesin_terakhir = mesin_pertama;
                mesin_now.next = mesin_terakhir;
                mesin_now.prev = mesin_terakhir;
            } else {
                mesin_now.next = new Mesin(i+1); //membuat mesin selanjutnya
                mesin_now.next.prev = mesin_now;
                mesin_now = mesin_now.next;
                mesin_now.next = mesin_pertama;
                mesin_terakhir = mesin_now;
                mesin_pertama.prev = mesin_terakhir;
            }
            int m = in.nextInt(); // jumlah score
            for (int j = 0; j < m; j++){ //loop sebanyak jumlah score
                int a = in.nextInt();
                mesin_now.total_score += a;
                mesin_now.popularity++;
                mesin_now.tree.root = mesin_now.tree.insertNode(mesin_now.tree.root, a); //insert node
            }
//            mesin_now.tree.printTree(mesin_now.tree.root, "", true, out);
        }
        mesin_now = mesin_pertama; //mengarahkan mesin_now ke mesin pertama setelah diinitiate semua mesin



        int l = in.nextInt(); // jumlah query

        for (int i = 0; i < l; i++) { //banyak query dilakukan
            String cmd = in.next(); //command

            if (cmd.equals("MAIN")) {
                int a = in.nextInt();
                mesin_now.total_score += a;
                if (mesin_now.tree.root == null) {
                    rank = 0;
                }
                else {
                    rank = mesin_now.tree.root.node_count;
                }
                mesin_now.popularity++;
                mesin_now.tree.root = mesin_now.tree.handleQueryMain(mesin_now.tree.root, a, rank, out);

            } else if (cmd.equals("GERAK")) {

                cmd = in.next();
                if (cmd.equals("KANAN")) {
                    mesin_now = mesin_now.next;
                } else {
                    mesin_now = mesin_now.prev;
                }
                out.println(mesin_now.id);

            } else if (cmd.equals("HAPUS")) {
                int a = in.nextInt();
                mesin_now.tree.handleQueryHapus(a, mesin_now, score, out); //TODO: handle geser jika rusak
                if (mesin_now.popularity == 0) {
                    if (mesin_now == mesin_pertama) {
                        mesin_pertama = mesin_now.next;
                        mesin_terakhir = mesin_now;
                        mesin_now = mesin_now.next;
                    } else if (mesin_now == mesin_terakhir) {
                        mesin_now = mesin_now.next;
                    } else {
                        mesin_now.prev.next = mesin_now.next;
                        mesin_now.next.prev = mesin_now.prev;
                        Mesin temp = mesin_now.next;
                        mesin_terakhir.next = mesin_now;
                        mesin_now.prev = mesin_terakhir;
                        mesin_now.next = mesin_pertama;
                        mesin_pertama.prev = mesin_now;
                        mesin_terakhir = mesin_now;
                        mesin_now = temp;
                    }
                }

            } else if (cmd.equals("LIHAT")) {
                int a = in.nextInt();
                int b = in.nextInt();
                mesin_now.tree.handleQueryLihat(a, b, mesin_now, out);

            } else if (cmd.equals("EVALUASI")) {
                handleQueryEvaluasi(n);

            }
//            mesin_now.tree.printTree(mesin_now.tree.root, "", true, out);
        }

        //mesin_now.tree.printTree(mesin_now.tree.root, "", true, out);

        out.close();

    }

    static void handleQueryEvaluasi(int count) {
        Mesin pointer;
        int counter = 0;
        arr_mesin[0] = mesin_pertama;
        pointer = mesin_pertama.next;
        while (pointer != mesin_pertama) {
            counter++;
            arr_mesin[counter] = pointer;
            pointer = pointer.next;
        }
        sort(arr_mesin, 0, count-1);
        mesin_pertama = arr_mesin[0];
        mesin_terakhir = arr_mesin[count - 1];
        for (int k = 0; k < count; k++){
            if (arr_mesin[k] == mesin_pertama) {
                arr_mesin[0].prev = mesin_terakhir;
            } else {
                arr_mesin[k].prev = arr_mesin[k-1];
            }
            if (arr_mesin[k] == mesin_terakhir) {
                arr_mesin[k].next = mesin_pertama;
            } else {
                arr_mesin[k].next = arr_mesin[k+1];
            }
            //out.println(arr_mesin[k].id + " " + arr_mesin[k].popularity);

            if (arr_mesin[k] == mesin_now) {
                out.println(k+1);
            }
        }
    }

    //gfg merge sort https://www.geeksforgeeks.org/merge-sort/
    static void merge(Mesin arr[], int l, int m, int r) {
        // Find sizes of two subarrays to be merged
        int n1 = m - l + 1;
        int n2 = r - m;

        /* Create temp arrays */
        Mesin L[] = new Mesin[n1];
        Mesin R[] = new Mesin[n2];

        /*Copy data to temp arrays*/
        for (int i = 0; i < n1; ++i)
            L[i] = arr[l + i];
        for (int j = 0; j < n2; ++j)
            R[j] = arr[m + 1 + j];

        /* Merge the temp arrays */

        // Initial indexes of first and second subarrays
        int i = 0, j = 0;

        // Initial index of merged subarray array
        int k = l;
        while (i < n1 && j < n2) {
            if (L[i].compareTo(R[j]) <= 0) {
                arr[k] = L[i];
                i++;
            }
            else {
                arr[k] = R[j];
                j++;
            }
            k++;
        }

        /* Copy remaining elements of L[] if any */
        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }

        /* Copy remaining elements of R[] if any */
        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }

    // Main function that sorts arr[l..r] using
    // merge()
    static void sort(Mesin arr[], int l, int r) {
        if (l < r) {
            // Find the middle point
            int m = l + (r - l) / 2;

            // Sort first and second halves
            sort(arr, l, m);
            sort(arr, m + 1, r);

            // Merge the sorted halves
            merge(arr, l, m, r);
        }
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

class Node { //node for every score
    int score, node_count, height, same_score_count;
    Node left, right;

    Node(int score) {
        this.score = score;
        this.node_count = 1;
        this.height = 1;
        this.same_score_count = 1;
    }
}

//some method based of gfg avl tree https://www.geeksforgeeks.org/insertion-in-an-avl-tree/
class AVLTree { //implements avl tree
    Node root;

    void printTree(Node currPtr, String indent, boolean last, PrintWriter out){ //print tree untuk debugging
        if (currPtr != null) {
            out.print(indent);
            if (last) {
                out.print("R----");
                indent += "   ";
            } else {
                out.print("L----");
                indent += "|  ";
            }
            out.println(currPtr.score + " height=" + currPtr.height + " node_count=" + currPtr.node_count + " same_score_count=" + currPtr.same_score_count);
            printTree(currPtr.left, indent, false, out);
            printTree(currPtr.right, indent, true, out);
        }

    }

    Node handleQueryMain(Node node, int score, int rank, PrintWriter out) { //
        if (node == null) { //if node is null, create new node
            out.println(rank+1);
            return (new Node(score));
        }

        node.node_count += 1; //increase node count

        if (score < node.score) { //kecil -> kiri (cari), besar -> kanan (hapus kiri + hapus same.node), sama -> tambah same_score dan rank dikurangi
            node.left = handleQueryMain(node.left, score, rank, out);
        } else if (score > node.score) {
            if (node.left != null) {
                rank -= node.left.node_count;
            }
            rank -= node.same_score_count;
            node.right = handleQueryMain(node.right, score, rank, out);
        } else {
            rank -= node.same_score_count;
            node.same_score_count += 1;
            if (node.left != null){
                rank -= node.left.node_count;
            }
            rank += 1;
            out.println(rank); //print rank dari kanan
            return node; //di return
        }

        node.height = 1 + Math.max(height(node.left), height(node.right)); //update height node

        int balance = getBalance(node); //balance the tree if needed

        if (balance > 1 && score < node.left.score) { //node kiri lebih tinggi dan skor lebih kecil dari node kiri
            return rightRotate(node);
        }

        if (balance < -1 && score > node.right.score) { //node kanan lebih tinggi dan skor lebih besar dari node kanan
            return leftRotate(node);
        }

        if (balance > 1 && score > node.left.score) { //node kiri lebih tinggi dan skor lebih besar dari node kiri
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        if (balance < -1 && score < node.right.score) { //node kanan lebih tinggi dan skor lebih kecil dari node kanan
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    void handleQueryHapus(int num_of_score, Mesin mesin, long score, PrintWriter out) {
        if (num_of_score >= mesin.popularity){ //jika jumlah score lebih besar dari popularity dan hapus tree + attribut mesin
            mesin.popularity = 0;
            out.println(mesin.total_score);
            mesin.total_score = 0;
            mesin.tree.root = null;
            return;
        }
        score = 0;
        for (int i = 0; i < num_of_score; i++) { //hapus score tertinggi sebanyak num_of_score
            int max = findMax(mesin.tree.root, mesin.tree.root);
            score += max;
            mesin.tree.root = mesin.tree.deleteNode(mesin.tree.root, max);
            //out.println(max);
            //printTree(mesin.tree.root, "", true, out);
        }
        mesin.popularity -= num_of_score; //kurangi popularity
        mesin.total_score -= score;
        out.println(score); //print score yang dihapus
    }

    void handleQueryLihat(int bottom, int top, Mesin mesin, PrintWriter out) {
        if (mesin.popularity == 0){
            out.println("0");
            return;
        }
        int countUp = rankTop(mesin.tree.root, top);
        int countDown = rankBottom(mesin.tree.root, bottom);
        int count = mesin.tree.root.node_count - countUp - countDown;
        out.println(count);
    }

    int rankTop(Node node, int top) {
        if (node == null) {
            return 0;
        }
        if (node.score < top) {
            return rankTop(node.right, top);
        } else if (node.score > top) {
            if (node.right != null) {
                return node.right.node_count + node.same_score_count + rankTop(node.left, top);
            } else {
                return node.same_score_count + rankTop(node.left, top);
            }
        } else {
            if (node.right != null) {
                return node.right.node_count;
            } else {
                return 0;
            }
        }
    }

    int rankBottom(Node node, int bottom) {
        if (node == null) {
            return 0;
        }
        if (node.score > bottom) {
            return rankBottom(node.left, bottom);
        } else if (node.score < bottom) {
            if (node.left != null) {
                return node.left.node_count + node.same_score_count + rankBottom(node.right, bottom);
            } else {
                return node.same_score_count + rankBottom(node.right, bottom);
            }
        } else {
            if (node.left != null) {
                return node.left.node_count;
            } else {
                return 0;
            }
        }
    }

    Node deleteNode(Node root, int score) {
        // STEP 1: PERFORM STANDARD BST DELETE
        if (root == null)
            return root;

        // If the num_of_score to be deleted is smaller than
        // the root's num_of_score, then it lies in left subtree
        if (score < root.score)
            root.left = deleteNode(root.left, score);

            // If the num_of_score to be deleted is greater than the
            // root's num_of_score, then it lies in right subtree
        else if (score > root.score)
            root.right = deleteNode(root.right, score);

            // if num_of_score is same as root's num_of_score, then this is the node
            // to be deleted
        else {
            if (root.same_score_count == 1) {
                // node with only one child or no child
                if ((root.left == null) || (root.right == null)) {
                    Node temp = null;
                    if (temp == root.left)
                        temp = root.right;
                    else
                        temp = root.left;

                    // No child case
                    if (temp == null) {
                        temp = root;
                        root = null;
                    } else // One child case
                        root = temp; // Copy the contents of
                    // the non-empty child
                } else {

                    // node with two children: Get the inorder
                    // successor (smallest in the right subtree)
                    Node temp = minValueNode(root.right);

                    // Copy the inorder successor's data to this node
                    System.out.println(root.score);
                    root.score = temp.score;
                    root.node_count = temp.node_count;
                    root.same_score_count = temp.same_score_count;

                    // Delete the inorder successor
                    root.right = deleteNode(root.right, temp.score);
                }
            } else {
                root.same_score_count -= 1;
                root.node_count -= 1;
                return root;
            }
        }

        // If the tree had only one node then return
        if (root == null)
            return root;

        // STEP 2: UPDATE HEIGHT OF THE CURRENT NODE
        root.height = Math.max(height(root.left), height(root.right)) + 1;

        // STEP 3: GET THE BALANCE FACTOR OF THIS NODE (to check whether
        // this node became unbalanced)
        int balance = getBalance(root);

        // If this node becomes unbalanced, then there are 4 cases
        if (balance > 1 ){
            //System.out.println("right");
            return rightRotate(root);
        }

        if (balance < -1 ) {
            //System.out.println("left");
            return leftRotate(root);
        }

        return root;
    }

    int findMax(Node node, Node root) {
        if (node == null) {
            return 0;
        }
        int max = node.score;
        if (node.right != null) {
            node.node_count--;
        }
        int rightMax = findMax(node.right, root);
        if (rightMax > max) {
            max = rightMax;
        }
        return max;
    }

    int height(Node node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    int getBalance(Node node) {
        if (node == null) {
            return 0;
        }
        return height(node.left) - height(node.right);
    }

    int node_count(Node node) {
        if (node == null) {
            return 0;
        }
        return node.node_count;
    }

    Node rightRotate(Node node) {
        Node left = node.left;
        Node right = left.right;

        left.right = node;
        node.left = right;

        //update height
        node.height = Math.max(height(node.left), height(node.right)) + 1;
        left.height = Math.max(height(left.left), height(left.right)) + 1;

        //update node_count
        node.node_count = node_count(node.left) + node_count(node.right) + node.same_score_count;
        left.node_count = node_count(left.left) + node_count(left.right) + left.same_score_count;


        return left;
    }

    Node leftRotate(Node node) {
        Node right = node.right;
        Node left = right.left;

        right.left = node;
        node.right = left;

        //update height
        node.height = Math.max(height(node.left), height(node.right)) + 1;
        right.height = Math.max(height(right.left), height(right.right)) + 1;

        //update node_count
        node.node_count = node_count(node.left) + node_count(node.right) + node.same_score_count;
        right.node_count = node_count(right.left) + node_count(right.right) + right.same_score_count;

        return right;
    }

    Node insertNode(Node node, int score) {
        if (node == null) {
            return (new Node(score));
        }

        node.node_count += 1;

        if (score < node.score) {
            node.left = insertNode(node.left, score);
        } else if (score > node.score) {
            node.right = insertNode(node.right, score);
        } else {
            node.same_score_count += 1;
            return node;
        }

        node.height = 1 + Math.max(height(node.left), height(node.right));

        int balance = getBalance(node);

        if (balance > 1 && score < node.left.score) {
            return rightRotate(node);
        }

        if (balance < -1 && score > node.right.score) {
            return leftRotate(node);
        }

        if (balance > 1 && score > node.left.score) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        if (balance < -1 && score < node.right.score) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    Node minValueNode(Node node) {
        Node current = node;

        while (current.left != null) {
            current = current.left;
        }

        return current;
    }

}

class Mesin implements Comparable<Mesin>{ //implement class mesin
    AVLTree tree;
    long total_score;
    int id, popularity;
    Mesin prev, next;


    Mesin(int id) { //constructor
        tree = new AVLTree();
        total_score = 0;
        this.id = id;
        popularity = 0;
    }

    public int compareTo(Mesin o) { //compare popularity yang lebih besar, dan id yang lebih kecil
        if (this.popularity == o.popularity){
            return this.id - o.id;
        } else {
            return o.popularity - this.popularity;
        }
    }
}
