package com.omidmsl.multiplyanddivisiononline;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public abstract class Connection {

    public static String ipv4 = "http://omidmsl.cloudsite.ir";

    public static String getDataFromServer(String fileName){
        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL(ipv4 + "/multiply_and_division_files/" + fileName);

            urlConnection = (HttpURLConnection) url
                    .openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            int responseCode = urlConnection.getResponseCode();
            Log.i("URLConnection" , "response code : \n" + responseCode);

            if (responseCode/100==4) {
                Log.i("URLConnection", "client error : \n");
                return "client error : " + responseCode;
            } else if (responseCode/100==5) {
                Log.i("URLConnection", "server error : \n");
                return "server error : " + responseCode;
            } else {

                String content = isToStr(urlConnection.getInputStream());

                Log.i("URLConnection", "content : \n" + content);
                return content;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

    public static boolean isOnline(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            // Network is present and connected
            isAvailable = true;
        }
        return isAvailable;
    }

    private static String isToStr(InputStream in){
        StringWriter writer = new StringWriter();
        try {
            IOUtils.copy(in, writer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return writer.toString();
    }

    public static String getDataFromServer(RequestPackage rp){
        String urlStr = ipv4;
        if (rp.getMethod().equals("GET"))
            urlStr+= "?" + rp.getEncodedParams();
        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL( urlStr + "/multiply_and_division_files/" + rp.getFileName());

            urlConnection = (HttpURLConnection) url
                    .openConnection();
            urlConnection.setRequestMethod(rp.getMethod());
            if (rp.getMethod().equals("POST")){
                urlConnection.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                writer.write(rp.getEncodedParams());
                writer.flush();
            }
            urlConnection.connect();


            int responseCode = urlConnection.getResponseCode();
            Log.i("URLConnection" , "response code : \n" + responseCode);

            if (responseCode/100==4) {
                Log.i("URLConnection", "client error : \n");
                return "client error : " + responseCode;
            } else if (responseCode/100==5) {
                Log.i("URLConnection", "server error : \n");
                return "server error : " + responseCode;
            } else {

                String content = isToStr(urlConnection.getInputStream());

                Log.i("URLConnection", "content : \n" + content);
                return content;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

    public static class RequestPackage{
        private String fileName;
        private String method;
        private Map<String , String> params;

        public RequestPackage(String fileName, String method, Map<String, String> params) {
            this.fileName = fileName;
            this.method = method;
            this.params = params;
        }

        public RequestPackage() {
            params = new HashMap<>();
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public Map<String, String> getParams() {
            return params;
        }

        public void setParams(Map<String, String> params) {
            this.params = params;
        }

        public void setParameter(String key, String value) {
            params.put(key , value);
        }

        public String getEncodedParams(){
            StringBuilder sb = new StringBuilder();
            for (String key : params.keySet()){
                try {
                    String value = URLEncoder.encode(params.get(key) , "UTF-8");
                    if (sb.length()>0)
                        sb.append('&');
                    sb.append(key).append("=").append(value);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
            return sb.toString();
        }

    }
}
