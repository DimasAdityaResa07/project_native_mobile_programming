<?php
include'../koneksi/koneksi.php';

$sql = "SELECT * FROM petugas";
$query = mysqli_query($db, $sql);

if (!$query) {
    echo json_encode(array('status' => 'error', 'message' => 'Query failed: ' . mysqli_error($db)));
    exit;
}

$list_data = array();

while ($data = mysqli_fetch_assoc($query)) {
    $list_data[] = array(
        'noidentitas_petugas' => $data['noidentitas_petugas'],
        'nama_petugas' => $data['nama_petugas'],
        'jenis_kelamin' => $data['jenis_kelamin'],
        'alamat_petugas' => $data['alamat_petugas'],
        'no_hp_petugas' => $data['no_hp_petugas'],
        'jabatan' => $data['jabatan']
    );
}

header('Content-Type: application/json');

echo json_encode(array('data' => $list_data));

