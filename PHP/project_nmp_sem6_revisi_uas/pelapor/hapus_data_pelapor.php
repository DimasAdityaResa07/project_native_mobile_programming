<?php
include '../koneksi/koneksi.php';

if (isset($_POST['no_identitas_pelapor'])) {
    $no_identitas_pelapor=$_POST['no_identitas_pelapor'];

    $sql = "DELETE FROM pelapor WHERE no_identitas_pelapor = '$no_identitas_pelapor'";
    $query = mysqli_query($db, $sql);

    if ($query) {
        echo json_encode(array('status' => 'success', 'message' => 'Data berhasil dihapus'));
    } else {
        echo json_encode(array('status' => 'error', 'message' => 'Gagal menghapus data: ' . mysqli_error($db)));
    }
}
