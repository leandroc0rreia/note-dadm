package com.example.maynote;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        configureNextButton();
    }

    private void configureNextButton() {
        //Login with Username
        Intent switchToMain = new Intent(login.this, main.class);
        Button nextBtnStartSession = findViewById(R.id.btnStartSession);
        nextBtnStartSession.setOnClickListener(view -> startActivity(switchToMain));
        //Login with Google
        Intent switchToGoogleLogin = new Intent(login.this, google_login.class);
        Button nextBtnStartSessionGoogle = findViewById(R.id.btnContinueGoogle);
        nextBtnStartSessionGoogle.setOnClickListener(view -> startActivity(switchToGoogleLogin));
        //Login with Facebook
        Intent switchToFacebookLogin = new Intent(login.this, facebook_login.class);
        Button nextBtnStartSessionFacebook = findViewById(R.id.btnContinueFacebook);
        nextBtnStartSessionFacebook.setOnClickListener(view -> startActivity(switchToFacebookLogin));
        //Forgot Password View
        Intent switchToForgotPassword = new Intent(login.this, forgotpassword.class);
        TextView nextTextViewForgotPassword = findViewById(R.id.textForgotPassword);
        nextTextViewForgotPassword.setOnClickListener(view -> startActivity(switchToForgotPassword));
        //Sign Up View
        Intent switchToRegistar = new Intent(login.this, registar.class);
        Button nextBtnCreateAccount = findViewById(R.id.btnCreateAccount);
        nextBtnCreateAccount.setOnClickListener(view -> startActivity(switchToRegistar));

    }
}