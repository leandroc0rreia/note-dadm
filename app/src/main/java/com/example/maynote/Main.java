package com.example.maynote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
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
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main extends AppCompatActivity {

    private static final int RESQUEST_IMAGE_CAPTURE = 1;
    private EditText textSpace;
    private Button btnPhoto;
    private ImageView menuHamburger;
    private DrawerLayout drawerLayout;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
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
        });

        menuHamburger = findViewById(R.id.btnHamburgerMenu);
        textSpace = findViewById(R.id.textSpace);
        btnPhoto = findViewById(R.id.btnPhoto);

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

        menuHamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.open();
            }
        });
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