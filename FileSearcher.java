import java.io.*;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class FileSearcher {
        private ArrayList <File> fileWithSearchStr = new ArrayList<>();
        private HashMap <String, Integer> searchResult = new HashMap<>();
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


        public boolean fileContainsWord(File f, String word) throws IOException{
            try {
                int wordIndex = 0;
                wordIndex = new String(Files.readAllBytes(Paths.get(f.getPath()))).indexOf(word);
                if (wordIndex != -1) {
                    System.out.println(f.getName());
                    synchronized (this) {searchResult.put(f.getName(), wordIndex);}
                    return true;
                }
            } catch (AccessDeniedException e){
                System.out.println("Access Denied");
            }
            return false;
        }

        public HashMap <String, Integer> getSearchResult() throws InterruptedException {
            return searchResult;
        }
    }


