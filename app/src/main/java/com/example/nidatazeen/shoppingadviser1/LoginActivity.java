package com.example.nidatazeen.shoppingadviser1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class LoginActivity extends AppCompatActivity
{
    DatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new DatabaseHandler(this);

        EditText emailTxt = (EditText) findViewById(R.id.etUsername);
        EditText pwdTxt = (EditText) findViewById(R.id.etPassword);
        emailTxt.setText("taskeenfathima28@gmail.com");
        pwdTxt.setText("taskeen28");

        Button loginbutton = (Button) findViewById(R.id.button3);
        loginbutton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                EditText usrTxt = (EditText) findViewById(R.id.etUsername);
                EditText pwdTxt = (EditText) findViewById(R.id.etPassword);
                if (usrTxt.length() == 0) {
                    Toast.makeText(LoginActivity.this, "Username cannot be empty", Toast.LENGTH_LONG).show();

                }
                if (pwdTxt.length() == 0) {
                    Toast.makeText(LoginActivity.this, "Password field cannot be empty", Toast.LENGTH_LONG).show();

                }
                if (usrTxt.length() != 0 && pwdTxt.length() != 0) {
                    //check if email is valid

                    String loginusername = usrTxt.getText().toString();

                    List<Contact> usernameList = db.getAllContacts();
                    for (Contact c: usernameList) {
if (loginusername.compareToIgnoreCase(c.getName()) == 0) {
    Toast.makeText(LoginActivity.this, "Welcome Registered user!", Toast.LENGTH_LONG).show();
    SharedPreferences shoppingAdviserPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    SharedPreferences.Editor editor = shoppingAdviserPreferences.edit();
    editor.putBoolean("isLoggedIn", true);
    editor.commit();
finish();
}
                    }
//                    String userNameStr = shoppingAdviserPreferences.getString("username", "username");
//                    String pwdNameStr = shoppingAdviserPreferences.getString("password", "password");
                    String passwordEntered = pwdTxt.getText().toString();

//                    if (isValidEmail(usrTxt.getText())&& loginusername.compareToIgnoreCase(userNameStr) == 0)
//                    { if (pwdNameStr.compareToIgnoreCase(passwordEntered) == 0) {
//                        //call that function and pass true to it

//                        editor.commit();
//                        Toast.makeText(LoginActivity.this, "Welcome Registered user!", Toast.LENGTH_LONG).show();
//                        finish();
//                    } else {
//                        Toast.makeText(LoginActivity.this, "Invalid Username/Password", Toast.LENGTH_LONG).show();
//
//                    }
//                    } else {
//                        Toast.makeText(LoginActivity.this, "Invalid Username/Password", Toast.LENGTH_LONG).show();
//                    }


                }
            }
        });


    }
    public final static boolean isValidEmail (CharSequence target) {
        if (target == null)
            return false;
        else {
            return Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }




//FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
  //      fab.setOnClickListener(new View.OnClickListener() {
    //        @Override
      //      public void onClick(View view) {
        //        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
          //              .setAction("Action", null).show();
          //  }
        //});
        }
