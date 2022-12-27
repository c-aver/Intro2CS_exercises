package Exe.Ex4;
/**
 * This class represents a set of parameters for Ex4 GUI Shape application for:
 * Introduction to Computer Science 2022, Ariel University.
 * Do NOT change this class
 * @author boaz.benmoshe
 */
public class Ex4_Const {
	public static final double EPS1 = 0.001, EPS2=Math.pow(EPS1, 2), EPS = EPS2;
	// Constant flags for sorting. 
	public static int sortTypeCount = 0;
	public static final int Sort_By_Tag = sortTypeCount++, Sort_By_Anti_Tag=sortTypeCount++, 
			Sort_By_Area =sortTypeCount++, Sort_By_Anti_Area = sortTypeCount++, 
			Sort_By_Perimeter =sortTypeCount++, Sort_By_Anti_Perimeter = sortTypeCount++, 
			Sort_By_toString = sortTypeCount++, Sort_By_Anti_toString = sortTypeCount++;
	public static int Width = 640, Height = 640;
	public static int DIM_SIZE = 10;
	
}
