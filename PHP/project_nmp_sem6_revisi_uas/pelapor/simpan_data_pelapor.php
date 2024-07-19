<?php

include '../koneksi/koneksi.php';

header('Content-Type: application/json');

if (isset($_POST['no_identitas_pelapor'], $_POST['nama_pelapor'], $_POST['no_hp_pelapor'], $_POST['alamat'], $_POST['email'])) {

    $no_identitas_pelapor = $_POST['no_identitas_pelapor'];
    $nama_pelapor = $_POST['nama_pelapor'];
    $no_hp_pelapor = $_POST['no_hp_pelapor'];
    $alamat = $_POST['alamat'];
    $email = $_POST['email'];

    $sql = "INSERT INTO pelapor (no_identitas_pelapor, nama_pelapor, no_hp_pelapor, alamat, email) VALUES
            ('$no_identitas_pelapor', '$nama_pelapor', '$no_hp_pelapor', '$alamat', '$email')";

    $query = mysqli_query($db, $sql);

    if ($query) {
        echo json_encode(array('status' => 'data_tersimpan', 'alamat' => $alamat));
    } else {
        echo json_encode(array('status' => 'gagal_tersimpan', 'error' => mysqli_error($db)));
    }

} else {
    echo json_encode(array('status' => 'data_tidak_lengkap', 'received' => $_POST));
}

mysqli_close($db);
