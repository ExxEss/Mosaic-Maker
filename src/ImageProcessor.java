import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * This class is for processing Image objects that contained
 * in list, because all these Image object don't have
 * red, green, blue and norm value, thus this class will help us
 * to calculate all the above.
 */
class ImageProcessor implements Runnable {
    private CountDownLatch countDownLatch;
    private ArrayList<Image> images;
    private int start;
    private int end;

    /**
     * @param countDownLatch is for multithreading, since we make several
     *                       sub-threads in our main thread, and main thread
     *                       can only be awaken until all sub-threads are finished,
     *                       if not, we will get wrong result because some of
     *                       sub-threads haven't finished yet, so CountDownLatch is
     *                       needed to count down the amount of sub-threads, when
     *                       it's value is 0, main thread will be awaken.
     * @param start          is the start index of a partition, we will divide whole list
     *                       into several parts, each part will be computed by a thread.
     * @param end            is the end index respectively.
     * @see CountDownLatch
     */
    ImageProcessor(CountDownLatch countDownLatch,
                   ArrayList<Image> images, int start, int end) {
        this.countDownLatch = countDownLatch;
        this.images = images;
        this.start = start;
        this.end = end;
    }


    @Override
    public void run() {

        calculateImagesNorm();

        // When current thread is finished, need to countdown the latch
        // to ensure that the main thread can be awaken when latch's value is 0.
        countDownLatch.countDown();
    }


    // Calculates norm of Image object contained by the image list.
    private void calculateImagesNorm() {

        for (int i = start; i < end; i++) {
            File image = new File(images.get(i).path);
            BufferedImage imageBuffer = openImage(image);

            if (imageBuffer == null)

                // Here we play a trick to avoid some
                // unavailable images are recorded.
                images.get(i).norm = -100;
            else
                modifyImagePixel(i, calculatePixel(openImage(image)));
        }
    }

    /**
     * Modifies Image object when we have average pixel value.
     *
     * @param position is the index of image list to modify.
     * @param rgb      average pixel value.
     */
    private void modifyImagePixel(int position, int rgb) {
        Image image = images.get(position);

        image.red = (rgb >> 16) & 0xFF;
        image.green = (rgb >> 8) & 0xFF;
        image.blue = rgb & 0xFF;

        image.calculateNorm();
    }


    /**
     * Opens a image file as a BufferedImage object.
     *
     * @param image is the File object of the image is to open.
     * @return BufferedImage object.
     */
    private BufferedImage openImage(File image) {
        BufferedImage imageBuffer = null;

        try {
            imageBuffer = ImageIO.read(image);
        } catch (IOException e) {

            // Some image i got can't be opened correctly,
            // maybe they are damaged by decompression,
            // so many IO exceptions will be thrown.
            System.out.println("This photo should be deleted: " +
                    image.getAbsolutePath());
            System.err.println("I/O Error");
            e.printStackTrace(System.err);
        }

        return imageBuffer;
    }

    /**
     * Calculates the average pixel value of the image.
     *
     * @param image BufferedImage
     * @return average pixel value.
     */
    private int calculatePixel(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int imageSize = width * height;

        int red = 0;
        int green = 0;
        int blue = 0;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = image.getRGB(i, j);

                red += (rgb >> 16) & 0xFF;
                green += (rgb >> 8) & 0xFF;
                blue += rgb & 0xFF;
            }
        }

        return (red / imageSize) << 16 |
                (green / imageSize) << 8 |
                blue / imageSize;
    }
}
