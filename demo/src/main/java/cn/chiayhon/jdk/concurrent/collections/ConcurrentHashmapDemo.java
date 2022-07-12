package cn.chiayhon.jdk.concurrent.collections;

import lombok.extern.slf4j.Slf4j;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class ConcurrentHashmapDemo {

    private final static Map<String, AtomicInteger> map = new ConcurrentHashMap<>();

    public static void main(String[] args) throws Exception {
//        testGetUniqueName();
//        System.out.println("123".equals(new String("123")));
        Map<String, Map<String, AtomicInteger>> nameSpaceMap = new ConcurrentHashMap<>();
        final Map<String, AtomicInteger> map1 = nameSpaceMap.computeIfAbsent("key", k -> new ConcurrentHashMap<>());
        map1.put("123", new AtomicInteger(1));
        final Map<String, AtomicInteger> map2 = nameSpaceMap.computeIfAbsent("key", k -> new ConcurrentHashMap<>());
        System.out.println(map2);

    }

    private static void testGetUniqueName() throws InterruptedException {
        Runnable runnable = () -> System.out.println(getUniqueName("testword.pdf", "D:\\Workspace\\IntelliJ-IDEA_Workspace\\req\\temp\\private-java\\data3\\20220711"));
        final List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            final Thread thread = new Thread(runnable);
            threads.add(thread);
        }
        threads.forEach(Thread::start);
        for (int i = 0; i < threads.size(); i++) {
            threads.get(i).join();
        }
    }


    private static String getUniqueName(String fileName, String path) {
        Path dirPath = Paths.get(path);
        String originName = fileName;
        String mainName = getMainName(originName);
        String extName = getExtName(originName);

        if (Files.exists(Paths.get(dirPath.toString(), fileName))) {
            // 同名则进行文件重命名
            AtomicInteger atomicInteger = map.computeIfAbsent(fileName, key -> {
                String tempName = originName;
                int num = 1;
                do {
                    tempName = String.format("%s (%s).%s", mainName, num++, extName);
                } while (Files.exists(Paths.get(dirPath.toString(), tempName)));
                return new AtomicInteger(num);
            });
            fileName = String.format("%s (%s).%s", mainName, atomicInteger.getAndIncrement(), extName);

            System.out.println(atomicInteger.getClass().getName() + "@" + Integer.toHexString(atomicInteger.hashCode()));

            log.info("存储目录下已经存在同名文件【{}】，重命名文件为【{}】", dirPath, originName, fileName);
        }

        System.out.println(map);
        return fileName;
    }

    public static void testComputeIfPresent() throws InterruptedException {
        String[] keys = {"1", "1", "1", "1", "1", "1", "1", "1"};
        final List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < keys.length; i++) {
            int index = i;
            final Thread thread = new Thread(() -> {
                final AtomicInteger result = getValue(keys[index]);
                final int andIncrement = result.getAndIncrement();
                System.out.println(keys[index] + "-" + andIncrement);
                System.out.println(result.getClass().getName() + "@" + Integer.toHexString(result.hashCode()));
            });
            threads.add(thread);
        }
        threads.forEach(Thread::start);
        for (int i = 0; i < threads.size(); i++) {
            final Thread thread = threads.get(i);
            thread.join();
        }
//        System.out.println(map);

    }


    private static AtomicInteger getValue(String key) {
        return map.computeIfAbsent(key, k -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return new AtomicInteger(1);
        });
    }


    public static String getExtName(String fileName) {
        if (fileName == null) {
            return null;
        } else {
            int extIndex = fileName.lastIndexOf(".");
            if (extIndex > 0 && extIndex + 1 < fileName.length()) {
                String ext = fileName.substring(extIndex + 1).toLowerCase();
                return ext;
            } else {
                return null;
            }
        }
    }

    public static String getMainName(String fileName) {
        if (fileName == null) {
            return null;
        } else {
            int extIndex = fileName.lastIndexOf(".");
            return extIndex > 0 && extIndex + 1 <= fileName.length() ? fileName.substring(0, extIndex) : fileName;
        }
    }
}
