package com.example.nidatazeen.shoppingadviser1;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EditText emailTxt = (EditText) findViewById(R.id.etEmail);
        EditText pwdTxt = (EditText) findViewById(R.id.etPassword);
        emailTxt.setText("taskeenfathima28@gmail.com");
        pwdTxt.setText("taskeen28");

        Button registerbutton = (Button) findViewById(R.id.bRegister);
        registerbutton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                EditText emailTxt = (EditText) findViewById(R.id.etEmail);
                EditText pwdTxt = (EditText) findViewById(R.id.etPassword);

                if (emailTxt.length() == 0) {
                    Toast.makeText(RegisterActivity.this, "Email field cannot be empty", Toast.LENGTH_LONG).show();

                }
                if (pwdTxt.length() == 0) {
                    Toast.makeText(RegisterActivity.this, "Password field cannot be empty", Toast.LENGTH_LONG).show();

                }
                if (emailTxt.length() != 0 && pwdTxt.length() != 0) {
                    //check if email is valid
                    if (isValidEmail(emailTxt.getText())) {

                        String userNameString = emailTxt.getText().toString();
                        String pwdString = pwdTxt.getText().toString();

                        Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_LONG).show();


                        SharedPreferences shoppingAdviserPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());// getPreferences(Context.MODE_APPEND);
                        SharedPreferences.Editor editor = shoppingAdviserPreferences.edit();
                        editor.putString("username", userNameString);
                        editor.putString("password", pwdString);
                        editor.commit();

                        finish();

                    } else  {
                        Toast.makeText(RegisterActivity.this, "Invalid Email", Toast.LENGTH_LONG).show();

                    }

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



      //  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setOnClickListener(new View.OnClickListener() {
          //  @Override
            //public void onClick(View view) {
              //  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
            //}
        //});
      public void finish()
      {
          super.finish();
      }
}



