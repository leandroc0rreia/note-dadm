package com.example.maynote;

import static android.Manifest.permission.CAMERA;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.maynote.databinding.MainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.util.Date;

public class Activity_Main extends Menu {

    private EditText textSpace;
    private Button btnPhoto, btnConfirm, btnCancel;
    private Spinner spinnerSpaces;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference dbReferenceUser;
    private String userID, chooseSpace;
    private MainBinding mainBinding;
    private static final int RESQUEST_IMAGE_CAPTURE = 1;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = MainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase =  FirebaseDatabase.getInstance("https://maynoted-default-rtdb.europe-west1.firebasedatabase.app");
        dbReferenceUser = mDatabase.getReference();
        textSpace = findViewById(R.id.textSpace);
        btnPhoto = findViewById(R.id.btnPhoto);
        btnConfirm = findViewById(R.id.confirmSpace);
        btnCancel = findViewById(R.id.cancelSpace);
        spinnerSpaces = findViewById(R.id.spinnerSpaces);
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkPermission()){
                    takePhoto();
                }else{
                    requestPermission();
                }
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textSpace.clearFocus();
                addToSpace(textSpace.getText().toString(), chooseSpace);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textSpace.setText("");
                textSpace.clearFocus();
            }
        });

        textSpace.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    btnConfirm.setVisibility(View.VISIBLE);
                    btnCancel.setVisibility(View.VISIBLE);
                    spinnerSpaces.setVisibility(View.VISIBLE);
                }else{
                    btnConfirm.setVisibility(View.INVISIBLE);
                    btnCancel.setVisibility(View.INVISIBLE);
                    spinnerSpaces.setVisibility(View.INVISIBLE);
                }
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spaces, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSpaces.setAdapter(adapter);

        spinnerSpaces.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                chooseSpace=parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                textSpace.clearFocus();
            }
        });
    }

    private void getText(Bitmap bit){
        InputImage image = InputImage.fromBitmap(bit,0);
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        Task<Text> result = recognizer.process(image).addOnSuccessListener(new OnSuccessListener<Text>() {
            @Override
            public void onSuccess(Text text) {
                StringBuilder result = new StringBuilder();
                String texto = "";
                for (Text.TextBlock block : text.getTextBlocks()) {
                    String blockText = block.getText();
                    Point[] blockCornerPoints = block.getCornerPoints();
                    Rect blockFrame = block.getBoundingBox();
                    for (Text.Line line : block.getLines()) {
                        String lineText = line.getText();
                        Point[] lineCornerPoints = line.getCornerPoints();
                        Rect lineFrame = line.getBoundingBox();
                        for (Text.Element element : line.getElements()) {
                            String elementText = element.getText();
                            result.append(elementText);
                        }
                        texto += blockText + "\n";
                    }
                }
                textSpace.setText(texto);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Activity_Main.this, "Falha ao detetar texto da imagem!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkPermission() {
        int cameraPermission = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        return cameraPermission == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission(){
        int PERMITION_CODE = 200;
        ActivityCompat.requestPermissions(this,new String[]{CAMERA},PERMITION_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            getText(bitmap);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0){
            boolean camPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            if(camPermission){
                System.out.println("Permissões garantidas.");
            }else{
                System.out.println("Permissões não garantidas.");
            }
        }
    }

    private void takePhoto(){
        Intent photo = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(photo.resolveActivity(getPackageManager())!=null){
            startActivityForResult(photo, RESQUEST_IMAGE_CAPTURE);
        }
    }

    private void addToSpace(String text, String chooseSpace){
        Date data = new Date();
        String dataPost = data.toLocaleString();
        switch (chooseSpace){
            case "Notas":
                Nota n = new Nota(chooseSpace,text,dataPost);
                addNota(n);
                break;
        }
        textSpace.setText("");
    }

    public Task<Void> addNota(Nota nota){
        return dbReferenceUser.child("Notas").child(mAuth.getUid()).push().setValue(nota);
    }
}