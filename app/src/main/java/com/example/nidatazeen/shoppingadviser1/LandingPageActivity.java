package com.example.nidatazeen.shoppingadviser1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

public class LandingPageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences shoppingAdviserPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = shoppingAdviserPreferences.edit();
        boolean isLoggedIn = shoppingAdviserPreferences.getBoolean("isLoggedIn",false);

        editor.clear();
        editor.commit();

        Button loginbutton = (Button) findViewById(R.id.button2);
        Button regbutton = (Button) findViewById(R.id.button);

        loginbutton.setVisibility(View.VISIBLE);
        regbutton.setVisibility(View.VISIBLE);

        loginbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Intent loginIntent = new Intent().setClass(LandingPageActivity.this, LoginActivity.class);
//                loginIntent.putExtra(Intent.ACTION_EDIT, new String[]{"Username"});
//                loginIntent.putExtra(Intent.ACTION_EDIT, new String[]{"Password"});
                startActivity(loginIntent);

            }
        });
        Button registerbutton = (Button) findViewById(R.id.button);
        registerbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Intent registerIntent = new Intent().setClass(LandingPageActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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

        //check isloggedIn variable and hide/show buttons
        Button loginbutton = (Button) findViewById(R.id.button2);
        Button regbutton = (Button) findViewById(R.id.button);

        SharedPreferences shoppingAdviserPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean isLoggedIn = shoppingAdviserPreferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            Toast.makeText(LandingPageActivity.this, "loggedin", Toast.LENGTH_LONG).show();
            loginbutton.setVisibility(View.GONE);
            regbutton.setVisibility(View.GONE);
        } else {

            loginbutton.setVisibility(View.VISIBLE);
            regbutton.setVisibility(View.VISIBLE);
        }

    }
    public void finish()
    {
        super.finish();
    }
}