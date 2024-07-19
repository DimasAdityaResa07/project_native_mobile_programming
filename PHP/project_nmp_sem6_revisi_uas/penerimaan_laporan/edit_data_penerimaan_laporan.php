<?php
include '../koneksi/koneksi.php';

header('Content-Type: application/json');

$id_laporan = isset($_POST['id_laporan']) ? $_POST['id_laporan'] : '';
$no_identitas_pelapor = isset($_POST['no_identitas_pelapor']) ? $_POST['no_identitas_pelapor'] : '';
$lokasi_kejadian = isset($_POST['lokasi_kejadian']) ? $_POST['lokasi_kejadian'] : '';
$waktu_laporan = isset($_POST['waktu_laporan']) ? $_POST['waktu_laporan'] : '';
$id_kategori = isset($_POST['id_kategori']) ? $_POST['id_kategori'] : '';
$deskripsi_kejadian = isset($_POST['deskripsi_kejadian']) ? $_POST['deskripsi_kejadian'] : '';
$status_laporan = isset($_POST['status_laporan']) ? $_POST['status_laporan'] : '';

$sql = "UPDATE penerimaan_laporan SET no_identitas_pelapor='" . $no_identitas_pelapor . "', 
        lokasi_kejadian='" . $lokasi_kejadian . "', 
        waktu_laporan='" . $waktu_laporan . "' , 
        id_kategori='" . $id_kategori . "',
        deskripsi_kejadian='" . $deskripsi_kejadian . "',
        status_laporan='" . $status_laporan . "'
        WHERE id_laporan='" . $id_laporan . "'";

$query = mysqli_query($db, $sql);

if ($query) {
    echo json_encode(array('status' => 'data_teredit'));
} else {
    echo json_encode(array('status' => 'gagal_teredit'));
}

