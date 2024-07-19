package com.example.project_uts_uas_nmp_sem6.crud_penanganan_laporan;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Adaptor_penanganan_laporan extends BaseAdapter {
    private Context context;
    private ArrayList<GetDataPenangananLaporan> model;
    private LayoutInflater inflater;
    private ProgressBar progressBar;
    private DataChangeListener dataChangeListener;

    public interface DataChangeListener {
        void onDataChanged();
    }

    public Adaptor_penanganan_laporan(Context context, ArrayList<GetDataPenangananLaporan> model, ProgressBar progressBar, DataChangeListener dataChangeListener) {
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
            convertView = inflater.inflate(R.layout.list_penanganan_laporan, parent, false);
            holder = new ViewHolder();
            holder.idPenanganan = convertView.findViewById(R.id.id_penanganan);
            holder.idLaporan = convertView.findViewById(R.id.id_laporan);
            holder.noPlat = convertView.findViewById(R.id.no_plat);
            holder.namaPetugas = convertView.findViewById(R.id.nama_petugas);
            holder.deskripsiPenanganan=convertView.findViewById(R.id.deskripsi_penanganan);
            holder.catatanTindakLanjut = convertView.findViewById(R.id.catatan_tindak_lanjut);
            holder.gambarAdaptor=convertView.findViewById(R.id.gambarAdaptor);
            holder.editButton = convertView.findViewById(R.id.editButton);
            holder.deleteButton = convertView.findViewById(R.id.deleteButton);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final GetDataPenangananLaporan data = model.get(position);
        holder.idPenanganan.setText("ID Penanganan : \n" + data.getId_penanganan());
        holder.idLaporan.setText("ID Laporan : \n"+ data.getId_laporan());
        holder.noPlat.setText(data.getNo_plat());
        holder.namaPetugas.setText(data.getNama_petugas());
        holder.deskripsiPenanganan.setText(data.getDeskripsi_penanganan());
        holder.catatanTindakLanjut.setText(data.getCatatan_tindak_lanjut());

//        if(data.getStatus_laporan().equals("Menunggu Konfirmasi")){
//            holder.gambarAdaptor.setImageResource(R.drawable.waiting_confirm);
//        } else if (data.getStatus_laporan().equals("Sedang Diproses")) {
//            holder.gambarAdaptor.setImageResource(R.drawable.laporan_proses);
//        }
//        else if(data.getStatus_laporan().equals("Selesai Dikerjakan")){
//            holder.gambarAdaptor.setImageResource(R.drawable.laporan_selesai);
//        }
//        else{
            holder.gambarAdaptor.setImageResource(R.drawable.report);
//        }


        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, edit_data_penanganan_laporan.class);
                intent.putExtra("id_penanganan", data.getId_penanganan());
                intent.putExtra("id_laporan", data.getId_laporan());
                intent.putExtra("lokasi_laporan", data.getLokasi_penanganan());
                intent.putExtra("no_plat", data.getNo_plat());
                intent.putExtra("noidentitas_petugas", data.getNoidentitas_petugas());
                intent.putExtra("deskripsi_penanganan", data.getDeskripsi_penanganan());
                intent.putExtra("catatan_tindak_lanjut", data.getCatatan_tindak_lanjut());

                context.startActivity(intent);
//                Toast.makeText(context, data.getNama_pelapor(),Toast.LENGTH_SHORT).show();
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData(data.getId_penanganan(), position);
//                Toast.makeText(context,data.getNo_plat(),Toast.LENGTH_SHORT).show();
            }

        });

        return convertView;
    }

    public void reloadData() {
        notifyDataSetChanged();
    }

    private void deleteData(final String id_penanganan, final int position) {
        String url = new Configurasi().baseUrl() + "penanganan_laporan/hapus_data_penanganan_laporan.php";

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
                            Toast.makeText(context, "Error Parsing Data", Toast.LENGTH_LONG).show();
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
                form.put("id_penanganan", id_penanganan);
                return form;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    private static class ViewHolder {
        TextView idPenanganan, idLaporan, noPlat, namaPetugas, deskripsiPenanganan, catatanTindakLanjut;
        ImageButton editButton, deleteButton;
        ImageView gambarAdaptor;
    }
}
