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
    final int impactArray;
    final int impactList;
    final String allocationSite;
    final String method;
    final List<String> methodArgs;

    LogLine(String line) {
        String squares = line.substring(1, line.indexOf(']'));
        String[] elements = squares.split(Pattern.quote(","));

        id = Integer.valueOf(elements[0]);
        sizeBefore = Integer.valueOf(elements[1]);
        impactArray = Integer.valueOf(elements[2]);
        impactList = Integer.valueOf(elements[3]);
        if (elements.length == 5)
            allocationSite = elements[4];
        else
            allocationSite = "";

        line = line.substring(line.indexOf(']') + 1);
        method = line.substring(0, line.indexOf('('));
        line = line.substring(line.indexOf('(') + 1, line.lastIndexOf(')'));
        elements = line.split(Pattern.quote(","));
        methodArgs = Arrays.asList(elements);
    }
}
