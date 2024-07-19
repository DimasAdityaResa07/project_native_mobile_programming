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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.project_uts_uas_nmp_sem6.Configurasi.Configurasi;
import com.example.project_uts_uas_nmp_sem6.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class simpan_data_penerimaan_laporan extends AppCompatActivity {

    private EditText viewNoIdentitasPelapor, uploadLokasiKejadian, uploadDeskripsiKejadian, uploadNamaPelapor, uploadNoIdentitas;
    Spinner nama_pelapor,kategori,uploadStatusLaporan;

    ArrayList<String> idPelaporList = new ArrayList<>();
    ArrayList<String> namaPelaporList = new ArrayList<>();

    ArrayList<String> idKategoriList = new ArrayList<>();
    ArrayList<String> namaKategoriList = new ArrayList<>();
    RequestQueue requestQueue;
    private Button saveButton, penangananButton;
    String getIdKategori,getIdPelapor;

    private Handler handler = new Handler();
    private Runnable dataReloadRunnable;
    private int lastSelectedPosition = 0; //gpt untuk menyimpan posisi terakhir sebelum auto reload

    private int lastSelectedPositionKategori = 0;
    int getIntent=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simpan_data_penerimaan_laporan);

        uploadNamaPelapor = findViewById(R.id.view_nama_pelapor);
        viewNoIdentitasPelapor = findViewById(R.id.view_no_identitas_pelapor);
        uploadLokasiKejadian = findViewById(R.id.input_lokasi);
        uploadDeskripsiKejadian=findViewById(R.id.input_deskripsi);
        uploadLokasiKejadian = findViewById(R.id.input_lokasi);
        uploadDeskripsiKejadian=findViewById(R.id.input_deskripsi);
        nama_pelapor=findViewById(R.id.input_nama_pelapor); //spinner
        kategori=findViewById(R.id.input_kategori); //spinner
        uploadStatusLaporan=findViewById(R.id.input_status_laporan); //Spinner
        saveButton = findViewById(R.id.btn_simpan);
        penangananButton = findViewById(R.id.btn_penanganan);

        requestQueue=Volley.newRequestQueue(this);

        Intent intent = getIntent();
        if (intent != null) {
            String namaPelapor = intent.getStringExtra("nama");
            String no_identitas_pelapor = intent.getStringExtra("no_identitas_pelapor");
            if (namaPelapor != null && !no_identitas_pelapor.isEmpty()) {
                nama_pelapor.setVisibility(View.GONE);
                uploadNamaPelapor.setVisibility(View.VISIBLE);
                uploadNamaPelapor.setText(namaPelapor);
                viewNoIdentitasPelapor.setText(no_identitas_pelapor);
                getIntent=1;
            }
        }

        loadDataFromServer();
        loadDataFromServerKategori();

        // Custom Adapter Spinner agar position 0 tidak dapat ditekan dan berwarna abu - gpt
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.status_laporan)) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }

            @Override
            public boolean isEnabled(int position) {
                return position != 0; // Disable the first item
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        uploadStatusLaporan.setAdapter(adapter);


        nama_pelapor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position != 0) {
                    lastSelectedPosition = position; //gpt
//                    String item = adapterView.getItemAtPosition(position).toString();
                    getIdPelapor = idPelaporList.get(position - 1);
                    viewNoIdentitasPelapor.setText(getIdPelapor);
//                    Toast.makeText(simpan_data_penerimaan_laporan.this, getIdPelapor, Toast.LENGTH_SHORT).show();
                }else{
                    lastSelectedPosition = 0;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        kategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position != 0) {
                    lastSelectedPositionKategori = position; //gpt
//                    String item = adapterView.getItemAtPosition(position).toString();
                      getIdKategori = idKategoriList.get(position - 1);
//                    Toast.makeText(simpan_data_penerimaan_laporan.this, getIdKategori, Toast.LENGTH_SHORT).show();
                }else{
                    lastSelectedPositionKategori = 0;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getIntent==0) {
                    simpanData();
                }else{
                    simpanDataIntent();
                }
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
                            ArrayList<String> newIdPelaporList = new ArrayList<>(); //gpt
                            ArrayList<String> newNamaPelaporList = new ArrayList<>(); //gpt
                            JSONArray jsonArray = response.getJSONArray("data");
                            namaPelaporList.add("-- Pilih --");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                newIdPelaporList.add(jsonObject.optString("no_identitas_pelapor"));
                                newNamaPelaporList.add(jsonObject.optString("nama_pelapor"));
                            }
                            if (!idPelaporList.equals(newIdPelaporList) || !namaPelaporList.equals(newNamaPelaporList)) {
                                idPelaporList.clear();
                                namaPelaporList.clear();
                                namaPelaporList.add("-- Pilih --");
                                idPelaporList.addAll(newIdPelaporList);
                                namaPelaporList.addAll(newNamaPelaporList);

                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(simpan_data_penerimaan_laporan.this,
                                        android.R.layout.simple_spinner_item, namaPelaporList) {
                                    @Override
                                    public View getDropDownView(int position, View convertView, ViewGroup parent) {
                                        View view = super.getDropDownView(position, convertView, parent);
                                        TextView tv = (TextView) view;
                                        if (position == 0) {
                                            tv.setTextColor(Color.GRAY);
                                        } else {
                                            tv.setTextColor(Color.BLACK);
                                        }
                                        return view;
                                    }

                                    @Override
                                    public boolean isEnabled(int position) {
                                        return position != 0;
                                    }
                                };
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                nama_pelapor.setAdapter(adapter);
                                nama_pelapor.setSelection(lastSelectedPosition); //gpt agar posisi tetap saat auto reload data
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
                            namaKategoriList.add("-- Pilih --");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                newIdKategoriList.add(jsonObject.optString("id_kategori"));
                                newNamaKategoriList.add(jsonObject.optString("nama_kategori"));
                            }
                            if (!idKategoriList.equals(newIdKategoriList) || !namaKategoriList.equals(newNamaKategoriList)) {
                                idKategoriList.clear();
                                namaKategoriList.clear();
                                namaKategoriList.add("-- Pilih --");
                                idKategoriList.addAll(newIdKategoriList);
                                namaKategoriList.addAll(newNamaKategoriList);

                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(simpan_data_penerimaan_laporan.this,
                                        android.R.layout.simple_spinner_item, namaKategoriList) {
                                    @Override
                                    public View getDropDownView(int position, View convertView, ViewGroup parent) {
                                        View view = super.getDropDownView(position, convertView, parent);
                                        TextView tv = (TextView) view;
                                        if (position == 0) {
                                            tv.setTextColor(Color.GRAY);
                                        } else {
                                            tv.setTextColor(Color.BLACK);
                                        }
                                        return view;
                                    }

                                    @Override
                                    public boolean isEnabled(int position) {
                                        return position != 0;
                                    }
                                };
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                kategori.setAdapter(adapter);
                                kategori.setSelection(lastSelectedPositionKategori); //gpt agar posisi tetap saat auto reload data
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


        private void simpanData() {
        final String idPelapor = getIdPelapor;
        final String lokasiKejadian = uploadLokasiKejadian.getText().toString().trim();
        final String idKategori = getIdKategori;
        final String deskripsiKejadian = uploadDeskripsiKejadian.getText().toString().trim();
        final String statusLaporan = String.valueOf(uploadStatusLaporan.getSelectedItem()).trim();

        if (idPelapor == null || lokasiKejadian.isEmpty() || idKategori == null || deskripsiKejadian.isEmpty() || statusLaporan.isEmpty()) {
            Toast.makeText(simpan_data_penerimaan_laporan.this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }
        if(statusLaporan.equals("-- Pilih --")){
            Toast.makeText(simpan_data_penerimaan_laporan.this, "Harap Pilih Laporan", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = new Configurasi().baseUrl() + "penerimaan_laporan/simpan_data_penerimaan_laporan.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Tangani respons dari server
                        Log.d("SimpanDataLaporan", "Response: " + response);
                        Toast.makeText(simpan_data_penerimaan_laporan.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                        sendRefreshFlag(true);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("SimpanDataKendaraan", "Error: " + error.getMessage());
                        Toast.makeText(simpan_data_penerimaan_laporan.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("no_identitas_pelapor", idPelapor);
                params.put("lokasi_kejadian", lokasiKejadian);
                params.put("id_kategori", idKategori);
                params.put("deskripsi_kejadian", deskripsiKejadian);
                params.put("status_laporan", statusLaporan);

                Log.d("simpan_data_kendaraan", "Params: " + params.toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

        private void simpanDataIntent() {
        final String idPelapor = viewNoIdentitasPelapor.getText().toString().trim();
        final String lokasiKejadian = uploadLokasiKejadian.getText().toString().trim();
        final String idKategori = getIdKategori;
        final String deskripsiKejadian = uploadDeskripsiKejadian.getText().toString().trim();
        final String statusLaporan = String.valueOf(uploadStatusLaporan.getSelectedItem()).trim();

        if (idPelapor.isEmpty() || lokasiKejadian.isEmpty() || idKategori == null || deskripsiKejadian.isEmpty() || statusLaporan.isEmpty()) {
            Toast.makeText(simpan_data_penerimaan_laporan.this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }
        if(statusLaporan.equals("-- Pilih --")){
            Toast.makeText(simpan_data_penerimaan_laporan.this, "Harap Pilih Laporan", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = new Configurasi().baseUrl() + "penerimaan_laporan/simpan_data_penerimaan_laporan.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Tangani respons dari server
                        Log.d("SimpanDataLaporan", "Response: " + response);
                        Toast.makeText(simpan_data_penerimaan_laporan.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                        sendRefreshFlag(true);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("SimpanDataKendaraan", "Error: " + error.getMessage());
                        Toast.makeText(simpan_data_penerimaan_laporan.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("no_identitas_pelapor", idPelapor);
                params.put("lokasi_kejadian", lokasiKejadian);
                params.put("id_kategori", idKategori);
                params.put("deskripsi_kejadian", deskripsiKejadian);
                params.put("status_laporan", statusLaporan);

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
