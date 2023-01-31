package exercises;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class CombinationLock {
    public static void main(String[] args) {
        String[] codes = allCodes();
        String[] codes2 = allcodes2();
        Arrays.sort(codes);
        Arrays.sort(codes2);
        for (int i = 0; i < 120; ++i) System.out.println(codes[i] + '\t' + codes2[i]);
        System.out.println(codes.length);
        System.out.println(codes2.length);
    }

    public static String[] allCodes() {
        List<Character> digits = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            digits.add((char) (i + '0'));
        }
        List<String> ans = new ArrayList<>();
        for (Character digit : digits) {
            List<Character> without = new ArrayList<>(digits);
            without.remove(digit);
            List<String> codes = allShuffles(without);
            for (String code : codes) {
                ans.add(code + '#');
            }
        }
        return ans.toArray(new String[0]);
    }

    public static String[] allcodes2()
    {
        ArrayList<String> arrL= new ArrayList<String>();
        for (int index = 1; index < 6; index++) {
            for (int i = 1; i < 6; i++) {
                if (i!=index)
                {
                    for (int j = 1; j < 6; j++) {
                        if (j!=index&&j!=i)
                        {
                            for (int x = 1; x < 6; x++) {
                                if (x!=index&&x!=i&&x!=j)
                                {
                                    arrL.add(""+index+i+j+x+'#');
                                }
                            }
                        }
                    }
                }
            }

        }

        String[] ans= new String [arrL.size()];
        for (int i = 0; i < ans.length; i++) {
            ans[i]= arrL.get(i);
        }
        return ans;
    }

    public static List<String> allShuffles(List<Character> chars) {
        List<String> ans = new ArrayList<>();
        
        if (chars.size() == 1) {
            ans.add(chars.get(0).toString());
            return ans;
        }

        for (Character ch : chars) {
            List<Character> without = new ArrayList<>(chars);
            without.remove(ch);
            List<String> subShuffles = allShuffles(without);
            for (String subShuffle : subShuffles) {
                ans.add(ch + subShuffle);
            }
        }
        return ans;
    }
}
