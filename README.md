# parallel-merge-sort
This program is done as a home exam in the course IN3030 - Efficient Parallel Programming. The program contains a sequential and a parallel version of the Merge Sort algorithm, as well as a description of how i did the implementation and measurements of runtime and speedup.

## User guide
-	 Compile program: "javac *.java"
-	Run program: "java TestParallelMergesort"
 
## Test Program
This is a test-program that tests the parallel version of Merge Sort and how it performs compared to the sequential version. To test the algorithm, the program will present a menu with three different options.

1.	Get median measurements from all values of N.
This option will test the algorithm with the values 100, 1000, 10,000, 100,000, 1,000,000 and 10,000,000. It will measure the median runtime of both the sequential and parallel versions and print the measurements to the terminal. This also includes speedup.
 
2.	Test program
This option runs a couple of program tests that test that the parallel algorithm is working properly. The first test checks that the sorting has been done correctly. The second test checks that the sequential and the parallel version produce the same output.
 
3.	Print parallel sorted array for 10 number.
This option is suitable if you want to check the output for sorting an array by printing it to the terminal. In order for it to be a suitable option, there is only a small default array set to 10 numbers that is to be sorted, but this can be changed in the code if you want to check a larger array.

## An in-depth description of how the program works
Like the sequential version, the parallel version will need an unsorted array to sort. In addition, the parallel version needs a maximum number of threads to perform the sorting. However, for the parallel version to be recursive, this variable will not be used in the same way as an iterative algorithm, but as a base case for when to stop the recursion. 

As in Merge Sort, the program starts by dividing the array in two parts and then copying the values from the input array into two separate arrays, one for the right side and the one for the left side. The array is simply divided in the middle, by finding the length of the array and then dividing it by 2, storing the value in a variable “mid”. 

Two threads are then created, one for the right array, and another for the left array. Each thread uses a Worker class that implements the Runnable interface. The Worker class will need two variables for the program to work, an array to sort and a thread-variable that determines when to stop the recursion. The array-argument is either the left part or the right part of the initial array. The maximum thread-variable is used as a base case when starting the recursion and is divided by 2 when initialized. 

When the threads start, the run-method in the Worker class is automatically called. The run method then calls the same method that initially started the two threads. This is where the recursion starts. 

Now each of the threads will go through the exact same procedure as described above. The only difference is that the initial array is now either the left array or the right array previously created, and the thread-variable is now half of what it was.

The two arrays will again be divided into a new right part and a new left part. Furthermore, each of them starts two new threads that do the exact same operations as the two previous threads. This means that for each step in the recursion twice as many threads will be created as the previous step, thus increasing the effect of parallelization.

We need a mechanism to stop the recursion, and thus decide when we are done sorting the array. For each step, the variable k (max number of threads) will be divided by 2. When k becomes less than or equal to 1, we have reached the base case of the method and the recursion stops. For example, if we have k = 8, we will have a development where

1.	Step: 8/2 = 4
2.	Step: 4/2 = 2
3.	Step: 2/2 = 1 (reached base case)

The number of threads created is then 21 + 22 + 23 = 2 + 4 + 8 = 14.
The threads will then be synchronized using join, and the arrays will be merged using the mergeSort method from the sequential algorithm. Finally, we are left with a parallel merge sorted array in ascending order. 
