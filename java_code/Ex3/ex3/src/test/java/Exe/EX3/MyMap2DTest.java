/* Name: Chaim Averbach
 * ID: 207486473
 */

package Exe.EX3;

import java.awt.Color;
import java.util.Arrays;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@TestMethodOrder(OrderAnnotation.class)
public class MyMap2DTest { // TODO: funcs for random point, random map (with list of colors)
    public static final int WHITE  = Color.WHITE.getRGB();   // precalculated colors for our tests, these are all the legal colors
    public static final int BLACK  = Color.BLACK.getRGB();
    public static final int BLUE   = Color.BLUE.getRGB();
    public static final int RED    = Color.RED.getRGB();
    public static final int YELLOW = Color.YELLOW.getRGB();
    public static final int GREEN  = Color.GREEN.getRGB();

    private static final int numberOfTests = 1000;   // a value to determine the number of random tests to perform in some functions

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
    private Map2D map;

    private static Point2D randPoint(double width, double height, Random rnd) {
        // double minX = -0.5, maxX = width - 0.5, minY = -0.5, maxY = height - 0.5;  // the bounds, taken from the bounds created by the scale in the GUI, not actually used in code so commented out
        double x = (rnd.nextDouble() * width) - 0.5, y = (rnd.nextDouble() * height) - 0.5;
        return new Point2D(x, y);
    }
    private static Point2D randPoint(double size, Random rnd) {
        return randPoint(size, size, rnd);
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
        map = decodeMap(originalEncodedMap.clone());  // decode the encoded original map to reset to it
    }

    @Test
    @Order(1)    // all other tests depend on the functionalities tested here, so we check them first
    public void testEncodeDecode() { // this tests the encoding and decoding process // TODO: test on random maps
        assertArrayEquals(originalEncodedMap, encodeMap(map));   // make sure encoding the decoded map gives the original map

        Random rnd = new Random(System.nanoTime());
        for (int i = 0; i < numberOfTests; ++i) {
            int sideLength = rnd.nextInt(200) + 1;  // generate a random side length up to 200
            MyMap2D randMap = randMap(sideLength);
            String[] encoding = encodeMap(randMap);
            assertEquals(randMap, decodeMap(encoding));
        }
    }
    @Test
    public void testDrawSegment() { // TODO: test no escapes with random triangles
        Random rnd = new Random(System.nanoTime());  // create a new random generator and seed it with the time
        for (int i = 0; i < 1000000; ++i) {           // run the test according to required number of times
            boolean line = false;
            int size = rnd.nextInt(200) + 20;               // get a decent random size for the map
            MyMap2D segmentMap = randMap(size); // set up a random map
            segmentMap.fill(WHITE);             // fill the map with white
            Point2D p1 = randPoint(size, rnd), p2 = randPoint(size, rnd), p3 = randPoint(size, rnd);  // set up 3 random points to draw a triangle
            double d;
            {
                double x0 = p3.ix(), y0 = p3.iy(), x1 = p1.x(), y1 = p1.y(), x2 = p2.x(), y2 = p2.y();  // preparing the numbers for the formula
                double dx10 = x1 - x0, dx21 = x2 - x1, dy10 = y1 - y0, dy21 = y2 - y1;      // some of the deltas we will need
                d = (Math.abs(dx21 * dy10 - dx10 * dy21)) / (Math.sqrt(dx21 * dx21 + dy21 * dy21)); // caluculate the distance between the pixel and the line using https://en.wikipedia.org/wiki/Distance_from_a_point_to_a_line#Line_defined_by_two_points
                line = (d <= 2);
            }
            segmentMap.drawSegment(p1, p2, BLACK);  // draw 1 line on the map, we will now test it for incorrect pixels
            for (int x = 0; x < size; ++x)          // iterate on all the pixels in the map
                for (int y = 0; y < size; ++y)
                    if (segmentMap.getPixel(x, y) == BLACK) { // if the pixel is black, it is part of our segment, we need to make sure that's correct
                        double x0 = x, y0 = y, x1 = p1.x(), y1 = p1.y(), x2 = p2.x(), y2 = p2.y();  // preparing the numbers for the formula
                        double dx10 = x1 - x0, dx21 = x2 - x1, dy10 = y1 - y0, dy21 = y2 - y1;      // some of the deltas we will need
                        double distance = (Math.abs(dx21 * dy10 - dx10 * dy21)) / (Math.sqrt(dx21 * dx21 + dy21 * dy21)); // caluculate the distance between the pixel and the line using https://en.wikipedia.org/wiki/Distance_from_a_point_to_a_line#Line_defined_by_two_points
                        assert distance <= 1 : "drawSegment colored point too far from segment";    // if the point is more than 1 far away from the theoretical segment it is not valid to be colored
                    }
            segmentMap.drawSegment(p1, p3, BLACK); // draw the other 2 sides of the triangle in black
            segmentMap.drawSegment(p2, p3, BLACK);
            Point2D midpoint = new Point2D((p1.x() + p2.x() + p3.x()) / 3, (p1.y() + p2.y() + p3.y()) / 3); // find the midpoint, guaranteed to be inside the triangle
            segmentMap.fill(midpoint, YELLOW);        // fill the inside of the triangle with blue
            try {
                assertNotEquals(YELLOW, segmentMap.getPixel(0  , 0  ), "Filling escaped segments " + i); // make sure the filling didn't escape the triangle
                assertNotEquals(YELLOW, segmentMap.getPixel(0  , (int) size - 1), "Filling escaped segments " + i); // this means our lines are without holes
                assertNotEquals(YELLOW, segmentMap.getPixel((int) size - 1, 0  ), "Filling escaped segments " + i);
                assertNotEquals(YELLOW, segmentMap.getPixel((int) size - 1, (int) size - 1), "Filling escaped segments " + i);
            } catch (AssertionError e) {
                StdDraw_Ex3.clear();
                StdDraw_Ex3.setScale(-0.5, segmentMap.getHeight() - 0.5);
                StdDraw_Ex3.enableDoubleBuffering();                 // enable double buffering to prevent map from being shown point by point
    		    Ex3.drawArray(segmentMap);		                             // draw the new map
                // new java.util.Scanner(System.in).nextLine();
                for (int x = 0; x < size; ++x)
                    for (int y = 0; y < size; ++y)
                        if (segmentMap.getPixel(x, y) == BLACK)
                            if (!line)
                                System.err.println();
            }
            if (i % 10 == 0) System.out.println("Finished " + (i + 1) + " tests");
        }
    }
    @Test
    public void testDrawRect() {   // TODO: make sure the rectangle has no holes, make sure no (out of bounds) errors
        Point2D p1 = new Point2D(3.3, 4.8), p2 = new Point2D(7, 9);
        map.drawRect(p1, p2, BLACK);
        String[] expected = {
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
        assertArrayEquals(expected, encodeMap(map));
    }
    @Test
    public void testDrawCircle() {   // TODO: make sure the circle has no holes, make sure no (out of bounds) errors
        Point2D p1 = new Point2D(3.3, 4.8), p2 = new Point2D(5, 9);
        map.drawCircle(p1, 4, BLUE);
        map.drawCircle(p2, 2, YELLOW);
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
        assertArrayEquals(expected, encodeMap(map));
    }
    @Test
    public void testFill() {   // TODO: make sure all the points were originally the same color, make sure the return value is the number of colored points, make sure there are paths to all colored points
        Point2D p = new Point2D(0, 0);
        map.fill(p, BLACK);
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
        assertArrayEquals(expected, encodeMap(map));
    }
    @Test
    public void testShortestPath() {    // TODO: make sure the length is the same (+1) as shortestPathDist, make sure all the points are the same color as the origin and destination, make sure the path is within the map
        Point2D p1 = new Point2D(2, 9), p2 = new Point2D(6, 7);
        Point2D[] path = map.shortestPath(p1, p2);
        for (Point2D p : path)
            map.setPixel(p, GREEN);
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
        assertArrayEquals(expected, encodeMap(map));
    }
    @Test
    public void testShortestPathDist() { // TODO: idea: make sure distance is the same from both directions, make sure there is no path to points outside, make sure there is no path between differently colored points
        Point2D p1 = new Point2D(2, 9), p2 = new Point2D(6, 7);
        int pathDist = map.shortestPathDist(p1, p2);
        assertEquals(16, pathDist);
    }
    @Test
    public void testNextGenGol() {
        map.nextGenGol();                             // evolve the default map one generation
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
        assertArrayEquals(expected, encodeMap(map));  // make sure the evolved array is as expected
        map.nextGenGol();                             // evolve another generation
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
        assertArrayEquals(expected, encodeMap(map)); // make sure it is correct again

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