# TP4 DPBO 2025/2026 - Java Swing GUI PeopleData: Genshin Impact Character Roster

Tugas Praktikum 4 mata kuliah Desain dan Pemrograman Berorientasi Objek (DPBO), Program Studi Ilmu Komputer, Universitas Pendidikan Indonesia, semester genap 2025/2026.

---

## Janji Kejujuran Akademik

Saya Devindra Azra Dwi Putra dengan NIM 2509003 mengerjakan TP 4 dalam mata kuliah DPBO untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.

---

## Deskripsi Singkat

Program ini adalah aplikasi desktop Graphical User Interface (GUI) berbasis **Java Swing** dengan tema **PeopleData: Genshin Impact Character Roster**. Program ini menerapkan konsep manajemen data *in-memory* berorientasi objek yang memfasilitasi operasi **CRUD** (*Create, Read, Update, Delete*) secara interaktif.

Sesuai dengan pedoman teknis TP4, program ini memenuhi seluruh kriteria wajib dan perolehan bonus:
1. **Arsitektur Model-View-Controller (In-Memory)**:
   - `Person.java`: Model entitas karakter yang menerapkan enkapsulasi penuh (seluruh atribut private, konstruktor default, konstruktor berparameter, getter, dan setter).
   - `PeopleMenu.java`: Controller dan jendela utama (`JFrame`) yang mengelola komponen visual, logika event listener, validasi data, serta manipulasi koleksi `ArrayList<Person>`.
   - `PeopleMenu.form`: Layout antarmuka visual berbasis XML yang disinkronisasikan melalui plugin IntelliJ IDEA Swing UI Designer.
2. **4 Atribut Standar**:
   - `ID Karakter` (`id`): Pengenal unik karakter (misal: `GEN001`, `GEN002`).
   - `Nama Karakter` (`nama`): Nama lengkap karakter (misal: `Neuvillette`, `Furina`).
   - `Tahun Rilis` (`tahunLahir`): Tahun rilis resmi karakter ke dalam game (tipe `int` dengan validasi numerik).
   - `Role / Kategori` (`kategori`): Peran tempur karakter di dalam tim yang dipilih melalui `JComboBox` (`Main DPS`, `Sub-DPS`, `Support`).
3. **Atribut Tambahan Unik & Bonus Komponen (+20 Poin)**:
   - Atribut ke-5: `Tier Ranking` (`tier`) yang merepresentasikan peringkat meta karakter berdasarkan referensi situs **Game8**.
   - Komponen masukan untuk atribut ini **bukan berupa `JTextField`**, melainkan menggunakan **`JComboBox` kedua** dengan pilihan meta: `SS-Tier`, `S-Tier`, `A-Tier`, `B-Tier`, `C-Tier`.
4. **Dialog Konfirmasi Penghapusan (Confirmation Prompt Wajib)**:
   - Sebelum data dihapus, aplikasi memunculkan dialog konfirmasi (`JOptionPane.showConfirmDialog`) yang menyebutkan nama karakter secara spesifik. Data hanya terhapus jika pengguna memilih opsi *Yes*.
5. **Validasi Lengkap & Umpan Balik Responsif**:
   - Validasi kolom kosong sebelum penambahan/pembaruan.
   - Validasi nilai numerik pada tahun rilis (`NumberFormatException`).
   - Validasi pencegahan duplikasi ID karakter.
   - Pengubahan otomatis status tombol antara mode **Add** dan **Update**.
   - Tombol **Delete** yang secara dinamis disembunyikan dan hanya muncul saat baris tabel diklik.

---

## Desain Kelas & Komponen GUI

### 1. Struktur Kelas (Class Diagram)
```text
+-------------------------------------------------------------+
|                           Person                            |
+-------------------------------------------------------------+
| - id: String                                                |
| - nama: String                                              |
| - tahunLahir: int                                           |
| - kategori: String                                          |
| - tier: String                                              |
+-------------------------------------------------------------+
| + Person()                                                  |
| + Person(id: String, nama: String, tahunLahir: int,         |
|          kategori: String, tier: String)                    |
| + getId(): String / + setId(id: String): void               |
| + getNama(): String / + setNama(nama: String): void         |
| + getTahunLahir(): int / + setTahunLahir(thn: int): void    |
| + getKategori(): String / + setKategori(kat: String): void  |
| + getTier(): String / + setTier(tier: String): void         |
+-------------------------------------------------------------+
                              ▲
                              │ mengelola ArrayList<Person>
+-------------------------------------------------------------+
|                         PeopleMenu                          |
|                      (extends JFrame)                       |
+-------------------------------------------------------------+
| - mainPanel: JPanel                                         |
| - titleLabel: JLabel                                        |
| - idField: JTextField                                       |
| - namaField: JTextField                                     |
| - tahunField: JTextField                                    |
| - kategoriComboBox: JComboBox<String>                       |
| - tierComboBox: JComboBox<String>                           |
| - addUpdateButton: JButton                                  |
| - deleteButton: JButton                                     |
| - cancelButton: JButton                                     |
| - peopleTable: JTable                                       |
| - tableScrollPane: JScrollPane                              |
| - listPerson: ArrayList<Person>                             |
| - selectedIndex: int                                        |
+-------------------------------------------------------------+
| + PeopleMenu()                                              |
| - populateList(): void                                      |
| + setTable(): DefaultTableModel                             |
| + insertData(): void                                        |
| + updateData(): void                                        |
| + deleteData(): void                                        |
| + clearForm(): void                                         |
| + main(args: String[]): void                                |
+-------------------------------------------------------------+
```

### 2. Komponen GUI & Fungsinya
| Nama Komponen | Tipe Komponen | Fungsi & Perilaku |
| :--- | :--- | :--- |
| `mainPanel` | `JPanel` | Kontainer utama antarmuka dengan latar belakang putih dan padding rapi. |
| `titleLabel` | `JLabel` | Judul aplikasi dengan gaya font tebal (*Bold, 20pt*). |
| `idField` | `JTextField` | Masukan kode unik pengenal karakter. |
| `namaField` | `JTextField` | Masukan nama karakter game. |
| `tahunField` | `JTextField` | Masukan tahun rilis karakter (wajib angka integer). |
| `kategoriComboBox` | `JComboBox<String>` | Pilihan dropdown role tempur karakter (*Main DPS*, *Sub-DPS*, *Support*). |
| `tierComboBox` | `JComboBox<String>` | Pilihan dropdown peringkat meta Game8 (*SS-Tier*, *S-Tier*, *A-Tier*, *B-Tier*, *C-Tier*) — **Bonus +20**. |
| `addUpdateButton` | `JButton` | Tombol dwifungsi: bertuliskan **Add** pada mode tambah data, berganti menjadi **Update** saat baris tabel dipilih. |
| `deleteButton` | `JButton` | Tombol penghapusan data. Disembunyikan saat mode tambah dan dimunculkan saat baris tabel dipilih. |
| `cancelButton` | `JButton` | Tombol pembatalan pemilihan dan pengosongan seluruh formulir input. |
| `peopleTable` | `JTable` | Penampil seluruh data koleksi menggunakan `DefaultTableModel`. |
| `tableScrollPane` | `JScrollPane` | Panel pembungkus scrollable untuk tabel data. |

![Tata Letak & Struktur Komponen GUI](<Dokumentasi/Komponen GUI.png>)

---

## Alur Program (Flow & Logika CRUD)

1. **Inisialisasi & Pembacaan Data Awal (Read)**:
   - Saat aplikasi pertama kali dijalankan, konstruktor menginisialisasi `listPerson` dan memanggil `populateList()` untuk mengisi 10 karakter awal Genshin Impact yang akurat sesuai tier Game8.
   - Metode `setTable()` membangun `DefaultTableModel` yang berisi kolom `No`, `ID Karakter`, `Nama Karakter`, `Tahun Rilis`, `Role / Kategori`, dan `Tier Ranking`.
   - Tombol `deleteButton` disembunyikan secara default, dan `addUpdateButton` menampilkan teks "Add".

2. **Penambahan Data (Create - `insertData`)**:
   - Pengguna mengisi data pada form masukan dan memilih opsi pada kedua dropdown `JComboBox`.
   - Saat tombol **Add** ditekan:
     - Program memverifikasi apakah ada field teks yang kosong atau dropdown yang belum dipilih (masih bernilai `???`).
     - Program memeriksa apakah ID yang dimasukkan sudah pernah digunakan oleh karakter lain di dalam list (pencegahan duplikasi ID).
     - Program memvalidasi konversi tahun rilis ke tipe integer (`NumberFormatException`).
     - Jika valid, objek `Person` baru dibuat dan ditambahkan ke `listPerson`.
     - Tabel diperbarui secara otomatis, formulir dikosongkan, dan dialog sukses ditampilkan.

3. **Pemilihan Baris Tabel (Table Row Click)**:
   - Saat pengguna mengeklik salah satu baris pada tabel, `MouseAdapter` menangkap indeks baris (`peopleTable.getSelectedRow()`).
   - Nilai dari setiap kolom tabel disalin kembali ke form input (`idField`, `namaField`, `tahunField`) dan kedua dropdown (`kategoriComboBox.setSelectedItem()`, `tierComboBox.setSelectedItem()`).
   - Status tombol aksi berubah: tombol `addUpdateButton` berganti teks menjadi **Update**, dan tombol `deleteButton` dimunculkan.

4. **Pembaruan Data (Update - `updateData`)**:
   - Pengguna mengubah data pada form masukan.
   - Saat tombol **Update** ditekan:
     - Program menjalankan validasi kelengkapan isian dan format tahun.
     - Program memastikan bahwa jika ID diubah, ID baru tersebut tidak bertabrakan dengan ID karakter lain di dalam list.
     - Objek pada indeks terpilih di dalam `listPerson` diperbarui melalui setter.
     - Tampilan tabel disegarkan, formulir dibersihkan, dan tombol dikembalikan ke status awal ("Add").

5. **Penghapusan Data (Delete - `deleteData`)**:
   - Saat baris tabel dipilih dan tombol **Delete** ditekan:
     - Program menampilkan dialog konfirmasi: `"Apakah Anda yakin ingin menghapus data karakter '[Nama Karakter]'?"` dengan opsi *YES/NO*.
     - Jika pengguna memilih *YES*, data pada indeks tersebut dihapus dari `listPerson`, tabel diperbarui, formulir dibersihkan, dan pesan berhasil dimunculkan.
     - Jika pengguna memilih *NO*, proses dibatalkan dan data tetap aman.

6. **Pembatalan & Reset (Cancel - `clearForm`)**:
   - Menekan tombol **Cancel** akan mengosongkan seluruh teks field, mengembalikan dropdown ke opsi `???`, mengembalikan tombol ke label "Add", menyembunyikan tombol "Delete", dan menghapus seleksi aktif pada tabel.

---

## Struktur Folder Repositori

Struktur repositori disusun sesuai dengan standar pedoman praktikum DPBO:

```text
TP4DPBO2526C.../
├── PeopleData/
│   ├── Person.java          # Kelas model entitas data karakter
│   ├── PeopleMenu.java      # Kelas controller antarmuka GUI JFrame
│   └── PeopleMenu.form      # Berkas layout IntelliJ Swing UI Designer
├── Dokumentasi/             # Berkas bukti eksekusi program (screenshot/GIF)
└── README.md                # Dokumentasi lengkap proyek
```

---

## Dokumentasi Eksekusi Program

### 1. Tampilan Awal Program (Read / Tabel Terisi Data Awal)
![Tampilan Awal Program](<Dokumentasi/Tampilan Awal Program.png>)

### 2. Operasi Tambah Data (Create) & Notifikasi Sukses
![Operasi Tambah Data dan Notifikasi Sukses](<Dokumentasi/Operasi Tambah Data (Create) dan Notifikasi Sukses.png>)

### 3. Pemilihan Baris Tabel & Operasi Ubah Data (Update)
![Pemilihan Baris Tabel & Operasi Ubah Data](<Dokumentasi/Pemilihan Baris Tabel & Operasi Ubah Data (Update).png>)
![Notifikasi Perubahan Sukses](<Dokumentasi/Notifikasi Perubahan Sukses.png>)

### 4. Kotak Dialog Konfirmasi Hapus & Operasi Hapus (Delete)
![Kotak Dialog Konfirmasi Hapus & Operasi Hapus](<Dokumentasi/Kotak Dialog Konfirmasi Hapus & Operasi Hapus (Delete).png>)
![Notifikasi Penghapusan Sukses](<Dokumentasi/Notifikasi Penghapusan Sukses.png>)

### 5. Validasi Masukan Kosong & Error Dialog
![Validasi Masukan Kosong & Error Dialog](<Dokumentasi/Validasi Masukan Kosong & Error Dialog.png>)
