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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(OrderAnnotation.class)
public class MyMap2DTest {
    public static final int WHITE  = Color.WHITE.getRGB();   // precalculated colors for our tests, these are all the legal colors
    public static final int BLACK  = Color.BLACK.getRGB();
    public static final int BLUE   = Color.BLUE.getRGB();
    public static final int RED    = Color.RED.getRGB();
    public static final int YELLOW = Color.YELLOW.getRGB();
    public static final int GREEN  = Color.GREEN.getRGB();

    private static final int numberOfTests = 1000;   // a value to determine the number of random tests to perform in some functions, low number is more likely to miss fails but is faster
    // if you set this below about 200 you should increase timeoutFactor because overheads and linear times will make you timeout
    private static final int timeoutFactor = 1000;   // test time can depend on the machine, if they all timeout this number should be changed to allow more time on slower machines
    // running this file as a main class runs a benchmark and prints the result, this result can be used as a timeoutFactor (I can't guarantee that this timeoutFactor will not timeout, but it's a decent approximation)
    // 1000 is set as default since it works well for my machine in the nominal case (in battery saver mode it is about 1600)
    // some of the slower tests also have a constant factor on them

    public final String[] originalEncodedMap = {  // a premade encoded map for deterministic tests
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

    private static Point2D randPoint(double width, double height, Random rnd) {       // this function gives a random point, take a Random to make sure points which are generated one after another are not the same
        // double minX = -0.5, maxX = width - 0.5, minY = -0.5, maxY = height - 0.5;  // the bounds, taken from the bounds created by the scale in the GUI, not actually used in code so commented out
        double x = (rnd.nextDouble() * width) - 0.5, y = (rnd.nextDouble() * height) - 0.5;  // the values of the point, after applying the bounds
        return new Point2D(x, y);      // return the point with the randomized values
    }
    private static final int[] allColors = { WHITE, BLACK, BLUE, RED, YELLOW, GREEN };      // literal array for all legal colors, to allow it as an implicit option when randomizing map

    /**
     * This function creates a random map
     * @param width the required width of the map
     * @param height the required height of the map
     * @param acceptableColors an array of the colors allowed in the map
     * @param whitePercentage the chance for any pixel to be white instead of a random color (setting this to 0 doesn't mean no white, it means no more white than other acceptable colors)
     * @return the generated map
     */
    private static MyMap2D randMap(int width, int height, int[] acceptableColors, double whitePercentage) {
        Random rnd = new Random(System.nanoTime());    // initialize the randomizer
        if (whitePercentage < 0 || whitePercentage > 1) whitePercentage = 0;  // if white percentage is an illegal number we default it to 0
        MyMap2D result = new MyMap2D(width, height);   // initialize the result map with the required side lengths
        for (int x = 0; x < width; ++x)                // iterate on the pixels
            for (int y= 0; y < height; ++y) {
                if (rnd.nextDouble() < whitePercentage)   // the chance of  a random number in (0, 1) to be less than whitePercentage is exactly whitePercentage
                    result.setPixel(x, y, WHITE);         // so we set the pixel to white
                else result.setPixel(x, y, acceptableColors[rnd.nextInt(acceptableColors.length)]);  // otherwise, we set the pixel to a random color from acceptableColors
            }
        return result;   // return the result
    }
    private static MyMap2D randMap(int w, int h) {             // same function with implicit colors and white percentage
        return randMap(w, h, allColors, 0);
    }

    static private char encodePixel(int pix) {   // this function encodes an int representing a color as a character
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
        assert false : "Illegal color in map";   // if we reached here and didn't return before the pixel was not a legal color
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
    static private String[] encodeMap(Map2D map) {      // this function encodes a map into an array of strings, each representing a row, for easier comparison using JUnit methods
        int w = map.getWidth(), h = map.getHeight();    // we get the map dimensions
        String[] result = new String[h];                // initialize the result string array
        Arrays.fill(result, "");                        // we fill the array with empty strings so that we can then concatenate on them (they were initialized null)
        for (int x = 0; x < w; ++x)                     // iterate on the columns
            for (int y = 0; y < h; ++y)                 // iterate on the rows
                result[y] += encodePixel(map.getPixel(x, y));  // add the encoding of the current pixel to the correct row array
        return result;                                  // return the encoded result
    }
    static private Map2D decodeMap(String[] rows) {     // this function is the inverse of encodeMap, for easier initialization of maps
        int w = rows[0].length(), h = rows.length ;      // get the size of the array and the length of the first string (we are kind of treating it as a two-dimensional array of chars)
        for (String row : rows)                         // iterate on the strings
            assertEquals(w, row.length(), "Cannot decode strings of different lengths into a map"); // make sure it is the same length as the first string, a jagged array is not a legal map encoding
        Map2D result = new MyMap2D(w, h);               // initialize a result map
        for (int x = 0; x < w; ++x)                     // iterate on the pixels
            for (int y = 0; y < h; ++y)
                result.setPixel(x, y, decodePixel(rows[y].charAt(x)));  // set the current pixel as the x char of the y string (y string is the row, x char of it is the correct column)
        return result;                                  // return the decoded result
    }

    public static void main(String[] args) {
        System.out.println("Benchmark result: " + benchmark());
    }

    public static long benchmark() {   // this function benchmarks your machine to determine an appropriate timeoutFactor
        Random rnd = new Random(System.nanoTime());                        // initialize a randomizer
        for (int i = 0; i < 1000; ++i) rnd.nextDouble();                   // run it a couple of times to warm up the Java compiler
        long start = System.nanoTime();                                    // start the stopwatch
        for (int i = 0; i < 5000000; ++i) rnd.nextDouble();                // run the randomizer 5000000 times for each test
        long end = System.nanoTime();                                      // stop the stopwatch
        long duration = end - start;                                       // calculate the duration
        return (duration / 100000);                                        // divide the duration by 100000 to determine timeoutFactor
    }   // the values in this function were determined in a way that makes my machine create the correct timeoutFactor

    @BeforeEach
    public void setUp() {    // this functions set ups the tests by resetting the map
        premadeMap = decodeMap(originalEncodedMap.clone());  // decode the encoded original map to reset to it
    }

    @Test
    @Order(1)    // all other tests depend on the functionalities tested here, so we check them first
    @Timeout(value = timeoutFactor * numberOfTests, unit = TimeUnit.MICROSECONDS, threadMode = ThreadMode.SEPARATE_THREAD)   // I had to read to read a long GitHub issue and look at the actual commit that implemented this feature in the junit5 repo to find how to make this work
    public void testEncodeDecode() { // this tests the encoding and decoding process
        assertArrayEquals(originalEncodedMap, encodeMap(premadeMap));   // make sure encoding the decoded map gives the original map

        Random rnd = new Random(System.nanoTime());
        for (int i = 0; i < numberOfTests; ++i) {     // run random tests
            int w = rnd.nextInt(160) + 1, h = rnd.nextInt(160) + 1;  // generate ranodm side lengths
            MyMap2D randMap = randMap(w, h);                 // create a random map
            String[] encoding = encodeMap(randMap);          // encode the map into a string array
            assertEquals(randMap, decodeMap(encoding));      // make sure decoding the string array gives back the map, using MyMap2D.equals(Object o)
        }
    }
    private boolean isTriangleLine(Point2D p1, Point2D p2, Point2D p3) {  // this function checks if 3 points are very close to being on the same line
        double x1 = p1.x(), y1 = p1.y(), x2 = p2.x(), y2 = p2.y(), x3 = p3.ix(), y3 = p3.iy();  // preparing the numbers for the formula
        double dx13 = x1 - x3, dx21 = x2 - x1, dy13 = y1 - y3, dy21 = y2 - y1, dx23 = x2 - x3, dy23 = y2 - y3;      // some of the deltas we will need
        double d12_3 = (Math.abs(dx21 * dy13 - dx13 * dy21)) / (Math.sqrt(dx21 * dx21 + dy21 * dy21)); // caluculate the distance between the p3 and the line p1-p2 using https://en.wikipedia.org/wiki/Distance_from_a_point_to_a_line#Line_defined_by_two_points
        double d13_2 = (Math.abs(dx13 * dy21 - dx21 * dy13)) / (Math.sqrt(dx13 * dx13 + dy13 * dy13)); // same thing for the two other points and their appropriate lines
        double d32_1 = (Math.abs(dx23 * dy13 - dx13 * dy23)) / (Math.sqrt(dx23 * dx23 + dy23 * dy23));
        return ((d12_3 <= 2) || (d13_2 <= 2) || (d32_1 <= 2));   // if the distance from any point to the line of the two others is less than 2 the triangle is too close to a line to try and fill
    }
    @Test
    @Timeout(value = timeoutFactor * numberOfTests, unit = TimeUnit.MICROSECONDS, threadMode = ThreadMode.SEPARATE_THREAD)
    public void testDrawSegment() {
        Random rnd = new Random(System.nanoTime());         // create a new random generator and seed it with the time
        for (int i = 0; i < numberOfTests; ++i) {           // run the test according to required number of times
            int w = rnd.nextInt(320) + 1, h = rnd.nextInt(320) + 1;  // get a random size for the map
            MyMap2D map = new MyMap2D(w, h); // set up a random map
            map.fill(WHITE);                 // fill the map with white
            Point2D p1 = randPoint(w, h, rnd), p2 = randPoint(w, h, rnd), p3 = randPoint(w, h, rnd);  // set up 3 random points to draw a triangle, we pass rnd to them because they might be initialized in the same nanosecond
            map.drawSegment(p1, p2, BLACK);  // draw 1 line on the map, we will now test it for incorrect pixels
            assertEquals(BLACK, map.getPixel(p1), "p1 was not colored by drawSegment");     // make sure the actual points were colored
            assertEquals(BLACK, map.getPixel(p2), "p2 was not colored by drawSegment");
            long minX = Math.round(Math.min(p1.x(), p2.x())),
                 minY = Math.round(Math.min(p1.y(), p2.y())),
                 maxX = Math.round(Math.max(p1.x(), p2.x())),
                 maxY = Math.round(Math.max(p1.y(), p2.y()));  // these are the limits of where our line is allowed to be
            for (int x = 0; x < w; ++x)          // iterate on all the pixels in the map
                for (int y = 0; y < h; ++y)
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
            assert (map.getPixel(0, 0) == WHITE)              // this asserts that the corners are not all colored
                  || (map.getPixel(0, h - 1) == WHITE)        // if they are, the filling escaped the triangle
                  || (map.getPixel(w - 1, 0) == WHITE)        // (they can't all be black because we drew a triangle)
                  || (map.getPixel(w - 1, h - 1) == WHITE)    // some might be black, some might even be yellow (if the filling was ON the triangle)
                  : "Filling escaped segment triangle, segments are holey";      
        }
    }
    @Test
    @Timeout(value = timeoutFactor * numberOfTests * 2, unit = TimeUnit.MICROSECONDS, threadMode = ThreadMode.SEPARATE_THREAD)
    public void testDrawRect() {
        Random rnd = new Random(System.nanoTime());      // create a new random generator and seed it with the time
        for (int i = 0; i < numberOfTests; ++i) {        // we perform numberOfTests random tests
            int w = rnd.nextInt(320) + 1, h = rnd.nextInt(320) + 1;               // get a decent random size for the map
            MyMap2D map = randMap(w, h, new int[] { WHITE, BLUE, RED, YELLOW, GREEN }, 0);  // set up a random map without black
            Point2D p1 = randPoint(w, h, rnd), p2 = randPoint(w, h, rnd);  // set up 2 random points to draw a rectangle, we pass rnd to them because they might be initialized in the same nanosecond
            map.drawRect(p1, p2, BLACK);                       // draw a black rectangle
            long minX = Math.round(Math.min(p1.x(), p2.x())),
                 minY = Math.round(Math.min(p1.y(), p2.y())),
                 maxX = Math.round(Math.max(p1.x(), p2.x())),
                 maxY = Math.round(Math.max(p1.y(), p2.y()));  // these are the limits of where the rectangle is allowed to be
            for (int x = 0; x < w; ++x)               // iterate on the pixels to check no wrong pixels
                for (int y = 0; y < h; ++y) {
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
    @Timeout(value = timeoutFactor * numberOfTests * 4, unit = TimeUnit.MICROSECONDS, threadMode = ThreadMode.SEPARATE_THREAD)
    public void testDrawCircle() {
        Random rnd = new Random(System.nanoTime());                // create a new random generator and seed it with the time
        for (int i = 0; i < numberOfTests; ++i) {        // we perform numberOfTests random tests
            int w = rnd.nextInt(320) + 1, h = rnd.nextInt(320) + 1;               // get a decent random size for the map
            MyMap2D map = randMap(w, h, new int[] { WHITE, BLUE, RED, GREEN }, 0);  // set up a random map without black or yellow
            Point2D center = randPoint(w, h, rnd);  // set up a random point as the center
            double radius = rnd.nextDouble() * Math.max(w, h);   // get a random radius up to the size of the map
            map.drawCircle(center, radius, YELLOW);        // draw the circle
            map.fill(center, BLACK);                       // we fill the circle to make sure it is all connected
            long minX = (long) Math.floor(center.x() - radius),
                 minY = (long)  Math.ceil(center.y() - radius),
                 maxX = (long) Math.floor(center.x() + radius),
                 maxY = (long)  Math.ceil(center.y() + radius);  // these are the limits of where the rectangle should be is allowed to be
            for (int x = 0; x < w; ++x)               // iterate on the pixels to check no wrong pixels
                for (int y = 0; y < h; ++y) {
                    int color = map.getPixel(x, y);
                    if ((color == BLACK) && radius >= 1) {         // if the pixel is black and the radius is greater than 1 (otherwise could be a circle with no pixels)
                        assert ((x >= minX) && (x <= maxX) && (y >= minY) && (y <= maxY)) : "drawCircle colored point outside of bounded array";  // make sure we are within bound
                        assert center.distance(new Point2D(x, y)) < radius : "drawCircle colored point too far away from center"; // make sure the point is actually within the circle
                    } else if (color == WHITE) {     // if it is white
                        assert center.distance(new Point2D(x, y)) > radius : "drawCircle didn't color point inside circle"; // make sure the point is actually outside the circle
                    } else if (color == YELLOW) {    // if we found yellow point, it means it was part of the circle (so drawn in yellow) but not orthogonally connected to the center (so wasn't filled in black)
                        assert false : "Found disconnected point in circle";  
                    }
                }
        }
        // next is a simple deterministic test on the premade map
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
    @Timeout(value = timeoutFactor * numberOfTests * 4, unit = TimeUnit.MICROSECONDS, threadMode = ThreadMode.SEPARATE_THREAD)
    public void testFill() {
        Random rnd = new Random(System.nanoTime());              // create a new random generator and seed it with the time
        for (int i = 0; i < numberOfTests; ++i) {                // we perform numberOfTests random tests
            int w = rnd.nextInt(80) + 1, h = rnd.nextInt(80) + 1;                     // get a decent random size for the map
            MyMap2D map = randMap(w, h, new int[] { WHITE, BLUE, RED, YELLOW, GREEN }, 0.4);  // set up a random map without black and a bit of white (so we have something to fill)
            Point2D origin = randPoint(w, h, rnd);               // set up a random point as the fill origin
            int originColor = map.getPixel(origin);              // remember its color, this is the only color we are allowed to fill
            MyMap2D original = new MyMap2D(map);                 // save the original map to cross-reference later
            int returnValue = map.fill(origin, BLACK);           // fill the map on the origin and save return value
            int colored = 0;                                     // initialize the number of colored points as 0
            for (int x = 0; x < w; ++x)             // iterate on the pixels
                for (int y = 0; y < h; ++y) {
                    int color = map.getPixel(x, y);              // check the pixel's color
                    if (color == BLACK) {                        // if it is black (was filled) we need to check some stuff
                        assertNotEquals(-1, original.shortestPathDist(origin, new Point2D(x, y)), "Colored point with no path");  // make sure there was a path from the origin to here in the original map
                        assertEquals(originColor, original.getPixel(x, y), "Colored point not in the origin color");              // make sure it was originally the same color as the origin
                        colored += 1;                            // incerement the number of colored points
                    } else {                                     // otherwise (wasn't filled)
                        assertEquals(-1, original.shortestPathDist(origin, new Point2D(x, y)), "Colored point with no path");     // make sure there wasn't a path to this point
                    }
                }
            assertEquals(colored, returnValue, "Return value was not number of colored points");   // make sure the return value is the actual number of colored points
        }
        // next is a simple deterministic test on the premade map
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
    @Timeout(value = timeoutFactor * numberOfTests, unit = TimeUnit.MICROSECONDS, threadMode = ThreadMode.SEPARATE_THREAD)
    public void testShortestPath() {
        Random rnd = new Random(System.nanoTime());              // create a new random generator and seed it with the time
        for (int i = 0; i < numberOfTests; ++i) {                // we perform numberOfTests random tests
            int w = rnd.nextInt(50) + 10, h = rnd.nextInt(50) + 10;                     // get a decent random size for the map
            MyMap2D map = randMap(w, h, allColors, 0.9);  // set up a random map with a lot of white (so we are likely to find paths)
            Point2D p1 = randPoint(w, h, rnd), p2 = randPoint(w + 1, h + 1, rnd);  // set up 2 random points for the path (we allow p2 to be outside bounds for a test)
            Point2D[] path = map.shortestPath(p1, p2);           // find the path
            int distance = map.shortestPathDist(p1, p2);         // calculate the length
            if (map.getPixel(p1) != map.getPixel(p2) || !map.inBounds(p2)) {          // if the points are differently colored or p2 was created outside there is no path
                assertNull(path, "Created path between differently colored points");  // make sure the created path is null
                assertEquals(-1, distance, "Found distance between differently colored points");  // make sure the distance is -1 (defined failure return value)
                continue;  // we have no other tests to perform on this path (most of them will error out)
            }
            if (path == null) {  // if didn't find path (but they are the same color)
                assertEquals(-1, distance, "Contradiction between shortestPath and shortestPathDist");  // make sure shortestPathDist agrees
                continue;   // we have no other tests to perform on this path
            }
            assertEquals(distance, map.shortestPathDist(p2, p1), "Calculated different distance in the two directions");    // make sure the distance is the same backwards
            assertEquals(distance, path.length - 1, "Path length was not equal to calculated distance");   // make the returned path's length is equal to the calculated length (-1 for the first point)
            int originColor = map.getPixel(p1);                  // get the color of the origin pixel
            for (int j = 0; j < path.length; ++j) {              // iterate on the path
                Point2D p = path[j];                 // get the j-th point on the path
                assertEquals(j, map.shortestPathDist(p, p2), "Path created in wrong order");   // make sure this point is j away from the destination (there is no definition for the direction of the path, in my implementation it is from p2 to p1)
                assertEquals(originColor, map.getPixel(p), "Created path through different color pixel");  // make sure each point is the same color as the origin
                assertTrue(map.inBounds(p), "Created path outside of map");     // make sure the point is within the map
            }
        }

        // next is a simple deterministic test
        Point2D p1 = new Point2D(2, 9), p2 = new Point2D(6, 7);
        Point2D[] path = premadeMap.shortestPath(p1, p2);
        int pathDistance = premadeMap.shortestPathDist(p1, p2);
        assertEquals(16, pathDistance);
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
    public void testNextGenGol() { // TODO: random testing?
        premadeMap.nextGenGol();                      // evolve the default map one generation
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

        String[] stable = new String[] {  // created using patterns from https://conwaylife.com/wiki/Still_life
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