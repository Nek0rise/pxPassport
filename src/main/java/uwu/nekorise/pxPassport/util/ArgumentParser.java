package uwu.nekorise.pxPassport.util;

import java.util.ArrayList;
import java.util.List;

public final class ArgumentParser {

    public static List<String> parse(String[] args, int start) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean quoted = false;

        for (int i = start; i < args.length; i++) {
            String arg = args[i];

            if (!quoted) {
                if (arg.startsWith("\"")) {
                    quoted = true;
                    current.append(arg.substring(1));
                    if (arg.endsWith("\"") && arg.length() > 1) {
                        current.setLength(current.length() - 1);
                        result.add(current.toString());
                        current.setLength(0);
                        quoted = false;
                    }
                } else {
                    result.add(arg);
                }
            } else {
                current.append(" ").append(arg);
                if (arg.endsWith("\"")) {
                    current.setLength(current.length() - 1);
                    result.add(current.toString());
                    current.setLength(0);
                    quoted = false;
                }
            }
        }

        if (current.length() > 0) {
            result.add(current.toString());
        }

        return result;
    }

    public static int logicalSize(String[] args, int start) {
        return parse(args, start).size();
    }

    public static int logicalIndexForTab(String[] args, int start) {
        boolean quoted = false;
        int logicalCount = 0;

        for (int i = start; i < args.length; i++) {
            String arg = args[i];

            if (!quoted) {
                if (arg.startsWith("\"") && !arg.endsWith("\"")) {
                    quoted = true;
                } else {
                    logicalCount++;
                }
            } else {
                if (arg.endsWith("\"")) {
                    quoted = false;
                    logicalCount++;
                }
            }
        }

        return quoted ? logicalCount : logicalCount;
    }

    public static boolean isInsideQuotes(String[] args, int start) {
        boolean quoted = false;

        for (int i = start; i < args.length; i++) {
            String arg = args[i];
            if (arg.startsWith("\"") && !arg.endsWith("\"")) quoted = true;
            else if (quoted && arg.endsWith("\"")) quoted = false;
        }

        return quoted;
    }
}