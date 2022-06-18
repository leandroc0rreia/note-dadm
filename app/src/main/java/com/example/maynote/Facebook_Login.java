package com.example.maynote;

import androidx.annotation.NonNull;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;


public class Facebook_Login extends Login {

    private static final String TAG = "FacebookLogin";
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        mCallbackManager = CallbackManager.Factory.create();
        FacebookSdk.sdkInitialize(Facebook_Login.this);
        LoginManager.getInstance().logInWithReadPermissions(Facebook_Login.this, Arrays.asList("public_profile"));
        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        finish();
                        Toast.makeText(Facebook_Login.this, "Login cancelado!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(Facebook_Login.this, "Error:"+exception, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    finish();
                    sendUserToNextActivity();
                    Toast.makeText(Facebook_Login.this, "Login com Facebook concluído!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Facebook_Login.this, "Erro do Dev", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void sendUserToNextActivity() {
        finish();
        Intent switchToMain = new Intent(Facebook_Login.this, Main.class);
        startActivity(switchToMain);
    }
}