package com.example.nidatazeen.shoppingadviser1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

public class MyOrder extends AppCompatActivity {

    public EditText editText;
    EditText textMessagefn,textMessageln, emailtextMesage, phonetextmessage, countrytextmessage, statetextmessage,streettextmessage, towntextmessage, ziptextmessage;
DatabaseHandler db;
    ModelProducts singlePdt;
    String quantity = "";
    String emailstring = "";
    private List<ModelProducts> mCartList;

    File imageFile = null;
String tablestring ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new DatabaseHandler(this);

        mCartList = db.getCartproducts(null, null, null);

        Bundle b = this.getIntent().getExtras();
        if (b != null) {

            quantity = b.getSerializable("quantity").toString();
            emailstring = b.getSerializable("emailstring").toString();
        }
       final Button confirmbtn = (Button) findViewById(R.id.confirmbutton);
        confirmbtn.setVisibility(View.VISIBLE);
        confirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmbtn.setVisibility(View.GONE);
                Date now = new Date();
                android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

                try {
                    // image naming and path  to include sd card  appending name you choose for file

                    String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";


                    // create bitmap screen capture
                    View v1 = getWindow().getDecorView().getRootView();
                    v1.setDrawingCacheEnabled(true);
                    Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
                    v1.setDrawingCacheEnabled(false);

                    imageFile = new File(mPath);

                    FileOutputStream outputStream = new FileOutputStream(imageFile);
                    int quality = 100;
                    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
                    outputStream.flush();
                    outputStream.close();

//                    openScreenshot(imageFile);

                } catch (Throwable e) {
                    // Several error may come out with file handling or OOM

                    e.printStackTrace();
                }
                Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_UNMOUNTED);

                if(isSDPresent)
                {
                    // yes SD-card is present
                    sendOrderEmail(emailstring, imageFile);

                }
                else
                {
                    // Sorry

                    sendOrderEmail(emailstring, null);

                }
finish();
            }

        });




        setUpWebView();


    }

    private void setUpWebView() {
        float totalprice = 0.0f;
        String myTable = "<table border=1>"+ "<tr>" +
                "<td>Product title</td>" +
                "<td>Product id</td>" +
                "<td>Quantity</td>" +
                "<td>Price</td>" +
                "</tr>";

        for (int i = 0; i <mCartList.size() ; i++) {
            float quantity=Integer.parseInt(mCartList.get(i).getProductqty().toString()) * 1.0f;
            myTable = myTable +
                    "<tr>" +
                    "<td>" + mCartList.get(i).getProductTitle() + "</td>" +
                    "<td>" + mCartList.get(i).getProductIdentifier() + "</td>" +
                    "<td>" + mCartList.get(i).getProductqty() + "</td>" +
                    "<td>" + mCartList.get(i).getProductDiscountPrice() + "</td>" +
                    "</tr>";
            tablestring = tablestring +"\n"+"Product id: " + mCartList.get(i).getProductIdentifier()+ "\n"+"Product title is: " + mCartList.get(i).getProductTitle() +"\n"+ "Quantity: "+ mCartList.get(i).getProductqty()+"\n" + "Price: "+mCartList.get(i).getProductDiscountPrice()+"\n" ;
        totalprice = totalprice +( Float.parseFloat(mCartList.get(i).getProductDiscountPrice())*quantity);
        }
        tablestring = tablestring + "\n"+ "\n" + "Total price is "+ totalprice;
        myTable = myTable +  "<tr>" +
                "<td>" + "Total Price" + "</td>" +
                "<td>" + totalprice + "</td>" +"</tr>"+"</table>";

        WebView browser = (WebView) findViewById(R.id.webview);
        browser.loadDataWithBaseURL(null, myTable, "text/html", "utf-8", null);
    }

    public void sendOrderEmail(String emailstr, File imageFile) {


        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);


        emailIntent.setType("text/html");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"support@shoppingadviser.in"});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");

        Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_UNMOUNTED);

        if(isSDPresent)
        {
    Uri uri = Uri.fromFile(imageFile);
   // Toast.makeText(MyOrder.this, "uri is " + uri, Toast.LENGTH_LONG).show();
    emailIntent.setType("image/*");
    emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
            emailIntent.putExtra(Intent.EXTRA_TEXT, (emailstr));

        } else {
    emailIntent.putExtra(Intent.EXTRA_TEXT, (emailstr + tablestring));

}
        startActivity(Intent.createChooser(emailIntent, "Send mail..."));

    }

    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }


}


