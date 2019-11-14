import java.io.*;
import java.lang.Math;
import java.util.LinkedHashMap;
import java.util.Arrays;
import java.util.StringTokenizer;


class TP3{
    private static InputReader in;
    private static PrintWriter out;

    static LinkedHashMap<String, Node> listWilayah = new LinkedHashMap<String, Node>();
    static LinkedHashMap<String, Integer> wilMngCalon = new LinkedHashMap<String, Integer>();
    static LinkedHashMap<String, Node> wilProvinsi = new LinkedHashMap<String, Node>();
    static LinkedHashMap<String, int[]> persenClnPerWil = new LinkedHashMap<String, int[]>();
    static int[] calon1persen = new int[101];
    static int[] calon2persen = new int[101];

    public static void main(String[] args) throws IOException {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        readInput();
        out.close();
    }

    private static void readInput() throws IOException{
        String inpt = in.nextLine();
        String[] listCalon = inpt.split(" ");
        for (String cln : listCalon){
            wilMngCalon.put(cln, 0);
            persenClnPerWil.put(cln, new int[101]);
        }

        int jmlCmnd = Integer.parseInt(in.nextLine());
        
        String[] listInpt;

        for (int i = 0; i < jmlCmnd; i++) {
            listInpt = in.nextLine().split(" ");
            String namaSuperWil = listInpt[0];
            Node superWil;
            int jmlSub = Integer.parseInt(listInpt[1]);
            
            if(listWilayah.get(namaSuperWil)!=null){
                superWil = listWilayah.get(namaSuperWil);
            } else {
                superWil = new Node(namaSuperWil);
                listWilayah.put(namaSuperWil, superWil);
            }

            for (int j = 0; j < jmlSub; j++) {
                Node subWil = new Node(superWil, listInpt[2+j]);
                if(i==0){
                    wilProvinsi.put(listInpt[2+j], subWil);
                }
                listWilayah.put(listInpt[2+j], subWil);
                // superWil.addSubWil(subWil);
            }
        }
        int[] tempArr;
        tempArr = persenClnPerWil.get(listCalon[0]);
        tempArr[50] = jmlCmnd;
        persenClnPerWil.put(listCalon[0], tempArr);
        persenClnPerWil.put(listCalon[1], tempArr);
            // String[] subWil = new String[jmlSub];
            
            // for (int j = 0; j < jmlSub; j++) {
            //     subWil[j] = listInpt[2+j];
            // }
        // System.out.println(listWilayah.toString());
        
        
        jmlCmnd = Integer.parseInt(in.nextLine());
        for (int i = 0; i < jmlCmnd; i++) {
            String[] inputs = in.nextLine().split(" ");
            String command = inputs[0];
            if(command.equals("TAMBAH")){
                String wilayah = inputs[1];
                Node wil = listWilayah.get(wilayah);
                int suara1 = Integer.parseInt(inputs[2]);
                int suara2 = Integer.parseInt(inputs[3]);
                tambahSuara(wil,listCalon[0], suara1,listCalon[1], suara2);
            } else if(command.equals("ANULIR")){
                String wilayah = inputs[1];
                Node wil = listWilayah.get(wilayah);
                int suara1 = Integer.parseInt(inputs[2]);
                int suara2 = Integer.parseInt(inputs[3]);
                kurangSuara(wil,listCalon[0], suara1,listCalon[1], suara2);
            } else if(command.equals("CEK_SUARA")){
                String wilayah = inputs[1];
                Node wil = listWilayah.get(wilayah);
                out.println(wil.cekSuara());
            } else if (command.equals("WILAYAH_MENANG")){
                String calon = inputs[1];
                int jmlWil = wilMngCalon.get(calon);
                out.println(jmlWil);
            } else if(command.equals("CEK_SUARA_PROVINSI")){
                for (String prov : wilProvinsi.keySet()) {
                    out.println(prov + " " + wilProvinsi.get(prov).cekSuara());
                }
            } else if(command.equals("WILAYAH_MINIMAL")){
                String calon = inputs[1];
                int prsn = Integer.parseInt(inputs[2]);
                int wilMin = 0;
                calon1persen = new int[101];
                calon2persen = new int[101];
                // out.println(Arrays.toString(persenClnPerWil.get(calon)));
                // out.println(persenClnPerWil.get(calon)[prsn]);
                for (Node item : listWilayah.values()) {
                    calon1persen[item.getPersen1()] += 1;
                    calon2persen[item.getPersen2()] += 1;
                }

                for (int j = prsn; j < 101; j++) {
                    if(calon.equals(listCalon[0])){
                        wilMin = wilMin + calon1persen[j];
                    } else {
                        wilMin = wilMin + calon2persen[j];
                    }
                    System.out.println("//" + j + "++" + calon1persen[j]);
                }
                // for (int k = prsn; k < 101; k++) {
                    //     // wilMin = wilMin + persenClnPerWil.get(calon)[k];
                    // System.out.println("//" + k + "++" + persenClnPerWil.get(calon)[k]);

                // }
                out.println(wilMin);
            } else if(command.equals("WILAYAH_SELISIH")){
                int selMin = Integer.parseInt(inputs[1]);
                int wilSel = 0;
                for (Node item : listWilayah.values()) {
                    if(item.getSelisih()>=selMin){
                        wilSel += 1;
                    }
                }
                out.println(wilSel);
            }
        }

        // System.out.println(listWilayah.toString());
    }


    private static void tambahSuara(Node wil,String calon1, int suara1,String calon2, int suara2) {
        if(wil!= null){
            wil.setPemenang();
            wil.setPersentase();

            int lastPmngWil = wil.getPemenang();

            // int lastPersen1 = wil.getPersen1();
            // int lastPersen2 = wil.getPersen2();

            wil.addSuara(0, suara1);
            wil.addSuara(1, suara2);
            wil.setPemenang();
            wil.setPersentase();

            int crntPmngWil = wil.getPemenang();

            // int crntPersen1 = wil.getPersen1();
            // int crntPersen2 = wil.getPersen2();
            // out.println(calon1 +"//"+lastPersen1 + "++" + crntPersen1 + "[[" + wil.getSuara1() + "||" + calon2 + "//" + lastPersen2 + "++" + crntPersen2 + "[[" + wil.getSuara2());
            if(lastPmngWil!=crntPmngWil){
                if(lastPmngWil == 0){
                    wilMngCalon.put(calon1, wilMngCalon.get(calon1)-1);
                } else if(lastPmngWil == 1){
                    wilMngCalon.put(calon2, wilMngCalon.get(calon2)-1);
                } else {

                }

                if(wil.getSuara1() > wil.getSuara2()){
                    wilMngCalon.put(calon1, wilMngCalon.get(calon1)+1);
                }
                
                if(wil.getSuara1() < wil.getSuara2()){
                    wilMngCalon.put(calon2, wilMngCalon.get(calon2)+1);
                }
            }

            // if((lastPersen1!=crntPersen1) || (lastPersen2!=crntPersen2)){
            //     int[] arrPrsnCln1 = persenClnPerWil.get(calon1);
            //     int[] arrPrsnCln2 = persenClnPerWil.get(calon2);

            //     arrPrsnCln1[lastPersen1] -= 1;
            //     arrPrsnCln2[lastPersen2] -= 1;
            //     persenClnPerWil.put(calon1, arrPrsnCln1);
            //     persenClnPerWil.put(calon2, arrPrsnCln2);

            //     if(arrPrsnCln1[lastPersen1]<=0){
            //         arrPrsnCln1[lastPersen1] = 0;
            //         persenClnPerWil.put(calon1, arrPrsnCln1);
            //     }

            //     if(arrPrsnCln2[lastPersen2]<=0){
            //         arrPrsnCln2[lastPersen2] = 0;
            //         persenClnPerWil.put(calon2, arrPrsnCln2);
            //     }

            //     arrPrsnCln1[crntPersen1] += 1;
            //     arrPrsnCln2[crntPersen2] += 1;
            //     persenClnPerWil.put(calon1, arrPrsnCln1);
            //     persenClnPerWil.put(calon2, arrPrsnCln2);

            // }

            // if(lastPersen1!=crntPersen1){
            //     int[] arrPrsnCln1 = persenClnPerWil.get(calon1);

            //     // if(arrPrsnCln1[lastPersen1]>0){
            //     // }
            //     arrPrsnCln1[lastPersen1] -= 1;
                
            //     arrPrsnCln1[crntPersen1] += 1;
                
            //     persenClnPerWil.put(calon1, arrPrsnCln1);
            // }
            
            // if(lastPersen2!=crntPersen2){
            
            //     int[] arrPrsnCln2 = persenClnPerWil.get(calon2);

            //     // if(arrPrsnCln2[lastPersen2]>0){
            //     // }
            //     arrPrsnCln2[lastPersen2] -= 1;

            //     arrPrsnCln2[crntPersen2] += 1;

            //     persenClnPerWil.put(calon2, arrPrsnCln2);
            // }

            tambahSuara(wil.getSuperWil(), calon1, suara1, calon2, suara2);
        } else {
            return;
        }
    }
    

    private static void kurangSuara(Node wil,String calon1, int suara1,String calon2, int suara2) {
        if(wil!= null){
            wil.setPemenang();
            wil.setPersentase();

            int lastPmngWil = wil.getPemenang();

            // int lastPersen1 = wil.getPersen1();
            // int lastPersen2 = wil.getPersen2();

            wil.subSuara(0, suara1);
            wil.subSuara(1, suara2);
            wil.setPemenang();
            wil.setPersentase();

            int crntPmngWil = wil.getPemenang();

            // int crntPersen1 = wil.getPersen1();
            // int crntPersen2 = wil.getPersen2();


            if(lastPmngWil!=crntPmngWil){
                if(lastPmngWil == 0){
                    wilMngCalon.put(calon1, wilMngCalon.get(calon1)-1);
                } else if(lastPmngWil == 1){
                    wilMngCalon.put(calon2, wilMngCalon.get(calon2)-1);
                } else {

                }
                
                if(wil.getSuara1() > wil.getSuara2()){
                    wilMngCalon.put(calon1, wilMngCalon.get(calon1)+1);
                }
                
                if(wil.getSuara1()<wil.getSuara2()){
                    wilMngCalon.put(calon2, wilMngCalon.get(calon2)+1);
                }
            }

            // if((lastPersen1!=crntPersen1) || (lastPersen2!=crntPersen2)){
            //     int[] arrPrsnCln1 = persenClnPerWil.get(calon1);
            //     int[] arrPrsnCln2 = persenClnPerWil.get(calon2);

            //     arrPrsnCln1[lastPersen1] -= 1;
            //     arrPrsnCln2[lastPersen2] -= 1;
            //     persenClnPerWil.put(calon1, arrPrsnCln1);
            //     persenClnPerWil.put(calon2, arrPrsnCln2);

            //     if(arrPrsnCln1[lastPersen1]<=0){
            //         arrPrsnCln1[lastPersen1] = 0;
            //         persenClnPerWil.put(calon1, arrPrsnCln1);
            //     }

            //    if(arrPrsnCln2[lastPersen2]<=0){
            //         arrPrsnCln2[lastPersen2] = 0;
            //         persenClnPerWil.put(calon2, arrPrsnCln2);
            //     }

            //     arrPrsnCln1[crntPersen1] += 1;
            //     arrPrsnCln2[crntPersen2] += 1;
            //     persenClnPerWil.put(calon1, arrPrsnCln1);
            //     persenClnPerWil.put(calon2, arrPrsnCln2);

            // }

            // if(lastPersen1!=crntPersen1){
            //     int[] arrPrsnCln1 = persenClnPerWil.get(calon1);

            //     // if(arrPrsnCln1[lastPersen1]>0){
            //     // }
            //     arrPrsnCln1[lastPersen1] -= 1;

            //     arrPrsnCln1[crntPersen1] += 1;
                
            //     persenClnPerWil.put(calon1, arrPrsnCln1);
            // }

            // if(lastPersen2!=crntPersen2){
            
            //     int[] arrPrsnCln2 = persenClnPerWil.get(calon2);

            //     // if(arrPrsnCln2[lastPersen2]>0){
            //     // }
            //     arrPrsnCln2[lastPersen2] -= 1;

            //     arrPrsnCln2[crntPersen2] += 1;

            //     persenClnPerWil.put(calon2, arrPrsnCln2);
            // }

            kurangSuara(wil.getSuperWil(), calon1, suara1, calon2, suara2);
        } else {
            return;
        }
    }

    private static void printOutput(String answer) throws IOException {
        out.println(answer);
    }

    static class InputReader {
        // taken from https://codeforces.com/submissions/Petr
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

        public String nextLine() throws IOException {
            return reader.readLine();
        }
    }    
}

class Node{
    Node superWil = null;
    String namaWilayah;
    long[] suara = new long[2];
    // private LinkedHashMap<String, Node> subWil = new LinkedHashMap<String, Node>();
    int pemenang;
    double[] persen = new double[2];


    Node(Node superWil, String namaWilayah){
        this.superWil = superWil;
        this.namaWilayah = namaWilayah;
    }

    Node(String namaWilayah){
        this.namaWilayah = namaWilayah;
    }

    public void addSuara(int calon,long suaraTmb){
        
        this.suara[calon] += suaraTmb;
    }

    public void subSuara(int calon, long suaraKrg){
        if(this.suara[calon] < suaraKrg){
            this.suara[calon] = 0;
        } else {
            this.suara[calon] -= suaraKrg;
        }
    }

    // public void addSubWil(Node sub){
    //     this.subWil.put(sub.getNama(), sub);
    // }

    // public String getNama(){
    //     return this.namaWilayah;
    // }

    // public String toString(){
    //     if(superWil!=null){
    //         return superWil.getNama() + " > " + this.namaWilayah + " suara : " + this.cekSuara();
    //     } else {
    //         return this.namaWilayah + " suara: " +this.cekSuara();
    //     }
    // }

    public Node getSuperWil(){
        return this.superWil;
    }
    
    public String cekSuara(){
        return this.suara[0] + " " + this.suara[1];
    }

    public long getSuara1(){
        return this.suara[0];
    }

    public long getSuara2(){
        return this.suara[1];
    }

    public void setPemenang(){
        if(this.suara[0]>this.suara[1]){
            this.pemenang = 0;
        } else if(this.suara[0]<this.suara[1]){
            this.pemenang = 1;
        } else {
            this.pemenang = -1;
        }
    }

    public int getPemenang(){
        return this.pemenang;
    }

    public void setPersentase() {
        long total = this.getSuara1() + this.getSuara2();
        if(this.getSuara1() == this.getSuara2() || total == 0){
            this.persen[0] = 50.0;
            this.persen[1] = 50.0;
        } else {
            this.persen[0] = Math.floor(this.getSuara1()*100 / total);
            this.persen[1] = Math.floor(this.getSuara2()*100 / total);
        }
        
    }

    public int getPersen1(){
        return (int) this.persen[0];
    }

    public int getPersen2(){
        return (int) this.persen[1];
    }

    public long getSelisih(){
        return Math.abs(this.getSuara1()-this.getSuara2());
    }

}