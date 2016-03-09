package com.example.nidatazeen.shoppingadviser1;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by Len on 09-03-2016.
 */

public class ImageDownloader extends AsyncTask<String, String, Bitmap> {
    ImageView bmImage; //= (ImageView)findViewById(R.id.productimageView);
    ProgressDialog pDialog;
    Bitmap bitmap;

    public ImageDownloader(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        pDialog = new ProgressDialog(LandingPageActivity.this);
//        pDialog.setMessage("Loading Image ....");
//        pDialog.show();

    }
    protected Bitmap doInBackground(String... args) {
        try {
            bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    protected void onPostExecute(Bitmap image) {

        if(image != null){
            bmImage.setImageBitmap(image);
//            pDialog.dismiss();

        }else{

//            pDialog.dismiss();
//            Toast.makeText(LandingPageActivity.this, "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();

        }
    }
}

