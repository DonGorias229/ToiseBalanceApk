package com.example.judeapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.judeapp.R;
import com.example.judeapp.models.DataPersonne;
import com.example.judeapp.utils.AppPreferences;
import com.example.judeapp.utils.ViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;
import java.util.Random;

public class ListPersonneActivity extends AppCompatActivity {
    private String userNum = AppPreferences.getUserNumero();

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_personne);
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        RecyclerView recyclerView = findViewById(R.id.id_person);
        recyclerView.setHasFixedSize(false);

        Log.d("UserNum", userNum);
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        com.google.firebase.firestore.Query query = FirebaseFirestore.getInstance()
                .collection("PERSONNE")
                .whereEqualTo("userNum", userNum)
                .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<DataPersonne> options = new FirestoreRecyclerOptions.Builder<DataPersonne>()
                .setQuery(query, DataPersonne.class)
                .build();


        FirestoreRecyclerAdapter recyclerAdapter = new FirestoreRecyclerAdapter<DataPersonne, ViewHolder>(options) {
            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, final int position, @NonNull final DataPersonne model) {

                Glide.with(getApplicationContext())
                        .load(model.getUrlImage())
                        .into(holder.image_avatar);

                holder.nomEtPrenom.setText(model.getNom() + " " + model.getPrenom());
                holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(ListPersonneActivity.this, DetailsActivity.class).putExtra("user", model);
                        startActivity(intent);

                    }
                });
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_list_person, viewGroup, false);
                return new ViewHolder(view);
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
            }

            @Override
            public int getItemCount() {
                return super.getItemCount();
            }
        };
        recyclerAdapter.startListening();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerAdapter);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private int generateRandomInteger(int i, int i1) {
        Random random = new Random();
        return random.nextInt(i1 - i) + i;
    }


    private int setColorFilter(int color) {
        if ((color % 4) == 0) {
            return ContextCompat.getColor(getApplicationContext(), R.color.one_round);
        }
        if ((color % 3) == 0) {
            return ContextCompat.getColor(getApplicationContext(), R.color.two_round);
        }
        if ((color % 2) == 0) {
            return ContextCompat.getColor(getApplicationContext(), R.color.three_round);
        } else {
            return ContextCompat.getColor(getApplicationContext(), R.color.four_round);
        }
    }

}
