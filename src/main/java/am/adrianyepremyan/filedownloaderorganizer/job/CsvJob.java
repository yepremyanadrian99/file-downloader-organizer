package am.adrianyepremyan.filedownloaderorganizer.job;

import static am.adrianyepremyan.filedownloaderorganizer.constant.Headers.ASSET_ID;
import static am.adrianyepremyan.filedownloaderorganizer.constant.Headers.STATUS;
import static am.adrianyepremyan.filedownloaderorganizer.constant.Headers.URL;
import static am.adrianyepremyan.filedownloaderorganizer.constant.Headers.USERNAME;

import am.adrianyepremyan.filedownloaderorganizer.constant.Status;
import am.adrianyepremyan.filedownloaderorganizer.domain.ModerationRecord;
import am.adrianyepremyan.filedownloaderorganizer.service.CacheService;
import am.adrianyepremyan.filedownloaderorganizer.service.CsvService;
import am.adrianyepremyan.filedownloaderorganizer.service.FileService;
import am.adrianyepremyan.filedownloaderorganizer.util.ProgressBarUtil;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class CsvJob implements CommandLineRunner {

    private final CsvService csvService;
    private final FileService fileService;
    private final RestTemplate restTemplate;
    private final CacheService cacheService;

    @SneakyThrows
    @Override
    public void run(String... args) {
        final Predicate<CSVRecord> predicate = record ->
            StringUtils.isNotBlank(record.get(USERNAME))
                && StringUtils.isNotBlank(record.get(URL))
                && StringUtils.isNotBlank(record.get(STATUS))
                && Status.from(record.get(STATUS)) == Status.ACCEPTED
                && !cacheService.exists(record.get(ASSET_ID));

        final var records = csvService.getRecordStream(predicate);

        ModerationRecord record;
        byte[] bytes;
        String ext;
        for (int i = 0; i < records.size(); ++i) {
            record = records.get(i);
            System.out.println("Downloading " + record.url() + " of user " + record.username());
            bytes = restTemplate.getForObject(record.url(), byte[].class);
            ext = record.url().substring(record.url().lastIndexOf("."));
            fileService.writeIntoFile(record.username(), record.assetId(), ext, bytes);
            cacheService.cache(record.assetId());
            ProgressBarUtil.print(records.size(), i + 1);
        }
        System.out.println("Job has finished successfully!");
        System.exit(0);
    }
}
