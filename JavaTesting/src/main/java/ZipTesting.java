import lombok.SneakyThrows;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipTesting {
    public List<String> unzip(InputStream in) {
        List<String> fileNameList = new ArrayList<>();
        try (ZipInputStream zis = new ZipInputStream(in)) {
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                String name = processZipEntry(zipEntry);
                fileNameList.add(name);
                zipEntry = zis.getNextEntry();
            }
            return fileNameList;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> unzipThreaded(InputStream in, int threads) {
        List<String> fileNameList = Collections.synchronizedList(new ArrayList<>());
        ThreadPoolExecutor threadPoolExecutor =
                (ThreadPoolExecutor) Executors.newFixedThreadPool(threads);
        try (ZipInputStream zis = new ZipInputStream(in)) {
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                ZipEntry finalZipEntry = zipEntry;
                threadPoolExecutor.execute(() -> {
                    String name = processZipEntry(finalZipEntry);
                    fileNameList.add(name);
                });
                zipEntry = zis.getNextEntry();
            }
//            while (!threadPoolExecutor.getQueue().isEmpty()) {
//                Thread.sleep(1000);
//                System.out.println(threadPoolExecutor.getQueue().size());
//            }
            threadPoolExecutor.shutdown();
            threadPoolExecutor.awaitTermination(60, TimeUnit.SECONDS);
            return fileNameList;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private String processZipEntry(ZipEntry zipEntry) {
        String name = zipEntry.getName();
        delay();
        return name;
    }

    @SneakyThrows
    private void delay() {
        Thread.sleep(3);
    }


}
