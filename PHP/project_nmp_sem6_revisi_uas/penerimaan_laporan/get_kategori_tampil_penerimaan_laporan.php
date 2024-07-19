<?php

include '../koneksi/koneksi.php';

$id_kategori = $_POST['id_kategori'];

$sql = "SELECT 
pl.id_laporan, 
pl.no_identitas_pelapor, 
p.nama_pelapor, 
pl.lokasi_kejadian, 
pl.waktu_laporan, 
pl.id_kategori, 
kl.nama_kategori,
pl.deskripsi_kejadian, 
pl.status_laporan 
FROM penerimaan_laporan pl
INNER JOIN pelapor p ON pl.no_identitas_pelapor = p.no_identitas_pelapor
INNER JOIN kategori_laporan kl ON pl.id_kategori = kl.id_kategori WHERE pl.id_kategori='$id_kategori'
ORDER BY pl.waktu_laporan DESC ";

// FROM penerimaan_laporan pl -> Tulis Nama Table lenkap nya kemudian tuliskan inisialnya
// INNER JOIN pelapor p -> Tulis Nama Table lenkap nya kemudian tuliskan inisialnya sama dengan tadi  //DIMAS

$result = $db->query($sql);

$data = array();
if ($result->num_rows > 0) {
    while($row = $result->fetch_assoc()) {
        $data[] = $row;
    }
} 


echo json_encode(array('data' => $data));

$db->close();
?>
