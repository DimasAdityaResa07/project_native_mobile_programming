<?php
include '../koneksi/koneksi.php';

if (isset($_POST['id_penanganan'])) {
    $id_penanganan = $_POST['id_penanganan'];

    // Ambil id_laporan sebelum menghapus data -- GPT
    $sql_get_laporan = "SELECT id_laporan FROM penanganan_laporan WHERE id_penanganan = '$id_penanganan'";
    $result_get_laporan = mysqli_query($db, $sql_get_laporan);

    if ($result_get_laporan && mysqli_num_rows($result_get_laporan) > 0) {
        $row = mysqli_fetch_assoc($result_get_laporan);
        $id_laporan = $row['id_laporan'];

        // Hapus data dari penanganan_laporan -- GPT
        $sql_delete = "DELETE FROM penanganan_laporan WHERE id_penanganan = '$id_penanganan'";
        $query_delete = mysqli_query($db, $sql_delete);

        if ($query_delete) {
            // Cek apakah id_laporan masih ada di penanganan_laporan -- GPT
            $sql_check_laporan = "SELECT * FROM penanganan_laporan WHERE id_laporan = '$id_laporan'";
            $result_check_laporan = mysqli_query($db, $sql_check_laporan);

            if ($result_check_laporan && mysqli_num_rows($result_check_laporan) == 0) {
                // Update status_laporan di penerimaan_laporan --GPT
                $sql_update_status = "UPDATE penerimaan_laporan SET status_laporan = 'Sedang Diproses' WHERE id_laporan = '$id_laporan'";
                $query_update_status = mysqli_query($db, $sql_update_status);

                if ($query_update_status) {
                    echo json_encode(array('status' => 'success', 'message' => 'Data berhasil dihapus dan status laporan diperbarui'));
                } else {
                    echo json_encode(array('status' => 'error', 'message' => 'Gagal memperbarui status laporan: ' . mysqli_error($db)));
                }
            } else {
                echo json_encode(array('status' => 'success', 'message' => 'Data berhasil dihapus'));
            }
        } else {
            echo json_encode(array('status' => 'error', 'message' => 'Gagal menghapus data: ' . mysqli_error($db)));
        }
    } else {
        echo json_encode(array('status' => 'error', 'message' => 'ID penanganan tidak ditemukan'));
    }
} else {
    echo json_encode(array('status' => 'error', 'message' => 'ID penanganan tidak diberikan'));
}
?>