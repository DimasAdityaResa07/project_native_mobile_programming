package com.example.project_uts_uas_nmp_sem6.crud_kategori_admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
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

public class edit_data_kategori extends AppCompatActivity {

    private EditText editNamaKategori, editKeterangan;
    private Button updateButton;
    private String id_kategori;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data_kategori);

        editNamaKategori = findViewById(R.id.input_nama_kategori);
        editKeterangan = findViewById(R.id.input_keterangan);
        updateButton = findViewById(R.id.btn_edit);

        Intent intent = getIntent();
        if (intent != null) {
            id_kategori= intent.getStringExtra("id_kategori");
            editNamaKategori.setText(intent.getStringExtra("nama_kategori"));
            editKeterangan.setText(intent.getStringExtra("keterangan"));
        }
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });
    }

    private void updateData() {
        final String namaKategori = editNamaKategori.getText().toString().trim();
        final String keterangan = editKeterangan.getText().toString().trim();

        if (namaKategori.isEmpty() || keterangan.isEmpty()) {
            Toast.makeText(edit_data_kategori.this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = new Configurasi().baseUrl() + "kategori/edit_data_kategori.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("edit_data_user", "Response: " + response);
                        Toast.makeText(edit_data_kategori.this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show();
                        sendRefreshFlag(true);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("edit_data_user", "Error: " + error.getMessage());
                        Toast.makeText(edit_data_kategori.this, "Gagal memperbarui data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Buat parameter untuk request
                Map<String, String> params = new HashMap<>();
                params.put("id_kategori", id_kategori);
                params.put("nama_kategori", namaKategori);
                params.put("keterangan", keterangan);
                Log.d("edit_data_user", "Params: " + params.toString());
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
