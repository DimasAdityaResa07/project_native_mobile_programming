<?php
include'../koneksi/koneksi.php';

$sql = "SELECT * FROM pelapor";
$query = mysqli_query($db, $sql);

if (!$query) {
    echo json_encode(array('status' => 'error', 'message' => 'Query failed: ' . mysqli_error($db)));
    exit;
}

$list_data = array();

while ($data = mysqli_fetch_assoc($query)) {
    $list_data[] = array(
        'no_identitas_pelapor' => $data['no_identitas_pelapor'],
        'nama_pelapor' => $data['nama_pelapor'],
        'no_hp_pelapor' => $data['no_hp_pelapor'],
        'alamat' => $data['alamat'],
        'email' => $data['email']
    );
}

header('Content-Type: application/json');

echo json_encode(array('data' => $list_data));

