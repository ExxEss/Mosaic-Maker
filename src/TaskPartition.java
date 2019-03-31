import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * Because of the usage of multithreading, a proper
 * partition of all the task is needed, this class divides
 * whole images list into several parts, a single
 * thread only deal with one of them.
 */
class TaskPartition {
    private int threadsNumber;
    private ArrayList<Image> images;

    /**
     * Constructs a TaskPartition object with the number of threads,
     * and list of Image objects.
     *
     * @param threadsNumber is the number of threads to create.
     * @param images        is list of Image object.
     */
    TaskPartition(int threadsNumber, ArrayList<Image> images) {
        this.threadsNumber = threadsNumber;
        this.images = images;
    }

    /**
     * This method will divide whole Image list into the number
     * of threads parts, then creates a bunch of threads to
     * deal with each part, and we need to divide the list as
     * evenly as we can.
     */
    void partition() {
        CountDownLatch latch = new CountDownLatch(threadsNumber);
        Thread[] threads = new Thread[threadsNumber];

        int averageSize = images.size() / threadsNumber;
        int rest = images.size() % threadsNumber;

        for (int i = 0; i < threadsNumber; i++) {
            // The first part threads will get one more
            // Image object than the rest
            if (i < rest)
                threads[i] = new Thread(
                        new ImageProcessor(latch, images,
                                i * (averageSize + 1),
                                (i + 1) * (averageSize + 1)));
            else
                threads[i] = new Thread(
                        new ImageProcessor(latch, images,
                                i * averageSize + rest,
                                (i + 1) * averageSize + rest));
        }

        for (Thread thread : threads)
            thread.start(); // Start to execute

        try {
            latch.await(); // Interrupt the main thread
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
