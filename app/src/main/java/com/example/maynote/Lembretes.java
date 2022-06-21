package com.example.maynote;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.maynote.databinding.LembretesBinding;

public class Lembretes extends Menu {

    private LembretesBinding lembretesBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lembretesBinding = LembretesBinding.inflate(getLayoutInflater());
        setContentView(lembretesBinding.getRoot());
        allocateActivityTitle("Lembretes");
    }
}