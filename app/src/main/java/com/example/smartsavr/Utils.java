package com.example.smartsavr;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Utils {

    public static final String CHILD = "child";
    public static final String PARENT_ID = "parentId";
    public static final String CHILD_ID = "childId";
    private static final String SERVER_KEY = "key=AAAAREXjElc:APA91bHrl3E3Ip2PVR8NOLaXU9a4L1M9TyurPbzaJOPgUd25Rr1K9PDTYLGroc_USYVQymaEbwUChS1tfhQel1tfXFZ1bc5WairAtObrpEp9m089txnM-ZEcUdPHLsBUyoJFOuK176eY";

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
    public static int dollarStringToCents(String dollarAmount) {
        if (dollarAmount.isBlank()) {
            return 0;
        }
        int dotIndex = dollarAmount.indexOf(".");
        int dollars;
        if (dotIndex == 0) {
            dollars = 0;
        } else if (dotIndex > 0) {
            dollars = Integer.parseInt(dollarAmount.substring(0, dotIndex));
        } else {
            dollars = Integer.parseInt(dollarAmount);
        }
        int cents = 0;
        if (dotIndex >= 0 && dotIndex < dollarAmount.length() - 1) {
            String centsStr = dollarAmount.substring(dotIndex + 1);
            if (centsStr.length() >= 2) {
                cents = Integer.parseInt(centsStr.substring(0, 2));
            } else if (centsStr.length() == 1) {
                cents = Integer.parseInt(centsStr + "0");
            }
        }
        return dollars * 100 + cents;
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


    public static int getImageResource(int profilePicture) {
        switch (profilePicture) {
            case 0:
                return R.drawable.asset_1;
            case 1:
                return R.drawable.asset_2;
            case 2:
                return R.drawable.asset_3;
            case 3:
                return R.drawable.asset_4;
            case 4:
                return R.drawable.asset_5;
            case 5:
                return R.drawable.asset_6;
            case 6:
                return R.drawable.asset_7;
            case 7:
                return R.drawable.asset_8;
            case 8:
                return R.drawable.asset_9;
            case 9:
                return R.drawable.asset_10;
            case 10:
                return R.drawable.asset_11;
            case 11:
                return R.drawable.asset_12;
            case 12:
                return R.drawable.asset_13;
            case 13:
                return R.drawable.asset_14;
            case 14:
                return R.drawable.asset_15;
            case 15:
                return R.drawable.asset_16;
            default:
                return R.drawable.unknown_profile_picture;
        }
    }

    public static String fcmHttpConnection(JSONObject jsonObject) {

        // try to open the HTTP connection
        try {
            // Open the HTTP connection and send the payload
            HttpURLConnection conn = (HttpURLConnection) new URL("https://fcm.googleapis.com/fcm/send").openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", SERVER_KEY);
            conn.setDoOutput(true);

            // Send FCM message content as output stream
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jsonObject.toString().getBytes());
            outputStream.close();

            // Read FCM input stream
            InputStream inputStream = conn.getInputStream();
            return convertStreamToString(inputStream);

            // look for IOExceptions - if they happen, return NULL
        } catch (IOException e) {
            Log.e("FCM Connection", "Error sending message content");
            return null;
        }
    }

    public static String convertStreamToString(InputStream inputStream) {
        // creating a new string builder
        StringBuilder stringBuilder = new StringBuilder();

        // read in stream
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String len;

            // append to stringBuilder
            while ((len = bufferedReader.readLine()) != null) {
                stringBuilder.append(len);
            }

            // close and return stringBuilder
            bufferedReader.close();
            return stringBuilder.toString().replace(",", ",\n");

        } catch (Exception e) {
            e.printStackTrace();
        }

        // otherwise return an empty string
        return "";
    }


}
