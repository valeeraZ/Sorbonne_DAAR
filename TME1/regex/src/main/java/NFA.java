import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Wenzhuo Zhao on 28/09/2021.
 * Nondeterministic finite automaton
 */
public class NFA {
    // 256 ASCII chars
    private final static int COL = 256;

    // relatively, root is the start state of an automate,
    // end is the accepting state of an automate
    private final NFAState root;
    private final NFAState accepting;

    public NFA(NFAState root, NFAState accepting){
        this.root = root;
        this.accepting = accepting;
    }

    public NFAState getRoot() {
        return root;
    }

    public NFAState getAccepting() {
        return accepting;
    }

    public static NFA fromRegExTreeToNFA(RegExTree ret){
        if(ret.subTrees.isEmpty()){
            NFAState start_state = new NFAState();
            NFAState final_state = new NFAState();
            if (ret.root != NodeEnum.DOT){
                // only 1 transition
                start_state.addTransition(ret.root, final_state);
            }else {
                for (int i = 0; i < COL; i++) {
                    // all the 256 transitions
                    start_state.addTransition(i, final_state);
                }
            }
            return new NFA(start_state, final_state);
        }

        if(ret.root == NodeEnum.CONCAT){
            // from left's end to right's start
            NFA left = fromRegExTreeToNFA(ret.subTrees.get(0));
            NFA right = fromRegExTreeToNFA(ret.subTrees.get(1));
            left.accepting.addTransition(right.root);
            return new NFA(left.root, right.accepting);
        }

        if (ret.root == NodeEnum.ALTERN){
            // from a new state to left's start and right's start
            // and connect the ends of left and right to end state
            NFAState start_state = new NFAState();
            NFA left = fromRegExTreeToNFA(ret.subTrees.get(0));
            NFA right = fromRegExTreeToNFA(ret.subTrees.get(1));
            NFAState end_state = new NFAState();

            start_state.addTransition(left.root);
            start_state.addTransition(right.root);
            left.accepting.addTransition(end_state);
            right.accepting.addTransition(end_state);

            return new NFA(start_state, end_state);
        }

        if (ret.root == NodeEnum.ETOILE){
            // see fig 10.31:
            NFAState start_state = new NFAState();
            NFA left = fromRegExTreeToNFA(ret.subTrees.get(0));
            NFAState end_state = new NFAState();

            start_state.addTransition(left.root);
            start_state.addTransition(end_state);
            left.accepting.addTransition(left.root);
            left.accepting.addTransition(end_state);

            return new NFA(start_state, end_state);
        }

        return new NFA(new NFAState(), new NFAState());
    }

    /**
     * get all the input symbols in this automaton
     * @return a set of Integer representing input symbol
     */
    public Set<Integer> getInputSymbols(){
        return getInputSymbols(this.root, new HashSet<>());
    }

    private Set<Integer> getInputSymbols(NFAState state, HashSet<NFAState> visited){
        // cycle
        if (!visited.add(state))
            return new HashSet<>();

        Set<Integer> res = new HashSet<>(state.getTransitions().keySet());

        for (NFAState nextState: state.getEpsilonTransitions()){
            res.addAll(getInputSymbols(nextState, visited));
        }

        for (Set<NFAState> states: state.getTransitions().values()){
            for (NFAState nextState: states) {
                res.addAll(getInputSymbols(nextState, visited));
            }
        }
        return res;
    }

    @Override
    public String toString() {
        return root.toString();
    }
}
