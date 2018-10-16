import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;


public class TabFill extends JPanel implements Runnable {

    private JTabbedPane jTab;
    private JPanel controlPanel = new JPanel();
    private JPanel controlLeftPanel = new JPanel();
    private JPanel controlRightPanel = new JPanel();
    private JPanel controlRightDownPanel = new JPanel();
    private JPanel treeInsPanel = new JPanel();
    private JPanel textPanel = new JPanel();
    private JLabel fileLable = new JLabel();
    private JLabel pathLabel = new JLabel();
    private JLabel resultLabel = new JLabel();
    private JButton pathButton = new JButton();
    private JButton searchButton = new JButton();
    private JButton newTabButton = new JButton();
    private JButton closeTabButton = new JButton();
    private JButton goToPageButton = new JButton();
    private TextField fileTypeField = new TextField(5);
    private TextField searchField = new TextField();
    private TextField pageField = new TextField(4);
    private TextArea searchTextArea = new TextArea();
    private JScrollPane treePanel = new JScrollPane(treeInsPanel);
    private JScrollPane searchPanel =  new JScrollPane(searchTextArea);
    private GridBagConstraints gbc = new GridBagConstraints();
    private TreeMaker searchTree;
    private File openedFile;
    boolean searchAccept = true;
    private String directoryOfSearch = "";
    private HashMap <String, Integer> searchResult;



    public TabFill(JTabbedPane jTab){
        this.jTab = jTab;
        this.setLayout(new BorderLayout());

        this.add(controlPanel, BorderLayout.NORTH);
        controlPanel.setLayout(new GridBagLayout());
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.3;
        gbc.gridx = 0;
        gbc.gridy = 0;
        controlPanel.add(controlLeftPanel,gbc);
        gbc.weightx = 1;
        gbc.gridwidth = 2;
        gbc.gridx = 1;
        gbc.gridy = 0;

        controlPanel.add(controlRightPanel,gbc);
        controlRightPanel.setLayout(new BorderLayout());
        controlRightPanel.add(searchField, BorderLayout.CENTER);
        controlRightPanel.add(searchButton, BorderLayout.WEST);
        controlRightPanel.add(newTabButton, BorderLayout.EAST);
        controlRightPanel.add(controlRightDownPanel, BorderLayout.SOUTH);
        controlRightDownPanel.setLayout(new GridBagLayout());
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.5;
        gbc.gridx = 0;
        gbc.gridy = 0;
        controlRightDownPanel.add(resultLabel, gbc);
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 1;
        gbc.gridy = 0;
        controlRightDownPanel.add(goToPageButton, gbc);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 2;
        gbc.gridy = 0;
        controlRightDownPanel.add(pageField, gbc);
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 3;
        gbc.gridy = 0;
        controlRightDownPanel.add(closeTabButton, gbc);

        controlLeftPanel.setLayout(new GridBagLayout());
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5;
        gbc.gridy = 0;
        gbc.gridx = 0;

        controlLeftPanel.add(pathButton, gbc);
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.1;
        gbc.gridy = 0;
        gbc.gridx = 2;
        controlLeftPanel.add(fileLable,gbc);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.5;
        gbc.gridx = 3;
        gbc.gridy = 0;
        controlLeftPanel.add(fileTypeField, gbc);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.1;
        gbc.gridwidth = 4;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(0,0,0,0);
        controlLeftPanel.add(pathLabel, gbc);

        pathButton.setText("Path to folder");
        searchButton.setText("Search");
        newTabButton.setText(" New Tab ");
        closeTabButton.setText("Close Tab");
        goToPageButton.setText("GoTo");
        fileLable.setText("File Type: ");
        searchField.setText("");
        resultLabel.setText("Result of search. Document: ...");
        pathLabel.setText("   Selected path: ... ");
        fileTypeField.setText(".log");

        this.add(textPanel, BorderLayout.CENTER);
        textPanel.setLayout(new GridBagLayout());
        gbc.gridwidth = 1;
        gbc.weightx = 0.5;
        gbc.weighty = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0,0,0,0);
        gbc.gridwidth = 1;
        textPanel.add(treePanel, gbc);
        treeInsPanel.setLayout(new BorderLayout());

        gbc.weightx = 1;
        gbc.insets = new Insets(0,30,0,0);
        gbc.gridwidth = 2;
        gbc.gridx = 1;
        gbc.gridy = 0;
        textPanel.add(searchPanel,gbc);
    }



    public void run(){

        newTabButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TabFill tabTmp = new TabFill(jTab);
                jTab.add(tabTmp, "New Tab");
                new Thread(tabTmp).start();
            }
        });

        closeTabButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 jTab.remove(getTab());
            }
        });

        pathButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = 0;
                JFileChooser jf = new JFileChooser();
                jf.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                try {
                    result = jf.showDialog(getTab(), "GetPath");
                } catch( IndexOutOfBoundsException c){
                    c.printStackTrace();
                }
                if (result == JFileChooser.APPROVE_OPTION){
                    File f = jf.getSelectedFile();
                    directoryOfSearch = f.getPath();
                }
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!fileTypeField.getText().equals("") && directoryOfSearch != "" && searchAccept) {

                   searchAccept = false;
                   jTab.setTitleAt(jTab.getSelectedIndex(),searchField.getText());
                   resultLabel.setText("Searching...");
                   jTab.repaint();
                   new Thread(() -> {
                        try {
                            ArrayList <File> filesFilteredByType = new Filter().getFiltered(directoryOfSearch, fileTypeField.getText());
                            FileSearcher fs = new FileSearcher();
                            if (searchField.getText() != "") {
                                searchTree = new TreeMaker(directoryOfSearch, fs.Search(searchField.getText(), filesFilteredByType));
                                searchResult = fs.getSearchResult();
                            }
                            else searchTree = new TreeMaker(directoryOfSearch,filesFilteredByType);
                            treeInsPanel.removeAll();
                            treeInsPanel.add(searchTree, BorderLayout.CENTER);
                            jTab.repaint();

                            searchTree.addTreeSelectionListener(new TreeSelectionListener() {
                                @Override
                                public void valueChanged(TreeSelectionEvent e) {
                                   openedFile = getFilePathFromTree(e.getPath().toString());
                                   openDocument(openedFile, 1);
                                }
                            });
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        resultLabel.setText("Searching done.");
                        jTab.repaint();
                        searchAccept = true;
                   }
                   ).start();
                } else{
                    searchTextArea.setText("Can't search. Path, file type or search field is empty.");
                    jTab.repaint();
                }
            }
        });

        goToPageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String str = pageField.getText();
               int pageNumber = 0;
               if (!str.equals("")) pageNumber = Integer.parseInt(str);
               if (openedFile != null && pageNumber * 2000 < openedFile.length() ){
                   System.out.println(openedFile.length());
                   openDocument(openedFile, pageNumber);
               }
            }
        });
    }

    //Show document in textPanel
    public void openDocument(File f, int pageNumber){
        pageNumber = pageNumber - 1;
        if (!f.isDirectory()) {
            long pageCount = f.length()/2000;
            if (searchField.getText() != ""){
                resultLabel.setText("Searching word at page " + (searchResult.get(f.getName())/2000 + 1) + " from " + pageCount + " pages.");
            }
            searchTextArea.setText("");
            try(RandomAccessFile rd = new RandomAccessFile(f,"r")) {
                int b = rd.read();
                StringBuilder fileToPanel = new StringBuilder();
                rd.seek((pageNumber*2000));
                while (b != -1 && rd.getFilePointer() != (pageNumber*2000 + 2000)){
                    fileToPanel.append((char) b);
                    b = rd.read();
                }
                searchTextArea.append(fileToPanel.toString());
                jTab.repaint();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }


    public File getFilePathFromTree(String path){
        String [] arr =  path.split(",");
        path = directoryOfSearch + "\\";
        for (int i = 1; i < arr.length ; i++) {
            if (arr[i].startsWith(" ")) arr[i] = arr[i].substring(1);
            if (arr[i].endsWith("]")) arr[i] = arr[i].substring(0, arr[i].length()-1);
            path += arr[i];
            if (i != arr.length - 1) path += "\\";
        }
        File f = new File(path);
        return f;
    }

    public TabFill getTab(){
        return this;
    }

}
