package com.example.nidatazeen.shoppingadviser1;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebRequest extends AsyncTask<String, Void, String> {

    Handler handler;
    String pagecount;

    public WebRequest(Handler handler) {
        this.handler = handler;
    }
    @Override
    protected String doInBackground(String... urls) {
        try {
            return loadFromNetwork(urls[0]);
        } catch (IOException e) {

            return "Error";//getString(R.string.connection_error);
        }
    }

    @Override
    protected void onPostExecute(final String result)
    {
        Message msg = Message.obtain();
        Map<String, Object> map = new HashMap<String, Object>() {{
        put("pagecount", pagecount);
        put("json", result);
    }};
        if (pagecount != null)
        msg.obj = map;
        else
        msg.obj = "pc";
        this.handler.sendMessage(msg);
    }





    /**
     * Uses the logging framework to display the output of the fetch
     * operation in the log fragment.
     */

    /** Initiates the fetch operation. */
    private String loadFromNetwork(String urlString) throws IOException {
        InputStream stream = null;
        String str ="";

        try {
            stream = downloadUrl(urlString);
            str = readIt(stream, 1048576);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
        return str;
    }

    /**
     * Given a string representation of a URL, sets up a connection and gets
     * an input stream.
     * @param urlString A string representation of a URL.
     * @return An InputStream retrieved from a successful HttpURLConnection.
     * @throws java.io.IOException
     */
    private InputStream downloadUrl(String urlString) throws IOException {
        // BEGIN_INCLUDE(get_inputstream)
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(1000000000 /* milliseconds */);
        conn.setConnectTimeout(1500000000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Start the query
        conn.connect();
        InputStream stream = conn.getInputStream();

        try {
            Map<String, List<String>> map = conn.getHeaderFields();
            List<String> pagecountList = map.get("x-wc-totalpages");

            if (pagecountList == null) {
            } else {
                for (String header : pagecountList) {
                    pagecount = header;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stream;

        // END_INCLUDE(get_inputstream)
    }




//            Log.i(TAG, result);

    private String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }
}

