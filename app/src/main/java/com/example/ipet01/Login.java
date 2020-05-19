package com.example.ipet01;

import android.accounts.Account;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import static com.example.ipet01.MainActivity.UID;

public class Login extends Activity {
        private SignInButton signInButton;
        private GoogleSignInClient mGoogleSignInClient;
        private  String TAG = "MainActivity";
        private FirebaseAuth mAuth;
        private Button btnSignOut;
        private int RC_SIGN_IN = 1;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

        String UID;




    @Override
    public void onBackPressed() {
        super.onPause();
        finishAffinity();
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if(account!=null){
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);


        }

        setContentView(R.layout.login);

        signInButton = findViewById(R.id.sign_in_button);
        mAuth = FirebaseAuth.getInstance();
        btnSignOut = findViewById(R.id.sign_out_button);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGoogleSignInClient.signOut();
                Toast.makeText(Login.this,"You are Logged Out",Toast.LENGTH_SHORT).show();
                btnSignOut.setVisibility(View.INVISIBLE);
            }
        });


    }

    private void signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask){
        try{

            GoogleSignInAccount acc = completedTask.getResult(ApiException.class);
            //Toast.makeText(Login.this,"Signed In Successfully",Toast.LENGTH_SHORT).show();
           FirebaseGoogleAuth(acc);





            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
















            // finish();

        }
        catch (ApiException e){
            Toast.makeText(Login.this,"Sign In Failed",Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(null);
        }
    }

    private void FirebaseGoogleAuth(GoogleSignInAccount acct) {
        //check if the account is null
        if (acct != null) {
            AuthCredential authCredential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
            mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                      //  Toast.makeText(Login.this, "Successful", Toast.LENGTH_SHORT).show();
                        Toast.makeText(Login.this,"登入中...." ,Toast.LENGTH_SHORT).show();
                        FirebaseUser user = mAuth.getCurrentUser();
                        UID = user.getUid();
                       // Toast.makeText(Login.this, UID, Toast.LENGTH_SHORT).show();

                        DatabaseReference readData = database.getReference().child("user").child(UID);
                        readData.addValueEventListener(new ValueEventListener() {  //文字資料讀取
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String test;
                              try {
                                    if ((test = dataSnapshot.getValue().toString()) != null) {
                                        Intent intent = new Intent(Login.this, MainActivity.class);
                                        startActivity(intent);


                                    }
                                } catch (java.lang.NullPointerException e) {

                                    Intent intent = new Intent(Login.this, Input_information.class);
                                    intent.putExtra("UID",UID);
                                    startActivity(intent);




                                }
                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });




                /*    Intent intent =new Intent();
                        intent.putExtra("UID",UID);
                        setResult(RESULT_OK,intent);
                        finish();*/


                      //  updateUI(user);
                    } else {
                        Toast.makeText(Login.this, "Failed", Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                }
            });
        }
        else{
            Toast.makeText(Login.this, "登入失敗", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI(FirebaseUser fUser){
        btnSignOut.setVisibility(View.VISIBLE);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if(account !=  null){
            String personName = account.getDisplayName();
            String personGivenName = account.getGivenName();
            String personFamilyName = account.getFamilyName();
            String personEmail = account.getEmail();
            String personId = account.getId();
            Uri personPhoto = account.getPhotoUrl();

            Toast.makeText(Login.this,"登入中...." ,Toast.LENGTH_SHORT).show();

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            UID = user.getUid();
            Toast.makeText(Login.this, UID, Toast.LENGTH_SHORT).show();
            Intent intent =new Intent(Login.this,MainActivity.class);
            startActivity(intent);
        }



    }
}
