package me.sfclog.unzip;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import java.io.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("Start upzipping file!");
        File zipFile = new File("/home/container/file.zip");
        File destinationDir = new File("/home/container/");
        try {
            unzip(zipFile, destinationDir);
            System.out.println("Unzipping completed successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void unzip(File zipFile, File destinationDir) throws IOException {
        try (InputStream is = new FileInputStream(zipFile);
             BufferedInputStream bis = new BufferedInputStream(is);
             ZipArchiveInputStream zis = new ZipArchiveInputStream(bis)) {

            ArchiveEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                File entryFile = new File(destinationDir, entry.getName());

                if (entry.isDirectory()) {
                    entryFile.mkdirs();
                } else {
                    File parent = entryFile.getParentFile();
                    if (!parent.exists()) {
                        parent.mkdirs();
                    }
                    try (OutputStream os = new FileOutputStream(entryFile)) {
                        IOUtils.copy(zis, os);
                    }
                }
            }
        }
    }
}