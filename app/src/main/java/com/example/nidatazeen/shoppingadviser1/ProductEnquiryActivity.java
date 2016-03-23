package com.example.nidatazeen.shoppingadviser1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
        textMessage1=(EditText) findViewById(R.id.EditName);
        emailtextMesage = (EditText)findViewById(R.id.EditEmail);

        Button SendEnquiry = (Button) findViewById(R.id.ButtonSendEnquiry);

        SendEnquiry.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

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

            }
        });


    }

}
