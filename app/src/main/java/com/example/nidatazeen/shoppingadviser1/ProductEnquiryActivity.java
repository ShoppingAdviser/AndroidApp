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

public class ProductEnquiryActivity extends AppCompatActivity {
    public EditText editText;
    EditText textMessage,textMessage1, emailtextMesage;
    String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_enquiry);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textMessage = (EditText) findViewById(R.id.EditEnquiry);
        textMessage1 = (EditText) findViewById(R.id.EditName);
        emailtextMesage = (EditText) findViewById(R.id.EditEmail);


        Button SendEnquiry = (Button) findViewById(R.id.ButtonSendEnquiry);

        SendEnquiry.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (textMessage.length() == 0 || textMessage1.length() == 0 || emailtextMesage.length() == 0) {
                    Toast.makeText(ProductEnquiryActivity.this, "Mandatory fields cannot be empty", Toast.LENGTH_LONG).show();

                } else {
                    if (emailtextMesage.length() != 0) {
                        //check if email is valid
                        if (isValidEmail(emailtextMesage.getText())) {

                            String message = textMessage.getText().toString();
                            String message1=textMessage1.getText().toString();
                            String emailtxtmessage =emailtextMesage.getText().toString();

                            StringBuilder total = new StringBuilder();
                            total.append("My name is:" + message1 + "\n"+ "Email is " + emailtxtmessage + "\n" +"Query is " + message);
                            String s= total.toString();
                            final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);


/* Fill it with Data */
                            emailIntent.setType("plain/text");
                            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"support@shoppingadviser.in"});
                            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");
                            emailIntent.putExtra(Intent.EXTRA_TEXT, s);


/* Send it off to the Activity-Chooser */
                            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                        } else {
                            Toast.makeText(ProductEnquiryActivity.this, "Invalid Email", Toast.LENGTH_LONG).show();

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


}