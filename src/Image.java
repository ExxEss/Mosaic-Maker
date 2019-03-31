/**
 * Class that holds the information that we
 * care about.
 */
class Image implements Comparable<Image> {
    int red, green, blue;
    int norm;
    String path;

    /**
     * Constructs a Image object only with a file path.
     *
     * @param path is the file path.
     */
    Image(String path) {
        this.path = path;
    }

    /**
     * Constructs Image object with three colors.
     *
     * @param red   ..
     * @param green ..
     * @param blue  ..
     */
    Image(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.norm = red * red + green * green + blue * blue;
    }

    /**
     * When we have a file named metadata.txt, we need to
     * initiate objects with the following attributes.
     *
     * @param red   ..
     * @param green ..
     * @param blue  ..
     * @param norm  ..
     * @param path  ..
     */
    Image(int red, int green, int blue,
          int norm, String path) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.norm = norm;
        this.path = path;
    }

    // Euclidean distance
    void calculateNorm() {
        norm = red * red + green * green + blue * blue;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (getClass() != obj.getClass())
            return false;

        if (obj instanceof Image) {
            Image Image = (Image) obj;

            // If two Image objects have same colors,
            // they will be considered as the same objects.
            return this.red == Image.red &&
                    this.green == Image.green &&
                    this.blue == Image.blue;
        }
        return false;
    }

    @Override
    public int compareTo(Image obj) {

        // A Image object is "bigger" than another if it's norm is bigger.
        return Integer.compare(this.norm, obj.norm);
    }
}
