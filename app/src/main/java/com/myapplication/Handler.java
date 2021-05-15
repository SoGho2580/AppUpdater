package com.myapplication;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Handler {

    private static final String TAG = Handler.class.getSimpleName();

    public Handler() {
    }

    public String httpServiceCall(String requestUrl) {
        String result = null;
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            try {
                InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                result = convertResultToString(inputStream);
            } finally {
                connection.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String convertResultToString(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();

        String li = null;

        while (true) {
            try {
                if ((li = bufferedReader.readLine()) != null) {
                    stringBuilder.append(li + "\n");
                }
                //inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return stringBuilder.toString();
        }
    }
}
