package am.adrianyepremyan.filedownloaderorganizer.util;

import am.adrianyepremyan.filedownloaderorganizer.domain.ModerationRecord;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtils {

    public static String moderationFilename(ModerationRecord record) {
        return record.assetId();
    }

    public static String moderationDir(ModerationRecord record) {
        return record.username();
    }

    public static String moderationMarketplaceDir(ModerationRecord record) {
        return record.username() + "/" + record.type();
    }
}
