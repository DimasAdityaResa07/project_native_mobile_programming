<?php
include '../koneksi/koneksi.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $no_identitas_pelapor = $_POST['no_identitas_pelapor'];

    $sql = "SELECT * FROM penerimaan_laporan WHERE no_identitas_pelapor = '$no_identitas_pelapor'";
    $result = mysqli_query($db, $sql);

    if (mysqli_num_rows($result) > 0) {
        echo "ada_laporan";
    } else {
        echo "tidak_ada_laporan";
    }
}
?>