import java.util.ArrayList;

/**
 * Our mission is to find out which image in the list
 * is most similar to a mosaic square of our goal image,
 * so here a binary version searching is implemented.
 */
class ImageFinder {
    private ArrayList<Image> images;
    private int radius;

    /**
     * @param radius is the radius that we defined to search our
     *               goal image object.
     */
    ImageFinder(ArrayList<Image> images, int radius) {
        this.images = images;
        this.radius = radius;
    }

    /**
     * Compares two Image object to our goal, return the index
     * of the more closely one.
     *
     * @param first  list index
     * @param second list index
     * @param goal   Image object
     * @return list index.
     */
    private int compareNorm(int first, int second, Image goal) {
        return (Math.abs(images.get(first).norm - goal.norm)
                > Math.abs(images.get(second).norm - goal.norm))
                ? second
                : first;
    }

    /**
     * Same as the first compareNorm(), but return the Image
     * object rather than index.
     *
     * @param first  Image object
     * @param second Image object
     * @param goal   Image object
     * @return Image object.
     */
    private Image compareNorm(Image first,
                              Image second, Image goal) {
        return (normDistance(first, goal)
                < normDistance(second, goal))
                ? first
                : second;
    }

    /**
     * calculates the "phony" Euclidean distance between two rgb values,
     * it's "phony" because we don't apply sqrt to it just for better
     * performance and simplicity.
     *
     * @param first  Image object
     * @param second Image object
     * @return norm
     */
    private int normDistance(Image first, Image second) {
        return (first.red - second.red) * (first.red - second.red) +
                (first.green - second.green) * (first.green - second.green) +
                (first.blue - second.blue) * (first.blue - second.blue);
    }

    /**
     * This method is to find the goal Image object by applying binary search,
     * with time complexity about log(N), which N is list's size.
     *
     * @param goal is the goal image object we want to find in
     *             list of images, if there is no object that
     *             has the same norm in the list, this method will
     *             return the index of the most closely one.
     * @return list index
     */
    private int binarySearch(Image goal) {
        int len = images.size();
        int left = 0;
        int right = len - 1;

        if (len == 0)
            return -1;

        int tmp;
        while (left != right) {
            if (images.get(left) == goal)
                return left;

            if (images.get(right) == goal)
                return right;

            if (images.get(left).compareTo(goal) < 0) {
                tmp = (right - left) / 2 + left;  // Reduces searching range

                if (left == tmp)
                    return compareNorm(left, right, goal);

                if (images.get(tmp) == goal)
                    return tmp;
                else if (images.get(tmp).compareTo(goal) < 0)
                    left = tmp;
                else
                    right = tmp;

            } else {
                return left;
            }
        }
        return left;
    }

    /**
     * Uses method binarySearch() to find the searching "center",
     * then find out the best result under the defined radius.
     *
     * @param goal Image object
     * @return Image object
     */
    Image searchImage(Image goal) {
        return searchImage(goal, binarySearch(goal));
    }

    /**
     * Once we get the index of the most "closely" Image object in the
     * list, here "closely" refer to the norm rather than Euclidean
     * distance, now we have to compare it and its' neighbors with the goal,
     * using Euclidean distance.
     * Image object is a neighbor only when the difference of it's index with
     * the start index is smaller than the radius we defined. Finally returns
     * the most closely object's index.
     *
     * @param goal  Image object
     * @param start is the center index for search.
     * @return list index
     */
    private Image searchImage(Image goal, int start) {
        int currentIndex = start;
        int count = radius;
        Image result = images.get(start);

        // Compares towards "left"
        while (currentIndex > -1 && count > 0) {
            result = compareNorm(images.get(currentIndex), result, goal);
            currentIndex--;
            count--;
        }

        count = radius;
        currentIndex = start;

        // Then towards "right"
        while (currentIndex < images.size() && count > 0) {
            result = compareNorm(images.get(currentIndex), result, goal);
            currentIndex++;
            count--;
        }

        return result;
    }
}
