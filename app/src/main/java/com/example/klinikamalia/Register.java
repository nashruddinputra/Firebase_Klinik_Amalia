package com.example.klinikamalia;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

import org.jetbrains.annotations.NotNull;

public class Register extends AppCompatActivity {

    private EditText myEmail, myPassword, myPhone, myUsername;
    private android.widget.Button btnDaftar;
    private TextView btnMasuk;
    private CheckBox showPassword;
    private ProgressBar Progress;
    private FirebaseAuth fAuth;
//    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fAuth = FirebaseAuth.getInstance();

//        database = FirebaseDatabase.getInstance();

        myEmail = findViewById(R.id.et_email);
        myPassword = findViewById(R.id.et_password);
        myPhone = findViewById(R.id.et_phone);
        myUsername = findViewById(R.id.et_username);
        btnMasuk = findViewById(R.id.btn_masuk);
        btnDaftar = findViewById(R.id.btn_daftar);
        showPassword = findViewById(R.id.showPass);
        Progress = findViewById(R.id.progressBar);


        //btn daftar
        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = myEmail.getText().toString().trim();
                String password = myPassword.getText().toString().trim();
                String username = myUsername.getText().toString().trim();
                String phone = myPhone.getText().toString().trim();

                boolean isvalid=true;

                if(TextUtils.isEmpty(email)) {
                    myEmail.setError("Masukan Email!");
                    isvalid=false;
                }

                if(TextUtils.isEmpty(password)) {
                    myPassword.setError("Masukan Password!");
                    isvalid=false;
                }

                if(TextUtils.isEmpty(username)) {
                    myUsername.setError("Masukan Username!");
                    isvalid=false;
                }

                if(TextUtils.isEmpty(phone)) {
                    myPhone.setError("Masukan No-Telp!");
                    isvalid=false;
                }

                if(!isvalid){
                    return;
                }
                Progress.setVisibility(View.VISIBLE);

                //create user firebase
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(myUsername.getText().toString())
                                    .build();
//                            String userid = fAuth.getCurrentUser().getUid();
//                            DatabaseReference myRef = database.getReference("user").child(userid).child("phone_number");
//                            myRef.setValue(myPhone.getText().toString());

                            fAuth.getCurrentUser().updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    Toast.makeText(Register.this,"Daftar Berhasil.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Register.this,MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            });

                        }else{
                            Toast.makeText(Register.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            Progress.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        //checkbox showpass
        showPassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (showPassword.isChecked()) {
                    myPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    myPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        //ke btn Masuk
        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });

    }

}
