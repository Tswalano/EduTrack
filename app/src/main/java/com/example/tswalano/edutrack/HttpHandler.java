package com.example.tswalano.edutrack;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by Tswalano on 2018/03/19.
 */

public class HttpHandler {

    private static final String TAG = HttpHandler.class.getSimpleName();

    public HttpHandler(){

    }

    public String makeServiceCall(String reqURL){
        String response = null;

        try {
            URL url = new URL(reqURL);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
//            Read The Response
            InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
            response = converStreamToString(inputStream);
        }catch (MalformedURLException e){
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        }catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        }catch (IOException e){
            Log.e(TAG, "IOException: " + e.getMessage());
        }catch(Exception e){
          e.printStackTrace();
        }

        return response;
    }

    public String converStreamToString(InputStream inputStream){
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder builder = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null){
                builder.append(line).append('\n');
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                inputStream.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        return builder.toString();
    }
}
