package regex;

import java.io.*;
import java.util.Set;

import static regex.RegEx.parse;

public class Main {
    static DFAState root;
    static Set<DFAState> acceptings;

    public static boolean search(DFAState state, String line, int position) {
        if (acceptings.contains(state))
            return true;

        if (position >= line.length())
            return false;

        int input = line.charAt(position);

        DFAState next = state.getTransition(input);

        if (next == null)
            return search(root, line, position + 1);

        if (!search(next, line, position + 1))
            return search(root, line, position + 1);

        return true;
    }

    public static String readToString(File file) {
        long filelength = file.length();
        byte[] filecontent = new byte[(int) filelength];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(filecontent);
    }


    public static void main(String[] args) throws IOException {
        String regEx, fileName;
        RegExTree ret;
        File file;

        if (args.length < 2) {
            System.out.println("usage : <RegEx pattern> <filename>");
            return;
        }
        regEx = args[0];
        fileName = args[1];
        System.out.println("  >> regEx : " + regEx);
        System.out.println("  >> file name : " + fileName);

        file = new File(fileName);

        long startTime = System.currentTimeMillis();

        if (regEx.length() < 1) {
            System.err.println("  >> ERROR: empty regEx.");
            return;
        } else {
            try {
                ret = parse(regEx);
            } catch (Exception e) {
                System.err.println("  >> ERROR: syntax error for regEx \"" + regEx + "\".");
                e.printStackTrace();
                return;
            }
        }

        assert ret != null;
        NFA nfa = NFA.fromRegExTreeToNFA(ret);
        DFA dfa = DFA.fromNFAtoDFA(nfa);
        root = dfa.getRoot();
        acceptings = dfa.getAcceptings();

        int lineNumber = 0;
        int resultNumber = 0;
        StringBuilder sb = new StringBuilder();
        String text = readToString(file);
        String[] lines = text.split("\\n");

        for (String line: lines) {
            lineNumber++;
            if (search(root, line, 0)) {
                sb.append(lineNumber).append(" - ").append(line).append("\n");
                resultNumber++;
            }
        }
        System.out.println(sb.toString());
        System.out.println(resultNumber + " lines matched found");
        long searchEndTime = System.currentTimeMillis();
        System.out.println("Time Used： " + (searchEndTime - startTime) + "ms");

    }
}
