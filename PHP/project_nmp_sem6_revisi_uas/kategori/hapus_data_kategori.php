<?php
include '../koneksi/koneksi.php';

if (isset($_POST['id_kategori'])) {
    $id_kategori=$_POST['id_kategori'];

    $sql = "DELETE FROM kategori_laporan WHERE id_kategori = '$id_kategori'";
    $query = mysqli_query($db, $sql);

    if ($query) {
        echo json_encode(array('status' => 'success', 'message' => 'Data berhasil dihapus'));
    } else {
        echo json_encode(array('status' => 'error', 'message' => 'Gagal menghapus data: ' . mysqli_error($db)));
    }
}
