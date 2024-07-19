<?php
include '../koneksi/koneksi.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $id_laporan = $_POST['id_laporan'];

    $sql = "SELECT * FROM penanganan_laporan WHERE id_laporan = '$id_laporan'";
    $result = mysqli_query($db, $sql);

    if (mysqli_num_rows($result) > 0) {
        echo "ada_penanganan";
    } else {
        echo "tidak_ada_penanganan";
    }
}
?>