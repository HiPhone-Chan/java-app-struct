package com.chf.app.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class FileUtil {

    private final static Logger LOG = LoggerFactory.getLogger(FileUtil.class);

    public static String readFile2String(File file) {
        try {
            return FileUtils.readFileToString(file);
        } catch (IOException e) {
            LOG.info("Read file exception : {} ", e);
        }
        return null;
    }

    public static byte[] readFile2Byte(File file) {
        try {
            return FileUtils.readFileToByteArray(file);
        } catch (IOException e) {
            LOG.info("Read file exception : {} ", e);
        }
        return null;
    }

    public static void writeFile(byte[] bytes, String dst) {
        writeFile(bytes, new File(dst));
    }

    public static void writeFile(byte[] bytes, File dst) {
        writeFile(new ByteArrayInputStream(bytes), dst);
    }

    public static void writeFile(InputStream is, File dst) {
        try {
            FileUtils.copyInputStreamToFile(is, dst);
        } catch (Exception e) {
            LOG.info("Write file exception : {} ", e);
        }
    }

    public static void writeFile(MultipartFile src, String dst) {
        if (src == null || src.getSize() == 0) {
            LOG.info("Empty file input");
            return;
        }

        try (InputStream is = src.getInputStream();) {
            FileUtils.copyInputStreamToFile(is, new File(dst));
        } catch (Exception e) {
            LOG.info("Write file exception : {} ", e);
        }
    }

    public static void writeFile(MultipartFile file, String dir, String fileName) {
        if (file == null || file.getSize() == 0) {
            LOG.info("Empty file input");
            return;
        }

        File dirFile = new File(dir);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        try (InputStream is = file.getInputStream();) {
            FileUtils.copyInputStreamToFile(is, new File(dir + fileName));
        } catch (Exception e) {
            LOG.info("Write file exception : {} ", e);
        }
    }

    public static boolean deleteFile(String file) {
        return FileUtils.deleteQuietly(new File(file));
    }

    public static void deleteDir(String dir) {
        deleteDir(new File(dir));
    }

    public static void deleteDir(File dir) {
        try {
            FileUtils.deleteDirectory(dir);
        } catch (IOException e) {
            LOG.warn("Delete dir exception", e);
        }
    }

    public static void move(File from, File to) {
        try {
            for (File file : from.listFiles()) {
                if (file.isDirectory()) {
                    FileUtils.moveDirectoryToDirectory(file, to, true);
                } else {
                    FileUtils.moveFileToDirectory(file, to, true);
                }
            }
        } catch (IOException e) {
            LOG.info("Move file exception : {} ", e);
        }
    }

    public static void writeLines(File file, List<String> lines, boolean append) {
        try {
            FileUtils.writeLines(file, lines, true);
        } catch (IOException e) {
            LOG.info("Write file lines exception : {} ", e);
        }
    }

    public static String getNameWithNoSuffix(String name) {
        int index = name.lastIndexOf(".");
        return name.substring(0, index);
    }

    public static String getNameWithNoSuffix(File file) {
        return getNameWithNoSuffix(file.getName());
    }

    public static String getSuffix(String name) {
        int index = name.lastIndexOf(".") + 1;
        return name.substring(index);
    }
}
