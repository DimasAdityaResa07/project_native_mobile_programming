<?php
include'../koneksi/koneksi.php';

$sql = "SELECT * FROM kategori_laporan";
$query = mysqli_query($db, $sql);

if (!$query) {
    echo json_encode(array('status' => 'error', 'message' => 'Query failed: ' . mysqli_error($db)));
    exit;
}

$list_data = array();

while ($data = mysqli_fetch_assoc($query)) {
    $list_data[] = array(
        'id_kategori' => $data['id_kategori'],
        'nama_kategori' => $data['nama_kategori'],
        'keterangan' => $data['keterangan']
    );
}

header('Content-Type: application/json');

echo json_encode(array('data' => $list_data));

