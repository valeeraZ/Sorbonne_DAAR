import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * Created by Wenzhuo Zhao on 02/10/2021.
 * Deterministic finite automaton
 */
public class DFA {
    // 256 ASCII chars
    private final static int COL = 256;

    // relatively, root is the start state of an automate,
    // end is the accepting state of an automate
    private final DFAState root;

    public DFA(DFAState root){
        this.root = root;
    }

    /**
     * from NFA to DFA
     * @param nfa a NFA
     * @return a DFA, without epsilon transition
     */
    public static DFA fromNFAtoDFA(NFA nfa){
        Set<Integer> inputSymbols = nfa.getInputSymbols();
        NFAState root = nfa.getRoot();

        DFAState start = new DFAState(root.epsilonClosure());

        // use HashSet to add only new DFAStates
        Set<DFAState> allDFAStates = new HashSet<>();
        allDFAStates.add(start);

        Queue<DFAState> queue = new LinkedList<>();
        queue.offer(start);

        while (!queue.isEmpty()){
            DFAState current = queue.poll();
            for (Integer input: inputSymbols) {
                Set<NFAState> set = getNextSubStates(current, input);
                if(!set.isEmpty()){
                    DFAState next = new DFAState(set);
                    if (!allDFAStates.add(next)){
                        for(DFAState same: allDFAStates){
                            if (same.equals(next)){
                                current.addTransition(input, same);
                                break;
                            }
                        }
                        DFAState.counter --;
                    }else {
                        queue.offer(next);
                        current.addTransition(input, next);
                    }
                }
            }
        }

        return new DFA(start);
    }

    private static Set<NFAState> getNextSubStates(DFAState state, int input){
        Set<NFAState> subset = new HashSet<>();
        Set<NFAState> tmp = NFAState.move(input, state.getSubset());
        for (NFAState nfaState: tmp) {
            subset.addAll(nfaState.epsilonClosure());
        }

        return subset;
    }

    @Override
    public String toString() {
        return root.toString();
    }
}
