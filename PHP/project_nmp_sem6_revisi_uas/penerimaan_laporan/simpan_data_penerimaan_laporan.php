<?php

include '../koneksi/koneksi.php';

header('Content-Type: application/json');

if (isset($_POST['no_identitas_pelapor'], $_POST['lokasi_kejadian'], $_POST['id_kategori'],$_POST['deskripsi_kejadian'], $_POST['status_laporan'])) {

    // $id_laporan = $_POST['id_laporan'];
    $no_identitas_pelapor = $_POST['no_identitas_pelapor'];
    $lokasi_kejadian = $_POST['lokasi_kejadian'];
    // $waktu_laporan = $_POST['waktu_laporan'];
    $id_kategori = $_POST['id_kategori'];
    $deskripsi_kejadian = $_POST['deskripsi_kejadian'];
    $status_laporan=$_POST['status_laporan'];

    $sql = "INSERT INTO penerimaan_laporan (id_laporan, no_identitas_pelapor, lokasi_kejadian, waktu_laporan, id_kategori,deskripsi_kejadian,status_laporan) VALUES
            (NULL, '$no_identitas_pelapor', '$lokasi_kejadian', current_timestamp(), '$id_kategori', '$deskripsi_kejadian', '$status_laporan')";

    $query = mysqli_query($db, $sql);

    if ($query) {
        echo json_encode(array('status' => 'data_tersimpan', 'id_laporan' => $id_laporan));
    } else {
        echo json_encode(array('status' => 'gagal_tersimpan', 'error' => mysqli_error($db)));
    }

} else {
    echo json_encode(array('status' => 'data_tidak_lengkap', 'received' => $_POST));
}

mysqli_close($db);
