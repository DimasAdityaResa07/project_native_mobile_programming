package com.example.project_uts_uas_nmp_sem6.crud_penanganan_laporan;

import java.io.Serializable;

public class GetDataPenangananLaporan implements Serializable {
    private String id_penanganan;
    private String id_laporan;
    private String lokasi_penanganan;
    private String no_plat;
    private String noidentitas_petugas;
    private String nama_petugas;
    private String deskripsi_penanganan;
    private String catatan_tindak_lanjut;

    public GetDataPenangananLaporan(String id_penanganan, String id_laporan, String lokasi_penanganan, String no_plat, String noidentitas_petugas, String nama_petugas, String deskripsi_penanganan, String catatan_tindak_lanjut){
        this.id_penanganan=id_penanganan;
        this.id_laporan=id_laporan;
        this.lokasi_penanganan=lokasi_penanganan;
        this.no_plat=no_plat;
        this.noidentitas_petugas=noidentitas_petugas;
        this.nama_petugas=nama_petugas;
        this.deskripsi_penanganan=deskripsi_penanganan;
        this.catatan_tindak_lanjut=catatan_tindak_lanjut;
    }

    public String getId_penanganan() {
        return id_penanganan;
    }

    public String getId_laporan() {
        return id_laporan;
    }

    public String getLokasi_penanganan() { return lokasi_penanganan;
    }

    public String getNo_plat() {
        return no_plat;
    }

    public String getNoidentitas_petugas() {
        return noidentitas_petugas;
    }

    public String getNama_petugas() {
        return nama_petugas;
    }

    public String getDeskripsi_penanganan() {
        return deskripsi_penanganan;
    }

    public String getCatatan_tindak_lanjut() {
        return catatan_tindak_lanjut;
    }

}



