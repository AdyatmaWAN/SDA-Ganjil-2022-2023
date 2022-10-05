import java.io.*;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.StringTokenizer;
import java.util.TreeSet;
// TODO : rubah equals ke "="
public class TP1_1 {
    static FastReader in;
    static PrintWriter out;

    // TODO : Merubah deque menjadi array agar bisa for loop dari index terakhir ke index pertama untuk mencari index terkecil
    // atau
    // TODO : Menghubungkan C dengan P dan L
    static TreeSet<koki> kokiAir = new TreeSet<koki>(koki::compareTo);
    static TreeSet<koki> kokiGround = new TreeSet<koki>(koki::compareTo);
    static TreeSet<koki> kokiSea = new TreeSet<koki>(koki::compareTo);
    static makanan[] listMakanan; //list seluruh makanan di restoran
    static koki[] listKoki; //list seluruh koki di restoran
    static pelanggan[] listPelangganID; //list seluruh pelanggan di restoran berdasarkan ID
    static Deque<pesanan> listPesanan; //list seluruh pesanan yang masuk ke restoran


    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new FastReader();
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int[] advancedScanning;

        int M = in.nextInt(); //insert makanan
        listMakanan = new makanan[M];
        for (int i = 0; i < M; i++) {
            listMakanan[i] = new makanan(in.nextInt(), in.next());
        }

        int V = in.nextInt(); //insert koki
        listKoki = new koki[V];
        for (int i = 0; i < V; i++) {
            String spesialisasi = in.next();
            listKoki[i] = new koki(i + 1, 0, spesialisasi);
            //memasukkan deque untuk koki sesuai spesialisasi agar bisa mengurutkan berdasarkan
            // banyak pelayanan tersedikit (head dari deque)
            if (spesialisasi.equals("A")) {
                kokiAir.add(listKoki[i]);
            } else if (spesialisasi.equals("G")) {
                kokiGround.add(listKoki[i]);
            } else if (spesialisasi.equals("S")) {
                kokiSea.add(listKoki[i]);
            }
        }

        int P = in.nextInt(); //insert total pelanggan
        listPelangganID = new pelanggan[P]; //list seluruh pelanggan di restoran

        int N = in.nextInt(); //insert total meja

        int Y = in.nextInt(); //insert total hari

        for (int i = 0; i < Y; i++) { //looping hari

            int Pi = in.nextInt(); //insert pelanggan hari ke-i
            listPesanan = new ArrayDeque<pesanan>();
            int count = 0;

            for (int j = 0; j < Pi; j++) {
                advancedScanning = new int[Pi];
                int id = in.nextInt();
                String status = in.next();
                int uang = in.nextInt();

                //TODO : fix this and make another array for pelanggan based on id
                if (status.equals("+") || status.equals("-")){
                    if (j == 0){
                        if (status.equals("+")) {
                            advancedScanning[j] = -1;
                        } else {
                            advancedScanning[j] = 1;
                        }
                    } else {
                        if (status.equals("+")) {
                            advancedScanning[j] = advancedScanning[j-1] -1;
                        } else {
                            advancedScanning[j] = advancedScanning[j-1] +1;
                        }
                    }
                } else if (status.equals("?")) { //menetukan positif negatif
                    int check = in.nextInt();
                    int checks;
                    if (j - check - 1 <= 0){
                        checks = advancedScanning[j - 1] + advancedScanning[0];
                    } else {
                        checks = advancedScanning[j - 1] - advancedScanning[j - check - 1];
                    }
                    if (checks >= 0) { //merubah status positif negatif
                        status = "-";
                        advancedScanning[j] = advancedScanning[j-1] + 1;
                    } else {
                        status = "+";
                        advancedScanning[j] = advancedScanning[j-1] - 1;
                    }

                }
                /* TODO : all of this
                    mencari apakah pelanggan
                    di BLACKLIST (terdaftar sebagai pelanggan keseluruha tapi tidak masuk),
                    POSITIF (terdaftar sebagai pelanggan keseluruha tapi tidak masuk),
                    masuk RUANG LAPAR (terdaftar sebagai pelanggan keseluruha tapi masuk ruang lapar)
                    MASUK pelayanan restoran (terdaftar sebagai pelanggan keseluruha dan masuk)
                     */
                if (listPelangganID[id - 1] != null) { //menyimpan pelanggan berdasarkan id
                    listPelangganID[id - 1].pelangganStatus = status;
                    listPelangganID[id - 1].pelangganUang = uang;
                } else {
                    listPelangganID[id - 1] = new pelanggan(id, status, false, uang);
                }
                if (listPelangganID[id - 1].pelangganBlacklist) {
                    out.print("3 ");
                } else if (listPelangganID[id - 1].pelangganStatus.equals("+")) {
                    out.print("0 ");
                } else { // TODO : Implementasi ruang lapar
                    count++;
                    if (count <= N) {
                        out.print("1 ");
                    } else {
                        out.print("2 ");
                    }
                }
            }
            out.print("\n");
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

    static void pesan(int idPelanggan, int idMakanan) { //membuat objek pesanan
        // TODO : cari koki dulu berdasarkan spesialisasi baru dibikin objek pesanan
        listPesanan.add(new pesanan(listPelangganID[idPelanggan - 1], listMakanan[idMakanan - 1]));
        pesanan pesananDiLayani = listPesanan.getLast();
        String tipeMakanan = pesananDiLayani.makanan.makananTipe;
        if (tipeMakanan.equals("A")) {
            pesananDiLayani.koki = kokiAir.first();
            out.print(kokiAir.first().kokiId);
        } else if (tipeMakanan.equals("G")) {
            pesananDiLayani.koki = kokiGround.first();
            out.print(kokiGround.first().kokiId);
        } else if (tipeMakanan.equals("S")) {
            pesananDiLayani.koki = kokiSea.first();
            out.print(kokiSea.first().kokiId);
        }
        out.print("\n");
    }

    static void layani() { //melayani
        pesanan pesananDiLayani = listPesanan.pollFirst();
        pesananDiLayani.pelanggan.pelangganHutang += pesananDiLayani.makanan.makananHarga;
        //out.print(pesananDiLayani.pelanggan.pelangganId);

        koki kokiTerpilih = pesananDiLayani.koki;

        // TODO : ngilngaingin sisa sisa dequeue
        if (kokiTerpilih.kokiSpesialisasi.equals("A")) {
            kokiAir.remove(kokiTerpilih);
            kokiTerpilih.kokiLayanan += 1;
            kokiAir.add(kokiTerpilih);
        } else if (kokiTerpilih.kokiSpesialisasi.equals("G")) {
            kokiGround.remove(kokiTerpilih);
            kokiTerpilih.kokiLayanan += 1;
            kokiGround.add(kokiTerpilih);
        } else if (kokiTerpilih.kokiSpesialisasi.equals("S")) {
            kokiSea.remove(kokiTerpilih);
            kokiTerpilih.kokiLayanan += 1;
            kokiSea.add(kokiTerpilih);
        }
        //out.print("\n");
    }

    static void bayar(int idPelanggan) {
        pelanggan pelangganDiLayani = listPelangganID[idPelanggan - 1];
        if (pelangganDiLayani.pelangganHutang > pelangganDiLayani.pelangganUang) {
            pelangganDiLayani.pelangganBlacklist = true;
            out.print("0");
        } else {
            pelangganDiLayani.pelangganUang -= pelangganDiLayani.pelangganHutang;
            out.print("1");
        }
        out.print("\n");
    }

    static void rankKoki(int nKoki) {
        TreeSet<koki> cloneA = (TreeSet<koki>)kokiSea.clone();

    }

    static class FastReader {
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
}

class koki implements Comparable<koki> {

    int kokiId;
    int kokiLayanan;
    String kokiSpesialisasi; // TODO : menjadikan int biar comparablenya enak

    public koki(int id, int layanan, String spesialisasi) {
        this.kokiId = id;
        this.kokiLayanan = layanan;
        this.kokiSpesialisasi = spesialisasi;
    }
    public int compareTo(koki koki) {
        if (this.kokiLayanan < koki.kokiLayanan) {
            return -1;
        } else if (this.kokiLayanan > koki.kokiLayanan) {
            return 1;
        } else {
            if (this.kokiId < koki.kokiId) {
                return -1;
            } else if (this.kokiId > koki.kokiId) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}

class pelanggan {

    int pelangganId;
    String pelangganStatus; // TODO : ganti int
    boolean pelangganBlacklist;
    long pelangganUang;
    long pelangganHutang;

    public pelanggan(int id, String pelangganStatus, boolean pelangganBlacklist, int pelangganUang) {
        this.pelangganId = id;
        this.pelangganStatus = pelangganStatus;
        this.pelangganBlacklist = pelangganBlacklist;
        this.pelangganUang = pelangganUang;
    }

}

class makanan {
    String makananTipe; // TODO : ganti int
    int makananHarga;

    public makanan(int harga, String tipe) {
        this.makananTipe = tipe;
        this.makananHarga = harga;
    }
}

class pesanan {
    pelanggan pelanggan;
    makanan makanan;
    koki koki;

    public pesanan(pelanggan pelanggan, makanan makanan) {
        this.pelanggan = pelanggan;
        this.makanan = makanan;
    }
}