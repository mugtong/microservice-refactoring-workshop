package app.anne.util;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

public class Qr {
    public static String generateQrCode(Object... args) {
        String value = createQrValue(args);
        return encrypt(value);
    }

    private static String createQrValue(Object[] args) {
        StringBuilder sb = new StringBuilder();
        for (Object arg : args) {
            sb.append(arg.toString()).append(":");
        }
        long epochSecond = Instant.now().getEpochSecond();
        sb.append(epochSecond);
        return sb.toString();
    }

    private static String encrypt(String originalString) {
        return Hashing.sha256()
                .hashString(originalString, StandardCharsets.UTF_8)
                .toString();
    }
}
