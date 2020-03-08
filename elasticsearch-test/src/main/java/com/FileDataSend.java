package com;




import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileDataSend {


    public void getSubdirIteratorFile(List<File> dirs, File dir) {
        if (dir.isDirectory()) {
            File[] childDirs = dir.listFiles();
            for (File f : childDirs) {
                getSubdirIteratorFile(dirs, f);
            }
        } else {
            dirs.add(dir);
        }
    }

    public void getSubdirIteratorPath(List<Path> dirs, File dir) {
        if (dir.isDirectory()) {
            File[] childDirs = dir.listFiles();
            for (File f : childDirs) {
                getSubdirIteratorPath(dirs, f);
            }
        } else {
            dirs.add(dir.toPath());
        }
    }

    public static void main(String[] args) throws IOException {
        FileDataSend fileDataSend = new FileDataSend();
        String filePath = "C:\\work\\tmp\\2020021801";
        File dir = new File(filePath);
        List<Path> dirs = new ArrayList<>();
        fileDataSend.getSubdirIteratorPath(dirs,dir);
        for(Path p:dirs){
            List<String> lines = Files.readAllLines(p);
            for(String line:lines){
                System.out.println(line);
            }
        }
    }
}
