import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * There are two different ways to initiate all Image objects:
 * if we run this program at first time, just read all image
 * files which belong to an input directory, then creates
 * object for each of them; since then metadata.txt will be
 * generated, so this program can read metadata.txt for
 * creating objects directly.
 */
class ImageInitializer {
    private String directoryPath;
    private File metaData;
    private int imagesNumber;
    ArrayList<Image> images; // list that contains all Image objects.

    /**
     * @param directoryPath is the path of our input image directory.
     * @param imagesNumber  we need to know grossly about how many images
     *                      in the image directory, to avoid extending
     *                      the capacity of the list which holds all image
     *                      objects too many times.
     */
    ImageInitializer(String directoryPath, int imagesNumber) {
        this.directoryPath = directoryPath;
        this.imagesNumber = imagesNumber;
    }

    /**
     * Reads from a file which contains all images' information
     *
     * @param metaData     is metadata.txt
     * @param imagesNumber ia the same stuff as the first constructor
     */
    ImageInitializer(File metaData, int imagesNumber) {
        this.metaData = metaData;
        this.imagesNumber = imagesNumber;
    }

    // Initiate the images list, then read images from a directory.
    void readImageDirectory() {
        images = new ArrayList<>(imagesNumber);
        File directory = new File(directoryPath);

        readImages(directory);
    }

    /**
     * Reads input directory to add new Image objects.
     *
     * @param directory will be a directory which contains images.
     */
    private void readImages(File directory) {
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {

                if (file.isDirectory())
                    readImages(file);
                else if (isImage(file))
                    images.add(new Image(file.getAbsolutePath()));
            }
        }
    }

    /**
     * Here we implement a method to determinate if is a jpg picture,
     * it will not work for many cases but it's ok for our case.
     *
     * @param file input file
     * @return boolean
     */
    private boolean isImage(File file) {
        String path = file.getAbsolutePath();
        String subString = path.substring(path.length() - 4);

        return subString.equals(".jpg") || subString.equals(".JPG");
    }

    // Creates Image objects
    void readMetaData() throws IOException {
        images = new ArrayList<>(imagesNumber);
        BufferedReader br = new BufferedReader(new FileReader(metaData));

        String str;
        while ((str = br.readLine()) != null)
            images.add(parseImage(str));

    }

    // Parses a string to a Image object
    private Image parseImage(String str) {
        String[] imageAttributes = str.split(",");

        int red = Integer.parseInt(imageAttributes[0].trim());
        int green = Integer.parseInt(imageAttributes[1].trim());
        int blue = Integer.parseInt(imageAttributes[2].trim());
        int norm = Integer.parseInt(imageAttributes[3].trim());
        String path = imageAttributes[4].trim();

        return new Image(red, green, blue, norm, path);
    }
}
