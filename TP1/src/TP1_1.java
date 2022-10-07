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
    static TreeSet<koki> kokiAll = new TreeSet<koki>(koki::compareTo);
    static makanan[] listMakanan; //list seluruh makanan di restoran
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
            int tipeMakanan;
            int hargaMakanan = in.nextInt();
            String namaMakanan = in.next();
            if (namaMakanan.equals("S")) {
                tipeMakanan = 0;
            } else if (namaMakanan.equals("G")) {
                tipeMakanan = 1;
            } else {
                tipeMakanan = 2;
            }
            listMakanan[i] = new makanan(hargaMakanan, tipeMakanan);
        }

        int V = in.nextInt(); //insert koki
        for (int i = 0; i < V; i++) {
            String namaMakanan = in.next();
            if (namaMakanan.equals("S")) {
                kokiSea.add(new koki(i + 1, 0, 0));
                kokiAll.add(new koki(i + 1, 0, 0));
            } else if (namaMakanan.equals("G")) {
                kokiGround.add(new koki(i + 1, 0, 1));
                kokiAll.add(new koki(i + 1, 0, 1));
            } else {
                kokiAir.add(new koki(i + 1, 0, 2));
                kokiAll.add(new koki(i + 1, 0, 2));
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
            int statusBaru = 0;
            advancedScanning = new int[Pi];
            for (int j = 0; j < Pi; j++) {
                int id = in.nextInt();
                String status = in.next();
                int uang = in.nextInt();
                /*
                if (status.equals("+") || status.equals("-")){
                    if (j == 0){
                        if (status.equals("+")) {
                            advancedScanning[j] = -1;
                            statusBaru = -1;
                        } else {
                            advancedScanning[j] = 1;
                            statusBaru = 1;
                        }
                    } else {
                        if (status.equals("+")) {
                            advancedScanning[j] = advancedScanning[j-1] -1;
                            statusBaru = -1;
                        } else {
                            advancedScanning[j] = advancedScanning[j-1] +1;
                            statusBaru = 1;
                        }
                    }
                } else if (status.equals("?")) { //menetukan positif negatif
                    int check = in.nextInt();
                    int checks;
                    if (j - check - 1 < 0){
                        checks = advancedScanning[j - 1];
                    } else {
                        checks = advancedScanning[j - 1] - advancedScanning[j - check - 1];
                    }
                    if (checks >= 0) { //merubah status positif negatif
                        statusBaru = 1;
                        advancedScanning[j] = advancedScanning[j-1] + 1;

                    } else {
                        statusBaru = -1;
                        advancedScanning[j] = advancedScanning[j-1] - 1;
                    }

                }*/
                if (status.equals("+")){
                    if (j == 0){
                        advancedScanning[j] = 1;
                        statusBaru = 1;
                    } else {
                        advancedScanning[j] = advancedScanning[j-1] + 1;
                        statusBaru = 1;
                    }
                } else if (status.equals("-")){
                    if (j == 0){
                        advancedScanning[j] = -1;
                        statusBaru = -1;
                    } else {
                        advancedScanning[j] = advancedScanning[j-1] - 1;
                        statusBaru = -1;
                    }
                } else {
                    int rangeCheck = in.nextInt();
                    int hasilScan = 0;
                    if (j - rangeCheck - 1 < 0){
                        hasilScan = advancedScanning[j - 1];
                    } else {
                        hasilScan = advancedScanning[j - 1] - advancedScanning[j - rangeCheck - 1];
                    }
                    if (hasilScan > 0) {
                        statusBaru = 1;
                        advancedScanning[j] = advancedScanning[j-1] + 1;

                    } else {
                        statusBaru = -1;
                        advancedScanning[j] = advancedScanning[j-1] - 1;
                    }
                    //out.println(hasilScan);
                }

                if (listPelangganID[id - 1] != null) { //menyimpan pelanggan berdasarkan id
                    listPelangganID[id - 1].pelangganStatus = statusBaru;
                    listPelangganID[id - 1].pelangganUang = uang;
                } else {
                    listPelangganID[id - 1] = new pelanggan(id, statusBaru, false, uang);
                }
                if (listPelangganID[id - 1].pelangganBlacklist) {
                    out.print("3 ");
                } else if (listPelangganID[id - 1].pelangganStatus == 1) {
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
//            out.println(advancedScanning.toString());
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

                } else if (pesanan.equals("D")) {
                    int hargaMakanan = 0;
                    int hargaA = in.nextInt();
                    int hargaG = in.nextInt();
                    int hargaS = in.nextInt();
                    for (int n = 0; n < listMakanan.length; n++) {
                        if (listMakanan[n] != null) {
                            hargaMakanan += listMakanan[n].makananHarga;
                        }
                    }
                    out.println(hargaMakanan);
                }
            }
        }
        out.close();
    }

    static void pesan(int idPelanggan, int idMakanan) { //membuat objek pesanan
        // TODO : cari koki dulu berdasarkan spesialisasi baru dibikin objek pesanan
        makanan makanan = listMakanan[idMakanan - 1];
        int tipeMakanan = makanan.makananTipe;
        koki koki;
        if (tipeMakanan == 0){
            koki = kokiSea.first();
        } else if (tipeMakanan == 1){
            koki = kokiGround.first();
        } else {
            koki = kokiAir.first();
        }
        out.println(koki.kokiId);
        listPesanan.add(new pesanan(listPelangganID[idPelanggan - 1], makanan, koki));
        /*pesanan pesananDiLayani = listPesanan.getLast();
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
        }*/
    }

    static void layani() { //melayani
        pesanan pesananDiLayani = listPesanan.pollFirst();
        pesananDiLayani.pelanggan.pelangganHutang += pesananDiLayani.makanan.makananHarga;
        out.println(pesananDiLayani.pelanggan.pelangganId);

        koki kokiTerpilih = pesananDiLayani.koki;
        kokiAll.remove(kokiTerpilih);

        if (kokiTerpilih.kokiSpesialisasi == 2) {
            kokiAir.remove(kokiTerpilih);
            kokiTerpilih.kokiLayanan += 1;
            kokiAir.add(kokiTerpilih);
        } else if (kokiTerpilih.kokiSpesialisasi == 1) {
            kokiGround.remove(kokiTerpilih);
            kokiTerpilih.kokiLayanan += 1;
            kokiGround.add(kokiTerpilih);
        } else if (kokiTerpilih.kokiSpesialisasi == 0) {
            kokiSea.remove(kokiTerpilih);
            kokiTerpilih.kokiLayanan += 1;
            kokiSea.add(kokiTerpilih);
        }
        kokiAll.add(kokiTerpilih);
    }

    static void bayar(int idPelanggan) {
        pelanggan pelangganDiLayani = listPelangganID[idPelanggan - 1];
        if (pelangganDiLayani.pelangganHutang > pelangganDiLayani.pelangganUang) {
            pelangganDiLayani.pelangganBlacklist = true;
            out.println("0");
        } else {
            pelangganDiLayani.pelangganUang -= pelangganDiLayani.pelangganHutang;
            out.println("1");
        }
        pelangganDiLayani.pelangganHutang = 0;
    }

    static void rankKoki(int nKoki) {
        TreeSet<koki> kokiAllClone = (TreeSet<koki>)kokiAll.clone();
        for (int k = 0; k < nKoki; k++) {
            koki kokiTerpilih = kokiAllClone.pollFirst();
            out.print(kokiTerpilih.kokiId + " ");
        }
        out.println();
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
    int kokiSpesialisasi; // TODO : menjadikan int biar comparablenya enak

    public koki(int id, int layanan, int spesialisasi) {
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
            if (this.kokiSpesialisasi < koki.kokiSpesialisasi) {
                return -1;
            } else if (this.kokiSpesialisasi > koki.kokiSpesialisasi) {
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
}

class pelanggan {

    int pelangganId;
    int pelangganStatus; // TODO : ganti int
    boolean pelangganBlacklist;
    long pelangganUang;
    long pelangganHutang;

    public pelanggan(int id, int pelangganStatus, boolean pelangganBlacklist, int pelangganUang) {
        this.pelangganId = id;
        this.pelangganStatus = pelangganStatus;
        this.pelangganBlacklist = pelangganBlacklist;
        this.pelangganUang = pelangganUang;
    }

}

class makanan {
    int makananTipe;
    int makananHarga;

    public makanan(int harga, int tipe) {
        this.makananTipe = tipe;
        this.makananHarga = harga;
    }
}

class pesanan {
    pelanggan pelanggan;
    makanan makanan;
    koki koki;

    public pesanan(pelanggan pelanggan, makanan makanan, koki koki) {
        this.pelanggan = pelanggan;
        this.makanan = makanan;
        this.koki = koki;
    }
}