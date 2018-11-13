/*
   The University of Melbourne
   School of Computing and Information Systems
   COMP90041 Programming and Software Development
   Lecturer: Prof. Rui Zhang
   Semester 1, 2018
   Project C
   Author: Chenyuan Zhang
   Student ID: 815901
*/

/*
    The class for SG functions operations
*/

import java.util.ArrayList;

public class SG {
    private static int[] s;         //Store the SG function for single values

    //Compute the SG function for single values
    private static void findS(int n) {
        ArrayList<Integer> t = new ArrayList<>();

        for (int i = 0; i < n - i; i++) {
            t.add(s[i] ^ s[n - 1 - i]);
        }

        for (int i = 0; i < n - 1 - i; i++) {
            t.add(s[i] ^ s[n - 2 - i]);
        }

        int temp = 0;
        while (t.contains(temp)) {
            temp++;
        }

        s[n] = temp;
    }

    //For any state, compute the SG function and find victory strategy if exists
    public static String getMove(ArrayList<Integer> l) {
        String ans;
        int tmpsg, num;

        int sg = 0;
        for (int a : l) {
            sg ^= s[a];
        }
        if (sg == 0) {
            ans = "0 0 1";
            return ans;
        }

        for (int i = 0; i < l.size(); i++) {
            num = l.get(i);
            tmpsg = sg ^ s[num];
            for (int k = 1; k <= 2; k++) {
                for (int j = 0; j <= num - j - k; j++) {
                    if ((tmpsg ^ s[j] ^ s[num - k - j]) == 0) {
                        ans = i + " " + j + " " + k;
                        return ans;
                    }
                }
            }
        }

        return "Error!";        //Shouldn't be executed if the function is right
    }

    public static void init(int max_stones) {
        s = new int[max_stones + 1];
        s[0] = 0;
        for (int i = 1; i <= max_stones; i++) {
            findS(i);
        }
    }
}
