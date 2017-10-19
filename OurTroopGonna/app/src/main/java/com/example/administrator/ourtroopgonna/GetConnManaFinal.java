package com.example.administrator.ourtroopgonna;


import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import cz.msebera.android.httpclient.Header;

public class GetConnManaFinal {




    AsyncHttpClient asyncHttpClient;
    RequestParams requestParams;


    public GetConnManaFinal() {
        asyncHttpClient = new AsyncHttpClient();
        requestParams = new RequestParams();
    }
    public String sendGet(String url) throws Exception {

        Log.i("dbg","function called");
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        Log.i("dbg","conncection built");
        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", "Android");
        Log.i("dbg","req prp setted");
        //int responseCode = con.getResponseCode();
        Log.i("dbg","send request to "+url);
        //Log.i("dbg","got res code "+responseCode);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);

        }
        in.close();
        Log.i("dbg","res : "+response+", tostring : "+response.toString());
        //print result


        return(response.toString());

    }

    public String sendPost(String url,String params) throws Exception {

        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "Android");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");


        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(params);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + params);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        Log.i("dbg","res : "+response+", tostring : "+response.toString());
        //print result
        return response.toString();

    }

}

/**
 * Created by Administrator on 2017-10-18.
 */
