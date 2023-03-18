package xyz.rajik;

import xyz.rajik.lfsr.LFSR;
import xyz.rajik.lfsr.LFSRByteEncoder;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.filechooser.*;

public class EncodingForm extends JFrame {

    private JTextField xorBitsField;
    private JTextField stateField;
    private JTextField fileField;
    private JTextField directoryField;
    private JTextField newFileNameField;
    private JTextArea keyArea;
    private JTextArea sourceArea;
    private JTextArea resultArea;
    private JComboBox<String> algorithmBox;

    private LFSRByteEncoder lfsr;

    public EncodingForm() {
        Font font = new Font("Noto", Font.PLAIN, 22);
        setTitle("Cipher Form");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel keyPanel = new JPanel();
        JLabel keyPanelLabel = new JLabel("Xor bits: ");
        keyPanelLabel.setFont(font);
        keyPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        keyPanel.add(keyPanelLabel);
        xorBitsField = new JTextField(20);
        xorBitsField.setFont(font);
        keyPanel.add(xorBitsField);

        JButton setDefaultXorBitsButton = new JButton("Set default");
        setDefaultXorBitsButton.setFont(font);
        keyPanel.add(setDefaultXorBitsButton);
        setDefaultXorBitsButton.addActionListener((event) -> {
            xorBitsField.setText(Integer.toBinaryString(LFSRByteEncoder.DEFAULT_BITS));
        });

        mainPanel.add(keyPanel);


        JPanel statePanel = new JPanel();
        JLabel statePanelLabel = new JLabel("State: ");
        statePanelLabel.setFont(font);
        statePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        statePanel.add(statePanelLabel);
        stateField = new JTextField(20);
        stateField.setFont(font);
        statePanel.add(stateField);

        JButton setDefaultStateButton = new JButton("Set default");
        setDefaultStateButton.setFont(font);
        statePanel.add(setDefaultStateButton);
        setDefaultStateButton.addActionListener((event) -> {
            stateField.setText(Integer.toBinaryString(LFSRByteEncoder.DEFAULT_STATE));
        });

        mainPanel.add(statePanel);

        JPanel newFileNamePanel = new JPanel();
        JLabel  newFileNameLabel = new JLabel("Output file name: ");
        newFileNameLabel.setFont(font);
        newFileNamePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        newFileNamePanel.add(newFileNameLabel);
        newFileNameField = new JTextField(20);
        newFileNameField.setFont(font);
        newFileNamePanel.add(newFileNameField);
        mainPanel.add(newFileNamePanel);

        JPanel outputDirField = new JPanel();
        outputDirField.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel outDirLab = new JLabel("Output directory: ");
        outDirLab.setFont(font);
        outputDirField.add(outDirLab);

        directoryField = new JTextField(20);
        directoryField.setFont(font);
        directoryField.setEditable(false);
        outputDirField.add(directoryField);

        JButton chooseOutputDirectoryButton = new JButton("Choose output directory");
        chooseOutputDirectoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int result = fileChooser.showDialog(EncodingForm.this, "Open");
                if (result == JFileChooser.APPROVE_OPTION) {
                    directoryField.setText(fileChooser.getSelectedFile().getAbsolutePath());
                }
            }
        });
        chooseOutputDirectoryButton.setFont(font);
        outputDirField.add(chooseOutputDirectoryButton);
        chooseOutputDirectoryButton.addActionListener((event) -> {
        });

        mainPanel.add(outputDirField);


        JPanel filePanel = new JPanel();
        filePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel filePanelLabel = new JLabel("File to encode/decode: ");
        filePanelLabel.setFont(font);
        filePanel.add(filePanelLabel);
        filePanel.setFont(font);
        fileField = new JTextField(20);
        fileField.setFont(font);
        fileField.setEditable(false);
        filePanel.add(fileField);
        JButton fileButton = new JButton("Choose File");
        fileButton.setFont(font);
        fileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(EncodingForm.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    fileField.setText(fileChooser.getSelectedFile().getAbsolutePath());
                }
            }
        });
        filePanel.add(fileButton);
        mainPanel.add(filePanel);

        JPanel keyOutputPanel = new JPanel();
        JLabel keyOutputLabel = new JLabel("Key:      ");
        keyOutputLabel.setFont(font);
        keyOutputPanel.setLayout(new BorderLayout());
        keyOutputPanel.add(keyOutputLabel, BorderLayout.WEST);
        keyArea = new JTextArea();
        keyArea.setFont(font);
        keyArea.setEditable(false);
        keyOutputPanel.add(new JScrollPane(keyArea), BorderLayout.CENTER);
        mainPanel.add(keyOutputPanel);

        JPanel sourcePanel = new JPanel();
        JLabel sourceLabel = new JLabel("source: ");
        sourceLabel.setFont(font);
        sourcePanel.setLayout(new BorderLayout());
        sourcePanel.add(sourceLabel, BorderLayout.WEST);
        sourceArea = new JTextArea();
        sourceArea.setFont(font);
        sourceArea.setEditable(false);
        sourcePanel.add(new JScrollPane(sourceArea), BorderLayout.CENTER);
        mainPanel.add(sourcePanel);

        JPanel resultPanel = new JPanel();
        JLabel resultLabel = new JLabel("result:   ");
        resultLabel.setFont(font);
        resultPanel.setLayout(new BorderLayout());
        resultPanel.add(resultLabel, BorderLayout.WEST);
        resultArea = new JTextArea();
        resultArea.setFont(font);
        resultArea.setEditable(false);
        resultPanel.add(new JScrollPane(resultArea), BorderLayout.CENTER);
        mainPanel.add(resultPanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JButton encryptButton = new JButton("Encode");
        encryptButton.setFont(font);

        ActionListener actionListener = (event) -> {
            try {
                if (stateField.getText().length() != 24 || xorBitsField.getText().length() != 24)
                    throw new RuntimeException("Error");
                lfsr = new LFSRByteEncoder(Integer.parseInt(stateField.getText(), 2), Integer.parseInt(xorBitsField.getText(), 2), LFSRByteEncoder.DEFAULT_SIZE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Format error in state and xor bits fields", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            //File file = new File(fileField.getText());
            File file = new File(directoryField.getText() + File.separator + newFileNameField.getText());
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try (FileInputStream fileInputStream = new FileInputStream(fileField.getText());
                 //FileOutputStream fileOutputStream = new FileOutputStream(file.getParentFile() + "/" + file.getName() + "_result")) {
                 FileOutputStream fileOutputStream = new FileOutputStream(file.getAbsoluteFile())) {
                byte[] bytes = fileInputStream.readAllBytes();
                byte[] newMsg = new byte[bytes.length];
                for (int i = 0; i < bytes.length; i++) {
                    for (int j = 0; j < 8; j++) {
                        int temp = bytes[i] & (1 << j);
                        if (temp > 0) {
                            newMsg[i] |= (128 >> j);
                        }
                    }
                }
                bytes = newMsg;
                byte[] encodedBytes = lfsr.encode(bytes);
                BitSet key = lfsr.getLfsr().getKey().get(0, bytes.length * 8);

                int offset = 50;

                String keyBits = LFSR.bitSetToString(key, bytes.length * 8, offset);
                String sourceBits = LFSR.byteArrayToBits(bytes, offset);
                String resultBits = LFSR.byteArrayToBits(encodedBytes, offset);


                keyArea.setText(keyBits);
                sourceArea.setText(sourceBits);
                resultArea.setText(resultBits);


                newMsg = new byte[encodedBytes.length];
                for (int i = 0; i < encodedBytes.length; i++) {
                    for (int j = 0; j < 8; j++) {
                        int temp = encodedBytes[i] & (1 << j);
                        if (temp > 0) {
                            newMsg[i] |= (128 >> j);
                        }
                    }
                }
                encodedBytes = newMsg;
                fileOutputStream.write(encodedBytes);

            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(this, "File not found", "ERROR", JOptionPane.ERROR_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Input output error", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        };

        encryptButton.addActionListener(actionListener);

        JButton decryptButton = new JButton("Decode");
        decryptButton.setFont(font);

        decryptButton.addActionListener(actionListener);

        buttonPanel.add(encryptButton);
        buttonPanel.add(decryptButton);
        mainPanel.add(buttonPanel);

        add(mainPanel);
    }

    public void setTextWithLineBreaks(JTextArea textArea, String text, Integer offset) {
        for (int i = 0; i < text.length() / offset; i++)
            textArea.append(text.substring(i * offset, offset * (1 + i)) + "\n");

        int lastAppended = text.length() - text.length() % offset;
        textArea.append(text.substring(lastAppended, text.length()));
    }

    public static void main(String[] args) {
        EncodingForm form = new EncodingForm();
        form.setVisible(true);
    }
}

