/** @author Savan Amitbhai Visalpara (sxv180069)
    @author Srikumar Ramaswamy (sxr170016)
This class inherits SinglyLinkedList and adds/overrides several methods
to implement functions such as add, prev, next, remove.
 **/

package sxv180069;
import java.util.Scanner;
import java.util.NoSuchElementException;

public class DoublyLinkedList<T> extends SinglyLinkedList<T> {

    // override Entry class to add prev
    static class Entry<E> extends SinglyLinkedList.Entry<E> {
        SinglyLinkedList.Entry<E> prev;
        /**
         * @param  x    element of a node
         * @param  next pointer to next node
         * @param  prev pointer to prev node
         * @return      none **/
        Entry(E x, Entry<E> next, SinglyLinkedList.Entry<E> prev) {
            super(x, next);
            this.prev = prev;
        }
    }


    public DLLIterator iterator() {
        return new DLLIterator();
    }

    // Inherit SLLIterator and add DoublyLinkedList specific methods
    protected class DLLIterator extends SLLIterator {
        
        /** @param  x    element of a node
          * @return      none **/
        public void add(T x) {
            if(cursor == head) //if the cursor is head, we should not typecast it to a DLL entry since we are inheriting.
                add(new Entry<T>(x, (Entry<T>)cursor.next, head));
            else
                add(new Entry<T>(x, (Entry<T>)cursor.next, (Entry<T>)cursor));
        }

        
        /** @param  ent  object of Entry (node)
          * @return      none (adds the element) **/
        public void add(Entry<T> ent) {

            // handle case when adding to tail of list
            if (cursor.next != null){
                ((Entry<T>) cursor.next).prev = ent;
            }
            cursor.next = ent;
//            assignment document didn't mention whether we should move cursor after adding the element.
//            if the requirement is to move the cursor to the newly added element then uncomment following two lines.

//            prev = cursor;
//            cursor = ent;
            ready = false;
            size++;
        }

        
        /** @return  boolean checks whether there is a previous element    **/
        public boolean hasPrev() {
            return prev != null;
        }

        
        /** @return      previous element **/
        public T prev() {
            cursor = prev;
            prev = ((Entry<T>) cursor).prev;
            // handle case when prev is the first element
            // it will work without this condition but it will throw an exception when reaches the head
            if (prev == head){
                prev = null;
            }
            ready = true;
            return cursor.element;
        }

        
        /** @return  none  removes current element **/
        @Override
        // remove current element. 
        // you must rub prev() or next() before running this command.
        public void remove() {
            super.remove();
            if (cursor.next != null) {
                if(prev == head)           //if prev is head, we should not typecast it to a DLL entry, since we are inheriting.
                    ((Entry<T>) cursor.next).prev = head;
                else
                    ((Entry<T>) cursor.next).prev = (Entry<T>)prev;
            }
        }
    }

    /**
     * @param  x  element of a node
     * @return  none  calls super method **/
    @Override
    public void add(T x) {
        super.add(new Entry<T>(x, null, tail));
    }


    public static void main(String[] args) throws NoSuchElementException {
        System.out.println("\n*** Doubly Linked List *** ");
        System.out.println("Press 1 to perform next() operation.");
        System.out.println("Press 2 to perform remove() operation. You must run next() or prev() before running remove().");
        System.out.println("Press 3 to perform prev() operation.");
        System.out.println("Press 4 to perform add() operation.\n");

        // initially populate list with 10 integers
        int n = 10;
        if(args.length > 0) {
            n = Integer.parseInt(args[0]);
        }

        DoublyLinkedList<Integer> lst = new DoublyLinkedList<>();
        DoublyLinkedList<Integer>.DLLIterator it = lst.iterator();

        for(int i=1; i<=n; i++) {
            lst.add(Integer.valueOf(i));
        }
        lst.printList();

        Scanner in = new Scanner(System.in);

        whileloop:
        while(in.hasNext()) {
            int com = in.nextInt();
            switch(com) {
                case 1:  // Move to next element and print it
                    if (it.hasNext()) {
                        System.out.println("Next Returned: " + it.next());
                    } else {
                        System.out.println("Reached the end of the list.");
                        break whileloop;
                    }
                    break;
                case 2: // Remove element
                    it.remove();
                    lst.printList();
                    break;
                case 3: // Move to previous element and print it
                    if (it.hasPrev()) {
                        System.out.println("Previous Returned: " + it.prev());
                    } else {
                        System.out.println("Reached head of the list.");
                        break whileloop;
                    }
                    break;
                case 4:  // add element before the item returned by next()
                    System.out.print("Enter integer: ");
                    int value = in.nextInt();
                    it.add(Integer.valueOf(value));
                    lst.printList();
                    break;
                default:  // Exit loop
                    break whileloop;
            }
        }
        lst.printList();
        System.out.println("Unzipping the list...");
        lst.unzip();
        lst.printList();
    }
}


/**

 ## Sample Input/Output ##

 *** Doubly Linked List ***
 Press 1 to perform next() operation.
 Press 2 to perform remove() operation. You must run next() or prev() before running remove().
 Press 3 to perform prev() operation.
 Press 4 to perform add() operation.

 10: 1 2 3 4 5 6 7 8 9 10
 1
 Next Returned: 1
 2
 9: 2 3 4 5 6 7 8 9 10
 1
 Next Returned: 2
 1
 Next Returned: 3
 4
 Enter integer: 1
 10: 2 3 1 4 5 6 7 8 9 10
 1
 Next Returned: 1
 3
 Previous Returned: 3
 3
 Previous Returned: 2
 1
 Next Returned: 3
 **/