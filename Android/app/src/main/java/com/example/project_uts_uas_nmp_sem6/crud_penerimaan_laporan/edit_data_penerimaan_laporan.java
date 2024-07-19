package com.example.project_uts_uas_nmp_sem6.crud_penerimaan_laporan;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.project_uts_uas_nmp_sem6.Configurasi.Configurasi;
import com.example.project_uts_uas_nmp_sem6.R;
import com.example.project_uts_uas_nmp_sem6.crud_penanganan_laporan.simpan_data_penanganan_laporan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class edit_data_penerimaan_laporan extends AppCompatActivity {

    private EditText viewNoIdentitasPelapor, editLokasi, editDeskripsi;
    Spinner nama_pelapor, kategori, editStatusLaporan;
    private Button updateButton, penangananButton;
    private String idLaporan,id_pelapor,namaPelapor,namaKategori, waktuLaporan;
    private String selectedIdPelapor;
    private String selectedIdKategori;

    ArrayList<String> idPelaporList = new ArrayList<>();
    ArrayList<String> namaPelaporList = new ArrayList<>();

    ArrayList<String> idKategoriList = new ArrayList<>();
    ArrayList<String> namaKategoriList = new ArrayList<>();

    private Map<String, Integer> idPelaporPositionMap = new HashMap<>();
    private Map<String, Integer> idKategoriPositionMap = new HashMap<>();
    RequestQueue requestQueue;
    String getIdKategori,getIdPelapor;
    private Handler handler = new Handler();
    private Runnable dataReloadRunnable;
    private int lastSelectedPosition = 0; //gpt untuk menyimpan posisi terakhir sebelum auto reload

    private int lastSelectedPositionKategori = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data_penerimaan_laporan);

        viewNoIdentitasPelapor = findViewById(R.id.view_no_identitas_pelapor);
        editLokasi = findViewById(R.id.input_lokasi);
        editDeskripsi = findViewById(R.id.input_deskripsi);
        nama_pelapor = findViewById(R.id.input_nama_pelapor); //spinner
        kategori = findViewById(R.id.input_kategori); //spinner
        editStatusLaporan=findViewById(R.id.input_status_laporan); //spinner
        updateButton = findViewById(R.id.btn_edit);
        penangananButton = findViewById(R.id.btn_penanganan);

        requestQueue=Volley.newRequestQueue(this);
        loadDataFromServer();
        loadDataFromServerKategori();

        editStatusLaporan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position==2){
                    penangananButton.setVisibility(View.VISIBLE);
                    updateButton.setVisibility(View.GONE);
                }else{
                    penangananButton.setVisibility(View.GONE);
                    updateButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        nama_pelapor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (!idPelaporList.isEmpty() && position < idPelaporList.size()) {
                    getIdPelapor = idPelaporList.get(position);
                    viewNoIdentitasPelapor.setText(getIdPelapor);
                    if (position != 0) {
                        lastSelectedPosition = position; //gpt
                    } else {
                        lastSelectedPosition = 0;
                    }
                } else {
                    viewNoIdentitasPelapor.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Tidak ada yang dipilih
            }
        });

        kategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                getIdKategori = idKategoriList.get(position);
//                Toast.makeText(edit_data_penerimaan_laporan.this, getIdKategori + idLaporan, Toast.LENGTH_SHORT).show();
                if (position != 0) {
                    lastSelectedPositionKategori = position; //gpt
//                    String item = adapterView.getItemAtPosition(position).toString();
                }else{
                    lastSelectedPositionKategori = 0;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status_laporan_edit, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editStatusLaporan.setAdapter(adapter);

        Intent intent = getIntent();
        if (intent != null) {
            idLaporan = intent.getStringExtra("id_laporan");
            id_pelapor=intent.getStringExtra("no_identitas");
            selectedIdPelapor = intent.getStringExtra("no_identitas"); // Tambahkan ini -gpt
            selectedIdKategori = intent.getStringExtra("kategori"); // Tambahkan ini -gpt
            waktuLaporan = intent.getStringExtra("waktu");
            editLokasi.setText(intent.getStringExtra("lokasi"));
            editDeskripsi.setText(intent.getStringExtra("deskripsi_kejadian"));

            String statusLaporan = intent.getStringExtra("status_laporan");
            if (statusLaporan != null) {
                int spinnerPosition = adapter.getPosition(statusLaporan);
                editStatusLaporan.setSelection(spinnerPosition);
            }
        }

        penangananButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDataPenanganan();
            }
        });



        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });
        dataReloadRunnable = new Runnable() {
            @Override
            public void run() {
                loadDataFromServer();
                loadDataFromServerKategori();
                handler.postDelayed(this, 10000);
            }
        };
        handler.post(dataReloadRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(dataReloadRunnable);
    }

    private void loadDataFromServer() {
        String url = new Configurasi().baseUrl() + "pelapor/tampil_data_pelapor.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ArrayList<String> newIdPelaporList = new ArrayList<>();
                            ArrayList<String> newNamaPelaporList = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String idPelapor = jsonObject.optString("no_identitas_pelapor");
                                String namaPelapor = jsonObject.optString("nama_pelapor");
                                newIdPelaporList.add(idPelapor);
                                newNamaPelaporList.add(namaPelapor);
                                idPelaporPositionMap.put(idPelapor, i); // Mapping ID ke posisi - gpt
                            }
                            if (!idPelaporList.equals(newIdPelaporList) || !namaPelaporList.equals(newNamaPelaporList)) {
                                idPelaporList.clear();
                                namaPelaporList.clear();
                                idPelaporList.addAll(newIdPelaporList);
                                namaPelaporList.addAll(newNamaPelaporList);

                                ArrayAdapter<String> adapter = new ArrayAdapter<>(edit_data_penerimaan_laporan.this,
                                        android.R.layout.simple_spinner_item, namaPelaporList);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                nama_pelapor.setAdapter(adapter);

                                // Set nilai awal berdasarkan id_pelapor - gpt
                                if (selectedIdPelapor != null && idPelaporPositionMap.containsKey(selectedIdPelapor)) {
                                    int position = idPelaporPositionMap.get(selectedIdPelapor);
                                    nama_pelapor.setSelection(position);
                                } else {
                                    nama_pelapor.setSelection(lastSelectedPosition);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    private void loadDataFromServerKategori() {
        String url = new Configurasi().baseUrl() + "kategori/tampil_data_kategori.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ArrayList<String> newIdKategoriList = new ArrayList<>(); //gpt
                            ArrayList<String> newNamaKategoriList = new ArrayList<>(); //gpt
                            JSONArray jsonArray = response.getJSONArray("data");
                            //gpt untuk set default spinner berdasarkan id yang didapat
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String idKategori = jsonObject.optString("id_kategori");
                                String namaKategori = jsonObject.optString("nama_kategori");
                                newIdKategoriList.add(idKategori);
                                newNamaKategoriList.add(namaKategori);
                                idKategoriPositionMap.put(idKategori, i); // Mapping ID ke posisi - gpt
                            }
                            if (!idKategoriList.equals(newIdKategoriList) || !namaKategoriList.equals(newNamaKategoriList)) {
                                idKategoriList.clear();
                                namaKategoriList.clear();
                                idKategoriList.addAll(newIdKategoriList);
                                namaKategoriList.addAll(newNamaKategoriList);

                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(edit_data_penerimaan_laporan.this,
                                        android.R.layout.simple_spinner_item, namaKategoriList);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                kategori.setAdapter(adapter);

                                // Set nilai awal berdasarkan id_kategori - gpt
                                if (selectedIdKategori != null && idKategoriPositionMap.containsKey(selectedIdKategori)) {
                                    int position = idKategoriPositionMap.get(selectedIdKategori);
                                    kategori.setSelection(position);
                                } else {
                                    kategori.setSelection(lastSelectedPositionKategori);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }


    private void updateData() {
        final String id_laporan = idLaporan;
        final String no_identitas_pelapor = getIdPelapor;
        final String lokasi_kejadian = editLokasi.getText().toString().trim();
        final String waktu_laporan = waktuLaporan;
        final String id_kategori = getIdKategori;
        final String deskripsi_kejadian = editDeskripsi.getText().toString().trim();
        final String status_laporan = String.valueOf(editStatusLaporan.getSelectedItem()).trim();


        if (id_laporan.isEmpty() || lokasi_kejadian.isEmpty() || waktu_laporan.isEmpty() || deskripsi_kejadian.isEmpty()) {
            Toast.makeText(edit_data_penerimaan_laporan.this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }


        String url = new Configurasi().baseUrl() + "penerimaan_laporan/edit_data_penerimaan_laporan.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("edit_data_kendaraan", "Response: " + response);
                        Toast.makeText(edit_data_penerimaan_laporan.this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show();
                        sendRefreshFlag(true);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("edit_data_kendaraan", "Error: " + error.getMessage());
                        Toast.makeText(edit_data_penerimaan_laporan.this, "Gagal memperbarui data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_laporan", id_laporan);
                params.put("no_identitas_pelapor", no_identitas_pelapor);
                params.put("lokasi_kejadian", lokasi_kejadian);
                params.put("waktu_laporan", waktu_laporan);
                params.put("id_kategori", id_kategori);
                params.put("deskripsi_kejadian", deskripsi_kejadian);
                params.put("status_laporan", status_laporan);
                Log.d("edit_data_kendaraan", "Params: " + params.toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void updateDataPenanganan() {
        final String id_laporan = idLaporan;
        final String no_identitas_pelapor = getIdPelapor;
        final String lokasi_kejadian = editLokasi.getText().toString().trim();
        final String waktu_laporan = waktuLaporan;
        final String id_kategori = getIdKategori;
        final String deskripsi_kejadian = editDeskripsi.getText().toString().trim();
        final String status_laporan = String.valueOf(editStatusLaporan.getSelectedItem()).trim();


        if (id_laporan.isEmpty() || lokasi_kejadian.isEmpty() || waktu_laporan.isEmpty() || deskripsi_kejadian.isEmpty()) {
            Toast.makeText(edit_data_penerimaan_laporan.this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }


        String url = new Configurasi().baseUrl() + "penerimaan_laporan/edit_data_penerimaan_laporan.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("edit_data_kendaraan", "Response: " + response);
                        Toast.makeText(edit_data_penerimaan_laporan.this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show();
                        sendRefreshFlag(true);

                        Intent intent = new Intent(edit_data_penerimaan_laporan.this, simpan_data_penanganan_laporan.class);
                        intent.putExtra("id_laporan", id_laporan);
                        intent.putExtra("lokasi_kejadian", lokasi_kejadian);
                        startActivity(intent);
//                        Toast.makeText(edit_data_penerimaan_laporan.this,id_laporan,Toast.LENGTH_SHORT).show();
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("edit_data_kendaraan", "Error: " + error.getMessage());
                        Toast.makeText(edit_data_penerimaan_laporan.this, "Gagal memperbarui data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_laporan", id_laporan);
                params.put("no_identitas_pelapor", no_identitas_pelapor);
                params.put("lokasi_kejadian", lokasi_kejadian);
                params.put("waktu_laporan", waktu_laporan);
                params.put("id_kategori", id_kategori);
                params.put("deskripsi_kejadian", deskripsi_kejadian);
                params.put("status_laporan", "Sedang Diproses");
                Log.d("edit_data_kendaraan", "Params: " + params.toString());
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
