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

    private final Set<DFAState> acceptings;

    public DFA(DFAState root, Set<DFAState> acceptings){
        this.root = root;
        this.acceptings = acceptings;
    }

    /**
     * from NFA to DFA
     * @param nfa a NFA
     * @return a DFA, without epsilon transition
     */
    public static DFA fromNFAtoDFA(NFA nfa){
        Set<Integer> inputSymbols = nfa.getInputSymbols();
        NFAState root = nfa.getRoot();
        NFAState accepting = nfa.getAccepting();

        DFAState start = new DFAState(root.epsilonClosure());
        Set<DFAState> accepts = new HashSet<>();

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
                                // cycle transition
                                current.addTransition(input, same);
                                break;
                            }
                        }
                        DFAState.counter --;
                    }else {
                        // check if the `next` DFAState is an accepting state
                        // which means it contains the accepting NFAState of the NFA
                        if (next.getSubset().contains(accepting)){
                            accepts.add(next);
                        }
                        queue.offer(next);
                        current.addTransition(input, next);
                    }
                }
            }
        }
        
        // divide the state in 2 parts
        // one for all ends and one for the others
        Queue<Set<DFAState>> worklist = new LinkedList<>();
        Set<DFAState> union1 = new HashSet<>();
        for (DFAState state : allDFAStates){
            if (!accepts.contains(state)){
                union1.add(state);
            }
        }
        // add two parts in worklist
        worklist.add(union1);
        worklist.add(accepts);
        Set<DFAState> min_state = new HashSet<>();
        while (!worklist.isEmpty()){
            Set<DFAState> current = worklist.poll();
            // find if their next step still in the union
            Set<DFAState> union2 = new HashSet<>();
            for (Integer input : inputSymbols){
                Map<DFAState,Set<NFAState>> map = new HashMap<>();
                Set<NFAState> in_union = new HashSet<>();
                for(DFAState st : current){
                    map.put(st,getNextSubStates(st,input));
                    in_union.addAll(st.getSubset());
                }
                // once found, divide the unoin
                for(DFAState st : current){
                    if (!in_union.containsAll(map.get(st))){
                        union2.add(st);
                        current.remove(st);
                    }
                }
            }
            // add the union into worklist
            if (union2.size() > 1){
                worklist.add(union2);
            }
            if (union2.size() == 1){
                min_state.addAll(union2);
            }
            // now for all state in current
            // their next step is in the union
            // so we can consider they are the same state
            if (current.size() == 1){
                min_state.addAll(union2);
            }else{
                if (current.size() == 0){
                    break;
                }
                // change the transitions for each state
                for (DFAState state : allDFAStates){
                    Map<Integer,Set<DFAState>> map = state.getTransitions();
                    for(Integer input : inputSymbols){
                        Set<DFAState> set = map.get(input);
                        for (DFAState state1 : set){
                            if (current.contains(state1)){
                                set.remove(state1);
                                set.add(current.iterator().next());
                            }
                        }
                    }
                }
                //
                Set<DFAState> res = new HashSet<>();
                res.add(current.iterator().next());
                // add the state into final
                min_state.addAll(res);
            }
        }

        return new DFA(start, accepts);
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
