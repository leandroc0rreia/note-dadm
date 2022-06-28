package com.example.maynote;

import androidx.annotation.NonNull;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.maynote.databinding.PerfilBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Activity_Perfil extends Menu {

    private PerfilBinding perfilBinding;
    private Button btnSave, btnCancel;
    private TextView fname, lname, email, password, cpassword, dbirth;
    private RadioGroup inputGender;
    private RadioButton btnGender;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference dbReferenceUser;
    private Query queryProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        perfilBinding = PerfilBinding.inflate(getLayoutInflater());
        setContentView(perfilBinding.getRoot());
        allocateActivityTitle("Perfil");

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase =  FirebaseDatabase.getInstance("https://maynoted-default-rtdb.europe-west1.firebasedatabase.app");
        dbReferenceUser = mDatabase.getReference().child("Users");
        fname = findViewById(R.id.profileFirstName);
        lname = findViewById(R.id.profileLastName);
        email = findViewById(R.id.profileEmail);
        password = findViewById(R.id.profilePassword);
        cpassword = findViewById(R.id.profilePasswordConfirm);
        dbirth = findViewById(R.id.profileDateOfBirth);
        inputGender = findViewById(R.id.profileChooseGender);
        btnSave = findViewById(R.id.btnUserSave);
        btnCancel = findViewById(R.id.btnUserBack);
        progressDialog = new ProgressDialog(this);

        setTextUser();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUser();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToNextActivity();
            }
        });
    }

    private void setTextUser(){
        queryProfile = dbReferenceUser.orderByChild(mUser.getUid());
        queryProfile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String firstName = snapshot.child(mUser.getUid()).child("firstName").getValue().toString();
                    String lastName = snapshot.child(mUser.getUid()).child("lastName").getValue().toString();
                    String emails = snapshot.child(mUser.getUid()).child("email").getValue().toString();
                    String dateOfBirth = snapshot.child(mUser.getUid()).child("dateOfBirth").getValue().toString();
                    String gender = snapshot.child(mUser.getUid()).child("gender").getValue().toString();
                    int genderId;
                    fname.setText(firstName);
                    lname.setText(lastName);
                    email.setText(emails);
                    dbirth.setText(dateOfBirth);
                    switch (gender){
                        case "Masc":
                            genderId = R.id.profileGenderMasc;
                            inputGender.check(genderId);
                            break;
                        case "Fem":
                            genderId = R.id.profileGenderFem;
                            inputGender.check(genderId);
                            break;
                        case "Outro":
                            genderId = R.id.profileGenderOther;
                            inputGender.check(genderId);
                            break;
                    }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void saveUser() {
        String fnames = fname.getText().toString();
        String lnames = lname.getText().toString();
        String emails = email.getText().toString();
        String passwords = password.getText().toString();
        String cpasswords = cpassword.getText().toString();
        String dbirths = dbirth.getText().toString();
        int genderID = inputGender.getCheckedRadioButtonId();
        btnGender = findViewById(genderID);
        String gender = btnGender.getText().toString();

        if(checkField(emails,fnames,lnames,dbirths)=="yes") {
            queryProfile = dbReferenceUser.orderByChild(mUser.getUid());
            queryProfile.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(passwords.isEmpty()){

                    }else if(passwords.equals(cpasswords)){
                        mAuth.getCurrentUser().updatePassword(passwords).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                mAuth.getCurrentUser().reload();
                            }
                        });
                        mAuth.getCurrentUser().updateEmail(emails).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                mAuth.getCurrentUser().reload();
                            }
                        });
                    }else if(!cpasswords.equals(passwords)){
                        cpassword.setError("Palavra-passe diferente!");
                        return;
                    }

                    mAuth.getCurrentUser().updateEmail(emails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mAuth.getCurrentUser().reload();
                        }
                    });
                    User u = new User(fname.getText().toString(),lname.getText().toString(),email.getText().toString(),dbirth.getText().toString(),gender);
                    snapshot.child(mUser.getUid()).getRef().setValue(u);
                    sendUserToNextActivity();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }
    }

    public String checkField(String emails,String fnames, String lnames, String dbirths){
        String check = "yes";

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String namePattern = "\\b([A-ZÀ-ÿ][-,a-z. ']+[ ]*)+";
        String dateBirthPattern = "(?:0[1-9]|[12][0-9]|3[01])[-\\/.](?:0[1-9]|1[012])[-\\/.](?:19\\d{2}|20[01][0-9]|2022)";

        if(fnames.isEmpty() || !fnames.matches(namePattern)){
            fname.setError("Nome inválido ou vazio!");
            check = "no";
        }else if(!lnames.matches(namePattern) && !lnames.isEmpty()){
            lname.setError("Apelido incorreto");
            check = "no";
        }else if(!emails.matches(emailPattern)){
            email.setError("Introduza o email corretamente!");
            check = "no";
        }else if(!dbirths.matches(dateBirthPattern) && !dbirths.isEmpty()){
            dbirth.setError("Data de nascimento inválida!");
            check = "no";
        }
        return check;
    }

    private void sendUserToNextActivity() {
        Intent switchToMain = new Intent(Activity_Perfil.this, Activity_Main.class);
        startActivity(switchToMain);
        finish();
    }

}