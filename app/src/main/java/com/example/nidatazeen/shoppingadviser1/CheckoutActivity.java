package com.example.nidatazeen.shoppingadviser1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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


        EditText emailTxt = (EditText) findViewById(R.id.checkoutemailedittext);
        emailTxt.setText("taskeenfathima28@gmail.com");

        Button ordernowbutton = (Button) findViewById(R.id.checkoutordernowbutton);
        ordernowbutton.setOnClickListener(new View.OnClickListener()

        {
            public void onClick(View v) {

                if (emailtextMesage.length() == 0 || textMessagefn.length() == 0 || textMessageln.length() == 0 || phonetextmessage.length() == 0 || countrytextmessage.length() == 0 || streettextmessage.length() == 0 || towntextmessage.length() == 0 || statetextmessage.length() == 0 || ziptextmessage.length() == 0) {
                    Toast.makeText(CheckoutActivity.this, "Mandatory fields cannot be empty", Toast.LENGTH_LONG).show();

                } else {
                    if (emailtextMesage.length() != 0) {
                        //check if email is valid
                        if (isValidEmail(emailtextMesage.getText())) {

                            String userNameString = emailtextMesage.getText().toString();
                            CustomDialog cd = new CustomDialog(CheckoutActivity.this);
                            cd.show();

                        } else {
                            Toast.makeText(CheckoutActivity.this, "Invalid Email", Toast.LENGTH_LONG).show();

                        }
                    }
                }
            }
        });

    }

    public final static boolean isValidEmail (CharSequence target) {
        if (target == null)
            return false;
        else {
            return Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
    //create a method which will take all details and form a string out of it and pass it to email composer
    public void sendOrderEmail() {
        Bundle b = this.getIntent().getExtras();
        String quantity = "";
        if(b!=null) {
            singlePdt = (ModelProducts)b.getSerializable("singleproduct");
            quantity = b.getSerializable("quantity").toString();
        }


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
        total.append("Hi. I would like to place an order for the following product : "+ "\n"+ "The products title is : " + singlePdt.getProductTitle() + "\n"+
                "The product id is : " + singlePdt.getProductIdentifier() + "\n"  + "The products sku is:" + singlePdt.getProductSKU() + "\n"
                + "Products price is : " + singlePdt.getProductDiscountPrice() + "\n" +  "\n"
                + "My details are : " + "\n"
                + "My first name is : " + messagefn + "\n" + "My last name is : " + messageln + "\n" + "My Email-id is : " + emailtxtmessage + "\n"
                + "My contact number is : " + phonetxtmessage + "\n" + "My Address is : " + "\n"
                + "Country : " + countrytxtmessage + "\n"
                + "Street : " + streettxtmessage + "\n" + "Town  : " + towntxtmessage + "\n"
                + "State  : " + statetxtmessage + "\n" + "Zip code is :" + ziptxtmessage + "\n"
                + "I would require a quantity of : " + quantity + "\n");
        String s = total.toString();


        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);


/* Fill it with Data */
        emailIntent.setType("text/html");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"support@shoppingadviser.in"});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, s);

        //emailIntent.setType("application/image");
        //emailIntent.putExtra(android.content.Intent.EXTRA_STREAM, R.drawable.shoppingadviserlogo);





/* Send it off to the Activity-Chooser */
        startActivity(Intent.createChooser(emailIntent, "Send mail..."));

    }
}
