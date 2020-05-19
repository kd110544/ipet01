package com.example.ipet01;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URI;




public class MainActivity extends AppCompatActivity {



    FirebaseAuth mAuth;
    public static String UID;
    boolean ok01=false;
    FirebaseDatabase database = FirebaseDatabase.getInstance();




    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("app","onCreate");

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            UID = user.getUid();

            Toast.makeText(MainActivity.this, UID, Toast.LENGTH_SHORT).show();

            ok01=true;
        }

        if(ok01==true) {


            DatabaseReference readData = database.getReference().child("user").child(UID);
            readData.addValueEventListener(new ValueEventListener() {  //文字資料讀取
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String test;
                    try {
                        if ((test = dataSnapshot.getValue().toString()) == null) {

                        }
                    } catch (java.lang.NullPointerException e) {

                        Intent intent = new Intent(MainActivity.this, Input_information.class);
                        intent.putExtra("UID",UID);
                        startActivity(intent);



                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }



        InitialComponent();  //初始化元件

        bottomNavigationView.setSelectedItemId(R.id.menu_pair);    //程式打開時 底部導覽按鈕"配對"選取
        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, new PairFragment()).commit();
        //程式打開時 頁面在"配對"
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                Fragment fragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.menu_test:
                        fragment = new TestFragment();
                        break;
                    case R.id.menu_camera:
                        fragment = new CameraFragment();
                        break;
                    case R.id.menu_Me:

                        fragment = new MeFragment();
                        break;
                    case R.id.menu_pair:
                        fragment = new PairFragment();
                        break;
                    case R.id.menu_like:
                        fragment = new LikeFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, fragment).commit();


                return true;   //true=被選取  false=未被選取
            }
        });
    }












    @Override
    protected void onRestart() {
        super.onRestart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);


        if (account != null) {

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            UID = user.getUid();
            Toast.makeText(MainActivity.this, UID, Toast.LENGTH_SHORT).show();

        }









    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("MainActivity", "onStart");




        }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.toolbar,menu);
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public void onBackPressed() {
        super.onPause();
        finishAffinity();
    }


    private void InitialComponent() {     //初始化元件區
        bottomNavigationView=findViewById(R.id.BottomNav);

    }

    BottomNavigationView bottomNavigationView;  //底部導覽欄


}

