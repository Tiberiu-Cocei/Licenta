package com.android.android.utilities;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class ApiCaller extends AsyncTask<String, Void, ApiResponse> {

    @Override
    public ApiResponse doInBackground(String... strings) {
        String[] alphabet = new String[]{"PUT", "POST", "GET"};
        List<String> requestMethodList = Arrays.asList(alphabet);
        if(strings.length < 3
                || (!requestMethodList.contains(strings[0]))
                || (!strings[0].equals("GET") && strings.length < 4)) {
            return null;
        }
        String requestMethod = strings[0];
        String stringUrl = strings[1];
        String bearerToken = strings[2];
        String jsonBody = null;
        if(strings.length == 4) {
            jsonBody = strings[3];
        }

        if(requestMethod.equals("POST")) {
            return callPostApi(stringUrl, bearerToken, jsonBody);
        }

        else if(requestMethod.equals("GET")) {
            return null;
        }

        else {
            return null;
        }
    }

    private ApiResponse callPostApi(String stringUrl, String bearerToken, String jsonBody) {
        if(stringUrl == null || jsonBody == null) {
            return null;
        }
        try {
            URL apiUrl = new URL(stringUrl);
            HttpURLConnection connection = (HttpURLConnection)apiUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            if(bearerToken != null) {
                connection.setRequestProperty("Authorization", "Bearer " + bearerToken);
            }

            OutputStream os = connection.getOutputStream();
            byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);

            int responseCode = connection.getResponseCode();
            String responseMessage = connection.getResponseMessage();
            try {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder responseJson = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    responseJson.append(responseLine.trim());
                }
                return new ApiResponse(responseJson.toString(), responseCode, responseMessage);
            } catch(Exception e) {
                e.printStackTrace();
                return new ApiResponse(null, responseCode, responseMessage);
            }
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
