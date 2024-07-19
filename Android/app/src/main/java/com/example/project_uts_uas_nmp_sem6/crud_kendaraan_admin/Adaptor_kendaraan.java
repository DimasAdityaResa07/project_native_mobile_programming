package com.example.project_uts_uas_nmp_sem6.crud_kendaraan_admin;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
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
import com.example.project_uts_uas_nmp_sem6.crud_penerimaan_laporan.daftar_penerimaan_laporan_admin;
import com.example.project_uts_uas_nmp_sem6.crud_penerimaan_laporan.simpan_data_penerimaan_laporan;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Adaptor_kendaraan extends BaseAdapter {
    private Context context;
    private ArrayList<GetDataKendaraan> model;
    private LayoutInflater inflater;
    private ProgressBar progressBar;
    private DataChangeListener dataChangeListener;

    public interface DataChangeListener {
        void onDataChanged();
    }

    public Adaptor_kendaraan(Context context, ArrayList<GetDataKendaraan> model, ProgressBar progressBar, DataChangeListener dataChangeListener) {
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
            convertView = inflater.inflate(R.layout.list_kendaraan, parent, false);
            holder = new ViewHolder();
            holder.noPlat = convertView.findViewById(R.id.no_plat);
            holder.merk = convertView.findViewById(R.id.merk);
            holder.type = convertView.findViewById(R.id.type);
            holder.jenis = convertView.findViewById(R.id.jenis);
            holder.tanggalStnk=convertView.findViewById(R.id.tanggal_stnk);
            holder.statusKendaraan = convertView.findViewById(R.id.status_kendaraan);
            holder.frameAdaptor = convertView.findViewById(R.id.frameAdaptor);
            holder.editButton = convertView.findViewById(R.id.editButton);
            holder.deleteButton = convertView.findViewById(R.id.deleteButton);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final GetDataKendaraan data = model.get(position);
        holder.noPlat.setText(data.getNo_plat());
        holder.merk.setText(data.getMerk());
        holder.type.setText(data.getType());
        holder.tanggalStnk.setText(data.getTanggal_stnk());
        holder.jenis.setText(data.getJenis());
        holder.statusKendaraan.setText(data.getStatus_kendaraan());

        holder.frameAdaptor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cekPunyaLaporan(data.getNo_plat(), data.getJenis());
            }
        });

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, edit_data_kendaraan.class);
                intent.putExtra("no_plat", data.getNo_plat());
                intent.putExtra("merk", data.getMerk());
                intent.putExtra("type", data.getType());
                intent.putExtra("jenis", data.getJenis());
                intent.putExtra("tanggal_stnk", data.getTanggal_stnk());
                intent.putExtra("status_kendaraan", data.getStatus_kendaraan());

                context.startActivity(intent);
//                Toast.makeText(context, data.getEmail()+data.getNama()+data.getPassword()+data.getAlamat()+data.getNomor_hp()+data.getRole(),Toast.LENGTH_SHORT).show();
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData(data.getNo_plat(), position);
//                Toast.makeText(context,data.getNo_plat(),Toast.LENGTH_SHORT).show();
            }

        });

        return convertView;
    }

    public void reloadData() {
        notifyDataSetChanged();
    }

    private void deleteData(final String NoPlat, final int position) {
        String url = new Configurasi().baseUrl() + "kendaraan/hapus_data_kendaraan.php";

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
                                if (dataChangeListener != null) {
                                    dataChangeListener.onDataChanged();
                                }
//                            } else {
                                if (dataChangeListener != null) {
                                    dataChangeListener.onDataChanged();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Kendaraan ini sedang/pernah menangani laporan sehinga tidak dapat DIHAPUS", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(context, "Error Deleting Data", Toast.LENGTH_SHORT).show();
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
                form.put("no_plat", NoPlat);
                return form;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    private void cekPunyaLaporan(String no_plat, String jenis) {
        final String noPlatCek = no_plat;
        String url = new Configurasi().baseUrl() + "kendaraan/cek_plat_punya_penanganan.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("ada_penanganan")) {
                            Intent intent = new Intent(context, daftar_penanganan_laporan_admin.class);
                            intent.putExtra("no_plat", no_plat);
                            intent.putExtra("jenis", jenis);
                            context.startActivity(intent);
                        } else {
                            Toast.makeText(context.getApplicationContext(),"Kendaraan Ini Tidak Menangani Permohonan Apapun", Toast.LENGTH_SHORT).show();
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
                params.put("no_plat", noPlatCek);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        requestQueue.add(request);
    }

    private static class ViewHolder {
        TextView noPlat,merk,type,jenis,tanggalStnk,statusKendaraan;
        LinearLayout frameAdaptor;
        ImageButton editButton, deleteButton;
    }
}
