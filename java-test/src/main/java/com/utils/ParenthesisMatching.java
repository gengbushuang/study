package com.utils;

import java.util.*;

public class ParenthesisMatching {
    List<Character> list = new ArrayList<>();

    public boolean matching(char[] symbols) {

        for (char symbol : symbols) {
            switch (symbol) {
                case '(':
                case '[':
                case '{':
                    list.add(symbol);
                    break;
                case '}':
                case ']':
                case ')':
                    if (list.isEmpty()) {
                        return false;
                    }
                    char c = list.get(list.size() - 1);
                    if ((c == '(' && symbol == ')') ||
                            (c == '[' && symbol == ']') ||
                            (c == '{' && symbol == '}')) {
                        list.remove(list.size() - 1);
                        break;
                    } else {
                        return false;
                    }
                default:
            }
        }

        return list.isEmpty();
    }

    public boolean matching(String symbol) {
        if (symbol == null || symbol.trim().length() == 0) {
            return false;
        }
        char[] chars = symbol.toCharArray();
        return this.matching(chars);
    }

    public static void main(String[] args) {
        String symbol = "(([])))[]{}";
        symbol = "([)]";
        boolean matching = new ParenthesisMatching().matching(symbol);
        System.out.println(symbol + " is " + matching);
    }
}
