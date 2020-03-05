/** @author Savan Amitbhai Visalpara (sxv180069)
 @author Srikumar Ramaswamy (sxr170016)
 This class implements bounded-size queue with several supporting methods.
 **/


package sxv180069;
import java.lang.Object;
import java.util.Scanner;

public class BoundedQueue<T> {
    T[] queueArr;
    int indexPtr;                               // pointer to the latest element in queue.
    int capacity;                               // maximum capacity of the queue.

    BoundedQueue(int size) {
        queueArr = (T[]) new Object[size];
        indexPtr = -1;
        capacity = size;
    }

    private boolean offer(T x) {
        if(this.indexPtr ==  this.capacity-1)    //queue is full, return false.
            return false;
        this.indexPtr += 1;                     // increment pointer
        queueArr[this.indexPtr] = x;
        return true;
    }

    /** @return T front element **/
    private T peek() {
        // queue is empty, return null
        if(indexPtr == -1) {
            return null;
        }
        return this.queueArr[0];
    }

    /** @return T remove and return front element **/
    private T poll() {
        // return null if the queue is empty
        int num = this.size() - 1;
        if(num < 0) {
            System.out.println("Queue is empty. Cannot poll.");
            return null;
        }
        // get the front element
        T val = this.peek();
        T[] temp = (T[]) new Object[this.indexPtr];
        System.arraycopy(queueArr, 1, temp, 0, num);
        this.clear();
        System.arraycopy(temp, 0, queueArr, 0, num);
        this.indexPtr = temp.length - 1;
        return val;
    }

    /** @return int size of the queue **/
    private int size() {
        return this.indexPtr + 1;
    }

    /** @return boolean check whether queue is empty **/
    private boolean isEmpty() {
        return this.indexPtr == -1;
    }

    /** @return void clear the queue **/
    private void clear() {
        queueArr = (T[]) new Object[this.capacity];       // re-initialize the array.
        this.indexPtr = -1;                               // move index back to initial position.
    }

    /** @return void print the queue **/
    private void printQueue() {
        System.out.println("Queue contains: ");
        for(T el : this.queueArr)
            System.out.print(el + " ");
        System.out.println("");
    }

    /** @return void copy queue to an array **/
    private void toArray(T[] a) {
        if(this.size()>0){
            System.arraycopy(this.queueArr, 0, a, 0, this.size());
            System.out.println("Copied array...\n");
        }
        else
            System.out.println("Queue is empty. No elements to copy to array.");   // queue is empty
    }

    public static void main(String[] args) {
        System.out.println("Enter the size of the queue..");
        Scanner szScanner = new Scanner(System.in);
        int size = szScanner.nextInt();
        BoundedQueue<Integer> bq = new BoundedQueue<>(size);
        Scanner scanner = new Scanner(System.in);
        int choice = 0;
        do {
            System.out.println("---------------------------------");
            System.out.println("1. Offer an element \n2. Poll \n3. Peek \n4. Find queue size \n5. Check if queue is empty \n6. Clear queue \n7. Copy to array \n8. Print queue \n0. Exit");
            System.out.println("---------------------------------");
            System.out.print("Enter your choice...");
            choice = scanner.nextInt();
            switch (choice) {
                case 0:
                    break;
                case 1:   // add an element
                    System.out.println("Enter the element to be added..");
                    Scanner s = new Scanner(System.in);
                    int x = s.nextInt();
                    if(!bq.offer(x))
                        System.out.println("Element cannot be added. Queue is full!");
                    else
                        bq.printQueue();
                    break;
                case 2:     // remove and return front element
                    Integer rem = bq.poll();
                    if(rem != null) {
                        System.out.println(rem + " removed from the queue.");
                        bq.printQueue();
                    }
                    break;
                case 3:      // return front element
                    Integer qHead  = bq.peek();
                    if(qHead != null)
                        System.out.println(bq.peek() + " is at the front of the queue.");
                    else
                        System.out.println("Queue is Empty. Cannot return front element.");
                    break;
                case 4:      // print queue size
                    System.out.println("Queue contains " + bq.size() + " elements");
                    break;
                case 5:      // check if queue is empty
                    System.out.println(bq.isEmpty() ? "Queue is empty. " : "Queue is not empty");
                    break;
                case 6:      // clear the queue
                    bq.clear();
                    System.out.println("Cleared the queue.");
                    bq.printQueue();
                    break;
                case 7:     // copy queue to an array
                    Integer[] copy = new Integer[bq.size()];
                    bq.toArray(copy);
                    System.out.print("Array: ");
                    for(int el : copy)
                        System.out.print(el + " ");
                    break;
                case 8:    // print the queue
                    bq.printQueue();
            }
        } while(choice != 0);
    }
}

/**    Sample Input-Output

 Enter the size of the queue..
3
---------------------------------
1. Offer an element
2. Poll
3. Peek
4. Find queue size
5. Check if queue is empty
6. Clear queue
7. Copy to array
8. Print queue
0. Exit
---------------------------------
Enter your choice...1
Enter the element to be added..
5
Queue contains:
5 null null

Enter your choice...1
Enter the element to be added..
9
Queue contains:
5 9 null

Enter your choice...1
Enter the element to be added..
3
Queue contains:
5 9 3

Enter your choice...2
5 removed from the queue.
Queue contains:
9 3 null

Enter your choice...4
Queue contains 2 elements

Enter your choice...5
Queue is not empty

Enter your choice...7
Copied array...

Array: 9 3 

Enter your choice...8
Queue contains:
9 3 null

Enter your choice...6
Cleared the queue.
Queue contains:
null null null


 **/