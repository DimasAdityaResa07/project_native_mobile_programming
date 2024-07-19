<?php
include '../koneksi/koneksi.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $no_plat = $_POST['no_plat'];

    $sql = "SELECT * FROM penanganan_laporan WHERE no_plat = '$no_plat'";
    $result = mysqli_query($db, $sql);

    if (mysqli_num_rows($result) > 0) {
        echo "ada_penanganan";
    } else {
        echo "tidak_ada_penanganan";
    }
}
?>