package com.example.nidatazeen.shoppingadviser1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.SearchView;
import android.text.Html;
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
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
//import android.widget.SearchView;
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
import java.util.List;
import java.util.Map;
import java.util.logging.Filter;

public class LandingPageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    int progressStatus=0;
    int position = 0;
    ProgressDialog pDialog;
    Bitmap imageObj;
    List<ModelProducts> productsArrayList = new ArrayList<ModelProducts>();
    ProgressDialog pd;

    ImageAdapter myAdapter;
    WebRequest web;
    String jsonstr = "";
DatabaseHandler db;
    Handler handler=new Handler();

    private Handler activityHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // Do something with msg.obj
            Map<String, Object> map = (Map<String, Object>)msg.obj;
            jsonstr = map.get("json").toString();
            pd.dismiss();
            String pagecount = map.get("pagecount").toString();


            parse();
            setUpGrid();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);


        db = new DatabaseHandler(this);
        pd = new ProgressDialog(LandingPageActivity.this);
       // handleIntent(getIntent());

        web = new WebRequest(activityHandler);
        web.execute("https://shoppingadviser.in/wc-api/v3/products?fields=title,sku,price,regular_price,description,short_description,rating_count,categories,images,featured_src&consumer_key=ck_4484117b7a8ef2f451a99a7e4920a1412fec2be6&consumer_secret=cs_18d0652d18b7309a407fd5c64255aac3fd9dcae8");

        // Initialize a new instance of progress dialog
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        pd.setMessage("Loading products.........");

        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFD4D9D0")));

        pd.setIndeterminate(true);

        pd.show();


        progressStatus = 0;

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(progressStatus < 100){

                    progressStatus +=1;

                    try{
                        Thread.sleep(20);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            pd.setProgress(progressStatus);

                            if(progressStatus == 100){

                                pd.dismiss();
                            }
                        }
                    });
                }
            }
        }).start();


        checkIfLoggedIn();


            Button registerbutton = (Button) findViewById(R.id.button);
            registerbutton.setOnClickListener(new View.OnClickListener()

            {
                public void onClick(View v) {


                    final Intent registerIntent = new Intent().setClass(LandingPageActivity.this, RegisterActivity.class);
                    startActivity(registerIntent);
                }
            });

       /* Singleton.getInstance().setString("Singleton");
        Intent intent = new Intent(getApplicationContext(),ProductDetailActivity.class);
        this.startActivity(intent);*/
Button searchbutton=  (Button) findViewById(R.id.searchbtn);
        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchclicked();
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
            b.putSerializable("product", (ModelProducts)productsArrayList.get(position));
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
        //getMenuInflater().inflate(R.menu.landing_page, menu);

        return super.onCreateOptionsMenu(menu);
    }

public void searchclicked(){
    EditText txt = (EditText) findViewById(R.id.search_edit_text);
    List<ModelProducts> c= db.getWordMatches(txt.getText().toString(),null);
    Integer i = 0;
    if(c!=null) {
        i = c.size();
        productsArrayList = c;
        Toast.makeText(LandingPageActivity.this, "Search clicked " + c.size() + txt.getText().toString(), Toast.LENGTH_LONG).show();
        setUpGrid();
    }
    else
    Toast.makeText(LandingPageActivity.this, "No item found" ,Toast.LENGTH_LONG).show();
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
long value = 0;

        String strJson = jsonstr.replace("&amp;", " &");

        JSONArray arr = null;
        JSONArray prodImages=null;
        ArrayList categoryArraylist = new ArrayList<String>();
        ArrayList allImages = new ArrayList<String>();

        try
        {
            JSONObject jsonRootObject = new JSONObject(strJson);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.getJSONArray("products");


            //Iterate the jsonArray and print the info of JSONObjects
//            for (int i = 0; i < jsonArray.length(); i++) {
            for (int i = 0; i < 30; i++) {

                StringBuilder category = new StringBuilder();
                StringBuilder images = new StringBuilder();

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String url = "";
                url = jsonObject.optString("featured_src").toString();

                //Response with keys that we have got

                int productId = (i + 1);//Integer.parseInt(jsonObject.optString("id").toString());
                String productTitle = jsonObject.optString("title").toString();
                String productDescription = jsonObject.optString("description").toString();
                String actualPrice = jsonObject.optString("regular_price").toString();
                String discountPrice = jsonObject.optString("price").toString();
                String soldBy = "Ftrendy";//jsonObject.optString("sold by").toString();// to be removed
                String tag = "item";//jsonObject.optString("tags").toString();// ARRAY SETTING
                String size = "xl, xxl";//jsonObject.optString("available size").toString();// to be removed
                String SKU = jsonObject.optString("sku").toString();
                int rating = Integer.parseInt(jsonObject.optString("rating_count").toString());
                String productdDescription = jsonObject.optString("short_description").toString();
                String productAdditionalInfo="additional info";//jsonObject.optString("additionalInfo").toString();// to be removed
                String productSellerInfo="seller info";//jsonObject.optString("seller info").toString(); to be removed
                arr  =  jsonObject.getJSONArray("categories");
                prodImages = jsonObject.getJSONArray("images");


                for (int k = 0; k < prodImages.length(); k++) {

                    try {
                        JSONObject Src = prodImages.getJSONObject(k);
                        String src = Src.getString("src");
//                        images.append(src);
//                        images.append(", ");
                        allImages.add(src);
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }



                for(int l=0;l<arr.length();l++)
                {
                    String categories = null;


                    try {

                        categories = arr.getString(l).toString();
                        category.append(arr.getString(l));
                        category.append(", ");
                        categoryArraylist.add(categories);


                    } catch (JSONException e) {

                    }

                }

                for(int m=0;m<allImages.size();m++)
                {
                        images.append(allImages.get(m).toString());
                        images.append(", ");

                }
                String s= category.toString();
                category.setLength(0);

                String j = images.toString();
                images.setLength(0);
allImages.clear();


                ModelProducts product = new ModelProducts(productTitle, productDescription, actualPrice, discountPrice,productId, rating, soldBy, s,tag, SKU,size, url,productdDescription,productAdditionalInfo,productSellerInfo,j);
                value = db.addProduct(product);

            }

            productsArrayList = db.getAllProducts();




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
            //return db.getProductsCount();

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

        private void clear() {

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
