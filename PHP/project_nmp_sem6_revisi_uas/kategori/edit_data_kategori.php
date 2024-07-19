<?php
include '../koneksi/koneksi.php';

header('Content-Type: application/json');

$id_kategori = isset($_POST['id_kategori']) ? $_POST['id_kategori'] : '';
$nama_kategori = isset($_POST['nama_kategori']) ? $_POST['nama_kategori'] : '';
$keterangan = isset($_POST['keterangan']) ? $_POST['keterangan'] : '';


$sql = "UPDATE kategori_laporan SET nama_kategori='" . $nama_kategori . "', 
        keterangan='" . $keterangan . "'
        WHERE id_kategori='" . $id_kategori . "'";

$query = mysqli_query($db, $sql);

if ($query) {
    echo json_encode(array('status' => 'data_teredit'));
} else {
    echo json_encode(array('status' => 'gagal_teredit'));
}

