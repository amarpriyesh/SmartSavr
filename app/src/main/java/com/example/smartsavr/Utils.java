package com.example.smartsavr;

public class Utils {
    public static String centsToDollarString(int cents) {
        return "$" + cents / 100 + "." + padLeftZeros(cents % 100, 2);
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
