package Exe.EX3;


import java.awt.Color;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(OrderAnnotation.class)
public class MyMap2DTest {
	public static final int WHITE  = Color.WHITE.getRGB();
	public static final int BLACK  = Color.BLACK.getRGB();
	public static final int BLUE   = Color.BLUE.getRGB();
	public static final int RED    = Color.RED.getRGB();
	public static final int YELLOW = Color.YELLOW.getRGB();
	public static final int GREEN  = Color.GREEN.getRGB();

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
    public Map2D map;

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
    public void setUp() { // this functions set ups the tests by resetting the map
        map = decodeMap(originalEncodedMap.clone());  // decode the encoded original map to reset to it
    }

    @Test
    @Order(1) // all other tests depend on the functionalities tested here, so we check them first
    public void testEncodeDecode() { // this tests the encoding and decoding process
        assertArrayEquals(originalEncodedMap, encodeMap(map)); // make sure encoding the decoded map gives the original decoded map
    }
    @Test
    public void testDrawSegment() {
        MyMap2D segmentMap = new MyMap2D(160); // set up a map
        segmentMap.fill(WHITE);                // fill the map with white
        Point2D p1 = new Point2D(22 , 125),     // set up 4 point to create a quadrilateral
                p2 = new Point2D(143, 157),
                p3 = new Point2D(9  , 47 ),
                p4 = new Point2D(70 , 31 );
        segmentMap.drawSegment(p1, p2, BLACK); // draw 1 line on the map, we will then test it for incorrect pixels
        for (int x = 0; x < 160; ++x)          // iterate on all the pixels in the map
            for (int y = 0; y < 160; ++y)
                if (segmentMap.getPixel(x, y) == BLACK) { // if the pixel is black, it is part of our segment, we need to make sure that's correct
                    double x0 = x, y0 = y, x1 = p1.x(), y1 = p1.y(), x2 = p2.x(), y2 = p2.y();  // preparing the numbers for the formula
                    double dx10 = x1 - x0, dx21 = x2 - x1, dy10 = y1 - y0, dy21 = y2 - y1;      // some of the deltas we will need
                    double distance = (Math.abs(dx21 * dy10 - dx10 * dy21)) / (Math.sqrt(dx21 * dx21 + dy21 * dy21)); // caluculate the distance between the pixel and the line using https://en.wikipedia.org/wiki/Distance_from_a_point_to_a_line#Line_defined_by_two_points
                    assert distance <= 1 : "drawSegment colored point too far from segment";  // if the point is more than 1 far away from the theoretical segment it is not valid to be colored
                }
        segmentMap.drawSegment(p1, p3, BLACK); // draw the other 3 sides of the quadrilateral in black
        segmentMap.drawSegment(p2, p4, BLACK);
        segmentMap.drawSegment(p3, p4, BLACK);
        segmentMap.fill(80, 100, BLUE);        // fill the inside of the quadrilateral with blue
        assertEquals(WHITE, segmentMap.getPixel(0  , 0  )); // make sure the filling didn't escape the quadrilateral
        assertEquals(WHITE, segmentMap.getPixel(0  , 159)); // this means our lines are without holes
        assertEquals(WHITE, segmentMap.getPixel(159, 0  ));
        assertEquals(WHITE, segmentMap.getPixel(159, 159));
    }
    @Test
    public void testDrawRect() {
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
    public void testDrawCircle() {
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
    public void testFill() {
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
    public void testShortestPath() {
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
    public void testShortestPathDist() {
        Point2D p1 = new Point2D(2, 9), p2 = new Point2D(6, 7);
        int pathDist = map.shortestPathDist(p1, p2);
        assertEquals(16, pathDist);
    }
    @Test
    public void testNextGenGol() {
        map.nextGenGol();
        String[] expected = new String[] {
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
        assertArrayEquals(expected, encodeMap(map));
        map.nextGenGol();
        expected = new String[] {
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
        assertArrayEquals(expected, encodeMap(map));

        String[] stable = new String[] { // created using patterns from https://conwaylife.com/wiki/Still_life
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
        Map2D stableMap = decodeMap(stable);
        for (int i = 0; i < 1000; ++i) stableMap.nextGenGol();
        assertArrayEquals(stable, encodeMap(stableMap));
    }
}