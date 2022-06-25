package com.example.maynote;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.maynote.databinding.NotasBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Activity_Nota extends Menu implements RecyclerNota_Interface{

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference dbReference;
    private Query queryNotas;
    private NotasBinding notasBinding;
    RecyclerView recyclerNotas;
    LinearLayoutManager layoutManager;
    List<ModelClassNota> modelClassNotas;
    AdapterClassNota adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notasBinding = NotasBinding.inflate(getLayoutInflater());
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase =  FirebaseDatabase.getInstance("https://maynoted-default-rtdb.europe-west1.firebasedatabase.app");
        dbReference = mDatabase.getReference().child("Notas").child(mUser.getUid());
        setContentView(notasBinding.getRoot());
        allocateActivityTitle("Notas");
        initData();
    }

    private void initData() {
        modelClassNotas = new ArrayList<>();
        queryNotas = dbReference.orderByChild(mUser.getUid());
        queryNotas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getChildren().forEach(dataSnapshot -> {
                    String titulo = dataSnapshot.child("titulo").getValue().toString();
                    String descricao = dataSnapshot.child("texto").getValue().toString();
                    String[] data = dataSnapshot.child("data").getValue().toString().split(" ");
                    modelClassNotas.add(new ModelClassNota(titulo,descricao,data[0]));
                });
                initRecyclerView();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initRecyclerView() {
        recyclerNotas = findViewById(R.id.recyclerNotas);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerNotas.setLayoutManager(layoutManager);
        adapter = new AdapterClassNota(this, modelClassNotas);
        recyclerNotas.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int pos) {
        Intent itemNota = new Intent(Activity_Nota.this, RecyclerNota_Click.class);
        itemNota.putExtra("titulo",modelClassNotas.get(pos).getTitle());
        itemNota.putExtra("descricao",modelClassNotas.get(pos).getDescription());
        itemNota.putExtra("data",modelClassNotas.get(pos).getDate());
        startActivity(itemNota);
        finish();
    }
}