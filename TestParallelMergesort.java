import java.util.Random;
import java.util.Arrays;
import java.util.Scanner;

/*
 * User guide
 * Compile program: "javac *.java"
 * Run program: "java TestParallelMergesort"
 *
 * This is a test program that tests the parallel version of Merge Sort
 * and how it performs compared to the sequential version. To test the algorithm,
 * the program will present a menu with three different options.
 *
 * 1. Get median measurements from all values ​​of N.
 * This option will test the algorithm with the values ​​100, 1000, 10,000,
 * 100,000, 1,000,000 and 10,000,000. It will measure the median runtime
 * of both the sequential and parallel versions and print the measurements
 * to the terminal. This also includes speedup.
 *
 * 2. Test program
 * This option runs a couple of program tests that test that the parallel
 * algorithm is working properly. The first test checks that the sorting has been
 * done correctly. The second test checks that the sequential and the parallel
 * version produce the same output.
 *
 * 3. Print parallel sorted array for 10 number.
 * This option is suitable if you want to check the output for sorting an array
 * by printing it to the terminal. In order for it to be a suitable option,
 * there is only a small default array set to 10 numbers that is to be sortet,
 * but this can be changed in the code if you want to check a larger array.
 *
 */

class TestParallelMergesort{

  static int n;
  static int mode;
  static int k = Runtime.getRuntime().availableProcessors();
  static int seed = 321;
  static int runs = 9;

  public static void main(String[] args) {

    //Modes for method timeMeasurements
    int timeMedian = 2;

    // Present menu and choose option
    Scanner input = new Scanner(System.in);
    showMenu();
    System.out.print("\nOption: ");
    int mode = input.nextInt();
    System.out.println();

    // Run chosen option
    switch (mode) {
      case 1:
        System.out.print("\nChoose number of threads: ");
        k = input.nextInt();
        timeMeasurementsForAllN(timeMedian);
        break;
      case 2:
        testProgram();
        break;
      case 3:
        print();
        break;
      default:
        System.out.println("Mode does not exist");
        break;
    }
}



  //Menu presented when user run the program
  public static void showMenu(){
    System.out.println("\n**** MENU ****");
    System.out.println("‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾");
    System.out.println("1. Get median measurements for all values of N");
    System.out.println("2. Test program");
    System.out.println("3. Print parallel sorted array for 10 number");
  }


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
      sort2.parallelMergesort(array2, k);
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
    System.out.println("N = " + n + " | Runs = " + runs + " | Threads = " + k);
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



  // Run program tests and print results to terminal
  public static void testProgram(){
    System.out.println("      **** TEST PROGRAM ****   ");
    System.out.println("‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾");
    boolean compare = compareSeqAndPara();
    if(!compare){
      System.out.println("Compare test failed");
      System.out.println(" - The sequentual and parallel version does not produce the same output");
    }

    else{
      System.out.println("Compare test passed");
    }

    boolean order = testCorrectSorting();
    if(!order){
      System.out.println("Sorting test failed");
      System.out.println(" - Parallel Mergesort does not sort in correct order");
    }

    else{
      System.out.println("Sorting test passed");
    }

    if(compare && order){
      System.out.println();
      System.out.println("All program tests passed!");
    }
    System.out.println("‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾");



  }


  // Compare sequential and parallel version
  public static boolean compareSeqAndPara(){

    int [] array = randomArray(n, seed);
    Mergesort sort = new Mergesort();
    sort.mergeSort(array,array.length);

    int [] array2 = randomArray(n, seed);
    ParallelMergesort sort2 = new ParallelMergesort();
    sort2.parallelMergesort(array2, k);

    for(int i = 0; i < array.length; i++){
      if(array[i] != array2[i]){
        return false;
      }
    }

    return true;
  }



  // Check that sorting is done correctly
  public static boolean testCorrectSorting(){

    int [] array = randomArray(n, seed);
    ParallelMergesort sort2 = new ParallelMergesort();
    sort2.parallelMergesort(array, k);

    int [] array2 = randomArray(n, seed);
    Arrays.sort(array2);

    for(int i = 0; i < array.length; i++){
      if(array[i] != array2[i]){
        return false;
      }
    }

    return true;
  }



  // Print parallel sorted array
  public static void print(){
    System.out.println("      **** PRINT SORTED ARRAY ****   ");
    System.out.println("‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾");

    int [] array = randomArray(10, 10);
    ParallelMergesort sort2 = new ParallelMergesort();
    sort2.parallelMergesort(array, k);

    for(int i = 0; i < array.length; i++){
      System.out.println(array[i]);
    }
    System.out.println("‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾");

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
