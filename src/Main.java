import java.io.IOException;
import java.util.Scanner;

import static java.lang.String.join;
import static java.util.Collections.nCopies;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите математическое выражение в формате 'a + b'. " +
                "Доступные операции : '+' '-' '*' '/'. " +
                "Оба числа должны быть в диапазоне от 1 до 10 включительно. ");
        System.out.println("При использовании римских цифр " +
                "оба числа должны быть в диапазоне от I до X включительно.");

        String input = scanner.nextLine();
        System.out.println("Результат вычислений: " + calc(input));
        scanner.close();

    }

    public static String calc(String input) throws IOException {

        // Т.к. мы подразумеваем, что выражение в нашем калькуляторе состоит из двух операндов и одного оператора,
        // то можем поделить выражение на 3 части и поместить в массив строк соответствующей длины
        // в примерах input явно указано, что между операндом и оператором есть пробел,
        // поэтому я использую его в качестве делиметра

        String[] mathExpression = input.split(" ");

        // если введено больше, чем 3 участника выражения(операторов/операндов), то мы можем выбросить исключение
        if (mathExpression.length > 3) {
            throw new IOException("Формат математической операции не удовлетворяет заданию " +
                    "- два операнда и один оператор (+, -, /, *)");
        }

        int firstNumber; // здесь будет храниться первый операнд из входных данных
        int secondNumber; // второй операнд
        String mathOperator = mathExpression[1]; // математический оператор
        String result = "Не удалось вычислить";

        // валидация входных данных
        // проверяем, что же содержится в строке переданной через консоль
        // арабские числа, римские или же просто какая-то строка
        // выбираем для каждого варианта соответствующую обработку данных

        if (isArabicNumber(mathExpression[0]) && isArabicNumber(mathExpression[2])) {

            firstNumber = Integer.parseInt(mathExpression[0]);
            secondNumber = Integer.parseInt(mathExpression[2]);
            result = arabicNumeralCalculator(firstNumber, secondNumber, mathOperator);

        } else if (isRomanNumber(mathExpression[0]) && isRomanNumber(mathExpression[2])) {

            firstNumber = romanToArabicNumber(mathExpression[0]);
            secondNumber = romanToArabicNumber(mathExpression[2]);

            try {
                result = arabicToRomanNumber(Integer.parseInt(
                        arabicNumeralCalculator(firstNumber, secondNumber, mathOperator)));

            } catch (IllegalArgumentException e) {

                System.out.println("Результатом работы калькулятора с римскими числами" +
                        " могут быть только положительные числа от I и больше");

            }

        } else if (isRomanNumber(mathExpression[0]) && isArabicNumber(mathExpression[2]) ||
                isArabicNumber(mathExpression[0]) && isRomanNumber(mathExpression[2])) {

            throw new IOException("Используются разные системы счисления внутри одного выражения");
        } else {
            System.err.println("Введенные данные не являются арабскими или римскими числами. Работа завершена.");
        }

        return result;
    }

    private static String arabicNumeralCalculator(int firstNumber, int secondNumber,
                                                  String mathOperation) throws IOException {

        String result;

        if ((firstNumber < 1 || firstNumber > 10) || (secondNumber < 1 || secondNumber > 10)) {
            throw new IOException("Калькулятор работает только с числами от 1 до 10 включительно");
        }

        switch (mathOperation) {
            case "+":
                result = String.valueOf(firstNumber + secondNumber);
                break;
            case "-":
                result = String.valueOf(firstNumber - secondNumber);
                break;
            case "*":
                result = String.valueOf(firstNumber * secondNumber);
                break;
            case "/":
                result = String.valueOf(firstNumber / secondNumber);
                break;
            default:
                throw new IOException("Строка не является математической операцией");
        }
        return result;
    }


    public static String arabicToRomanNumber(int number) throws IllegalArgumentException {
        if (number > 0) {
            return join("", nCopies(number, "I"))
                    .replace("IIIII", "V")
                    .replace("IIII", "IV")
                    .replace("VV", "X")
                    .replace("VIV", "IX")
                    .replace("XXXXX", "L")
                    .replace("XXXX", "XL")
                    .replace("LL", "C");
        } else {
            throw new IllegalArgumentException("Результатом работы калькулятора с римскими числами" +
                    " могут быть только положительные числа от I и больше");
        }

    }

    public static int romanToArabicNumber(String romanNumber) throws IOException {

        RomanNumeral[] romanNumerals = RomanNumeral.values();

        int result = 0;

        for (RomanNumeral romanNumeral : romanNumerals) {
            if (romanNumber.equalsIgnoreCase(romanNumeral.toString())) {
                result = romanNumeral.getValue();
            }
        }
        if (result == 0) {
            throw new IOException("Введенное римское число находится вне допустимого диапазона или не существует");
        }
        return result;
    }

    public static boolean isArabicNumber(String string) throws IOException {

        if (string == null || string.equals("")) {
            throw new IOException("Формат математической операции не удовлетворяет заданию - введена пустая строка");
        }
        // проверка - содержится ли в строке целое число
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            // не выбрасываю исключение т.к. если это не число, то это может быть римская цифра или просто строка
            // это будет проверяться после выполнения этого метода
        }
        return false;
    }

    public static boolean isRomanNumber(String string) throws IOException {

        if (string == null || string.equals("")) {
            throw new IOException("Формат математической операции не удовлетворяет заданию - введена пустая строка");
        }

        RomanNumeral[] romanNumerals = RomanNumeral.values();

        for (RomanNumeral romanNumeral : romanNumerals) {
            if (string.equalsIgnoreCase(romanNumeral.toString())) {
                return true;
            }
        }
        return false;
    }


}





