package com.example.maynote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class registar extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference dbReferenceUser;
    private EditText inputEmail,inputPassword, inputFirstName, inputLastName, inputDateOfBirth;
    private RadioGroup inputGender;
    private RadioButton inputGenderBtn;
    private Button btnRegister;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registar);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase =  FirebaseDatabase.getInstance("https://maynoted-default-rtdb.europe-west1.firebasedatabase.app");
        dbReferenceUser = mDatabase.getReference().child("Users");

        inputFirstName = findViewById(R.id.textFirstName);
        inputLastName = findViewById(R.id.textLastName);
        inputEmail = findViewById(R.id.textEmail);
        inputPassword = findViewById(R.id.textPassword);
        inputDateOfBirth = findViewById(R.id.textDateOfBirth);
        inputGender = findViewById(R.id.chooseGender);
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
        String firstName = inputFirstName.getText().toString();
        String lastName = inputLastName.getText().toString();
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        String dateOfBirth = inputDateOfBirth.getText().toString();
        int genderID = inputGender.getCheckedRadioButtonId();
        inputGenderBtn = findViewById(genderID);
        String gender = inputGenderBtn.getText().toString();

        if(lastName.isEmpty()){
            lastName = "Null";
        }
        if(dateOfBirth.isEmpty()){
            dateOfBirth = "01/01/1970";
        }

        User u = new User(firstName,lastName,email,dateOfBirth,gender);

        if(checkField(email,password,firstName,lastName,dateOfBirth)=="yes") {
            progressDialog.setMessage("Verificando...");
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
                        addUser(u);
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(registar.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
    //
    //  QUANDO NÃO SE PREENCHE UM CAMPO COLOCA-LO ASSIM: "", A DB NÃO ACEITA VAZIOS
    //
    private void sendUserToNextActivity() {
        Intent switchToMain = new Intent(registar.this, login.class);
        startActivity(switchToMain);
    }

    public String checkField(String email, String password, String fName, String lName, String dateBirth){
        String check = "yes";

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String namePattern = "\\b([A-ZÀ-ÿ][-,a-z. ']+[ ]*)+";
        String dateBirthPattern = "(?:0[1-9]|[12][0-9]|3[01])[-\\/.](?:0[1-9]|1[012])[-\\/.](?:19\\d{2}|20[01][0-9]|2020)";

        if(fName.isEmpty() || !fName.matches(namePattern)){
            inputFirstName.setError("Nome inválido ou vazio!");
            check = "no";
        }else if(!lName.matches(namePattern) && !lName.isEmpty()){
            inputLastName.setError("Apelido incorreto");
            check = "no";
        }else if(!email.matches(emailPattern)){
            inputEmail.setError("Introduza o email corretamente!");
            check = "no";
        }else if(password.isEmpty() || password.length()<6){
            inputPassword.setError("Introduza a palavra-chave corretamente!");
            check = "no";
        }else if(!dateBirth.matches(dateBirthPattern) && !dateBirth.isEmpty()){
            inputDateOfBirth.setError("Data de nascimento inválida!");
            check = "no";
        }
        return check;
    }

    public Task<Void> addUser(User user){
        return dbReferenceUser.child(mAuth.getUid()).setValue(user);
    }
}