<?php

include '../koneksi/koneksi.php';

header('Content-Type: application/json');

if (isset($_POST['noidentitas_petugas'], $_POST['nama_petugas'], $_POST['jenis_kelamin'], $_POST['alamat_petugas'], $_POST['no_hp_petugas'],$_POST['jabatan'])) {

    $noidentitas_petugas = $_POST['noidentitas_petugas'];
    $nama_petugas = $_POST['nama_petugas'];
    $jenis_kelamin = $_POST['jenis_kelamin'];
    $alamat_petugas = $_POST['alamat_petugas'];
    $no_hp_petugas = $_POST['no_hp_petugas'];
    $jabatan = $_POST['jabatan'];

    $sql = "INSERT INTO petugas (noidentitas_petugas, nama_petugas, jenis_kelamin, alamat_petugas, no_hp_petugas,jabatan) VALUES
            ('$noidentitas_petugas', '$nama_petugas', '$jenis_kelamin', '$alamat_petugas', '$no_hp_petugas', '$jabatan')";

    $query = mysqli_query($db, $sql);

    if ($query) {
        echo json_encode(array('status' => 'data_tersimpan', 'alamat_petugas' => $alamat_petugas));
    } else {
        echo json_encode(array('status' => 'gagal_tersimpan', 'error' => mysqli_error($db)));
    }

} else {
    echo json_encode(array('status' => 'data_tidak_lengkap', 'received' => $_POST));
}

mysqli_close($db);
