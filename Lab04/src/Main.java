import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

// TODO: Lengkapi Class Gedung
class Gedung {
    int id;
    String nama;
    int jumlahLantai;
    Gedung selanjutnya;
    public Gedung(String nama, int jumlahLantai, Gedung selanjutnya) {
        this.nama = nama;
        this.jumlahLantai = jumlahLantai;
        this.selanjutnya = selanjutnya;
    }

}

// TODO: Lengkapi Class Karakter
class Karakter {
    int id;
    Gedung posisiGedung;
    int posisiLantai;
    int arah;

    public Karakter( int id, Gedung posisiGedung, int posisiLantai) {
        this.id = id;
        this.posisiGedung = posisiGedung;
        this.posisiLantai = posisiLantai;
        if (id == 1) {
            this.arah = 1;
        } else if (id == 2) {
            this.arah = -2;
        }
    }

}

public class Main {

    private static InputReader in;
    static PrintWriter out;
    static Karakter denji;
    static Karakter iblis;
    static int jumlahBertemu = 0;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int jumlahGedung = in.nextInt();
        String namaGedung = in.next();
        int jumlahLantai = in.nextInt();

        // TODO: Inisiasi gedung pada kondisi awal

        Gedung gedungPertama = new Gedung(namaGedung, jumlahLantai, null);
        gedungPertama.selanjutnya = gedungPertama;
        Gedung pointer1 = gedungPertama;
        Gedung pointer2 = gedungPertama;
        for (int i = 1; i < jumlahGedung; i++) {
            namaGedung = in.next();
            jumlahLantai = in.nextInt();
            pointer1 = new Gedung(namaGedung, jumlahLantai, gedungPertama);
            pointer2.selanjutnya = pointer1;
            pointer2 = pointer1;
        }

        String gedungDenji = in.next();
        int lantaiDenji = in.nextInt();
        // TODO: Tetapkan kondisi awal Denji
        pointer1 = gedungPertama;
        if (pointer1.nama.equals(gedungDenji)) {
            denji = new Karakter(1, pointer1, lantaiDenji);
        }
        else {
            while (!pointer1.nama.equals(gedungDenji)) {
                pointer1 = pointer1.selanjutnya;
            }
            denji = new Karakter(1, pointer1, lantaiDenji);
        }

        String gedungIblis = in.next();
        int lantaiIblis = in.nextInt();
        // TODO: Tetapkan kondisi awal Iblis
        pointer2 = gedungPertama;
        if (pointer2.nama.equals(gedungIblis)) {
            iblis = new Karakter(2, pointer2, lantaiIblis);
        }
        else {
            while (!pointer2.nama.equals(gedungIblis)) {
                pointer2 = pointer2.selanjutnya;
            }
            iblis = new Karakter(2, pointer2, lantaiIblis);
        }

        int Q = in.nextInt();

        for (int j = 0; j < Q; j++) {

            String command = in.next();

            if (command.equals("GERAK")) {
                gerak();
            } else if (command.equals("HANCUR")) {
                hancur();
            } else if (command.equals("TAMBAH")) {
                tambah();
            } else if (command.equals("PINDAH")) {
                pindah();
            }
        }
        out.close();
    }

    // TODO: Implemen perintah GERAK
    static void gerak() {
        if (denji.arah == 1){
            if (denji.posisiLantai == denji.posisiGedung.jumlahLantai) {
                denji.arah = -1;
                denji.posisiLantai = denji.posisiGedung.selanjutnya.jumlahLantai;
                denji.posisiGedung = denji.posisiGedung.selanjutnya;
            } else {
                denji.posisiLantai += 1;
            }
        } else {
            if (denji.posisiLantai == 1) {
                denji.arah = 1;
                denji.posisiLantai = 1;
                denji.posisiGedung = denji.posisiGedung.selanjutnya;
            } else {
                denji.posisiLantai -= 1;
            }
        }
        if (denji.posisiLantai == iblis.posisiLantai && denji.posisiGedung == iblis.posisiGedung) {
            jumlahBertemu+=1;
        }
        if (iblis.arah == 2){
            if (iblis.posisiLantai == iblis.posisiGedung.jumlahLantai) {
                iblis.arah = -2;
                iblis.posisiGedung = iblis.posisiGedung.selanjutnya;
                iblis.posisiLantai = iblis.posisiGedung.jumlahLantai;
                if (iblis.posisiLantai - 1 < 1) {
                    iblis.arah = 2;
                    iblis.posisiGedung = iblis.posisiGedung.selanjutnya;
                    iblis.posisiLantai = 1;
                } else if (iblis.posisiLantai - 1 >= 1) {
                    iblis.posisiLantai -= 1;
                } else {
                    iblis.posisiLantai = iblis.posisiGedung.selanjutnya.jumlahLantai - 1;
                }
            } else if (iblis.posisiLantai == iblis.posisiGedung.jumlahLantai - 1) {
                iblis.arah = -2;
                iblis.posisiLantai = iblis.posisiGedung.selanjutnya.jumlahLantai;
                iblis.posisiGedung = iblis.posisiGedung.selanjutnya;
            } else {
                iblis.posisiLantai += 2;
            }
        } else {
            if (iblis.posisiLantai == 1) {
                iblis.arah = 2;
                iblis.posisiGedung = iblis.posisiGedung.selanjutnya;
                iblis.posisiLantai = 1;
                if (iblis.posisiLantai + 1 > iblis.posisiGedung.jumlahLantai) {
                    iblis.posisiGedung = iblis.posisiGedung.selanjutnya;
                    iblis.posisiLantai = iblis.posisiGedung.jumlahLantai;
                    iblis.arah = -2;
                } else if (iblis.posisiLantai + 1 <= iblis.posisiGedung.jumlahLantai){
                    iblis.posisiLantai += 1;
                }else {
                    iblis.posisiLantai += 1;
                }
            } else if (iblis.posisiLantai == 2) {
                iblis.arah = 2;
                iblis.posisiLantai = 1;
                iblis.posisiGedung = iblis.posisiGedung.selanjutnya;
            } else {
                iblis.posisiLantai -= 2;
            }
        }
        if (denji.posisiLantai == iblis.posisiLantai && denji.posisiGedung == iblis.posisiGedung) {
            jumlahBertemu += 1;
        }
        out.println(denji.posisiGedung.nama + " " + denji.posisiLantai + " " + iblis.posisiGedung.nama + " " + iblis.posisiLantai + " " + jumlahBertemu);
    }

    // TODO: Implemen perintah HANCUR + outputnya
    static void hancur() {
        if (denji.posisiLantai != 1) {
            if (denji.posisiGedung != iblis.posisiGedung) {
                denji.posisiGedung.jumlahLantai -= 1;
                denji.posisiLantai -= 1;
                out.println(denji.posisiGedung.nama + ' ' + denji.posisiLantai);
            } else {
                if (denji.posisiLantai <= iblis.posisiLantai) {
                    denji.posisiGedung.jumlahLantai -= 1;
                    denji.posisiLantai -=1;
                    iblis.posisiLantai -=1;
                    out.println(denji.posisiGedung.nama + ' ' + denji.posisiLantai);
                } else if (denji.posisiLantai > iblis.posisiLantai) {
                    if (denji.posisiLantai - 1 == iblis.posisiLantai) {
                        //TODO : iblis dibawah denji persis
                        out.println(denji.posisiGedung.nama + " -1");
                    } else {
                        denji.posisiGedung.jumlahLantai -=1;
                        denji.posisiLantai -=1;
                        out.println(denji.posisiGedung.nama + ' ' + denji.posisiLantai);
                    }
                }
            }
        } else {
            //TODO : denji di lantai 1
            out.println(denji.posisiGedung.nama + " -1");
        }
    }

    // TODO: Implemen perintah TAMBAH + outputnya
    static void tambah() {
        out.println(iblis.posisiGedung.nama + " " + iblis.posisiLantai);
        if (iblis.posisiGedung != denji.posisiGedung){
            iblis.posisiLantai++;
            iblis.posisiGedung.jumlahLantai +=1;
        } else if (iblis.posisiLantai <= denji.posisiLantai) {
            iblis.posisiLantai+=1;
            iblis.posisiGedung.jumlahLantai+=1;
            denji.posisiLantai+=1;
        } else {
            iblis.posisiGedung.jumlahLantai+=1;
            iblis.posisiLantai+=1;
        }
    }

    // TODO: Implemen perintah PINDAH
    static void pindah() {
        if (denji.arah == 1){
            denji.posisiLantai = 1;
            denji.posisiGedung = denji.posisiGedung.selanjutnya;
        } else {
            denji.posisiGedung = denji.posisiGedung.selanjutnya;
            denji.posisiLantai = denji.posisiGedung.jumlahLantai;
        }
        if (denji.posisiLantai == iblis.posisiLantai && denji.posisiGedung == iblis.posisiGedung) {
            jumlahBertemu+=1;
        }
        out.println(denji.posisiGedung.nama + " " + denji.posisiLantai);
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