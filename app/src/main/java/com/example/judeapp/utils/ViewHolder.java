package com.example.judeapp.utils;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.judeapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewHolder extends RecyclerView.ViewHolder {
    public RelativeLayout root;
    public TextView nomEtPrenom;
    public CircleImageView image_avatar;
    public TextView sexe;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        root = itemView.findViewById(R.id.row_linearlayout);
        nomEtPrenom = itemView.findViewById(R.id.name_prenom_id);
        image_avatar = itemView.findViewById(R.id.image_avatar);
        //sexe = itemView.findViewById(R.id.sexe);

    }
}
