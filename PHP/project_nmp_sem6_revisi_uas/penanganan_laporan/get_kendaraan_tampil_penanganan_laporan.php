<?php

include '../koneksi/koneksi.php';

$no_plat = $_POST['no_plat'];

$sql = "SELECT 
pnl.id_penanganan,
pnl.id_laporan,
pl.lokasi_kejadian,
k.no_plat,
pnl.noidentitas_petugas,
p.nama_petugas,
pnl.deskripsi_penanganan,
pnl.catatan_tindak_lanjut

FROM penanganan_laporan pnl
INNER JOIN penerimaan_laporan pl ON pl.id_laporan = pnl.id_laporan
INNER JOIN kendaraan k ON k.no_plat = pnl.no_plat
INNER JOIN petugas p ON p.noidentitas_petugas = pnl.noidentitas_petugas 
WHERE k.no_plat = '$no_plat' ORDER BY pnl.no_plat DESC;
";

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
