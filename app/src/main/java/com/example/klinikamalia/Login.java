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

public class Login extends AppCompatActivity {

    private CheckBox showPassword;
    private EditText myEmail, myPassword;
    private android.widget.Button btnMasuk;
    private TextView btnDaftar;
    private ProgressBar Progress;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fAuth = FirebaseAuth.getInstance();

        myEmail = findViewById(R.id.et_email);
        myPassword = findViewById(R.id.et_password);
        btnMasuk = findViewById(R.id.btn_masuk);
        btnDaftar = findViewById(R.id.btn_daftar);
        showPassword = findViewById(R.id.showPass);
        Progress = findViewById(R.id.progressBar);


        //btn masuk
        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = myEmail.getText().toString().trim();
                String password = myPassword.getText().toString().trim();

                boolean isvalid=true;
                if(TextUtils.isEmpty(email)){
                    myEmail.setError("Masukan Email!");
                    isvalid=false;
                }

                if(TextUtils.isEmpty(password)){
                    myPassword.setError("Masukan Password!");
                    isvalid=false;
                }

                if(!isvalid){
                    return;
                }



                // Loginuser firebase
                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Login.this,"Login Berhasil.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }else{
                            Toast.makeText(Login.this, "Password salah!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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

        //ke btn daftar
        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Register.class));
            }
        });

    }

}