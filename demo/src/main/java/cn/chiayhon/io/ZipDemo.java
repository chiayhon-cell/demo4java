package cn.chiayhon.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipDemo {

    public static void main(String[] args) {
        decompress("d:/test.zip", "d:");
    }

    /**
     * 解压文件
     *
     * @param path      压缩文件所在路径
     * @param unzipPath 解压路径
     */
    private static void decompress(String path, String unzipPath) {
        try {
            final byte[] bytes = Files.readAllBytes(Paths.get(path));
            final ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(bytes));
            Map<String, Object> fileMetas = new HashMap<>(16);
            ZipEntry zipEntry;
            // 读取压缩文件数据
            while ((zipEntry = zis.getNextEntry()) != null) {
                String fileName = zipEntry.getName();
                final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int n;
                byte[] buffer = new byte[1024];
                while ((n = zis.read(buffer, 0, buffer.length)) != -1) {
                    baos.write(buffer, 0, n);
                }
                baos.flush();
                final byte[] data = baos.toByteArray();
                fileMetas.put(fileName, data);
                baos.reset();
            }
            // 解压缩
            for (Map.Entry<String, Object> entry : fileMetas.entrySet()) {
                final String key = entry.getKey();
                final byte[] data = (byte[]) entry.getValue();
                Files.write(Paths.get(unzipPath + File.separator + key), data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
