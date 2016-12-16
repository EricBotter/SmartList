package ch.usi.inf.splab.logtocsv;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Scanner;

public class IndexDistributionExtractor {

    private static DecimalFormat df = new DecimalFormat("         0");

    private static class ImpactPerAction {
        int insert = 0, remove = 0, get = 0, set = 0, other = 0, maxlistsize = 0;
    }

    public static void main(String[] args) throws IOException {
        String pathToCsv = args.length > 0 ? args[0] : "log.txt";
        System.out.println("Analyzing " + pathToCsv);
        Scanner sc = new Scanner(new File(pathToCsv));

        FileWriter detail = new FileWriter(pathToCsv+"detail.csv");
        detail.write("id,listsize,action,index,impact\n");

        HashMap<Integer, ImpactPerAction> impactArray = new HashMap<>(10000000);
        HashMap<Integer, ImpactPerAction> impactList = new HashMap<>(10000000);

        int j = 0;
        long start = System.nanoTime();
        while (sc.hasNextLine()) {
            LogLine l = new LogLine(sc.nextLine());

            ImpactPerAction ipaA, ipaL;
            if (impactArray.containsKey(l.id)) {
                ipaA = impactArray.get(l.id);
                ipaL = impactList.get(l.id);
            } else {
                ipaA = new ImpactPerAction();
                impactArray.put(l.id, ipaA);
                ipaL = new ImpactPerAction();
                impactList.put(l.id, ipaL);
            }

            ipaA.maxlistsize = Math.max(ipaA.maxlistsize, l.sizeBefore);
            int impactA = l.impactArray, impactL = l.impactList;
            String action = "";

            switch (l.method) {
                case "indexOf":
                case "get":
                case "Itr$next":
                case "ListItr$previous":
                    action = "Get";
                    ipaA.get += impactA;
                    ipaL.get += impactL;
                    break;
                case "set":
                case "ListItr$set":
                    action = "Set";
                    ipaA.set += impactA;
                    ipaL.set += impactL;
                    break;
                case "add":
                case "addAll":
                case "ListItr$add":
                    action = "Insert";
                    ipaA.insert += impactA;
                    ipaL.insert += impactL;
                    break;
                case "remove":
                case "removeRange":
                case "retainAll":
                case "removeAll":
                case "Itr$remove":
                    action = "Remove";
                    ipaA.remove += impactA;
                    ipaL.remove += impactL;
                    break;
                case "clear":
                case "subList":
                case "<init>":
                    action = "Other";
                    ipaA.other += impactA;
                    ipaL.other += impactL;
            }
            String sb = String.valueOf(l.id) +
                    ',' +
                    l.sizeBefore +
                    ',' +
                    action +
                    ',' +
                    impactA +
                    ',' +
                    impactL +
                    '\n';
            detail.write(sb);
            if (++j % 10000 == 0) {
                long end = System.nanoTime();
                System.out.print("\rParsed log line " + j + "... (" + df.format(10e14/(end-start)) + " lines/s)         ");
                start = end;
            }
        }

        detail.close();
        System.out.println("\rAnalyzed log line " + j + ". Done                         ");


        FileWriter summary = new FileWriter(pathToCsv+"summary.csv");
        summary.write("id,maxlistsize,insertarray,removearray,getarray,setarray,otherarray,insertlist,removelist,getlist,setlist,otherlist\n");
        for (int i = 0; i < impactArray.size(); i++) {
            ImpactPerAction ipaA = impactArray.get(i);
            ImpactPerAction ipaL = impactList.get(i);

            String sb = String.valueOf(i) +
                    ',' +
                    ipaA.maxlistsize +
                    ',' +
                    ipaA.insert +
                    ',' +
                    ipaA.remove +
                    ',' +
                    ipaA.get +
                    ',' +
                    ipaA.set +
                    ',' +
                    ipaA.other +
                    ',' +
                    ipaL.insert +
                    ',' +
                    ipaL.remove +
                    ',' +
                    ipaL.get +
                    ',' +
                    ipaL.set +
                    ',' +
                    ipaL.other +
                    '\n';
            summary.write(sb);

            if (i % 100000 == 0) {
                System.out.print("\rSummarized list no. " + j + "...   ");
            }
        }
        summary.close();

        System.out.println("\rSummarized list no. " + j + ". Done   ");
    }
}
