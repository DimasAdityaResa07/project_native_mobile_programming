<?php
include '../koneksi/koneksi.php'; 
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $no_plat = $_POST['no_plat'];

    $sql = "SELECT * FROM kendaraan WHERE no_plat = '$no_plat'"; 
    $result = mysqli_query($db, $sql);

    if (mysqli_num_rows($result) > 0) {
        echo "no_plat_sudah_ada";
    } else {
        echo "no_plat_belum_ada";
    }
}
?>