import java.io.*;
import java.util.ArrayList;

/**
 * This class is used for generating a file that is named
 * metadata.txt which contains all images information.
 */
class FileGenerator {
    private String directoryPath;
    private ArrayList<Image> images;

    /**
     * @param directoryPath ..
     * @param images        ..
     */
    FileGenerator(String directoryPath,
                  ArrayList<Image> images) {
        this.directoryPath = directoryPath;
        this.images = images;
    }

    // Notice that only those Image objects which have
    // positive norm will be written into metadata.txt
    void generateFile() throws IOException {
        String fileName = "metadata.txt";

        FileWriter fileWriter = new FileWriter(
                new File(directoryPath + fileName));

        for (Image image : images) {
            if (image.norm > 0)
                writeImageData(fileWriter, image);
        }

        fileWriter.flush();  // To write into the file immediately
        fileWriter.close();
    }


    /**
     * Writes information of each image into metadata.txt
     *
     * @param fileWriter ..
     * @param image      Image object
     * @throws IOException ..
     */
    private void writeImageData(FileWriter fileWriter, Image image)
            throws IOException {
        String text = image.red + ", " + image.green + ", " + image.blue + ", "
                + image.norm + ", " + image.path + " \n";
        fileWriter.write(text);
    }

}
