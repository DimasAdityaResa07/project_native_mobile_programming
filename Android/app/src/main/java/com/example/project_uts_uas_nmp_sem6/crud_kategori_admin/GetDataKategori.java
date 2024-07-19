package com.example.project_uts_uas_nmp_sem6.crud_kategori_admin;

import java.io.Serializable;

public class GetDataKategori implements Serializable {
    private String id_kategori;
    private String nama_kategori;
    private String keterangan;

    public GetDataKategori(String id_kategori, String nama_kategori, String keterangan) {
        this.id_kategori=id_kategori;
        this.nama_kategori=nama_kategori;
        this.keterangan=keterangan;

    }

    public String getId_kategori() {
        return id_kategori;
    }

    public String getNama_kategori() {
        return nama_kategori;
    }

    public String getKeterangan() {
        return keterangan;
    }

}



