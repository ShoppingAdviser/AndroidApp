package com.example.nidatazeen.shoppingadviser1;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

public class ProductDetailActivity extends AppCompatActivity {
    NumberPicker np;
    TextView tv1;
    private Integer[] mThumbIds = {
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7,
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7,
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7,
            R.drawable.sample_6, R.drawable.sample_7
            ,                R.drawable.sample_6, R.drawable.sample_7
            ,                R.drawable.sample_6, R.drawable.sample_7
            ,                R.drawable.sample_6, R.drawable.sample_7
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent mIntent = getIntent();
        int intValue = mIntent.getIntExtra("itemSelected", 2);
        ImageView imgView = (ImageView)findViewById(R.id.imageView12);
        imgView.setImageResource(mThumbIds[intValue]);

        Button addtocartbutton = (Button) findViewById(R.id.button5);
        addtocartbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // final Intent cartIntent = new Intent().setClass(ProductDetailActivity.this, ProductDetailActivity.class);
                Toast.makeText(ProductDetailActivity.this, "Added to cart", Toast.LENGTH_LONG).show();
                // startActivity(cartIntent);
            }
        });

        np = (NumberPicker) findViewById(R.id.numberPicker);

        tv1 = (TextView) findViewById(R.id.textView2);
        np.setMinValue(0);
        np.setMaxValue(10);
        np.setWrapSelectorWheel(false);

        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                // TODO Auto-generated method stub
                String New = "New Value : ";
                tv1.setText(New.concat(String.valueOf(newVal)));


            }
        });

        TextView tv2 = (TextView) findViewById(R.id.textView12);
        tv2.setText("Rs.2,192");
        tv2.setPaintFlags(tv2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

}
