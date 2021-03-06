package com.example.nidatazeen.shoppingadviser1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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
    DatabaseHandler db;

    HashMap<String, List<String>> listDataChild;
int urlcount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


     db= new DatabaseHandler(this);

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
        expListView.setFocusable(false);

        setListViewHeightBasedOnItems(expListView);
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
        rate.setRating(product.getRating());


        final EditText qtytxt = (EditText)findViewById(R.id.qtyeditText);
        qtytxt.setText(product.getProductqty());
        qtytxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                product.setProductqty(s.toString());
                db.updateProduct(product);

            }

            @Override
            public void afterTextChanged(Editable s) {


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
        });
        ImageView imgView = (ImageView) findViewById(R.id.productImageViewLarge);
        Picasso.with(getApplicationContext()).load(product.getProductImageUrl()).into(imgView);

        GridView gridview = (GridView) findViewById(R.id.extraimagesgridView);

        gridview.setAdapter(new ImageAdapter(this));


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView parent, View v,
                                    int position, long id) {
                ImageView imageView = (ImageView) findViewById(R.id.productImageViewLarge);
                Picasso.with(getApplicationContext()).load(imgurllist[position]).into(imageView);

            }
        });


        Button buyNowbutton = (Button) findViewById(R.id.buyNowbtn);
        if (product.getSelected() == 1) {
            buyNowbutton.setText("Remove From Cart?");
qtytxt.setText(product.getProductqty());
        }
        else {
            buyNowbutton.setText("Add to Cart");
qtytxt.setText("1");
        }

        buyNowbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (product.getSelected() == 1) {
                    setButtonText("Add to Cart");
                    product.setSelected(0);
                    product.setProductqty("1");
                    db.updateProduct(product);
                } else {
                    setButtonText("Remove From Cart?");
                    product.setSelected(1);
                    product.setProductqty(qtytxt.getText().toString());
                    db.updateProduct(product);

                    final Intent cartIntent = new Intent().setClass(ProductDetailActivity.this, ShoppingCartActivity.class);

                    Bundle b = new Bundle();
                    b.putSerializable("quantity", qtytxt.getText().toString());
                    cartIntent.putExtras(b);
                    startActivity(cartIntent);

                }

            }
        });

        TextView categoryTxtView = (TextView) findViewById(R.id.categoryTextView);
        categoryTxtView.setText("Categories: " + product.getCategory());

        TextView sku = (TextView) findViewById(R.id.sku);
        sku.setText("SKU: "+product.getProductSKU());

        TextView priceTxtView = (TextView) findViewById(R.id.priceTextView);
        priceTxtView.setText("Rs."+product.getProductPrice());
        priceTxtView.setPaintFlags(priceTxtView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        TextView prodTxtView = (TextView) findViewById(R.id.titletextview);
        prodTxtView.setText(product.getProductTitle());

        TextView discountTxtView = (TextView) findViewById(R.id.discountPriceTextView);
        discountTxtView.setText("Rs."+product.getProductDiscountPrice());



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.enquirybtn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent abtIntent = new Intent().setClass(ProductDetailActivity.this, ProductEnquiryActivity.class);
startActivity(abtIntent);
            }
        });

    }

    public void setButtonText(String txt) {
        Button buyNowbutton = (Button) findViewById(R.id.buyNowbtn);
buyNowbutton.setText(txt);
    }
    int imageurlcount() {
        String data = product.getProductGridImages();
        if (data != null) {

            String[] items = data.split(",");
            imgurllist = items;
            return items.length - 1;
        }
        return 1;
    }

    private void prepareListData(ModelProducts prodct) {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Description");
        listDataHeader.add("Additional Information");

        // Adding child data
        List<String> descString = new ArrayList<String>();
       // Toast.makeText(ProductDetailActivity.this, Html.fromHtml(prodct.getProductDescription()).toString(), Toast.LENGTH_LONG).show();
        descString.add( Html.fromHtml(prodct.getProductDescription()).toString());

        List<String> addInfo = new ArrayList<String>();
        addInfo.add(Html.fromHtml(prodct.getProductDetailedDescription()).toString());



        listDataChild.put(listDataHeader.get(0), descString); // Header, Child data
        listDataChild.put(listDataHeader.get(1), addInfo);


    }
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromTouch) {
        final int numStars = ratingBar.getNumStars();
        ratingText.setText((Math.round(rating) + "/" + numStars));
        product.setRating(Math.round(rating));
        db.updateProduct(product);
    }

    public boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems;itemPos++){
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight + 300;
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }

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
            GridView gridview = (GridView) findViewById(R.id.extraimagesgridView);

            ViewHolder holder;
            if (convertView == null) {

                convertView = LayoutInflater.from(mContext).inflate(R.layout.collectiongriditem, null, false);

                holder = new ViewHolder();
                holder.icon = (ImageView) convertView.findViewById(R.id.newimage);

                if(imgurllist!=null) {
                    Picasso.with(mContext).load(imgurllist[position]).into(holder.icon);
                }


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