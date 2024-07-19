<?php
include '../koneksi/koneksi.php';

header('Content-Type: application/json');

$no_identitas_pelapor = isset($_POST['no_identitas_pelapor']) ? $_POST['no_identitas_pelapor'] : '';
$nama_pelapor = isset($_POST['nama_pelapor']) ? $_POST['nama_pelapor'] : '';
$alamat = isset($_POST['alamat']) ? $_POST['alamat'] : '';
$no_hp_pelapor = isset($_POST['no_hp_pelapor']) ? $_POST['no_hp_pelapor'] : '';
$email = isset($_POST['email']) ? $_POST['email'] : '';

$sql = "UPDATE pelapor SET nama_pelapor='" . $nama_pelapor . "', 
        no_hp_pelapor='" . $no_hp_pelapor . "' , 
        alamat='" . $alamat . "', 
        email='" . $email . "'
        WHERE no_identitas_pelapor='" . $no_identitas_pelapor . "'";

$query = mysqli_query($db, $sql);

if ($query) {
    echo json_encode(array('status' => 'data_teredit'));
} else {
    echo json_encode(array('status' => 'gagal_teredit'));
}

