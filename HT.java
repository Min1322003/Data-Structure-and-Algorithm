package csc365;

import java.io.*;
class HT implements Serializable {
    static final class Node implements Serializable{
        Object key;
        Node next;
        double value;
        // Object value;
        Node(Object k, Node n) { key = k; value = 1; next = n; }
        Node(Object k,double value, Node n) { key = k; this.value = value; next = n; }
    }

    Node[] table = new Node[2048]; // always a power of 2
    int size = 0;
    private static final long serialVersionUID = 2L;
    boolean contains(Object key) {
        int h = key.hashCode();
        int i = h & (table.length - 1);
        for (Node e = table[i]; e != null; e = e.next) {
            if (key.equals(e.key))
                return true;
        }
        return false;
    }
    double getValue(Object key) {
        int h = key.hashCode();
        int i = h & (table.length - 1);
        for (Node e = table[i]; e != null; e = e.next) {
            if (key.equals(e.key))
                return e.value;
        }
        return 0.0;
    }

    void add(Object key) {
        int h = key.hashCode();
        int i = h & (table.length - 1);
        for (Node e = table[i]; e != null; e = e.next) {
            if (key.equals(e.key)){
                e.value += 1;
                return;
            }
        }
        table[i] = new Node(key, table[i]);
        ++size;
        if ((float)size/table.length >= 0.8f)
            resize();
    }
    void put(Object key, double val){
        int h = key.hashCode();
        int i = h & (table.length - 1);
        table[i] = new Node(key,val, table[i]);
        ++size;
        if ((float)size/table.length >= 0.8f)
            resize();
    }


    void resize() {
        Node[] oldTable = table;
        int oldCapacity = oldTable.length;
        int newCapacity = oldCapacity << 1;
        Node[] newTable = new Node[newCapacity];
        for (int i = 0; i < oldCapacity; ++i) {
            for (Node e = oldTable[i]; e != null; e = e.next) {
                int h = e.key.hashCode();
                int j = h & (newTable.length - 1);
                newTable[j] = new Node(e.key, newTable[j]);
            }
        }
        table = newTable;
    }
    void resizeS2() {
        Node[] newTable = new Node[table.length/2];
        table = newTable;
    }

    void resizeV2() { // avoids unnecessary creation
        Node[] oldTable = table;
        int oldCapacity = oldTable.length;
        int newCapacity = oldCapacity << 1;
        Node[] newTable = new Node[newCapacity];
        for (int i = 0; i < oldCapacity; ++i) {
            Node e = oldTable[i];
            while (e != null) {
                Node next = e.next;
                int h = e.key.hashCode();
                int j = h & (newTable.length - 1);
                e.next = newTable[j];
                newTable[j] = e;
                e = next;
            }
        }
        table = newTable;
    }

    void remove(Object key) {
        int h = key.hashCode();
        int i = h & (table.length - 1);
        Node e = table[i], p = null;
        while (e != null) {
            if (key.equals(e.key)) {
                if (p == null)
                    table[i] = e.next;
                else
                    p.next = e.next;
                break;
            }
            p = e;
            e = e.next;
        }
    }

    void printAll() {
        for (int i = 0; i < table.length; ++i)
            for (Node e = table[i]; e != null; e = e.next)
                System.out.printf("["+e.key+" , "+e.value+"], ");

        System.out.println();
    }
    public String toString() {
        String toS = "";
        for (int i = 0; i < table.length; ++i){
            for (Node e = table[i]; e != null; e = e.next){
                toS += "["+e.key+" , "+e.value+"], ";
                System.out.printf("["+e.key+" , "+e.value+"], ");
            }
        }
        return toS;
    }

    private void writeObject(ObjectOutputStream s) throws Exception {
        s.defaultWriteObject();
        s.writeInt(size);
        for (int i = 0; i < table.length; ++i) {
            for (Node e = table[i]; e != null; e = e.next) {
                s.writeObject(e.key);
            }
        }
    }
    private void readObject(ObjectInputStream s) throws Exception {
        s.defaultReadObject();
        Object m;
        int n = s.readInt();
//        System.out.println(n);
        for (int i = 0; i < n; ++i){
            if((m=s.readObject())!=null){
                add(m);
//            System.out.println(i);
            }
        }
    }
}

