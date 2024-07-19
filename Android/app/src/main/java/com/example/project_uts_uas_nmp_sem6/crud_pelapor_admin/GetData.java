package com.example.project_uts_uas_nmp_sem6.crud_pelapor_admin;

import java.io.Serializable;

public class GetData implements Serializable {
    private String no_identitas_pelapor;
    private String nama_pelapor;
    private String no_hp_pelapor;
    private String alamat;
    private String email;

    public GetData(String no_identitas_pelapor, String nama_pelapor, String no_hp_pelapor, String alamat, String email) {
        this.no_identitas_pelapor=no_identitas_pelapor;
        this.nama_pelapor=nama_pelapor;
        this.no_hp_pelapor=no_hp_pelapor;
        this.alamat=alamat;
        this.email=email;
    }

    public String getNo_identitas_pelapor() {
        return no_identitas_pelapor;
    }

    public String getNamaPelapor() {
        return nama_pelapor;
    }

    public String getNo_hp_pelapor() {
        return no_hp_pelapor;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getEmail() {
        return email;
    }
}



