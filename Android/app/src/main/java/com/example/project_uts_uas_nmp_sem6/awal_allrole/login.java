package com.example.project_uts_uas_nmp_sem6.awal_allrole;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.project_uts_uas_nmp_sem6.Configurasi.Configurasi;
import com.example.project_uts_uas_nmp_sem6.tampilan_admin.MainActivity;
import com.example.project_uts_uas_nmp_sem6.R;
import com.example.project_uts_uas_nmp_sem6.tampilan_user.MainActivity_user;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {

    private Button btnLogin;
    private EditText inputEmail, inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView registerButton = findViewById(R.id.daftar);
        btnLogin = findViewById(R.id.btn_login);
        inputEmail = findViewById(R.id.input_email);
        inputPassword = findViewById(R.id.input_password);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this, register.class);
                startActivity(intent);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(login.this, MainActivity.class);
//                startActivity(intent);
                login();
            }
        });
    }
    private  void login(){
            final String email = inputEmail.getText().toString().trim();
            final String password = inputPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(login.this, "Mohon Masukkan Email dan Password", Toast.LENGTH_SHORT).show();
                return;
            }

            String url = new Configurasi().baseUrl() + "login.php";
            StringRequest request = new StringRequest(
                    Request.Method.POST,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                if (success) {
                                    String role = jsonObject.getString("role");
                                    if (role.equals("User")) {
                                        Intent intent = new Intent(login.this, MainActivity_user.class);
                                        intent.putExtra("email",email);
                                        startActivity(intent);
                                        finish();
                                    } else if (role.equals("Admin")) {
                                        Intent intent = new Intent(login.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                } else {
                                    String message = jsonObject.getString("message");
                                    Toast.makeText(login.this, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                Log.e("login", "Gagal Mengambil data JSON: " + e.getMessage());
                                Toast.makeText(login.this, "Gagal Mengambil data JSON", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Log.e("login", "Volley error: " + volleyError.getMessage());
                            Toast.makeText(login.this, "Error Volley", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", email);
                    params.put("password", password);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request);
    }
}



