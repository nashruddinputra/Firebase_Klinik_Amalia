package com.example.klinikamalia;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class activity_profile extends AppCompatActivity {

    private TextView tvUsername, tvPhoneUser, tvEmailUser;
    private android.widget.Button btn_Ubah_Profile;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        FirebaseApp.initializeApp(this);
        fAuth = FirebaseAuth.getInstance();


        btn_Ubah_Profile = findViewById(R.id.btn_ubah_profile);
        tvUsername = findViewById(R.id.tv_username);
        tvPhoneUser = findViewById(R.id.tv_phone_user);
        tvEmailUser = findViewById(R.id.tv_email_user);


        fAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {

                Log.d("TAG", "user: "+fAuth.getCurrentUser());
                String username = fAuth.getCurrentUser().getDisplayName();
                if (username == null) {
                    username = "Empty";
                }
                tvUsername.setText(username);

                String phone = fAuth.getCurrentUser().getPhoneNumber();
                if (phone == null) {
                    phone = "Empty";
                }
                tvPhoneUser.setText(phone);

                String email = fAuth.getCurrentUser().getEmail();
                if (email == null) {
                    email = "Empty";
                }
                tvEmailUser.setText(email);
            }
        });


        btn_Ubah_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),activity_ubah_profile.class);
                startActivity(intent);
            }
        });

    }
}