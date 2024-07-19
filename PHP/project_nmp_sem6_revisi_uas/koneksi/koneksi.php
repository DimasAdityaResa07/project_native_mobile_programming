<?php
$server = "localhost";
$user = "root";
$password = "";
$nama_db = "aplikasi_damkar_nmp";

// Membuat koneksi ke database
$db = mysqli_connect($server, $user, $password, $nama_db);

// Memeriksa apakah koneksi berhasil
if (!$db) {
    die("Koneksi gagal: " . mysqli_connect_error());
}
