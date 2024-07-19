package com.example.project_uts_uas_nmp_sem6.crud_petugas_admin;

import java.io.Serializable;

public class GetDataPetugas implements Serializable {
    private String noidentitas_petugas;
    private String nama_petugas;
    private String jenis_kelamin;
    private String alamat_petugas;
    private String no_hp_petugas;
    private String jabatan;

    public GetDataPetugas(String noidentitas_petugas, String nama_petugas, String jenis_kelamin, String alamat_petugas, String no_hp_petugas, String jabatan) {
        this.noidentitas_petugas=noidentitas_petugas;
        this.nama_petugas=nama_petugas;
        this.jenis_kelamin=jenis_kelamin;
        this.alamat_petugas=alamat_petugas;
        this.no_hp_petugas=no_hp_petugas;
        this.jabatan=jabatan;
    }

    public String getNoidentitasPetugas() {
        return noidentitas_petugas;
    }

    public String getNamaPetugas() {
        return nama_petugas;
    }

    public String getJenisKelamin() {
        return jenis_kelamin;
    }

    public String getAlamatPetugas() {
        return alamat_petugas;
    }
    public String getNoHpPetugas() {
        return no_hp_petugas;
    }
    public String getjabatan() {
        return jabatan;
    }
}



