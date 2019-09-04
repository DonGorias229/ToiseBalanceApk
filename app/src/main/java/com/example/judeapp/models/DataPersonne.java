package com.example.judeapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.util.Date;

public class DataPersonne implements Parcelable {

    public static final Creator<DataPersonne> CREATOR = new Creator<DataPersonne>() {
        @Override
        public DataPersonne createFromParcel(Parcel in) {
            return new DataPersonne(in);
        }

        @Override
        public DataPersonne[] newArray(int size) {
            return new DataPersonne[size];
        }
    };
    private String nom;
    private String prenom;
    private String age;
    private String taille;
    private String poids;
    private String sexe;
    private String userNum;
    private String urlImage;
    private Date mTimestamp;

    public DataPersonne() {
    }

    public DataPersonne(String nom, String prenom, String age, String taille, String poids, String sexe) {
        this.nom = nom;
        this.prenom = prenom;
        this.age = age;
        this.taille = taille;
        this.poids = poids;
        this.sexe = sexe;
    }

    public DataPersonne(String nom, String prenom, String age, String taille, String poids, String sexe, String userNum) {
        this.nom = nom;
        this.prenom = prenom;
        this.age = age;
        this.taille = taille;
        this.poids = poids;
        this.sexe = sexe;
        this.userNum = userNum;
    }

    public DataPersonne(String nom, String prenom, String age, String taille, String poids, String sexe, String userNum, String urlImage) {
        this.nom = nom;
        this.prenom = prenom;
        this.age = age;
        this.taille = taille;
        this.poids = poids;
        this.sexe = sexe;
        this.userNum = userNum;
        this.urlImage = urlImage;
    }

    private DataPersonne(Parcel in) {
        nom = in.readString();
        prenom = in.readString();
        age = in.readString();
        taille = in.readString();
        poids = in.readString();
        sexe = in.readString();
        userNum = in.readString();
        urlImage = in.readString();
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getTaille() {
        return taille;
    }

    public void setTaille(String taille) {
        this.taille = taille;
    }

    public String getPoids() {
        return poids;
    }

    public void setPoids(String poids) {
        this.poids = poids;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getUserNum() {
        return userNum;
    }

    public void setUserNum(String userNum) {
        this.userNum = userNum;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    @ServerTimestamp
    public Date getTimestamp() {


        return mTimestamp;
    }

    public void setTimestamp(Date timestamp) {
        mTimestamp = timestamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nom);
        dest.writeString(prenom);
        dest.writeString(age);
        dest.writeString(taille);
        dest.writeString(poids);
        dest.writeString(sexe);
        dest.writeString(userNum);
        dest.writeString(urlImage);
    }
}