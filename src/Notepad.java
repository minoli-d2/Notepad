// Index No: s16996

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Notepad extends JFrame {
    private JTextArea textArea;
    private JFileChooser fileChooser;
    private File currentFile = null;
    private boolean isModified = false;

    public Notepad() {
        // Frame setup
        setTitle("Notepad");
        setSize(600, 400);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        // Text Area with Scroll
        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane);

        // Track changes
        textArea.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { isModified = true; }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { isModified = true; }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { isModified = true; }
        });

        // Menu Bar
        JMenuBar menuBar = new JMenuBar();

        // File Menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem newItem = new JMenuItem("New");
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem saveAsItem = new JMenuItem("Save As");
        JMenuItem exitItem = new JMenuItem("Exit");

        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // Edit Menu
        JMenu editMenu = new JMenu("Edit");
        JMenuItem cutItem = new JMenuItem("Cut");
        JMenuItem copyItem = new JMenuItem("Copy");
        JMenuItem pasteItem = new JMenuItem("Paste");

        editMenu.add(cutItem);
        editMenu.add(copyItem);
        editMenu.add(pasteItem);

        // Help Menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        helpMenu.add(aboutItem);

        // Format Menu (Optional Font/Color)
        JMenu formatMenu = new JMenu("Format");
        JMenuItem fontItem = new JMenuItem("Change Font");
        JMenuItem colorItem = new JMenuItem("Change Color");
        formatMenu.add(fontItem);
        formatMenu.add(colorItem);

        // Add menus to bar
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(formatMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);

        // File Chooser
        fileChooser = new JFileChooser();

        // Action Listeners (File menu)
        newItem.addActionListener(e -> newFile());
        openItem.addActionListener(e -> openFile());
        saveItem.addActionListener(e -> saveFile());
        saveAsItem.addActionListener(e -> saveAsFile());
        exitItem.addActionListener(e -> exitApp());

        // Edit menu
        cutItem.addActionListener(e -> textArea.cut());
        copyItem.addActionListener(e -> textArea.copy());
        pasteItem.addActionListener(e -> textArea.paste());

        // Help menu
        aboutItem.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        "Notepad Project\nDeveloped by: A.M.D Silva\nID: s16996",
                        "About Notepad",
                        JOptionPane.INFORMATION_MESSAGE)
        );

        // Format menu
        fontItem.addActionListener(e -> changeFont());
        colorItem.addActionListener(e -> changeColor());

        // Handle window close
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                exitApp();
            }
        });
    }

    // File Handling 
    private void newFile() {
        if (checkUnsavedChanges()) {
            textArea.setText("");
            currentFile = null;
            isModified = false;
            setTitle("Notepad");
        }
    }

    private void openFile() {
        if (checkUnsavedChanges()) {
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    textArea.read(reader, null);
                    currentFile = file;
                    isModified = false;
                    setTitle("Notepad - " + file.getName());
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Could not open file!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void saveFile() {
        if (currentFile == null) {
            saveAsFile();
            return;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentFile))) {
            textArea.write(writer);
            isModified = false;
            setTitle("Notepad - " + currentFile.getName());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Could not save file!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveAsFile() {
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();
            saveFile();
        }
    }

    private void exitApp() {
        if (checkUnsavedChanges()) {
            System.exit(0);
        }
    }

    private boolean checkUnsavedChanges() {
        if (isModified) {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Do you want to save changes?",
                    "Unsaved Changes",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            if (choice == JOptionPane.CANCEL_OPTION) {
                return false;
            } else if (choice == JOptionPane.YES_OPTION) {
                saveFile();
            }
        }
        return true;
    }

    //Format Handling
    private void changeFont() {
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        String font = (String) JOptionPane.showInputDialog(
                this,
                "Choose Font:",
                "Font Selector",
                JOptionPane.PLAIN_MESSAGE,
                null,
                fonts,
                textArea.getFont().getFamily()
        );
        if (font != null) {
            textArea.setFont(new Font(font, Font.PLAIN, 16));
        }
    }

    private void changeColor() {
        Color color = JColorChooser.showDialog(this, "Choose Text Color", textArea.getForeground());
        if (color != null) {
            textArea.setForeground(color);
        }
    }

    //Main 
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Notepad().setVisible(true);
        });
    }
}
