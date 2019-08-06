/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {                //view.onclicklistner is is used to identify any clicks on anyviews
    Boolean signupModeActive=true;  //for checking which mode login or signup
    EditText username,password;
    TextView login;

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {         //this method will be called whenever any key is pressed in password edittext and is called twice i.e when it is clicked and 2nd when it is lifted
        if(i==KeyEvent.KEYCODE_ENTER && keyEvent.getAction()==KeyEvent.ACTION_DOWN)       //second condn prevents the function to be called twice
        {
            signup(view);
        }
        return false;
    }


    @Override
    public void onClick(View view) {                            //this function is used to identify clicks on whole screen
        if(view.getId()==R.id.login) {                              //for checking when our textview is clicked
            Button signup=(Button)findViewById(R.id.signup);
            if (signupModeActive) {

            signupModeActive=false;
            signup.setText("Login");
            login.setText("Or,SignUp");
            }
            else
            {
                signupModeActive=true;
                signup.setText("SignUp");
                login.setText("Or,Login");
            }
        }
        else if(view.getId()== R.id.good || view.getId()==R.id.image2){                                              //to remove keyboard
            InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
    }


  public void signup(View view)
  {
    ParseUser user=new ParseUser();
    if(username.getText().toString().matches("") || password.getText().toString().matches(""))
      {
          Toast.makeText(this,"A username or password is required",Toast.LENGTH_LONG).show();
      }
    else {
        if(signupModeActive) {
            user.setUsername(username.getText().toString());
            user.setPassword(password.getText().toString());
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Log.i("SignUp=", "Successful");
                    } else {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();  //getMessage to show the mssg from parse exception
                    }
                }
            });
            showUserlist();
        }
        else
        {
            ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if(user!=null)
                    {
                        Log.i("Login=","Successful");
                        showUserlist();
                    }
                    else
                    {
                        Log.i("Login=","Failed!");

                    }
                }
            });
        }
    }
  }

  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Instagram ");
        username=(EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password) ;
        ConstraintLayout constraintLayout=(ConstraintLayout)findViewById(R.id.good);
        ImageView imageView=(ImageView)findViewById(R.id.image2) ;
        login=(TextView)findViewById(R.id.login);
        constraintLayout.setOnClickListener(this);
        imageView.setOnClickListener(this);
        login.setOnClickListener(this);
        password.setOnKeyListener(this);
        if(ParseUser.getCurrentUser()!=null)                //to check if a user is already login
        {
            showUserlist();
        }

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }
    public void showUserlist()
    {
        Intent intent=new Intent(getApplicationContext(),Main3Activity.class);
        startActivity(intent);

    }



}