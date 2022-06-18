package com.example.maynote;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class Google_Login extends Login {

    private GoogleSignInClient googleSignInClient;
    private GoogleSignInOptions googleSignInOptions;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        signUp();
    }

    public void signUp() {
        Intent signIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signIntent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
                FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            sendUserToNextActivity();
                            Toast.makeText(Google_Login.this, "Login com Google concluído!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(Google_Login.this, "Login para continuar!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }catch (ApiException e){
                e.printStackTrace();
            }
        }
    }

    private void sendUserToNextActivity() {
        Intent switchToMain = new Intent(Google_Login.this, Main.class);
        startActivity(switchToMain);
        finish();
    }
}
