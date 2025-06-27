package csc365;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;


public class Assignment3 {
    private static JFrame frame;
    private static JPanel panel;
    private static JButton button;
    private static JList<String> businessList;
    private static JList<String> businessList2;
    public static final double R = 6371; // Radius of the Earth in kilometers


    public static void main(String[] args) throws IOException, ClassNotFoundException {
//        storing();
//        hashing();
//        graph();
//        resetGroup();
//        group();
        GUI();
    }

    private static void GUI()  throws IOException, ClassNotFoundException {

        frame = new JFrame();
        panel = new JPanel();
        button = new JButton("Search");
        button.setPreferredSize(new Dimension(100, 30));

        String currentDir = System.getProperty("user.dir");
        String FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator+ "data"+ File.separator + "map";
        File f = new File(FilePath);
        FileInputStream fin = new FileInputStream(f);
        ObjectInputStream in = new ObjectInputStream(fin);
        HT names =(HT) in.readObject();
        in.close();
        String [] businessNames = new String[100];
        int count = 0;
        for(int i =0 ; i < names.table.length ; i++) {
            for (HT.Node e = names.table[i]; e != null; e = e.next) {
                FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + i+ File.separator + e.key;
                f = new File(FilePath);
                fin = new FileInputStream(f);
                in = new ObjectInputStream(fin);
                Review rv = (Review) in.readObject();
                if(rv.getGroup() ==7){
                businessNames[count] = rv.getName();count++;}

            }
        }

    JLabel label = new JLabel("Choose 2 the business name !");
    label.setFont(new Font("Arial", Font.BOLD, 16));

    panel.setLayout(new BorderLayout());
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    panel.add(label, BorderLayout.NORTH);

    JPanel listPanel = new JPanel(new GridLayout(0, 2, 10, 10)); // GridLayout with 2 columns and spacing
    listPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding to the list panel

    businessList = new JList<>(businessNames);
    businessList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    businessList.setVisibleRowCount(100);
    businessList.setPreferredSize(new Dimension(200, 200)); // Set preferred size

    businessList2 = new JList<>(businessNames);
    businessList2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    businessList2.setVisibleRowCount(100);
    businessList2.setPreferredSize(new Dimension(200, 200)); // Set preferred size

    listPanel.add(new JScrollPane(businessList));
    listPanel.add(new JScrollPane(businessList2));

    panel.add(listPanel, BorderLayout.CENTER);

    panel.add(button, BorderLayout.SOUTH);

    frame.add(panel);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setTitle("Business Recommendation");
    frame.pack();
    frame.setLocationRelativeTo(null); // Center the frame on the screen
    frame.setVisible(true);

    button.addActionListener(e -> {
        String selectedBusiness = businessList.getSelectedValue();
        String selectedBusiness2 = businessList2.getSelectedValue();
        if (selectedBusiness != null && selectedBusiness2 != null) {
            try {
                search(selectedBusiness, selectedBusiness2, label);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            label.setText("Please select a business from the list.");
        }
    });


    }

    private static void search(String selectedBusiness, String selectedBusiness2 , JLabel label) throws IOException, ClassNotFoundException{
        String currentDir = System.getProperty("user.dir");
        String FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + "map";
        File file = new File(FilePath);
        FileInputStream fmin = new FileInputStream(file);
        ObjectInputStream min = new ObjectInputStream(fmin);
        HT map = (HT) min.readObject();
        fmin.close();
        min.close();

        Review rv1 = null;
        Review rv2 = null;
        for (int i = 0; i < map.table.length; i++) {
            for (HT.Node e = map.table[i]; e != null; e = e.next) {
                FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + i + File.separator+ e.key;
                file = new File(FilePath);
                fmin = new FileInputStream(file);
                min = new ObjectInputStream(fmin);
                Review rv = (Review) min.readObject();
                min.close();
                fmin.close();
                if(rv.getName().equalsIgnoreCase(selectedBusiness))rv1=rv;
                else if (rv.getName().equalsIgnoreCase(selectedBusiness2)) {
                    rv2 = rv;
                }
            }
        }
        if(rv1.getGroup() == rv2.getGroup()){
            HT path = dijkstra(rv1,rv2);
        if (path != null) {
            updatePathList(businessList, businessList2, path);




            // Update the label
            label.setText("Path Taken below.(Directly connected if empty)");
        }
        } else {
            label.setText("businisses are from two disjointed set !!! Select another");
        }
    }
    private static void updatePathList(JList<String> pathList,JList<String> pathList2, HT path) throws IOException, ClassNotFoundException {
        DefaultListModel<String> pathListModel = new DefaultListModel<>();
        DefaultListModel<String> pathListModel2 = new DefaultListModel<>();
        int count = 0;
        for (int i = 0; i < path.table.length; i++) {
            for (HT.Node e = path.table[i]; e != null; e = e.next) {
                String currentDir = System.getProperty("user.dir");
                String FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + i + File.separator+ e.key;
                File file = new File(FilePath);
                FileInputStream fmin = new FileInputStream(file);
                ObjectInputStream min = new ObjectInputStream(fmin);
                Review rv = (Review) min.readObject();
                min.close();
                fmin.close();
                pathListModel.add(count,rv.getName());
                pathListModel2.add(count,(String.valueOf(e.value)+" km"));
                count++;
            }
        }
        pathList.setModel(pathListModel);
        pathList2.setModel(pathListModel2);
    }
    private static ArrayList<String> getUnusedNodes(HT path, HT map) {
        ArrayList<String> unusedNodes = new ArrayList<>();
        // Iterate over all nodes in the map and add those not present in the path to unusedNodes
        for (int i = 0; i < map.table.length; i++) {
            for (HT.Node e = map.table[i]; e != null; e = e.next) {
                boolean found = false;
                for (int k = 0; k < map.table.length; k++) {
                    for (HT.Node node = map.table[i]; node != null; node = node.next) {
                    if (node.key.equals(e.key)) {
                        found = true;

                    }
                    }
                    if(found==true)break;
                }
                if (!found) {
                    unusedNodes.add(e.key.toString()); // Assuming key is the node name
                }
            }
        }
        return unusedNodes;
    }
    private static void updateUnusedNodesList(JList<String> unusedNodesList, ArrayList<String> unusedNodes) {
        DefaultListModel<String> unusedNodesListModel = new DefaultListModel<>();
        for (String node : unusedNodes) {
            unusedNodesListModel.addElement(node);
        }
        unusedNodesList.setModel(unusedNodesListModel);
    }

    private static void resetGroup() throws IOException, ClassNotFoundException{
        String currentDir = System.getProperty("user.dir");
        String FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + "map";
        File file = new File(FilePath);
        FileInputStream fmin = new FileInputStream(file);
        ObjectInputStream min = new ObjectInputStream(fmin);
        HT map = (HT) min.readObject();
        fmin.close();
        min.close();
        for (int i = 0; i < map.table.length; i++) {
            for (HT.Node e = map.table[i]; e != null; e = e.next) {
                FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + i + File.separator+ e.key;
                file = new File(FilePath);
                fmin = new FileInputStream(file);
                min = new ObjectInputStream(fmin);
                Review rv = (Review) min.readObject();
                min.close();
                fmin.close();

                rv.setGroup(-1);
                rv.setWeight(99999);

                FileOutputStream fout = new FileOutputStream(file);
                ObjectOutputStream out= new ObjectOutputStream(fout);
                out.writeObject(rv);
                out.close();
                fout.close();
            }
        }
    }

    private static HT dijkstra (Review rv , Review rv2) throws IOException, ClassNotFoundException {
        HT path =  new HT();

        if(rv.getGroup() != rv2.getGroup()){
            System.out.println("businisses are from two disjointed set !!!");
            return path;
        }

        HashMap<String,Double> nei = rv.getNeighbors();

        HashMap<String, Double> distances = new HashMap<>();
        HashMap<String, String> prev = new HashMap<>();
        for (String id : nei.keySet()) {
            distances.put(id, Double.POSITIVE_INFINITY);
            prev.put(id, null);
        }
        distances.put(rv.getbusinessID(), 0.0);

        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingDouble(distances::get));
        pq.add(rv.getbusinessID());

        while (!pq.isEmpty()) {
            String current = pq.poll();
            double distToCurrent = distances.get(current);
            if (current.equals(rv2.getbusinessID())) {
                // Reached the destination, construct the path
                String node = current;
                while (node != null) {
                    path.put(node, distances.get(node));
                    node = prev.get(node);
                }
                return path;
            }


            for (String neighbor : nei.keySet()) {
                double weight = nei.get(neighbor);
                double newDist = distToCurrent + weight;
                if (newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    prev.put(neighbor, current);
                    pq.add(neighbor); // Add to the priority queue for exploration
                }
            }
        }
        // If destination not reached, return an empty path (destination not reachable)
        System.out.println("Destination not reachable from the source.");
        return path;
    }

    private static void group() throws IOException,ClassNotFoundException{
        String currentDir = System.getProperty("user.dir");
        String FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + "map";
        File file = new File(FilePath);
        FileInputStream fmin = new FileInputStream(file);
        ObjectInputStream min = new ObjectInputStream(fmin);
        HT map = (HT) min.readObject();
        fmin.close();
        min.close();
        int group = 1;
        int count = 0;
        boolean checked;
        while(count < 1000){
        for (int i = 0; i < map.table.length; i++) {
            for (HT.Node e = map.table[i]; e != null; e = e.next) {
                FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + i + File.separator+ e.key;
                file = new File(FilePath);
                fmin = new FileInputStream(file);
                min = new ObjectInputStream(fmin);
                Review rv = (Review) min.readObject();
                min.close();
                fmin.close();

                if(rv.getGroup() == -1){
                    rv.setGroup(group);
                    count++;
                    FileOutputStream fout = new FileOutputStream(file);
                    ObjectOutputStream out= new ObjectOutputStream(fout);
                    out.writeObject(rv);
                    out.close();
                    fout.close();
                    String root = rv.getbusinessID();
                    checked = true;
                    for (String id : rv.neighbors.keySet()) {
                        int h = id.hashCode();
                        int k = h & (2048 - 1);
                        currentDir = System.getProperty("user.dir");
                        FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + k + File.separator + id;
                        file = new File(FilePath);
                        fmin = new FileInputStream(file);
                        min = new ObjectInputStream(fmin);
                        Review rv2 = (Review) min.readObject();
                        min.close();
                        fmin.close();
                        if (rv2.getGroup() == -1) {
                            rv2.setGroup(group);
                            rv2.setPrevNode(rv.getbusinessID());
                            count++;
                            fout = new FileOutputStream(file);
                            out = new ObjectOutputStream(fout);
                            out.writeObject(rv2);
                            out.close();
                            fout.close();
                            rv = rv2;
                            checked = false;
                        } else if (rv2.getGroup() != rv.getGroup()) {
                            System.out.println("hey");
                        }
                    }
                    if (checked == true) {
                        if (!(rv.getbusinessID().equalsIgnoreCase(root))) {
                            int h = rv.getPrevNode().hashCode();
                            int k = h & (2048 - 1);
                            currentDir = System.getProperty("user.dir");
                            FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + k + File.separator + rv.getPrevNode();
                            file = new File(FilePath);
                            fmin = new FileInputStream(file);
                            min = new ObjectInputStream(fmin);
                            rv = (Review) min.readObject();
                            min.close();
                            fmin.close();
                        }
                    }
                    while (!(rv.getbusinessID().equalsIgnoreCase(root))) {
                        checked = true;
                        for (String id : rv.neighbors.keySet()) {
                            int h = id.hashCode();
                            int k = h & (2048 - 1);
                            currentDir = System.getProperty("user.dir");
                            FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + k + File.separator + id;
                            file = new File(FilePath);
                            fmin = new FileInputStream(file);
                            min = new ObjectInputStream(fmin);
                            Review rv2 = (Review) min.readObject();
                            min.close();
                            fmin.close();
                            if (rv2.getGroup() == -1) {
                                rv2.setGroup(group);
                                rv2.setPrevNode(rv.getbusinessID());
                                count++;
                                fout = new FileOutputStream(file);
                                out = new ObjectOutputStream(fout);
                                out.writeObject(rv2);
                                out.close();
                                fout.close();
                                rv = rv2;
                                checked = false;
                            }
                        }
                        if (checked == true) {
                            if (!(rv.getbusinessID().equalsIgnoreCase(root))) {
                                int h = rv.getPrevNode().hashCode();
                                int k = h & (2048 - 1);
                                currentDir = System.getProperty("user.dir");
                                FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + k + File.separator + rv.getPrevNode();
                                file = new File(FilePath);
                                fmin = new FileInputStream(file);
                                min = new ObjectInputStream(fmin);
                                rv = (Review) min.readObject();
                                min.close();
                                fmin.close();
                            }
                        }
                    }
                    System.out.println(count + " bussinesses are grouped after "+group);
                group++;
                }
            }
        }

        }
    }


    private static void graph() throws IOException, ClassNotFoundException {
        String currentDir = System.getProperty("user.dir");
        String FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + "map";
        File file = new File(FilePath);
        FileInputStream fmin = new FileInputStream(file);
        ObjectInputStream min = new ObjectInputStream(fmin);
        HT map = (HT) min.readObject();
        fmin.close();
        min.close();

        String[] neighbors = new String[4];
        double[] distances ;
        double tempD;
        String tempN;

        for (int i = 0; i < map.table.length; i++) {
            for (HT.Node e = map.table[i]; e != null; e = e.next) {
                FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + i + File.separator+ e.key;
                file = new File(FilePath);
                fmin = new FileInputStream(file);
                min = new ObjectInputStream(fmin);
                Review rv = (Review) min.readObject();
                min.close();
                fmin.close();
                distances = new double[]{99999, 99999, 99999, 99999};
                for (int j = 0; j < map.table.length; j++) {
                    for (HT.Node k = map.table[j]; k != null; k = k.next) {
                        FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + j + File.separator+ k.key;
                        file = new File(FilePath);
                        fmin = new FileInputStream(file);
                        min = new ObjectInputStream(fmin);
                        Review rv2 = (Review) min.readObject();
                        min.close();
                        fmin.close();
                        if(!(rv.getbusinessID().equalsIgnoreCase(rv2.getbusinessID()))){
                        tempD = haversine(rv.getLatitude(),rv.getLongitude(),rv2.getLatitude(),rv2.getLongitude());
//                        System.out.println(tempD);
                        if(distances[3]> tempD){
                            distances[3] = tempD;
                            neighbors[3] = rv2.getbusinessID();
                            if(distances[2]>distances[3]){
                                tempD = distances[2];
                                tempN = neighbors[2];
                                distances[2] = distances[3];
                                neighbors[2] = neighbors[3];
                                distances[3] = tempD;
                                neighbors[3] = tempN;
                                if(distances[1]>distances[2]){
                                    tempD = distances[1];
                                    tempN = neighbors[1];
                                    distances[1] = distances[2];
                                    neighbors[1] = neighbors[2];
                                    distances[2] = tempD;
                                    neighbors[2] = tempN;
                                    if(distances[0]>distances[1]){
                                        tempD = distances[0];
                                        tempN = neighbors[0];
                                        distances[0] = distances[1];
                                        neighbors[0] = neighbors[1];
                                        distances[1] = tempD;
                                        neighbors[1] = tempN;
                                    }
                                }
                            }
                        }
                    }}
                }
                HashMap<String,Double> nei = new HashMap<>(4);
                for(int l = 0 ; l <4 ; l++){
                nei.put(neighbors[l],distances[l]);
                    System.out.println(distances[l]);
                }
                rv.setNeighbors(nei);
                FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + i + File.separator+ e.key;
                file = new File(FilePath);
                FileOutputStream fout = new FileOutputStream(FilePath);
                ObjectOutputStream out= new ObjectOutputStream(fout);
                out.writeObject(rv);
                System.out.println(rv.toString()+" neighbors: "+ nei.toString());
                out.close();
                fout.close();
            }
        }
    }

//    private static void cluster() {
//
//    }

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
        HT[] ctgs = new HT[1000];
        HT[] texts= new HT[1000];
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
        int reviewsToRead = 1000;
        String[] IDs = new String[reviewsToRead];
        String[] names = new String[reviewsToRead];
        String[] categories = new String[reviewsToRead];
        double[] lat = new double[reviewsToRead];
        double[] lon = new double[reviewsToRead];
        JsonObject jsonObject;
        int count = 0;

        while ((line = reader2.readLine()) != null && count<reviewsToRead) {
            jsonObject = gson.fromJson(line, JsonObject.class);
            String businessId = jsonObject.get("business_id").getAsString();
            String name = jsonObject.get("name").getAsString();
            String category = String.valueOf(jsonObject.get("categories"));
            double latitude = jsonObject.get("latitude").getAsDouble();
            double longitude = jsonObject.get("longitude").getAsDouble();
            IDs[count] = businessId;
            names[count]= name;
            categories[count] = category;
            lat[count] = latitude;
            lon[count] = longitude;
//            System.out.println(category);
            System.out.println("ID:" + businessId + ", Name:" + name + ", catagory:" + category );
            count++;
            for (int i = 0 ; i < 99  ; i++){
                reader2.readLine();
            }
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
                    mout.writeObject(new Review(IDs[i],names[i],text,categories[i],lat[i],lon[i],-1));
                    System.out.println(i);
                    mout.close();
                    Fout.close();
                    break;
                }
            }
        }
    }
    public static double haversine(double lat1, double lon1, double lat2, double lon2) {
        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);
        double dLat = (lat2 - lat1);
        double dLon = (lon2 - lon1);
        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;
        return distance;
    }
}
