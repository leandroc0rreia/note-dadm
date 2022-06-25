package com.example.maynote;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.maynote.databinding.RecyclerlembretesClickBinding;
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

public class RecyclerLembretes_Click extends Menu {

    private RecyclerlembretesClickBinding recyclerlembretesClickBinding;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference dbReference;
    private Query queryLembretes;
    private EditText title, date, hour;
    private Button btnGuardarNota, btnRemoverNota;
    private String titulo,hora,data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerlembretesClickBinding = RecyclerlembretesClickBinding.inflate(getLayoutInflater());
        setContentView(recyclerlembretesClickBinding.getRoot());
        allocateActivityTitle("Lembretes");

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase =  FirebaseDatabase.getInstance("https://maynoted-default-rtdb.europe-west1.firebasedatabase.app");
        dbReference = mDatabase.getReference().child("Lembretes").child(mUser.getUid());
        titulo = getIntent().getStringExtra("title");
        data = getIntent().getStringExtra("date");
        hora = getIntent().getStringExtra("hour");

        title = findViewById(R.id.textItemTitleLembretes);
        date = findViewById(R.id.textItemDataLembretes);
        hour = findViewById(R.id.textItemHoraLembretes);

        btnGuardarNota = findViewById(R.id.btnItemSaveLembretes);
        btnRemoverNota = findViewById(R.id.btnItemRemoveLembretes);

        title.setText(titulo);
        date.setText(data);
        hour.setText(hora);

        btnGuardarNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(title.getText().toString().isEmpty()){
                    Toast.makeText(RecyclerLembretes_Click.this, "Título vazio", Toast.LENGTH_SHORT).show();
                }else if(date.getText().toString().isEmpty()){
                    Toast.makeText(RecyclerLembretes_Click.this, "Data vazia", Toast.LENGTH_SHORT).show();
                }else if(hour.getText().toString().isEmpty()){
                    Toast.makeText(RecyclerLembretes_Click.this, "Hora vazia", Toast.LENGTH_SHORT).show();
                }else{
                    queryLembretes = dbReference.orderByChild(mUser.getUid());
                    queryLembretes.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            snapshot.getChildren().forEach(dataSnapshot -> {
                                String tituloSnapshot = dataSnapshot.child("title").getValue().toString();
                                if(tituloSnapshot.equals(titulo)){
                                    Date data = new Date();
                                    String dataPost = data.toLocaleString();
                                    String[] firstData = dataPost.split(" ");
                                    ModelClassLembretes lembretes = new ModelClassLembretes(title.getText().toString(),date.getText().toString(),hour.getText().toString(),dataPost);
                                    dataSnapshot.getRef().setValue(lembretes);
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
                queryLembretes = dbReference.orderByChild(mUser.getUid());
                queryLembretes.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        snapshot.getChildren().forEach(dataSnapshot -> {
                            String tituloSnapshot = dataSnapshot.child("title").getValue().toString();
                            if(tituloSnapshot.equals(titulo)){
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
        Intent back = new Intent(RecyclerLembretes_Click.this, Activity_Lembretes.class);
        startActivity(back);
        finish();
    }
}