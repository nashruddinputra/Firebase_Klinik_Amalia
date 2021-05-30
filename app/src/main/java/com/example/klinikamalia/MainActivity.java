package com.example.klinikamalia;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.example.klinikamalia.AntrianActivity.EXTRA_USER;

public class MainActivity extends AppCompatActivity {

    private View btnHumb;
    private NavigationView navView;
    private DrawerLayout drawerLayout;
    private TextView tvUsername;
    private FirebaseUser user;
    private LinearLayout c_Antrian, c_Jadwal, c_Konsultasi, c_Lokasi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = FirebaseAuth.getInstance().getCurrentUser();

        btnHumb = findViewById(R.id.btn_humberger);
        navView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout_main);
        c_Antrian = findViewById(R.id.container_antrian);
        c_Jadwal = findViewById(R.id.container_jadwal);
        c_Konsultasi = findViewById(R.id.container_konsultasi);
        c_Lokasi = findViewById(R.id.container_lokasi);

        View headerView = navView.getHeaderView(0);
        tvUsername = headerView.findViewById(R.id.text_username);
        String username = user.getDisplayName();
        if (username == null) {
            username = "Empty";
        }
        tvUsername.setText(username);

        c_Konsultasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, KonsultasiActivity.class));
//                KonsultasiActivity
            }
        });

        c_Lokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MapsActivity.class));
            }
        });


        c_Antrian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AntrianActivity.class);
                intent.putExtra(EXTRA_USER, user);
                startActivity(intent);
            }
        });

        c_Jadwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, JadwalActivity.class));
            }
        });

        btnHumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.open();
            }
        });

        FirebaseAuth.getInstance().signOut();

        //mengaktifkan tombol navigasi dan onclick pada setiap item menu
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                Log.d("TAG", "onNavigationItemSelected: " + item.getItemId());
                switch (item.getItemId()) {
                    case R.id.btn_logout:
//                        fAuth.signOut();
                        Intent intent = new Intent(getApplicationContext(), Halaman_awal.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                        return false;

                    case R.id.tentang:
                        Intent i = new Intent(getApplicationContext(), TentangActivity.class);
                        startActivity(i);

                        return false;

                    case R.id.profile:
                        Intent t = new Intent(getApplicationContext(),activity_profile.class );
                        startActivity(t);

                        return false;
                }
                return true;
            }

        });

    }
}