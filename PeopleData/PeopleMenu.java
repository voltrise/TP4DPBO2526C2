import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class PeopleMenu extends JFrame {
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JTextField idField;
    private JTextField namaField;
    private JTextField tahunField;
    private JComboBox<String> kategoriComboBox;
    private JComboBox<String> tierComboBox;
    private JButton addUpdateButton;
    private JButton deleteButton;
    private JButton cancelButton;
    private JTable peopleTable;
    private JScrollPane tableScrollPane;

    private ArrayList<Person> listPerson;
    private int selectedIndex = -1;

    public PeopleMenu() {
        if (mainPanel == null) {
            buildUI();
        }

        listPerson = new ArrayList<>();
        populateList();

        peopleTable.setModel(setTable());

        if (titleLabel != null) {
            titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));
        }

        String[] kategoriData = { "???", "Main DPS", "Sub-DPS", "Support" };
        kategoriComboBox.setModel(new DefaultComboBoxModel<>(kategoriData));

        String[] tierData = { "???", "SS-Tier", "S-Tier", "A-Tier", "B-Tier", "C-Tier" };
        tierComboBox.setModel(new DefaultComboBoxModel<>(tierData));

        deleteButton.setVisible(false);

        addUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedIndex == -1) {
                    insertData();
                } else {
                    updateData();
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteData();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });

        peopleTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                selectedIndex = peopleTable.getSelectedRow();
                if (selectedIndex != -1) {
                    String curId = peopleTable.getModel().getValueAt(selectedIndex, 1).toString();
                    String curNama = peopleTable.getModel().getValueAt(selectedIndex, 2).toString();
                    String curTahun = peopleTable.getModel().getValueAt(selectedIndex, 3).toString();
                    String curKategori = peopleTable.getModel().getValueAt(selectedIndex, 4).toString();
                    String curTier = peopleTable.getModel().getValueAt(selectedIndex, 5).toString();

                    idField.setText(curId);
                    namaField.setText(curNama);
                    tahunField.setText(curTahun);
                    kategoriComboBox.setSelectedItem(curKategori);
                    tierComboBox.setSelectedItem(curTier);

                    addUpdateButton.setText("Update");
                    deleteButton.setVisible(true);
                }
            }
        });
    }

    private void populateList() {
        listPerson.add(new Person("GEN001", "Neuvillette", 2023, "Main DPS", "SS-Tier"));
        listPerson.add(new Person("GEN002", "Furina", 2023, "Sub-DPS", "SS-Tier"));
        listPerson.add(new Person("GEN003", "Nahida", 2022, "Sub-DPS", "S-Tier"));
        listPerson.add(new Person("GEN004", "Kaedehara Kazuha", 2021, "Support", "SS-Tier"));
        listPerson.add(new Person("GEN005", "Zhongli", 2020, "Support", "S-Tier"));
        listPerson.add(new Person("GEN006", "Hu Tao", 2021, "Main DPS", "S-Tier"));
        listPerson.add(new Person("GEN007", "Bennett", 2020, "Support", "S-Tier"));
        listPerson.add(new Person("GEN008", "Xiangling", 2020, "Sub-DPS", "S-Tier"));
        listPerson.add(new Person("GEN009", "Raiden Shogun", 2021, "Main DPS", "S-Tier"));
        listPerson.add(new Person("GEN010", "Diluc", 2020, "Main DPS", "B-Tier"));
    }

    public final DefaultTableModel setTable() {
        Object[] cols = { "No", "ID Karakter", "Nama Karakter", "Tahun Rilis", "Role / Kategori", "Tier Ranking" };
        DefaultTableModel tmp = new DefaultTableModel(null, cols);

        for (int i = 0; i < listPerson.size(); i++) {
            Object[] row = {
                i + 1,
                listPerson.get(i).getId(),
                listPerson.get(i).getNama(),
                listPerson.get(i).getTahunLahir(),
                listPerson.get(i).getKategori(),
                listPerson.get(i).getTier()
            };
            tmp.addRow(row);
        }
        return tmp;
    }

    public void insertData() {
        String id = idField.getText().trim();
        String nama = namaField.getText().trim();
        String tahunStr = tahunField.getText().trim();

        if (id.isEmpty() || nama.isEmpty() || tahunStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field teks wajib diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (kategoriComboBox.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this, "Silakan pilih Role / Kategori karakter!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (tierComboBox.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this, "Silakan pilih Tier Ranking karakter!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        for (Person p : listPerson) {
            if (p.getId().equalsIgnoreCase(id)) {
                JOptionPane.showMessageDialog(this, "ID '" + id + "' sudah terdaftar! Gunakan ID lain.", "Error Duplikasi ID", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        try {
            int tahun = Integer.parseInt(tahunStr);
            String kategori = kategoriComboBox.getSelectedItem().toString();
            String tier = tierComboBox.getSelectedItem().toString();

            listPerson.add(new Person(id, nama, tahun, kategori, tier));
            peopleTable.setModel(setTable());
            clearForm();

            JOptionPane.showMessageDialog(this, "Data karakter berhasil ditambahkan!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Tahun rilis harus berupa angka!", "Error Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateData() {
        if (selectedIndex == -1) {
            return;
        }

        String id = idField.getText().trim();
        String nama = namaField.getText().trim();
        String tahunStr = tahunField.getText().trim();

        if (id.isEmpty() || nama.isEmpty() || tahunStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field teks wajib diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (kategoriComboBox.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this, "Silakan pilih Role / Kategori karakter!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (tierComboBox.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this, "Silakan pilih Tier Ranking karakter!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        for (int i = 0; i < listPerson.size(); i++) {
            if (i != selectedIndex && listPerson.get(i).getId().equalsIgnoreCase(id)) {
                JOptionPane.showMessageDialog(this, "ID '" + id + "' sudah digunakan oleh karakter lain!", "Error Duplikasi ID", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        try {
            int tahun = Integer.parseInt(tahunStr);
            String kategori = kategoriComboBox.getSelectedItem().toString();
            String tier = tierComboBox.getSelectedItem().toString();

            listPerson.get(selectedIndex).setId(id);
            listPerson.get(selectedIndex).setNama(nama);
            listPerson.get(selectedIndex).setTahunLahir(tahun);
            listPerson.get(selectedIndex).setKategori(kategori);
            listPerson.get(selectedIndex).setTier(tier);

            peopleTable.setModel(setTable());
            clearForm();

            JOptionPane.showMessageDialog(this, "Data karakter berhasil diubah!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Tahun rilis harus berupa angka!", "Error Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deleteData() {
        if (selectedIndex == -1) {
            return;
        }

        String targetNama = listPerson.get(selectedIndex).getNama();
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Apakah Anda yakin ingin menghapus data karakter '" + targetNama + "'?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            listPerson.remove(selectedIndex);
            peopleTable.setModel(setTable());
            clearForm();
            JOptionPane.showMessageDialog(this, "Data karakter berhasil dihapus!");
        }
    }

    public void clearForm() {
        idField.setText("");
        namaField.setText("");
        tahunField.setText("");
        kategoriComboBox.setSelectedIndex(0);
        tierComboBox.setSelectedIndex(0);

        addUpdateButton.setText("Add");
        deleteButton.setVisible(false);
        peopleTable.clearSelection();
        selectedIndex = -1;
    }

    private void buildUI() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        mainPanel.setBackground(Color.WHITE);

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(Color.WHITE);
        titleLabel = new JLabel("Daftar Karakter Genshin Impact");
        headerPanel.add(titleLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel formWrapper = new JPanel(new BorderLayout(10, 10));
        formWrapper.setBackground(Color.WHITE);

        JPanel formGrid = new JPanel(new GridLayout(5, 2, 8, 8));
        formGrid.setBackground(Color.WHITE);

        idField = new JTextField();
        namaField = new JTextField();
        tahunField = new JTextField();
        kategoriComboBox = new JComboBox<>();
        tierComboBox = new JComboBox<>();

        formGrid.add(new JLabel("ID Karakter:"));
        formGrid.add(idField);
        formGrid.add(new JLabel("Nama Karakter:"));
        formGrid.add(namaField);
        formGrid.add(new JLabel("Tahun Rilis:"));
        formGrid.add(tahunField);
        formGrid.add(new JLabel("Role / Kategori:"));
        formGrid.add(kategoriComboBox);
        formGrid.add(new JLabel("Tier Ranking (Game8):"));
        formGrid.add(tierComboBox);

        formWrapper.add(formGrid, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        buttonPanel.setBackground(Color.WHITE);
        addUpdateButton = new JButton("Add");
        deleteButton = new JButton("Delete");
        cancelButton = new JButton("Cancel");

        buttonPanel.add(addUpdateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(cancelButton);
        formWrapper.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(formWrapper, BorderLayout.CENTER);

        peopleTable = new JTable();
        tableScrollPane = new JScrollPane(peopleTable);
        tableScrollPane.setPreferredSize(new Dimension(700, 260));
        mainPanel.add(tableScrollPane, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        PeopleMenu menu = new PeopleMenu();
        menu.setTitle("PeopleData - Genshin Impact Roster");
        menu.setSize(750, 650);
        menu.setLocationRelativeTo(null);
        menu.setContentPane(menu.mainPanel);
        menu.getContentPane().setBackground(Color.WHITE);
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menu.setVisible(true);
    }
}
