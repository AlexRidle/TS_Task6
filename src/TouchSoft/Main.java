package TouchSoft;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        int[] nums = {1, 2, 3, 4};
        System.out.println(canBeEqualTo24(nums));
    }

    static boolean canBeEqualTo24(int[] nums) {
        boolean isPossible = false;
        char[] chars = {'+', '-', '*', '/'};
        ArrayList<String> possibleSigns = new ArrayList<>();

        class CombinationGenerator {
            void getAllSignCombinations(char[] chars, String prefix, int charsLength, int numberOfSymbols) {
                if (numberOfSymbols == 0) {
                    possibleSigns.add(prefix);
//                    System.out.println(prefix);
                    return;
                }
                for (int i = 0; i < charsLength; ++i) {
                    String newPrefix = prefix + chars[i];
                    getAllSignCombinations(chars, newPrefix, charsLength, numberOfSymbols - 1);
                }
            }
        }
        CombinationGenerator combinationGenerator = new CombinationGenerator();
        combinationGenerator.getAllSignCombinations(chars, "", chars.length, 3);

        for(String signs : possibleSigns){
            System.out.println(""
                    + nums[0] + signs.charAt(0)
                    + nums[1] + signs.charAt(1)
                    + nums[2] + signs.charAt(2)
                    + nums[3]);
        }

//        Possible brackets with 4 nums:
//
//        ((nn)n)n
//        n(n(nn))
//        (nn)(nn)
//        (nn)nn
//        n(nn)n
//        nn(nn)
//        (nnn)n
//        n(nnn)
//        nnnn

        //

        return isPossible;
    }


}


