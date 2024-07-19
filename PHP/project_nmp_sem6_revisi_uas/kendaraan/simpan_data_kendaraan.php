<?php

include '../koneksi/koneksi.php';

header('Content-Type: application/json');

if (isset($_POST['no_plat'], $_POST['merk'], $_POST['type'], $_POST['jenis'], $_POST['tanggal_stnk'], $_POST['status_kendaraan'])) {

    $no_plat = $_POST['no_plat'];
    $merk = $_POST['merk'];
    $type = $_POST['type'];
    $jenis = $_POST['jenis'];
    $tanggal_stnk = $_POST['tanggal_stnk'];
    $status_kendaraan = $_POST['status_kendaraan'];

    $sql = "INSERT INTO kendaraan (no_plat,merk,type,jenis,tanggal_stnk,status_kendaraan) 
            VALUES ('$no_plat', '$merk', '$type', '$jenis', '$tanggal_stnk', '$status_kendaraan')";

    $query = mysqli_query($db, $sql);

    if ($query) {
        echo json_encode(array('status' => 'data_tersimpan', 'no_plat' => $no_plat));
    } else {
        echo json_encode(array('status' => 'gagal_tersimpan', 'error' => mysqli_error($db)));
    }

} else {
    echo json_encode(array('status' => 'data_tidak_lengkap', 'received' => $_POST));
}

mysqli_close($db);
