package sxr170016; /**
 * Sample starter code for SP3.
 *
 * @author Savan Amitbhai Visalpara (sxv180069)
 * @author Srikumar Ramaswamy (sxr170016)
 * This class implements various versions (takes) of the mergesort algorithm.
 */


import java.util.Arrays;
import java.util.Random;

public class SP3 {
    public static Random random = new Random();
    public static int numTrials = 50;

    public static int threshold = 10;

    public static void main(String[] args) {
        int n = 5;
        int choice = 1 + random.nextInt(4);
        if (args.length > 0) {
            n = Integer.parseInt(args[0]);
        }
        if (args.length > 1) {
            choice = Integer.parseInt(args[1]);
        }
        int[] arr = new int[n];
        // initialize an array
        initializeArray(n, arr);
        Timer timer = new Timer();
        switch (choice) {
            case 2:           // take 2
                for (int i = 0; i < numTrials; i++) {
                    Shuffle.shuffle(arr);
                    //Shuffle.printArray(Arrays.stream(arr).boxed().toArray(Integer[]::new), "Trial " + (i+1) + " before sorting");
                    mergeSort2(arr);
                    //Shuffle.printArray(Arrays.stream(arr).boxed().toArray(Integer[]::new), "Trial " + (i+1) + " after sorting");
                }
                break;
            case 3:            // take 3
                for (int i = 0; i < numTrials; i++) {
                    Shuffle.shuffle(arr);
                    //Shuffle.printArray(Arrays.stream(arr).boxed().toArray(Integer[]::new), "Trial " + (i+1) + " before sorting");
                    mergeSort3(arr);
                    //Shuffle.printArray(Arrays.stream(arr).boxed().toArray(Integer[]::new), "Trial " + (i+1) + " after sorting");
                }
                break;  
            case 4:              // take 4
                for (int i = 0; i < numTrials; i++) {
                    Shuffle.shuffle(arr);
                    Shuffle.printArray(Arrays.stream(arr).boxed().toArray(Integer[]::new), "Trial " + (i+1) + " before sorting");
                    mergeSort4(arr);
                    Shuffle.printArray(Arrays.stream(arr).boxed().toArray(Integer[]::new), "Trial " + (i+1) + " after sorting");
                }
                break;  
            case 6:              // take 6
                for (int i = 0; i < numTrials; i++) {
                    Shuffle.shuffle(arr);
                    //Shuffle.printArray(Arrays.stream(arr).boxed().toArray(Integer[]::new), "Trial " + (i+1) + " before sorting");
                    mergeSort6(arr);
                    //Shuffle.printArray(Arrays.stream(arr).boxed().toArray(Integer[]::new), "Trial " + (i+1) + " after sorting");
                }
                break;
        }

        timer.end();
        timer.scale(numTrials);

        System.out.println("Choice: " + choice + "\n" + timer);
    }

    /** @param   n   number of elements
      * @param   arr empty array to be initialized
      * @return      none **/
    private static void initializeArray(int n, int[] arr) {
        for (int i = 0; i < n; i++) {
            arr[i] = i;
        }
    }


    // take 2
    // do not create B in merge
    // instead create it in mergeSort2()
    public static void mergeSort2(int[] arr) {
        int[] B = new int[arr.length];
        mergeSort2(arr, B, 0, arr.length - 1);
    }

    private static void mergeSort2(int[] A, int[] B, int p, int r) {
        if (p < r) {
            int q = p + (r - p) / 2;
            mergeSort2(A, B, p, q);
            mergeSort2(A, B, q + 1, r);
            merge2(A, B, p, q, r);
        }
    }

    private static void merge2(int[] A, int[] B, int p, int q, int r) {
        System.arraycopy(A, p, B, p, r - p + 1);
        int i = p, k = p, j = q + 1;
        while (i <= q && j <= r) {
            if (B[i] <= B[j])
                A[k++] = B[i++];
            else
                A[k++] = B[j++];
        }
        while (i <= q)
            A[k++] = B[i++];
        while (j <= r)
            A[k++] = B[j++];
    }


    // take 3
    // avoid unnecessary copying between A and B in merge
    public static void mergeSort3(int[] arr) {
        int[] B = new int[arr.length];
        System.arraycopy(arr, 0, B, 0, arr.length);
        mergeSort3(arr, B, 0, arr.length - 1);
    }

    public static void mergeSort3(int[] A, int[] B, int p, int r) {
        if (p < r) {
            int q = p + (r - p) / 2;
            mergeSort3(B, A, p, q);
            mergeSort3(B, A, q + 1, r);
            merge3(A, B, p, q, r);
        }
    }

    private static void merge3(int[] A, int[] B, int p, int q, int r) {
        int i = p, k = p, j = q + 1;
        while (i <= q && j <= r) {
            if (B[i] <= B[j])
                A[k++] = B[i++];
            else
                A[k++] = B[j++];
        }
        while (i <= q)
            A[k++] = B[i++];
        while (j <= r)
            A[k++] = B[j++];
    }

    // take 4
    // use insertion sort if number of elements (threshold) is below certain number
    private static void mergeSort4(int[] arr) {
        int[] B = new int[arr.length];
        System.arraycopy(arr, 0, B, 0, arr.length);
        mergeSort4(arr, B, 0, arr.length - 1);
    }

    private static void mergeSort4(int[] A, int[] B, int p, int r) {
        if(r-p+1 < threshold)
            insertionSort(A, p, r);
        if (p < r) {
            int q = p + (r - p) / 2;
            mergeSort3(B, A, p, q);
            mergeSort3(B, A, q + 1, r);
            merge3(A, B, p, q, r);    // merge func is same
        }
    }


    // take 6
    // use iteration and insertion sort
    private static void mergeSort6(int[] A) {
        int n = A.length;
        int[] B = new int[A.length];
        System.arraycopy(A, 0, B,0, n);
        int[] inp = A;
        for(int j=0; j<n; j = j+threshold) {
            insertionSort(A, j, j+threshold-1);
        }
        int remStartPointer = 0;
        for(int i=threshold; i<n; i=2*i) {
            remStartPointer = i;
            for(int j=0; j<n; j=j+2*i) {
                int mid = Math.min(j+i-1, n-1);
                int end = Math.min(j+2*i-1, n-1);
                merge3(B, A, j, mid, end);
            }
            int[] temp = A;
            A = B;
            B = temp;
        }
        merge3(B, A, 0, remStartPointer-1, n-1);

        if(inp != A)
            System.arraycopy(A, 0, inp, 0, n);
    }

    public static void insertionSort(int[] arr, int start, int end) {
        for(int i=start+1; i<end+1 && i<arr.length; i++) {
            int temp = arr[i];
            int j = i-1;
            while(j>=start && (arr[j] > temp) ){
                arr[j+1] = arr[j];
                j--;
            }
            arr[j+1] = temp;
        }
    }


    /** Timer class for roughly calculating running time of programs
     *  @author rbk
     *  Usage:  Timer timer = new Timer();
     *          timer.start();
     *          timer.end();
     *          System.out.println(timer);  // output statistics
     */

    public static class Timer {
        long startTime, endTime, elapsedTime, memAvailable, memUsed;
        boolean ready;

        public Timer() {
            startTime = System.currentTimeMillis();
            ready = false;
        }

        public void start() {
            startTime = System.currentTimeMillis();
            ready = false;
        }

        public Timer end() {
            endTime = System.currentTimeMillis();
            elapsedTime = endTime - startTime;
            memAvailable = Runtime.getRuntime().totalMemory();
            memUsed = memAvailable - Runtime.getRuntime().freeMemory();
            ready = true;
            return this;
        }

        public long duration() {
            if (!ready) {
                end();
            }
            return elapsedTime;
        }

        public long memory() {
            if (!ready) {
                end();
            }
            return memUsed;
        }

        public void scale(int num) {
            elapsedTime /= num;
        }

        public String toString() {
            if (!ready) {
                end();
            }
            return "Time: " + elapsedTime + " msec.\n" + "Memory: " + (memUsed / 1048576) + " MB / " + (memAvailable / 1048576) + " MB.";
        }
    }

    /** @author rbk : based on algorithm described in a book
     */


    /* Shuffle the elements of an array arr[from..to] randomly */
    public static class Shuffle {

        public static void shuffle(int[] arr) {
            shuffle(arr, 0, arr.length - 1);
        }

        public static <T> void shuffle(T[] arr) {
            shuffle(arr, 0, arr.length - 1);
        }

        public static void shuffle(int[] arr, int from, int to) {
            int n = to - from + 1;
            for (int i = 1; i < n; i++) {
                int j = random.nextInt(i);
                swap(arr, i + from, j + from);
            }
        }

        public static <T> void shuffle(T[] arr, int from, int to) {
            int n = to - from + 1;
            Random random = new Random();
            for (int i = 1; i < n; i++) {
                int j = random.nextInt(i);
                swap(arr, i + from, j + from);
            }
        }

        static void swap(int[] arr, int x, int y) {
            int tmp = arr[x];
            arr[x] = arr[y];
            arr[y] = tmp;
        }

        static <T> void swap(T[] arr, int x, int y) {
            T tmp = arr[x];
            arr[x] = arr[y];
            arr[y] = tmp;
        }

        public static <T> void printArray(T[] arr, String message) {
            printArray(arr, 0, arr.length - 1, message);
        }

        public static <T> void printArray(T[] arr, int from, int to, String message) {
            System.out.print(message);
            for (int i = from; i <= to; i++) {
                System.out.print(" " + arr[i]);
            }
            System.out.println();
        }
    }
}

