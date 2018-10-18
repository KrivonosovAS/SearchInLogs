import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.io.File;
import java.util.ArrayList;

public class TreeMaker extends JTree {
    ArrayList <File> fileArray;
    String path;

    DefaultMutableTreeNode root;

    TreeMaker(String path, ArrayList <File> fileArray){
        this.path = path;
        String arr[];
        System.out.println(path);
        arr = path.split("\\\\");
        root = new DefaultMutableTreeNode(arr[arr.length - 1]);
        makeTree(path, fileArray);
    }

    //Make tree from arrayList of files
    public void makeTree(String path, ArrayList<File> fileArray){
        this.fileArray = fileArray;
        this.path = path;
        String [] elementPath;
        for (File f:fileArray) {
            elementPath = f.getPath().substring(path.length() + 1).split("\\\\");
            pasteInTree(root, elementPath,0 );
        }
        DefaultTreeModel mod = new DefaultTreeModel(root);
        this.setModel(mod);
    }

    //Insert leaf in Tree
    public void pasteInTree(DefaultMutableTreeNode tmpRoot, String [] elementPath, int numberOfPosition){
        DefaultMutableTreeNode tmp = new DefaultMutableTreeNode(elementPath[numberOfPosition]);
        boolean sameChild = false;
        if(tmpRoot.getChildCount() != 0) {
            for (int j = 0; j < tmpRoot.getChildCount(); j++) {
                if (tmpRoot.getChildAt(j).toString().equals(tmp.toString()) && numberOfPosition != (elementPath.length - 1)) {
                    sameChild = true;
                    pasteInTree((DefaultMutableTreeNode) tmpRoot.getChildAt(j), elementPath, (numberOfPosition + 1));
                }
            }
            if (!sameChild){
                tmpRoot.add(tmp);
                if (numberOfPosition != (elementPath.length - 1)) pasteInTree( tmp, elementPath, (numberOfPosition+1));
            }
        }else {
            tmpRoot.add(tmp);
            if (numberOfPosition != (elementPath.length - 1)) pasteInTree(tmp, elementPath, (numberOfPosition +1) );
        }
    }
}
