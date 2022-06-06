package com.example.maynote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.Login;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;


public class facebook_login extends login {

    private static final String TAG = "FacebookLogin";
    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(facebook_login.this);
        mAuth = FirebaseAuth.getInstance();
        LoginManager.getInstance().logInWithReadPermissions(facebook_login.this, Arrays.asList("public_profile"));

        mCallbackManager = CallbackManager.Factory.create();

<<<<<<< HEAD
            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
=======
        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
>>>>>>> 32a8205 (facebook funciona mas não guarda dados na DB)
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        finish();
                        sendUserToNextActivity();
                        Toast.makeText(facebook_login.this, "Login com Facebook concluído!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        finish();
                        Toast.makeText(facebook_login.this, "Login cancelado!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(facebook_login.this, "Error:"+exception, Toast.LENGTH_SHORT).show();
                    }
                });
    }

<<<<<<< HEAD
    private void updateUI(FirebaseUser user) {
        if(user!=null){
            sendUserToNextActivity();
            Toast.makeText(facebook_login.this, "Login com Facebook concluído!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(facebook_login.this, "Não foi", Toast.LENGTH_SHORT).show();
        }
=======

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
>>>>>>> 32a8205 (facebook funciona mas não guarda dados na DB)
    }

    private void sendUserToNextActivity() {
        finish();
        Intent switchToMain = new Intent(facebook_login.this, main.class);
        startActivity(switchToMain);
    }
}