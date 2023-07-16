import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;

public class PDFSplitterApp extends JFrame {
    private JTextField inputField;
    private JTextField outputField;

    public PDFSplitterApp() {
        setTitle("PDF Splitter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(3, 1));

        // Input PDF file selection
        JPanel inputPanel = new JPanel(new FlowLayout());
        JLabel inputLabel = new JLabel("Choose PDF File:");
        inputField = new JTextField(20);
        JButton inputButton = new JButton("Browse");
        inputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    inputField.setText(selectedFile.getAbsolutePath());
                }
            }
        });
        inputPanel.add(inputLabel);
        inputPanel.add(inputField);
        inputPanel.add(inputButton);

        // Output directory selection
        JPanel outputPanel = new JPanel(new FlowLayout());
        JLabel outputLabel = new JLabel("Output Directory:");
        outputField = new JTextField(20);
        JButton outputButton = new JButton("Browse");
        outputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedDirectory = fileChooser.getSelectedFile();
                    outputField.setText(selectedDirectory.getAbsolutePath());
                }
            }
        });
        outputPanel.add(outputLabel);
        outputPanel.add(outputField);
        outputPanel.add(outputButton);

        // Split button
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton splitButton = new JButton("Split PDF");
        splitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                splitPDF();
            }
        });
        buttonPanel.add(splitButton);

        mainPanel.add(inputPanel);
        mainPanel.add(outputPanel);
        mainPanel.add(buttonPanel);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void splitPDF() {
        String inputFilePath = inputField.getText();
        String outputDirectory = outputField.getText();

        try {
            PDDocument document = PDDocument.load(new File(inputFilePath));

            Splitter splitter = new Splitter();
            List<PDDocument> splitDocuments = splitter.split(document);

            int pageCount = 1;
            for (PDDocument splitDocument : splitDocuments) {
                String outputFileName = outputDirectory + "/split_" + pageCount + ".pdf";
                splitDocument.save(outputFileName);
                splitDocument.close();
                System.out.println("Split PDF saved: " + outputFileName);
                pageCount++;
            }

            document.close();
            JOptionPane.showMessageDialog(this, "PDF split successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error splitting PDF: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                PDFSplitterApp app = new PDFSplitterApp();
                app.setVisible(true);
            }
        });
    }
}
