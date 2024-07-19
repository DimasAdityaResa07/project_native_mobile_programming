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

public class MainActivity extends AppCompatActivity {
FrameLayout frameDaftarUser,framePenerimaanLaporan,framePenangananLaporan, frameLainnya;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        frameDaftarUser=findViewById(R.id.frame_datauser);
        framePenerimaanLaporan=findViewById(R.id.frame_penerimaan_laporan);
        framePenangananLaporan=findViewById(R.id.frame_penanganan_laporan);
        frameLainnya=findViewById(R.id.frame_lainnya);

        frameDaftarUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(MainActivity.this,"Daftar User",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, daftar_pelapor_admin.class);
                startActivity(intent);
            }
        });

        framePenerimaanLaporan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, daftar_penerimaan_laporan_admin.class);
                startActivity(intent);
            }
        });

        framePenangananLaporan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, daftar_penanganan_laporan_admin.class);
                startActivity(intent);
            }
        });

        frameLainnya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainActivity_lanjutan.class);
                startActivity(intent);
            }
        });



    }
}