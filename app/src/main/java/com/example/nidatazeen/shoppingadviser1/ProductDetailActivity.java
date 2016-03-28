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
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.GridView;
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
    String[] imgurllist;

    HashMap<String, List<String>> listDataChild;
int urlcount;

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
urlcount = imageurlcount();
        // preparing list data
        prepareListData(product);

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        expListView.expandGroup(0);
        expListView.expandGroup(1);
        expListView.expandGroup(2);
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


        ImageView imgView = (ImageView) findViewById(R.id.productImageViewLarge);
        new ImageDownloader(imgView).execute(product.getProductImageUrl());

        GridView gridview = (GridView) findViewById(R.id.extraimagesgridView);

        gridview.setAdapter(new ImageAdapter(this));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView parent, View v,
                                    int position, long id) {
                ImageView imageView = (ImageView) findViewById(R.id.productImageViewLarge);
                new ImageDownloader(imageView).execute(imgurllist[position]);
                Toast.makeText(ProductDetailActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();
//
//                ImageView img = (ImageView)
//                        img.setImageResource(prodImage);
            }
        });

        Button buyNowbutton = (Button) findViewById(R.id.buyNowbtn);
        buyNowbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                final Intent cartIntent = new Intent().setClass(ProductDetailActivity.this, CartActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("singleproduct", product);
                cartIntent.putExtras(b);
                startActivity(cartIntent);
            }
        });

        TextView categoryTxtView = (TextView) findViewById(R.id.categoryTextView);
        categoryTxtView.setText(product.getCategory());

        TextView tagTxtView = (TextView) findViewById(R.id.tagTextView);
        tagTxtView.setText(product.getTag());

        TextView sku = (TextView) findViewById(R.id.sku);
        sku.setText(product.getProductSKU());

        TextView priceTxtView = (TextView) findViewById(R.id.priceTextView);
        priceTxtView.setText(product.getProductPrice());
        priceTxtView.setPaintFlags(priceTxtView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        TextView prodTxtView = (TextView) findViewById(R.id.titletextview);
        prodTxtView.setText(product.getProductTitle());

        TextView discountTxtView = (TextView) findViewById(R.id.discountPriceTextView);
        discountTxtView.setText(product.getProductDiscountPrice());



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.enquirybtn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent abtIntent = new Intent().setClass(ProductDetailActivity.this, ProductEnquiryActivity.class);
startActivity(abtIntent);
            }
        });

    }

    int imageurlcount() {
        String data = product.getProductGridImages();
        String[] items = data.split(",");
        imgurllist = items;
        return items.length - 1;
    }

    private void prepareListData(ModelProducts prodct) {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Description");
        listDataHeader.add("Additional Information");
        listDataHeader.add("Seller Info ");

        // Adding child data
        List<String> descString = new ArrayList<String>();
       // Toast.makeText(ProductDetailActivity.this, Html.fromHtml(prodct.getProductDescription()).toString(), Toast.LENGTH_LONG).show();
        descString.add( Html.fromHtml(prodct.getProductDescription()).toString());

        List<String> addInfo = new ArrayList<String>();
        addInfo.add( Html.fromHtml(prodct.getProductDetailedDescription()).toString());

        List<String> sellers = new ArrayList<String>();
        sellers.add(product.getSellerInfo());


        listDataChild.put(listDataHeader.get(0), descString); // Header, Child data
        listDataChild.put(listDataHeader.get(1), addInfo);
        listDataChild.put(listDataHeader.get(2), sellers);

    }
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromTouch) {
        final int numStars = ratingBar.getNumStars();
        ratingText.setText(rating + "/" + numStars);
    }

    private class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return urlcount;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.collectiongriditem, null, false);

                holder = new ViewHolder();
                holder.icon = (ImageView) convertView.findViewById(R.id.newimage);
                //      holder.icon.setImageResource(R.drawable.sample_2);
//                holder.icon.setImageResource(mThumbIds[position]);


                    new ImageDownloader(holder.icon).execute(imgurllist[position]);

               //allImages= product.getProductGridImages().toString();

               /* ModelProducts product = new ModelProducts(productTitle, productDescription, actualPrice, discountPrice,productId, rating, soldBy, category, tag, SKU,size, url,productdDescription,productAdditionalInfo,productSellerInfo);
                value = db.addProduct(product);
                productsArrayList.add(product);

               holder.productTitle = (TextView)convertView.findViewById(R.id.productTitleView);
                holder.productTitle.setText(((ModelProducts) productsArrayList.get(position)).getProductTitle());*/





                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            return convertView;
        }

        private class ViewHolder {
            ImageView icon;
        }


        private ArrayList productList;

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