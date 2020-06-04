package sxv180069;

import java.util.Arrays;
import java.util.Random;

public class RMQ {
    /*
    Implement Hybrid one approach discussed in the class.
    Use sparse table RMQ structure for block minima
    O(n), O(logn)

     */
    int blockSize;
    int[] array;
    int[][] sparseTable;
    int[] blockMinimas;
    int result = Integer.MIN_VALUE;
    int[] lookupTable;
    // constructor
    public RMQ(int[] arr){
        array = arr;
        // set block size ot log n
        blockSize = (int) (Math.log(array.length)/Math.log(2))+1;
//        System.out.println(blockSize);
        int numBlocks = (int) Math.ceil((double)array.length/blockSize);
        blockMinimas = new int[numBlocks];
//        System.out.println(blockMinimas.length);
        //initialization of block minimas
        initialize_minimas(array);
        lookupTable = new int[numBlocks + 1];
        lookupTable[0] = -1;
        for (int i = 1; i <= numBlocks; i++) {
            lookupTable[i] = lookupTable[i >> 1] + 1;
        }
//        System.out.println(Arrays.toString(lookupTable));
        populateSparseTable(numBlocks);
    }

    /*
    Function to populate sparse table.
    @params numBlocks  number of blocks considered
     */
    public void populateSparseTable(int numBlocks){

        sparseTable = new int[numBlocks][lookupTable[numBlocks]+1];
        //populate table
        for (int j = 0; j <= lookupTable[numBlocks]; j++) {
            for (int i = 0; i + (1 << j) <= numBlocks; i++) {
                if (j == 0) {
                    sparseTable[i][j] = blockMinimas[i];
                } else {
                    int x1 = sparseTable[i][j - 1];
                    int x2 = sparseTable[i + (1 << (j - 1))][j - 1];
                    if (array[x1] < array[x2]){
                        sparseTable[i][j] = x1;
                    }
                    else{
                        sparseTable[i][j] = x2;
                    }
                }
            }
        }

    }

    /*
    Function to initialize block minimas
    @params int[] array  (given array)
     */
    public void initialize_minimas(int[] array){
        int i;
        java.util.Arrays.fill(blockMinimas, Integer.MIN_VALUE);

        for (i = 0; i < array.length; i++) {
            int current_block_min = blockMinimas[i/blockSize];
//            System.out.println(current_block_min);
            if (current_block_min == Integer.MIN_VALUE || array[i] < array[current_block_min]) {
                blockMinimas[i/blockSize] = i;
            }
        }

    }

    /*
    Utility function to compare values and recompute result.
    @params int x  index
     */
    public void compareValues(int x){
        if(result == Integer.MIN_VALUE || array[x] < array[result]){
            result = x;
        }
    }


    /*
    Query on given array.
    @params i  initial index
    @params j  end index
    @returns index of the min num
     */
    public int query(int i, int j){
        int start = (int) Math.ceil(i/blockSize);
        int end = (int) Math.ceil((j+1)/blockSize)-1;
        if(start <= end){
            int lookupIdx = lookupTable[end-start+1];
            int x1 = sparseTable[start][lookupIdx];
            int x2 = sparseTable[end - (1<<lookupIdx)+1][lookupIdx];
            if (array[x1] < array[x2]){
                result = x1;
            }
            else{
                result = x2;
            }
            for(int x=1; x<start*blockSize; x++){
               compareValues(x);
            }
            for (int x = j; x >= (end + 1) * blockSize; x--) {
                compareValues(x);
            }
        }
        else {
            for (int x = i; x <= j; x++) {
               compareValues(x);
            }
        }
        return result;
        }



    public static void main(String[] arg){
        int NUM_ELEMENTS = 128000000;

        int[] test_input = new int[NUM_ELEMENTS];
        Random random = new Random();

        for(int i=0; i<NUM_ELEMENTS; i++){
            test_input[i] = random.nextInt();
        }

        Timer timer = new Timer();
        RMQ rmq = new RMQ(test_input);
        System.out.println("For Array size: " + NUM_ELEMENTS);
        System.out.print("Preprocessing Time: "+timer.end()+"\n\n");
        int totalTime = 0;

        for(int trial=1; trial <=5; trial++){
            Timer timerQ = new Timer();
            //run query on various ranges
            int i = random.nextInt(NUM_ELEMENTS);
            int j = i + random.nextInt(NUM_ELEMENTS - i -1) + 1;
            int result = rmq.query(i, j);
            System.out.println("\n\nRange: " + (j-i+1));
            Timer end = timerQ.end();
            System.out.println("Query Time: "+ end +"\n");
            System.out.println("Output Index: "+result);
            totalTime += end.elapsedTime;
        }

        System.out.println("Average Time for size " + NUM_ELEMENTS + " is: " + totalTime/5 + "ms" );
    }
}
