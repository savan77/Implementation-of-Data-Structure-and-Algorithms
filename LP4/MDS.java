
package sxv180069;

/**
 * CS 6301.011: Implementation of Data Structures and Algorithms
 * Long Project LP4: Multidimensional search (MDS) Implementation
 *
 * @author Shrey Shah (sxs190184)
 * @author Savan Visalpara (sxv180069)
 * @author Harshita Rastogi (hxr190001)
 * @author Tejas Gupta (txg180021)
 *
 */
import java.util.*;

public class MDS {
    class Item{
        Long id;
        HashSet<Long> description;
        Money price;

        public Item(){
            id = Long.valueOf(0);
            price = new Money();
            description = new HashSet<>();
        }

        public Item(long id, Money price, List<Long> description){
            this.id = id;
            this.price = price;
            this.description = new HashSet<>();

            for(Long i : description)
                this.description.add(i);
        }
    }
    // Add fields of MDS here
    TreeMap<Long, Item> tree;
    HashMap<Long,HashSet<Long>> table;
    long size;

    // Constructors
    public MDS() {
        tree = new TreeMap<>();
        table = new HashMap<>();
        size = 0;
    }
    /** Public methods of MDS. Do not change their signatures.
     __________________________________________________________________
     a. Insert(id,price,list): insert a new item whose description is given
     in the list.  If an entry with the same id already exists, then its
     description and price are replaced by the new values, unless list
     is null or empty, in which case, just the price is updated.
     Returns 1 if the item is new, and 0 otherwise.
     * @param id the id of Item to be inserted
     * @param price the price of the Item to be inserted
     * @param list description(s) of the Item to be inserted
     * @return Returns 1 if the item is new, and 0 otherwise.
     */
    public int insert(long id, Money price, java.util.List<Long> list) {
        // tree already has the id
        if (tree.containsKey(id)){
            Item temp = tree.get(id);
            // if the list is not null then also replace list with new one
            if(list!=null && !(list.isEmpty())){
                for(long e: temp.description){
                    table.get(e).remove(temp.id);
                    if(table.get(e).isEmpty())
                        table.remove(e);
                }
                temp.description.clear();
                for(Long d: list)
                    temp.description.add(d);
                for(Long d:temp.description){
                    if(table.containsKey(d))
                        table.get(d).add(temp.id);
                    else{
                        HashSet<Long> nhset = new HashSet<>();
                        nhset.add(temp.id);
                        table.put(d,nhset);
                    }
                }
            }
            // only replace price
            temp.price = price;
            return 0;
        }
        Item nitem = new Item(id,price,list);
        tree.put(id,nitem);
        Item temp = tree.get(id);
        for(Long d:temp.description){
            if(table.containsKey(d))
                table.get(d).add(temp.id);
            else{
                HashSet<Long> nhset = new HashSet<>();
                nhset.add(temp.id);
                table.put(d,nhset);
            }
        }
        size++;
        return 1;
    }

    /** b. Find(id): return price of item with given id (or 0, if not found).
     * @param id the id of the item
     * @return price of the item, if exists, else 0
     */
    public Money find(long id) {
        if(tree.containsKey(id))
            return tree.get(id).price;
        return new Money();
    }

    /**
     c. Delete(id): delete item from storage.  Returns the sum of the
     long ints that are in the description of the item deleted,
     or 0, if such an id did not exist.
     * @param id the id of the Item to be deleted
     * @return Returns the sum of the long integers that are in the
     * description of the Item(id), or 0 if such an id does not exist.
     */
    public long delete(long id) {
        if (!tree.containsKey(id))
            return 0;
        Item temp = tree.get(id);
        long sum = 0;
        for(Long d: temp.description){
            table.get(d).remove(temp.id);
            if(table.get(d).isEmpty())
                table.remove(d);
            sum+=d;
        }
        tree.remove(id);
        return sum;
    }

    /**
     d. FindMinPrice(n): given a long int, find items whose description
     contains that number (exact match with one of the long ints in the
     item's description), and return lowest price of those items.
     Return 0 if there is no such item.
     * @param n the description
     * @return minimum price if that description is present, else 0 (Money).
     */
    public Money findMinPrice(long n) {
        if(!table.containsKey(n))
            return new Money();
        HashSet<Long> set = table.get(n);
        Money min = new Money(Long.MAX_VALUE,0);
        for(Long i : set){
            Item temp = tree.get(i);
            if(temp.price.compareTo(min)<0)
                min = temp.price;
        }
        return min;
    }

    /**
     e. FindMaxPrice(n): given a long int, find items whose description
     contains that number, and return highest price of those items.
     Return 0 if there is no such item.
     * @param n the description
     * @return maximum price if that description is present, else 0 (Money).
     */
    public Money findMaxPrice(long n) {
        if(!table.containsKey(n))
            return new Money();
        HashSet<Long> set = table.get(n);
        Money max = new Money();
        for(Long i : set){
            Item temp = tree.get(i);
            if(temp.price.compareTo(max)>0)
                max = temp.price;
        }
        return max;
    }

    /**
     f. FindPriceRange(n,low,high): given a long int n, find the number
     of items whose description contains n, and in addition,
     their prices fall within the given range, [low, high].
     * @param n the description
     * @param low the lower limit of price
     * @param high the upper limit of price
     * @return count of Items whose prices are in [low, high] and has
     * description n
     */
    public int findPriceRange(long n, Money low, Money high) {
        HashSet<Long> temp = table.get(n);
        int count = 0;

        for(Long i:temp){
            Item item = tree.get(i);
            if(item.price.compareTo(low)>=0 && item.price.compareTo(high)<=0)
                count++;
        }
        return count;
    }

    /**
     * Helper method: Increments price of item(id) by rate%.
     *
     * @param id the id of the item
     * @param rate the rate by which price increases
     * @return the incremented price in long format
     */
    long priceHicker(Long id,double rate){
        Money temp = tree.get(id).price;
        long currPrice = temp.dollars()*100 + temp.cents();
        long increment = (long)Math.floor((currPrice*(rate/100)));
        long newPrice = currPrice + increment;
        int cents = (int)(newPrice%100);
        long dollar = newPrice/100;
        insert(id,new Money(dollar,cents),null);
        return increment;
    }

    /**
     g. PriceHike(l,h,r): increase the price of every product, whose id is
     in the range [l,h] by r%.  Discard any fractional pennies in the new
     prices of items.  Returns the sum of the net increases of the prices.
     * @param l lower limit
     * @param h higher limit
     * @param rate the rate of change
     * @return Returns the sum of the net increases of the prices.
     */
    public Money priceHike(long l, long h, double rate) {
        if(l>h)
            return new Money();
        if(tree.firstKey().compareTo(h)>0)
            return new Money();
        if(tree.lastKey().compareTo(l)<0)
            return new Money();
        long increment = 0;
        Long sid = tree.ceilingKey(l);
        Long eid = tree.floorKey(h);
        if(sid.equals(eid)){
            increment += priceHicker(sid,rate);
            int cents = (int)increment%100;
            long dollar = increment/100;
            return new Money(dollar,cents);
        }
        Map<Long, Item> submap = tree.subMap(sid,true,eid,true);
        for(Long i :submap.keySet()){
            increment += priceHicker(i,rate);
        }
        int cents = (int)increment%100;
        long dollar = increment/100;
        return new Money(dollar,cents);
    }

    /**
     h. RemoveNames(id, list): Remove elements of list from the description of id.
     It is possible that some of the items in the list are not in the
     id's description.  Return the sum of the numbers that are actually
     deleted from the description of id.  Return 0 if there is no such id.
     * @param id the Item(id) whose Names is to be removed
     * @param list the Names to be removed
     * @return Return the sum of the numbers that are actually deleted from
     * the description of id. Return 0 if there is no such id.
     */
    public long removeNames(long id, java.util.List<Long> list) {
        if(list==null||list.isEmpty())
            return 0;
        Item temp = tree.get(id);
        long sum = 0;
        for(Long i :list){
            if(temp.description.contains(i)){
                sum += i;
                table.get(i).remove(temp.id);
                if(table.get(i).isEmpty())
                    table.remove(i);
            }
            tree.get(id).description.remove(i);
        }
        return sum;
    }

    // Do not modify the Money class in a way that breaks LP3Driver.java
    public static class Money implements Comparable<Money> {
        long d;  int c;
        public Money() { d = 0; c = 0; }
        public Money(long d, int c) { this.d = d; this.c = c; }
        public Money(String s) {
            String[] part = s.split("\\.");
            int len = part.length;
            if (len < 1) {
                d = 0;
                c = 0;
            } else if (len == 1) {
                d = Long.parseLong(s);
                c = 0;
            } else {
                d = Long.parseLong(part[0]);
                c = Integer.parseInt(part[1]);
                if (part[1].length() == 1) {
                    c = c * 10;
                }
            }
        }
        public long dollars() { return d; }
        public int cents() { return c; }
        public int compareTo(Money other) { // Complete this, if needed
            if(this.d < other.d)
                return -1;
            else if(this.d > other.d)
                return 1;
            else{
                if(this.c < other.c)
                    return -1;
                else if(this.c > other.c)
                    return 1;
                else
                    return 0;
            }
        }
        public String toString() { return d + "." + c; }
    }

}
