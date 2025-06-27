//package csc365;
//
//import com.google.gson.Gson;
//import com.google.gson.JsonObject;
//
//import javax.swing.*;
//import java.io.*;
//import java.util.ArrayList;
//
//public class Clustering{
//    static HT textIDF = new HT();
//    static HT ctgIDF = new HT();
////    public static void main(String[] args) throws IOException, ClassNotFoundException {
////        Hashing();
////        Cluster();
////        test();
////    }
//
//    private static void test() throws IOException, ClassNotFoundException {
//        String currentDir = System.getProperty("user.dir");
//        String FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + "map";
//        File f = new File(FilePath);
//        FileInputStream fin = new FileInputStream(f);
//        ObjectInputStream in = new ObjectInputStream(fin);
//        HT hhh = new HT();
//        hhh = hhh.readObject(in);
//
//        System.out.println(hhh.size);
//        int count = 0 ;
//        for(int i = 0 ; i < hhh.table.length ; i++){
//            for(HT.Node e = hhh.table[i]; e!=null; e=e.next){
//                System.out.println(e.key+"..."+e.value+"!!!"+count);
//                FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + e.key+File.separator+e.value;
//                File fs = new File(FilePath);
//                FileInputStream fins = new FileInputStream(fs);
//                ObjectInputStream ins = new ObjectInputStream(fins);
//                Review rv = (Review) ins.readObject();
//                count++;
//            }
//        }
//        System.out.println(count);
//    }
//
//    private static void Cluster() throws IOException, ClassNotFoundException {
//        Review rv;
//
//        String currentDir = System.getProperty("user.dir");
//        String FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + "map";
//        File f = new File(FilePath);
//        FileInputStream fin = new FileInputStream(f);
//        ObjectInputStream in = new ObjectInputStream(fin);
//        HT names = (HT) in.readObject();
//        System.out.println(names.size);
//
//        double[][] Centroids = new double[5][];
//
//        FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + "936"+ File.separator+ "ZpXZgnBrNodHH8pKpOK_Kg";
//        f = new File(FilePath);
//        fin = new FileInputStream(f);
//        in = new ObjectInputStream(fin);
//        rv = (Review) in.readObject();
//        Centroids[0] = rv.getCtgNumbers();
//        HT name0 = new HT();
//
//        FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + "2672"+ File.separator+ "fsFZQ2ZbTutgUlnWPhTU6A";
//        f = new File(FilePath);
//        fin = new FileInputStream(f);
//        in = new ObjectInputStream(fin);
//        rv = (Review) in.readObject();
//        Centroids[1] = rv.getCtgNumbers();
//        HT name1 = new HT();
//
//        FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + "3251"+ File.separator+ "46_2e1hnQ4csLrYnhn6bkw";
//        f = new File(FilePath);
//        fin = new FileInputStream(f);
//        in = new ObjectInputStream(fin);
//        rv = (Review) in.readObject();
//        Centroids[2] = rv.getCtgNumbers();
//        HT name2 = new HT();
//
//        FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + "5169"+ File.separator+ "2cDrt_Xac_j15fdBVPfTKg";
//        f = new File(FilePath);
//        fin = new FileInputStream(f);
//        in = new ObjectInputStream(fin);
//        rv = (Review) in.readObject();
//        Centroids[3] = rv.getCtgNumbers();
//        HT name3 = new HT();
//
//        FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + "5400"+ File.separator+ "ab3pRv-b0o-BwMK2jVbH3Q";
//        f = new File(FilePath);
//        fin = new FileInputStream(f);
//        in = new ObjectInputStream(fin);
//        rv = (Review) in.readObject();
//        Centroids[4] = rv.getCtgNumbers();
//        HT name4 = new HT();
//
//        double similarity = 0 ;
//        for(int i =0 ; i < names.table.length ; i++){
//            for(HT.Node e = names.table[i]; e!= null ; e=e.next){
//                FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + (int)e.value + File.separator+ e.key;
//                f = new File(FilePath);
//                fin = new FileInputStream(f);
//                in = new ObjectInputStream(fin);
//                rv = (Review) in.readObject();
//                System.out.println(rv .toString());
//            }
//        }
//
////        names.printAll();
//
////      pick 7 random centriods
////      calculate 10000 rvs distance to 7 and assign to nearest arraylist
////
////      while(changes!=0)
////      take each of arraylist and find new centroids
////      foreach arraylist compare each of rv and check if we have to change list;
////      calculate 10000 rvs distance to 7 and assign to nearest arraylist
//
////
//}
//
//    static void Hashing() throws IOException {
//        String currentDir = System.getProperty("user.dir");
//        String line;
//        Gson gson = new Gson();
//        Review[] reviews = new Review[10000];
//        ArrayList<String> IDs = new ArrayList<>();
//        ArrayList<String> categories = new ArrayList<>();
//
//        String Data = "yelp_academic_dataset_review";
//        String FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + Data + ".json";
//        BufferedReader reader = new BufferedReader(new FileReader(FilePath));
//        System.out.println("Review file read");
//
//        String Data2 = "yelp_academic_dataset_business";
//        String FilePath2 = currentDir +File.separator+ "src" + File.separator + "csc365" +File.separator+ Data2 + ".json";
//        BufferedReader reader2 = new BufferedReader(new FileReader(FilePath2));
//        System.out.println("Business file read");
//
//        while ((line = reader2.readLine()) != null ) {
//            JsonObject jsonObject = gson.fromJson(line, JsonObject.class);
//            String businessId = jsonObject.get("business_id").getAsString();
//            String name = jsonObject.get("name").getAsString();
//            String category = String.valueOf(jsonObject.get("categories"));
////        System.out.println(category);
//            categories.add(category);
//            IDs.add(businessId);
//            IDs.add(name);
//        }
//
//
//        int count = 0;
//
//        while ((line = reader.readLine()) != null && count < 10000) {
//            Review review = gson.fromJson(line, Review.class);
//            String Rv;
//            Rv = review.getreview();
//            Rv = Rv.replaceAll("[^a-zA-Z']", " ");
//            review.setText(Rv);
//
//            for(int i=0; i<IDs.size();i+=2){
//                if (review.getbusinessID().equalsIgnoreCase(IDs.get(i))) {
//                    review.setName(IDs.get(i+1));
//                    review.setCate(categories.get(i / 2));
////                    System.out.println(review.getCate());
//                    break;
//                }
//            }
//
//            if (review.getName()==null){
//                review.setName(review.getbusinessID()+"(Name Unknown)");
//            }
//
//
//            HT ctgTF= new HT();
//            line = review.getreview();
//            String line2 = review.getCate();
//            line2 = line2.replaceAll("[^a-zA-Z',]", "");
////                System.out.println("Processing line: " + line2);
//            if(!line2.trim().equals("")){
//                String[] words2 = line2.split(",");
//                for (String word : words2) {
//
//                    if (!word.trim().equals("")) {
//                        String processed = word.toLowerCase();
////                    System.out.println("ppp:  "+processed);
//                        if (!ctgTF.contains(processed)) {
//                            ctgIDF.add(processed);
//                        }
//                        ctgTF.add(processed);
//                    }
//                }
//                review.setCtgTF(ctgTF);
//            }
//            int wordCount=0;
//            if(!line.trim().equals("")) {
//                String[] words = line.split(" ");
//                HT frequency = new HT();
//                for (String word : words) {
//                    if (!word.trim().equals("")) {
//                        String processed = word.toLowerCase();
//                        if (!frequency.contains(processed)) {
//                            textIDF.add(processed);
//                        }
//                        frequency.add(processed);
//                        wordCount++;
////                        System.out.println(frequency.toString());
//                    }
//                }
//                HT TF = new HT();
////                    System.out.println(frequency.toString());
//                for (int i = 0; i < frequency.table.length; i++) {
//                    for (HT.Node e = frequency.table[i]; e != null; e = e.next) {
//                        double val = e.value / wordCount;
//                        TF.put(e.key, val);
//                    }
//                }
////                TF.printAll();
//                review.setTF(TF);
//            }
//            reviews[count] = review;
//            count++;
//        }
//
//
//        System.out.println(reviews.length +"..."+count);
//
////        textIDF.printAll();
////        System.out.println();
////        System.out.println();
////        System.out.println();
////        System.out.println();
////        System.out.println();
//        ctgIDF.printAll();
//        HT naames = new HT();
//        count =0;
//        for(Review review: reviews) {
//            double[] numbers = new double[textIDF.size];
//            int ind = 0;
//            for (int i = 0; i < textIDF.table.length; i++) {
//                for (HT.Node e = textIDF.table[i]; e != null; e = e.next) {
//                    if (e.value == 1) {
//                        textIDF.remove(e.key);
//                    } else {
//                        double TFIDF = (review.getTF().getValue(e.key) * Math.log(10000 / e.value));
//                        numbers[ind] = TFIDF;
//                        ind++;
//                    }
//                }
//            }
//
////            System.out.println(numbers);
//            review.setTextNumbers(numbers);
//
//            ind = 0;
//            numbers = new double[ctgIDF.size];
//
//            for (int i = 0; i < ctgIDF.table.length; i++) {
//                for (HT.Node e = ctgIDF.table[i]; e != null; e = e.next) {
//                    if (e.value == 1) {
//                        ctgIDF.remove(e.key);
//                    } else {
//                        double TFIDF = (review.getCtgTF().getValue(e.key) * Math.log(5000 / e.value));
//                        numbers[ind] = TFIDF;
//                        ind++;
//                    }
//                }
//            }
//            review.setCtgNumbers(numbers);
//
////     System.out.println(review.toString());
//            int h = review.getbusinessID().hashCode();
//            int i = h & (naames.table.length - 1);
//            naames.put(review.getbusinessID(), i);
//            System.out.println(naames.size);
//
//            String FilePath3 = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + i;
//            File f = new File(FilePath3);
//            f.mkdirs();
//            FilePath3 += File.separator + review.getbusinessID();
//            f = new File(FilePath3);
//            FileOutputStream fOut = new FileOutputStream(f);
//            ObjectOutputStream out = new ObjectOutputStream(fOut);
//            Review rv = new Review(review.getbusinessID(), review.getName(), review.getTextNumbers(), review.getCtgNumbers());
////            rv.getTF().printAll();
////            System.out.println(rv.toString() );
//            out.writeObject(rv);
//            count++;
////            System.out.println(count);
//            out.close();
//        }
//        FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + "map";
//        File fm = new File(FilePath);
//        FileOutputStream mfOut = new FileOutputStream(fm);
//        ObjectOutputStream mout = new ObjectOutputStream(mfOut);
//        System.out.println(naames.size);
//        naames.writeObject(mout);
////        System.out.println(count);
//        mout.close();
//    }
//    private double compare(double[] number1, double[] number2 ){
//        double dotProduct = 0.0;
//        double magnitude1 = 0.0;
//        double magnitude2 = 0.0;
//        // Calculate the dot product and magnitudes
//        if (number1.length == number2.length) {
//            for (int i = 0; i < number1.length; i++) {
//                dotProduct += (number1[i] * number2[i]);
//                magnitude1 += Math.pow(number1[i], 2);
//                magnitude2 += Math.pow(number2[i], 2);
//
//            }
//        } else {
//            System.out.println("error in TFIDF");
//        }
//        // Calculate the cosine similarity
//        if (magnitude1 != 0 && magnitude2 != 0) {
//            return dotProduct / (Math.sqrt(magnitude1) * Math.sqrt(magnitude2));
//        }return 0;
//    }
//}