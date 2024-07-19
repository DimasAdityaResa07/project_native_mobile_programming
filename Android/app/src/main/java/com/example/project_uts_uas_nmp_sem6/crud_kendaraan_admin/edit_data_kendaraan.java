package com.example.project_uts_uas_nmp_sem6.crud_kendaraan_admin;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class edit_data_kendaraan extends AppCompatActivity {

    private EditText editMerk, editType, editJenis, editTanggalStnk,editNoPlat;
    Spinner editStatusKendaraan;
    private Button updateButton;
    private String noPlat;
    private ImageView datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data_kendaraan);

        editMerk = findViewById(R.id.input_merk);
        editType = findViewById(R.id.input_type);
        editJenis = findViewById(R.id.input_jenis);
        editTanggalStnk = findViewById(R.id.input_tanggal_stnk);
        editStatusKendaraan = findViewById(R.id.input_status_kendaraan);
        editNoPlat=findViewById(R.id.input_no_plat);
        updateButton = findViewById(R.id.btn_edit);
        datePicker=findViewById(R.id.icon_date_picker);

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status_kendaraan_edit, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editStatusKendaraan.setAdapter(adapter);

        Intent intent = getIntent();
        if (intent != null) {
            noPlat = intent.getStringExtra("no_plat");
            editNoPlat.setText(intent.getStringExtra("no_plat"));
            editMerk.setText(intent.getStringExtra("merk"));
            editType.setText(intent.getStringExtra("type"));
            editJenis.setText(intent.getStringExtra("jenis"));
            editTanggalStnk.setText(intent.getStringExtra("tanggal_stnk"));

            String statusKendaraan = intent.getStringExtra("status_kendaraan");
            if (statusKendaraan != null) {
                int spinnerPosition = adapter.getPosition(statusKendaraan);
                editStatusKendaraan.setSelection(spinnerPosition);
            }
        }

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });
    }
    private void showDatePicker(){
        String tanggalSTNK = editTanggalStnk.getText().toString().trim();

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        if (!tanggalSTNK.isEmpty()) {
            try {
                String[] parts = tanggalSTNK.split("-");
                year = Integer.parseInt(parts[0]);
                month = Integer.parseInt(parts[1]) - 1; // Bulan dalam Calendar dimulai dari 0 //Dimas
                dayOfMonth = Integer.parseInt(parts[2]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(edit_data_kendaraan.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String selectedDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        editTanggalStnk.setText(selectedDate);
                    }
                }, year, month, dayOfMonth);

        datePickerDialog.show();
    }

    private void updateData() {
        final String merk = editMerk.getText().toString().trim();
        final String type = editType.getText().toString().trim();
        final String jenis = editJenis.getText().toString().trim();
        final String tanggal_stnk = editTanggalStnk.getText().toString().trim();
        final String status_kendaraan = String.valueOf(editStatusKendaraan.getSelectedItem()).trim();


        if (merk.isEmpty() || type.isEmpty() || jenis.isEmpty() || tanggal_stnk.isEmpty() || status_kendaraan.isEmpty()) {
            Toast.makeText(edit_data_kendaraan.this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }


        String url = new Configurasi().baseUrl() + "kendaraan/edit_data_kendaraan.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("edit_data_kendaraan", "Response: " + response);
                        Toast.makeText(edit_data_kendaraan.this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show();
                        sendRefreshFlag(true);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("edit_data_kendaraan", "Error: " + error.getMessage());
                        Toast.makeText(edit_data_kendaraan.this, "Gagal memperbarui data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("no_plat", noPlat);
                params.put("merk", merk);
                params.put("type", type);
                params.put("jenis", jenis);
                params.put("tanggal_stnk", tanggal_stnk);
                params.put("status_kendaraan", status_kendaraan);
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
