package com.example.maynote;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.maynote.databinding.LembretesBinding;
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

public class Activity_Lembretes extends Menu implements RecyclerLembretes_Interface {

    //https://www.youtube.com/watch?v=7GPUpvcU1FE
    private LembretesBinding lembretesBinding;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference dbReference;
    private Query queryLembretes;
    RecyclerView recyclerLembretes;
    LinearLayoutManager layoutManager;
    List<ModelClassLembretes> modelClassLembretes;
    AdapterClassLembretes adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lembretesBinding = LembretesBinding.inflate(getLayoutInflater());
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase =  FirebaseDatabase.getInstance("https://maynoted-default-rtdb.europe-west1.firebasedatabase.app");
        dbReference = mDatabase.getReference().child("Lembretes").child(mUser.getUid());
        setContentView(lembretesBinding.getRoot());
        allocateActivityTitle("Lembretes");
        initData();
    }

    private void initData() {
        modelClassLembretes = new ArrayList<>();
        queryLembretes = dbReference.orderByChild(mUser.getUid());
        queryLembretes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getChildren().forEach(dataSnapshot -> {
                    String titulo = dataSnapshot.child("title").getValue().toString();
                    String data = dataSnapshot.child("date").getValue().toString();
                    String hora = dataSnapshot.child("hour").getValue().toString();
                    String dataPost = dataSnapshot.child("dataPost").getValue().toString();
                    modelClassLembretes.add(new ModelClassLembretes(titulo,data,hora,dataPost));
                });
                initRecyclerView();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initRecyclerView() {
        recyclerLembretes = findViewById(R.id.recyclerLembretes);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerLembretes.setLayoutManager(layoutManager);
        adapter = new AdapterClassLembretes(this, modelClassLembretes);
        recyclerLembretes.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int pos) {
        Intent itemLembretes = new Intent(Activity_Lembretes.this, RecyclerLembretes_Click.class);
        itemLembretes.putExtra("title",modelClassLembretes.get(pos).getTitle());
        itemLembretes.putExtra("date",modelClassLembretes.get(pos).getDate());
        itemLembretes.putExtra("hour",modelClassLembretes.get(pos).getHour());
        startActivity(itemLembretes);
        finish();
    }
}