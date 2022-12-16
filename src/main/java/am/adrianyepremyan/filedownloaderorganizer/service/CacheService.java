package am.adrianyepremyan.filedownloaderorganizer.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class CacheService {

    private static final Set<String> assetIdCache = new HashSet<>();

    private static FileWriter writer;

    private final String cacheFilePath;

    @SneakyThrows
    public CacheService(@Value("${cache.file.path}") String cacheFilePath) {
        this.cacheFilePath = cacheFilePath;
        final var file = ensureCacheFileExists();
        writer = new FileWriter(file, true);
        updateCache();
    }

    public void updateCache() throws IOException {
        final var fis = new FileInputStream(cacheFilePath);
        final var reader = new BufferedReader(new InputStreamReader(fis));
        reader.lines().forEach(assetId -> {
            System.out.println(assetId + " was already cached");
            assetIdCache.add(assetId);
        });
    }

    public void clearCache() {
        assetIdCache.clear();
    }

    public void cache(String assetId) throws IOException {
        assetIdCache.add(assetId);
        writer.write(assetId);
    }

    public boolean exists(String assetId) {
        return assetIdCache.contains(assetId);
    }

    @Scheduled(fixedRate = 5000)
    public void printCache() {
        System.out.print("Cache: [");
        assetIdCache.forEach(assetId -> System.out.print(assetId + ","));
        System.out.println("]");
    }

    private File ensureCacheFileExists() throws IOException {
        final var file = new File(cacheFilePath);
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }
}
