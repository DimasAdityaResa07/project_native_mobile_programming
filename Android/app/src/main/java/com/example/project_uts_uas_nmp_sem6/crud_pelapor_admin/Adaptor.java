package com.example.project_uts_uas_nmp_sem6.crud_pelapor_admin;

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
import com.example.project_uts_uas_nmp_sem6.crud_penerimaan_laporan.daftar_penerimaan_laporan_admin;
import com.example.project_uts_uas_nmp_sem6.crud_penerimaan_laporan.simpan_data_penerimaan_laporan;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Adaptor extends BaseAdapter {
    private Context context;
    private ArrayList<GetData> model;
    private LayoutInflater inflater;
    private ProgressBar progressBar;
    private DataChangeListener dataChangeListener;

    public interface DataChangeListener {
        void onDataChanged();
    }

    public Adaptor(Context context, ArrayList<GetData> model, ProgressBar progressBar, DataChangeListener dataChangeListener) {
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
            convertView = inflater.inflate(R.layout.list_pelapor, parent, false);
            holder = new ViewHolder();
            holder.no_identitas = convertView.findViewById(R.id.no_identitas);
            holder.nama = convertView.findViewById(R.id.nama);
            holder.nomorHp = convertView.findViewById(R.id.nomor_hp);
            holder.alamat = convertView.findViewById(R.id.alamat);
            holder.email = convertView.findViewById(R.id.email);
            holder.frameLaporan = convertView.findViewById(R.id.frameAdaptor);
            holder.editButton = convertView.findViewById(R.id.editButton);
            holder.deleteButton = convertView.findViewById(R.id.deleteButton);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final GetData data = model.get(position);
        holder.no_identitas.setText(data.getNo_identitas_pelapor());
        holder.nama.setText(data.getNamaPelapor());
        holder.nomorHp.setText(data.getNo_hp_pelapor());
        holder.alamat.setText(data.getAlamat());
        holder.email.setText(data.getEmail());

        holder.frameLaporan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cekPunyaLaporan(data.getNo_identitas_pelapor(), data.getNamaPelapor());
//                    Toast.makeText(context.getApplicationContext(), "dimas",Toast.LENGTH_SHORT).show();
            }
        });

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, edit_data_pelapor.class);
                intent.putExtra("no_identitas_pelapor", data.getNo_identitas_pelapor());
                intent.putExtra("nama", data.getNamaPelapor());
                intent.putExtra("alamat", data.getAlamat());
                intent.putExtra("nomor_hp", data.getNo_hp_pelapor());
                intent.putExtra("email", data.getEmail());

                context.startActivity(intent);
//                Toast.makeText(context, data.getEmail()+data.getNama()+data.getPassword()+data.getAlamat()+data.getNomor_hp()+data.getRole(),Toast.LENGTH_SHORT).show();
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData(data.getNo_identitas_pelapor(), position);
//                Toast.makeText(context,data.getEmail(),Toast.LENGTH_SHORT).show();
            }

        });
        return convertView;
    }

    public void reloadData() {
        notifyDataSetChanged();
    }

    private void deleteData(final String no_identitas, final int position) {
        String url = new Configurasi().baseUrl() + "pelapor/hapus_data_pelapor.php";

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
                form.put("no_identitas_pelapor", no_identitas);
                return form;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    private void cekPunyaLaporan(String no_identitas_pelapor, String nama) {
        final String noIdentitasCek = no_identitas_pelapor;
        String url = new Configurasi().baseUrl() + "pelapor/cek_pelapor_punya_laporan.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("ada_laporan")) {
                            Intent intent = new Intent(context, daftar_penerimaan_laporan_admin.class);
                            intent.putExtra("no_identitas_pelapor", no_identitas_pelapor);
                            intent.putExtra("nama", nama);

                            context.startActivity(intent);
                        } else {
                            Intent intent = new Intent(context, simpan_data_penerimaan_laporan.class);
                            intent.putExtra("no_identitas_pelapor", no_identitas_pelapor);
                            intent.putExtra("nama", nama);

                            context.startActivity(intent);
                            Toast.makeText(context.getApplicationContext(),"Tidak Ada Riwayat Laporan, Silahkan Tambahkan Laporan", Toast.LENGTH_SHORT).show();
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
                params.put("no_identitas_pelapor", noIdentitasCek);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        requestQueue.add(request);
    }

    private static class ViewHolder {
        TextView no_identitas, nama, alamat, nomorHp, email;
        LinearLayout frameLaporan;
        ImageButton editButton, deleteButton;
    }
}
