import java.util.Arrays;
import java.util.Random;

/*
 * This program implements the parallel version of Merger Sort. Testing of the program is primarily
 * added to the class TestParallelMergesort. However, a main method
 * is also included in this program that measures runtime and speedup for the program
 * with values from 100 to 10 million. This can also be done in TestParallelMergesort.
 *
 *
 * User guide
 * - Compiler program: "javac *.java"
 * - Run program: "java ParallelMergesort"
 */

public class ParallelMergesort {

    static int seed = 321;
    static int runs = 9;

    // Uses number of threads equal to number of cores on the machine
    static int threads = Runtime.getRuntime().availableProcessors();



/*                       Parallel Merge Sort algorithm                        */
/******************************************************************************/

    // Main method which measure runtime and speedup from 100 - 10 million
    public static void main(String[] args) {
      timeMeasurementsForAllN(2);
    }
/******************************************************************************/







/*                       Parallel Merge Sort algorithm                        */
/******************************************************************************/

    // Method doing the merge sorting
    public void parallelMergesort(int[] arr, int k) {
        // Base case
        if (k <= 1) {
          Mergesort.mergeSort(arr, arr.length);
        }

        // As long as the arryas is divisible
        else if (arr.length >= 0) {

            // Get indexes for separating array
            int len = arr.length;
            int mid = len/2;

            // Create two arrays for left and right part of array
            int [] left_arr = new int[mid];
            int [] right_arr = new int[len - mid];

            //Dividing array into two and copying into two separate arrays (same as in precode)
            int p = 0;
            for(int i = 0;i<len;++i){

              // Fill ip left array
              if(i < mid){
                left_arr[i] = arr[i];
              }

              // Fill up right array
              else{
                right_arr[p] = arr[i];
                p = p + 1;
              }
            }

            // Create two threads, one with left array and one with right array
            Thread left = new Thread(new Worker(left_arr,  k));
            Thread right = new Thread(new Worker(right_arr, k));

            // Start both threads
            left.start();
            right.start();

            // Synchoronize using join
            try {
                left.join();
                right.join();
            } catch (Exception e) {
              e.printStackTrace();
            }

            // Using the merge() method from the sequential version to merge arrays
            Mergesort.merge(left_arr, right_arr, arr, mid, len-mid);
        }
    }



    // Inner Worker class
    class Worker implements Runnable {
      int[] arr;
      int k;

      // Worker constructor
      public Worker(int[] arr,  int k) {
          this.arr = arr;
          this.k = k / 2;
      }

      // Run method starting the recursion
      @Override
      public void run() {
          parallelMergesort(arr, k);
      }
    }
/******************************************************************************/







/*                       Methods used for testing                             */
/******************************************************************************/

    //Time measurements for all values of N
    public static void timeMeasurementsForAllN(int timeMode){
      int allN[] = {100, 1000, 10000, 100000, 1000000, 10000000};
      for(int i = 0; i < allN.length; i++){
        timeMeasurements(timeMode, allN[i]);
        System.gc();
      }
    }


    //Gets runtime for all algorithms
    public static void timeMeasurements(int timeMode, int n){
      double[] seqTimes = new double[runs];
      double[] parTimes = new double[runs];

      for(int i = 0; i < runs; i++){

        //Time sequential mergesort
        int [] array = randomArray(n, seed);
        Mergesort sort = new Mergesort();
        long startTime = System.nanoTime();
        sort.mergeSort(array,array.length);
        double time = (System.nanoTime() - startTime) / 1000000.0;
        seqTimes[i] = time;
        System.gc();

        //Time parallel mergesort
        int [] array2 = randomArray(n, seed);
        ParallelMergesort sort2 = new ParallelMergesort();
        long startTime2 = System.nanoTime();
        sort2.parallelMergesort(array2, threads);
        double time2 = (System.nanoTime() - startTime2) / 1000000.0;
        parTimes[i] = time2;
        System.gc();
      }

      //Prints median measurements
      if(timeMode == 2){
        getMedianMeasurements(seqTimes, parTimes, n);
      }
    }



    //Uses the runtime data to print median measurements
    public static void getMedianMeasurements(double[] seq, double[] par, int n){

      //Sorts runtimes
      Arrays.sort(seq);
      Arrays.sort(par);

      //Finds median runtime Merge Sort
      double seqMedian = seq[runs/2];
      double parMedian = par[runs/2];
      double speedup = seqMedian / parMedian;

      System.out.println("      **** MEDIAN MEASUREMENTS ****   ");
      System.out.println("‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾");
      System.out.println("N = " + n + " | Runs = " + runs + " | Threads = " + threads);
      System.out.println();

      //Prints out runtime and speedup
      System.out.println("Merge Sort");
      System.out.println("• Sequential median: " + seqMedian + " ms");
      System.out.println("• Parallel median: " + parMedian + " ms");
      System.out.println("• Speedup: " + speedup);
      System.out.println("‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾");
      System.out.println("\n");

    }



    //Prints runtime and speedup
    public static void printRuntime(String name1, String name2, double runtimeSeq, double runtimePar){
      System.out.println("• Runtime " + name1 + ": " + runtimeSeq + " ms");
      System.out.println("• Runtime " + name2 + ": " + runtimePar + " ms");
      System.out.println("• Speedup: " + runtimeSeq / runtimePar);
      System.out.println();
    }


    // Generate random array (taken from oblig1)
    public static int[] randomArray(int n, int seed) {
      Random random = new Random(seed);
      int[] a = new int[n];

      for (int i = 0; i < n; i++) {
        a[i] = random.nextInt(n);
      }

      return a;
    }

}
