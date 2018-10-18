import java.io.*;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;

public class FileSearcher {
        private ArrayList <File> fileWithSearchStr = new ArrayList<>();
        private ArrayList <Thread> t = new ArrayList<>();


        public ArrayList<File> Search(String searchText, ArrayList <File> typedFiles) throws InterruptedException {
            for (File f: typedFiles) {
                   Thread tmp = new Thread(() -> {
                       try {
                           if (fileContainsWord(f,searchText))  fileWithSearchStr.add(f);
                       } catch (IOException e) {
                           e.printStackTrace();
                       }
                   });
                   tmp.start();
                   t.add(tmp);

            }
            for (Thread tmp: t) {
                tmp.join();
            }
            return fileWithSearchStr;
            }


        public boolean fileContainsWord(File f, String word) throws IOException {

            try (BufferedReader bf = new BufferedReader (new FileReader(f))) {
                String str;
                while ((str = bf.readLine()) != null) {
                    if ((str.indexOf(word)) != -1) return true;
                }
            } catch (AccessDeniedException e) {
                System.out.println("Access Denied");
            }
            return false;
        }
    }


