package com.example.project_uts_uas_nmp_sem6.tampilan_user;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class edit_profile_user extends AppCompatActivity {

    private EditText editNama, editAlamat, editNomorHp, editPassword,editEmail;
    private Button updateButton;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_user);

        editNama = findViewById(R.id.input_nama);
        editAlamat = findViewById(R.id.input_alamat);
        editNomorHp = findViewById(R.id.input_no_hp);
        editPassword = findViewById(R.id.input_password);
        editEmail=findViewById(R.id.input_email);
        updateButton = findViewById(R.id.btn_edit);

        // Mendapatkan data dari intent
        Intent intent = getIntent();
        if (intent != null) {
            email = intent.getStringExtra("email");
            editEmail.setText(email);
            loadData();
        }else{
            Toast.makeText(this, "Email Tidak Ditemukan", Toast.LENGTH_SHORT).show();
            finish();
        }
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });
    }

    private void loadData() {
        if (email == null || email.isEmpty()) {
            Toast.makeText(this, "Email Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = new Configurasi().baseUrl() + "get_data_user.php";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            if (jsonArray.length() > 0) {
                                JSONObject userData = jsonArray.getJSONObject(0);
                                editNama.setText(userData.getString("nama"));
                                editAlamat.setText(userData.getString("alamat"));
                                editNomorHp.setText(userData.getString("nomor_hp"));
                                editPassword.setText(userData.getString("password"));
                            }
                        } catch (JSONException e) {
                            Log.e("ModifyDataActivity", "Gagal Mengambil data JSON" + e.getMessage());
                            Toast.makeText(edit_profile_user.this, "Gagal Mengambil data JSON", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e("ModifyDataActivity", "Volley error: " + volleyError.getMessage());
                        Toast.makeText(edit_profile_user.this, "Volley Error", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void updateData() {
        final String nama = editNama.getText().toString().trim();
        final String alamat = editAlamat.getText().toString().trim();
        final String nomorHp = editNomorHp.getText().toString().trim();
        final String password = editPassword.getText().toString().trim();

        if (nama.isEmpty() || alamat.isEmpty() || nomorHp.isEmpty() || password.isEmpty()) {
            Toast.makeText(edit_profile_user.this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = new Configurasi().baseUrl() + "update_data_user.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("edit_data_user", "Response: " + response);
                        Toast.makeText(edit_profile_user.this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show();
                        sendRefreshFlag(true);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("edit_data_user", "Error: " + error.getMessage());
                        Toast.makeText(edit_profile_user.this, "Gagal memperbarui data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("nama", nama);
                params.put("alamat", alamat);
                params.put("nomor_hp", nomorHp);
                params.put("password", password);
                params.put("role", "User");
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
