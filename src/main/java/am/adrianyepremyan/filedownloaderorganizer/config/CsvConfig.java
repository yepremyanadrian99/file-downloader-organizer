package am.adrianyepremyan.filedownloaderorganizer.config;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CsvConfig {

    @Value("${csv.file.path}")
    private String filePath;

    @Bean
    public CSVFormat moderationCsvFormat() {
        return CSVFormat.Builder.create(CSVFormat.DEFAULT)
            .setHeader("pkg", "username", "user id", "url", "asset id", "people on image", "status")
            .setSkipHeaderRecord(true)
            .setIgnoreHeaderCase(true)
            .build();
    }

    @Bean
    public CSVFormat moderationMarketplaceCsvFormat() {
        return CSVFormat.Builder.create(CSVFormat.DEFAULT)
            .setHeader("pkg", "username", "user id", "asset id / url", "people on image", "status",
                "rejection reason 1", "rejection reason 2", "rejection reason 3", "moderation date", "moderator",
                "\"moderator comment\"", "manager comment", "mp", "type", "upload date", "asset id", "url")
            .setSkipHeaderRecord(true)
            .setIgnoreHeaderCase(true)
            .build();
    }

    @Bean
    public FileReader fileReader() throws FileNotFoundException {
        return new FileReader(filePath);
    }

    @Bean
    public CSVParser moderationCsvParser(FileReader fileReader,
                                         CSVFormat moderationCsvFormat) throws IOException {
        return CSVParser.parse(fileReader, moderationCsvFormat);
    }

    @Bean
    public CSVParser moderationMarketplaceCsvParser(FileReader fileReader,
                                                    CSVFormat moderationMarketplaceCsvFormat) throws IOException {
        return CSVParser.parse(fileReader, moderationMarketplaceCsvFormat);
    }
}
