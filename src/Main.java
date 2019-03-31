import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.util.Collections.sort;

/**
 * @author Yuguo Xie
 * @version 0.1
 */
class Main {
    public static Class<?> clazz;
    private final static int initialCapacity = 70000;
    private final static int threadsNumber = 10;
    private final static int radius = 200;

    private static File
    MakeMyBeautifulMosaicImageAtFirstTime(String directoryPath) {
        File[] files = new File(directoryPath).listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.getAbsolutePath().equals(
                        directoryPath + "/metadata.txt"))
                    return file;
            }
        }
        return null;
    }

    private static void init(String directoryPath, String image) throws IOException {
        ImageInitializer pi;
        File metaData = MakeMyBeautifulMosaicImageAtFirstTime(directoryPath);

        if (metaData == null) {
            pi = new ImageInitializer(directoryPath, initialCapacity);
            pi.readImageDirectory();

            TaskPartition tp = new TaskPartition(threadsNumber, pi.images);

            tp.partition();

            sort(pi.images); // Sort all Image object

            FileGenerator fg = new FileGenerator(directoryPath + "/", pi.images);
            fg.generateFile();
        } else {
            pi = new ImageInitializer(metaData, initialCapacity);
            pi.readMetaData();
        }

        ImageFinder pf = new ImageFinder(pi.images, radius);
        MosaicImage mosaicImage = new MosaicImage(pf, image, 20, 20);
        mosaicImage.makeMosaicImage();
    }

    public static void main(String[] args) throws IOException {
        clazz = int.class;
        String directoryPath;
        String imagePath;
        BufferedReader bufferedReader;

        bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Choose a image directory: ");
        directoryPath = bufferedReader.readLine();

        System.out.println("Choose a image to process: ");
        imagePath = bufferedReader.readLine();

        init(directoryPath, imagePath);
    }
}
