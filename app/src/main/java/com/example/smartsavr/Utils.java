package com.example.smartsavr;

public class Utils {
    public static String centsToDollarString(int cents) {
        return centsToDollarString(cents, true);
    }

    public static String centsToDollarString(int cents, boolean includeDollarSign) {
        int remainder = cents % 100;
        if (remainder == 0) {
            return (includeDollarSign ? "$" : "") + cents / 100;
        }
        return (includeDollarSign ? "$" : "") + cents / 100 + "." + padLeftZeros(remainder, 2);
    }

    /**
     * Converts dollar string (e.g. "8.54", "", "2", "0.0") to cents.
     */
    public static int dollarStringToCents(String dollarString) {
        if (dollarString.isBlank()) {
            return 0;
        } else {
            return (int) (Double.parseDouble(dollarString) * 100);
        }
    }

    public static String padLeftZeros(int number, int length) {
        String numberString = Integer.toString(number);
        if (numberString.length() >= length) {
            return numberString;
        }
        StringBuilder builder = new StringBuilder();
        while (builder.length() < length - numberString.length()) {
            builder.append("0");
        }
        builder.append(numberString);

        return builder.toString();
    }
}
