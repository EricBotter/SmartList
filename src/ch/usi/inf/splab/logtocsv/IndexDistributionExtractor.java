package ch.usi.inf.splab.logtocsv;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class IndexDistributionExtractor {

    private static class ImpactPerAction {
        int insert = 0, remove = 0, get = 0, set = 0, maxlistsize = 0;
    }

    public static void main(String[] args) throws IOException {
        String pathToCsv = args.length > 0 ? args[0] : "log.txt";
        System.out.println("Analyzing " + pathToCsv);
        Scanner sc = new Scanner(new File(pathToCsv));

        ArrayList<LogLine> al = new ArrayList<>(100000);

        int j = 0;
        while (sc.hasNextLine()) {
            LogLine line = new LogLine(sc.nextLine());
            al.add(line);
            if (++j % 10000 == 0) {
                System.out.print("\rParsed log line " + j + "...   ");
            }
        }
        System.out.println("\rParsed log line " + j + ". Done");

        al.sort((o1, o2) -> Integer.compare(o1.id, o2.id));
        System.out.println("Sorted.");
        ImpactPerAction[] impactArray = new ImpactPerAction[al.get(al.size()-1).id + 1];
        for (int i = 0; i < impactArray.length; i++) {
            impactArray[i] = new ImpactPerAction();
        }

        FileWriter detail = new FileWriter(pathToCsv+"detail.csv");

        detail.write("id,listsize,action,index,impact\n");

        j = 0;
        for (LogLine l : al) {
            ImpactPerAction ipa = impactArray[l.id];
            ipa.maxlistsize = Math.max(ipa.maxlistsize, l.sizeBefore);

            if (l.params.size() == 0) continue;

            String action = l.params.keySet().iterator().next();
            int index = l.params.values().iterator().next();
            int impact;
            switch (action) {
                case "Insert":
                    impact = l.sizeBefore - index + 1;
                    break;
                case "Remove":
                    impact = l.sizeBefore - index;
                    break;
                case "Get":
                case "Set":
                    impact = 1;
                    break;
                default:
                    continue;
            }
            switch (action) {
                case "Insert":
                    ipa.insert += impact;
                    break;
                case "Remove":
                    ipa.remove += impact;
                    break;
                case "Get":
                    ipa.get += impact;
                    break;
                case "Set":
                    ipa.set += impact;
            }

            String sb = String.valueOf(l.id) +
                    ',' +
                    l.sizeBefore +
                    ',' +
                    action +
                    ',' +
                    index +
                    ',' +
                    impact +
                    '\n';
            detail.write(sb);
            if (++j % 10000 == 0) {
                System.out.print("\rAnalyzed log line " + j + "...   ");
            }
        }

        detail.close();
        System.out.println("\rAnalyzed log line " + j + ". Done");


        FileWriter summary = new FileWriter(pathToCsv+"summary.csv");
        summary.write("id,maxlistsize,insertimpact,removeimpact,getimpact,setimpact\n");
        for (int i = 0; i < impactArray.length; i++) {
            ImpactPerAction ipa = impactArray[i];

            String sb = String.valueOf(i) +
                    ',' +
                    ipa.maxlistsize +
                    ',' +
                    ipa.insert +
                    ',' +
                    ipa.remove +
                    ',' +
                    ipa.get +
                    ',' +
                    ipa.set +
                    '\n';
            summary.write(sb);

            if (i % 10000 == 0) {
                System.out.print("\rSummarized list no. " + j + "...   ");
            }
        }
        System.out.print("\rSummarized list no. " + j + ". Done   ");

        summary.close();
    }
}
