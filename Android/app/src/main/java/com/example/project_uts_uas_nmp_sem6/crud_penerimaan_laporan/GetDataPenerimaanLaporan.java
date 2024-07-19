package com.example.project_uts_uas_nmp_sem6.crud_penerimaan_laporan;

import java.io.Serializable;

public class GetDataPenerimaanLaporan implements Serializable {
    private String id_laporan;
    private String no_identitas_pelapor;
    private String nama_pelapor;
    private String lokasi_kejadian;
    private String waktu_laporan;
    private String id_kategori;
    private String nama_kategori;
    private String deskripsi_kejadian;
    private String status_laporan;

    public GetDataPenerimaanLaporan(String id_laporan,String no_identitas_pelapor, String nama_pelapor,String lokasi_kejadian, String waktu_laporan, String id_kategori, String nama_kategori, String deskripsi_kejadian, String status_laporan){
        this.id_laporan=id_laporan;
        this.no_identitas_pelapor=no_identitas_pelapor;
        this.nama_pelapor=nama_pelapor;
        this.lokasi_kejadian=lokasi_kejadian;
        this.waktu_laporan=waktu_laporan;
        this.id_kategori=id_kategori;
        this.nama_kategori=nama_kategori;
        this.deskripsi_kejadian=deskripsi_kejadian;
        this.status_laporan=status_laporan;
    }

    public String getId_laporan() {
        return id_laporan;
    }

    public String getNo_identitas_pelapor() {
        return no_identitas_pelapor;
    }

    public String getNama_pelapor() {
        return nama_pelapor;
    }

    public String getLokasi_kejadian() {
        return lokasi_kejadian;
    }

    public String getWaktu_laporan() {
        return waktu_laporan;
    }

    public String getId_kategori() {
        return id_kategori;
    }

    public String getNama_kategori() {
        return nama_kategori;
    }

    public String getDeskripsi_kejadian() {
        return deskripsi_kejadian;
    }

    public String getStatus_laporan() {
        return status_laporan;
    }
}



