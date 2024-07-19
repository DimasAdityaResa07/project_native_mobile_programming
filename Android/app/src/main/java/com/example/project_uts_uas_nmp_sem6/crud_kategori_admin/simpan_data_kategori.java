package com.example.project_uts_uas_nmp_sem6.crud_kategori_admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.project_uts_uas_nmp_sem6.Configurasi.Configurasi;
import com.example.project_uts_uas_nmp_sem6.R;

import java.util.HashMap;
import java.util.Map;

public class simpan_data_kategori extends AppCompatActivity {

    private EditText uploadNamaKategori,uploadKeterangan;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simpan_data_kategori);

        uploadNamaKategori = findViewById(R.id.input_nama_kategori);
        uploadKeterangan = findViewById(R.id.input_keterangan);

        saveButton = findViewById(R.id.btn_simpan);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpanData();
            }
        });
    }

    private void simpanData() {
        final String namaKategori = uploadNamaKategori.getText().toString().trim();
        final String keterangan = uploadKeterangan.getText().toString().trim();

        if (namaKategori.isEmpty() || keterangan.isEmpty()) {
            Toast.makeText(simpan_data_kategori.this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = new Configurasi().baseUrl() + "kategori/simpan_data_kategori.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("SimpanData", "Response: " + response);
                        Toast.makeText(simpan_data_kategori.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                        sendRefreshFlag(true);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("SimpanData", "Error: " + error.getMessage());
                        Toast.makeText(simpan_data_kategori.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nama_kategori", namaKategori);
                params.put("keterangan", keterangan);

                Log.d("simpan_data_user", "Params: " + params.toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void sendRefreshFlag(boolean refresh) {
        Intent intent = new Intent();
        intent.putExtra("refreshflag", refresh);
        setResult(RESULT_OK, intent);
    }
}
