// SumTask class for partial summing
class SumTask extends Thread {
    private int[] array;
    private int start, end;
    private int partialSum;

    // Constructor to initialize the array and the range (start, end) for the sum
    public SumTask(int[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
        this.partialSum = 0;
    }

    // Run method to compute the sum for the given range
    @Override
    public void run() {
        for (int i = start; i < end; i++) {
            partialSum += array[i];
        }
        System.out.println("Partial sum from processor (thread): " + partialSum);
    }

    // Getter for the partial sum
    public int getPartialSum() {
        return partialSum;
    }
}

// Main class to handle distributed sum
public class DistributedSum {
    public static void main(String[] args) {
        int N = 1000; // Size of the array
        int n = 4;    // Number of processors (threads)

        // Create and initialize an array of N elements with values 1 to N
        int[] array = new int[N];
        for (int i = 0; i < N; i++) {
            array[i] = i + 1;
        }

        // Calculate the size of each chunk
        int chunkSize = N / n;

        // Create threads for computing the partial sums
        SumTask[] tasks = new SumTask[n];
        for (int i = 0; i < n; i++) {
            int start = i * chunkSize;
            int end = (i == n - 1) ? N : (i + 1) * chunkSize; // Handle remainder in last thread
            tasks[i] = new SumTask(array, start, end);
        }

        // Start all threads
        for (int i = 0; i < n; i++) {
            tasks[i].start();
        }

        // Wait for all threads to complete and aggregate the results
        int totalSum = 0;
        for (int i = 0; i < n; i++) {
            try {
                tasks[i].join();
                totalSum += tasks[i].getPartialSum();
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted: " + e);
            }
        }

        // Output the final result
        System.out.println("Final total sum: " + totalSum);
    }
}
