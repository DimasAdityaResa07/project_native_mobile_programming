package com.example.project_uts_uas_nmp_sem6.crud_pelapor_admin;

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

public class simpan_data_pelapor extends AppCompatActivity {

    private EditText uploadNoIdentitas,uploadNama ,uploadNomorHp, uploadAlamat, uploadEmail;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simpan_data_pelapor);

        uploadNoIdentitas = findViewById(R.id.input_no_identitas);
        uploadNama = findViewById(R.id.input_nama);
        uploadNomorHp = findViewById(R.id.input_no_hp);
        uploadAlamat = findViewById(R.id.input_alamat);
        uploadEmail = findViewById(R.id.input_email);
        saveButton = findViewById(R.id.btn_simpan);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cekNoIdentitas();
            }

            private void cekNoIdentitas() {
                final String noIdentitasCek = uploadNoIdentitas.getText().toString().trim();
                if (noIdentitasCek.isEmpty()) {
                    Toast.makeText(simpan_data_pelapor.this, "Harap isi No Identitas", Toast.LENGTH_SHORT).show();
                    return;
                }

                String url = new Configurasi().baseUrl() + "pelapor/cek_no_identitas.php";
                StringRequest request = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.equals("no_identitas_sudah_ada")) {
                                    Toast.makeText(simpan_data_pelapor.this, "No Identitas sudah terdaftar", Toast.LENGTH_SHORT).show();
                                } else {
                                    simpanData();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("CekNoIdentitas", "Error: " + error.getMessage());
                                Toast.makeText(simpan_data_pelapor.this, "Gagal memeriksa No Identitas", Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("no_identitas_pelapor", noIdentitasCek);
                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(request);
            }
        });
    }

    private void simpanData() {
        final String noIdentitas = uploadNoIdentitas.getText().toString().trim();
        final String nama = uploadNama.getText().toString().trim();
        final String nomorHp = uploadNomorHp.getText().toString().trim();
        final String alamat = uploadAlamat.getText().toString().trim();
        final String email = uploadEmail.getText().toString().trim();

        if (noIdentitas.isEmpty() || nama.isEmpty() || alamat.isEmpty() || nomorHp.isEmpty() || email.isEmpty()) {
            Toast.makeText(simpan_data_pelapor.this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = new Configurasi().baseUrl() + "pelapor/simpan_data_pelapor.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("SimpanData", "Response: " + response);
                        Toast.makeText(simpan_data_pelapor.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                        sendRefreshFlag(true);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("SimpanData", "Error: " + error.getMessage());
                        Toast.makeText(simpan_data_pelapor.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("no_identitas_pelapor", noIdentitas);
                params.put("nama_pelapor", nama);
                params.put("no_hp_pelapor", nomorHp);
                params.put("alamat", alamat);
                params.put("email", email);


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
