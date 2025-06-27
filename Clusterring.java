package csc365;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.*;

public class Clusterring {

    public static void main(String[] args) throws Exception {
//        storing();
        hashing();
//        test();
    }

    private static void test() throws IOException,ClassNotFoundException{
        String currentDir = System.getProperty("user.dir");
        String FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + "map";
        File F = new File(FilePath);
        FileInputStream fin = new FileInputStream(F);
        ObjectInputStream in = new ObjectInputStream(fin);
        HT names =(HT) in.readObject();
        in.close();
        fin.close();
        for(int i =0 ; i < names.table.length ; i++) {
            for (HT.Node e = names.table[i]; e != null; e = e.next) {
                FilePath =currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator +i+File.separator+ e.key;
                File F0 = new File(FilePath);
                FileInputStream fin0 = new FileInputStream(F0);
                ObjectInputStream in0 = new ObjectInputStream(fin0);
                Review rv = (Review) in0.readObject();
                in0.close();
                fin0.close();
                System.out.println(rv.getCate()+"   "+rv.getGroup());
            }
        }
    }

    private static void hashing() throws IOException,ClassNotFoundException {
        String currentDir = System.getProperty("user.dir");
        String FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "raw";
        File folder = new File(FilePath);
        File[] files = folder.listFiles();

        int count = 0;
        String line;
        String line2;

        HT ctgIDF = new HT();
        HT textIDF= new HT();
        HT[] ctgs = new HT[10000];
        HT[] texts= new HT[10000];
        HT names = new HT();

        for(File F : files) {
            FileInputStream fin = new FileInputStream(F);
            ObjectInputStream in = new ObjectInputStream(fin);
            Review rv =(Review) in.readObject();
            line = rv.getText();
            int wordCount = 0;
            if (!line.trim().equals("")) {
                String[] words = line.split(" ");
                HT frequency = new HT();
                for (String word : words) {
                    if (!word.trim().equals("")) {
                        String processed = word.toLowerCase();
                        if (!frequency.contains(processed)) {
                            textIDF.add(processed);
                        }
                        frequency.add(processed);
                        wordCount++;
//                        System.out.println(frequency.toString());
                    }
                }
                HT TF = new HT();
                TF.resizeS2();

//                    System.out.println(frequency.toString());
                for (int i = 0; i < frequency.table.length; i++) {
                    for (HT.Node e = frequency.table[i]; e != null; e = e.next) {
                        double val = e.value / wordCount;
                        TF.put(e.key, val);
                    }
                }
//                TF.printAll();
                texts[count] = TF;
            }
            HT ctgTF = new HT();
            line2 = rv.getCate();
            line2 = line2.replaceAll("[^a-zA-Z',]", "");
//                System.out.println("Processing line: " + line2);
            if (!line2.trim().equals("")) {
                String[] words2 = line2.split(",");
                for (String word : words2) {

                    if (!word.trim().equals("")) {
                        String processed = word.toLowerCase();
//                    System.out.println("ppp:  "+processed);
                        if (!ctgTF.contains(processed)) {
                            ctgIDF.add(processed);
                        }
                        ctgTF.add(processed);
                    }
                }
                ctgs[count] = ctgTF;
            }
            count++;
            in.close();
        }



        count = 0 ;
        int ind;
        double[] numbers;

        for(File F: files){
            FileInputStream fin = new FileInputStream(F);
            ObjectInputStream in2 = new ObjectInputStream(fin);
            Review review = (Review) in2.readObject();

            numbers = new double[textIDF.size];
            ind = 0;
            for (int i = 0; i < textIDF.table.length; i++) {
                for (HT.Node e = textIDF.table[i]; e != null; e = e.next) {
                    if (e.value == 1) {
                        textIDF.remove(e.key);
                    } else {
                        double TFIDF = (texts[count].getValue(e.key) * Math.log(10000 / e.value));
                        numbers[ind] = TFIDF;
                        ind++;
                    }
                }
            }

//            System.out.println(numbers);
            review.setTextNumbers(numbers);

            ind = 0;
            numbers = new double[ctgIDF.size];

            for (int i = 0; i < ctgIDF.table.length; i++) {
                for (HT.Node e = ctgIDF.table[i]; e != null; e = e.next) {
                    if (e.value == 1) {
                        ctgIDF.remove(e.key);
                    } else {
                        double TFIDF = (ctgs[count].getValue(e.key) * Math.log(10000 / e.value));
                        numbers[ind] = TFIDF;
                        ind++;
                    }
                }
            }
            review.setCtgNumbers(numbers);
            in2.close();
//     System.out.println(review.toString());
            int h = review.getbusinessID().hashCode();
            int i = h & (names.table.length - 1);
            names.put(review.getbusinessID(), i);
            System.out.println(names.size);

            String FilePath2 = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + i;
            File f2 = new File(FilePath2);
            f2.mkdirs();
            FilePath2 += File.separator + review.getbusinessID();
            f2 = new File(FilePath2);
            FileOutputStream fout2 = new FileOutputStream(f2);
            ObjectOutputStream out2 = new  ObjectOutputStream(fout2);
            review.setGroup(-1);
//            System.out.println(rv.toString() );
            out2.writeObject(review);
            out2.close();
            count++;
        }


        FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + "map";
        File file = new File(FilePath);
        FileOutputStream mfOut = new FileOutputStream(file);
        ObjectOutputStream mout = new ObjectOutputStream(mfOut);
//        System.out.println(names.size);
        mout.writeObject(names);
        mout.close();
        mfOut.close();
    }

    private static void Clustering() throws Exception {
        Review rv;

        String currentDir = System.getProperty("user.dir");
        String FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + "map";
        File F = new File(FilePath);
        FileInputStream fin = new FileInputStream(F);
        ObjectInputStream in = new ObjectInputStream(fin);
        HT names = (HT) in.readObject();
        in.close();
        fin.close();

//        for(int i =0 ; i < names.table.length ; i++) {
//            for (HT.Node e = names.table[i]; e != null; e = e.next) {
//                FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + i+ File.separator+ e.key;
//                f = new File(FilePath);
//                fin = new FileInputStream(f);
//                in = new ObjectInputStream(fin);
//                rv = (Review) in.readObject();
//                rv.printCtgNum();
//            }
//        }
        System.out.println(names.size + "   " + names.table.length);


        double[][] Centroids = new double[9][];
        HT[] groups = new HT[9];

        FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + "Clusters" + File.separator + "group8";
        F = new File(FilePath);
        fin = new FileInputStream(F);
        in = new ObjectInputStream(fin);
        groups[8] = (HT) in.readObject();
        in.close();
        fin.close();

        FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + "4778" + File.separator + "iIJDIIUVmcvFUZrgnj983g";
        F = new File(FilePath);
        fin = new FileInputStream(F);
        in = new ObjectInputStream(fin);
        rv = (Review) in.readObject();
        rv.printCtgNum();
        Centroids[0] = rv.getCtgNumbers();
        in.close();
        fin.close();
//        FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + "Clusters" + File.separator + "group0";
//        F = new File(FilePath);
//        fin = new FileInputStream(F);
//        in = new ObjectInputStream(fin);
        groups[0] = new HT();
//        groups[0] =(HT) in.readObject();
//        in.close();
//        fin.close();


        FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + "4777" + File.separator + "7czFDmcXhqzcqvUnenNg6w";
        F = new File(FilePath);
        fin = new FileInputStream(F);
        in = new ObjectInputStream(fin);
        rv = (Review) in.readObject();
        rv.printCtgNum();
        Centroids[1] = rv.getCtgNumbers();
        in.close();
        fin.close();
//        FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + "Clusters" + File.separator + "group1";
//        F = new File(FilePath);
//        fin = new FileInputStream(F);
//        in = new ObjectInputStream(fin);
        groups[1] = new HT();
//        groups[1] =(HT) in.readObject();
//        in.close();
//        fin.close();

        FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + "4773" + File.separator + "q4aAaxdN4wmUZoC6sKEwsw";
        F = new File(FilePath);
        fin = new FileInputStream(F);
        in = new ObjectInputStream(fin);
        rv = (Review) in.readObject();
        rv.printCtgNum();
        Centroids[2] = rv.getCtgNumbers();
        in.close();
        fin.close();
//        FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + "Clusters" + File.separator + "group2";
//        F = new File(FilePath);
//        fin = new FileInputStream(F);
//        in = new ObjectInputStream(fin);
        groups[2] = new HT();
//        groups[2] =(HT) in.readObject();
//        in.close();
//        fin.close();

        FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + "669" + File.separator + "JwjvyUXL5T3vxT4GU-5kRg";
        F = new File(FilePath);
        fin = new FileInputStream(F);
        in = new ObjectInputStream(fin);
        rv = (Review) in.readObject();
        rv.printCtgNum();
        Centroids[3] = rv.getCtgNumbers();
        in.close();
        fin.close();
//        FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + "Clusters" + File.separator + "group3";
//        F = new File(FilePath);
//        fin = new FileInputStream(F);
//        in = new ObjectInputStream(fin);
        groups[3] = new HT();
//        groups[3] =(HT) in.readObject();
//        in.close();
//        fin.close();

        FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + "4767" + File.separator + "xhE6yH67ZxYv02KFOS2BUw";
        F = new File(FilePath);
        fin = new FileInputStream(F);
        in = new ObjectInputStream(fin);
        rv = (Review) in.readObject();
        rv.printCtgNum();
        Centroids[4] = rv.getCtgNumbers();
        in.close();
        fin.close();
//        FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + "Clusters" + File.separator + "group4";
//        F = new File(FilePath);
//        fin = new FileInputStream(F);
//        in = new ObjectInputStream(fin);
        groups[4] = new HT();
//        groups[4] =(HT) in.readObject();
//        in.close();
//        fin.close();

        FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + "4760" + File.separator + "SLHmMe2b4NhMW1CSzRJsYA";
        F = new File(FilePath);
        fin = new FileInputStream(F);
        in = new ObjectInputStream(fin);
        rv = (Review) in.readObject();
        rv.printCtgNum();
        Centroids[5] = rv.getCtgNumbers();
        in.close();
        fin.close();
//        FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + "Clusters" + File.separator + "group5";
//        F = new File(FilePath);
//        fin = new FileInputStream(F);
//        in = new ObjectInputStream(fin);
        groups[5] = new HT();
//        groups[5] =(HT) in.readObject();
//        in.close();
//        fin.close();

        FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + "4758" + File.separator + "RxDSFJEAU7sZs3rN9EW0Pg";
        F = new File(FilePath);
        fin = new FileInputStream(F);
        in = new ObjectInputStream(fin);
        rv = (Review) in.readObject();
        rv.printCtgNum();
        Centroids[6] = rv.getCtgNumbers();
        in.close();
        fin.close();
//        FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + "Clusters" + File.separator + "group6";
//        F = new File(FilePath);
//        fin = new FileInputStream(F);
//        in = new ObjectInputStream(fin);
        groups[6] = new HT();
//        groups[6] =(HT) in.readObject();
//        in.close();
//        fin.close();

        FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + "3714" + File.separator + "n9x79K-6XfB9QCRa7aN1WA";
        F = new File(FilePath);
        fin = new FileInputStream(F);
        in = new ObjectInputStream(fin);
        rv = (Review) in.readObject();
        rv.printCtgNum();
        Centroids[7] = rv.getCtgNumbers();
        in.close();
        fin.close();
//        FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + "Clusters" + File.separator + "group7";
//        F = new File(FilePath);
//        fin = new FileInputStream(F);
//        in = new ObjectInputStream(fin);
        groups[7] = new HT();
//        groups[7] =(HT) in.readObject();
//        in.close();
//        fin.close();


        System.out.println("Centroids created !");
        int Index;
        double similarity;
        double calSimilarity;
        int changes = 1;
        int iteration = 0;
        for (int i = 0; i < names.table.length; i++) {
            for (HT.Node e = names.table[i]; e != null; e = e.next) {
                FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + i + File.separator + e.key;
                F = new File(FilePath);
                fin = new FileInputStream(F);
                in = new ObjectInputStream(fin);
                rv = (Review) in.readObject();
                in.close();
                fin.close();

                similarity = 0;
                Index = -1;
                for (int j = 0; j < 8; j++) {
//                    System.out.println(Centroids[j].toString()+"###"+j);
                     calSimilarity = compare(Centroids[j], rv.getCtgNumbers());
//                    System.out.println(similarities[j]);
                    if (calSimilarity > similarity) {
                        similarity = calSimilarity;
                        Index = j;
                    }
                }
                if (Index == -1) {
                    Index = 8;
                }
                int groupAssigned = rv.getGroup();

                if (Index != groupAssigned) {
                    if (groupAssigned != -1) {groups[groupAssigned].remove(rv.getbusinessID());}
                    rv.setGroup(Index);
                    groups[Index].add(rv.getbusinessID());
                    File file = new File(FilePath);
                    FileOutputStream fout = new FileOutputStream(file);
                    ObjectOutputStream out = new ObjectOutputStream(fout);
                    out.writeObject(rv);
                    out.close();
                    fout.close();
                    changes++;
                }
            }
        }
        iteration++;
        System.out.println("Iteration :" + iteration);

        while (changes != 0) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < groups[i].table.length; j++) {
                    for (HT.Node e = groups[i].table[i]; e != null; e = e.next) {
                        FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + i + File.separator + e.key;
                        F = new File(FilePath);
                        fin = new FileInputStream(F);
                        in = new ObjectInputStream(fin);
                        rv = (Review) in.readObject();
                        in.close();
                        fin.close();
                        Centroids[i] = add(Centroids[i], rv.getCtgNumbers());
                    }
                }
                System.out.println(groups[i].size);
//                Centroids[i] = divide(Centroids[i], ((groups[i].size+1)/2000));
                for(int h=0 ; h< Centroids[i].length;h++){
                    System.out.printf(Centroids[i][h]+",,");
                }
                System.out.println();
            }
            System.out.println(groups[8].size);
            System.out.println("New Centriods created!!!");
            changes = 0;
            System.out.println(names.size);
            for (int i = 0; i < names.table.length; i++) {
                for (HT.Node e = names.table[i]; e != null; e = e.next) {
                    FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + i + File.separator + e.key;
                    F = new File(FilePath);
                    fin = new FileInputStream(F);
                    in = new ObjectInputStream(fin);
                    rv = (Review) in.readObject();
                    in.close();
                    fin.close();
                    similarity = 0;
                    Index = -1;
                    for (int j = 0; j < 8; j++) {
                        calSimilarity = compare(Centroids[j], rv.getCtgNumbers());
//                    System.out.println(similarities[j]);
                        if (calSimilarity > similarity) {
                            similarity = calSimilarity;
                            Index = j;
                        }
                    }
                    if (Index == -1) {
                        Index = 8;
                    }
                    int groupAssigned = rv.getGroup();
                    if (Index != groupAssigned) {
                        if (groupAssigned != -1) {groups[groupAssigned].remove(rv.getbusinessID());}
                        rv.setGroup(Index);
                        groups[Index].add(rv.getbusinessID());
                        File file = new File(FilePath);
                        FileOutputStream fout = new FileOutputStream(file);
                        ObjectOutputStream out = new ObjectOutputStream(fout);
                        out.writeObject(rv);
                        out.close();
                        fout.close();
                        changes++;
                    }
                }
            }
            iteration++;
            System.out.println("Iteration :" + iteration);

        }
        System.out.println("Grouping completed !!");
        for (int i = 0; i < 9; i++) {
            FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + "Clusters" + File.separator + "group" + i;
            File file = new File(FilePath);
            FileOutputStream fout = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fout);
            out.writeObject(groups[i]);
            out.close();
            fout.close();
        }
    }


    private static void storing() throws IOException {

        String line;
        String currentDir = System.getProperty("user.dir");
        String Data = "yelp_academic_dataset_review";
        String FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + Data + ".json";


        String Data2 = "yelp_academic_dataset_business";
        String FilePath2 = currentDir +File.separator+ "src" + File.separator + "csc365" +File.separator+ Data2 + ".json";
        BufferedReader reader2 = new BufferedReader(new FileReader(FilePath2));
        System.out.println("Business file read");

        Gson gson = new Gson();
        int reviewsToRead = 10000;
        String[] IDs = new String[reviewsToRead];
        String[] names = new String[reviewsToRead];
        String[] categories = new String[reviewsToRead];
        JsonObject jsonObject;
        int count = 0;

        while ((line = reader2.readLine()) != null && count<reviewsToRead) {
            jsonObject = gson.fromJson(line, JsonObject.class);
            String businessId = jsonObject.get("business_id").getAsString();
            String name = jsonObject.get("name").getAsString();
            String category = String.valueOf(jsonObject.get("categories"));
//            System.out.println(category);
            categories[count] =category;
            IDs[count] = businessId;
            names[count]= name;
            System.out.println("ID:" + businessId + ", Name:" + name + ", catagory:" + category );
            count++;
        }


        for(int i = 0 ; i < reviewsToRead ; i++) {
            BufferedReader reader = new BufferedReader(new FileReader(FilePath));
            while ((line = reader.readLine()) != null) {
                jsonObject = gson.fromJson(line, JsonObject.class);
                String text = jsonObject.get("text").getAsString();
                String id2 = jsonObject.get("business_id").getAsString();
                if(IDs[i].equalsIgnoreCase(id2)){
                    FilePath2 = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "raw" + File.separator + IDs[i];
                    File F = new File(FilePath2);
                    FileOutputStream Fout = new FileOutputStream(F);
                    ObjectOutputStream mout = new ObjectOutputStream(Fout);
                    mout.writeObject(new Review(IDs[i],names[i],text,categories[i],1,1,-1));
                    System.out.println(i);
                    mout.close();
                    Fout.close();
                    break;
                }
            }
        }


    }
    private static double compare(double[] number1, double[] number2){
        double dotProduct = 0.0;
        double magnitude1 = 0.0;
        double magnitude2 = 0.0;
        // Calculate the dot product and magnitudes
        if (number1.length == number2.length) {
            for (int i = 0; i < number1.length; i++) {
                dotProduct += (number1[i] * number2[i]);
                magnitude1 += Math.pow(number1[i], 2);
                magnitude2 += Math.pow(number2[i], 2);

            }
        } else {
            System.out.println("error in TFIDF");
        }
        // Calculate the cosine similarity
        if (magnitude1 != 0 && magnitude2 != 0) {
//            System.out.println(dotProduct / (Math.sqrt(magnitude1) * Math.sqrt(magnitude2)));
            return dotProduct / (Math.sqrt(magnitude1) * Math.sqrt(magnitude2));
        }return 0;
    }
    private static double[] add(double[] number1, double[] number2){
        double[] newNumbers = number1;
        for (int i =0 ;i < number1.length ; i++){
            newNumbers[i] = number1[i]+number2[i];
        }
        return newNumbers;
    }
}