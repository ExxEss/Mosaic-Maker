import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * This will be the pivotal class to create a mosaic image.
 */
class MosaicImage {
    private ImageFinder imageFinder;
    private String path;
    private final int mosaicWidth;
    private final int mosaicHeight;
    private final int size;

    /**
     * Constructor
     *
     * @param imageFinder  ..
     * @param path         ..
     * @param mosaicWidth  ..
     * @param mosaicHeight ..
     */
    MosaicImage(ImageFinder imageFinder, String path,
                int mosaicWidth, int mosaicHeight) {
        this.imageFinder = imageFinder;
        this.path = path;
        this.mosaicWidth = mosaicWidth;
        this.mosaicHeight = mosaicHeight;
        this.size = mosaicWidth * mosaicHeight;
    }

    // To make a beautiful shiny mosaic image
    void makeMosaicImage() throws IOException {
        File file = new File(path);
        BufferedImage imageBuffer = ImageIO.read(file);


        int width = imageBuffer.getWidth();
        int height = imageBuffer.getHeight();

        int sizeX = width / mosaicWidth;
        int sizeY = height / mosaicHeight;


        BufferedImage newImage = new BufferedImage(
                sizeX * mosaicWidth,
                sizeY * mosaicHeight,
                BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {

                int red = 0;
                int green = 0;
                int blue = 0;

                for (int m = i * mosaicWidth;
                     m < (i + 1) * mosaicWidth; m++) {
                    for (int n = j * mosaicHeight;
                         n < (j + 1) * mosaicHeight; n++) {

                        int rgb = imageBuffer.getRGB(m, n);

                        red += ((rgb >> 16) & 0xFF);
                        green += ((rgb >> 8) & 0xFF);
                        blue += (rgb & 0xFF);
                    }
                }

                Image image = imageFinder.searchImage(new Image(
                        red / size,
                        green / size,
                        blue / size));

                int[][][] reducedImage = reduceImage(image);

                writeNewImage(newImage, reducedImage, i, j);
            }
        }

        ImageIO.write(newImage, "BMP", new File("MosaicImage.bmp"));
    }

    /**
     * To reduce the size of a image to a new size
     * of mosaicWidth * mosaicHeight
     *
     * @param image ..
     * @return a images' pixels values
     * @throws IOException ..
     */
    private int[][][] reduceImage(Image image) throws IOException {
        int[][][] pixelArray = new int[mosaicWidth][mosaicHeight][3];
        BufferedImage imageBuffer = ImageIO.read(new File(image.path));

        if (imageBuffer == null)
            return pixelArray;

        int width = imageBuffer.getWidth();
        int height = imageBuffer.getHeight();

        int sizeX = width / mosaicWidth;
        int restX = width % mosaicWidth;

        int sizeY = height / mosaicHeight;
        int restY = height % mosaicHeight;

        for (int i = 0; i < mosaicWidth; i++) {
            for (int j = 0; j < mosaicHeight; j++) {
                if (i < restX && j < restY)

                    // To divide the image as uniform as we can,
                    // for example, if we want to divide 6 into 4 parts,
                    // we let first 2 parts have one more than the rest
                    // parts, we get 2, 2, 1, 1 as the result, maybe 
                    // is unnecessary and a little bit over-designed,
                    // but i like :)
                    pixelArray[i][j] = getAveragePixel(imageBuffer,
                            i * (sizeX + 1),
                            (i + 1) * (sizeX + 1),
                            j * (sizeY + 1),
                            (j + 1) * (sizeY + 1));

                if (i < restX && j >= restY)
                    pixelArray[i][j] = getAveragePixel(imageBuffer,
                            i * (sizeX + 1),
                            (i + 1) * (sizeX + 1),
                            j * sizeY + restY,
                            (j + 1) * sizeY + restY);

                if (i >= restX && j < restY)
                    pixelArray[i][j] = getAveragePixel(imageBuffer,
                            i * sizeX + restX,
                            (i + 1) * sizeX + restX,
                            j * (sizeY + 1),
                            (j + 1) * (sizeY + 1));

                if (i >= restX && j >= restY)
                    pixelArray[i][j] = getAveragePixel(imageBuffer,
                            i * sizeX + restX,
                            (i + 1) * sizeX + restX,
                            j * sizeY + restY,
                            (j + 1) * sizeY + restY);
            }
        }
        return pixelArray;
    }


    /**
     * Calculates the average value of all pixels within
     * a specific region
     *
     * @param imageBuffer ..
     * @param startX      ..
     * @param endX        ..
     * @param startY      ..
     * @param endY        ..
     * @return average value array
     */
    private int[] getAveragePixel(BufferedImage imageBuffer,
                                  int startX, int endX,
                                  int startY, int endY) {
        int red = 0;
        int green = 0;
        int blue = 0;
        int mosaicSize = (endX - startX) * (endY - startY);

        for (int i = startX; i < endX; i++) {
            for (int j = startY; j < endY; j++) {
                int pixel = imageBuffer.getRGB(i, j);

                red += ((pixel >> 16) & 0xFF);
                green += ((pixel >> 8) & 0xFF);
                blue += (pixel & 0xFF);
            }
        }
        return new int[]{red / mosaicSize,
                green / mosaicSize,
                blue / mosaicSize};
    }

    /**
     * Writes a reduced image into the new image's buffer
     *
     * @param newImage     ..
     * @param reducedImage ..
     * @param startX       ..
     * @param startY       ..
     */
    private void writeNewImage(BufferedImage newImage,
                               int[][][] reducedImage,
                               int startX, int startY) {

        int baseX = startX * mosaicWidth;
        int baseY = startY * mosaicHeight;

        for (int i = baseX; i < baseX + mosaicWidth; i++) {
            for (int j = baseY; j < baseY + mosaicHeight; j++) {
                int[] pixel = reducedImage[i - baseX][j - baseY];

                newImage.setRGB(i, j,
                        (pixel[0] << 16) |
                                (pixel[1] << 8) |
                                (pixel[2]));
            }
        }
    }

}