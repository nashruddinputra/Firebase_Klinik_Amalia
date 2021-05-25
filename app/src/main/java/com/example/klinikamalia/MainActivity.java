package com.example.klinikamalia;

import android.content.Intent;
import android.net.Uri;
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

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity /*implements NavigationView.OnNavigationItemSelectedListener*/ {

    private View btnHumb;
    private NavigationView navView;
    private DrawerLayout drawerLayout;
    private TextView tvUsername;
    private FirebaseAuth fAuth;
    private LinearLayout c_Antrian, c_Jadwal, c_Konsultasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fAuth = FirebaseAuth.getInstance();

        btnHumb = findViewById(R.id.btn_humberger);
        navView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        c_Antrian = findViewById(R.id.container_antrian);
        c_Jadwal = findViewById(R.id.container_jadwal);
        c_Konsultasi = findViewById(R.id.container_konsultasi);

        View headerView = navView.getHeaderView(0);
        tvUsername = headerView.findViewById(R.id.text_username);
        String username = fAuth.getCurrentUser().getDisplayName();
        if (username == null) {
            username = "Belum Di isi";
        }
        tvUsername.setText(username);

        btnHumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.open();
            }
        });

        c_Konsultasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWhatsappContact("+6281578743265");
            }
        });

        //mengaktifkan tombol navigasi dan onclick pada setiap item menu
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                Log.d("TAG", "onNavigationItemSelected: " + item.getItemId());
                switch (item.getItemId()) {
                    case R.id.btn_logout:
                        fAuth.signOut();
                        Intent intent = new Intent(getApplicationContext(), Halaman_awal.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        return true;
                    default:
                        return false;
                }
            }
        });

    }
    //memanggil fungsi openWhatsappContact dari variable c_Konsultasi
    void openWhatsappContact(String number) {
        Log.d("TAG", "openWhatsappContact: ");
        Uri uri = Uri.parse("smsto:" + number);
        Intent i = new Intent(Intent.ACTION_SENDTO, uri);
        i.setPackage("com.whatsapp");
        startActivity(Intent.createChooser(i, ""));
    }
}