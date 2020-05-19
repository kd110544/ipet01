package com.example.ipet01;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
//import static com.example.ipet01.MainActivity.UID;



public class Input_information extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    DatabaseReference reff;
    String personName;
    String personGivenName;
    String personFamilyName;
    String personEmail;
    String personId;
    Uri personPhoto;
    String userCity;  //用戶居住地
    String petCount; //用戶幾隻寵物
    DatePickerDialog.OnDateSetListener mDateSetListener;
    String UserBirthday;
    Boolean hasPet=false;
    FirebaseAuth mAuth;
    String UID;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_information);
        Intent intent=getIntent();
        UID=intent.getStringExtra("UID");

        InitialComponent();



        //居住地選擇下拉選單
        ArrayAdapter<CharSequence> adapter01 =ArrayAdapter.createFromResource(this,R.array.city,android.R.layout.simple_spinner_item);
        adapter01.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setAdapter(adapter01);
        spinnerCity.setOnItemSelectedListener(this);

        //幾隻寵物下拉選單
        ArrayAdapter<CharSequence> adapter02 =ArrayAdapter.createFromResource(this,R.array.userPetCount,android.R.layout.simple_spinner_item);
        adapter02.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPetCount.setAdapter(adapter02);
        spinnerPetCount.setOnItemSelectedListener(this);

        //生日日期選擇
        txtUserBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        Input_information.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                UserBirthday=year+"/"+month+"/"+dayOfMonth;
                txtUserBirthday.setText(UserBirthday);
                btnOK.setEnabled(true);
            }
        };













        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());


                if(account !=  null) {
                    personName = account.getDisplayName();
                    personGivenName = account.getGivenName();
                    personFamilyName = account.getFamilyName();
                    personEmail = account.getEmail();
                    personId = account.getId();
                    personPhoto = account.getPhotoUrl();
                }

                reff= FirebaseDatabase.getInstance().getReference().child("user");
                reff.child(UID).child("UID").setValue(UID);
                reff.child(UID).child("Email").setValue(personEmail);
                reff.child(UID).child("Name").setValue(personName);
                reff.child(UID).child("姓").setValue(personFamilyName);
                reff.child(UID).child("名").setValue(personGivenName);
                reff.child(UID).child("Image").setValue(String.valueOf(personPhoto));
                reff.child(UID).child("City").setValue(userCity);
                if (hasPet==true) {
                    reff.child(UID).child("Pet_Count").setValue(petCount);
                }
                if (hasPet==false){
                    reff.child(UID).child("Pet_Count").setValue("0");
                }

                reff.child(UID).child("Birthday").setValue(UserBirthday);





                Intent intent =new Intent(Input_information.this,MainActivity.class);
                startActivity(intent);

            }
        });



    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();

    }




    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id .spinnerCity){    //居住地選擇
        userCity = parent.getSelectedItem().toString();
        //Toast.makeText(this,userCity,Toast.LENGTH_SHORT).show();
        }
        else if (parent.getId()==R.id.spinnerPetCount){  //有幾隻寵物
            petCount=String.valueOf(parent.getItemIdAtPosition(position)+1);
          //  Toast.makeText(this,petCount,Toast.LENGTH_SHORT).show();
        }



    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void hasPetNo(View view) {
        spinnerPetCount.setVisibility(View.INVISIBLE);
        hasPet=false;


    }

    public void hasPetYes(View view) {
        spinnerPetCount.setVisibility(View.VISIBLE);
        hasPet=true;

    }


    private void InitialComponent() {
        btnOK=findViewById(R.id.btnOK);
        radioGroupHasPet=findViewById(R.id.radioGroupHasPet);
        spinnerPetCount=findViewById(R.id.spinnerPetCount);
        spinnerCity=findViewById(R.id.spinnerCity);
        txtUserBirthday=findViewById(R.id.txtUserBirthday);
    }


    Button btnOK;
    RadioGroup radioGroupHasPet;
    Spinner spinnerCity;
    Spinner spinnerPetCount;
    TextView txtUserBirthday;

}
