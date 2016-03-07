package com.example.nidatazeen.shoppingadviser1;

        import android.annotation.SuppressLint;
        import android.content.Context;
        import android.content.Intent;
        import android.graphics.Paint;
        import android.media.Rating;
        import android.os.Bundle;
        import android.support.design.widget.FloatingActionButton;
        import android.support.design.widget.Snackbar;
        import android.support.v7.app.ActionBarActivity;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.util.AttributeSet;
        import android.view.View;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.NumberPicker;
        import android.widget.RadioButton;
        import android.widget.RadioGroup;
        import android.widget.RatingBar;
        import android.widget.RatingBar.OnRatingBarChangeListener;
        import android.widget.RadioGroup.OnCheckedChangeListener;

        import android.widget.TextView;
        import android.widget.Toast;

public class ProductDetailActivity extends AppCompatActivity implements RatingBar.OnRatingBarChangeListener {
    NumberPicker np;
    TextView tv1;
    RatingBar ratingBar;
    TextView ratingText;
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
            R.drawable.sample_6, R.drawable.sample_7,
            R.drawable.sample_6, R.drawable.sample_7,
            R.drawable.sample_6, R.drawable.sample_7,
            R.drawable.sample_6, R.drawable.sample_7,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ratingText = (TextView) findViewById(R.id.rating);

        RatingBar rate = (RatingBar) findViewById(R.id.ratingBar);
        rate.setOnRatingBarChangeListener(this);

        Intent mIntent = getIntent();
        int intValue = mIntent.getIntExtra("itemSelected", 2);
        ImageView imgView = (ImageView) findViewById(R.id.productImageViewLarge);
        imgView.setImageResource(mThumbIds[intValue]);

        Button buyNowbutton = (Button) findViewById(R.id.buyNowbtn);
        buyNowbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // final Intent cartIntent = new Intent().setClass(ProductDetailActivity.this, ProductDetailActivity.class);
                Toast.makeText(ProductDetailActivity.this, "Added to cart", Toast.LENGTH_LONG).show();
                // startActivity(cartIntent);
            }
        });


        TextView tv2 = (TextView) findViewById(R.id.priceTextView);
        tv2.setText("Rs.2,999");
        tv2.setPaintFlags(tv2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

    }

    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromTouch) {
        final int numStars = ratingBar.getNumStars();
        ratingText.setText(rating + "/" + numStars);
    }


    public class SegmentButton extends AppCompatActivity {

        SegmentedRadioGroup segmentText;
        Toast mToast;

        @SuppressLint("ShowToast")
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_product_detail);

            segmentText = (SegmentedRadioGroup) findViewById(R.id.segment_text);
            mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
            segmentText.clearCheck();
            segmentText.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    mToast.setText("id selected is " + segmentText.getCheckedRadioButtonId());
                    mToast.show();
                    if (group == segmentText) {
                        if (checkedId == R.id.descriptionRadiobutton) {
                            mToast.setText("Description");
                            mToast.show();
                        } else if (checkedId == R.id.additionalInfoRadioButton) {
                            mToast.setText("Additional info");
                            mToast.show();
                        } else if (checkedId == R.id.productEnquiryRadioButton) {
                            mToast.setText("Product Enquiry");
                            mToast.show();
                        } else {
                            mToast.setText("Sellers info");
                            mToast.show();
                        }
                    }
                }

            });


        }
    }
}