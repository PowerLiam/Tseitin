import javafx.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    static int seencount = 0;
    // Control Vars.
    static String start = "S";
    static String end   = "bbbbaaabb";//bbbbaaabb
    static int max_len = end.length() * 100; // Maximum length of string to process.
    static int max_depth = 100; // Maximum recursive depth
    static boolean oneWay = true; // True if substitutions only go one way.
    // End Control Vars

    static List<Pair<String, String>> subs;
    static List<String> seen;

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        File input = new File(in.nextLine());
        subs = new ArrayList<>();
        seen = new ArrayList<>();

        try {
            Scanner fscan = new Scanner(input);
            while(fscan.hasNextLine()) {
                String[] arr;
                arr = fscan.nextLine().split(" = ");
                if (arr[1].equals("|e|")) {
                    arr[1] = "";
                }
                subs.add(new Pair<>(arr[0], arr[1]));
                if (!oneWay) {
                    subs.add(new Pair<>(arr[1], arr[0]));
                }
            }
            List<String> genUnderMaxDepth = new ArrayList<>();
            /*for (int i = 0; i < subs.size(); i++) {
                System.out.println(("Sub: " + subs.get(i).getKey() + " to " + subs.get(i).getValue()));
            }*/
            transform(start, new ArrayList<>(), genUnderMaxDepth, 0);
            for (String s : genUnderMaxDepth) {
                //Ignore ones that have nonfinal stuff.
                if (!s.toLowerCase().equals(s)) {
                  continue;
                }
                System.out.println(s);
            }
            System.out.println(("Strings are not equivalent with size constraint: " + max_len));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    private static void transform(String start, List<Pair<String, Pair<String, String>>> applied, List<String> genUnderMaxDepth, int depth) {
        //System.out.println(start);
        seencount++;
        if (seencount % 10000 == 0) {
            System.out.println("Processed: " + seencount);
        }
        seen.add(start);
        List<Pair<String, Pair<String, String>>> my_applied = new ArrayList<>(applied);
        if (start.equals(end)) {
            for (Pair<String, Pair<String, String>> p : my_applied) {
                System.out.println(p.getKey() + ": " + p.getValue().getKey() + "=" + p.getValue().getValue());
            }
            System.out.println(start);
            System.exit(0);
        }
        if (start.length() > max_len || lowerLen(start) > end.length()) {
            //System.out.println(("String " + start + " || Lower len " + lowerLen(start)));
            return;
        }
        if (depth > max_depth) {
            return;
        }
        genUnderMaxDepth.add(start);
        for (int i = 0; i < start.length(); i++) {
            for (int j = 0; j < subs.size(); j++) {
                String s = subs.get(j).getKey();
                if (i + s.length() <= start.length()) {
                    if (start.substring(i, i + s.length()).equals(s)) {
                        //System.out.println(("Replacing " + s + " with " + subs.get(j).getValue() + " at index " + i + " in " + start));
                        String sub = subs.get(j).getValue();
                        String next = start.substring(0, i) + sub + start.substring(i + s.length());
                        if(seen.contains(next) || next.length() > max_len) {
                            continue;
                        }
                        my_applied.add(new Pair<>(start, new Pair<>(s, sub)));
                        transform(next, my_applied, genUnderMaxDepth,depth + 1);
                    }
                }
            }
        }
    }
    private static int lowerLen(String s) {
        int len = 0;
        for (Character c: s.toCharArray()) {
            if (c.toString().equals(c.toString().toLowerCase())) {
                len += 1;
            }
        }
        return len;
    }
}

