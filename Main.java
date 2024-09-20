package org.chiper.kriptografi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

class CipherAppSwing {
    public static void main(String[] args) {
        // Membuat JFrame
        JFrame frame = new JFrame("Cipher Encryptor/Decryptor");
        frame.setSize(400, 400); // Lebar lebih ramping
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Mengatur layout untuk frame
        frame.setLayout(new BorderLayout());

        // Membuat Panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        frame.add(panel, BorderLayout.CENTER);

        // Menambahkan komponen ke panel
        placeComponents(panel);

        // Menampilkan frame
        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(10, 10, 10, 10); // Padding

        // Judul
        JLabel titleLabel = new JLabel("Cipher Encryptor/Decryptor");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        panel.add(titleLabel, constraints);

        // Label untuk input pesan
        JLabel messageLabel = new JLabel("Message:");
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(messageLabel, constraints);

        // Text field untuk input pesan
        JTextField messageText = new JTextField(15); // Ukuran lebih kecil
        constraints.gridx = 1;
        constraints.gridy = 1;
        panel.add(messageText, constraints);

        // Tombol untuk mengunggah file .txt
        JButton uploadButton = new JButton("Upload .txt File");
        constraints.gridx = 2;
        constraints.gridy = 1;
        panel.add(uploadButton, constraints);

        // Label untuk input kunci
        JLabel keyLabel = new JLabel("Key (min. 12 chars):");
        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(keyLabel, constraints);

        // Text field untuk input kunci
        JTextField keyText = new JTextField(15); // Ukuran lebih kecil
        constraints.gridx = 1;
        constraints.gridy = 2;
        panel.add(keyText, constraints);

        // ComboBox untuk memilih cipher
        JLabel cipherLabel = new JLabel("Select Cipher:");
        constraints.gridx = 0;
        constraints.gridy = 3;
        panel.add(cipherLabel, constraints);

        String[] ciphers = {"Vigenere", "Playfair", "Hill"};
        JComboBox<String> cipherComboBox = new JComboBox<>(ciphers);
        constraints.gridx = 1;
        constraints.gridy = 3;
        panel.add(cipherComboBox, constraints);

        // Tombol untuk enkripsi
        JButton encryptButton = new JButton("Encrypt");
        constraints.gridx = 0;
        constraints.gridy = 4;
        panel.add(encryptButton, constraints);

        // Tombol untuk dekripsi
        JButton decryptButton = new JButton("Decrypt");
        constraints.gridx = 1;
        constraints.gridy = 4;
        panel.add(decryptButton, constraints);

        // Label untuk hasil
        JLabel resultLabel = new JLabel("Result:");
        constraints.gridx = 0;
        constraints.gridy = 5;
        panel.add(resultLabel, constraints);

        // Text area untuk menampilkan hasil
        JTextArea resultArea = new JTextArea(5, 15); // Ukuran lebih kecil
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        constraints.gridx = 1;
        constraints.gridy = 5;
        constraints.gridwidth = 2;
        panel.add(scrollPane, constraints);

        // Action untuk tombol Upload
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            stringBuilder.append(line).append("\n");
                        }
                        messageText.setText(stringBuilder.toString().trim());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        // Action untuk tombol Encrypt
        encryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = messageText.getText();
                String key = keyText.getText();
                String selectedCipher = (String) cipherComboBox.getSelectedItem();
                if (key.length() >= 12) {
                    String encryptedMessage = "";
                    switch (selectedCipher) {
                        case "Vigenere":
                            encryptedMessage = CipherUtils.vigenereEncrypt(message, key);
                            break;
                        case "Playfair":
                            encryptedMessage = CipherUtils.playfairEncrypt(message, key);
                            break;
                        case "Hill":
                            encryptedMessage = CipherUtils.hillEncrypt(message, key);
                            break;
                    }
                    resultArea.setText(encryptedMessage);
                } else {
                    resultArea.setText("Key must be at least 12 characters");
                }
            }
        });

        // Action untuk tombol Decrypt
        decryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = messageText.getText();
                String key = keyText.getText();
                String selectedCipher = (String) cipherComboBox.getSelectedItem();
                if (key.length() >= 12) {
                    String decryptedMessage = "";
                    switch (selectedCipher) {
                        case "Vigenere":
                            decryptedMessage = CipherUtils.vigenereDecrypt(message, key);
                            break;
                        case "Playfair":
                            decryptedMessage = CipherUtils.playfairDecrypt(message, key);
                            break;
                        case "Hill":
                            decryptedMessage = CipherUtils.hillDecrypt(message, key);
                            break;
                    }
                    resultArea.setText(decryptedMessage);
                } else {
                    resultArea.setText("Key must be at least 12 characters");
                }
            }
        });
    }
}