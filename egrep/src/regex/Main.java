package regex;

import java.io.*;
import java.util.Set;

import static regex.RegEx.parse;

public class Main {
    static DFAState root;
    static Set<DFAState> acceptings;

    public static boolean search(DFAState state, String line, int position){
        if (position >= line.length())
            return false;

        int input = line.charAt(position);

        if (acceptings.contains(state))
            return true;
        if (state.getTransition(input) == null)
            return search(root, line, position+1);

        for (DFAState next: state.getTransition(input)) {
            if (!search(next, line, position+1))
                return search(root, line, position+1);
        }
        return true;
    }


    public static void main(String[] args) throws IOException {
        String regEx, fileName;
        RegExTree ret;
        File file;

        if(args.length < 2) {
            System.out.println("usage : <RegEx pattern> <filename>");
            return;
        }
        regEx = args[0];
        fileName = args[1];
        System.out.println("  >> regEx : " + regEx);
        System.out.println("  >> file name : " + fileName);

        file = new File(fileName);

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

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        int lineNumber = 0;
        int resultNumber = 0;
        while ( (line = reader.readLine()) != null){
            if (!line.isEmpty()){
                if (search(root, line, 0)){
                    System.out.println(lineNumber + " - " + line);
                    resultNumber ++;
                }
            }
            lineNumber ++;
        }
        System.out.println(resultNumber + " lines matched found");

    }
}
