import java.util.*;

/**
 * Created by Wenzhuo Zhao on 28/09/2021.
 */
public class NFAState {
    public static int counter = 0;

    private final int id;

    // input symbols and next states
    private final Map<Integer, Set<NFAState>> transitions;

    // non input symbol (epsilon), only next states
    private final Set<NFAState> epsilonTransitions;

    public NFAState(){
        id = counter++;
        transitions = new HashMap<>();
        epsilonTransitions = new HashSet<>();
    }

    public Map<Integer, Set<NFAState>> getTransitions() {
        return transitions;
    }

    public Set<NFAState> getEpsilonTransitions() {
        return epsilonTransitions;
    }

    public void addTransition(int input){
        addTransition(input, new NFAState());
    }

    public void addTransition(int input, NFAState next){
        Set<NFAState> states = this.transitions.get(input);
        if (states == null){
            states = new HashSet<>();
        }
        states.add(next);
        this.transitions.put(input, states);
    }

    // add an epsilon transition - no input, only the next state
    public void addTransition(NFAState next){
        this.epsilonTransitions.add(next);
    }

    // get next state(s) by the input symbol
    public Set<NFAState> getTransition(int input){
        return this.transitions.get(input);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NFAState state = (NFAState) o;
        return id == state.id && Objects.equals(transitions, state.transitions) && Objects.equals(epsilonTransitions, state.epsilonTransitions);
    }
    @Override
    public String toString(){
        return print(new HashSet<NFAState>());
    }

    public String print(HashSet<NFAState> visited) {
        if (!visited.add(this))
            return null;
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, Set<NFAState>> entry: transitions.entrySet()) {
            for (NFAState state : entry.getValue()) {
                // (char): convert input symbol to ascii char
                sb.append(this.id).append(" -- ").append((char)entry.getKey().intValue()).append(" --> ").append(state.id);
                sb.append("\n");
            }
            for (NFAState state: entry.getValue()) {
                String seq = state.print(visited);
                if(seq != null){
                    sb.append(seq);
                }
            }
        }

        // and epsilon transitions
        for(NFAState state : epsilonTransitions){
            sb.append(this.id).append(" -- ").append("Ɛ").append(" --> ").append(state.id);
            sb.append("\n");
        }
        for (NFAState state: epsilonTransitions) {
            String seq = state.print(visited);
            if(seq != null){
                sb.append(seq);
            }
        }

        return sb.toString();
    }
}
