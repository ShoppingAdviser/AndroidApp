package com.example.nidatazeen.shoppingadviser1;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Lenovo on 18-04-2016.
 */
public class PostClass extends AsyncTask<String, Void, String> {

    Handler handler;

    public PostClass(Handler handler) {
        this.handler = handler;
    }


    protected void onPreExecute(){

    }

    @Override
    protected String doInBackground(String... params) {

        return performPostCall(params[0], null);
    }

    @Override
    protected void onPostExecute(final String result)
    {
        Message msg = Message.obtain();
        Map<String, Object> map = new HashMap<String, Object>() {{
           put("json", result);
        }};
            msg.obj = map;
        this.handler.sendMessage(msg);
    }

    private String  performPostCall(String requestURL,
                                   HashMap<String, String> postDataParams) {

        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(1000000000);
            conn.setConnectTimeout(1500000000);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type",
                    "application/json");

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
//            writer.write(getPostDataString(postDataParams));
            writer.write("{\n" +
                    "  \"customer\": {\n" +
                    "    \"email\": \"john.doe@example.com\",\n" +
                    "    \"first_name\": 'John',\n" +
                    "    last_name: 'Doe',\n" +
                    "    username: 'john.doe',\n" +
                    "    billing_address: {\n" +
                    "      first_name: 'John',\n" +
                    "      last_name: 'Doe',\n" +
                    "      company: '',\n" +
                    "      address_1: '969 Market',\n" +
                    "      address_2: '',\n" +
                    "      city: 'San Francisco',\n" +
                    "      state: 'CA',\n" +
                    "      postcode: '94103',\n" +
                    "      country: 'US',\n" +
                    "      email: 'john.doe@example.com',\n" +
                    "      phone: '(555) 555-5555'\n" +
                    "    },\n" +
                    "    shipping_address: {\n" +
                    "      first_name: 'John',\n" +
                    "      last_name: 'Doe',\n" +
                    "      company: '',\n" +
                    "      address_1: '969 Market',\n" +
                    "      address_2: '',\n" +
                    "      city: 'San Francisco',\n" +
                    "      state: 'CA',\n" +
                    "      postcode: '94103',\n" +
                    "      country: 'US'\n" +
                    "    }\n" +
                    "  }\n" +
                    "}");
            writer.flush();
            writer.close();
            os.close();

            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            }
            else {
                response="";

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }
}
