/* Name: Chaim Averbach
 * ID: 207486473
 */

package Exe.EX3;

import java.awt.Color;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Timeout.ThreadMode;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(OrderAnnotation.class)
public class MyMap2DTest {
    public static final int WHITE  = Color.WHITE.getRGB();   // precalculated colors for our tests, these are all the legal colors
    public static final int BLACK  = Color.BLACK.getRGB();
    public static final int BLUE   = Color.BLUE.getRGB();
    public static final int RED    = Color.RED.getRGB();
    public static final int YELLOW = Color.YELLOW.getRGB();
    public static final int GREEN  = Color.GREEN.getRGB();

    private static final int numberOfTests = 1000;   // a value to determine the number of random tests to perform in some functions
    private static final int timeoutFactor = 1;      // test time can depend on the machine, if they all timeout this number can be changed to allow more time on slower machines
    // default value was determined on my machine
    // TODO; benchmark the machine and automatically set timeoutFactor?
    public final String[] originalEncodedMap = { 
        "WWWWWWWWLL",
        "WWWWBWWWRW",
        "WWWWWWWWWW",
        "WRRWWWWWBG",
        "WWWWWWWWLL",
        "WBRBRWWWYW",
        "WWWYGWWWLY",
        "WRLWWWWWGG",
        "WWLLLWWWWW",
        "WWWYGWBWWW",
    };
    private Map2D premadeMap;

    private static Point2D randPoint(double width, double height, Random rnd) {
        // double minX = -0.5, maxX = width - 0.5, minY = -0.5, maxY = height - 0.5;  // the bounds, taken from the bounds created by the scale in the GUI, not actually used in code so commented out
        double x = (rnd.nextDouble() * width) - 0.5, y = (rnd.nextDouble() * height) - 0.5;
        return new Point2D(x, y);
    }
    private static Point2D randPoint(double size, Random rnd) {
        return randPoint(size, size, rnd);
    }
    private static Point2D randPoint(double size) {
        return randPoint(size, size, new Random(System.nanoTime()));
    }
    private static int[] allColors = { WHITE, BLACK, BLUE, RED, YELLOW, GREEN };

    /**
     * This function creates a random map
     * @param width the required width of the map
     * @param height the required height of the map
     * @param acceptableColors an array of the colors allowed in the map
     * @param whitePercentage the chance for any pixel to be white instead of a random color (setting this to 0 doesn't mean no white, it means no more white than other acceptable colors)
     * @return the generated map
     */
    private static MyMap2D randMap(int width, int height, int[] acceptableColors, double whitePercentage) {
        Random rnd = new Random(System.nanoTime());
        if (whitePercentage < 0 || whitePercentage > 1) whitePercentage = 0;  // if white percentage is an illegal number we default it to 0
        MyMap2D result = new MyMap2D(width, height);   // initialize the result map with the required side lengths
        for (int x = 0; x < width; ++x)                // iterate on the pixels
            for (int y= 0; y < width; ++y) {
                if (rnd.nextDouble() < whitePercentage)   // the chance of  a random number in (0, 1) to be less than whitePercentage is exactly whitePercentage
                    result.setPixel(x, y, WHITE);      // so we set the pixel to white
                else result.setPixel(x, y, acceptableColors[rnd.nextInt(acceptableColors.length)]);  // otherwise, we set the pixel to a random color from acceptableColors
            }
        return result;   // return the result
    }
    private static MyMap2D randMap(int width, int height, int[] acceptableColors) {
        return randMap(width, height, acceptableColors, 0);
    }
    private static MyMap2D randMap(int width, int height, double whitePercentage) {
        return randMap(width, height, allColors, whitePercentage);
    }
    private static MyMap2D randMap(int width, int height) {
        return randMap(width, height, allColors, 0);
    }
    private static MyMap2D randMap(int sideLength) {
        return randMap(sideLength, sideLength, allColors, 0);
    }

    static private char encodePixel(int pix) {   // this function encoded an int representing a color as a character
        if(pix == WHITE)     // this is essentially a mapping of colors (or rather their numeric RGB representation) to a character
            return 'W';      // I would do this with a switch case but the color constants are not computed in compile-time
        if(pix == BLACK)
            return 'B';
        if(pix == BLUE)
            return 'L';
        if(pix == RED)
            return 'R';
        if(pix == YELLOW)
            return 'Y';
        if(pix == GREEN)
            return 'G';
        assert false : "Illegal color in map";   // if we reached here and didn't return before the map had a color which is not allowed
        return '\0';   // this is only required so Java doesn't think the function has a branch that does not return a result
    }
    static private int decodePixel(char pix) {   // this is the inverse of encodePixel
        switch (pix) {            // we switch on the pixel character
            case 'W':             // in each case (for a legal character encoding)
                return WHITE;     // we return that number constant
            case 'B':
                return BLACK;
            case 'L':
                return BLUE;
            case 'R':
                return RED;
            case 'Y':
                return YELLOW;
            case 'G':
                return GREEN;
            default:              // the default means illegal character
                assert false : "Illegal color encoding";
                return 0;   // this is only required so Java doesn't think the function has a branch that does not return a result
        }
    }
    static private String[] encodeMap(Map2D map) {      // this functions encodes a map into an array of strings, each representing a row, for easier comparison using JUnit methods
        int w = map.getWidth(), h = map.getHeight();    // we get the map dimensions
        String[] result = new String[w];                // initialize the result string array
        Arrays.fill(result, "");                        // we fill the array with empty strings so that we can then concatenate on them (they are initialized null)
        for (int x = 0; x < w; ++x)                     // iterate on the columns
            for (int y = 0; y < h; ++y)                 // iterate on the rows
                result[y] += encodePixel(map.getPixel(x, y));  // add the encoding of the current pixel to the correct row array
        return result;                                  // return the encoded result
    }
    static private Map2D decodeMap(String[] rows) {     // this function is the inverse of encodeMap, for easier initialization of maps
        int w = rows.length, h = rows[0].length();      // get the size of the array and the length of the first string (we are kind of treating it as a two-dimensional array of chars)
        for (String row : rows)                         // iterate on the strings
            assert row.length() == h : "Cannot decode strings of different lengths into a map"; // make sure it is the same length as the first string, a jagged array is not a legal map encoding
        assert w == h : "Cannot decode non-square map"; // make sure the encoded map is square, otherwise it is not a legal map
        Map2D result = new MyMap2D(w, h);               // initialize a result map
        for (int x = 0; x < w; ++x)                     // iterate on the pixels
            for (int y = 0; y < h; ++y)
                result.setPixel(x, y, decodePixel(rows[y].charAt(x)));  // set the current pixel as the x char of the y string (y string is the row, x char of it is the correct column)
        return result;                                  // return the decoded result
    }

    @BeforeEach
    public void setUp() {    // this functions set ups the tests by resetting the map
        premadeMap = decodeMap(originalEncodedMap.clone());  // decode the encoded original map to reset to it
    }

    @Test
    @Order(1)    // all other tests depend on the functionalities tested here, so we check them first
    @Timeout(value = timeoutFactor * numberOfTests * 2, unit = TimeUnit.MILLISECONDS, threadMode = ThreadMode.SEPARATE_THREAD)   // I had to read to read a long GitHub issue and look at the actual commit that implemented this feature in the junit5 repo to find how to make this work
    public void testEncodeDecode() { // this tests the encoding and decoding process // TODO: test on random maps
        assertArrayEquals(originalEncodedMap, encodeMap(premadeMap));   // make sure encoding the decoded map gives the original map

        Random rnd = new Random(System.nanoTime());
        for (int i = 0; i < numberOfTests; ++i) {
            int sideLength = rnd.nextInt(200) + 1;  // generate a random side length up to 200
            MyMap2D randMap = randMap(sideLength);
            String[] encoding = encodeMap(randMap);
            assertEquals(randMap, decodeMap(encoding));
        }
    }
    private boolean isTriangleLine(Point2D p1, Point2D p2, Point2D p3) {  // this function checks if 3 points are very close to being on the same line
        double x1 = p1.x(), y1 = p1.y(), x2 = p2.x(), y2 = p2.y(), x3 = p3.ix(), y3 = p3.iy();  // preparing the numbers for the formula
        double dx13 = x1 - x3, dx21 = x2 - x1, dy13 = y1 - y3, dy21 = y2 - y1, dx23 = x2 - x3, dy23 = y2 - y3;      // some of the deltas we will need
        double d12_3 = (Math.abs(dx21 * dy13 - dx13 * dy21)) / (Math.sqrt(dx21 * dx21 + dy21 * dy21)); // caluculate the distance between the p3 and the line p1-p2 using https://en.wikipedia.org/wiki/Distance_from_a_point_to_a_line#Line_defined_by_two_points
        double d13_2 = (Math.abs(dx13 * dy21 - dx21 * dy13)) / (Math.sqrt(dx13 * dx13 + dy13 * dy13)); //
        double d32_1 = (Math.abs(dx23 * dy13 - dx13 * dy23)) / (Math.sqrt(dx23 * dx23 + dy23 * dy23));
        return ((d12_3 <= 2) || (d13_2 <= 2) || (d32_1 <= 2));
    }
    @Test
    @Timeout(value = timeoutFactor * numberOfTests, unit = TimeUnit.MILLISECONDS, threadMode = ThreadMode.SEPARATE_THREAD)
    public void testDrawSegment() {
        Random rnd = new Random(System.nanoTime());                // create a new random generator and seed it with the time
        for (int i = 0; i < numberOfTests; ++i) {           // run the test according to required number of times
            int size = rnd.nextInt(200) + 20;               // get a decent random size for the map
            MyMap2D map = randMap(size); // set up a random map
            map.fill(WHITE);             // fill the map with white
            Point2D p1 = randPoint(size, rnd), p2 = randPoint(size, rnd), p3 = randPoint(size, rnd);  // set up 3 random points to draw a triangle, we pass rnd to them because they might be initialized in the same nanosecond
            map.drawSegment(p1, p2, BLACK);  // draw 1 line on the map, we will now test it for incorrect pixels
            assertEquals(BLACK, map.getPixel(p1), "p1 was not colored by drawSegment");     // make sure the actual points were colored
            assertEquals(BLACK, map.getPixel(p2), "p2 was not colored by drawSegment");
            long minX = Math.round(Math.min(p1.x(), p2.x())),
                 minY = Math.round(Math.min(p1.y(), p2.y())),
                 maxX = Math.round(Math.max(p1.x(), p2.x())),
                 maxY = Math.round(Math.max(p1.y(), p2.y()));  // these are the limits of where our line is allowed to be
            for (int x = 0; x < size; ++x)          // iterate on all the pixels in the map
                for (int y = 0; y < size; ++y)
                    if (map.getPixel(x, y) == BLACK) { // if the pixel is black, it is part of our segment, we need to make sure that's correct
                        double x0 = x, y0 = y, x1 = p1.x(), y1 = p1.y(), x2 = p2.x(), y2 = p2.y();  // preparing the numbers for the formula
                        double dx10 = x1 - x0, dx21 = x2 - x1, dy10 = y1 - y0, dy21 = y2 - y1;      // some of the deltas we will need
                        double distance = (Math.abs(dx21 * dy10 - dx10 * dy21)) / (Math.sqrt(dx21 * dx21 + dy21 * dy21)); // caluculate the distance between the pixel and the line using https://en.wikipedia.org/wiki/Distance_from_a_point_to_a_line#Line_defined_by_two_points
                        assert distance < 1 : "drawSegment colored point too far from the line";
                        assert ((x >= minX) && (x <= maxX) && (y >= minY) && (y <= maxY)) : "drawSegment colored point outside of bounded array";
                    }
            if (isTriangleLine(p1, p2, p3)) continue;  // if the triangle is actually a line it is meaningless to try to fill it
            map.drawSegment(p1, p3, BLACK); // draw the other 2 sides of the triangle in black
            map.drawSegment(p2, p3, BLACK);
            Point2D midpoint = new Point2D((p1.x() + p2.x() + p3.x()) / 3, (p1.y() + p2.y() + p3.y()) / 3); // find the midpoint, guaranteed to be inside the triangle
            map.fill(midpoint, YELLOW);        // fill the inside of the triangle with blue
            assert (map.getPixel(0, 0) == WHITE)                 // this asserts that the corners are not all colored
                  || (map.getPixel(0, size - 1) == WHITE)        // if they are, the filling escaped the triangle
                  || (map.getPixel(size - 1, 0) == WHITE)        // (they can't all be black because we drew a triangle)
                  || (map.getPixel(size - 1, size - 1) == WHITE) // some might be black, some might even be yellow (if the filling was ON the triangle)
                  : "Filling escaped segment triangle, segments are holey";      
        }
    }
    @Test
    @Timeout(value = timeoutFactor * numberOfTests, unit = TimeUnit.MILLISECONDS, threadMode = ThreadMode.SEPARATE_THREAD)
    public void testDrawRect() {
        Random rnd = new Random(System.nanoTime());                // create a new random generator and seed it with the time
        for (int i = 0; i < numberOfTests; ++i) {        // we perform numberOfTests random tests
            int size = rnd.nextInt(200) + 20;               // get a decent random size for the map
            MyMap2D map = randMap(size);  // set up a random map
            map.fill(WHITE);              // fill the map with white
            Point2D p1 = randPoint(size, rnd), p2 = randPoint(size, rnd);  // set up 2 random points to draw a rectangle, we pass rnd to them because they might be initialized in the same nanosecond
            map.drawRect(p1, p2, BLACK);
            long minX = Math.round(Math.min(p1.x(), p2.x())),
                 minY = Math.round(Math.min(p1.y(), p2.y())),
                 maxX = Math.round(Math.max(p1.x(), p2.x())),
                 maxY = Math.round(Math.max(p1.y(), p2.y()));  // these are the limits of where the rectangle should be is allowed to be
            for (int x = 0; x < size; ++x)               // iterate on the pixels to check no wrong pixels
                for (int y = 0; y < size; ++y) {
                    if (map.getPixel(x, y) == BLACK) {   // if the pixel is black
                        assert ((x >= minX) && (x <= maxX) && (y >= minY) && (y <= maxY)) : "drawRect colored point outside of bounded array";  // make sure we are within bound
                    } else {                             // if it is not
                        assert ((x < minX) || (x > maxX) || (y < minY) || (y > maxY)) : "drawRect left hole inside boundaries";   // make sure we are outside the bounds
                    }
                }
        }

        Point2D p1 = new Point2D(3.3, 4.8), p2 = new Point2D(7, 9);    // set points for deterministic test
        premadeMap.drawRect(p1, p2, BLACK);                // drawing the rectangle on the premade map
        String[] expected = {           // this is what we expect after the drawing
            "WWWWWWWWLL",
            "WWWWBWWWRW",
            "WWWWWWWWWW",
            "WRRWWWWWBG",
            "WWWWWWWWLL",
            "WBRBBBBBYW",
            "WWWBBBBBLY",
            "WRLBBBBBGG",
            "WWLBBBBBWW",
            "WWWBBBBBWW",
        };
        assertArrayEquals(expected, encodeMap(premadeMap));   // make sure we got what we expected
    }
    @Test
    @Timeout(value = timeoutFactor * numberOfTests * 2, unit = TimeUnit.MILLISECONDS, threadMode = ThreadMode.SEPARATE_THREAD)
    public void testDrawCircle() {   // TODO: make sure the circle has no holes, make sure no (out of bounds) errors
        Random rnd = new Random(System.nanoTime());                // create a new random generator and seed it with the time
        for (int i = 0; i < numberOfTests; ++i) {        // we perform numberOfTests random tests
            int size = rnd.nextInt(200) + 20;               // get a decent random size for the map
            MyMap2D map = randMap(size);  // set up a random map
            map.fill(WHITE);              // fill the map with white
            Point2D center = randPoint(size);  // set up a random point as the center
            double radius = rnd.nextDouble() * size;   // get a random radius up to the size of the map
            map.drawCircle(center, radius, YELLOW);        // draw the circle
            map.fill(center, BLACK);                       // we fill the circle to make sure it is all connected
            long minX = (long) Math.floor(center.x() - radius),
                 minY = (long)  Math.ceil(center.y() - radius),
                 maxX = (long) Math.floor(center.x() + radius),
                 maxY = (long)  Math.ceil(center.y() + radius);  // these are the limits of where the rectangle should be is allowed to be
            for (int x = 0; x < size; ++x)               // iterate on the pixels to check no wrong pixels
                for (int y = 0; y < size; ++y) {
                    int color = map.getPixel(x, y);
                    if ((color == BLACK) && radius >= 1) {         // if the pixel is black and the radius is greater than 1 (otherwise could be a circle with no pixels)
                        assert ((x >= minX) && (x <= maxX) && (y >= minY) && (y <= maxY)) : "drawCircle colored point outside of bounded array";  // make sure we are within bound
                        assert center.distance(new Point2D(x, y)) < radius : "drawCircle colored point too far away from center"; // make sure the point is actually within the circle
                    } else if (color == WHITE) {     // if it is white
                        assert center.distance(new Point2D(x, y)) > radius : "drawCircle didn't color point inside circle"; // make sure the point is actually outside the circle
                    } else if (color == YELLOW) {    // if we found yellow point, it means it was part of the circle (so drawn in yellow) but not orthogonally connected to the center (so wasn't filled in black)
                        assert false : "Found disconnected point in circle";  
                    } else {    // any other color
                        assert false : "Unreachable"; // is impossible
                    }
                }
        }

        Point2D p1 = new Point2D(3.3, 4.8), p2 = new Point2D(5, 9);
        premadeMap.drawCircle(p1, 4, BLUE);
        premadeMap.drawCircle(p2, 2, YELLOW);
        String[] expected = {
            "WWWWWWWWLL",
            "WWWLLWWWRW",
            "WLLLLLLWWW",
            "LLLLLLLWBG", 
            "LLLLLLLLLL",
            "LLLLLLLLYW",
            "LLLLLLLLLY",
            "LLLLLLLWGG",
            "WLLLYYYWWW",
            "WWWYYYYWWW",
        };
        assertArrayEquals(expected, encodeMap(premadeMap));
    }
    @Test
    public void testFill() {   // TODO: make sure all the points were originally the same color, make sure the return value is the number of colored points, make sure there are paths to all colored points
        Point2D p = new Point2D(0, 0);
        premadeMap.fill(p, BLACK);
        String[] expected = {
            "BBBBBBBBLL",
            "BBBBBBBBRB",
            "BBBBBBBBBB",
            "BRRBBBBBBG",
            "BBBBBBBBLL",
            "BBRBRBBBYW",
            "BBBYGBBBLY",
            "BRLBBBBBGG",
            "BBLLLBBBBB",
            "BBBYGBBBBB",
        };
        assertArrayEquals(expected, encodeMap(premadeMap));
    }
    @Test
    public void testShortestPath() {    // TODO: make sure the length is the same (+1) as shortestPathDist, make sure all the points are the same color as the origin and destination, make sure the path is within the map
        Point2D p1 = new Point2D(2, 9), p2 = new Point2D(6, 7);
        Point2D[] path = premadeMap.shortestPath(p1, p2);
        for (Point2D p : path)
            premadeMap.setPixel(p, GREEN);
        String[] expected = {
            "WWWWWWWWLL",
            "WWWWBWWWRW",
            "WWWWWWWWWW",
            "WRRWWWWWBG",
            "GGGGGGWWLL",
            "GBRBRGWWYW",
            "GWWYGGWWLY",
            "GRLWWGGWGG",
            "GGLLLWWWWW",
            "WGGYGWBWWW",
        };
        assertArrayEquals(expected, encodeMap(premadeMap));
    }
    @Test
    public void testShortestPathDist() { // TODO: idea: make sure distance is the same from both directions, make sure there is no path to points outside, make sure there is no path between differently colored points
        Point2D p1 = new Point2D(2, 9), p2 = new Point2D(6, 7);
        int pathDist = premadeMap.shortestPathDist(p1, p2);
        assertEquals(16, pathDist);
    }
    @Test
    public void testNextGenGol() {
        premadeMap.nextGenGol();                             // evolve the default map one generation
        String[] expected = new String[] {            // the next generation from the default map
            "WWWWWWWWBB",
            "WWWWWWWWBB",
            "WWWWWWWWBB",
            "WWWWWWWWBB",
            "WWWWWWWBWW",
            "WWBWBWWBWW",
            "WWWWBWWBWW",
            "WBWWWWWWBB",
            "WBWWBBWWWW",
            "WWBWBBWWWW",
        };
        assertArrayEquals(expected, encodeMap(premadeMap));  // make sure the evolved array is as expected
        premadeMap.nextGenGol();                             // evolve another generation
        expected = new String[] {                     // the next expected generation
            "WWWWWWWWBB",
            "WWWWWWWBWW",
            "WWWWWWWBWW",
            "WWWWWWWBWB",
            "WWWWWWWBWW",
            "WWWBWWBBBW",
            "WWWBWWWBWW",
            "WWWWBBWWBW",
            "WBBBBBWWWW",
            "WWWBBBWWWW",
        };
        assertArrayEquals(expected, encodeMap(premadeMap)); // make sure it is correct again

        String[] stable = new String[] {       // created using patterns from https://conwaylife.com/wiki/Still_life
            "BBWWBBWBBW",
            "BBWWWBWBWW",
            "WWWWWBWBWW",
            "WWWWBBWBBW",
            "WWWWWWWWWW",
            "WBBWWWWWWW",
            "BWWBWWWWWW",
            "WBBWBWWWWW",
            "WWWWBWWWWW",
            "WWWWBBWWWW",
        };
        Map2D stableMap = decodeMap(stable);   // decode to start running GoL
        for (int i = 0; i < 1000; ++i) stableMap.nextGenGol();  // evolve 1000 times
        assertArrayEquals(stable, encodeMap(stableMap));  // make sure the stable patterns remain
    }
}