import java.io.*;
import java.util.*;

public class TP1_1 {
    static InputReader in;
    static PrintWriter out;

    // TODO : Merubah deque menjadi array agar bisa for loop dari index terakhir ke index pertama untuk mencari index terkecil
    // atau
    // TODO : Menghubungkan C dengan P dan L
    static Deque<koki> kokiAir = new ArrayDeque<koki>();
    static Deque<koki> kokiGround = new ArrayDeque<koki>();
    static Deque<koki> kokiSea = new ArrayDeque<koki>();
    static makanan[] listMakanan; //list seluruh makanan di restoran
    static koki[] listKoki; //list seluruh koki di restoran
    static pelanggan[] listPelangganID; //list seluruh pelanggan di restoran berdasarkan ID
    static Deque<pesanan> listPesanan; //list seluruh pesanan yang masuk ke restoran

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int M = in.nextInt(); //insert makanan
        listMakanan = new makanan[M];
        for (int i = 0; i < M; i++) {
            listMakanan[i] = new makanan(in.nextInt(), in.next());
        }

        int V = in.nextInt(); //insert koki
        listKoki = new koki[V];
        for (int i = 0; i < V; i++){
            String spesialisasi = in.next();
            listKoki[i] = new koki(i+1, 0, spesialisasi);
            //memasukkan deque untuk koki sesuai spesialisasi agar bisa mengurutkan berdasarkan
            // banyak pelayanan tersedikit (head dari deque)
            if (spesialisasi.equals("A")){
                kokiAir.add(listKoki[i]);
            } else if (spesialisasi.equals("G")){
                kokiGround.add(listKoki[i]);
            } else if (spesialisasi.equals("S")){
                kokiSea.add(listKoki[i]);
            }
        }

        int P = in.nextInt(); //insert total pelanggan
        listPelangganID = new pelanggan[P]; //list seluruh pelanggan di restoran

        int N = in.nextInt(); //insert total meja

        int Y = in.nextInt(); //insert total hari

        for (int i = 0; i < Y; i++){ //looping hari

            int Pi = in.nextInt(); //insert pelanggan hari ke-i
            pelanggan[] listPelanggan = new pelanggan[Pi]; //list pelanggan hari ke-i
            listPesanan = new ArrayDeque<pesanan>();
            int count = 0;

            for (int j = 0; j < Pi; j++){
                int id = in.nextInt();
                String status = in.next();
                int uang = in.nextInt();

                //TODO : fix this and make another array for pelanggan based on id

                if (status.equals("?")){ //menetukan positif negatif
                    int check = in.nextInt();
                    int positive = 0;
                    int negative = 0;

                    for (int k = j - check; k < j; k++){ //menghitung positif negatif
                        if (listPelanggan[k].pelangganStatus.equals("+")){
                            positive++;
                        } else if (listPelanggan[k].pelangganStatus.equals("-")){
                            negative++;
                        }
                    }
                    if (negative >= positive){ //merubah status positif negatif
                        status = "-";
                    } else {
                        status = "+";
                    }
                }
                /* TODO : all of this
                    mencari apakah pelanggan
                    di BLACKLIST (terdaftar sebagai pelanggan keseluruha tapi tidak masuk),
                    POSITIF (terdaftar sebagai pelanggan keseluruha tapi tidak masuk),
                    masuk RUANG LAPAR (terdaftar sebagai pelanggan keseluruha tapi masuk ruang lapar)
                    MASUK pelayanan restoran (terdaftar sebagai pelanggan keseluruha dan masuk)
                     */
                if (listPelangganID[id-1] != null){ //menyimpan pelanggan berdasarkan id
                    listPelangganID[id-1].pelangganStatus = status;
                    listPelangganID[id-1].pelangganUang = uang;
                }else {
                    listPelangganID[id-1] = new pelanggan(id, status, false, uang);
                }
                if (listPelangganID[id-1].pelangganBlacklist == true){
                    out.print("3 ");
                } else if (listPelangganID[id-1].pelangganStatus.equals("+")){
                    out.print("0 ");
                } else{ // TODO : Implementasi ruang lapar
                    count++;
                    if (count <= N){
                        out.print("1 ");
                    } else {
                        out.print("2 ");
                    }
                }
                listPelanggan[j] = listPelangganID[id-1]; //memasukkan pelanggan ke list pelanggan hari ke-i
            }
            //setelah pelanggan hari ke-i selesai, mulai pesanan. TODO : Implementasi pesanan
            int Xi = in.nextInt(); //insert pesanan hari ke-i
            for (int j = 0; j < Xi; j++) {
                String pesanan = in.next();
                if (pesanan.equals("P")) {
                    //TODO : Implementasi pesan
                    int idPelanggan = in.nextInt();
                    int idMakanan = in.nextInt();
                    pesan(idPelanggan, idMakanan);

                } else if (pesanan.equals("L")) {
                    //TODO : Implementasi layani pesanan
                    layani();

                } else if (pesanan.equals("B")) {
                    //TODO : Implementasi bayar pesanan
                    int idPelanggan = in.nextInt();
                    bayar(idPelanggan);

                } else if (pesanan.equals("C")) {
                    //TODO : Implementasi urutan koki layanan tersedikit
                    int nKoki = in.nextInt();
                    rankKoki(nKoki);

                }
            }
        }
        out.close();
    }
    static void pesan(int idPelanggan, int idMakanan){ //membuat objek pesanan
        listPesanan.add(new pesanan(listPelangganID[idPelanggan-1], listMakanan[idMakanan-1]));
        pesanan pesananDiLayani = listPesanan.peek();
        String tipeMakanan = pesananDiLayani.makanan.makananTipe;
        if (tipeMakanan.equals("A")){
            pesananDiLayani.koki = kokiAir.peek();
            out.print("\n" + kokiAir.peek().kokiId);
        } else if (tipeMakanan.equals("G")){
            pesananDiLayani.koki = kokiGround.peek();
            out.print("\n" + kokiGround.peek().kokiId);
        } else if (tipeMakanan.equals("S")){
            pesananDiLayani.koki = kokiSea.peek();
            out.print("\n" + kokiSea.peek().kokiId);
        }
    }
    static void layani(){ //melayani
        pesanan pesananDiLayani = listPesanan.peek();
        pesananDiLayani.pelanggan.pelangganHutang += pesananDiLayani.makanan.makananHarga;
        out.print("\n" + pesananDiLayani.pelanggan.pelangganId);

        koki kokiTerpilih = pesananDiLayani.koki;
        kokiTerpilih.kokiLayanan += 1;
        if (kokiTerpilih.kokiSpesialisasi.equals("A") && kokiTerpilih == kokiAir.peek()){
            kokiAir.removeFirst();
            kokiAir.add(kokiTerpilih);
        } else if (kokiTerpilih.kokiSpesialisasi.equals("G") && kokiTerpilih == kokiGround.peek()){
            kokiGround.removeFirst();
            kokiGround.add(kokiTerpilih);
        } else if (kokiTerpilih.kokiSpesialisasi.equals("S") && kokiTerpilih == kokiSea.peek()){
            kokiSea.removeFirst();
            kokiSea.add(kokiTerpilih);
        }
    }
    static void bayar(int idPelanggan){
        pelanggan pelangganDiLayani = listPelangganID[idPelanggan-1];
        if (pelangganDiLayani.pelangganHutang > pelangganDiLayani.pelangganUang){
            pelangganDiLayani.pelangganBlacklist = true;
            out.print("\n0");
        }else {
            pelangganDiLayani.pelangganUang -= pelangganDiLayani.pelangganHutang;
            out.print("\n1");
        }
    }
    static void rankKoki(int nKoki){
        for (int i = 0; i < nKoki; i++){

        }
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

class koki {

    int kokiId;
    int kokiLayanan;
    String kokiSpesialisasi;

    public koki(int id, int layanan, String spesialisasi) {
        this.kokiId = id;
        this.kokiLayanan = layanan;
        this.kokiSpesialisasi = spesialisasi;
    }

}

class pelanggan {

    int pelangganId;
    String pelangganStatus;
    boolean pelangganBlacklist;
    int pelangganUang;
    int pelangganHutang;

    public pelanggan(int id, String pelangganStatus, boolean pelangganBlacklist, int pelangganUang) {
        this.pelangganId = id;
        this.pelangganStatus = pelangganStatus;
        this.pelangganBlacklist = pelangganBlacklist;
        this.pelangganUang = pelangganUang;
    }

}

class makanan {
    String makananTipe;
    int makananHarga;

    public makanan(int harga, String tipe) {
        this.makananTipe = tipe;
        this.makananHarga = harga;
    }
}

class pesanan{
    pelanggan pelanggan;
    makanan makanan;
    koki koki;

    public pesanan(pelanggan pelanggan, makanan makanan){
        this.pelanggan = pelanggan;
        this.makanan = makanan;
    }
}