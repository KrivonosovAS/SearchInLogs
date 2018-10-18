import javax.swing.*;
import java.awt.*;



public class mainWindow extends JFrame {
    private JPanel rootPanel = new JPanel();
    private JTabbedPane jTab = new JTabbedPane();


    public mainWindow(){
        this.setTitle("LogSearcher");
        setContentPane(rootPanel);
        Dimension d = new Dimension(800,600);
        setMinimumSize(d);
        setSize(1024,1024);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        rootPanel.add(jTab);
        rootPanel.setLayout(new GridLayout(1,1));
        TabFill tab1 = new TabFill(jTab);
        jTab.add(tab1, "New Tab");
        new Thread(tab1).start();
        setVisible(true);
    }

    public static void main(String[] args) throws InterruptedException {
        new mainWindow();
    }
}
