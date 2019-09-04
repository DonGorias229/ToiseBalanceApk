package com.example.judeapp.activities;

import android.content.ContentResolver;
import android.content.Intent;

import com.example.judeapp.utils.AppPreferences;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.judeapp.R;
import com.example.judeapp.models.DataPersonne;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.IOException;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String STORAGE_PATH_UPLOADS = "uploads/";
    //constant to track image chooser intent
    private static final int PICK_IMAGE_REQUEST = 234;
    private RadioGroup radioGroup;
    private EditText edtNom;
    private EditText edtPrenom;
    private EditText edtAge;
    private EditText edtTaille;
    private EditText edtPoids;
    private CircleImageView imageView;
    private LinearLayout viewRoot;
    private FirebaseFirestore firebaseFirestore;
    private KProgressHUD kProgressHU;
    private String userNum = AppPreferences.getUserNumero();
    //uri to store file
    private Uri filePath;
    private StorageReference storageReference;

    {
        //display an error if no file is selected
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtNom = findViewById(R.id.nom);
        edtPrenom = findViewById(R.id.prenom);
        edtAge = findViewById(R.id.age);
        edtTaille = findViewById(R.id.taille);
        edtPoids = findViewById(R.id.poids);
        radioGroup = findViewById(R.id.radiogroup);
        viewRoot = findViewById(R.id.viewRoot);
        imageView = findViewById(R.id.image);
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        Button buttonEnregistre = findViewById(R.id.btn_enregistrer);
        Button buttonAnnuler = findViewById(R.id.btn_annuler);

        buttonEnregistre.setOnClickListener(this);
        buttonAnnuler.setOnClickListener(this);
        imageView.setOnClickListener(this);

        buttonEnregistre.setEnabled(true);

        kProgressHU = new KProgressHUD(this);
        kProgressHU.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.liste_personne:
                startActivity(new Intent(MainActivity.this, ListPersonneActivity.class));
                break;

            case R.id.deconnexion:
                AppPreferences.setUserLoggedIn(false);
                AppPreferences.setUserNumero("");
                startActivity(new Intent(MainActivity.this, SignupActivity.class));
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_enregistrer:

                if (filePath == null) {

                    Snackbar.make(viewRoot, "Selection une photo de profils", Snackbar.LENGTH_SHORT).show();

                } else {

                    String vNom = edtNom.getText().toString().trim();
                    String vPrenom = edtPrenom.getText().toString().trim();
                    String vAge = edtAge.getText().toString().trim();
                    String vTaille = edtTaille.getText().toString().trim();
                    String vPoids = edtPoids.getText().toString().trim();
                    String vSexe = "";
                    int selectedId = radioGroup.getCheckedRadioButtonId();

                    if (selectedId <= 0) {
                        Toast.makeText(this, "Selectionner le sexe de la personne", Toast.LENGTH_SHORT).show();
                    } else {
                        RadioButton radioButtonSexe = findViewById(selectedId);
                        vSexe = radioButtonSexe.getText().toString();
                    }

                    if (vNom.isEmpty() || vPrenom.isEmpty() || vAge.isEmpty()
                            || vTaille.isEmpty() || vPoids.isEmpty() || vSexe.isEmpty()) {

                        kProgressHU.dismiss();
                        Snackbar.make(viewRoot, "Veillez bien remplir tout les champs", Snackbar.LENGTH_SHORT).show();

                    } else {
                        uploadFile(vNom, vPrenom, vAge, vTaille, vPoids, vSexe);

                    }
                }

                break;
            case R.id.btn_annuler:
                edtNom.setText("");
                edtPrenom.setText("");
                edtAge.setText("");
                edtTaille.setText("");
                edtPoids.setText("");
                break;
            case R.id.image:
                showFileChooser();
                break;
            default:
        }

    }

    private void addPersonne(String url) {
        String vNom = edtNom.getText().toString().trim();
        String vPrenom = edtPrenom.getText().toString().trim();
        String vAge = edtAge.getText().toString().trim();
        String vTaille = edtTaille.getText().toString().trim();
        String vPoids = edtPoids.getText().toString().trim();
        String vSexe = "";
        int selectedId = radioGroup.getCheckedRadioButtonId();

        if (selectedId <= 0) {
            Toast.makeText(this, "Selectionner le sexe de la personne", Toast.LENGTH_SHORT).show();
        } else {
            RadioButton radioButtonSexe = findViewById(selectedId);
            vSexe = radioButtonSexe.getText().toString();
        }

        if (vNom.isEmpty() || vPrenom.isEmpty() || vAge.isEmpty()
                || vTaille.isEmpty() || vPoids.isEmpty() || vSexe.isEmpty()) {

            kProgressHU.dismiss();
            Snackbar.make(viewRoot, "Veillez bien remplir tout les champs", Snackbar.LENGTH_SHORT).show();
        } else {
            DataPersonne dataPersonne = new DataPersonne(vNom, vPrenom, vAge, vTaille, vPoids, vSexe, userNum, url);
            firebaseFirestore.collection("PERSONNE")
                    .add(dataPersonne).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {

                    //kProgressHU.dismiss();
                    edtNom.setText("");
                    edtPrenom.setText("");
                    edtAge.setText("");
                    edtTaille.setText("");
                    edtPoids.setText("");

                }
            }).addOnCanceledListener(new OnCanceledListener() {
                @Override
                public void onCanceled() {
                    kProgressHU.dismiss();
                    Toast.makeText(MainActivity.this, "Echec", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    private void uploadFile(String nom, String prenom, String age, String taille, String poids, String sexe) {

        //checking if file is available
        if (filePath != null) {
            //displaying progress dialog while image is uploading
            kProgressHU.setLabel("Enregistrement...").show();
            //getting the storage reference
            final StorageReference sRef = storageReference.child(STORAGE_PATH_UPLOADS + System.currentTimeMillis() + "." + getFileExtension(filePath));


            //            //adding the file to reference
            sRef.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Objects.requireNonNull(Objects.requireNonNull(taskSnapshot.getMetadata()).getReference()).getDownloadUrl()
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    String imageUrl = Objects.requireNonNull(task.getResult()).toString();

                                    DataPersonne dataPersonne = new DataPersonne(nom, prenom, age, taille, poids, sexe, userNum, imageUrl);

                                    firebaseFirestore.collection("PERSONNE")
                                            .add(dataPersonne).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {

                                            edtNom.setText("");
                                            edtPrenom.setText("");
                                            edtAge.setText("");
                                            edtTaille.setText("");
                                            edtPoids.setText("");
                                            imageView.setImageResource(R.drawable.image);

                                            kProgressHU.dismiss();

                                            Snackbar.make(viewRoot, "Enregistrement avec succes", Snackbar.LENGTH_SHORT).show();

                                        }
                                    }).addOnCanceledListener(new OnCanceledListener() {
                                        @Override
                                        public void onCanceled() {
                                            Toast.makeText(MainActivity.this, "Echec", Toast.LENGTH_SHORT).show();

                                        }
                                    });


                                }
                            });
                }
            });
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception exception) {
//                            kProgressHU.dismiss();
//                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    })
//                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                            //displaying the upload progress
//                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//                            kProgressHU.setLabel("Enregistrement " + ((int) progress) + "%");
//
//                            if (progress == 100)
//                                kProgressHU.dismiss();
//                            Snackbar.make(viewRoot, "Enregistrement avec succes", Snackbar.LENGTH_SHORT).show();
//
//
//                        }
//
//                    });
        }
    }
}
