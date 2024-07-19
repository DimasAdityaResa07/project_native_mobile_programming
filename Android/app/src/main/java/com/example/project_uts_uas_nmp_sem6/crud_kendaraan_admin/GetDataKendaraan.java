package com.example.project_uts_uas_nmp_sem6.crud_kendaraan_admin;

import java.io.Serializable;

public class GetDataKendaraan implements Serializable {
    private String no_plat;
    private String merk;
    private String type;
    private String jenis;
    private String tanggal_stnk;
    private String status_kendaraan;

    public GetDataKendaraan(String no_plat,String merk,String type, String jenis, String tanggal_stnk, String status_kendaraan) {
        this.no_plat=no_plat;
        this.merk=merk;
        this.type=type;
        this.jenis=jenis;
        this.tanggal_stnk=tanggal_stnk;
        this.status_kendaraan=status_kendaraan;

    }

    public String getNo_plat() {
        return no_plat;
    }

    public String getMerk() {
        return merk;
    }

    public String getType() {
        return type;
    }

    public String getJenis() {
        return jenis;
    }

    public String getTanggal_stnk() {
        return tanggal_stnk;
    }

    public String getStatus_kendaraan() {
        return status_kendaraan;
    }
}



