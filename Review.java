package csc365;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

public class Review implements Serializable {
    private String business_id;
    private String name;
    private String text;
    private String catagories;
    private double latitude;
    private double longitude;
    private double[] textNumbers;
    private double[] ctgNumbers;
    private int group;
    private String prevNode;
    private double weight;
    HashMap<String,Double> neighbors;
    private static final long serialVersionUID = 1L;

    // Constructor

    public Review( String business_id,String name, String text, String catagories,double latitude, double longitude,int group) {

        this.business_id = business_id;
        this.name = name;
        this.text = text;
        this.catagories = catagories;
        this.latitude = latitude;
        this.longitude = longitude;
        this.group=group;
    }
        public Review( String business_id,String name, String text, String catagories) {
        this.business_id = business_id;
        this.name = name;
        this.text = text;
        this.catagories = catagories;
        this.group=-1;
    }

    // Getters and setters (optional)
    public String getbusinessID() {
        return business_id;
    }
    public String getName() {
        return name;
    }
    public String getText() {
        return text;
    }
    public String getCate() {
        return catagories;
    }
    public double[] getTextNumbers() {return textNumbers;}
    public double[] getCtgNumbers() {return ctgNumbers;}
    public double getLatitude(){return latitude;}
    public double getLongitude(){return longitude;}
    public String getPrevNode(){return prevNode;}
    public double getWeight(){return weight;}
    public HashMap<String, Double> getNeighbors(){return neighbors;}
    public int getGroup() {
        return group;
    }
    public void setGroup(int group){this.group = group;}
    public void setPrevNode(String prevNode){this.prevNode = prevNode;}
    public void setWeight(double weight){this.weight = weight;}
    public void setNeighbors(HashMap neighbors){this.neighbors =neighbors;}
    public void setTextNumbers(double[] textNumbers) {
        this.textNumbers = textNumbers;
    }
    public void setCtgNumbers(double[] ctgNumbers) {
        this.ctgNumbers = ctgNumbers;
    }
    public void getLatitude(double lat){latitude = lat;}
    public void getLongitude(double lon){longitude = lon;}

    public String toString() {
        return "Review{" +
                "business_ID='" + business_id + '\'' +
                ", business_name='" + name + '\'' +
                ", Categories : "+ catagories+ '\''+ ", Review :"+ text+
                '}';
    }
    public void printCtgNum(){
        System.out.printf("%s %s, %s{%n", name, business_id ,catagories);
        for(double number : ctgNumbers){
            System.out.printf(number+" , ");
        }
        System.out.println("}");
    }
    public void printTextNum(){
        System.out.printf("%s %s{%n", name, business_id);
        for(double number : textNumbers){
            System.out.printf(number+" , ");
        }
        System.out.println("}");
    }
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
    }
}

