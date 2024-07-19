<?php

include '../koneksi/koneksi.php';

header('Content-Type: application/json');

if (isset($_POST['id_laporan'], $_POST['no_plat'], $_POST['noidentitas_petugas'],$_POST['deskripsi_penanganan'], $_POST['catatan_tindak_lanjut'])) {

    $id_laporan = $_POST['id_laporan'];
    $no_plat = $_POST['no_plat'];
    $noidentitas_petugas = $_POST['noidentitas_petugas'];
    $deskripsi_penanganan = $_POST['deskripsi_penanganan'];
    $catatan_tindak_lanjut=$_POST['catatan_tindak_lanjut'];

    $sqlcek_id_laporan = "SELECT * FROM penerimaan_laporan WHERE id_laporan = $id_laporan";
    $result = mysqli_query($db, $sqlcek_id_laporan);

    if($result->num_rows >0){
    $insertsql = "INSERT INTO penanganan_laporan (id_penanganan, id_laporan, no_plat, noidentitas_petugas,deskripsi_penanganan,catatan_tindak_lanjut) VALUES
            (NULL, '$id_laporan', '$no_plat', '$noidentitas_petugas', '$deskripsi_penanganan', '$catatan_tindak_lanjut')";
        if($db->query($insertsql)===TRUE){
            $update_sql = "UPDATE penerimaan_laporan SET status_laporan = 'Selesai Dikerjakan' where id_laporan = $id_laporan";
            if ($db->query($update_sql) === TRUE) {
                echo "Penanganan berhasil ditambahkan dan status laporan diupdate menjadi selesai";
        }else{
            echo "Error Update Data";
        }
        } else{
            echo "Error Insert Data";
        }
    }else{
        echo "ID Laporan Tidak Ditemukan";
    }
        $db->close();
    }
