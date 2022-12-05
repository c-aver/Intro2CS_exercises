package Exe.EX3;


import java.awt.Color;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

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

    static private char encodePixel(int pix) {
        if(pix == WHITE)
			return 'W';
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
        assert false : "Illegal color in map";
        return '\0';
    }
    static private int decodePixel(char pix) {
        if(pix == 'W')
			return WHITE;
		if(pix == 'B')
			return BLACK;
		if(pix == 'L')
			return BLUE;
		if(pix == 'R')
			return RED;
		if(pix == 'Y')
			return YELLOW;
		if(pix == 'G')
			return GREEN;
        assert false : "Illegal color encoding";
        return 0;
    }
    static private String[] encodeMap(Map2D map) {
        int w = map.getWidth(), h = map.getHeight();
        String[] result = new String[w];
        Arrays.fill(result, "");
        for (int x = 0; x < w; ++x)
            for (int y = 0; y < h; ++y)
            {
                result[y] += encodePixel(map.getPixel(x, y));
            }
        return result;
    }
    static private Map2D decodeMap(String[] cols) {
        int w = cols.length, h = cols[0].length();
        assert w == h : "Cannot decode non-square map";
        for (String col : cols)
            assert col.length() == h : "Cannot decode strings of different lengths into a map";
        Map2D result = new MyMap2D(w, h);
        for (int x = 0; x < w; ++x)
            for (int y = 0; y < h; ++y)
            {
                result.setPixel(x, y, decodePixel(cols[y].charAt(x)));;
            }
        return result;
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
        for (Point2D p : path)
            map.setPixel(p, GREEN);
        assertArrayEquals(expected, encodeMap(map));
    }
    @Test
    public void testShortestPathDist() {
        // TODO: implement test
    }
    @Test
    public void testNextGenGol() {
        // TODO: implement test
    }
}