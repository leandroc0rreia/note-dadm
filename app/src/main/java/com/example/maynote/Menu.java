package com.example.maynote;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Menu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference dbReferenceUser;
    private String userID;
    private Query queryUserData;
    private TextView firstNameAndLastName, emailText;

    public void setContentView(View view){
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.menu,null);
        FrameLayout container = drawerLayout.findViewById(R.id.activityContainer);
        container.addView(view);
        super.setContentView(drawerLayout);
        Toolbar toolbar = drawerLayout.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationView navigationView = drawerLayout.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase =  FirebaseDatabase.getInstance("https://maynoted-default-rtdb.europe-west1.firebasedatabase.app");
        dbReferenceUser = mDatabase.getReference().child("Users");
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        queryUserData = dbReferenceUser.orderByChild(userID);
        queryUserData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String firstNameUser,lastNameUser,emailUser;
                    firstNameAndLastName = findViewById(R.id.nameSpace);
                    emailText = findViewById(R.id.emailSpace);
                    firstNameUser = snapshot.child(userID).child("firstName").getValue().toString();
                    lastNameUser = snapshot.child(userID).child("lastName").getValue().toString();
                    emailUser = snapshot.child(userID).child("email").getValue().toString();
                    if(firstNameAndLastName!=null) firstNameAndLastName.setText(firstNameUser+" "+lastNameUser);
                    if(emailText!=null) emailText.setText(emailUser);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                Intent intentHome = new Intent(Menu.this, Activity_Main.class);
                startActivity(intentHome);
                finish();
                break;
            case R.id.nav_notes:
                Intent intentNotas = new Intent(Menu.this, Activity_Nota.class);
                startActivity(intentNotas);
                break;
            case R.id.nav_lembretes:
                Intent intentLembretes = new Intent(Menu.this, Activity_Lembretes.class);
                startActivity(intentLembretes);
                //  finish();
                Toast.makeText(Menu.this, "Em desenvolvimento...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_profile:
                Intent intentPerfil = new Intent(Menu.this, Activity_Perfil.class);
                startActivity(intentPerfil);
                //finish();
                Toast.makeText(Menu.this, "Em desenvolvimento...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_logout:
                if(mUser.getUid()!=null){
                    mAuth.signOut();
                    Toast.makeText(Menu.this, "Até breve!", Toast.LENGTH_SHORT).show();
                    Intent intentLogout = new Intent(Menu.this, Activity_Login.class);
                    startActivity(intentLogout);
                    finish();
                }
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void allocateActivityTitle(String title){
        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle(title);
        }
    }

}