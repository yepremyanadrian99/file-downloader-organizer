package am.adrianyepremyan.filedownloaderorganizer.service;

import static am.adrianyepremyan.filedownloaderorganizer.constant.Headers.ASSET_ID;
import static am.adrianyepremyan.filedownloaderorganizer.constant.Headers.STATUS;
import static am.adrianyepremyan.filedownloaderorganizer.constant.Headers.URL;
import static am.adrianyepremyan.filedownloaderorganizer.constant.Headers.USERNAME;

import am.adrianyepremyan.filedownloaderorganizer.domain.ModerationRecord;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

@Service
public record CsvService(CSVParser csvParser) {

    public Stream<ModerationRecord> getRecordStream(Predicate<CSVRecord> predicate) {
        return csvParser.stream()
            .filter(predicate)
            .map(csvRecord -> new ModerationRecord(
                    csvRecord.get(USERNAME),
                    csvRecord.get(URL),
                    csvRecord.get(ASSET_ID),
                    csvRecord.get(STATUS)
                )
            );
    }
}
