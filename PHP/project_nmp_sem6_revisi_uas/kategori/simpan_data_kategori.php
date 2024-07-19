<?php

include '../koneksi/koneksi.php';

header('Content-Type: application/json');

if (isset($_POST['nama_kategori'], $_POST['keterangan'])) {

    $nama_kategori = $_POST['nama_kategori'];
    $keterangan = $_POST['keterangan'];

    $sql = "INSERT INTO kategori_laporan (id_kategori, nama_kategori, keterangan) VALUES
            (NULL, '$nama_kategori', '$keterangan')";

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
