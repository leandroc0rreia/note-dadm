package com.example.maynote;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.maynote.databinding.PerfilBinding;

public class Perfil extends Menu {

    private PerfilBinding perfilBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        perfilBinding = PerfilBinding.inflate(getLayoutInflater());
        setContentView(perfilBinding.getRoot());
        allocateActivityTitle("Perfil");
    }
}