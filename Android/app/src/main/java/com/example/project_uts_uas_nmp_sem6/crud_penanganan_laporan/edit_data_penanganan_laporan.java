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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class edit_data_penanganan_laporan extends AppCompatActivity {

    private EditText viewLokasiLaporan, editDeskripsiPenanganan, editCatatanPenanganan;
    Spinner id_laporan,no_plat,nama_petugas;

    ArrayList<String> idLaporanList = new ArrayList<>();
    ArrayList<String> lokasiLaporanList = new ArrayList<>();

    ArrayList<String> noPlatLaporanList = new ArrayList<>();
    ArrayList<String> idPetugasList = new ArrayList<>();
    ArrayList<String> namaPetugasList = new ArrayList<>();

    private Map<String, Integer> idLaporanPositionMap = new HashMap<>();
    private Map<String, Integer> idPlatNoPositionMap = new HashMap<>();
    private Map<String, Integer> idPetugasPositionMap = new HashMap<>();

    RequestQueue requestQueue;
    private Button updateButton;
    String getLokasiLaporan,NoPlat, idPenanganan,Idlaporan, IdPetugas, GetIdPetugas, GetIdLaporan, GetPlat;


    private Handler handler = new Handler();
    private Runnable dataReloadRunnable;
    private int lastSelectedPosition = 0; //gpt untuk menyimpan posisi terakhir sebelum auto reload

    private int lastSelectedPositionNoPlat = 0;
    private int lastSelectedPositionNamaPetugas = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data_penanganan_laporan);

        viewLokasiLaporan = findViewById(R.id.view_lokasi_laporan);
        editDeskripsiPenanganan = findViewById(R.id.input_deskripsi);
        editCatatanPenanganan=findViewById(R.id.input_catatan);
        id_laporan = findViewById(R.id.input_id_laporan); //spinner
        no_plat=findViewById(R.id.input_no_plat);//spinner
        nama_petugas=findViewById(R.id.input_nama_petugas);//spinner
        updateButton = findViewById(R.id.btn_edit);

        requestQueue=Volley.newRequestQueue(this);
        loadDataFromServer();
        loadDataFromServerNoPlat();
        loadDataFromServerNamaPetugas();

        id_laporan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (!idLaporanList.isEmpty() && position < idLaporanList.size()) {
                    getLokasiLaporan = lokasiLaporanList.get(position);
                    viewLokasiLaporan.setText(getLokasiLaporan);
                    GetIdLaporan = idLaporanList.get(position);
//                    Toast.makeText(edit_data_penanganan_laporan.this, GetIdLaporan, Toast.LENGTH_SHORT).show();

                    if (position != 0) {
                        lastSelectedPosition = position; //gpt
                    } else {
                        lastSelectedPosition = 0;
                    }
                } else {
                    viewLokasiLaporan.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Tidak ada yang dipilih
            }
        });

        no_plat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                GetPlat = noPlatLaporanList.get(position);
//                Toast.makeText(edit_data_penanganan_laporan.this, GetPlat, Toast.LENGTH_SHORT).show();
                if (position != 0) {
                    lastSelectedPositionNoPlat = position; //gpt
//                    String item = adapterView.getItemAtPosition(position).toString();
                }else{
                    lastSelectedPositionNoPlat = 0;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        nama_petugas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                    GetIdPetugas = idPetugasList.get(position);
                if (position != 0) {
                    lastSelectedPositionNamaPetugas = position; //gpt
//                    String item = adapterView.getItemAtPosition(position).toString();
//                    Toast.makeText(edit_data_penanganan_laporan.this, GetIdPetugas, Toast.LENGTH_SHORT).show();
                }else{
                    lastSelectedPositionNamaPetugas= 0;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            idPenanganan = intent.getStringExtra("id_penanganan");
            Idlaporan=intent.getStringExtra("id_laporan");
            NoPlat = intent.getStringExtra("no_plat"); // Tambahkan ini -gpt
            IdPetugas= intent.getStringExtra("noidentitas_petugas"); // Tambahkan ini -gpt
            editDeskripsiPenanganan.setText(intent.getStringExtra("deskripsi_penanganan"));
            editCatatanPenanganan.setText(intent.getStringExtra("catatan_tindak_lanjut"));
        }

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

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String idLaporan = jsonObject.optString("id_laporan");
                                String alamatLaporan = jsonObject.optString("lokasi_kejadian");
                                newIdLaporanList.add(idLaporan);
                                newLokasiLaporanList.add(alamatLaporan);
                                idLaporanPositionMap.put(idLaporan, i); // Mapping ID ke posisi - gpt

                            }
                            if (!idLaporanList.equals(newIdLaporanList) || !lokasiLaporanList.equals(newLokasiLaporanList)) {
                                idLaporanList.clear();
                                lokasiLaporanList.clear();
                                idLaporanList.addAll(newIdLaporanList);
                                lokasiLaporanList.addAll(newLokasiLaporanList);

                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(edit_data_penanganan_laporan.this,
                                        android.R.layout.simple_spinner_item, idLaporanList);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                id_laporan.setAdapter(adapter);

                                // Set nilai awal berdasarkan id_pelapor - gpt
                                if (Idlaporan != null && idLaporanPositionMap.containsKey(Idlaporan)) {
                                    int position = idLaporanPositionMap.get(Idlaporan);
                                    id_laporan.setSelection(position);
                                } else {
                                    id_laporan.setSelection(lastSelectedPosition);
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

    private void loadDataFromServerNoPlat() {
        String url = new Configurasi().baseUrl() + "kendaraan/tampil_data_kendaraan.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ArrayList<String> newNoPlatList = new ArrayList<>(); //gpt
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String PlatNo = jsonObject.optString("no_plat");
                                newNoPlatList.add(PlatNo);
                                idPlatNoPositionMap.put(PlatNo, i); // Mapping ID ke posisi - gpt
                            }
                            if (!noPlatLaporanList.equals(newNoPlatList)) {
                                noPlatLaporanList.clear();
                                noPlatLaporanList.addAll(newNoPlatList);

                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(edit_data_penanganan_laporan.this,
                                        android.R.layout.simple_spinner_item, noPlatLaporanList);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                no_plat.setAdapter(adapter);

                                // Set nilai awal berdasarkan id_pelapor - gpt
                                if (NoPlat != null && idPlatNoPositionMap.containsKey(NoPlat)){
                                    int position = idPlatNoPositionMap.get(NoPlat);
                                    no_plat.setSelection(position);
                                }
                                } else {
                                    no_plat.setSelection(lastSelectedPositionNoPlat);
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

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String noIdentitasPetugas = jsonObject.optString("noidentitas_petugas");
                                String namaPetugas = jsonObject.optString("nama_petugas");
                                newNoIdentitasPetugas.add(noIdentitasPetugas);
                                newNamaPetugas.add(namaPetugas);
                                idPetugasPositionMap.put(noIdentitasPetugas, i); // Mapping ID ke posisi - gpt
                            }
                            if (!idPetugasList.equals(newNoIdentitasPetugas) || !namaPetugasList.equals(newNamaPetugas)) {
                                idPetugasList.clear();
                                namaPetugasList.clear();
                                idPetugasList.addAll(newNoIdentitasPetugas);
                                namaPetugasList.addAll(newNamaPetugas);


                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(edit_data_penanganan_laporan.this,
                                        android.R.layout.simple_spinner_item, namaPetugasList);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                nama_petugas.setAdapter(adapter);
                                // Set nilai awal berdasarkan id_pelapor - gpt
                                if (IdPetugas != null && idPetugasPositionMap.containsKey(IdPetugas)) {
                                    int position = idPetugasPositionMap.get(IdPetugas);
                                    nama_petugas.setSelection(position);
                                } else {
                                    nama_petugas.setSelection(lastSelectedPositionNamaPetugas);
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
        final String s_id_penanganan = idPenanganan;
        final String s_id_laporan = GetIdLaporan;
        final String s_no_plat = GetPlat;
        final String s_noidentitas = GetIdPetugas;
        final String deskripsi_penanganan = editDeskripsiPenanganan.getText().toString().trim();
        final String catatan_tindak_lanjut = editCatatanPenanganan.getText().toString().trim();

        if (s_id_penanganan.isEmpty() || s_id_laporan==null || s_no_plat==null || s_noidentitas==null || deskripsi_penanganan.isEmpty() || catatan_tindak_lanjut.isEmpty()) {
            Toast.makeText(edit_data_penanganan_laporan.this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }


        String url = new Configurasi().baseUrl() + "penanganan_laporan/edit_data_penanganan_laporan.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("edit_data_kendaraan", "Response: " + response);
                        Toast.makeText(edit_data_penanganan_laporan.this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show();
                        sendRefreshFlag(true);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("edit_data_kendaraan", "Error: " + error.getMessage());
                        Toast.makeText(edit_data_penanganan_laporan.this, "Gagal memperbarui data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_penanganan", s_id_penanganan);
                params.put("id_laporan", s_id_laporan);
                params.put("no_plat", s_no_plat);
                params.put("noidentitas_petugas", s_noidentitas);
                params.put("deskripsi_penanganan", deskripsi_penanganan);
                params.put("catatan_tindak_lanjut", catatan_tindak_lanjut);
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
