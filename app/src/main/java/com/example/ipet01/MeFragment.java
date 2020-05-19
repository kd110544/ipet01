package com.example.ipet01;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import static com.example.ipet01.MainActivity.UID;




public class MeFragment extends Fragment {


    private View view;
    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("mefragment","onCreateView");
        view = inflater.inflate(R.layout.me_fragment, container, false);
        final ImageView imgUserImg=(ImageView)view.findViewById(R.id.imgUserImg);
        final TextView txtUserName =(TextView)view.findViewById(R.id.txtUserName);
        final TextView txtUserEmail =(TextView)view.findViewById(R.id.txtUserEmail);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference readImg;


        try {

            readImg = database.getReference().child("user").child(UID).child("Image");
            readImg.addValueEventListener(new ValueEventListener() { //圖片讀取
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String link = dataSnapshot.getValue(String.class);
                    Picasso.get().load(link).into(imgUserImg);    //picasso 是一種讀取相片的套件

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            DatabaseReference readData=database.getReference().child("user").child(UID);
            readData.addValueEventListener(new ValueEventListener() {  //文字資料讀取
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String userName=dataSnapshot.child("Name").getValue().toString();
                    String userEmail=dataSnapshot.child("Email").getValue().toString();


                    txtUserName.setText(userName);
                    txtUserEmail.setText(userEmail);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });


} catch (NullPointerException e) {
        e.getMessage();

        }



        return view;
    }

    @Override
    public void onStart() {
        Log.d("mefragment","onStart");
        super.onStart();


    }

    @Override
    public void onAttach(@NonNull Context context) {
        Log.d("mefragment","onAttach");
        super.onAttach(context);
    }
}








