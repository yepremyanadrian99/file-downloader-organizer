package am.adrianyepremyan.filedownloaderorganizer.service;

import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class CacheService {

    private static final Set<String> assetIdCache = new HashSet<>();

    private final FileService fileService;
    private final String rootPath;

    public CacheService(FileService fileService,
                        @Value("${rootPath}") String rootPath) {
        this.fileService = fileService;
        this.rootPath = rootPath;
        updateCache();
    }

    @Scheduled(fixedRate = 5000)
    public void printCache() {
        System.out.printf("Cache size: [%d]\n", assetIdCache.size());
    }

    public void updateCache() {
        fileService.getFilesUnderDirStream(rootPath, 5)
            .map(filename -> filename.substring(0, filename.lastIndexOf('.')))
            .forEach(assetIdCache::add);
    }

    public void cache(String assetId) {
        assetIdCache.add(assetId);
    }

    public boolean exists(String assetId) {
        return assetIdCache.contains(assetId);
    }
}
