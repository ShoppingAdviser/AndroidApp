package com.example.nidatazeen.shoppingadviser1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.AvoidXfermode;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.Rating;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.RadioGroup.OnCheckedChangeListener;

import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductDetailActivity extends AppCompatActivity implements RatingBar.OnRatingBarChangeListener {
    NumberPicker np;
    TextView tv1;
    RatingBar ratingBar;
    TextView ratingText;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    ScrollView containerScrollView;
    ModelProducts product;
    ArrayList productsArrayList;

    HashMap<String, List<String>> listDataChild;
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

        Bundle b = this.getIntent().getExtras();
        if(b!=null)
            product = (ModelProducts)b.getSerializable("product");

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        containerScrollView = (ScrollView) findViewById(R.id.container_scroll_view);

        // preparing list data
        prepareListData(product);

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
expListView.expandGroup(0);
        expListView.expandGroup(1);
        expListView.expandGroup(2);
        expListView.expandGroup(3);
        expListView.setFocusable(false);


        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {

                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                parent.expandGroup(groupPosition);
                return true;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });
        ratingText = (TextView) findViewById(R.id.rating);

        RatingBar rate = (RatingBar) findViewById(R.id.ratingBar);
        rate.setOnRatingBarChangeListener(this);

//        Intent mIntent = getIntent();
//        int intValue = mIntent.getIntExtra("itemSelected", 2);
        ImageView imgView = (ImageView) findViewById(R.id.productImageViewLarge);
//        imgView.setImageResource(mThumbIds[intValue]);
        new ImageDownloader(imgView).execute(product.getProductImageUrl());

        Button buyNowbutton = (Button) findViewById(R.id.buyNowbtn);
        buyNowbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

//                    Toast.makeText(ProductDetailActivity.this, "Added to cart", Toast.LENGTH_LONG).show();
//                    final Intent cartIntent = new Intent().setClass(ProductDetailActivity.this, CartActivity.class);
//                    startActivity(cartIntent);

                final Intent cartIntent = new Intent().setClass(ProductDetailActivity.this, CartActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("singleproduct", product);
                cartIntent .putExtras( b);
                startActivity(cartIntent);
                }
        });


        TextView priceTxtView = (TextView) findViewById(R.id.priceTextView);
        priceTxtView.setText(Float.toString(product.getProductPrice()));
        priceTxtView.setPaintFlags(priceTxtView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        TextView prodTxtView = (TextView) findViewById(R.id.titletextview);
        prodTxtView.setText(product.getProductTitle());

        TextView discountTxtView = (TextView) findViewById(R.id.discountPriceTextView);
        discountTxtView.setText(Float.toString(product.getProductDiscountPrice()));

    }
    private void prepareListData(ModelProducts prodct) {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Description");
        listDataHeader.add("Additional Information");
        listDataHeader.add("Product Enquiry");
        listDataHeader.add("Seller Info ");

        // Adding child data
        List<String> descString = new ArrayList<String>();
        descString.add(prodct.getProductDescription());

        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("The Conjuring");
        nowShowing.add("Despicable Me 2");
        nowShowing.add("Turbo");
        nowShowing.add("Grown Ups 2");
        nowShowing.add("Red 2");
        nowShowing.add("The Wolverine");

        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("2 Guns");
        comingSoon.add("The Smurfs 2");
        comingSoon.add("The Spectacular Now");
        comingSoon.add("The Canyons");
        comingSoon.add("Europa Report");

        List<String> comingSoo2n = new ArrayList<String>();
        comingSoo2n.add("2 Guns");
        comingSoo2n.add("The Smurfs 2");
        comingSoo2n.add("The Spectacular Now");
        comingSoo2n.add("The Canyons");
        comingSoo2n.add("Europa Report");

        listDataChild.put(listDataHeader.get(0), descString); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
        listDataChild.put(listDataHeader.get(3), comingSoo2n);


    }
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromTouch) {
        final int numStars = ratingBar.getNumStars();
        ratingText.setText(rating + "/" + numStars);
    }


    private  class ExpandableListAdapter extends BaseExpandableListAdapter {

        private Context _context;
        private List<String> _listDataHeader; // header titles
        // child data in format of header title, child title
        private HashMap<String, List<String>> _listDataChild;

        public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                     HashMap<String, List<String>> listChildData) {
            this._context = context;
            this._listDataHeader = listDataHeader;
            this._listDataChild = listChildData;
        }

        @Override
        public Object getChild(int groupPosition, int childPosititon) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .get(childPosititon);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, final int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {

            final String childText = (String) getChild(groupPosition, childPosition);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_item, null);
            }

            TextView txtListChild = (TextView) convertView
                    .findViewById(R.id.lblListItem);

            txtListChild.setText(childText);
            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this._listDataHeader.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return this._listDataHeader.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            String headerTitle = (String) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_group, null);
            }

            TextView lblListHeader = (TextView) convertView
                    .findViewById(R.id.lblListHeader);
            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle);

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}