package com.example.maynote;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;

public class login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private EditText inputEmail,inputPassword;
    private Button btnLogin, btnSignUp,btnGoogle,btnFacebook;
    private TextView textForgotPassword;
    private final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mAuth = FirebaseAuth.getInstance();
        inputEmail = findViewById(R.id.textFieldUsername);
        inputPassword = findViewById(R.id.textFieldPassword);
        btnLogin = findViewById(R.id.btnStartSession);
        btnSignUp = findViewById(R.id.btnCreateAccount);
        btnGoogle = findViewById(R.id.btnContinueGoogle);
        btnFacebook = findViewById(R.id.btnContinueFacebook);
        textForgotPassword = findViewById(R.id.textForgotPassword);
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
                Intent switchToRegistar = new Intent(login.this, registar.class);
                startActivity(switchToRegistar);
            }
        });
        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchToGoogleLogin = new Intent(login.this, google_login.class);
                startActivity(switchToGoogleLogin);
            }
        });
        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchToFacebookLogin = new Intent(login.this, facebook_login.class);
                startActivity(switchToFacebookLogin);
            }
        });
        textForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Forgot Password View
                Intent switchToForgotPassword = new Intent(login.this, forgotpassword.class);
                startActivity(switchToForgotPassword);
            }
        });
    }

    private void PermissionLogin() {
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        if(!email.matches(emailPattern)){
            inputEmail.setError("Introduza o email corretamente!");
        }else if(password.isEmpty() || password.length()<6){
            inputPassword.setError("Introduza a palavra-chave corretamente!");
        }else {
            progressDialog.setMessage("Verificando...");
            progressDialog.setTitle("Login");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        progressDialog.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(login.this, "Login concluído com sucesso!", Toast.LENGTH_SHORT).show();
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(login.this, "Email ou palavra-chave incorreto!", Toast.LENGTH_SHORT).show();
                        //""+task.getException()
                    }
                }
            });
        }
    }

    private void sendUserToNextActivity() {
        Intent switchToMain = new Intent(login.this, main.class);
        startActivity(switchToMain);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null){
//            finish();
//            sendUserToNextActivity();
//            Toast.makeText(login.this, "Já se encontra com login ativo!", Toast.LENGTH_SHORT).show();
        }
    }

}