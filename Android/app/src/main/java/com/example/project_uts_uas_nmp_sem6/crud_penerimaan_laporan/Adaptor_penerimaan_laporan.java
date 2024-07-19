package com.example.project_uts_uas_nmp_sem6.crud_penerimaan_laporan;

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
import com.example.project_uts_uas_nmp_sem6.crud_penanganan_laporan.simpan_data_penanganan_laporan;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Adaptor_penerimaan_laporan extends BaseAdapter {
    private Context context;
    private ArrayList<GetDataPenerimaanLaporan> model;
    private LayoutInflater inflater;
    private ProgressBar progressBar;
    private DataChangeListener dataChangeListener;

    public interface DataChangeListener {
        void onDataChanged();
    }

    public Adaptor_penerimaan_laporan(Context context, ArrayList<GetDataPenerimaanLaporan> model, ProgressBar progressBar, DataChangeListener dataChangeListener) {
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
            convertView = inflater.inflate(R.layout.list_penerimaan_laporan, parent, false);
            holder = new ViewHolder();
            holder.idLaporan = convertView.findViewById(R.id.id_laporan);
            holder.nama = convertView.findViewById(R.id.nama_pelapor);
            holder.lokasi = convertView.findViewById(R.id.lokasi);
            holder.waktu = convertView.findViewById(R.id.waktu);
            holder.kategori=convertView.findViewById(R.id.kategori);
            holder.statusLaporan = convertView.findViewById(R.id.status_laporan);
            holder.deskripsiKejadian = convertView.findViewById(R.id.deskripsi_kejadian);
            holder.gambarAdaptor=convertView.findViewById(R.id.gambarAdaptor);
            holder.framePenanganan=convertView.findViewById(R.id.framePenanganan);
            holder.editButton = convertView.findViewById(R.id.editButton);
            holder.deleteButton = convertView.findViewById(R.id.deleteButton);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final GetDataPenerimaanLaporan data = model.get(position);
        holder.idLaporan.setText(data.getId_laporan());
        holder.nama.setText(data.getNama_pelapor());
        holder.lokasi.setText(data.getLokasi_kejadian());
        holder.waktu.setText(data.getWaktu_laporan());
        holder.kategori.setText(data.getNama_kategori());
        holder.deskripsiKejadian.setText(data.getDeskripsi_kejadian());
        holder.statusLaporan.setText(data.getStatus_laporan());

        if(data.getStatus_laporan().equals("Menunggu Konfirmasi")){
            holder.gambarAdaptor.setImageResource(R.drawable.waiting_confirm);
        } else if (data.getStatus_laporan().equals("Sedang Diproses")) {
            holder.gambarAdaptor.setImageResource(R.drawable.laporan_proses);
        }
        else if(data.getStatus_laporan().equals("Selesai Dikerjakan")){
            holder.gambarAdaptor.setImageResource(R.drawable.laporan_selesai);
        }
        else{
            holder.gambarAdaptor.setImageResource(R.drawable.report);
        }

        holder.framePenanganan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cekPunyaPenanganan(data.getId_laporan(),data.getLokasi_kejadian());
//                    Toast.makeText(context.getApplicationContext(), "dimas",Toast.LENGTH_SHORT).show();
            }
        });

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, edit_data_penerimaan_laporan.class);
                intent.putExtra("id_laporan", data.getId_laporan());
                intent.putExtra("no_identitas", data.getNo_identitas_pelapor());
                intent.putExtra("nama_pelapor", data.getNama_pelapor());
                intent.putExtra("lokasi", data.getLokasi_kejadian());
                intent.putExtra("waktu", data.getWaktu_laporan());
                intent.putExtra("kategori", data.getId_kategori());
                intent.putExtra("nama_kategori", data.getNama_kategori());
                intent.putExtra("deskripsi_kejadian", data.getDeskripsi_kejadian());
                intent.putExtra("status_laporan", data.getStatus_laporan());

                context.startActivity(intent);
//                Toast.makeText(context, data.getNama_pelapor(),Toast.LENGTH_SHORT).show();
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData(data.getId_laporan(), position);
//                Toast.makeText(context,data.getNo_plat(),Toast.LENGTH_SHORT).show();
            }

        });

        return convertView;
    }

    public void reloadData() {
        notifyDataSetChanged();
    }

    private void deleteData(final String id_laporan, final int position) {
        String url = new Configurasi().baseUrl() + "penerimaan_laporan/hapus_data_penerimaan_laporan.php";

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
                            Toast.makeText(context, "Laporan ini sudah ditangani sehingga tidak dapat DIHAPUS", Toast.LENGTH_LONG).show();
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
                form.put("id_laporan", id_laporan);
                return form;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    private void cekPunyaPenanganan(String id_laporan, String lokasi_laporan) {
        final String noIdentitasCek = id_laporan;
        String url = new Configurasi().baseUrl() + "penerimaan_laporan/cek_penerimaan_punya_penanganan.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("ada_penanganan")) {
                            Intent intent = new Intent(context, daftar_penanganan_laporan_admin.class);
                            intent.putExtra("id_laporan", id_laporan);
                            intent.putExtra("lokasi_kejadian",lokasi_laporan);
//                            Toast.makeText(context.getApplicationContext(),lokasi_laporan, Toast.LENGTH_SHORT).show();
                            context.startActivity(intent);
                        } else {
                            Intent intent = new Intent(context, simpan_data_penanganan_laporan.class);
                            intent.putExtra("id_laporan", id_laporan);
                            intent.putExtra("lokasi_kejadian",lokasi_laporan);
//                            Toast.makeText(context.getApplicationContext(),lokasi_laporan, Toast.LENGTH_SHORT).show();
                            Toast.makeText(context.getApplicationContext(),"Tidak Ada Riwayat Penanganan, Silahkan Buat Permohonan", Toast.LENGTH_SHORT).show();
                            context.startActivity(intent);
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
                params.put("id_laporan", noIdentitasCek);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        requestQueue.add(request);
    }

    private static class ViewHolder {
        TextView idLaporan,nama,lokasi,waktu,kategori,statusLaporan, deskripsiKejadian;
        LinearLayout framePenanganan;
        ImageButton editButton, deleteButton;
        ImageView gambarAdaptor;
    }
}
