package tech.yojigen.util;

import java.util.UUID;

public class YUUID {
    public static String get() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
