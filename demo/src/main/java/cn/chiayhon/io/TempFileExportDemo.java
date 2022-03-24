package cn.chiayhon.io;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class TempFileExportDemo {

    private static String content = "This is a message for testing !";

    public static void main(String[] args) {
        exportTempFile("d:", "test.json", content);
    }

    /**
     * 生成字符串文件并导出
     *
     * @param path
     * @param fileName
     * @param content
     */
    private static void exportTempFile(String path, String fileName, String content) {
        final String[] arr = fileName.split("\\.");
        try {
            // 利用临时文件中转
            final Path tempFile = Files.createTempFile(arr[0], "." + arr[1]);
            final FileWriter fileWriter = new FileWriter(tempFile.toFile());
            final BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(content);
            bufferedWriter.flush();
            bufferedWriter.close();
            // 写入流
            final FileOutputStream fileOutputStream = new FileOutputStream(path + File.separator + fileName);
            fileOutputStream.write(Files.readAllBytes(tempFile));
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
