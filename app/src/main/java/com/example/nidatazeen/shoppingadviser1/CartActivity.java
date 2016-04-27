package com.example.nidatazeen.shoppingadviser1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class CartActivity extends AppCompatActivity {

    ModelProducts singlePdt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle b = this.getIntent().getExtras();
        if(b!=null)
            singlePdt = (ModelProducts)b.getSerializable("singleproduct");

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

/*        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });   */
        TextView totalTxtView = (TextView) findViewById(R.id.carttotalTextView);
        totalTxtView.setText(singlePdt.getProductDiscountPrice());
// priceTxtView.setPaintFlags(priceTxtView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        TextView titleTxtView = (TextView) findViewById(R.id.carttitletextview);
        titleTxtView.setText(singlePdt.getProductTitle());


       /* TextView priceTxtView = (TextView) findViewById(R.id.cartpriceTextView);
        priceTxtView.setText(singlePdt.getProductPrice());*/


     /*   TextView sellingTxtView = (TextView) findViewById(R.id.cartsoldbytextview);
        sellingTxtView.setText(singlePdt.getSoldby()); */


        TextView sku = (TextView) findViewById(R.id.cartsku);
        sku.setText(singlePdt.getProductSKU());

        ImageView imgView = (ImageView) findViewById(R.id.cartproductImage);
        Picasso.with(getApplicationContext()).load(singlePdt.getProductImageUrl()).into(imgView);

        Button checkoutbutton = (Button) findViewById(R.id.cartcheckoutButton);
        checkoutbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Intent checkoutIntent = new Intent().setClass(CartActivity.this, CheckoutActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("singleproduct", singlePdt);
                checkoutIntent.putExtras(b);
                startActivity(checkoutIntent);

            }
        });
        Button continueshpngbutton = (Button) findViewById(R.id.cartcontinueButton);
        continueshpngbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Intent continueshpngIntent = new Intent().setClass(CartActivity.this, LandingPageActivity.class);
                startActivity(continueshpngIntent);

            }
        });
    }

}
