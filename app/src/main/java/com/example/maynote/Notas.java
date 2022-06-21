package com.example.maynote;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.maynote.databinding.NotasBinding;

public class Notas extends Menu {

    private NotasBinding notasBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notasBinding = NotasBinding.inflate(getLayoutInflater());
        setContentView(notasBinding.getRoot());
        allocateActivityTitle("Notas");
    }
}