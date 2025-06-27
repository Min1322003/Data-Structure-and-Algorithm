package csc365;

import com.google.gson.*;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class Recomand{

        private JFrame frame;
        private JPanel panel;
        private JButton button;
        private JList<String> businessList;
        String[] businessNames;
        double[][] textNumbers;
        HT names;

        public Recomand() throws IOException, ClassNotFoundException {
            //GUI set up
            frame = new JFrame();
            panel = new JPanel();
            button = new JButton("Search");
            button.setPreferredSize(new Dimension(100, 30));
            Review rv;

            String currentDir = System.getProperty("user.dir");
            String FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator+ "data"+ File.separator + "Clusters" + File.separator + "group8";
            File f = new File(FilePath);
            FileInputStream fin = new FileInputStream(f);
            ObjectInputStream in = new ObjectInputStream(fin);
            names =(HT) in.readObject();
            in.close();
            businessNames = new String[names.size];
            textNumbers = new double[names.size][];

            int count =0;

            for(int i =0 ; i < names.table.length ; i++) {
                for (HT.Node e = names.table[i]; e != null; e = e.next) {
                    FilePath = currentDir + File.separator + "src" + File.separator + "csc365" + File.separator + "data" + File.separator + i+ File.separator + e.key;
                    f = new File(FilePath);
                    fin = new FileInputStream(f);
                    in = new ObjectInputStream(fin);
                    rv = (Review) in.readObject();
                    if(rv.getTextNumbers()!= null){
                    businessNames[count] = rv.getName();
                    textNumbers[count] = rv.getTextNumbers();
                    count++;
                    }
                }
            }


            JLabel label = new JLabel("Choose a the business name !");
            panel.setBorder(BorderFactory.createEmptyBorder(100, 300, 100, 300));
            panel.setLayout(new GridLayout(0, 1));
            panel.add(label);
            businessList = new JList<>(businessNames);
            businessList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            businessList.setVisibleRowCount(10);
            JScrollPane scrollPane = new JScrollPane(businessList);
            panel.add(scrollPane, BorderLayout.CENTER);

            panel.add(button, BorderLayout.SOUTH);

            frame.add(panel, BorderLayout.CENTER);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setTitle("Business Recomandation");
            frame.pack();
            frame.setVisible(true);

            button.addActionListener(e -> {
                String selectedBusiness = businessList.getSelectedValue();
                if (selectedBusiness != null) {
                    search(selectedBusiness, label);
                } else {
                    label.setText("Please select a business from the list.");
                }
            });
        }

    private void search(String name,JLabel label) {
        double[] numbersToCompare = null;
        String rv = null;

        for (int i = 0 ; i< names.size; i++) {
            if(businessNames[i].equalsIgnoreCase(name)){
                rv = businessNames[i];
                numbersToCompare = textNumbers[i];
                break;
            }
        }
        double max1 = 0;
        double max2 = 0;
        double temp = 0;
        double similarity;

        String rmax1 = null;
        String rmax2 = null;
        String rtemp = null;
        if(rv != null){
        for (int i = 0 ; i < names.size ; i++) {
            if (!rv.equalsIgnoreCase(businessNames[i])) {
                similarity = compare(numbersToCompare,textNumbers[i]);
                System.out.println(similarity+" # "+ max1 +" # "+ max2);
                if(similarity > max2){
                    max2 = similarity;
                    rmax2 = businessNames[i];
                }
                if (max2 > max1){
                    temp = max1;
                    max1 = max2;
                    max2 = temp;
                    rtemp = rmax1;
                    rmax1 = rmax2;
                    rmax2 = rtemp;
                }
            }
        }
        label.setText("Similar business "+name+" are: "+ rmax1 +" and "+ rmax2);
}else {
    System.out.println("Name not found!");
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

//
//

    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new Recomand();
                } catch (IOException | ClassNotFoundException e) {
                }
            }
        });
    }
}