package com.example.judeapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.judeapp.R;
import com.example.judeapp.models.Utilisateur;
import com.example.judeapp.utils.AppPreferences;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout rootSigup;
    private FirebaseFirestore firebaseFirestore;
    private KProgressHUD kProgressHU;
    private AppPreferences appPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Button buttonConnexion = findViewById(R.id.btn_login);
        Button buttonInscription = findViewById(R.id.btn_register);
        rootSigup = findViewById(R.id.rootSigup);
        buttonConnexion.setOnClickListener(this);
        buttonInscription.setOnClickListener(this);

        firebaseFirestore = FirebaseFirestore.getInstance();
        appPreferences = new AppPreferences(getApplicationContext());

        kProgressHU = new KProgressHUD(this);
        kProgressHU.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);

        if (AppPreferences.isUserLoggedIn()) {
            startActivity(new Intent(SignupActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register: {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
                View mView = layoutInflaterAndroid.inflate(R.layout.item_signup, rootSigup, false);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
                alertDialogBuilderUserInput.setView(mView);
                alertDialogBuilderUserInput.setCancelable(false)
                        .setPositiveButton("Crée un compte", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                TextInputEditText textInputEditTextNom = mView.findViewById(R.id.edit_nom);
                                TextInputEditText textInputEditTextPrenom = mView.findViewById(R.id.edit_prenom);
                                TextInputEditText textInputEditTextNumero = mView.findViewById(R.id.edit_numero);
                                TextInputEditText textInputEditTextPassword = mView.findViewById(R.id.edit_password);
                                TextInputEditText textInputEditTextCfmPassword = mView.findViewById(R.id.edit_cfmpassword);

                                String vNom = requireNonNull(textInputEditTextNom.getText()).toString().trim();
                                String vPrenom = requireNonNull(textInputEditTextPrenom.getText()).toString();
                                String vNumero = requireNonNull(textInputEditTextNumero.getText()).toString();
                                String vPassword = requireNonNull(textInputEditTextPassword.getText()).toString();
                                String vCfmPassword = requireNonNull(textInputEditTextCfmPassword.getText()).toString();

                                if (vNom.isEmpty() || vPrenom.isEmpty() || vNumero.isEmpty() || vPassword.isEmpty() || vCfmPassword.isEmpty()) {
                                    Toast.makeText(SignupActivity.this, "Veillez bien remplir tout les champs", Toast.LENGTH_SHORT).show();
                                } else if (!vCfmPassword.equals(vPassword)) {
                                    Toast.makeText(SignupActivity.this, "Les mots de passe entrer sont pas correct", Toast.LENGTH_SHORT).show();
                                } else {
                                    Utilisateur utilisateur = new Utilisateur(vNom, vPrenom, vNumero, vPassword);
                                    kProgressHU.setLabel("Inscription...").show();
                                    doesUserNumberExist(vNumero, utilisateur);

                                }

                            }
                        })
                        .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                alertDialogAndroid.show();
                break;
            }
            case R.id.btn_login: {

                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
                View mView = layoutInflaterAndroid.inflate(R.layout.item_signin, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
                alertDialogBuilderUserInput.setView(mView);
                alertDialogBuilderUserInput.setCancelable(false)
                        .setPositiveButton("Se connecter", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                kProgressHU.setLabel("Connexion...").show();
                                TextInputEditText textInputEditTextNumero = mView.findViewById(R.id.edit_numero);
                                TextInputEditText textInputEditTexPassword = mView.findViewById(R.id.edit_password);

                                String mNumero = textInputEditTextNumero.getText().toString().trim();
                                String mPassword = textInputEditTexPassword.getText().toString().trim();
                                if (mNumero.isEmpty() || mPassword.isEmpty()) {
                                    kProgressHU.setLabel("Connexion...").dismiss();
                                    Toast.makeText(SignupActivity.this, "Champs vide", Toast.LENGTH_SHORT).show();
                                } else {
                                    DocumentReference documentReference = firebaseFirestore.collection("UTILISATEUR").document(mNumero);
                                    documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {

                                                Utilisateur utilisateur = Objects.requireNonNull(task.getResult().toObject(Utilisateur.class));
                                                if (utilisateur != null) {

                                                    try {
                                                        if (utilisateur.getPassword().equals(mPassword)) {
                                                            kProgressHU.setLabel("Connexion...").dismiss();
                                                            AppPreferences.setUserLoggedIn(true);
                                                            AppPreferences.setUserNumero(mNumero);
                                                            startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                                            finish();

                                                        } else {
                                                            kProgressHU.setLabel("Connexion...").dismiss();
                                                            Toast.makeText(getApplicationContext(), "Mauvais mot de passe", Toast.LENGTH_SHORT).show();
                                                        }

                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                } else {
                                                    //User not existe

                                                }

                                            } else {
                                            }
                                        }
                                    });
                                }

                            }
                        })
                        .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }


                        });

                AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                alertDialogAndroid.show();

                break;
            }


        }


    }


    public void doesUserNumberExist(String numero, Utilisateur firestoreUserModel) {
        Query query = firebaseFirestore.collection("UTILISATEUR").whereEqualTo("numero", numero);
        query.get().addOnCompleteListener(task -> {
            if (Objects.requireNonNull(task.getResult()).size() > 0) {
                Toast.makeText(getApplicationContext(), "Ce numéro existe déjà dans la base de données", Toast.LENGTH_SHORT).show();
                kProgressHU.setLabel("Enregistrement des informations")
                        .dismiss();
            } else {
                //add a new user to Firestore database
                addNewRegisteredUser(firestoreUserModel);
            }
        });
    }

    public void addNewRegisteredUser(Utilisateur firestoreUserModel) {

        Task<Void> newUser = firebaseFirestore.collection("UTILISATEUR").document(firestoreUserModel.numero).set(firestoreUserModel);
        newUser.addOnSuccessListener(aVoid -> {

            kProgressHU.setLabel("Enregistrement des informations")
                    .dismiss();

            Toast.makeText(getApplication(), "Utilisateur enregistré avec succès", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e ->

                Log.d("Erro", "Error has occurred " + e.getMessage()));
        kProgressHU.setLabel("Enregistrement des informations")
                .dismiss();

//        Snackbar.make(, "Vérifiez votre connexion internet",
//                Snackbar.LENGTH_SHORT).show();

    }
}
