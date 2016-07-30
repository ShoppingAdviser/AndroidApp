package com.example.nidatazeen.shoppingadviser1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Len on 09-03-2016.
 */
public class ImageDownloader extends AsyncTask<String, Void, String> {

    ImageView bmImage;
    Bitmap bitmap;

    public ImageDownloader(ImageView bmImage) {
        this.bmImage = bmImage;
    }
    @Override
    protected String doInBackground(String... urls) {
        try {
            return loadFromNetwork(urls[0]);
        } catch (IOException e) {
            return "Error";//getString(R.string.connection_error);
        }
    }

    /**
     * Uses the logging framework to display the output of the fetch
     * operation in the log fragment.
     */
    @Override
    protected void onPostExecute(String result) {
        this.bmImage.setImageBitmap(bitmap);
    }
    /** Initiates the fetch operation. */
    private String loadFromNetwork(String urlString) throws IOException {
        InputStream stream = null;
        String str ="";

        try {
            stream = downloadUrl(urlString);
            str = readIt(stream, 450000);
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
        conn.setReadTimeout(100000 /* milliseconds */);
        conn.setConnectTimeout(150000 /* milliseconds */);
//        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Start the query
        conn.connect();
        InputStream stream = conn.getInputStream();
        bitmap = BitmapFactory.decodeStream(stream);
        return stream;


        // END_INCLUDE(get_inputstream)
    }


    private String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }
}
