package com.example.nidatazeen.shoppingadviser1;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filterable;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Filter;

public class LandingPageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    int position = 0;
    ProgressDialog pDialog;
    Bitmap imageObj;
    ArrayList productsArrayList;
    ImageAdapter myAdapter;
    WebRequest web;
    String jsonstr = "";

    private Handler activityHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // Do something with msg.obj
            jsonstr = msg.obj.toString();

            parse();
            setUpGrid();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
            web = new WebRequest(activityHandler);
            web.execute("https://shoppingadviser.in/wc-api/v3/products?consumer_key=ck_4484117b7a8ef2f451a99a7e4920a1412fec2be6&consumer_secret=cs_18d0652d18b7309a407fd5c64255aac3fd9dcae8");


            checkIfLoggedIn();


            Button registerbutton = (Button) findViewById(R.id.button);
            registerbutton.setOnClickListener(new View.OnClickListener()

            {
                public void onClick (View v){


                final Intent registerIntent = new Intent().setClass(LandingPageActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(true);
        drawer.setDrawerListener(toggle);
            toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




    }

private void setUpGrid() {
    myAdapter = new ImageAdapter(this);
    GridView gridview = (GridView) findViewById(R.id.gridview);
    gridview.setAdapter(myAdapter);
    gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View v,
                                int position, long id) {


            final Intent productDetailIntent = new Intent().setClass(LandingPageActivity.this, ProductDetailActivity.class);
            Bundle b = new Bundle();
            b.putSerializable("product", (ModelProducts) productsArrayList.get(position));
            productDetailIntent.putExtras(b);
            startActivity(productDetailIntent);
        }
    });

}
    private void checkIfLoggedIn() {

        SharedPreferences shoppingAdviserPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = shoppingAdviserPreferences.edit();
        boolean isLoggedIn = shoppingAdviserPreferences.getBoolean("isLoggedIn", false);

        Button loginbutton = (Button) findViewById(R.id.button2);
            Button regbutton = (Button) findViewById(R.id.button);
            if (isLoggedIn) {
                loginbutton.setText("Logout");
                loginbutton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        logoutUser();

                    }
                });
                loginbutton.setVisibility(View.VISIBLE);
                regbutton.setVisibility(View.GONE);
            } else {
                logoutUser();
            }

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.landing_page, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_first_fragment) {
            // Handle thintent = new Intent().setClass(this, Tab1Activity.class);e camera action
            Intent thintent = new Intent().setClass(this, MyAccountsActivity.class);
            startActivity(thintent);
        } else if (id == R.id.nav_second_fragment) {
            Intent aboutUsintent = new Intent().setClass(this, AboutUsActivity.class);
            startActivity(aboutUsintent);

        } else if (id == R.id.nav_third_fragment) {
            Intent contactUsintent = new Intent().setClass(this, ContactUsActivity.class);
            startActivity(contactUsintent);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getDelegate().onPostResume();

        checkIfLoggedIn();
    }
    private void logoutUser() {
        Button loginbutton = (Button) findViewById(R.id.button2);
        Button regbutton = (Button) findViewById(R.id.button);
        SharedPreferences shoppingAdviserPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = shoppingAdviserPreferences.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.clear();
        editor.commit();

        loginbutton.setVisibility(View.VISIBLE);
        loginbutton.setText("Login");
        loginbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Intent loginIntent = new Intent().setClass(LandingPageActivity.this, LoginActivity.class);
                startActivity(loginIntent);

            }
        });
        regbutton.setVisibility(View.VISIBLE);

    }

    public void finish()
    {
        super.finish();
    }
    public void parse() {

        productsArrayList = new ArrayList<Integer>();
        String strJson = jsonstr;

        try
        {
            JSONObject jsonRootObject = new JSONObject(strJson);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.getJSONArray("products");
            Toast.makeText(LandingPageActivity.this, "jsonArray  " + jsonArray.length(), Toast.LENGTH_LONG).show();


            //Iterate the jsonArray and print the info of JSONObjects
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String url = "";
                url = jsonObject.optString("featured_src").toString();

                int productId = Integer.parseInt(jsonObject.optString("id").toString());
                String productTitle = jsonObject.optString("title").toString();
                String productDescription = jsonObject.optString("description").toString();
                String actualPrice = jsonObject.optString("price").toString();
                String discountPrice = "10000";//jsonObject.optString("sale_price").toString();
                String soldBy = "Ftrendy";//jsonObject.optString("sold by").toString();// to be removed
                String category = "category";//jsonObject.optString("categories").toString();// array setting
                String tag = "item";//jsonObject.optString("tags").toString();// ARRAY SETTING
                String size = "xl, xxl";//jsonObject.optString("available size").toString();// to be removed
                String SKU = jsonObject.optString("sku").toString();
                int rating = Integer.parseInt(jsonObject.optString("rating_count").toString());
                String productdDescription = jsonObject.optString("short_description").toString();
                String productAdditionalInfo="additional info";//jsonObject.optString("additionalInfo").toString();// to be removed
                String productSellerInfo="seller info";//jsonObject.optString("seller info").toString(); to be removed

                ModelProducts product = new ModelProducts(productTitle, productDescription, actualPrice, discountPrice,rating, soldBy, category, tag, productId, SKU,size, url,productdDescription,productAdditionalInfo,productSellerInfo);
                productsArrayList.add(product);
//                Toast.makeText(LandingPageActivity.this, "product title is  " + product.getProductTitle(), Toast.LENGTH_LONG).show();


            }
//            Toast.makeText(LandingPageActivity.this, "productsArrayList size is  " + productsArrayList.size(), Toast.LENGTH_LONG).show();


        }
        catch (JSONException e)
        {
            Toast.makeText(LandingPageActivity.this, "error", Toast.LENGTH_LONG).show();

            e.printStackTrace();
        }
    }
    //IMAGEADAPTER

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return productsArrayList.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public ImageAdapter getFilter() {
            // TODO Auto-generated method stub
            return null;
        }
        // create a new ImageView for each item referenced by the Adapter
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null){

                convertView = LayoutInflater.from(mContext).inflate(R.layout.griditem, null, false);

                holder = new ViewHolder();
                holder.icon = (ImageView)convertView.findViewById(R.id.productimageView);
                new ImageDownloader(holder.icon).execute(((ModelProducts) productsArrayList.get(position)).getProductImageUrl());

                holder.productPrice = (TextView)convertView.findViewById(R.id.price);
                holder.productPrice.setPaintFlags(holder.productPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.productPrice.setText((((ModelProducts) productsArrayList.get(position)).getProductPrice()));

                holder.productTitle = (TextView)convertView.findViewById(R.id.productTitleView);
                holder.productTitle.setText(((ModelProducts) productsArrayList.get(position)).getProductTitle());

                holder.productDiscountprice = (TextView)convertView.findViewById(R.id.discountprice);
                holder.productDiscountprice.setText((((ModelProducts) productsArrayList.get(position)).getProductDiscountPrice()));

                holder.addToCartBton = (Button)convertView.findViewById(R.id.addToCartButton);
                holder.addToCartBton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Toast.makeText(LandingPageActivity.this, "Added to cart", Toast.LENGTH_LONG).show();

                    }
                });

                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            return convertView;
        }

        private class ViewHolder {
            TextView productTitle;
            TextView productPrice;
            TextView productDiscountprice;
            Button addToCartBton;
            ImageView icon;
        }

    }

}
