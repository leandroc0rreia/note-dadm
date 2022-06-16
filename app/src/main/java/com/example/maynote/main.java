package com.example.maynote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class main extends AppCompatActivity {

    private EditText textSpace;
    private Button btnPhoto;
    private ImageView menuHamburger;
    private static final int RESQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

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
                Intent ya = new Intent(main.this, menuhamburger.class);
                startActivity(ya);
            }
        });
    }

    private void startActivityForResult(Intent camera) {
        // VIRTUALIZACAO DA IMAGEM PARA TEXTO
    }

    //vai buscar o Uri da foto criado em getOutputMediaFile()
    private Uri getUri() {
        return FileProvider.getUriForFile(main.this, BuildConfig.APPLICATION_ID + ".provider", getOutputMediaFile());
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