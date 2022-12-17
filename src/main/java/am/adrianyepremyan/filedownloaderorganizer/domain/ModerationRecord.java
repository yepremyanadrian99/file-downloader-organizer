package am.adrianyepremyan.filedownloaderorganizer.domain;

import am.adrianyepremyan.filedownloaderorganizer.constant.Status;

public record ModerationRecord(String username, String url, String assetId, Status status, String type) {

    public ModerationRecord(String username, String url, String assetId, String status, String type) {
        this(username, url, assetId, Status.from(status), type);
    }
}
