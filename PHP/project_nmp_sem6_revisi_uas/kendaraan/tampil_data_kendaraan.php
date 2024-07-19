<?php
include'../koneksi/koneksi.php';

$sql = "SELECT * FROM kendaraan";
$query = mysqli_query($db, $sql);

if (!$query) {
    echo json_encode(array('status' => 'error', 'message' => 'Query failed: ' . mysqli_error($db)));
    exit;
}

$list_data = array();

while ($data = mysqli_fetch_assoc($query)) {
    $list_data[] = array(
        'no_plat' => $data['no_plat'],
        'merk' => $data['merk'],
        'type' => $data['type'],
        'jenis' => $data['jenis'],
        'tanggal_stnk' => $data['tanggal_stnk'],
        'status_kendaraan' => $data['status_kendaraan']
    );
}

header('Content-Type: application/json');

echo json_encode(array('data' => $list_data));

