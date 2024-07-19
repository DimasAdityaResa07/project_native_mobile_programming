<?php
include '../koneksi/koneksi.php';

if (isset($_POST['no_plat'])) {
    $no_plat=$_POST['no_plat'];

    $sql = "DELETE FROM kendaraan WHERE no_plat = '$no_plat'";
    $query = mysqli_query($db, $sql);

    if ($query) {
        echo json_encode(array('status' => 'success', 'message' => 'Data berhasil dihapus'));
    } else {
        echo json_encode(array('status' => 'error', 'message' => 'Gagal menghapus data: ' . mysqli_error($db)));
    }
}
