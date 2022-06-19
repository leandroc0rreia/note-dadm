package com.example.maynote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int RESQUEST_IMAGE_CAPTURE = 1;
    private DrawerLayout drawerLayout;
    private EditText textSpace;
    private Button btnPhoto;
    private ImageView menuHamburger;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference dbReferenceUser;
    private String userID;
    private Query queryUserData;
    private TextView firstNameAndLastName, emailText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase =  FirebaseDatabase.getInstance("https://maynoted-default-rtdb.europe-west1.firebasedatabase.app");
        dbReferenceUser = mDatabase.getReference().child("Users");
        drawerLayout = findViewById(R.id.drawer_layout);
        menuHamburger = findViewById(R.id.btnHamburgerMenu);
        textSpace = findViewById(R.id.textSpace);
        btnPhoto = findViewById(R.id.btnPhoto);
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();System.out.println("userID: "+userID);

        NavigationView navigationView = drawerLayout.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        queryUserData = dbReferenceUser.orderByChild(userID);
        queryUserData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String firstNameUser,lastNameUser,emailUser;
                    firstNameAndLastName = (TextView) findViewById(R.id.nameSpace);
                    emailText = (TextView) findViewById(R.id.emailSpace);
                    firstNameUser = snapshot.child(userID).child("firstName").getValue().toString();
                    lastNameUser = snapshot.child(userID).child("lastName").getValue().toString();
                    emailUser = snapshot.child(userID).child("email").getValue().toString();
                    if(firstNameAndLastName!=null) firstNameAndLastName.setText(firstNameUser+" "+lastNameUser);;
                    if(emailText!=null) emailText.setText(emailUser);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        menuHamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.open();
            }
        });

        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri file = getUri();
                camera.putExtra(MediaStore.EXTRA_OUTPUT, file);
                camera.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(camera, RESQUEST_IMAGE_CAPTURE);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                Intent intentHome = new Intent(Main.this, Main.class);
                startActivity(intentHome);
                finish();
                break;
            case R.id.nav_notes:
                Toast.makeText(Main.this, "Em desenvolvimento...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_lembretes:
                Toast.makeText(Main.this, "Em desenvolvimento...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_profile:
                Toast.makeText(Main.this, "Em desenvolvimento...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_logout:
                if(mUser.getUid()!=null){
                    mAuth.signOut();
                    Toast.makeText(Main.this, "Até breve!", Toast.LENGTH_SHORT).show();
                    Intent intentLogout = new Intent(Main.this, Login.class);
                    startActivity(intentLogout);
                    finish();
                }
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void startActivityForResult(Intent camera) {
        // VIRTUALIZACAO DA IMAGEM PARA TEXTO
    }

    //vai buscar o Uri da foto criado em getOutputMediaFile()
    private Uri getUri() {
        return FileProvider.getUriForFile(Main.this, BuildConfig.APPLICATION_ID + ".provider", getOutputMediaFile());
    }

    //cria um ficheiro para guardar imagem
    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyCameraApp");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("Error: ", "Nao foi possivel criar o directorio.");
                return null;
            }
        }
        // Cria um nome para a imagem
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG" + timeStamp + ".jpg");
        return mediaFile;
    }


}