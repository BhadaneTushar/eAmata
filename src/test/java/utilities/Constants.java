package utilities;

import java.util.HashMap;
import java.util.Map;

public class Constants {
    public static final Map<String, String> COUNTRY_ZIP_PATTERNS = new HashMap<>();

    static {
        COUNTRY_ZIP_PATTERNS.put("US", "^\\d{5}(-\\d{4})?$");
        COUNTRY_ZIP_PATTERNS.put("CA", "^[A-Z]\\d[A-Z] \\d[A-Z]\\d$");
        COUNTRY_ZIP_PATTERNS.put("UK", "^[A-Z]{1,2}\\d[A-Z\\d]? ?\\d[A-Z]{2}$");
        COUNTRY_ZIP_PATTERNS.put("AU", "^\\d{4}$");
        COUNTRY_ZIP_PATTERNS.put("IN", "^\\d{6}$");
    }

    public static final String DEFAULT_COUNTRY = "US";
}
