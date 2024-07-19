package com.example.project_uts_uas_nmp_sem6.crud_petugas_admin;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.project_uts_uas_nmp_sem6.Configurasi.Configurasi;
import com.example.project_uts_uas_nmp_sem6.R;
import com.example.project_uts_uas_nmp_sem6.crud_penanganan_laporan.daftar_penanganan_laporan_admin;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Adaptor_petugas extends BaseAdapter {
    private Context context;
    private ArrayList<GetDataPetugas> model;
    private LayoutInflater inflater;
    private ProgressBar progressBar;
    private DataChangeListener dataChangeListener;

    public interface DataChangeListener {
        void onDataChanged();
    }

    public Adaptor_petugas(Context context, ArrayList<GetDataPetugas> model, ProgressBar progressBar, DataChangeListener dataChangeListener) {
        this.context = context;
        this.model = model;
        this.progressBar = progressBar;
        this.dataChangeListener = dataChangeListener;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return model.size();
    }

    @Override
    public Object getItem(int position) {
        return model.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_petugas, parent, false);
            holder = new ViewHolder();
            holder.gambarAdaptor = convertView.findViewById(R.id.gambarAdaptor);
            holder.no_identitas = convertView.findViewById(R.id.no_identitas);
            holder.nama = convertView.findViewById(R.id.nama);
            holder.alamat = convertView.findViewById(R.id.alamat);
            holder.nomorHp = convertView.findViewById(R.id.nomor_hp);
            holder.jabatan = convertView.findViewById(R.id.jabatan);
            holder.frameAdaptor = convertView.findViewById(R.id.frameAdaptor);
            holder.editButton = convertView.findViewById(R.id.editButton);
            holder.deleteButton = convertView.findViewById(R.id.deleteButton);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final GetDataPetugas data = model.get(position);

        if(data.getJenisKelamin().equals("Laki-Laki")){
            holder.gambarAdaptor.setImageResource(R.drawable.firefightermale);
        }
        else if(data.getJenisKelamin().equals("Perempuan")){
            holder.gambarAdaptor.setImageResource(R.drawable.firefighterfemale);
        }
        else{
            holder.gambarAdaptor.setImageResource(R.drawable.team);
        }

        holder.no_identitas.setText(data.getNoidentitasPetugas());
        holder.nama.setText(data.getNamaPetugas());
        holder.nomorHp.setText(data.getNoHpPetugas());
        holder.alamat.setText(data.getAlamatPetugas());
        holder.jabatan.setText(data.getjabatan());

        holder.frameAdaptor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cekPunyaLaporan(data.getNoidentitasPetugas(), data.getNamaPetugas());
            }
        });

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, edit_data_petugas.class);
                intent.putExtra("noidentitas_petugas", data.getNoidentitasPetugas());
                intent.putExtra("nama", data.getNamaPetugas());
                intent.putExtra("jenis_kelamin", data.getJenisKelamin());
                intent.putExtra("alamat", data.getAlamatPetugas());
                intent.putExtra("nomor_hp", data.getNoHpPetugas());
                intent.putExtra("jabatan", data.getjabatan());

                context.startActivity(intent);
//                Toast.makeText(context, data.getEmail()+data.getNama()+data.getPassword()+data.getAlamat()+data.getNomor_hp()+data.getRole(),Toast.LENGTH_SHORT).show();
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData(data.getNoidentitasPetugas(), position);
//                Toast.makeText(context,data.getEmail(),Toast.LENGTH_SHORT).show();
            }

        });
        return convertView;
    }

    public void reloadData() {
        notifyDataSetChanged();
    }

    private void deleteData(final String no_identitaspetugas, final int position) {
        String url = new Configurasi().baseUrl() + "petugas/hapus_data_petugas.php";

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
                            String status = jsonObject.getString("status");
                            if ("success".equals(status)) {
                                Toast.makeText(context, "Data Berhasil Terhapus", Toast.LENGTH_SHORT).show();
                                model.remove(position);
                                notifyDataSetChanged();
                                // Notify data change
                                if (dataChangeListener != null) {
                                    dataChangeListener.onDataChanged();
                                }
                                if (dataChangeListener != null) {
                                    dataChangeListener.onDataChanged();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Pelapor ini terdapat data laporan sehinga tidak dapat DIHAPUS", Toast.LENGTH_LONG).show();
                            // Reload data even if deletion fails
                            if (dataChangeListener != null) {
                                dataChangeListener.onDataChanged();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(context, "Volley Errr", Toast.LENGTH_SHORT).show();
                        // Reload data even if deletion fails
                        if (dataChangeListener != null) {
                            dataChangeListener.onDataChanged();
                        }
                    }
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> form = new HashMap<>();
                form.put("noidentitas_petugas", no_identitaspetugas);
                return form;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    private void cekPunyaLaporan(String id_petugas, String nama_petugas) {
        final String noPetugasCek = id_petugas;
        String url = new Configurasi().baseUrl() + "petugas/cek_petugas_punya_penanganan.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("ada_penanganan")) {
                            Intent intent = new Intent(context, daftar_penanganan_laporan_admin.class);
                            intent.putExtra("noidentitas_petugas", noPetugasCek);
                            intent.putExtra("nama_petugas", nama_petugas);
                            context.startActivity(intent);
                        } else {
                            Toast.makeText(context.getApplicationContext(),"Petugas Ini Tidak Menangani Permohonan Apapun", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("CekNoIdentitas", "Error: " + error.getMessage());
                        Toast.makeText(context.getApplicationContext(), "Gagal memeriksa No Identitas", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("noidentitas_petugas", noPetugasCek);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        requestQueue.add(request);
    }

    private static class ViewHolder {
        TextView no_identitas, nama, alamat, nomorHp, jabatan;
        LinearLayout frameAdaptor;
        ImageButton editButton, deleteButton;
        ImageView gambarAdaptor;
    }
}
