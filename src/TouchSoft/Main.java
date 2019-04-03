package TouchSoft;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        int[] nums = {3, 3, 7, 7};
        int[] nums1 = {3, 3, 8, 8};
        int[] nums2 = {4, 1, 8, 7};
        int[] nums3 = {1, 2, 1, 2};
        canBeEqualTo24(nums);
        canBeEqualTo24(nums1);
        canBeEqualTo24(nums2);
        canBeEqualTo24(nums3);
    }

    private static boolean canBeEqualTo24(int[] nums) {

        class Calculator {

            private ArrayList<String> tempTask;
            private String tempNumberOrSign;
            private Calculator calc;

            private String calculate(String task) {
                calc = new Calculator();
                while (task.contains(Character.toString('(')) || task.contains(Character.toString(')'))) {
                    for (int currentChar = 0; currentChar < task.length(); currentChar++) {
                        if (task.charAt(currentChar) == ')') {
                            for (int i = currentChar; i >= 0; i--) {
                                if (task.charAt(i) == '(') {
                                    String taskInsideBrackets = task.substring(i + 1, currentChar);
                                    taskInsideBrackets = calc.separateNumbersFromSignsAndGetResult(taskInsideBrackets);
                                    task = task.substring(0, i) + taskInsideBrackets + task.substring(currentChar + 1);
                                    currentChar = 0;
                                    i = 0;
                                }
                            }
                        }
                    }
                }
                return calc.separateNumbersFromSignsAndGetResult(task);
            }

            private String separateNumbersFromSignsAndGetResult(String task) {
                tempTask = new ArrayList<>();
                tempNumberOrSign = "";
                for (int currentChar = task.length() - 1; currentChar >= 0; currentChar--) {
                    if (Character.isDigit(task.charAt(currentChar))) {
                        tempNumberOrSign = task.charAt(currentChar) + tempNumberOrSign;
                        if (currentChar == 0) {
                            addCharToTempTaskIfExists();
                        }
                    } else {
                        if (task.charAt(currentChar) == '.') {
                            tempNumberOrSign = task.charAt(currentChar) + tempNumberOrSign;
                        } else if (task.charAt(currentChar) == '-'
                                && (currentChar == 0 || (!Character.isDigit(task.charAt(currentChar - 1))))) {
                            tempNumberOrSign = task.charAt(currentChar) + tempNumberOrSign;
                            addCharToTempTaskIfExists();
                        } else {
                            addCharToTempTaskIfExists();
                            tempNumberOrSign += task.charAt(currentChar);
                            addCharToTempTaskIfExists();
                        }
                    }
                }
                tempTask = getResult(tempTask, "*", "/");
                tempTask = getResult(tempTask, "+", "-");
                return tempTask.get(0);
            }

            private void addCharToTempTaskIfExists() {
                if (!tempNumberOrSign.equals("")) {
                    tempTask.add(0, tempNumberOrSign);
                    tempNumberOrSign = "";
                }
            }

            private ArrayList<String> getResult(ArrayList<String> numbersAndSignsInArray, String signOne, String signTwo) {
                BigDecimal result = new BigDecimal(0);
                for (int currentChar = 0; currentChar < numbersAndSignsInArray.size(); currentChar++) {
                    if (numbersAndSignsInArray.get(currentChar).equals(signOne) || numbersAndSignsInArray.get(currentChar).equals(signTwo)) {
                        switch (numbersAndSignsInArray.get(currentChar)) {
                            case "*":
                                result = new BigDecimal(numbersAndSignsInArray.get(currentChar - 1)).multiply(new BigDecimal(numbersAndSignsInArray.get(currentChar + 1)));
                                break;
                            case "/":
                                result = new BigDecimal(numbersAndSignsInArray.get(currentChar - 1)).divide(new BigDecimal(numbersAndSignsInArray.get(currentChar + 1)), 15, BigDecimal.ROUND_DOWN);
                                break;
                            case "+":
                                result = new BigDecimal(numbersAndSignsInArray.get(currentChar - 1)).add(new BigDecimal(numbersAndSignsInArray.get(currentChar + 1)));
                                break;
                            case "-":
                                result = new BigDecimal(numbersAndSignsInArray.get(currentChar - 1)).subtract(new BigDecimal(numbersAndSignsInArray.get(currentChar + 1)));
                                break;
                        }
                        try {
                            numbersAndSignsInArray.set(currentChar, (result.setScale(15, RoundingMode.HALF_DOWN)
                                    .stripTrailingZeros().toPlainString()));
                            numbersAndSignsInArray.remove(currentChar + 1);
                            numbersAndSignsInArray.remove(currentChar - 1);
                        } catch (Exception ignored) {
                            /* ignoring arithmetic exceptions */
                        }
                    } else {
                        continue;
                    }
                    currentChar = 0;
                }
                return numbersAndSignsInArray;
            }

        }

        class TaskTools {

            private char[] chars = {'+', '-', '*', '/'};
            private ArrayList<String> possibleSigns = new ArrayList<>();
            private ArrayList<String> possibleNumbers = new ArrayList<>();
            private ArrayList<String> possibleTasksWithoutBrackets = new ArrayList<>();
            private ArrayList<String> completedTasks = new ArrayList<>();

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

            private void writeNumbersCombinationsIntoArray(int[] numbers, String prefix, int numbersLength, ArrayList<String> array) {
                if (numbers.length == 1) {
                    prefix += numbers[0];
                    array.add(prefix);
                    return;
                }
                for (int i = 0; i < numbersLength; ++i) {
                    int[] newNumbers = new int[numbersLength - 1];
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

            private void mergeSignsAndNumbersIntoArray(ArrayList<String> signs, ArrayList<String> numbers, ArrayList<String> result) {
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
                //I didn't figured out, how to generate brackets
                //depending on the length of the mathematical problem.
                //Have an idea, how to do that, but time is almost run out.
                //I know, this method is really bad, but i can't solve it better yet.
                for (String task : tasksWithoutBrackets) {
                    for (int i = 0; i < 9; i++) {
                        String completedTask = task;
                        switch (i) {
                            case 0:
                                completedTask = "((" + task.substring(0, 3) + ")" + task.substring(3, 5) + ")" + task.substring(5); // ((n+n)+n)+n
                                break;
                            case 1:
                                completedTask = task.substring(0, 2) + "(" + task.substring(2, 4) + "(" + task.substring(4) + "))"; // n+(n+(n+n))
                                break;
                            case 2:
                                completedTask = "(" + task.substring(0, 3) + ")" + task.charAt(3) + "(" + task.substring(4) + ")"; // (n+n)+(n+n)
                                break;
                            case 3:
                                completedTask = "(" + task.substring(0, 3) + ")" + task.substring(3); // (n+n)+n+n
                                break;
                            case 4:
                                completedTask = task.substring(0, 2) + "(" + task.substring(2, 5) + ")" + task.substring(5); // n+(n+n)+n
                                break;
                            case 5:
                                completedTask = task.substring(0, 4) + "(" + task.substring(4) + ")"; // n+n+(n+n)
                                break;
                            case 6:
                                completedTask = "(" + task.substring(0, 5) + ")" + task.substring(5); // (n+n+n)+n
                                break;
                            case 7:
                                completedTask = task.substring(0, 2) + "(" + task.substring(2) + ")"; // n+(n+n+n)
                                break;
                        }
                        completedTasks.add(completedTask);
                    }
                }
                tasksWithoutBrackets.clear();
            }

            private void printInfo(String message, boolean answer) {
                System.out.println("На входе: " + Arrays.toString(nums));
                System.out.println("На выходе: " + answer);
                System.out.println("Пояснение: " + message);
                System.out.println();
            }

            private boolean solveTask() {
                writeSignsCombinationsIntoArray(chars, "", chars.length, 3, possibleSigns);
                writeNumbersCombinationsIntoArray(nums, "", nums.length, possibleNumbers);
                mergeSignsAndNumbersIntoArray(possibleSigns, possibleNumbers, possibleTasksWithoutBrackets);
                writeCompletedTasksIntoArray(possibleTasksWithoutBrackets, completedTasks);
                Calculator calculator = new Calculator();
                for (String answer : completedTasks) {
                    try {
                        DecimalFormat decimalFormat = new DecimalFormat("#.########");
                        if (decimalFormat.format(Double
                                .valueOf(calculator.calculate(answer)))
                                .equals("24")) {
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

        return new TaskTools().solveTask();
    }
}