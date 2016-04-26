package com.example.nidatazeen.shoppingadviser1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CheckoutActivity extends AppCompatActivity {

    public EditText editText;
    EditText textMessagefn,textMessageln, emailtextMesage, phonetextmessage, countrytextmessage, statetextmessage,streettextmessage, towntextmessage, ziptextmessage;

    ModelProducts singlePdt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        textMessagefn = (EditText) findViewById(R.id.checkoutfnameedittext);
        textMessageln=(EditText) findViewById(R.id.checkoutlnameedittext);
        emailtextMesage = (EditText)findViewById(R.id.checkoutemailedittext);
        phonetextmessage = (EditText)findViewById(R.id.checkoutphoneedittext);
        countrytextmessage = (EditText)findViewById(R.id.checkoutcountryedittext);
        streettextmessage = (EditText)findViewById(R.id.checkoutstreetedittext);
        towntextmessage = (EditText)findViewById(R.id.checkouttownedittext);
        statetextmessage = (EditText)findViewById(R.id.checkoutstateedittext);
        ziptextmessage = (EditText)findViewById(R.id.checkoutzipedittext);


        Button ordernowbutton = (Button) findViewById(R.id.checkoutordernowbutton);
        ordernowbutton.setOnClickListener(new View.OnClickListener()

        {
            public void onClick(View v) {


                //final Intent ordernowIntent = new Intent().setClass(CheckoutActivity.this, CustomDialog.class);
                //startActivity(ordernowIntent);

                CustomDialog cd=new CustomDialog(CheckoutActivity.this);
                cd.show();
            }
        });





        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    //create a method which will take all details and form a string out of it and pass it to email composer
    public void sendOrderEmail() {
        Bundle b = this.getIntent().getExtras();
        if(b!=null)
            singlePdt = (ModelProducts)b.getSerializable("singleproduct");


        String messagefn = textMessagefn.getText().toString();
        String messageln = textMessageln.getText().toString();
        String emailtxtmessage = emailtextMesage.getText().toString();
        String phonetxtmessage = phonetextmessage.getText().toString();
        String countrytxtmessage = countrytextmessage.getText().toString();
        String streettxtmessage = streettextmessage.getText().toString();
        String towntxtmessage = towntextmessage.getText().toString();
        String statetxtmessage = statetextmessage.getText().toString();
        String ziptxtmessage = ziptextmessage.getText().toString();


        StringBuilder total = new StringBuilder();
        total.append("product title is:" + singlePdt.getProductTitle() + "\n"+ "product id is:" + singlePdt.getProductId() + "\n"  + "product sku is:" + singlePdt.getProductSKU() + "\n"  + "Product discount price is:" + singlePdt.getProductDiscountPrice() + "\n" +  "\n" +"My first name is:" + messagefn + "\n" + "My last name is:" + messageln + "\n" + "Email is " + emailtxtmessage + "\n" + "Phone is " + phonetxtmessage + "\n" + "Country is:" + countrytxtmessage + "\n" + "Street is:" + streettxtmessage + "\n" + "Town is:" + towntxtmessage + "\n" + "State is:" + statetxtmessage + "\n" + "Zip is:" + ziptxtmessage + "\n");
        String s = total.toString();
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);


/* Fill it with Data */
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"support@shoppingadviser.in"});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, s);



/* Send it off to the Activity-Chooser */
        startActivity(Intent.createChooser(emailIntent, "Send mail..."));

    }
}
