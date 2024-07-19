package com.example.project_uts_uas_nmp_sem6.crud_pelapor_admin;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class daftar_pelapor_admin extends AppCompatActivity implements Adaptor.DataChangeListener {
    private static final int REQUEST_CODE_SIMPAN_DATA = 1; //gpt - auto reload
    private static final int EDIT_DATA_REQUEST = 2; //gpt - auto reload
    private static final int RELOAD_DELAY_MS = 1000; // 1 second delay -gpt - auto reload
    private static final int AUTO_RELOAD_INTERVAL_MS = 10000; // 10 seconds delay -gpt - auto reload
    private ListView listView;
    private ProgressBar progressBar;
    private ArrayList<GetData> model;
    private Adaptor adaptor;

    //handler auto reload - gpt
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
        setContentView(R.layout.activity_daftar_pelapor_admin);

        listView = findViewById(R.id.list);
        progressBar = findViewById(R.id.progressBar);

        FloatingActionButton fab = findViewById(R.id.tambah_data);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(daftar_pelapor_admin.this, simpan_data_pelapor.class); // Start SimpanData activity
                startActivityForResult(intent, REQUEST_CODE_SIMPAN_DATA);
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
        String url = new Configurasi().baseUrl() + "pelapor/tampil_data_pelapor.php";
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
                                model.add(new GetData(
                                        getData.getString("no_identitas_pelapor"),
                                        getData.getString("nama_pelapor"),
                                        getData.getString("no_hp_pelapor"),
                                        getData.getString("alamat"),
                                        getData.getString("email")
                                ));
                            }

                            adaptor = new Adaptor(daftar_pelapor_admin.this, model, progressBar, daftar_pelapor_admin.this);
                            listView.setAdapter(adaptor);
                            Log.d("daftaruser", "Data loaded successfully");

                        } catch (JSONException e) {
                            Log.e("daftaruser", "JSON Parsing error: " + e.getMessage());
                            Toast.makeText(daftar_pelapor_admin.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                            reloadData();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("daftaruser", "Volley error: " + volleyError.getMessage());
                        Toast.makeText(daftar_pelapor_admin.this, "Terjadi kesalahan saat memuat data", Toast.LENGTH_SHORT).show();
                        reloadData();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    @Override
    public void onDataChanged() {
        Log.d("daftaruser", "Terjadi Perubahan Data, Melakukan Reload Data");
        reloadData();
    }

    @Override //auto reload ketika setelah menyimpan dan edit
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
