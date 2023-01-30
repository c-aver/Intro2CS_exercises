package intro2cs_exercises;

public class Exp_Main1 {
    public static void main(String[] args) {
        System.out.println("abc".substring(3).length());
        
        int[][] ans = Q1(6,5);
        for (int i = 0; i < ans.length; i++) {
            for (int j = 0; j < ans[0].length; ++j ) {
                System.out.print(ans[i][j] + "\t");
            }
            System.out.println();
        }
        System.out.println(q2("uuuuuurrrrrrriiiiiiieeeeeeellllllsssssshhhhhaaaaaaappppppppiiiiiiirrrrrroooooochaimmmmmmmm"));
    }

    public static int[][] Q1(int n, int m)
    {
        int[][] ans = new int[n][m];
        for(int i = 0; i < m; i++)
        {
            ans[0][i] = 1;
        }
        for(int i = 0; i < n; i++)
        {
            ans[i][0] = 1;
        }
        for(int i = 1; i < n; i++)
        {
            for(int j = 1; j < m; j++)
            {
                ans[i][j] = ans[i-1][j] + ans[i][j-1];
            }
        }
        return ans;
    }

//     public static String q2(String str)
//     {
//         if(str == "")
//         {
//             return "";
//         }
//         int i = 1;
//         while(str.charAt(0)==str.charAt(i))
//     {
//         i++;
//     }
//         return str.charAt(0) + q2(str.substring(i));
    
    
//     }
// }

public static String q2(String str)
    {
        if(str.equals(""))
        {
            return "";
        }
        String ans = "";
        ans+=str.charAt(0);
        int num = 0;
        while(num<str.length()-1 && str.charAt(num)==str.charAt(num+1))
        {
            num++;
        }
        return ans+q2(str.substring(num + 1));
    }
}