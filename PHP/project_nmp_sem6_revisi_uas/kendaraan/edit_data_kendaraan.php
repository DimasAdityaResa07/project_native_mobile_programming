<?php
include '../koneksi/koneksi.php';

header('Content-Type: application/json');

$no_plat = isset($_POST['no_plat']) ? $_POST['no_plat'] : '';
$merk = isset($_POST['merk']) ? $_POST['merk'] : '';
$type = isset($_POST['type']) ? $_POST['type'] : '';
$jenis = isset($_POST['jenis']) ? $_POST['jenis'] : '';
$tanggal_stnk = isset($_POST['tanggal_stnk']) ? $_POST['tanggal_stnk'] : '';
$status_kendaraan = isset($_POST['status_kendaraan']) ? $_POST['status_kendaraan'] : '';


if (!empty($no_plat)) {
    $sql = "UPDATE kendaraan SET merk='" . $merk . "', 
    type='" . $type . "', 
    jenis='" . $jenis . "' , 
    tanggal_stnk='" . $tanggal_stnk . "' ,
    status_kendaraan='" . $status_kendaraan . "' 
    WHERE no_plat='" . $no_plat . "'";
    
} else {
    $sql = "INSERT INTO kendaraan (no_plat,merk,type,jenis,tanggal_stnk,status_kendaraan) 
            VALUES ('$no_plat', '$merk', '$type', '$jenis', '$tanggal_stnk', '$status_kendaraan')";
}

$query = mysqli_query($db, $sql);

if ($query) {
    echo json_encode(array('status' => 'data_tersimpan'));
} else {
    echo json_encode(array('status' => 'gagal_tersimpan'));
}

