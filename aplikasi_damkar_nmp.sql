-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Jul 19, 2024 at 11:04 AM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `aplikasi_damkar_nmp`
--

-- --------------------------------------------------------

--
-- Table structure for table `kategori_laporan`
--

CREATE TABLE `kategori_laporan` (
  `id_kategori` int(11) NOT NULL,
  `nama_kategori` varchar(100) NOT NULL,
  `keterangan` varchar(120) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `kategori_laporan`
--

INSERT INTO `kategori_laporan` (`id_kategori`, `nama_kategori`, `keterangan`) VALUES
(6, 'Rescue Hewan', 'Barang yang basa digunakan adalah, tangga, safety toools, dsb'),
(7, 'Kebakaran Rumah', 'Kebakaran yang biasanya terjadi akibat ledakan atau pun kecerobohan terhadap barang-barang yang ada pada rumah.');

-- --------------------------------------------------------

--
-- Table structure for table `kendaraan`
--

CREATE TABLE `kendaraan` (
  `no_plat` varchar(30) NOT NULL,
  `merk` varchar(50) NOT NULL,
  `type` varchar(50) NOT NULL,
  `jenis` varchar(100) NOT NULL,
  `tanggal_stnk` date NOT NULL,
  `status_kendaraan` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `kendaraan`
--

INSERT INTO `kendaraan` (`no_plat`, `merk`, `type`, `jenis`, `tanggal_stnk`, `status_kendaraan`) VALUES
('BM 370 DW', 'Mitsubishi', 'FL4GLZ', 'Water Canon', '2024-04-10', 'Aktif'),
('BM 43 ZF', 'Mitsubishi', 'FD33LD', 'Pump Truck', '2024-06-06', 'Aktif');

-- --------------------------------------------------------

--
-- Table structure for table `pelapor`
--

CREATE TABLE `pelapor` (
  `no_identitas_pelapor` varchar(30) NOT NULL,
  `nama_pelapor` varchar(50) NOT NULL,
  `no_hp_pelapor` varchar(20) NOT NULL,
  `alamat` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pelapor`
--

INSERT INTO `pelapor` (`no_identitas_pelapor`, `nama_pelapor`, `no_hp_pelapor`, `alamat`, `email`) VALUES
('123', 'Dimas Aditya Resa', '0812681212', 'Bangkinang', 'dimas@dimas'),
('124', 'Nepla Awang', '093240812', 'Rohul', 'awang@awang'),
('129', 'Alan Andika Pradana', '0812732873', 'Jl. Melati, Rohul', 'alan@alan');

-- --------------------------------------------------------

--
-- Table structure for table `penanganan_laporan`
--

CREATE TABLE `penanganan_laporan` (
  `id_penanganan` int(11) NOT NULL,
  `id_laporan` int(11) NOT NULL,
  `no_plat` varchar(30) NOT NULL,
  `noidentitas_petugas` varchar(50) NOT NULL,
  `deskripsi_penanganan` varchar(150) NOT NULL,
  `catatan_tindak_lanjut` varchar(150) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `penanganan_laporan`
--

INSERT INTO `penanganan_laporan` (`id_penanganan`, `id_laporan`, `no_plat`, `noidentitas_petugas`, `deskripsi_penanganan`, `catatan_tindak_lanjut`) VALUES
(51, 27, 'BM 43 ZF', '123', 'Memanjat tangga dan memotong ranting yang membuat kucing tersangkut', 'Membawa kucing ke tempat perawatan hewan');

-- --------------------------------------------------------

--
-- Table structure for table `penerimaan_laporan`
--

CREATE TABLE `penerimaan_laporan` (
  `id_laporan` int(11) NOT NULL,
  `no_identitas_pelapor` varchar(30) NOT NULL,
  `lokasi_kejadian` varchar(80) NOT NULL,
  `waktu_laporan` datetime NOT NULL DEFAULT current_timestamp(),
  `id_kategori` int(11) NOT NULL,
  `deskripsi_kejadian` varchar(100) NOT NULL,
  `status_laporan` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `penerimaan_laporan`
--

INSERT INTO `penerimaan_laporan` (`id_laporan`, `no_identitas_pelapor`, `lokasi_kejadian`, `waktu_laporan`, `id_kategori`, `deskripsi_kejadian`, `status_laporan`) VALUES
(27, '123', 'Bangkinang', '2024-07-19 10:04:20', 6, 'Terdapat Kucing yang tersangkut di atas pohon', 'Selesai Dikerjakan'),
(28, '124', 'Rimbo Panjang', '2024-07-19 10:05:01', 7, 'Disebabkan Kebocoran Tabung Gas', 'Sedang Diproses');

-- --------------------------------------------------------

--
-- Table structure for table `petugas`
--

CREATE TABLE `petugas` (
  `noidentitas_petugas` varchar(50) NOT NULL,
  `nama_petugas` varchar(50) NOT NULL,
  `jenis_kelamin` varchar(50) NOT NULL,
  `alamat_petugas` varchar(100) NOT NULL,
  `no_hp_petugas` varchar(20) NOT NULL,
  `jabatan` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `petugas`
--

INSERT INTO `petugas` (`noidentitas_petugas`, `nama_petugas`, `jenis_kelamin`, `alamat_petugas`, `no_hp_petugas`, `jabatan`) VALUES
('123', 'Rahel', 'Perempuan', 'Pekanbaru', '08128793123', 'Anggota Regu'),
('173910', 'Amin', 'Laki-Laki', 'Jl. Melur, Kampar', '0812391011', 'Anggota Regu');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `kategori_laporan`
--
ALTER TABLE `kategori_laporan`
  ADD PRIMARY KEY (`id_kategori`);

--
-- Indexes for table `kendaraan`
--
ALTER TABLE `kendaraan`
  ADD PRIMARY KEY (`no_plat`);

--
-- Indexes for table `pelapor`
--
ALTER TABLE `pelapor`
  ADD PRIMARY KEY (`no_identitas_pelapor`);

--
-- Indexes for table `penanganan_laporan`
--
ALTER TABLE `penanganan_laporan`
  ADD PRIMARY KEY (`id_penanganan`),
  ADD KEY `id_laporan` (`id_laporan`),
  ADD KEY `no_plat` (`no_plat`),
  ADD KEY `noidentitas_petugas` (`noidentitas_petugas`);

--
-- Indexes for table `penerimaan_laporan`
--
ALTER TABLE `penerimaan_laporan`
  ADD PRIMARY KEY (`id_laporan`),
  ADD KEY `no_identitas_pelapor` (`no_identitas_pelapor`),
  ADD KEY `id_kategori` (`id_kategori`);

--
-- Indexes for table `petugas`
--
ALTER TABLE `petugas`
  ADD PRIMARY KEY (`noidentitas_petugas`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `kategori_laporan`
--
ALTER TABLE `kategori_laporan`
  MODIFY `id_kategori` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `penanganan_laporan`
--
ALTER TABLE `penanganan_laporan`
  MODIFY `id_penanganan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=52;

--
-- AUTO_INCREMENT for table `penerimaan_laporan`
--
ALTER TABLE `penerimaan_laporan`
  MODIFY `id_laporan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `penanganan_laporan`
--
ALTER TABLE `penanganan_laporan`
  ADD CONSTRAINT `penanganan_laporan_ibfk_1` FOREIGN KEY (`no_plat`) REFERENCES `kendaraan` (`no_plat`),
  ADD CONSTRAINT `penanganan_laporan_ibfk_2` FOREIGN KEY (`noidentitas_petugas`) REFERENCES `petugas` (`noidentitas_petugas`) ON UPDATE CASCADE,
  ADD CONSTRAINT `penanganan_laporan_ibfk_3` FOREIGN KEY (`id_laporan`) REFERENCES `penerimaan_laporan` (`id_laporan`) ON UPDATE CASCADE;

--
-- Constraints for table `penerimaan_laporan`
--
ALTER TABLE `penerimaan_laporan`
  ADD CONSTRAINT `penerimaan_laporan_ibfk_1` FOREIGN KEY (`no_identitas_pelapor`) REFERENCES `pelapor` (`no_identitas_pelapor`) ON UPDATE CASCADE,
  ADD CONSTRAINT `penerimaan_laporan_ibfk_2` FOREIGN KEY (`id_kategori`) REFERENCES `kategori_laporan` (`id_kategori`) ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
