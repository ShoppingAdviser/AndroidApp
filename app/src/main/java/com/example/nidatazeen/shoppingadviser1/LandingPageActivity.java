package com.example.nidatazeen.shoppingadviser1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



public class LandingPageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,GeneralDialogFragment.OnDialogFragmentClickListener {
    private ArrayList<GridItem> mGridData;
    private GridViewAdapter mGridAdapter;
    private GridView mGridView;
    int progressStatus=0;
    int position = 0;
    ProgressDialog pDialog;
    Bitmap imageObj;
    List<ModelProducts> productsArrayList = new ArrayList<ModelProducts>();
    ProgressDialog pd;
    int identifier;
    WebRequest web;
    String jsonstr = "";
    DatabaseHandler db;
    Handler handler=new Handler();
    int pagecount;
    int currentpage=1;
    boolean issearchclicked = false;
    boolean readFromFile = false;

    private Handler activityHandler = new Handler() {
        //     @Override
        public void handleMessage(Message msg) {
            // Do something with msg.obj
            Map<String, Object> map = (Map<String, Object>)msg.obj;
            jsonstr = map.get("json").toString();
            pagecount = Integer.parseInt(map.get("pagecount").toString());


            parse();
            pd.dismiss();

            setUpGrid();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        SharedPreferences shoppingAdviserPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = shoppingAdviserPreferences.edit();
        boolean b = shoppingAdviserPreferences.getBoolean("isFirstTimeLaunch",true);
        if (b) {
            Toast.makeText(LandingPageActivity.this, "TIP: Long press on any product to add them to cart!!!", Toast.LENGTH_LONG).show();
            editor.putBoolean("isFirstTimeLaunch", false);
            editor.commit();
        }

        Button showmorebutton=  (Button) findViewById(R.id.showmore);

        mGridData = new ArrayList<>();
        db = new DatabaseHandler(this);
        productsArrayList = db.getAllProducts();

        pd = new ProgressDialog(LandingPageActivity.this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        pd.setMessage("Loading products.........");

        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFD4D9D0")));

        pd.setIndeterminate(true);
        if(productsArrayList.size() == 0) {// && shouldFireRequest()){
            LoadRequest(currentpage);
            pd.show();
        }
        else{

            setUpGrid();

        }

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        progressStatus = 0;

        checkIfLoggedIn();


        Button registerbutton = (Button) findViewById(R.id.button);
        registerbutton.setOnClickListener(new View.OnClickListener()

        {
            public void onClick(View v) {


                final Intent registerIntent = new Intent().setClass(LandingPageActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });

        Button viewcartbtn = (Button)findViewById(R.id.ViewcartButton);
        viewcartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent cartIntent = new Intent().setClass(LandingPageActivity.this, ShoppingCartActivity.class);
                startActivity(cartIntent);


            }
        });
        Button searchbutton=  (Button) findViewById(R.id.searchbtn);
        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                issearchclicked = true;
                searchclicked();
            }
        });

        EditText txt = (EditText)findViewById(R.id.search_edit_text);
        txt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }


            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() == 0){
                    fillData();
                    issearchclicked = false;
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
        });

        showmorebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentpage<pagecount)
                {
                    currentpage++;

                }
                pd.show();

                LoadRequest(currentpage);
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

    public boolean shouldFireRequest() {
        long days_1 = 1000 * 60 * 60 * 24 * 1;
        SharedPreferences shoppingAdviserPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = shoppingAdviserPreferences.edit();

        long oldTime = shoppingAdviserPreferences.getLong("smstimestamp",0);

        if(oldTime == 0 || System.currentTimeMillis() - oldTime > days_1) {
            // here, more than 1 day
            editor.putLong("smstimestamp", System.currentTimeMillis());

            return true;
        }
        else {
            return false;
        }
    }

    public void ShowDialog() {

        GeneralDialogFragment generalDialogFragment =
                GeneralDialogFragment.newInstance("Network error! Would you like to retry?", "");
        generalDialogFragment.show(getFragmentManager(), "dialog");
    }


    @Override
    public void onOkClicked(GeneralDialogFragment dialog) {
        pd.show();
        LoadRequest(currentpage);

        // do your stuff
    }


    @Override
    public void onCancelClicked(GeneralDialogFragment dialog) {
        dialog.dismiss();
        // do your stuff
    }


    public void LoadRequest(int pagecnt){

        web = new WebRequest(activityHandler);
        web.execute("https://shoppingadviser.in/wc-api/v3/products?fields=title,id,sku,price,regular_price,description,short_description,rating_count,categories,images,featured_src&consumer_key=ck_4484117b7a8ef2f451a99a7e4920a1412fec2be6&consumer_secret=cs_18d0652d18b7309a407fd5c64255aac3fd9dcae8&page=" + pagecnt);
    }
    private void setUpGrid() {
        if (mGridData.size() == 0) {
            fillData();
        } else {
            mGridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, mGridData);
            mGridAdapter.setNotifyOnChange(true);

            mGridView = (GridView) findViewById(R.id.gridview);
            mGridView.setAdapter(mGridAdapter);

            mGridView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {

                    mGridView.setLayoutParams(mGridView.getLayoutParams());

                }
            });
            mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {

                    final Intent productDetailIntent = new Intent().setClass(LandingPageActivity.this, ProductDetailActivity.class);
                    Bundle b = new Bundle();
                    if(!issearchclicked)
                        productsArrayList = db.getAllProducts();
                    b.putSerializable("product", (ModelProducts) productsArrayList.get(position));
                    productDetailIntent.putExtras(b);
                    startActivity(productDetailIntent);

                }
            });

            mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if (firstVisibleItem + visibleItemCount == totalItemCount) {
                        // End has been reached
//


                    }
                }

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
//

                }
            });
            mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id) {
                    return onLongListItemClick(v, pos, id);
                }
            });
        }
    }

    protected boolean onLongListItemClick(View v, int pos, long id) {
        ModelProducts prod = productsArrayList.get(pos);
        if (prod.getSelected() == 1) {
            prod.setSelected(0);
            Toast.makeText(LandingPageActivity.this, "Item Removed from cart",Toast.LENGTH_LONG).show();

        }
        else
        {
            prod.setSelected(1);
            Toast.makeText(LandingPageActivity.this, "Item Added to cart", Toast.LENGTH_LONG).show();


        }
        db.updateProduct(prod);
        return true;
    }

    private void checkIfLoggedIn() {

        SharedPreferences shoppingAdviserPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
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

    public void fillData () {
        productsArrayList = db.getAllProducts();
        if(productsArrayList.size() != 0) {
            mGridData.clear();
            for (int i = 0; i < productsArrayList.size(); i++) {
                GridItem item = new GridItem();
                item.setTitle(productsArrayList.get(i).getProductTitle());
                item.setDiscountPrice(productsArrayList.get(i).getProductDiscountPrice());
                item.setPrice(productsArrayList.get(i).getProductPrice());
                item.setImage(productsArrayList.get(i).getProductImageUrl());
                mGridData.add(item);
            }
            setUpGrid();
        }
//        else {
//            readFromFile = true;
//            parse();
//        }

    }
    public boolean searchclicked(){

        pd.show();

        EditText txt = (EditText) findViewById(R.id.search_edit_text);
        List<ModelProducts> c = db.getWordMatches(txt.getText().toString(),null);

        if(c!=null) {
            productsArrayList = c;
            mGridData.clear();
            for (int i = 0; i < c.size(); i++) {
                GridItem item = new GridItem();
                item.setTitle(c.get(i).getProductTitle());
                item.setDiscountPrice(c.get(i).getProductDiscountPrice());
                item.setPrice(c.get(i).getProductPrice());
                item.setImage(c.get(i).getProductImageUrl());
                mGridData.add(item);
            }
            setUpGrid();
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

                                if(progressStatus == 50){

                                    pd.dismiss();
                                }
                            }
                        });
                    }
                }
            }).start();

        }
        else {
            pd.dismiss();

            Toast.makeText(LandingPageActivity.this, "No item found" ,Toast.LENGTH_LONG).show();

        }
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


        if (id == R.id.nav_second_fragment) {
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
    public void removed() {

        List<ModelProducts> mCartList = db.getCartproducts(null, null, null);

        for (int i = 0; i <= mCartList.size(); i++) {

            int selectedValue = mCartList.get(i).getSelected();

            if (selectedValue == 1) {
                mCartList.get(i).setSelected(0);
                mCartList.get(i).setProductqty("1");
                db.updateProduct(mCartList.get(i));
            }


        }
    }
    public String loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = getAssets().open("jsonresponse.txt");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public void parse() {
        long value = 0;
        String strJson = "";

        if (!readFromFile)
        //Real time data as on website from API
        strJson = jsonstr.replace("&amp;", " &");

        else

        //to read from file.
        strJson = loadJSONFromAsset().replace("&amp;", " &");//jsonstr.replace("&amp;", " &");


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
            for (int i = 0; i < jsonArray.length(); i++) {

                identifier = db.getProductsCount();

                GridItem item = new GridItem();
                StringBuilder category = new StringBuilder();
                StringBuilder images = new StringBuilder();

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String url = "";
                url = jsonObject.optString("featured_src").toString();

                //Response with keys that we have got

                int productId = identifier + 1;//Integer.parseInt(jsonObject.optString("id").toString());
                int productIdentifier = Integer.parseInt(jsonObject.optString("id").toString());

                String productTitle = jsonObject.optString("title").toString();
                String productDescription = jsonObject.optString("description").toString();
                String actualPrice = jsonObject.optString("regular_price").toString();
                String discountPrice = jsonObject.optString("price").toString();
                String soldBy = "Seller";//jsonObject.optString("sold by").toString();// to be removed
                String tag = "product";//jsonObject.optString("tags").toString();// ARRAY SETTING
                String size ="size";//jsonObject.optString("available size").toString();// to be removed
                String SKU = jsonObject.optString("sku").toString();

                int rating = Integer.parseInt(jsonObject.optString("rating_count").toString());
                String productdDescription = jsonObject.optString("short_description").toString();
                String productAdditionalInfo="additional info";//jsonObject.optString("additionalInfo").toString();// to be removed
                String productSellerInfo="seller info";//jsonObject.optString("seller info").toString(); to be removed
                arr = jsonObject.getJSONArray("categories");
                prodImages = jsonObject.getJSONArray("images");
                item.setTitle(productTitle);
                item.setImage(url);
                item.setPrice(actualPrice);
                item.setDiscountPrice(discountPrice);
                mGridData.add(item);

                for (int k = 0; k < prodImages.length(); k++) {

                    try {
                        JSONObject Src = prodImages.getJSONObject(k);
                        String src = Src.getString("src");

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


                ModelProducts product = new ModelProducts(productTitle, productDescription, actualPrice, discountPrice,productId, rating, soldBy, s,tag,size,SKU, url,productdDescription,productAdditionalInfo,productSellerInfo,j,productIdentifier, 0,"1" );
                value = db.addProduct(product);

            }
            productsArrayList = db.getAllProducts();



        }
        catch (JSONException e)
        {
            ShowDialog();

            e.printStackTrace();
        }
        exportDB();
    }


    private void exportDB() {
        File sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File data = Environment.getDataDirectory();
        FileChannel source = null;
        FileChannel destination = null;
        String currentDBPath = "/data/" + getApplicationContext().getPackageName() + "/databases/" + "dbManager";
        String backupDBPath = "dbManager";
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();

        } catch (IOException e) {
            Log.e("", "Error exporting DB", e);
        }
    }
}