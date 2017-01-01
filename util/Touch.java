package util;

import java.io.*;


public class Touch {

    public static void touch(File dir, long time) {
        File[] files = dir.listFiles();
        for (File file : files) {
            file.setLastModified(time);
            if (file.isDirectory()) {
                touch(file, time);
            }
        }
    }


    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        touch(new File("."), time);
    }

}
