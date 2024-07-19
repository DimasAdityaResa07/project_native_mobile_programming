<?php
include '../koneksi/koneksi.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $noidentitas_petugas = $_POST['noidentitas_petugas'];

    $sql = "SELECT * FROM petugas WHERE noidentitas_petugas = '$noidentitas_petugas'";
    $result = mysqli_query($db, $sql);

    if (mysqli_num_rows($result) > 0) {
        echo "no_identitas_sudah_ada";
    } else {
        echo "no_identitas_belum_ada";
    }
}
?>