package com.example.maynote;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.maynote.databinding.RecyclernotaClickBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class RecyclerNota_Click extends Menu {

    private RecyclernotaClickBinding recyclernotaClickBinding;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference dbReference;
    private Query queryNota;
    private EditText title, description;
    private Button btnGuardarNota, btnRemoverNota;
    private String titulo,descricao,data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclernotaClickBinding = RecyclernotaClickBinding.inflate(getLayoutInflater());
        setContentView(recyclernotaClickBinding.getRoot());
        allocateActivityTitle("Notas");

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase =  FirebaseDatabase.getInstance("https://maynoted-default-rtdb.europe-west1.firebasedatabase.app");
        dbReference = mDatabase.getReference().child("Notas").child(mUser.getUid());
        titulo = getIntent().getStringExtra("titulo");
        descricao = getIntent().getStringExtra("descricao");
        data = getIntent().getStringExtra("data");

        title = findViewById(R.id.textItemTitle);
        description = findViewById(R.id.textItemDescription);
        btnGuardarNota = findViewById(R.id.btnItemSave);
        btnRemoverNota = findViewById(R.id.btnItemRemove);

        title.setText(titulo);
        description.setText(descricao);

        btnGuardarNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(title.getText().toString().isEmpty()){
                    Toast.makeText(RecyclerNota_Click.this, "Descrição vazia", Toast.LENGTH_SHORT).show();
                }else if(description.getText().toString().isEmpty()){
                    Toast.makeText(RecyclerNota_Click.this, "Título vazio", Toast.LENGTH_SHORT).show();
                }else{
                    queryNota = dbReference.orderByChild(mUser.getUid());
                    queryNota.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            snapshot.getChildren().forEach(dataSnapshot -> {
                                String descricaoSnapshot = dataSnapshot.child("texto").getValue().toString();
                                if(descricaoSnapshot.equals(descricao)){
                                    Date data = new Date();
                                    String dataPost = data.toLocaleString();
                                    String[] firstData = dataPost.split(" ");
                                    Nota nota = new Nota(title.getText().toString(),description.getText().toString(),firstData[0]);
                                    dataSnapshot.getRef().setValue(nota);
                                    nextActivity();
                                }
                            });
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        btnRemoverNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryNota = dbReference.orderByChild(mUser.getUid());
                queryNota.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        snapshot.getChildren().forEach(dataSnapshot -> {
                            String descricaoSnapshot = dataSnapshot.child("texto").getValue().toString();
                            if(descricaoSnapshot.equals(descricao)){
                                dataSnapshot.getRef().removeValue();
                            }
                        });
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                nextActivity();
            }
        });
    }

    private void nextActivity(){
        Intent back = new Intent(RecyclerNota_Click.this, Activity_Nota.class);
        startActivity(back);
        finish();
    }
}