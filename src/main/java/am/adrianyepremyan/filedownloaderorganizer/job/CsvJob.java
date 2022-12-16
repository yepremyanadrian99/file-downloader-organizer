package am.adrianyepremyan.filedownloaderorganizer.job;

import static am.adrianyepremyan.filedownloaderorganizer.constant.Headers.STATUS;
import static am.adrianyepremyan.filedownloaderorganizer.constant.Headers.URL;
import static am.adrianyepremyan.filedownloaderorganizer.constant.Headers.USERNAME;

import am.adrianyepremyan.filedownloaderorganizer.ProgressBarUtil;
import am.adrianyepremyan.filedownloaderorganizer.constant.Status;
import am.adrianyepremyan.filedownloaderorganizer.domain.ModerationRecord;
import am.adrianyepremyan.filedownloaderorganizer.service.CsvService;
import am.adrianyepremyan.filedownloaderorganizer.service.FileService;
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

    private static final Predicate<CSVRecord> predicate = record ->
        StringUtils.isNotBlank(record.get(USERNAME))
            && StringUtils.isNotBlank(record.get(URL))
            && StringUtils.isNotBlank(record.get(STATUS));

    private final CsvService csvService;
    private final FileService fileService;
    private final RestTemplate restTemplate;

    @SneakyThrows
    @Override
    public void run(String... args) {
        final var records = csvService.getRecordStream(predicate)
            .filter(record -> record.status() == Status.ACCEPTED)
            .toList();

        ModerationRecord record;
        for (int i = 0; i < records.size(); ++i) {
            record = records.get(i);
            System.out.println("Downloading " + record.url() + " of user " + record.username());
            final var bytes = restTemplate.getForObject(record.url(), byte[].class);
            final var ext = record.url().substring(record.url().lastIndexOf("."));
            fileService.writeIntoFile(record.username(), record.assetId(), ext, bytes);
            ProgressBarUtil.print(records.size(), i + 1);
        }
    }
}
