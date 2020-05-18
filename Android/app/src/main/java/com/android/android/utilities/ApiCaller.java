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
        if(strings.length < 4 || !requestMethodList.contains(strings[0])) {
            return null;
        }
        String requestMethod = strings[0];
        String stringUrl = strings[1];
        String bearerToken = strings[2];
        String body = strings[3];

        if(requestMethod.equals("POST")) {
            return callPostOrPutApi(stringUrl, bearerToken, body, "POST");
        }

        else if(requestMethod.equals("GET")) {
            return callGetApi(stringUrl, bearerToken, body);
        }

        else {
            return callPostOrPutApi(stringUrl, bearerToken, body, "PUT");
        }
    }

    private ApiResponse callGetApi(String stringUrl, String bearerToken, String getId) {
        if(stringUrl == null) {
            return null;
        }
        try {
            if(getId != null) {
                stringUrl += "/" + getId;
            }
            URL apiUrl = new URL(stringUrl);
            HttpURLConnection connection = (HttpURLConnection)apiUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);
            if(bearerToken != null) {
                connection.setRequestProperty("Authorization", "Bearer " + bearerToken);
            }

            int responseCode = connection.getResponseCode();
            String responseMessage = connection.getResponseMessage();
            try {
                return processApiStream(connection, responseCode, responseMessage);
            } catch(Exception e) {
                e.printStackTrace();
                return new ApiResponse(null, responseCode, responseMessage);
            }
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private ApiResponse callPostOrPutApi(String stringUrl, String bearerToken, String jsonBody, String type) {
        if(stringUrl == null || jsonBody == null) {
            return null;
        }
        try {
            URL apiUrl = new URL(stringUrl);
            HttpURLConnection connection = (HttpURLConnection)apiUrl.openConnection();
            connection.setRequestMethod(type);
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);
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
                return processApiStream(connection, responseCode, responseMessage);
            } catch(Exception e) {
                e.printStackTrace();
                return new ApiResponse(null, responseCode, responseMessage);
            }
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private ApiResponse processApiStream(HttpURLConnection connection, int responseCode, String responseMessage) throws Exception {
        BufferedReader bufferedReader;
        if(responseCode < 400) {
            bufferedReader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
        }
        else {
            bufferedReader = new BufferedReader(
                    new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8));
        }
        StringBuilder responseJson = new StringBuilder();
        String responseLine;
        while ((responseLine = bufferedReader.readLine()) != null) {
            responseJson.append(responseLine.trim());
        }
        return new ApiResponse(responseJson.toString(), responseCode, responseMessage);
    }

}
