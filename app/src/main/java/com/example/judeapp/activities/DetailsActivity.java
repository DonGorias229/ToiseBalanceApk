package com.example.judeapp.activities;

import android.annotation.SuppressLint;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.judeapp.R;
import com.example.judeapp.models.DataPersonne;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailsActivity extends AppCompatActivity {
    TextView textViewNomPrenom;
    TextView textViewAge;
    TextView textViewSexe;
    TextView textViewPoids;
    TextView textViewTaille;
    CircleImageView circleImageView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        DataPersonne dataPersonne = Objects.requireNonNull(getIntent().getExtras()).getParcelable("user");

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        textViewNomPrenom = findViewById(R.id.nom_prenom_id);
        textViewAge = findViewById(R.id.age_id);
        textViewSexe = findViewById(R.id.sexe);
        textViewPoids = findViewById(R.id.poids_id);
        textViewTaille = findViewById(R.id.taille_id);
        circleImageView = findViewById(R.id.image_id);


        assert dataPersonne != null;

//        Log.d("URL", dataPersonne.getUrlImage());

        textViewNomPrenom.setText(dataPersonne.getNom() + " " + dataPersonne.getPrenom());
        textViewAge.setText(dataPersonne.getAge() + " ans");
        textViewSexe.setText(dataPersonne.getSexe());
        textViewPoids.setText(dataPersonne.getPoids());
        textViewTaille.setText(dataPersonne.getTaille());
        Glide.with(getApplicationContext())
                .load(dataPersonne.getUrlImage())
                .into(circleImageView);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
