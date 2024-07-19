<?php
include '../koneksi/koneksi.php';

header('Content-Type: application/json');

$noidentitas_petugas = isset($_POST['noidentitas_petugas']) ? $_POST['noidentitas_petugas'] : '';
$nama_petugas = isset($_POST['nama_petugas']) ? $_POST['nama_petugas'] : '';
$jenis_kelamin = isset($_POST['jenis_kelamin']) ? $_POST['jenis_kelamin'] : '';
$alamat_petugas = isset($_POST['alamat_petugas']) ? $_POST['alamat_petugas'] : '';
$no_hp_petugas = isset($_POST['no_hp_petugas']) ? $_POST['no_hp_petugas'] : '';
$jabatan = isset($_POST['jabatan']) ? $_POST['jabatan'] : '';

$sql = "UPDATE petugas SET nama_petugas='" . $nama_petugas . "', 
        jenis_kelamin='" . $jenis_kelamin . "', 
        alamat_petugas='" . $alamat_petugas . "' , 
        no_hp_petugas='" . $no_hp_petugas . "',
        jabatan='" . $jabatan . "'
        WHERE noidentitas_petugas='" . $noidentitas_petugas . "'";

$query = mysqli_query($db, $sql);

if ($query) {
    echo json_encode(array('status' => 'data_teredit'));
} else {
    echo json_encode(array('status' => 'gagal_teredit'));
}

