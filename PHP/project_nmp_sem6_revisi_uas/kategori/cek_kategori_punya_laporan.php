<?php
include '../koneksi/koneksi.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $id_kategori = $_POST['id_kategori'];

    $sql = "SELECT * FROM penerimaan_laporan WHERE id_kategori = '$id_kategori'";
    $result = mysqli_query($db, $sql);

    if (mysqli_num_rows($result) > 0) {
        echo "ada_laporan";
    } else {
        echo "tidak_ada_laporan";
    }
}
?>