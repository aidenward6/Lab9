
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

class DataStreamFrame extends JFrame {
    private JTextArea[] fileTA;
    private JTextArea[] searchTA;
    private JTextField[] searchTextField;
    private JButton[] loadFileBut;
    private JButton[] searchBut;
    private JButton quitBut;
    private Path[] loadedFile;

    public DataStreamFrame() {
        setTitle("DataStreamsGUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setVisible(true);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(4, 3));

        fileTA = new JTextArea[3];
        searchTA = new JTextArea[3];
        searchTextField = new JTextField[3];
        loadFileBut = new JButton[3];
        searchBut = new JButton[3];

        loadedFile = new Path[3];

        for (int i = 0; i < 3; i++) {
            JPanel filePanel = new JPanel();
            filePanel.setLayout(new BorderLayout());

            fileTA[i] = new JTextArea(12, 12);
            fileTA[i].setEditable(false);
            JScrollPane displayScrolller = new JScrollPane(fileTA[i]);
            filePanel.add(displayScrolller, BorderLayout.WEST);

            searchTA[i] = new JTextArea(12, 12);
            searchTA[i].setEditable(false);
            JScrollPane searchScroller = new JScrollPane(searchTA[i]);
            filePanel.add(searchScroller, BorderLayout.EAST);

            searchTextField[i] = new JTextField(12);

            JPanel buttons = new JPanel();
            loadFileBut[i] = new JButton("File " + (i + 1));
            searchBut[i] = new JButton("Search ");
            buttons.add(loadFileBut[i]);
            buttons.add(searchTextField[i]);
            buttons.add(searchBut[i]);

            mainPanel.add(filePanel);
            mainPanel.add(buttons);
        }

        quitBut = new JButton("Quit");
        mainPanel.add(quitBut);

        for (int i = 0; i < 3; i++) {
            final int index = i;
            loadFileBut[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFileChooser fileChooser = new JFileChooser();
                    int result = fileChooser.showOpenDialog(DataStreamFrame.this);

                    if (result == JFileChooser.APPROVE_OPTION) {
                        loadedFile[index] = fileChooser.getSelectedFile().toPath();
                        fileTA[index].setText("");
                        searchTA[index].setText("");

                        try {
                            List<String> lines = Files.readAllLines(loadedFile[index]);
                            lines.forEach(line -> fileTA[index].append(line + "\n"));
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(DataStreamFrame.this, "Cannot load the file.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });

            searchBut[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (loadedFile[index] != null) {
                        String filterWord = searchTextField[index].getText().toLowerCase();
                        searchTA[index].setText("");

                        try {
                            List<String> filteredLines = Files.lines(loadedFile[index])
                                    .filter(line -> line.toLowerCase().contains(filterWord))
                                    .collect(Collectors.toList());
                            filteredLines.forEach(line -> searchTA[index].append(line + "\n"));
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(DataStreamFrame.this, "Error searching the file.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });
        }

        quitBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
    }


}


