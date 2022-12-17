package am.adrianyepremyan.filedownloaderorganizer.constant;

import static java.util.function.Function.identity;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Status {
    ACCEPTED("accepted"),
    REJECTED("rejected"),
    MANAGER_REVIEW("manager review"),
    TBD("tbd"),
    MARKETPLACE("marketplace");

    private static final Map<String, Status> cache = Arrays.stream(values())
        .collect(Collectors.toMap(status -> status.toString().toLowerCase(), identity()));

    private final String value;

    public static Status from(String source) {
        return cache.get(source.toLowerCase());
    }

    @Override
    public String toString() {
        return value;
    }
}
