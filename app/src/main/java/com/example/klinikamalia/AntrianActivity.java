package com.example.klinikamalia;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

public class AntrianActivity extends AppCompatActivity{

    private android.widget.Button btnAmbilAntrian;
    private FirebaseUser user;
    private String toDay;
    private TextView tvAntrianSaatIni, tvAntrianUser, tvAntrianTotal;

    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_antrian);


        database = FirebaseDatabase.getInstance();

        getIntentData();

        btnAmbilAntrian = findViewById(R.id.btn_ambil_antrian);
        tvAntrianSaatIni = findViewById(R.id.tv_antrian_saat_now);
        tvAntrianUser = findViewById(R.id.tv_antrian_user);
        tvAntrianTotal = findViewById(R.id.tv_antrian_total);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        toDay = df.format(c);

        getAntrianSaatIni();
        getTotalAntrian();
        getMyAntrian();

        btnAmbilAntrian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference antrianRef = database.getReference("Antrian").child(toDay).child(String.valueOf(System.currentTimeMillis()));

                String userid = user.getUid();
                antrianRef.child("user_id").setValue(userid);
                antrianRef.child("has_done").setValue(false);
            }
        });
    }

    private void getTotalAntrian() {
        DatabaseReference  antrianRef = database.getReference("Antrian");

        antrianRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.hasChild(toDay)){
                    antrianRef.child(toDay).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            tvAntrianTotal.setText(String.valueOf(snapshot.getChildrenCount()));
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
                } else {
                    tvAntrianTotal.setText("-");
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void getAntrianSaatIni(){
        DatabaseReference antrianRef = database.getReference("Antrian");

        antrianRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.hasChild(toDay)){
                    antrianRef.child(toDay).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            int lastDone = 0;

                            int i = 0;
                            for (Iterator<DataSnapshot> antrian = snapshot.getChildren().iterator(); antrian.hasNext(); i++) {
                                if((boolean) antrian.next().child("has_done").getValue()) {
                                    lastDone = i + 1;
                                }
                            }

                            tvAntrianSaatIni.setText(String.valueOf(lastDone));
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
                } else {
                    tvAntrianSaatIni.setText("-");
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void getMyAntrian() {
        DatabaseReference antrianRef = database.getReference("Antrian");

        antrianRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.hasChild(toDay)) {
                    antrianRef.child(toDay).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            int myIndex = -1;

                            int i = 0;
                            for (Iterator<DataSnapshot> antrian = snapshot.getChildren().iterator(); antrian.hasNext(); i++) {
                                String myUserId = (String) antrian.next().child("user_id").getValue();
                                Log.d("LL:::", "i: " + i + " ,user_id " + myUserId + " ,myIndex: " + myIndex);
                                Log.d("LL:::", "i: " + i + " ,currentUid() " + user.getUid() + " ,myIndex: " + myIndex);
                                if(myUserId.equals(user.getUid())) {
                                    Log.d("LL:::", "myIndex++  i: " + i);
                                    myIndex = i + 1;
                                    Log.d("LL:::", "myIndex++: " + myIndex);
                                }
                            }

                            if (myIndex != -1) {
                                tvAntrianUser.setText(String.valueOf(myIndex));
                            } else {
                                tvAntrianUser.setText("-");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
                } else {
                    tvAntrianUser.setText("-");
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void getIntentData() {
        user = getIntent().getParcelableExtra(EXTRA_USER);
    }

    static String EXTRA_USER = "EXTRA_USER";
}
