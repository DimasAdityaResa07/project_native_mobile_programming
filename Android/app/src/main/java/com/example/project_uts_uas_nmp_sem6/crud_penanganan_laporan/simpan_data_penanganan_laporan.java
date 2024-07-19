package com.example.project_uts_uas_nmp_sem6.crud_penanganan_laporan;

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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.project_uts_uas_nmp_sem6.Configurasi.Configurasi;
import com.example.project_uts_uas_nmp_sem6.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class simpan_data_penanganan_laporan extends AppCompatActivity {

    private EditText viewLokasiLaporan, uploadDeskripsiPenanganan, uploadCatatanPenanganan, viewidLaporan;
    Spinner id_laporan,no_plat,nama_petugas;

    ArrayList<String> idLaporanList = new ArrayList<>();
    ArrayList<String> lokasiLaporanList = new ArrayList<>();

    ArrayList<String> noPlatLaporanList = new ArrayList<>();
    ArrayList<String> idPetugasList = new ArrayList<>();
    ArrayList<String> namaPetugasList = new ArrayList<>();
    RequestQueue requestQueue;
    private Button saveButton;
    String getLokasiLaporan,getNoPlat, getIdPetugas, getIdLaporan, getIdLaporanIntent, getLokasiLaporanIntent;


    private Handler handler = new Handler();
    private Runnable dataReloadRunnable;
    private int lastSelectedPosition = 0; //gpt untuk menyimpan posisi terakhir sebelum auto reload

    private int lastSelectedPositionNoPlat = 0;
    private int lastSelectedPositionNamaPetugas = 0;

    private int getIntent=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simpan_data_penanganan_laporan);

        viewLokasiLaporan = findViewById(R.id.view_lokasi_laporan);
        uploadDeskripsiPenanganan = findViewById(R.id.input_deskripsi);
        uploadCatatanPenanganan=findViewById(R.id.input_catatan);
        viewidLaporan=findViewById(R.id.view_id_laporan);
        id_laporan = findViewById(R.id.input_id_laporan); //spinner
        no_plat=findViewById(R.id.input_no_plat);//spinner
        nama_petugas=findViewById(R.id.input_nama_petugas);//spinner
        saveButton = findViewById(R.id.btn_simpan);

        requestQueue=Volley.newRequestQueue(this);

        Intent intent = getIntent();
        if (intent != null) {
            String idLaporanIntent = intent.getStringExtra("id_laporan");
            String lokasiLaporanIntent = intent.getStringExtra("lokasi_kejadian");
            if (idLaporanIntent != null && !idLaporanIntent.isEmpty()) {
                id_laporan.setVisibility(View.GONE);
                viewidLaporan.setVisibility(View.VISIBLE);
                viewidLaporan.setText(idLaporanIntent);
                viewLokasiLaporan.setText(lokasiLaporanIntent);
                getIntent=1;
            }
        }

        loadDataFromServer();
        loadDataFromServerNoPlat();
        loadDataFromServerNamaPetugas();



        id_laporan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position != 0) {
                    lastSelectedPosition = position; //gpt
                    getIdLaporan = idLaporanList.get(position);
                    viewLokasiLaporan.setText(lokasiLaporanList.get(position-1));
//                    Toast.makeText(simpan_data_penanganan_laporan.this, getIdLaporan, Toast.LENGTH_SHORT).show();
                }else{
                    lastSelectedPosition = 0;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        no_plat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position != 0) {
                    lastSelectedPositionNoPlat = position; //gpt
//                    String item = adapterView.getItemAtPosition(position).toString();
                      getNoPlat = noPlatLaporanList.get(position);
//                    Toast.makeText(simpan_data_penanganan_laporan.this, getNoPlat, Toast.LENGTH_SHORT).show();
                }else{
                    lastSelectedPositionNoPlat= 0;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        nama_petugas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position != 0) {
                    lastSelectedPositionNamaPetugas = position; //gpt
//                    String item = adapterView.getItemAtPosition(position).toString();
                      getIdPetugas = idPetugasList.get(position - 1);
//                    Toast.makeText(simpan_data_penanganan_laporan.this, getIdPetugas, Toast.LENGTH_SHORT).show();
                }else{
                    lastSelectedPositionNamaPetugas= 0;
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
                loadDataFromServerNoPlat();
                loadDataFromServerNamaPetugas();
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


    private void loadDataFromServerNamaPetugas() {
        String url = new Configurasi().baseUrl() + "petugas/tampil_data_petugas.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ArrayList<String> newNoIdentitasPetugas = new ArrayList<>(); //gpt
                            ArrayList<String> newNamaPetugas = new ArrayList<>(); //gpt
                            JSONArray jsonArray = response.getJSONArray("data");
                            namaPetugasList.add("-- Pilih --");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                newNoIdentitasPetugas.add(jsonObject.optString("noidentitas_petugas"));
                                newNamaPetugas.add(jsonObject.optString("nama_petugas"));
                            }
                            if (!idPetugasList.equals(newNoIdentitasPetugas) || !namaPetugasList.equals(newNamaPetugas)) {
                                idPetugasList.clear();
                                namaPetugasList.clear();
                                namaPetugasList.add("-- Pilih --");
                                idPetugasList.addAll(newNoIdentitasPetugas);
                                namaPetugasList.addAll(newNamaPetugas);

                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(simpan_data_penanganan_laporan.this,
                                        android.R.layout.simple_spinner_item, namaPetugasList) {
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
                                nama_petugas.setAdapter(adapter);
                                nama_petugas.setSelection(lastSelectedPositionNamaPetugas); //gpt agar posisi tetap saat auto reload data
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

    private void loadDataFromServer() {
        String url = new Configurasi().baseUrl() + "penerimaan_laporan/tampil_data_penerimaan_laporan.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ArrayList<String> newIdLaporanList = new ArrayList<>(); //gpt
                            ArrayList<String> newLokasiLaporanList = new ArrayList<>(); //gpt
                            JSONArray jsonArray = response.getJSONArray("data");
                            idLaporanList.add("-- Pilih --");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                newIdLaporanList.add(jsonObject.optString("id_laporan"));
                                newLokasiLaporanList.add(jsonObject.optString("lokasi_kejadian"));
                            }
                            if (!idLaporanList.equals(newIdLaporanList) || !lokasiLaporanList.equals(newLokasiLaporanList)) {
                                idLaporanList.clear();
                                lokasiLaporanList.clear();
                                idLaporanList.add("-- Pilih --");
                                idLaporanList.addAll(newIdLaporanList);
                                lokasiLaporanList.addAll(newLokasiLaporanList);

                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(simpan_data_penanganan_laporan.this,
                                        android.R.layout.simple_spinner_item, idLaporanList) {
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
                                id_laporan.setAdapter(adapter);
                                id_laporan.setSelection(lastSelectedPosition); //gpt agar posisi tetap saat auto reload data
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


    private void loadDataFromServerNoPlat() {
        String url = new Configurasi().baseUrl() + "kendaraan/tampil_data_kendaraan.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ArrayList<String> newNoPlatList = new ArrayList<>(); //gpt
                            JSONArray jsonArray = response.getJSONArray("data");
                            noPlatLaporanList.add("-- Pilih --");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                newNoPlatList.add(jsonObject.optString("no_plat"));
                            }
                            if (!noPlatLaporanList.equals(newNoPlatList)) {
                                noPlatLaporanList.clear();
                                noPlatLaporanList.add("-- Pilih --");
                                noPlatLaporanList.addAll(newNoPlatList);

                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(simpan_data_penanganan_laporan.this,
                                        android.R.layout.simple_spinner_item, noPlatLaporanList) {
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
                                no_plat.setAdapter(adapter);
                                no_plat.setSelection(lastSelectedPositionNoPlat); //gpt agar posisi tetap saat auto reload data
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
        final String id_laporan = getIdLaporan;
        final String no_plat = getNoPlat;
        final String noidentitas_petugas = getIdPetugas;
        final String deskripsiKejadian = uploadDeskripsiPenanganan.getText().toString().trim();
        final String catatan_tindak_lanjut = uploadCatatanPenanganan.getText().toString().trim();

        if (id_laporan == null|| no_plat == null || noidentitas_petugas == null|| deskripsiKejadian.isEmpty() || catatan_tindak_lanjut.isEmpty()) {
            Toast.makeText(simpan_data_penanganan_laporan.this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = new Configurasi().baseUrl() + "penanganan_laporan/simpan_data_penanganan_laporan.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Tangani respons dari server
                        Log.d("SimpanDataLaporan", "Response: " + response);
                        Toast.makeText(simpan_data_penanganan_laporan.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                        sendRefreshFlag(true);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("SimpanDataKendaraan", "Error: " + error.getMessage());
                        Toast.makeText(simpan_data_penanganan_laporan.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_laporan", id_laporan);
                params.put("no_plat", no_plat);
                params.put("noidentitas_petugas", noidentitas_petugas);
                params.put("deskripsi_penanganan", deskripsiKejadian);
                params.put("catatan_tindak_lanjut", catatan_tindak_lanjut);

                Log.d("simpan_data_kendaraan", "Params: " + params.toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

         private void simpanDataIntent() {
        final String id_laporan = viewidLaporan.getText().toString().trim();
        final String no_plat = getNoPlat;
        final String noidentitas_petugas = getIdPetugas;
        final String deskripsiKejadian = uploadDeskripsiPenanganan.getText().toString().trim();
        final String catatan_tindak_lanjut = uploadCatatanPenanganan.getText().toString().trim();

        if (id_laporan.isEmpty()|| no_plat == null || noidentitas_petugas == null|| deskripsiKejadian.isEmpty() || catatan_tindak_lanjut.isEmpty()) {
            Toast.makeText(simpan_data_penanganan_laporan.this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = new Configurasi().baseUrl() + "penanganan_laporan/simpan_data_penanganan_laporan.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Tangani respons dari server
                        Log.d("SimpanDataLaporan", "Response: " + response);
                        Toast.makeText(simpan_data_penanganan_laporan.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                        sendRefreshFlag(true);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("SimpanDataKendaraan", "Error: " + error.getMessage());
                        Toast.makeText(simpan_data_penanganan_laporan.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_laporan", id_laporan);
                params.put("no_plat", no_plat);
                params.put("noidentitas_petugas", noidentitas_petugas);
                params.put("deskripsi_penanganan", deskripsiKejadian);
                params.put("catatan_tindak_lanjut", catatan_tindak_lanjut);

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
