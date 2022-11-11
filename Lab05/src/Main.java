import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Main {

    private static InputReader in;
    static PrintWriter out;
    static AVLTree tree = new AVLTree();

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int numOfInitialPlayers = in.nextInt();
        for (int i = 0; i < numOfInitialPlayers; i++) {
            // TODO: process inputs
            String P = in.next();
            int S = in.nextInt();
            tree.root = tree.insertNode(tree.root, S, P);
        }

        int numOfQueries = in.nextInt();
        for (int i = 0; i < numOfQueries; i++) {
            String cmd = in.next();
            if (cmd.equals("MASUK")) {
                handleQueryMasuk();
            } else {
                handleQueryDuo();
            }
        }

        out.close();
    }

    static void handleQueryMasuk() {
        // TODO
        String P = in.next();
        int S = in.nextInt();
        tree.root = tree.insertNode(tree.root, S, P);
        int total = inorder(tree.root, S);
        out.println(total);
    }

    static int inorder(Node node, int S) { //bukan inorder wkwkwk
        if (node == null) {
            return 0;
        }
        else if (node.left == null && node.right == null && node.key < S) {
            return 1;
        }
        int total = 0;
        if (node.key < S) {
            total += inorder(node.right, S);
            total += inorder(node.left, S);
        }
        else if (node.key >= S){ // Traverse left
            total += inorder(node.left, S);
        }
        if (node.key < S) {
            total += 1;
        }
        return total;
    }

    static void handleQueryDuo() {
        // TODO
        int S = in.nextInt();
        int T = in.nextInt();
        String[] names = new String[2];
        names[0] = tree.lowerBound(tree.root, S);
        names[1] = tree.upperBound(tree.root, T);
        if (names[0].compareTo(names[1]) > 0) {
            String temp = names[0];
            names[0] = names[1];
            names[1] = temp;
            out.println(names[0] + " " + names[1]);
        }
        else {
            out.println(names[0] + " " + names[1]);
        }
    }

    // taken from https://codeforces.com/submissions/Petr
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


// TODO: modify as needed
class Node {
    String name;
    int key, height;
    Node left, right;

    Node(String name, int key) {
        this.name = name;
        this.key = key;
        this.height = 0;
    }
}


class AVLTree {

    Node root;

    //most avl function is edited from programiz.com
    Node rightRotate(Node node) { //salah gan
        // TODO: implement right rotate
        Node newNode1 = node.left;
        Node newNode2 = newNode1.right;
        newNode1.right = node;
        node.left = newNode2;
        node.height = max(getHeight(node.left), getHeight(node.right)) + 1;
        newNode1.height = max(getHeight(newNode1.left), getHeight(newNode1.right)) + 1;
        return newNode1;
    }

    Node leftRotate(Node node) {
        // TODO: implement left rotate
        Node newNode1 = node.right;
        Node newNode2 = newNode1.left;
        newNode1.left = node;
        node.right = newNode2;
        node.height = max(getHeight(node.left), getHeight(node.right)) + 1;
        newNode1.height = max(getHeight(newNode1.left), getHeight(newNode1.right)) + 1;
        return newNode1;
    }

    Node insertNode(Node node, int key, String playerName) {
        // TODO: implement insert node
        // Find the position and insert the node
        if (node == null) {
            node = new Node(playerName, key);
            return (node);
        }
        if (key <= node.key) {
            node.left = insertNode(node.left, key, playerName);
        }
        else if (key > node.key) {
            node.right = insertNode(node.right, key, playerName);
        }
        // Update the balance factor of each node
        // And, balance the tree
        node.height = 1 + max(getHeight(node.left), getHeight(node.right));
        int balance = getBalance(node);
        if (balance > 1) {
            if (key < node.left.key) {
                return rightRotate(node);
            } else if (key > node.left.key) {
                node.left = leftRotate(node.left);
                return rightRotate(node);
            }
        }
        if (balance < -1) {
            if (key > node.right.key) {
                return leftRotate(node);
            } else if (key < node.right.key) {
                node.right = rightRotate(node.right);
                return leftRotate(node);
            }
        }
        return node;
    }

    Node deleteNode(Node root, int key, String name) {
        // Find the node to be deleted and remove it
        if (root == null)
            return root;
        if (key < root.key)
            root.left = deleteNode(root.left, key, name);
        else if (key > root.key)
            root.right = deleteNode(root.right, key, name);
        else {
            if (root.name.compareTo(name) == 0) {
                if ((root.left == null) || (root.right == null)) {
                    Node temp = null;
                    if (temp == root.left)
                        temp = root.right;
                    else
                        temp = root.left;
                    if (temp == null) {
                        temp = root;
                        root = null;
                    } else
                        root = temp;
                } else {
                    Node temp = nodeWithMimumValue(root.right);
                    root.key = temp.key;
                    root.right = deleteNode(root.right, temp.key, name);
                }
            }
            else
                root.left = deleteNode(root.left, key, name);
        }
        if (root == null)
            return root;

        // Update the balance factor of each node and balance the tree
        root.height = max(getHeight(root.left), getHeight(root.right)) + 1;
        int balanceFactor = getBalance(root);
        if (balanceFactor > 1) {
            if (getBalance(root.left) >= 0) {
                return rightRotate(root);
            } else {
                root.left = leftRotate(root.left);
                return rightRotate(root);
            }
        }
        if (balanceFactor < -1) {
            if (getBalance(root.right) <= 0) {
                return leftRotate(root);
            } else {
                root.right = rightRotate(root.right);
                return leftRotate(root);
            }
        }
        return root;
    }

    String lowerBound(Node node, int value) {
        // TODO: return node with the lowest key that is >= value
        if (node.left != null && node.key >= value) {
            return lowerBound(node.left, value);
        } else if (node.right != null && node.key < value) {
            return lowerBound(node.right, value);
        }
        String name = node.name;
        deleteNode(root, node.key, node.name);
        return name;
    }

    String upperBound(Node node, int value) {
        // TODO: return node with the greatest key that is <= value
        if (node.right != null && node.key < value) {
            return upperBound(node.right, value);
        } else if (node.left != null && node.key > value) {
            return upperBound(node.left, value);
        }
        String name = node.name;
        deleteNode(root, node.key, node.name);
        return name;
    }

    // Utility function to get height of node
    int getHeight(Node node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    // Utility function to get balance factor of node
    int getBalance(Node node) {
        if (node == null) {
            return 0;
        }
        return getHeight(node.left) - getHeight(node.right);
    }

    int max (int a, int b) {
        return (a > b) ? a : b;
    }

    Node nodeWithMimumValue(Node node) {
        Node current = node;
        while (current.left != null)
            current = current.left;
        return current;
    }

}