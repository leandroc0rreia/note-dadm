package com.example.maynote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;

public class registar extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private EditText inputEmail,inputPassword;
    private Button btnRegister;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registar);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        inputEmail = findViewById(R.id.textEmail);
        inputPassword = findViewById(R.id.textPassword);
        btnRegister = findViewById(R.id.btnFinishSignup);
        progressDialog = new ProgressDialog(this);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionAuth();
            }
        });

    }

    private void PermissionAuth() {
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(!email.matches(emailPattern)){
            inputEmail.setError("Introduza o email corretamente!");
        }else if(password.isEmpty() || password.length()<6){
            inputPassword.setError("Introduza a palavra-chave corretamente!");
        }else {
            progressDialog.setMessage("Wait...");
            progressDialog.setTitle("Sign up");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        progressDialog.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(registar.this, "Registo concluído com sucesso!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(registar.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private void sendUserToNextActivity() {
        Intent switchToMain = new Intent(registar.this, login.class);
        startActivity(switchToMain);
    }

}