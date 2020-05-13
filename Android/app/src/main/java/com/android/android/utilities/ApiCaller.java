package com.android.android.utilities;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ApiCaller extends AsyncTask<String, Void, String> {

    @Override
    public String doInBackground(String... strings) {
        String requestMethod = strings[0];
        String stringUrl = strings[1];
        String errorMessage = strings[2];

        if(requestMethod.equals("POST")) {
            String jsonBody = strings[3];
            try {
                URL apiUrl = new URL(stringUrl);
                HttpURLConnection connection = (HttpURLConnection)apiUrl.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; utf-8");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true);
                try(OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                } catch(Exception e) {
                    e.printStackTrace();
                    return "1";
                }
                try(BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    return response.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                    return "2";
                }
            } catch(Exception e) {
                //return errorMessage;
                e.printStackTrace();
                return "3";
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
