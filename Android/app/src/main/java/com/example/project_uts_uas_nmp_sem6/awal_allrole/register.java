package com.example.project_uts_uas_nmp_sem6.awal_allrole;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class register extends AppCompatActivity {

    private EditText uploadEmail,uploadNama, uploadAlamat, uploadNomorHp, uploadPassword;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        uploadNama = findViewById(R.id.input_nama);
        uploadAlamat = findViewById(R.id.input_alamat);
        uploadNomorHp = findViewById(R.id.input_no_hp);
        uploadPassword = findViewById(R.id.input_password);
        uploadEmail= findViewById(R.id.input_email);
        saveButton = findViewById(R.id.btn_daftar);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cekEmail();
            }

            private void cekEmail() {
                final String email = uploadEmail.getText().toString().trim();

                if (email.isEmpty()) {
                    Toast.makeText(register.this, "Harap isi email", Toast.LENGTH_SHORT).show();
                    return;
                }

                String url = new Configurasi().baseUrl() + "cek_email.php";

                StringRequest request = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.equals("email_sudah_ada")) {
                                    Toast.makeText(register.this, "Email sudah terdaftar", Toast.LENGTH_SHORT).show();
                                } else {
                                    simpanData();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("CekEmail", "Error: " + error.getMessage());
                                Toast.makeText(register.this, "Gagal memeriksa email", Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("email", email);
                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(request);
            }
        });
    }

    private void simpanData() {
        final String nama = uploadNama.getText().toString().trim();
        final String alamat = uploadAlamat.getText().toString().trim();
        final String nomorHp = uploadNomorHp.getText().toString().trim();
        final String email = uploadEmail.getText().toString().trim();
        final String password = uploadPassword.getText().toString().trim();

        if (nama.isEmpty() || alamat.isEmpty() || nomorHp.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(register.this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = new Configurasi().baseUrl() + "simpan_data_user.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Tangani respons dari server
                        Log.d("Register", "Response: " + response);
                        Toast.makeText(register.this, "Data berhasil disimpan, Silahkan Login", Toast.LENGTH_SHORT).show();
                        sendRefreshFlag(true); // Send refresh flag to indicate data change
                        Intent intent = new Intent(register.this, login.class);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Tangani kesalahan
                        Log.e("Register", "Error: " + error.getMessage());
                        Toast.makeText(register.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nama", nama);
                params.put("alamat", alamat);
                params.put("nomor_hp", nomorHp);
                params.put("email", email);
                params.put("password", password);
                params.put("role", "User");


                Log.d("register", "Params: " + params.toString());
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
