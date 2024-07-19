package com.example.project_uts_uas_nmp_sem6.crud_kendaraan_admin;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class simpan_data_kendaraan extends AppCompatActivity {

    private EditText uploadNoPlat,uploadMerk, uploadType, uploadJenis, uploadTanggalStnk;
    Spinner uploadStatusKendaraan;
    private Button saveButton;
    private ImageView datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simpan_data_kendaraan);

        uploadNoPlat = findViewById(R.id.input_no_plat);
        uploadMerk = findViewById(R.id.input_merk);
        uploadType = findViewById(R.id.input_type);
        uploadJenis = findViewById(R.id.input_jenis);
        uploadTanggalStnk= findViewById(R.id.input_tanggal_stnk);
        uploadStatusKendaraan=findViewById(R.id.input_status_kendaraan);
        saveButton = findViewById(R.id.btn_simpan);
        datePicker = findViewById(R.id.icon_date_picker);

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }

            private void showDatePicker() {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(simpan_data_kendaraan.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String selectedDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                uploadTanggalStnk.setText(selectedDate);
                            }
                        }, year, month, dayOfMonth);

                datePickerDialog.show();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cekNoPlat();
            }

            private void cekNoPlat() {
                final String noPlat = uploadNoPlat.getText().toString();
                if(noPlat.isEmpty()){
                    Toast.makeText(simpan_data_kendaraan.this,"Harap Isi No Plat", Toast.LENGTH_SHORT).show();
                    return;
                }
                String url = new Configurasi().baseUrl()+"kendaraan/cek_plat.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("no_plat_sudah_ada")) {
                            Toast.makeText(simpan_data_kendaraan.this, "Nomor Plat sudah terdaftar", Toast.LENGTH_SHORT).show();
                        } else {
                            simpanData();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("simpandatakendaraan", "Error: " + error.getMessage());
                        Toast.makeText(simpan_data_kendaraan.this, "Gagal memeriksa No Plat", Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String,String> params= new HashMap<>();
                        params.put("no_plat", noPlat);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);
            }
        });
    }

    private void simpanData() {
        final String noPlat = uploadNoPlat.getText().toString().trim();
        final String merk = uploadMerk.getText().toString().trim();
        final String type = uploadType.getText().toString().trim();
        final String jenis = uploadJenis.getText().toString().trim();
        final String tanggal_stnk = uploadTanggalStnk.getText().toString().trim();
        final String status_kendaraan = String.valueOf(uploadStatusKendaraan.getSelectedItem()).trim();

        // Validasi input
        if (noPlat.isEmpty() || merk.isEmpty() || type.isEmpty() || jenis.isEmpty() || tanggal_stnk.isEmpty()) {
            Toast.makeText(simpan_data_kendaraan.this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }
        if(status_kendaraan.equals("-- Pilih --")){
            Toast.makeText(simpan_data_kendaraan.this, "Harap Pilih Status Kendaraan", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = new Configurasi().baseUrl() + "kendaraan/simpan_data_kendaraan.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Tangani respons dari server
                        Log.d("SimpanDataKendaraan", "Response: " + response);
                        Toast.makeText(simpan_data_kendaraan.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                        sendRefreshFlag(true);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("SimpanDataKendaraan", "Error: " + error.getMessage());
                        Toast.makeText(simpan_data_kendaraan.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("no_plat", noPlat);
                params.put("merk", merk);
                params.put("type", type);
                params.put("jenis", jenis);
                params.put("tanggal_stnk", tanggal_stnk);
                params.put("status_kendaraan", status_kendaraan);


                Log.d("simpan_data_kendaraan", "Params: " + params.toString());
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
