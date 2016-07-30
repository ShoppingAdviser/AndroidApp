package com.example.nidatazeen.shoppingadviser1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
public class ContactUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button btnFoo = (Button) findViewById(R.id.btnFoo);
        btnFoo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

/* Fill it with Data */
                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"support@shoppingadviser.in"});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Text");

/* Send it off to the Activity-Chooser */
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            }
        });
        Button callbutton = (Button) findViewById(R.id.callbtn);
        callbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri number = Uri.parse("tel:+91 991669000");
                Intent callIntent = new Intent(Intent.ACTION_DIAL, number);

                startActivity(callIntent);
            }

        });

    }
}
