package com.example.wt_sample1;

import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

        static String USADARA_BASE_URL = "";
        static String USADARA_BASE_URL_GET_SERVICE="https://oracleapex.com/ords/tanganelli/servicerecords/servicerecords";
        static String username = "adm";
        static String password = "12345678";


        public static URL buildUrl(){

            Uri urlStr = Uri.parse(USADARA_BASE_URL_GET_SERVICE).buildUpon().build();
            URL url= null;
            try {
                url = new URL(urlStr.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            return url;
        }
    public static String getDatafromHttpUrl(URL url, String cod_pesquisa) throws IOException {
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        // para conectar com usuario e senha (OAUTH)
        String authString = username + ":" + password;
        connection.setRequestProperty("grant_type", "client_credentials" );
        connection.setRequestProperty("content_type", "application/x-www-form-urlencoded" );

        String authHeaderValue = "Basic " + Base64.encodeToString(authString.getBytes(), Base64.NO_WRAP);
        connection.setRequestProperty("Authorization", authHeaderValue);
        connection.setRequestProperty("COD_PESQUISA", cod_pesquisa);
        connection.setRequestMethod("GET");
        try {
            InputStream stream = connection.getInputStream();
            Scanner scanner = new Scanner(stream);
            scanner.useDelimiter("\\A");
            boolean hasnext = scanner.hasNext();
            if (hasnext) {
                return scanner.next();
            } else
                return null;
        }
        finally {
            connection.disconnect();
        }

    }
        public static String putDatatoHttpUrl(URL url , String postDataStr) throws IOException {
            HttpURLConnection connection = null;
            try {
                 connection = (HttpURLConnection)url.openConnection();
                // para conectar com usuario e senha (OAUTH)
                String authString = username + ":" + password;
                connection.setRequestProperty("grant_type", "client_credentials" );
                connection.setRequestProperty("content_type", "application/x-www-form-urlencoded" );

                String authHeaderValue = "Basic " + Base64.encodeToString(authString.getBytes(), Base64.NO_WRAP);
                connection.setRequestProperty("Authorization", authHeaderValue);
                connection.setRequestMethod("PUT");

                OutputStream out = new BufferedOutputStream(connection.getOutputStream());
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                        out, "UTF-8"));
                //writer.write(MainActivity.postData.toString());
                writer.write(postDataStr);
                writer.flush();

                int code = connection.getResponseCode();
                if (code !=  201) {
                    throw new IOException("Invalid response from server: " + code);
                }

                BufferedReader rd = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));
                String line;
                while ((line = rd.readLine()) != null) {
                    Log.i("data", line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        return "zz";

        }
}
