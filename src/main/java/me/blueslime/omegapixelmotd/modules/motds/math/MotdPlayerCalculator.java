package me.blueslime.omegapixelmotd.modules.motds.math;

import java.util.Stack;

public class MotdPlayerCalculator {
    public static int findExpression(String expression) {
        expression = expression.replaceAll("\\s+", "");

        Stack<Character> operatorList = new Stack<>();
        Stack<Integer> operatorTask = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char character = expression.charAt(i);

            if (Character.isDigit(character)) {

                int num = 0;

                while (i < expression.length() && Character.isDigit(expression.charAt(i))) {
                    num = num * 10 + (expression.charAt(i) - '0');
                    i++;
                }

                i--;

                operatorTask.push(num);
            } else if (character == '(') {
                operatorList.push(character);
            } else if (character == ')') {
                while (!operatorList.isEmpty() && operatorList.peek() != '(') {
                    operatorTask.push(applyOperator(operatorList.pop(), operatorTask.pop(), operatorTask.pop()));
                }

                operatorList.pop();
            } else if (character == '+' || character == '-' || character == '*' || character == '/' || character == '%') {
                while (!operatorList.isEmpty() && priorityOperator(character) <= priorityOperator(operatorList.peek())) {
                    operatorTask.push(applyOperator(operatorList.pop(), operatorTask.pop(), operatorTask.pop()));
                }

                operatorList.push(character);
            }
        }

        while (!operatorList.isEmpty()) {
            operatorTask.push(applyOperator(operatorList.pop(), operatorTask.pop(), operatorTask.pop()));
        }

        return operatorTask.pop();
    }

    private static int applyOperator(char operador, int b, int a) {
        switch (operador) {
            case '+':
                return a + b;
            case '%':
                return a % b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) {
                    return 0;
                }
                return a / b;
        }
        return 0;
    }

    private static int priorityOperator(char operador) {
        switch (operador) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
            case '%':
                return 2;
        }
        return 0;
    }
}
