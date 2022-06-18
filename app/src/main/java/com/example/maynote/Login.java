package com.example.maynote;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference dbReferenceUser;
    private EditText inputEmail,inputPassword;
    private Button btnLogin, btnSignUp,btnGoogle,btnFacebook;
    private final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private ProgressDialog progressDialog;
    private Query queryfirstName;
    private String temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase =  FirebaseDatabase.getInstance("https://maynoted-default-rtdb.europe-west1.firebasedatabase.app");
        dbReferenceUser = mDatabase.getReference().child("Users");
        inputEmail = findViewById(R.id.textFieldUsername);
        inputPassword = findViewById(R.id.textFieldPassword);
        btnLogin = findViewById(R.id.btnStartSession);
        btnSignUp = findViewById(R.id.btnCreateAccount);
        btnGoogle = findViewById(R.id.btnContinueGoogle);
        btnFacebook = findViewById(R.id.btnContinueFacebook);
        progressDialog = new ProgressDialog(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionLogin();
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchToRegistar = new Intent(Login.this, Registar.class);
                startActivity(switchToRegistar);
            }
        });
        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchToGoogleLogin = new Intent(Login.this, Google_Login.class);
                startActivity(switchToGoogleLogin);
            }
        });
        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchToFacebookLogin = new Intent(Login.this, Facebook_Login.class);
                startActivity(switchToFacebookLogin);
            }
        });
    }

    private void PermissionLogin() {
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        if(!email.matches(emailPattern)){
            inputEmail.setError("Introduza o email corretamente!");
        }else if(password.isEmpty() || password.length()<6){
            inputPassword.setError("Introduza a palavra-passe corretamente!");
        }else {
            progressDialog.setMessage("Verificando...");
            progressDialog.setTitle("Login");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        queryfirstName = dbReferenceUser.orderByChild(mAuth.getCurrentUser().getUid());
                        queryfirstName.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    temp = snapshot.child(mAuth.getCurrentUser().getUid()).child("firstName").getValue().toString();
                                    Toast.makeText(Login.this, "Bem-vindo, "+temp+"!", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(Login.this, "Bem-vindo!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        progressDialog.dismiss();
                        sendUserToNextActivity();
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(Login.this, "Email ou palavra-chave incorreto!", Toast.LENGTH_SHORT).show();
                        //""+task.getException()
                    }
                }
            });
        }
    }

    private void sendUserToNextActivity() {
        Intent switchToMain = new Intent(Login.this, Main.class);
        startActivity(switchToMain);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null){
            sendUserToNextActivity();
//            queryfirstName = dbReferenceUser.orderByChild(mAuth.getCurrentUser().getUid());
//            queryfirstName.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if(snapshot.exists()){
//                        temp = snapshot.child(mAuth.getCurrentUser().getUid()).child("firstName").getValue().toString();
//                        Toast.makeText(Login.this, "Olá, "+temp+"!", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                    Toast.makeText(Login.this, "Bem-vindo!", Toast.LENGTH_SHORT).show();
//                }
//            });
        }
    }

}