import java.io.File;
import java.util.ArrayList;


public class Filter {
    private ArrayList<File> fl = new ArrayList<>();

    public ArrayList <File> getFiltered(String path, String fileType){
        typeFilter(path, fileType);
        return fl;
    }

    public void typeFilter(String path, String fileType) {
        File dir = new File(path);
        if (dir.isFile() && dir.getName().contains(fileType)) fl.add(dir);
        if (dir.isDirectory() && dir.listFiles() != null) {
            for (File f : dir.listFiles()) {
                typeFilter(f.getPath(), fileType);
            }
        }
    }
}

