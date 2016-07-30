package com.example.nidatazeen.shoppingadviser1;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    DatabaseHandler db;
    RegisterActivity ab;
    android.os.Handler handler=new android.os.Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

         db = new DatabaseHandler(this);

        EditText emailTxt = (EditText) findViewById(R.id.etEmail);
        EditText pwdTxt = (EditText) findViewById(R.id.etPassword);
        emailTxt.setText("");
        pwdTxt.setText("");

        final Button registerbutton = (Button) findViewById(R.id.bRegister);
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



                        registerUser(userNameString);




                    } else  {
                        Toast.makeText(RegisterActivity.this, "Invalid Email", Toast.LENGTH_LONG).show();

                    }

                }
            }
        });


    }


    private android.os.Handler activityHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            // Do something with msg.obj
            Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_LONG).show();
finish();
        }
    };
    public final static boolean isValidEmail (CharSequence target) {
        if (target == null)
            return false;
        else {
            return Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

public void registerUser(final String username) {
    // Inserting Contacts

    db.addContact(new Contact(username.toString()));
    PostClass req = new PostClass(activityHandler);
    req.execute("https://shoppingadviser.in/wc-api/v3/customers");

}

      public void finish()
      {
          super.finish();
      }
}



