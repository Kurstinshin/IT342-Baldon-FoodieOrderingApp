package edu.cit.baldon.foodieorderingapp.shared;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * Utility methods shared across all feature slices.
 */
public final class SliceUtils {

    private static final DateTimeFormatter FORMATTER =
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").withZone(ZoneOffset.UTC);

    private SliceUtils() {}

    public static String timestamp() {
        return FORMATTER.format(Instant.now());
    }

    public static <T> ApiResponse<T> ok(T data, String message) {
        return ApiResponse.ok(data, message, timestamp());
    }

    public static <T> ApiResponse<T> fail(String message, String code, String detail) {
        return ApiResponse.fail(message, new ApiError(code, message, detail), timestamp());
    }

    public static <T> ApiResponse<T> notFound(String resource) {
        return fail(resource + " not found", "404", resource + " does not exist");
    }
}
