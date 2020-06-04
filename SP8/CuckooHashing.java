/*
Class which implements Cuckoo hashing algorithm.
@author Savan Amitbhai Visalpara (sxv180069)
@author Srikumar Ramaswamy (sxr170016)
 */

package sxr170016;
import java.util.HashSet;
import java.util.Hashtable;

public class CuckooHashing<T> {
    public static final double LOAD_FACTOR = 0.9;
    Entry<T>[][] hashTables;                 //2 hash tables used for cuckoo hashing. second index denotes the table number.
    HashSet<T> secondaryHash;
    int tableSize;
    int capacity;
    int threshold;

    // constructor
    public CuckooHashing() {
        capacity = 64;
        threshold = (int)Math.log((capacity));
        hashTables = new Entry[capacity][2];
        secondaryHash = new HashSet<>();
        tableSize = 0;
    }

    /**
     * Entry Class
     */
    public static class Entry<T> {
        T element;
        public Entry(T val){
            this.element = val;
        }
    }

    /**
     *
     * @param obj - key for which hash value needs to be found.
     * @return the hash value.
     */
    public Integer hash1(T obj) {
        int h = obj.hashCode();
        h ^= (h >>> 20) ^ (h >>> 12);
        return (h ^ (h >>> 7) ^ (h >>> 4)) & (hashTables.length - 1);
    }

    /**
     *
     * @param obj - key for which hash value needs to be found.
     * @return the hash value.
     */
    public Integer hash2(T obj){
        return (hash1(obj) + (13 - (obj.hashCode() % 13))) & (hashTables.length - 1);
    }

    /**
     *
     * @param obj - key to be hashed.
     * @param i  - choose the 1st or 2nd hash function based on this value.
     * @return the hash value.
     */
    public Integer getHashVal(T obj, int i) {
        if(i == 0)
            return hash1(obj);
        else
            return hash2(obj);
    }

    /**
     *  Resize the hash tables by increasing the capacity to twice the original size.
     *  returns nothing.
     */
    public void resize(){
        Entry<T>[][] tempTables = hashTables;
        capacity *= 2;
        threshold = (int)Math.log(capacity);
        hashTables = new Entry[capacity][2];


        //Add all elements to hash tables of increased capacity.
        for(int i=0; i<tempTables.length; i++) {
            int tableNum = 0;
            while(tableNum < 2) {
                if(tempTables[i][tableNum] != null) {
                    add(tempTables[i][tableNum].element);
                }
                tableNum++;
            }
        }
    }

    /**
     *
     * @param t - The object which has to be checked if present in the hash tables.
     * @return true if present, false otherwise.
     */
    public boolean contains(T t) {
        int tableNum=0;                                     //index used to denote the hash table number among 0, 1 or 2.
        while(tableNum < 2){
            int hashVal = getHashVal(t, tableNum);
            if(hashTables[hashVal][tableNum] != null && t.equals(hashTables[hashVal][tableNum].element))
                return true;
            else
                tableNum++;
        }
        // after searching the 2 hash tables above, check secondary hash.
        return secondaryHash.contains(t);
    }

    /**
     *
     * @param t - Object which is to be added to the hash table. If number of tries crosses threshold, insert into
     *          Java's hash table.
     */
    public void add(T t) {
        if(contains(t)){
            System.out.println("Entry is already present.");
            return ;
        }

        tableSize++;
        if((double)tableSize/capacity >= LOAD_FACTOR)
            resize();

        int count = 0, tableNum = 0;

        while(count < threshold){
            int hashVal = getHashVal(t, tableNum);

            if(hashTables[hashVal][tableNum] != null){
                T temp = hashTables[hashVal][tableNum].element;
                hashTables[hashVal][tableNum].element = t;
                tableNum = (tableNum + 1) % 2;
                t = temp;
                count++;
            }

            else {
                hashTables[hashVal][tableNum] = new Entry<>(t);
                break;
            }
        }

        if(count >= threshold)
            secondaryHash.add(t);

    }

    /**
     * @param t - Object to be removed from the hash table.
     * @return if able to remove the object successfully, false if the object is not present in hash table.
     */
    public boolean remove(T t) {
        int tableNum = 0;

        while(tableNum < 2){
            int hashVal = getHashVal(t, tableNum);
            if(hashTables[hashVal][tableNum] != null && t.equals(hashTables[hashVal][tableNum].element)){
                hashTables[hashVal][tableNum] = null;
                tableSize--;
                return true;
            }
            else
                tableNum++;
        }

        return false;
    }

    public int tableSize(){
        return tableSize;
    }

    // Prints the Hash Table for Cuckoo Hashing with k = 2.
    public void printHashTable() {
        System.out.println("\n Hash Table: ");
        System.out.format("%40s", "+--------------------------------------+\n");
        System.out.format("%-11s%-14s%-13s%-2s", "| Location", "| First Cell", "| Second Cell", " |\n");
        System.out.println("|--------------------------------------|");

        int location = 0;
        while (location < hashTables.length) {
            Entry<T> c1 = hashTables[location][0];
            Entry<T> c2 = hashTables[location][1];
            Integer nothing = null;

            if (c1 != null) {
                if (c2 != null) {
                    System.out.format("%-11s%-14s%-13s%-2s", "| "+ location, "| "
                            + c1.element, "| " + c2.element, " |\n");
                }
                else {
                    System.out.format("%-11s%-14s%-13s%-2s", "| "+ location, "| "
                            + c1.element, "| " + nothing, " |\n");
                }
            }
            else {
                if (c2 != null) {
                    System.out.format("%-11s%-14s%-13s%-2s", "| "+ location, "| "
                            + nothing, "| " + c2.element, " |\n");
                }
                else {
                    System.out.format("%-11s%-14s%-13s%-2s", "| "+ location, "| "
                            + nothing, "| " + nothing, " |\n");
                }
            }
            location++;
        }

        System.out.format("%40s","+--------------------------------------+\n\n");
        System.out.println("Size = " + tableSize + " Capacity = " + capacity);
    }

    public static void main(String[] args) {
        CuckooHashing<Integer> c1 = new CuckooHashing<>();
        int[] nums = { 121, 62, 166, 35, 43, 3, 24, 0, 45, 42, 30, 12, 50, 24, 49, 26, 17, 187};

        for(int num: nums)
            c1.add(num);
        c1.printHashTable();
        System.out.println(c1.tableSize);

        for(int num: nums)
            System.out.println(c1.contains(num));

        for(int num: nums)
            c1.remove(num);
        c1.printHashTable();
        System.out.println(c1.tableSize);
    }
}
