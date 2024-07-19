package com.example.project_uts_uas_nmp_sem6.crud_petugas_admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class edit_data_petugas extends AppCompatActivity {

    private EditText editNoIdentitasPelapor, editNama, editNomorHp, editAlamat,editJabatan;
    private Button updateButton;

    Spinner editJenisKelamin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data_petugas);

        editNoIdentitasPelapor = findViewById(R.id.input_no_identitas);
        editNama = findViewById(R.id.input_nama);
        editNomorHp = findViewById(R.id.input_no_hp);
        editAlamat = findViewById(R.id.input_alamat);
        editJabatan=findViewById(R.id.input_jabatan);
        editJenisKelamin=findViewById(R.id.input_jenis_kelamin);
        updateButton = findViewById(R.id.btn_edit);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.jenis_kelamin_edit, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editJenisKelamin.setAdapter(adapter);

        Intent intent = getIntent();
        if (intent != null) {
//            noIdentitasPelapor= intent.getStringExtra("no_identitas_petugas");
            editNoIdentitasPelapor.setText(intent.getStringExtra("noidentitas_petugas"));
            editNama.setText(intent.getStringExtra("nama"));
            editAlamat.setText(intent.getStringExtra("alamat"));
            editNomorHp.setText(intent.getStringExtra("nomor_hp"));
            editJabatan.setText(intent.getStringExtra("jabatan"));

            String jenisKelamin = intent.getStringExtra("jenis_kelamin");
            if (jenisKelamin != null) {
                int spinnerPosition = adapter.getPosition(jenisKelamin);
                editJenisKelamin.setSelection(spinnerPosition);
            }
        }
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });
    }

    private void updateData() {
        final String noIdentitasPelaporan = editNoIdentitasPelapor.getText().toString().trim();
        final String nama = editNama.getText().toString().trim();
        final String alamat = editAlamat.getText().toString().trim();
        final String nomorHp = editNomorHp.getText().toString().trim();
        final String email = editJabatan.getText().toString().trim();
        final String jabatan = editJabatan.getText().toString().trim();
        final String jenis_kelamin = String.valueOf(editJenisKelamin.getSelectedItem()).trim();

        if (nama.isEmpty() || alamat.isEmpty() || nomorHp.isEmpty() || email.isEmpty()) {
            Toast.makeText(edit_data_petugas.this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = new Configurasi().baseUrl() + "petugas/edit_data_petugas.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("edit_data_user", "Response: " + response);
                        Toast.makeText(edit_data_petugas.this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show();
                        sendRefreshFlag(true);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("edit_data_user", "Error: " + error.getMessage());
                        Toast.makeText(edit_data_petugas.this, "Gagal memperbarui data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Buat parameter untuk request
                Map<String, String> params = new HashMap<>();
                params.put("noidentitas_petugas", noIdentitasPelaporan);
                params.put("nama_petugas", nama);
                params.put("jenis_kelamin", jenis_kelamin);
                params.put("alamat_petugas", alamat);
                params.put("no_hp_petugas", nomorHp);
                params.put("jabatan", jabatan);
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
