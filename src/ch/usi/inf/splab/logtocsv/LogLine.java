package ch.usi.inf.splab.logtocsv;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

class LogLine {
    final int id;
    final int sizeBefore;
    final HashMap<String, Integer> params;
    final String method;
    final List<String> methodArgs;

    LogLine(String line) {
        String squares = line.substring(1, line.indexOf(']'));
        String[] elements = squares.split(Pattern.quote(","));

        id = Integer.valueOf(elements[0]);
        sizeBefore = Integer.valueOf(elements[1]);
        params = new HashMap<>(elements.length-2, 1);

        for (int i = 2; i < elements.length; i++) {
            String key = elements[i].substring(0, elements[i].indexOf(':'));
            String value = elements[i].substring(elements[i].indexOf(':') + 1);
            int intval;
            try {
                intval = Integer.parseInt(value);
            } catch (NumberFormatException x) {
                continue;
            }
            params.put(key, intval);
        }

        line = line.substring(line.indexOf(']') + 1);
        method = line.substring(0, line.indexOf('('));
        line = line.substring(line.indexOf('(') + 1, line.lastIndexOf(')'));
        elements = line.split(Pattern.quote(","));
        methodArgs = Arrays.asList(elements);
    }
}
