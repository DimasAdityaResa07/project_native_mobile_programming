<?php
include '../koneksi/koneksi.php';

header('Content-Type: application/json');

$id_penanganan = isset($_POST['id_penanganan']) ? $_POST['id_penanganan'] : '';
$id_laporan = isset($_POST['id_laporan']) ? $_POST['id_laporan'] : '';
$no_plat = isset($_POST['no_plat']) ? $_POST['no_plat'] : '';
$noidentitas_petugas = isset($_POST['noidentitas_petugas']) ? $_POST['noidentitas_petugas'] : '';
$deskripsi_penanganan = isset($_POST['deskripsi_penanganan']) ? $_POST['deskripsi_penanganan'] : '';
$catatan_tindak_lanjut = isset($_POST['catatan_tindak_lanjut']) ? $_POST['catatan_tindak_lanjut'] : '';

$sql = "UPDATE penanganan_laporan SET id_laporan='" . $id_laporan . "', 
        no_plat='" . $no_plat . "', 
        noidentitas_petugas='" . $noidentitas_petugas . "' , 
        deskripsi_penanganan='" . $deskripsi_penanganan . "',
        catatan_tindak_lanjut='" . $catatan_tindak_lanjut . "'
        WHERE id_penanganan='" . $id_penanganan . "'";

$query = mysqli_query($db, $sql);

if ($query) {
    echo json_encode(array('status' => 'data_teredit'));
} else {
    echo json_encode(array('status' => 'gagal_teredit'));
}

