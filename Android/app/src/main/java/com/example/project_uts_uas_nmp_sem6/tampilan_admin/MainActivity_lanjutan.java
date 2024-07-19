package com.example.project_uts_uas_nmp_sem6.tampilan_admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project_uts_uas_nmp_sem6.R;
import com.example.project_uts_uas_nmp_sem6.crud_kategori_admin.daftar_kategori_admin;
import com.example.project_uts_uas_nmp_sem6.crud_kendaraan_admin.daftar_kendaraan_admin;
import com.example.project_uts_uas_nmp_sem6.crud_pelapor_admin.daftar_pelapor_admin;
import com.example.project_uts_uas_nmp_sem6.crud_penanganan_laporan.daftar_penanganan_laporan_admin;
import com.example.project_uts_uas_nmp_sem6.crud_penerimaan_laporan.daftar_penerimaan_laporan_admin;
import com.example.project_uts_uas_nmp_sem6.crud_petugas_admin.daftar_petugas_admin;

public class MainActivity_lanjutan extends AppCompatActivity {
FrameLayout frameDaftarKendaraan,frameDaftarPetugas, frameDatftarKategori, frameKembali;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_lainnya);
        frameDaftarKendaraan=findViewById(R.id.frame_datakendaraan);
        frameDaftarPetugas=findViewById(R.id.frame_datapetugas);
        frameDatftarKategori=findViewById(R.id.frame_datakategori);
        frameKembali=findViewById(R.id.frame_kembali);


        frameDaftarKendaraan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity_lanjutan.this, daftar_kendaraan_admin.class);
                startActivity(intent);
            }
        });
        frameDaftarPetugas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity_lanjutan.this, daftar_petugas_admin.class);
                startActivity(intent);
            }
        });

        frameDatftarKategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity_lanjutan.this, daftar_kategori_admin.class);
                startActivity(intent);
            }
        });

        frameKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity_lanjutan.this, MainActivity.class);
                startActivity(intent);
            }
        });




    }
}