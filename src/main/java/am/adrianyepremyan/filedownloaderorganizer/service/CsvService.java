package am.adrianyepremyan.filedownloaderorganizer.service;

import static am.adrianyepremyan.filedownloaderorganizer.constant.Headers.ASSET_ID;
import static am.adrianyepremyan.filedownloaderorganizer.constant.Headers.STATUS;
import static am.adrianyepremyan.filedownloaderorganizer.constant.Headers.TYPE;
import static am.adrianyepremyan.filedownloaderorganizer.constant.Headers.URL;
import static am.adrianyepremyan.filedownloaderorganizer.constant.Headers.USERNAME;

import am.adrianyepremyan.filedownloaderorganizer.domain.ModerationRecord;
import java.util.List;
import java.util.function.Predicate;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

@Service
public record CsvService(CSVParser moderationCsvParser,
                         CSVParser moderationMarketplaceCsvParser) {

    public List<ModerationRecord> getModerationRecordStream(Predicate<CSVRecord> predicate) {
        return getModerationRecordStream(moderationCsvParser, predicate);
    }

    public List<ModerationRecord> getModerationMarketplaceRecordStream(Predicate<CSVRecord> predicate) {
        return getModerationRecordStream(moderationMarketplaceCsvParser, predicate);
    }

    private List<ModerationRecord> getModerationRecordStream(CSVParser csvParser, Predicate<CSVRecord> predicate) {
        return csvParser.stream()
            .filter(predicate)
            .map(csvRecord -> new ModerationRecord(
                    csvRecord.get(USERNAME),
                    csvRecord.get(URL),
                    csvRecord.get(ASSET_ID),
                    csvRecord.get(STATUS),
                    csvRecord.isMapped(TYPE) ? csvRecord.get(TYPE) : null
                )
            )
            .toList();
    }
}
