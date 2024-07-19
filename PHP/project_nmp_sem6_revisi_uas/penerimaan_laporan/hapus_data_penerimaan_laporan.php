<?php
include '../koneksi/koneksi.php';

if (isset($_POST['id_laporan'])) {
    $id_laporan=$_POST['id_laporan'];

    $sql = "DELETE FROM penerimaan_laporan WHERE id_laporan = '$id_laporan'";
    $query = mysqli_query($db, $sql);

    if ($query) {
        echo json_encode(array('status' => 'success', 'message' => 'Data berhasil dihapus'));
    } else {
        echo json_encode(array('status' => 'error', 'message' => 'Gagal menghapus data: ' . mysqli_error($db)));
    }
}
