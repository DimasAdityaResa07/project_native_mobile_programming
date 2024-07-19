<?php
include '../koneksi/koneksi.php';

if (isset($_POST['noidentitas_petugas'])) {
    $noidentitas_petugas=$_POST['noidentitas_petugas'];

    $sql = "DELETE FROM petugas WHERE noidentitas_petugas = '$noidentitas_petugas'";
    $query = mysqli_query($db, $sql);

    if ($query) {
        echo json_encode(array('status' => 'success', 'message' => 'Data berhasil dihapus'));
    } else {
        echo json_encode(array('status' => 'error', 'message' => 'Gagal menghapus data: ' . mysqli_error($db)));
    }
}
