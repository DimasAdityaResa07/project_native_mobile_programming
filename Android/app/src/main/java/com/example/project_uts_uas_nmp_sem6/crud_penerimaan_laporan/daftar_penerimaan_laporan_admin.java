package com.example.project_uts_uas_nmp_sem6.crud_penerimaan_laporan;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.project_uts_uas_nmp_sem6.Configurasi.Configurasi;
import com.example.project_uts_uas_nmp_sem6.R;
import com.example.project_uts_uas_nmp_sem6.crud_penanganan_laporan.simpan_data_penanganan_laporan;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class daftar_penerimaan_laporan_admin extends AppCompatActivity implements Adaptor_penerimaan_laporan.DataChangeListener {
    private static final int REQUEST_CODE_SIMPAN_DATA = 1; //gpt
    private static final int EDIT_DATA_REQUEST = 2; //gpt
    private static final int RELOAD_DELAY_MS = 1000; // 1 second delay - gpt
    private static final int AUTO_RELOAD_INTERVAL_MS = 10000; // 10 seconds delay -gpt
    private ListView listView;
    private ProgressBar progressBar;
    private ArrayList<GetDataPenerimaanLaporan> model;
    private Adaptor_penerimaan_laporan adaptor;
    String no_identitas, nama_identitas, idKategori;
    int cek_intent = 0;

    private Handler handler = new Handler();
    private Runnable autoReloadRunnable = new Runnable() {
        @Override
        public void run() {
            loadData();
            handler.postDelayed(this, AUTO_RELOAD_INTERVAL_MS); //gpt
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_penerimaan_laporan_admin);

        listView = findViewById(R.id.list);
        progressBar = findViewById(R.id.progressBar);
        FloatingActionButton fab = findViewById(R.id.tambah_data);

        Intent intent = getIntent();
        if (intent!=null){
            no_identitas = intent.getStringExtra("no_identitas_pelapor");
            nama_identitas = intent.getStringExtra("nama");
            idKategori = intent.getStringExtra("id_kategori");

            if(no_identitas!=null) {
                cek_intent = 1;
            }if (idKategori!= null){
                cek_intent=2;
                fab.setVisibility(View.INVISIBLE);
            }
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cek_intent == 1 && no_identitas != null){
                    Intent intent = new Intent(daftar_penerimaan_laporan_admin.this, simpan_data_penerimaan_laporan.class);
                    intent.putExtra("no_identitas_pelapor", no_identitas);
                    intent.putExtra("nama", nama_identitas);
                    startActivity(intent);
//                    Toast.makeText(daftar_penerimaan_laporan_admin.this,no_identitas + nama_identitas,Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(daftar_penerimaan_laporan_admin.this, simpan_data_penerimaan_laporan.class);
                    startActivityForResult(intent, REQUEST_CODE_SIMPAN_DATA);
                }
            }
        });



        loadData();
    }

    //gpt onResume dan onPause - auto reload
    @Override
    protected void onResume() {
        super.onResume();
        loadData();
        handler.postDelayed(autoReloadRunnable, AUTO_RELOAD_INTERVAL_MS);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(autoReloadRunnable);
    }

    private void loadData() {
        if (cek_intent == 1 && no_identitas != null) {
            String url = new Configurasi().baseUrl() + "penerimaan_laporan/get_tampil_data_penerimaan_laporan.php";
            progressBar.setVisibility(View.VISIBLE);

            StringRequest request = new StringRequest(
                    Request.Method.POST,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressBar.setVisibility(View.GONE);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                model = new ArrayList<>();

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject getData = jsonArray.getJSONObject(i);
                                    model.add(new GetDataPenerimaanLaporan(
                                            getData.getString("id_laporan"),
                                            getData.getString("no_identitas_pelapor"),
                                            getData.getString("nama_pelapor"),
                                            getData.getString("lokasi_kejadian"),
                                            getData.getString("waktu_laporan"),
                                            getData.getString("id_kategori"),
                                            getData.getString("nama_kategori"),
                                            getData.getString("deskripsi_kejadian"),
                                            getData.getString("status_laporan")
                                    ));
                                }
                                adaptor = new Adaptor_penerimaan_laporan(daftar_penerimaan_laporan_admin.this, model, progressBar, daftar_penerimaan_laporan_admin.this);
                                listView.setAdapter(adaptor);
                                Log.d("daftarkendaraan", "Data Berhasil diload");

                            } catch (JSONException e) {
                                Log.e("daftarkendaraan", "Gagal Mengambil data JSON " + e.getMessage());
                                Toast.makeText(daftar_penerimaan_laporan_admin.this, "Gagal Mengambil data JSON", Toast.LENGTH_SHORT).show();
                                // Reload data on error
                                reloadData();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            progressBar.setVisibility(View.GONE);
                            Log.e("daftarkendaraan", "Volley error: " + volleyError.getMessage());
                            Toast.makeText(daftar_penerimaan_laporan_admin.this, "Volley Error", Toast.LENGTH_SHORT).show();
                            reloadData();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("no_identitas_pelapor", no_identitas);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request);

        }
        else if(cek_intent ==2 && idKategori !=null){
            String url = new Configurasi().baseUrl() + "penerimaan_laporan/get_kategori_tampil_penerimaan_laporan.php";
            progressBar.setVisibility(View.VISIBLE);

            StringRequest request = new StringRequest(
                    Request.Method.POST,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressBar.setVisibility(View.GONE);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                model = new ArrayList<>();

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject getData = jsonArray.getJSONObject(i);
                                    model.add(new GetDataPenerimaanLaporan(
                                            getData.getString("id_laporan"),
                                            getData.getString("no_identitas_pelapor"),
                                            getData.getString("nama_pelapor"),
                                            getData.getString("lokasi_kejadian"),
                                            getData.getString("waktu_laporan"),
                                            getData.getString("id_kategori"),
                                            getData.getString("nama_kategori"),
                                            getData.getString("deskripsi_kejadian"),
                                            getData.getString("status_laporan")
                                    ));
                                }
                                adaptor = new Adaptor_penerimaan_laporan(daftar_penerimaan_laporan_admin.this, model, progressBar, daftar_penerimaan_laporan_admin.this);
                                listView.setAdapter(adaptor);
                                Log.d("daftarkendaraan", "Data Berhasil diload");

                            } catch (JSONException e) {
                                Log.e("daftarkendaraan", "Gagal Mengambil data JSON " + e.getMessage());
                                Toast.makeText(daftar_penerimaan_laporan_admin.this, "Gagal Mengambil data JSON", Toast.LENGTH_SHORT).show();
                                // Reload data on error
                                reloadData();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            progressBar.setVisibility(View.GONE);
                            Log.e("daftarkendaraan", "Volley error: " + volleyError.getMessage());
                            Toast.makeText(daftar_penerimaan_laporan_admin.this, "Volley Error", Toast.LENGTH_SHORT).show();
                            reloadData();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("id_kategori", idKategori);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request);
        }

        else {
            String url = new Configurasi().baseUrl() + "penerimaan_laporan/tampil_data_penerimaan_laporan.php";
            progressBar.setVisibility(View.VISIBLE);

            StringRequest request = new StringRequest(
                    Request.Method.POST,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressBar.setVisibility(View.GONE);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                model = new ArrayList<>();

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject getData = jsonArray.getJSONObject(i);
                                    model.add(new GetDataPenerimaanLaporan(
                                            getData.getString("id_laporan"),
                                            getData.getString("no_identitas_pelapor"),
                                            getData.getString("nama_pelapor"),
                                            getData.getString("lokasi_kejadian"),
                                            getData.getString("waktu_laporan"),
                                            getData.getString("id_kategori"),
                                            getData.getString("nama_kategori"),
                                            getData.getString("deskripsi_kejadian"),
                                            getData.getString("status_laporan")
                                    ));
                                }

                                adaptor = new Adaptor_penerimaan_laporan(daftar_penerimaan_laporan_admin.this, model, progressBar, daftar_penerimaan_laporan_admin.this);
                                listView.setAdapter(adaptor);
                                Log.d("daftarkendaraan", "Data Berhasil diload");

                            } catch (JSONException e) {
                                Log.e("daftarkendaraan", "Gagal Mengambil data JSON " + e.getMessage());
                                Toast.makeText(daftar_penerimaan_laporan_admin.this, "Gagal Mengambil data JSON", Toast.LENGTH_SHORT).show();
                                // Reload data on error
                                reloadData();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            progressBar.setVisibility(View.GONE);
                            Log.e("daftarkendaraan", "Volley error: " + volleyError.getMessage());
                            Toast.makeText(daftar_penerimaan_laporan_admin.this, "Volley Error", Toast.LENGTH_SHORT).show();
                            reloadData();
                        }
                    });

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request);
        }
    }

    @Override
    public void onDataChanged() {
        Log.d("daftarkendaraan", "Perubahan Data, Data Reload");
        reloadData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SIMPAN_DATA) {
            if (resultCode == RESULT_OK && data != null) {
                boolean refresh = data.getBooleanExtra("refreshflag", false);
                if (refresh) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadData(); // Reload data
                        }
                    }, RELOAD_DELAY_MS);
                }
            }
        } else if (requestCode == EDIT_DATA_REQUEST) {
            if (resultCode == RESULT_OK && data != null) {
                boolean refresh = data.getBooleanExtra("refreshflag", false);
                if (refresh) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadData(); // Reload data
                        }
                    }, RELOAD_DELAY_MS);
                }
            }
        }
    }

    private void reloadData() {
        loadData(); // Reload data
    }
}
