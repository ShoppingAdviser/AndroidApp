package com.example.nidatazeen.shoppingadviser1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

public class LandingPageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    int position = 0;
    ProgressDialog pDialog;
    Bitmap imageObj;
    ArrayList productsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(LandingPageActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();


                final Intent productDetailIntent = new Intent().setClass(LandingPageActivity.this, ProductDetailActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("product", (ModelProducts) productsArrayList.get(position));
                productDetailIntent.putExtras(b);
                startActivity(productDetailIntent);
            }
        });

        checkIfLoggedIn();

        Button regbutton = (Button) findViewById(R.id.button);


        Button registerbutton = (Button) findViewById(R.id.button);
        registerbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
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
        return true;
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



    //IMAGEADAPTER

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
            parse();
        }

        public int getCount() {
            return productList.size();
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
            if(convertView == null){
                convertView = LayoutInflater.from(mContext).inflate(R.layout.griditem, null, false);

                holder = new ViewHolder();
                holder.icon = (ImageView)convertView.findViewById(R.id.productimageView);
                new ImageDownloader(holder.icon).execute(((ModelProducts) productList.get(position)).getProductImageUrl());
//                holder.icon.setImageResource(mThumbIds[position]);

                holder.productPrice = (TextView)convertView.findViewById(R.id.price);
                holder.productPrice.setPaintFlags(holder.productPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.productPrice.setText(Float.toString(((ModelProducts) productList.get(position)).getProductPrice()));

                holder.productTitle = (TextView)convertView.findViewById(R.id.productTitleView);
                holder.productTitle.setText(((ModelProducts) productList.get(position)).getProductTitle());

                holder.productDiscountprice = (TextView)convertView.findViewById(R.id.discountprice);
                holder.productDiscountprice.setText(Float.toString(((ModelProducts) productList.get(position)).getProductDiscountPrice()));

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
        public void parse() {

            productList = new ArrayList<Integer>();

            String strJson = " {\n" +
                    "           \"products\" : [\n" +
                    "     {\n" +
                    "       \"image url\" : \"http://shoppingadviser.in/wp-content/uploads/2016/02/SM541DVMST65001-600x600.jpg\" ,\n" +
                    "        \"product id\" : 1 ,\n" +
                    "        \"product title\" : \"Ftrendy Yellow Embroidered & Printed Bhagalpuri Silk Semi Stitched Anarkali Churidar Dress Material\" ,\n" +
                    "        \"actual price\" : 2500.00 ,\n" +
                    "        \"discount price\" : 1999.00,\n" +
                    "        \"SKU\" : \"SM541DVMST65005\" ,\n" +
                    "        \"rating\" : 5 ,\n" +
                    "         \"description\" : \"Floor Length Resham Embroidered Semi Stitched Anarkali Suit::2.25 mtr Nazneen Lace Dupatta to enhance the look::Suitable for Weddings & Parties::High Quality Light Weight Bhagalpuri Silk Fabric ::Trendy Resham Embroidery and embellishments with Patch Border\" ,\n" +
                    "         \"sold by\" : \"Ftrendy\" ,\n" +
                    "         \"category\" : \"Women's, Women's Clothing\",\n" +
                    "          \"tag\" : \"Anarkali Suits, Dress Matrerials\",\n" +
                    "         \"ddescription\" : \"Floor Length Resham Embroidered Semi Stitched Anarkali Suit::2.25 mtr Nazneen Lace Dupatta to enhance the look::Suitable for Weddings & Parties::High Quality Light Weight Bhagalpuri Silk Fabric ::Trendy Resham Embroidery and embellishments with Patch Border\" ,\n" +
                    "         \"additionalInfo\" : \"weight     1999g\",\n" +
                    "         \"seller info\" : \"Seller Information \n Store Name:Ftrendy \n Seller:Ftrendy \n  \",\n" +
                    "         \"available size\" : \"S,M,L,XL,XXL,XXXL\"\n" +
                    "       }," +
                    "     {\n" +
                    "       \"image url\" : \"http://shoppingadviser.in/wp-content/uploads/2016/02/SM541DVMST65004-600x600.jpg\" ,\n" +
                    "        \"product id\" : 2 ,\n" +
                    "        \"product title\" : \"Ftrendy Light Pink Embroidered & Printed Bhagalpuri Silk Semi Stitched Anarkali Churidar Dress Material\" ,\n" +
                    "        \"actual price\" : 2500.00 ,\n" +
                    "        \"discount price\" : 1999.00,\n" +
                    "        \"SKU\" : \"SM541DVMST65007\" ,\n" +
                    "        \"rating\" : 5 ,\n" +
                    "         \"description\" : \"Floor Length Resham Embroidered Semi Stitched Anarkali Suit::2.25 mtr Nazneen Lace Dupatta to enhance the look::Suitable for                                                   Weddings & Parties::High Quality Light Weight Bhagalpuri Silk Fabric ::Trendy Resham Embroidery and embellishments with Patch Border\" ,\n" +
                    "         \"sold by\" : \"Ftrendy\" ,\n" +
                    "         \"category\" : \"Women's, Women's Clothing\",\n" +
                    "          \"tag\" : \"Anarkali Suits, Dress Matrerials\",\n" +
                    "         \"available size\" : \"S,M,L,XL,XXL,XXXL\"\n" +
                    "       }," +
                    "     {\n" +
                    "       \"image url\" : \"http://shoppingadviser.in/wp-content/uploads/2016/02/SM541DVMST65006-600x600.jpg\" ,\n" +
                    "        \"product id\" : 3 ,\n" +
                    "        \"product title\" : \"Ftrendy Dark Sea Green Embroidered & Printed Bhagalpuri Silk Semi Stitched Anarkali Churidar Dress Material\" ,\n" +
                    "        \"actual price\" : 2500.00 ,\n" +
                    "        \"discount price\" : 1999.00,\n" +
                    "        \"SKU\" : \"SM541DVMST65008\" ,\n" +
                    "        \"rating\" : 5 ,\n" +
                    "         \"description\" : \"Floor Length Resham Embroidered Semi Stitched Anarkali Suit::2.25 mtr Nazneen Lace Dupatta to enhance the look::Suitable for                                                   Weddings & Parties::High Quality Light Weight Bhagalpuri Silk Fabric ::Trendy Resham Embroidery and embellishments with Patch Border\" ,\n" +
                    "         \"sold by\" : \"Ftrendy\" ,\n" +
                    "         \"category\" : \"Women's, Women's Clothing\",\n" +
                    "          \"tag\" : \"Anarkali Suits, Dress Matrerials\",\n" +
                    "         \"available size\" : \"S,M,L,XL,XXL,XXXL\"\n" +
                    "       }," +
                    "     {\n" +
                    "       \"image url\" : \"http://shoppingadviser.in/wp-content/uploads/2016/02/SM541DVMST65008-600x600.jpg\" ,\n" +
                    "        \"product id\" : 4 ,\n" +
                    "        \"product title\" : \"Ftrendy Red Off White Embroidered & Printed Bhagalpuri Silk Semi Stitched Anarkali Churidar Dress Material\" ,\n" +
                    "        \"actual price\" : 2500.00 ,\n" +
                    "        \"discount price\" : 1999.00,\n" +
                    "        \"SKU\" : \"SM541DVMST65004\" ,\n" +
                    "        \"rating\" : 5 ,\n" +
                    "         \"description\" : \"Floor Length Resham Embroidered Semi Stitched Anarkali Suit::2.25 mtr Nazneen Lace Dupatta to enhance the look::Suitable for                                                   Weddings & Parties::High Quality Light Weight Bhagalpuri Silk Fabric ::Trendy Resham Embroidery and embellishments with Patch Border\" ,\n" +
                    "         \"sold by\" : \"Ftrendy\" ,\n" +
                    "         \"category\" : \"Women's, Women's Clothing\",\n" +
                    "          \"tag\" : \"Anarkali Suits, Dress Matrerials\",\n" +
                    "         \"available size\" : \"S,M,L,XL,XXL,XXXL\"\n" +
                    "       }," +
                    "     {\n" +
                    "       \"image url\" : \"http://shoppingadviser.in/wp-content/uploads/2016/02/SM541DVMST65005-600x600.jpg\" ,\n" +
                    "        \"product id\" :  5,\n" +
                    "        \"product title\" : \"Ftrendy Orange Black Embroidered & Printed Bhagalpuri Silk Semi Stitched Anarkali Churidar Dress Material\" ,\n" +
                    "        \"actual price\" : 2500.00 ,\n" +
                    "        \"discount price\" : 1999.00,\n" +
                    "        \"SKU\" : \"SM541DVMST65001\" ,\n" +
                    "        \"rating\" : 5 ,\n" +
                    "         \"description\" : \"Floor Length Resham Embroidered Semi Stitched Anarkali Suit::2.25 mtr Nazneen Lace Dupatta to enhance the look::Suitable for                                                   Weddings & Parties::High Quality Light Weight Bhagalpuri Silk Fabric ::Trendy Resham Embroidery and embellishments with Patch Border\" ,\n" +
                    "         \"sold by\" : \"Ftrendy\" ,\n" +
                    "         \"category\" : \"Women's, Women's Clothing\",\n" +
                    "          \"tag\" : \"Anarkali Suits, Dress Matrerials\",\n" +
                    "         \"available size\" : \"S,M,L,XL,XXL,XXXL\"\n" +
                    "       }" +
                    "]}";

            String data = "";
            try {
                JSONObject jsonRootObject = new JSONObject(strJson);

                //Get the instance of JSONArray that contains JSONObjects
                JSONArray jsonArray = jsonRootObject.optJSONArray("products");

                //Iterate the jsonArray and print the info of JSONObjects
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String url = jsonObject.optString("image url").toString();
//                    new LoadImage().execute(url);

                    int productId = Integer.parseInt(jsonObject.optString("product id").toString());
                    String productTitle = jsonObject.optString("product title").toString();
                    String productDescription = jsonObject.optString("description").toString();
                    float actualPrice = Float.parseFloat(jsonObject.optString("actual price").toString());
                    float discountPrice = Float.parseFloat(jsonObject.optString("discount price").toString());
                    String soldBy = jsonObject.optString("sold by").toString();
                    String category = jsonObject.optString("category").toString();
                    String tag = jsonObject.optString("tag").toString();
                    String size = jsonObject.optString("available size").toString();
                    String SKU = jsonObject.optString("SKU").toString();
                    int rating = Integer.parseInt(jsonObject.optString("rating").toString());
                    String productdDescription = jsonObject.optString("ddescription").toString();
                    String productAdditionalInfo=jsonObject.optString("additionalInfo").toString();
                    String productSellerInfo=jsonObject.optString("seller info").toString();

                    ModelProducts product = new ModelProducts(productTitle, productDescription, actualPrice, discountPrice,rating, soldBy, category, tag, productId, SKU,size, url,productdDescription,productAdditionalInfo,productSellerInfo);
                    productList.add(product);

                }
                productsArrayList = productList;
//                Toast.makeText(LandingPageActivity.this, "" + productList.size(),
//                        Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        private ArrayList productList;
        // references to our images
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

    }
}
