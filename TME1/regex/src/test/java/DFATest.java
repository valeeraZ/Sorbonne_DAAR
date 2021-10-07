import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DFATest {

    private List<RegExTree> trees;

    /*@Before
    public void init(){
        String[] exprs = {"(a|b)*ab"};
        trees = Arrays.stream(exprs).map(RegEx::parse).collect(Collectors.toList());
    }*/

    @Test
    public void NFAtoDFATest(){
       /*trees.stream()
            .map(NFA::fromRegExTreeToNFA)
            .map(DFA::fromNFAtoDFA).forEach(System.out::println);*/
        System.out.println(DFA.fromNFAtoDFA(NFA.fromRegExTreeToNFA(RegEx.parse("(a|b)*ab"))));
    }
}
/*
before min
A: {2, 1, 4, 7, 0, 8} -- a --> B: {6, 2, 3, 9, 1, 10, 4, 7, 8}
B: {6, 2, 3, 9, 1, 10, 4, 7, 8} -- a --> B: {6, 2, 3, 9, 1, 10, 4, 7, 8}
B: {6, 2, 3, 9, 1, 10, 4, 7, 8} -- b --> D: {6, 2, 1, 4, 7, 5, 8, 11}
D: {6, 2, 1, 4, 7, 5, 8, 11} -- a --> B: {6, 2, 3, 9, 1, 10, 4, 7, 8}
D: {6, 2, 1, 4, 7, 5, 8, 11} -- b --> C: {6, 2, 1, 4, 7, 5, 8}
C: {6, 2, 1, 4, 7, 5, 8} -- a --> B: {6, 2, 3, 9, 1, 10, 4, 7, 8}
C: {6, 2, 1, 4, 7, 5, 8} -- b --> C: {6, 2, 1, 4, 7, 5, 8}
A: {2, 1, 4, 7, 0, 8} -- b --> C: {6, 2, 1, 4, 7, 5, 8}
after min
A: {2, 1, 4, 7, 0, 8} -- a --> B: {6, 2, 3, 9, 1, 10, 4, 7, 8}
B: {6, 2, 3, 9, 1, 10, 4, 7, 8} -- a --> B: {6, 2, 3, 9, 1, 10, 4, 7, 8}
B: {6, 2, 3, 9, 1, 10, 4, 7, 8} -- b --> D: {6, 2, 1, 4, 7, 5, 8, 11}
D: {6, 2, 1, 4, 7, 5, 8, 11} -- a --> B: {6, 2, 3, 9, 1, 10, 4, 7, 8}
D: {6, 2, 1, 4, 7, 5, 8, 11} -- b --> A: {2, 1, 4, 7, 0, 8}
A: {2, 1, 4, 7, 0, 8} -- b --> A: {2, 1, 4, 7, 0, 8}
*/
