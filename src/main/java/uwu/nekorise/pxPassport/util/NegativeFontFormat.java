package uwu.nekorise.pxPassport.util;

import uwu.nekorise.pxPassport.config.NegativeCharsConfig;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NegativeFontFormat {

    private static final Pattern FONT_PATTERN = Pattern.compile("<font:[^>]+>(.*?)</font>", Pattern.DOTALL);

    private static final Pattern ANGLE_TAG_PATTERN = Pattern.compile("<[^>]+>");

    public static String format(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        Map<String, String> negativeChars =
                NegativeCharsConfig.getNegativeChars();

        Matcher matcher = FONT_PATTERN.matcher(input);
        StringBuilder result = new StringBuilder();

        int lastEnd = 0;

        while (matcher.find()) {
            result.append(input, lastEnd, matcher.start());

            String fullBlock = matcher.group(0);
            String innerText = matcher.group(1);

            result.append(fullBlock);

            String textForCalculation = ANGLE_TAG_PATTERN.matcher(innerText).replaceAll("");

            StringBuilder suffix = new StringBuilder();

            textForCalculation.codePoints().forEach(codePoint -> {
                String ch = new String(Character.toChars(codePoint));
                String mapped = negativeChars.get(ch);

                if (mapped != null && !mapped.isEmpty()) {
                    suffix.append(mapped);
                }
            });

            result.append(suffix);

            lastEnd = matcher.end();
        }

        result.append(input.substring(lastEnd));

        return result.toString();
    }
}
