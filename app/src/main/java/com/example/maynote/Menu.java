package com.example.maynote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Menu extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    public Menu (FirebaseAuth mAuth){
        this.mAuth = mAuth;
        mUser = mAuth.getCurrentUser();
        drawerLayout = findViewById(R.id.drawer_layout);

    }
}
