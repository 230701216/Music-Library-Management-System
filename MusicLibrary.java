import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class MusicLibrary extends JFrame {
    private JTextField titleField, artistField, albumField, genreField;
    private JTable songTable;
    private DefaultTableModel tableModel;
    private Connection connection;

    public MusicLibrary() {
        connectToDatabase();
        setTitle("Music Library Management System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        inputPanel.add(new JLabel("Title:"));
        titleField = new JTextField();
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("Artist:"));
        artistField = new JTextField();
        inputPanel.add(artistField);
        inputPanel.add(new JLabel("Album:"));
        albumField = new JTextField();
        inputPanel.add(albumField);
        inputPanel.add(new JLabel("Genre:"));
        genreField = new JTextField();
        inputPanel.add(genreField);
        JButton addButton = new JButton("Add Song");
        JButton deleteButton = new JButton("Delete Song");
        inputPanel.add(addButton);
        inputPanel.add(deleteButton);
        tableModel = new DefaultTableModel(new String[]{"ID", "Title", "Artist", "Album", "Genre"}, 0);
        songTable = new JTable(tableModel);
        loadSongs();
        add(new JScrollPane(songTable), BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSong();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSong();
            }
        });
    }
    private void connectToDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/musiclibrarydb", "root", "2006");
            System.out.println("Database connected successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void loadSongs() {
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM Songs");

            tableModel.setRowCount(0); // Clear existing rows
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String artist = rs.getString("artist");
                String album = rs.getString("album");
                String genre = rs.getString("genre");
                tableModel.addRow(new Object[]{id, title, artist, album, genre});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void addSong() {
        String title = titleField.getText();
        String artist = artistField.getText();
        String album = albumField.getText();
        String genre = genreField.getText();

        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO Songs (title, artist, album, genre) VALUES (?, ?, ?, ?)");
            ps.setString(1, title);
            ps.setString(2, artist);
            ps.setString(3, album);
            ps.setString(4, genre);
            ps.executeUpdate();

            loadSongs(); // Refresh table
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void deleteSong() {
        int selectedRow = songTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a song to delete.");
            return;
        }
        int songId = (int) tableModel.getValueAt(selectedRow, 0);

        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM Songs WHERE id = ?");
            ps.setInt(1, songId);
            ps.executeUpdate();

            loadSongs(); // Refresh table
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void clearFields() {
        titleField.setText("");
        artistField.setText("");
        albumField.setText("");
        genreField.setText("");
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MusicLibrary().setVisible(true);
        });
    }
}
