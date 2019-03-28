package TouchSoft;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        int[] nums1 = {1, 2, 1, 2};
        int[] nums2 = {4, 1, 8, 7};
        canBeEqualTo24(nums1);
        System.out.println();
        canBeEqualTo24(nums2);
    }

    private static boolean canBeEqualTo24(int[] nums) {
        char[] chars = {'+', '-', '*', '/'};
        char[] numbers = {(char) (nums[0] + '0'), (char) (nums[1] + '0'), (char) (nums[2] + '0'), (char) (nums[3] + '0')};
        ArrayList<String> possibleSigns = new ArrayList<>();
        ArrayList<String> possibleNumbers = new ArrayList<>();
        ArrayList<String> possibleTasksWithoutBrackets = new ArrayList<>();
        ArrayList<String> completedTasks = new ArrayList<>();

        class Calculator {

            private ArrayList<String> tempTask;
            private String item;
            private Calculator calc;

            private String brackets(String task) {
                calc = new Calculator();
                while (task.contains(Character.toString('(')) || task.contains(Character.toString(')'))) {
                    for (int currentChar = 0; currentChar < task.length(); currentChar++) {
                        if (task.charAt(currentChar) == ')') {
                            for (int i = currentChar; i >= 0; i--) {
                                if (task.charAt(i) == '(') {
                                    String taskInsideBrackets = task.substring(i + 1, currentChar);
                                    taskInsideBrackets = calc.recognize(taskInsideBrackets);
                                    task = task.substring(0, i) + taskInsideBrackets + task.substring(currentChar + 1);
                                    i = currentChar = 0;
                                }
                            }
                        }
                    }
                }
                return calc.recognize(task);
            }

            private String recognize(String task) {
                Solver solver = new Solver();
                tempTask = new ArrayList<>();
                item = "";
                for (int currentChar = task.length() - 1; currentChar >= 0; currentChar--) {
                    if (Character.isDigit(task.charAt(currentChar))) {
                        item = task.charAt(currentChar) + item;
                        if (currentChar == 0) {
                            solver.put();
                        }
                    } else {
                        if (task.charAt(currentChar) == '.') {
                            item = task.charAt(currentChar) + item;
                        } else if (task.charAt(currentChar) == '-' && (currentChar == 0 || (!Character.isDigit(task.charAt(currentChar - 1))))) {
                            item = task.charAt(currentChar) + item;
                            solver.put();
                        } else {
                            solver.put();
                            item += task.charAt(currentChar);
                            solver.put();
                        }
                    }
                }
                tempTask = solver.result(tempTask, "*", "/");
                tempTask = solver.result(tempTask, "+", "-");
                return tempTask.get(0);
            }

            class Solver {

                void put() {
                    if (!item.equals("")) {
                        tempTask.add(0, item);
                        item = "";
                    }
                }

                ArrayList<String> result(ArrayList<String> arrayList, String op1, String op2) {
                    int scale = 10;
                    BigDecimal result = new BigDecimal(0);
                    for (int c = 0; c < arrayList.size(); c++) {
                        if (arrayList.get(c).equals(op1) || arrayList.get(c).equals(op2)) {
                            if (arrayList.get(c).equals("*")) {
                                result = new BigDecimal(arrayList.get(c - 1)).multiply(new BigDecimal(arrayList.get(c + 1)));
                            } else if (arrayList.get(c).equals("/")) {
                                result = new BigDecimal(arrayList.get(c - 1)).divide(new BigDecimal(arrayList.get(c + 1)), scale, BigDecimal.ROUND_DOWN);
                            } else if (arrayList.get(c).equals("+")) {
                                result = new BigDecimal(arrayList.get(c - 1)).add(new BigDecimal(arrayList.get(c + 1)));
                            } else if (arrayList.get(c).equals("-")) {
                                result = new BigDecimal(arrayList.get(c - 1)).subtract(new BigDecimal(arrayList.get(c + 1)));
                            }
                            try {
                                arrayList.set(c, (result.setScale(scale, RoundingMode.HALF_DOWN).
                                        stripTrailingZeros().toPlainString()));
                                arrayList.remove(c + 1);
                                arrayList.remove(c - 1);
                            } catch (Exception ignored) {
                            }
                        } else {
                            continue;
                        }
                        c = 0;
                    }
                    return arrayList;
                }
            }
        }

        class TaskTools {

            private void writeSignsCombinationsIntoArray(char[] chars, String prefix, int charsLength, int numberOfSymbols, ArrayList<String> array) {
                if (numberOfSymbols == 0) {
                    array.add(prefix);
                    return;
                }
                for (int i = 0; i < charsLength; ++i) {
                    String newPrefix = prefix + chars[i];
                    writeSignsCombinationsIntoArray(chars, newPrefix, charsLength, numberOfSymbols - 1, array);
                }
            }

            private void writeNumbersCombinationsIntoArray(char[] numbers, String prefix, int numbersLength, ArrayList<String> array) {
                if (numbers.length == 1) {
                    prefix += numbers[0];
                    array.add(prefix);
                    return;
                }
                for (int i = 0; i < numbersLength; ++i) {
                    char[] newNumbers = new char[numbersLength - 1];
                    int counter = 0;
                    for (int j = 0; j < numbersLength; j++) {
                        if (j == i) {
                            continue;
                        }
                        newNumbers[counter] = numbers[j];
                        counter++;
                    }
                    String newPrefix = prefix + numbers[i];
                    writeNumbersCombinationsIntoArray(newNumbers, newPrefix, newNumbers.length, array);
                }
            }

            private void mergeSignsAndNumbersIntoArray(ArrayList<String> signs, ArrayList<String> numbers, ArrayList<String> result){
                for (String sign : signs) {
                    for (String number : numbers) {
                        result.add(""
                                + number.charAt(0) + sign.charAt(0)
                                + number.charAt(1) + sign.charAt(1)
                                + number.charAt(2) + sign.charAt(2)
                                + number.charAt(3));
                    }
                }
                signs.clear();
                numbers.clear();
            }

            private void writeCompletedTasksIntoArray(ArrayList<String> tasksWithoutBrackets, ArrayList<String> completedTasks) {
                ///////////////////////////////////////////////////////////////////
                //TEST METHOD! NEED TO REPLACE WITH BRACKETS GENERATOR!
                ///////////////////////////////////////////////////////////////////
                StringBuilder temp;
                for (String task : tasksWithoutBrackets) {
                    for (int i = 0; i < 9; i++) { //  Possible brackets with 4 nums:
                        temp = new StringBuilder(task);
                        switch (i) {
                            case 0:
                                temp.insert(5, ")").insert(3, ")").insert(0, "((");// ((n+n)+n)+n
                                break;
                            case 1:
                                temp.insert(7, "))").insert(4, "(").insert(2, "(");// n+(n+(n+n))
                                break;
                            case 2:
                                temp.insert(7, ")").insert(4, "(").insert(3, ")").insert(0, "(");// (n+n)+(n+n)
                                break;
                            case 3:
                                temp.insert(3, ")").insert(0, "(");// (n+n)+n+n
                                break;
                            case 4:
                                temp.insert(5, ")").insert(2, "(");// n+(n+n)+n
                                break;
                            case 5:
                                temp.insert(7, ")").insert(4, "(");// n+n+(n+n)
                                break;
                            case 6:
                                temp.insert(5, ")").insert(0, "(");// (n+n+n)+n
                                break;
                            case 7:
                                temp.insert(7, ")").insert(2, "(");// n+(n+n+n)
                                break;
                        }
                        completedTasks.add(temp.toString());
                    }
                }
                tasksWithoutBrackets.clear();
                ///////////////////////////////////////////////////////////////////
                //TEST METHOD! END
                ///////////////////////////////////////////////////////////////////
            }

            private void printInfo(String message, boolean answer) {
                System.out.println("На входе: " + Arrays.toString(nums));
                System.out.println("На выходе: " + answer);
                System.out.println("Пояснение: " + message);
            }

            private boolean solveTask(ArrayList<String> possibleTasks) {
                Calculator calculator = new Calculator();
                for (String answer : possibleTasks) {
                    try {
                        if (calculator.brackets(answer).equals("24")) {
                            printInfo(answer + "=24", true);
                            return true;
                        }
                    } catch (ArithmeticException ignored) {
                        /* ignoring arithmetic exceptions */
                    }
                }
                printInfo("из данного набора чисел невозможно составить выражение, равное 24.", false);
                return false;
            }
        }

        TaskTools tools = new TaskTools();
        tools.writeSignsCombinationsIntoArray(chars, "", chars.length, 3, possibleSigns);
        tools.writeNumbersCombinationsIntoArray(numbers, "", nums.length, possibleNumbers);
        tools.mergeSignsAndNumbersIntoArray(possibleSigns,possibleNumbers,possibleTasksWithoutBrackets);
        tools.writeCompletedTasksIntoArray(possibleTasksWithoutBrackets, completedTasks);
        return tools.solveTask(completedTasks);
    }
}
